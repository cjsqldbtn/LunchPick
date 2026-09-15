import { useState } from "react";
import axios from "axios";
import "../css/ReportPopup.css";

const ReportPopup = ({ onClose }) => {
    const [content, setContent] = useState("");
    const [image, setImage] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleFile = (e) => {
        const selectedImage = e.target.files[0];

		if (!selectedImage) {
	        setImage(null);
	        return;
	    }

		// 이미지인지 확인
	    if (!selectedImage.type.startsWith("image/")) {
	        alert("이미지 파일만 첨부할 수 있습니다.");
	        e.target.value = "";
	        return;
	    }

	    // 10MB 제한
	    if (selectedImage.size > 10 * 1024 * 1024) {
	        alert("이미지는 10MB 이하만 첨부할 수 있습니다.");
	        e.target.value = "";
	        return;
	    }

	    setImage(selectedImage);
    };

    const submitReport = async (e) => {
        e.preventDefault();

        // 공백만 입력한 것도 내용 없음으로 처리
        if (!content.trim() && !image) {
            alert("신고 내용 또는 이미지를 첨부해주세요.");
            return;
        }

        const formData = new FormData();
        formData.append("content", content.trim());

        if (image) {
            formData.append("image", image);
        }

		setLoading(true);

		await axios.post("/report", formData)
	    .then(res => {
	        window.Toastify({
	            text: "신고가 접수되었습니다.",
	            duration: 3000,
	            close: true,
	            gravity: "top",
	            position: "center",
	            style: {
	                background: "linear-gradient(to left, #F4A261, #ea580c)"
	            }
	        }).showToast();
	        onClose();
	    })
	    .catch(err => {
	        console.error("신고 등록 실패:", err);
	        alert("신고 접수에 실패했습니다.");
	    })
	    .finally(() => {
	        setLoading(false);
	    });
    };

    const canSubmit = content.trim().length > 0 || image !== null;

    return (
        <div className="report-overlay" onClick={onClose}>
            <div className="report-popup" onClick={(e) => e.stopPropagation()}>
				<button className="btn-close" type="button" onClick={onClose}>
				    ✕
                </button>
                <div className="report-header">
                    <span className="report-label">
                        REPORT
                    </span>
                    <h2>신고하기</h2>
                    <p>
                        문제가 있는 내용을 알려주세요.
                    </p>
                </div>
                <form className="report-form" onSubmit={submitReport}>
                    <div className="report-field">
                        <label>
                            신고 내용
                            <small>선택</small>
                        </label>
                        <textarea
                            value={content}
                            onChange={(e) =>
                                setContent(e.target.value)
                            }
                            placeholder="신고 사유를 입력해주세요."
                            maxLength={1000}
                        />
                        <span className="report-count">
                            {content.length} / 1000
                        </span>
                    </div>
                    <div className="report-field">
                        <label>
                            이미지 첨부
                            <small>선택</small>
                        </label>
                        <label className="report-file-box">
                            <input type="file" accept="image/*" onChange={handleFile}/>
                            {!image ? (
                                <>
                                    <span className="file-icon">
                                        ＋
                                    </span>
                                    <strong>
                                        이미지 첨부
                                    </strong>
                                    <p>
                                        신고에 도움이 되는 이미지를 첨부해주세요.
                                    </p>
                                </>
                            ) : (
                                <div className="selected-file">
                                    <span>📎</span>
                                    <div>
                                        <strong>
                                            {image.name}
                                        </strong>
                                        <small>
                                            {(image.size / 1024 / 1024)
                                                .toFixed(2)} MB
                                        </small>
                                    </div>
                                </div>
                            )}
                        </label>
                    </div>
                    <p className="report-guide">
                        신고 내용 또는 파일 중 하나 이상을
                        입력해주세요.
                    </p>
                    <button type="submit" className="report-submit" disabled={!canSubmit || loading}>
                        {loading ? "접수 중..." : "신고 접수하기"}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default ReportPopup;