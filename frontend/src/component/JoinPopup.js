import { useState } from "react";
import axios from "axios";
import "../css/JoinPopup.css";

const JoinPopup = ({ isOpen, onClose, onLogin }) => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [passwordCheck, setPasswordCheck] = useState("");

	if (!isOpen) return null;
	
    const join = async (e) => {
        e.preventDefault();
        if (!email.trim()) {
			window.Toastify({
                text: '이메일을 입력해주세요.',
                duration: 3000,
                newWindow: true,
                close: true,
                gravity: 'top',
                position: 'center',
                stopOnFocus: true,
                style: {
                    background: 'linear-gradient(to left, #F4A261, #ea580c)',
                }
            }).showToast();
            return;
        }
		if (password.length < 8) {
			window.Toastify({
                text: '비밀번호는 최소 8자리 이상이어야 합니다.',
                duration: 3000,
                newWindow: true,
                close: true,
                gravity: 'top',
                position: 'center',
                stopOnFocus: true,
                style: {
                    background: 'linear-gradient(to left, #F4A261, #ea580c)',
                }
            }).showToast();
	        return;
	    }
        if (password !== passwordCheck) {
			window.Toastify({
                text: '비밀번호가 일치하지 않습니다.',
                duration: 3000,
                newWindow: true,
                close: true,
                gravity: 'top',
                position: 'center',
                stopOnFocus: true,
                style: {
                    background: 'linear-gradient(to left, #F4A261, #ea580c)',
                }
            }).showToast();
            return;
        }
        try {
            await axios.post("/LunchPick/member/join", {
                email,
                password
            });
			window.Toastify({
                text: '회원가입 되었습니다.',
                duration: 3000,
                newWindow: true,
                close: true,
                gravity: 'top',
                position: 'center',
                stopOnFocus: true,
                style: {
                    background: 'linear-gradient(to left, #F4A261, #ea580c)',
                }
            }).showToast();
			// 로그인 열기
            onLogin();
        } catch (err) {
			if (err.response?.status === 409) {
				window.Toastify({
	                text: '이미 가입된 이메일입니다.',
	                duration: 3000,
	                newWindow: true,
	                close: true,
	                gravity: 'top',
	                position: 'center',
	                stopOnFocus: true,
	                style: {
	                    background: 'linear-gradient(to left, #F4A261, #ea580c)',
	                }
	            }).showToast();
	            return;
	        }
	        console.error("회원가입 실패:", err);
			window.Toastify({
                text: '회원가입에 실패했습니다.',
                duration: 3000,
                newWindow: true,
                close: true,
                gravity: 'top',
                position: 'center',
                stopOnFocus: true,
                style: {
                    background: 'linear-gradient(to left, #F4A261, #ea580c)',
                }
            }).showToast();
        }
    };

    return (
        <div className="join-modal-overlay" onClick={onClose}>
            <div className="join-modal" onClick={(e) => e.stopPropagation()}>
				<button className="btn-close" type="button" onClick={onClose}>
				    ✕
                </button>
                <div className="join-header">
                    <div className="join-brand">
                        🍴
                    </div>
                    <span className="join-eyebrow">
                        JOIN LUNCHPICK
                    </span>
                    <h2>같이 점심 고르러 가볼까요?</h2>
                    <p>
                        회원가입하고 친구들과 메뉴를 골라보세요.
                    </p>
                </div>
                <form className="join-form" onSubmit={join}>
                    <div className="join-form-group">
                        <label>EMAIL</label>
                        <input
                            type="email"
                            placeholder="example@email.com"
                            value={email}
                            onChange={(e) =>
                                setEmail(e.target.value)
                            }
                        />
                    </div>
                    <div className="join-form-group">
                        <label>PASSWORD</label>
                        <input
                            type="password"
                            placeholder="비밀번호를 입력해주세요"
                            value={password}
                            onChange={(e) =>
                                setPassword(e.target.value)
                            }
							minLength={8}
						    required
                        />
                    </div>
                    <div className="join-form-group">
                        <label>PASSWORD CHECK</label>
                        <input
                            type="password"
                            placeholder="비밀번호를 한 번 더 입력해주세요"
                            value={passwordCheck}
                            onChange={(e) =>
                                setPasswordCheck(e.target.value)
                            }
                        />
                        {passwordCheck && (
                            <small
                                className={
                                    password === passwordCheck
                                        ? "password-match"
                                        : "password-error"
                                }
                            >
                                {password === passwordCheck
                                    ? "비밀번호가 일치해요."
                                    : "비밀번호가 일치하지 않아요."
                                }
                            </small>
                        )}
                    </div>
                    <button type="submit" className="join-submit">
                        회원가입
                        <span>→</span>
                    </button>
                </form>
                <div className="join-footer">
                    <span>이미 회원이신가요?</span>
                    <button type="button" onClick={onLogin}>
                        로그인
                    </button>
                </div>
            </div>
        </div>
    );
};

export default JoinPopup;