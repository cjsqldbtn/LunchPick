import React, { useState }from 'react';
import LoginPopup from './LoginPopup';
import naverLogo from '../img/NAVER_login_Dark_KR_green_icon_H48.png';
import kakaoLogo from '../img/kakao.png';

const Header = () => {
	
	
	const [isLoginOpen, setIsLoginOpen] = useState(false);

	  
	  
	const naverLogin = () => {
		alert("네이버 로그인!");
	};
	
	const kakaoLogin = () => {
		alert("카카오 로그인!");
	};	
	
	const login = () => {
		alert("로그인!");
		setIsLoginOpen(true);
	};
	
	const closeLogin = () => {
        setIsLoginOpen(false);
    };
	
	const signUp = () => {
		alert("회원가입!");
	};
	
	const logout = () => {
		alert("로그아웃!");
	};
	return (
		<>
            <header className="header">
                <a className="brand" href="/">
                    <span className="brand-mark">N</span>
                    NOON MENU
                </a>

                <div className="header-date">
                    <span>TUE · AUG 04, 2026</span>
                    <span className="dot">•</span>
                    <span className="weather">27° ☁</span>
                </div>

                <div className="header-actions">
                    <img src={naverLogo} className="social" onClick={naverLogin}/>
                    <img src={kakaoLogo} className="social" onClick={kakaoLogin}/>
                    <button className="header-btn" type="button" onClick={login}>로그인</button>
                    <button className="header-btn" type="button" onClick={signUp}>회원가입</button>
                    <button className="header-btn" type="button" onClick={logout}>로그아웃</button>
                </div>
            </header>
			
			<LoginPopup isOpen={isLoginOpen} onClose={closeLogin} />
		</>
	);
}

export default Header;