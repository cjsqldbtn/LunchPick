import React, { useState, useContext }from 'react';
import { useNavigate } from 'react-router-dom';
import LoginPopup from './LoginPopup';
import { AuthContext } from '../App';

const Header = () => {
	const navigate = useNavigate();
	
	const { isLogin, logout } = useContext(AuthContext);
	const [isLoginOpen, setIsLoginOpen] = useState(false);
	  
	
	// 로그인 버튼.
	const loginBtn = () => {
		setIsLoginOpen(true);
	};
	// 로그인 닫힘 버튼.
	const closeLoginBtn = () => {
        setIsLoginOpen(false);
    };
	
	const signUpBtn = () => {
		alert("회원가입!");
	};
	
	const logoutBtn = () => {
		navigate('/logout');
		alert('로그아웃 되었습니다.');
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
				{
					isLogin ? (
				        <button className="header-btn" type="button" onClick={logoutBtn}>로그아웃</button>
				    ) : (
				        <>
				            <button className="header-btn" type="button" onClick={loginBtn}>로그인</button>
				            <button className="header-btn" type="button" onClick={signUpBtn}>회원가입</button>
				        </>
				    )}
                </div>
            </header>
			
			<LoginPopup isOpen={isLoginOpen} onClose={closeLoginBtn} />
		</>
	);
}

export default Header;