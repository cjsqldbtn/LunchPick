import React, { useContext, useState } from 'react';
import axios from 'axios';
import naverLogo from '../img/NAVER_login_Dark_KR_green_icon_H48.png';
import kakaoLogo from '../img/kakao.png';
import { AuthContext } from '../App';

const LoginPopup = ({ isOpen, onClose, onJoin, onFindPassword }) => {
    const { login, setMemberId } = useContext(AuthContext);
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
                    if (jwts) localStorage.setItem('jwt', jwts.replace('Bearer ', ''));
                    window.Toastify({
                        text: '로그인되었습니다.',
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
					console.log('로그인 성공!',res);
					setMemberId(res.data);
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
        <div className="login-modal-overlay" onClick={onClose}>
			<div className="login-modal" onClick={(e) => e.stopPropagation()}>
                <button className="btn-close" type="button" onClick={onClose}>
                    ✕
                </button>
                <div className="login-header">
                    <div className="login-brand">
                        🍴
                    </div>
                    <span className="login-eyebrow">
                        WELCOME BACK
                    </span>
                    <h2>
                        오늘 점심도 같이 골라볼까요?
                    </h2>
                    <p>
                        로그인하고 친구들과 메뉴를 골라보세요.
                    </p>
                </div>
                <form className="login-form-new" onSubmit={handleSubmit}>
                    <div className="login-form-group">
                        <label htmlFor="email">
                            EMAIL
                        </label>
                        <input
                            type="text"
                            id="email"
                            name="email"
                            value={email}
                            placeholder="example@email.com"
                            onChange={handleChange}
                            autoComplete="email"
                            required
                        />
                    </div>
                    <div className="login-form-group">
                        <label htmlFor="password">
                            PASSWORD
                        </label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={password}
                            placeholder="비밀번호를 입력해주세요"
                            onChange={handleChange}
                            autoComplete="current-password"
                            required
                        />
						<div className="forgot-password">
					        <button
					            type="button"
					            onClick={onFindPassword}
					        >
					            비밀번호를 잊으셨나요?
					        </button>
					    </div>
                    </div>
                    <button className="login-submit" type="submit">
                        로그인
                        <span>→</span>
                    </button>
                </form>
                <div className="login-social">
                    <div className="login-social-title">
                        <span></span>
                        <p>소셜 계정으로 로그인 및 회원가입</p>
                        <span></span>
                    </div>
                    <div className="login-social-buttons">
                        <button type="button" className="login-social-btn" onClick={naverLogin}title="네이버 로그인">
                            <img src={naverLogo} alt="네이버 로그인"/>
                        </button>
                        <button type="button" className="login-social-btn" onClick={kakaoLogin} title="카카오 로그인">
                            <img src={kakaoLogo} alt="카카오 로그인"/>
                        </button>
                    </div>
                </div>
				<div className="login-footer">
				    <span>아직 회원이 아니신가요?</span>
				    <button type="button" onClick={onJoin}>
				        회원가입
				    </button>
				</div>
            </div>
        </div>
    );
};

export default LoginPopup;