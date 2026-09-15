import React, { useState } from 'react';
import axios from 'axios';
import "../css/PasswordFindPopup.css";

const PasswordFindPopup = ({ isOpen, onClose, onLogin }) => {
    const [email, setEmail] = useState('');
    const [loading, setLoading] = useState(false);
    const [sent, setSent] = useState(false);

    if (!isOpen) return null;

    // 비밀번호 재설정 메일 전송
    const handleSubmit = (e) => {
        e.preventDefault();
        if (!email.trim()) {
            alert('이메일을 입력해주세요.');
            return;
        }
        setLoading(true);
        axios.post('/member/password/reset', {
            email: email
        })
        .then(res => {
            setSent(true);
        })
        .catch(err => {
            console.error('비밀번호 재설정 메일 전송 실패 : ', err);
            alert(
                err.response?.data ||
                '메일 전송에 실패했습니다.'
            );
        })
        .finally(() => {
            setLoading(false);
        });
    };

    return (
        <div className="password-find-overlay" onClick={onClose}>
            <div className="password-find-modal" onClick={(e) => e.stopPropagation()}>
                <button className="btn-close" type="button" onClick={onClose}>
                    ✕
                </button>
                {!sent ? (
                    <>
                        <div className="password-find-header">
                            <div className="password-find-brand">
                                🔑
                            </div>
                            <span className="password-find-eyebrow">
                                RESET PASSWORD
                            </span>
                            <h2>
                                비밀번호를 잊으셨나요?
                            </h2>
                            <p>
                                가입할 때 사용한 이메일을 입력해주세요.<br />
                                비밀번호 변경 링크를 보내드릴게요.
                            </p>
                        </div>
                        <form className="password-find-form" onSubmit={handleSubmit}>
                            <div className="password-find-form-group">
                                <label htmlFor="resetEmail">
                                    EMAIL
                                </label>
                                <input
                                    type="email"
                                    id="resetEmail"
                                    value={email}
                                    placeholder="example@email.com"
                                    onChange={(e) =>
                                        setEmail(e.target.value)
                                    }
                                    autoComplete="email"
                                    required
                                />
                            </div>
                            <button
                                className="password-find-submit"
                                type="submit"
                                disabled={loading}
                            >
                                {loading ? '메일 보내는 중...' : '변경 링크 받기'}
                                {!loading && <span>→</span>}
                            </button>
                        </form>
                        <div className="password-find-footer">
                            <span>
                                비밀번호가 생각나셨나요?
                            </span>
                            <button type="button" onClick={onLogin}>
                                로그인
                            </button>
                        </div>
                    </>
                ) : (
                    <div className="password-find-success">
                        <div className="password-find-brand">
                            ✉️
                        </div>
                        <span className="password-find-eyebrow">
                            CHECK YOUR EMAIL
                        </span>
                        <h2>
                            메일을 확인해주세요
                        </h2>
                        <p>
                            <strong>{email}</strong>
                            <br />
                            비밀번호 변경 링크를 전송했습니다.
                        </p>

                        <div className="password-find-guide">
                            <span>⏱</span>
                            <p>
                                변경 링크는
                                <strong> 10분 동안</strong>
                                유효합니다.
                            </p>
                        </div>
                        <button className="password-find-submit" type="button" onClick={onClose}>
                            확인
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default PasswordFindPopup;