import React, { useContext, useState } from 'react';
import axios from 'axios';
import naverLogo from '../img/NAVER_login_Dark_KR_green_icon_H48.png';
import kakaoLogo from '../img/kakao.png';
import { AuthContext } from '../App';

const LoginPopup = ({ isOpen, onClose }) => {
    const { login } = useContext(AuthContext);
    const [ member, setMember ] = useState({ email: '', password: '' });
    const { email, password } = member;

    if (!isOpen) return null;

    // 네이버 로그인 버튼 클릭 (전체 페이지 이동)
    const naverLogin = () => {
		window.location.href = 'http://localhost:9090/LunchPick/member/naverLogin';
    };

    // 카카오 로그인 버튼 클릭 (전체 페이지 이동)
    const kakaoLogin = () => {
        window.location.href = 'http://localhost:9090/LunchPick/member/kakaoLogin';
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setMember({ ...member, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
		localStorage.removeItem('jwt');
        axios.post('/member/login', member)
            .then((res) => {
                if (res.status === 200) {
                    const jwts = res.headers.authorization;
                    if (jwts) {
                        localStorage.setItem('jwt', jwts.replace('Bearer ', ''));
                    }
                    alert('로그인 되었습니다.');
                    onClose();
                    login();
                }
            })
            .catch((err) => {
                console.error('로그인 실패! : ' + err);
                alert('로그인에 실패했습니다. 아이디와 비밀번호를 확인해 주세요.');
            });
    };

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

                <form className="login-form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="email">이메일</label>
                        <input type="text" id="email" name="email" value={email} placeholder="이메일을 입력하세요" onChange={handleChange} required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">비밀번호</label>
                        <input type="password" id="password" name="password" value={password} placeholder="비밀번호를 입력하세요" onChange={handleChange} required />
                    </div>

                    <button className="submit-btn">로그인</button>
                </form>

                <div className="modal-social">
                    <p>소셜 계정으로 로그인</p>
                    <div className="social-btns">
                        <img src={naverLogo} className="social" onClick={naverLogin} alt="naver" />
                        <img src={kakaoLogo} className="social" onClick={kakaoLogin} alt="kakao" />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default LoginPopup;