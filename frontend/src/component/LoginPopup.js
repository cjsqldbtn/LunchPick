import React from 'react';

const LoginPopup = ({ isOpen, onClose }) => {
    if (!isOpen) return null;

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <button className="modal-close" type="button" onClick={onClose}>
                    &times;
                </button>

                <div className="modal-header">
                    <span className="brand-mark">N</span>
                    <h2>NOON MENU 로그인</h2>
                    <p>서비스 이용을 위해 로그인해 주세요.</p>
                </div>

                <form className="login-form" onSubmit={(e) => e.preventDefault()}>
                    <div className="form-group">
                        <label htmlFor="loginId">아이디</label>
                        <input type="text" id="loginId" placeholder="아이디를 입력하세요" required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="loginPw">비밀번호</label>
                        <input type="password" id="loginPw" placeholder="비밀번호를 입력하세요" required />
                    </div>

                    <button type="submit" className="submit-btn">로그인</button>
                </form>

                <div className="modal-social">
                    <p>소셜 계정으로 로그인</p>
                    <div className="social-btns">
                        <button className="social kakao" type="button">K</button>
                        <button className="social naver" type="button">N</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default LoginPopup;