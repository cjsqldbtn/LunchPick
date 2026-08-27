import React, { useState }from 'react';
import LoginPopup from './LoginPopup';

const Header = () => {
	
	
	const [isLoginOpen, setIsLoginOpen] = useState(false);

	  
	  
	
	// 로그인 버튼.
	const login = () => {
		setIsLoginOpen(true);
	};
	// 로그인 닫힘 버튼.
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