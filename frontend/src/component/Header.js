import React, { useState, useContext }from 'react';
import { useNavigate } from 'react-router-dom';
import LoginPopup from './LoginPopup';
import JoinPopup from './JoinPopup';
import PasswordFindPopup from './PasswordFindPopup';
import { AuthContext } from '../App';
import { WeatherContext } from "../pages/Home";

const Header = () => {
	const { weatherIcon, temperature } = useContext(WeatherContext);
	const navigate = useNavigate();
	
	const { isLogin, logout } = useContext(AuthContext);
	const [isLoginOpen, setIsLoginOpen] = useState(false);
	const [isJoinOpen, setIsJoinOpen] = useState(false);
	const [isPasswordFindOpen, setIsPasswordFindOpen] = useState(false);
	
	// 오늘 날짜
	const today = new Date();
	const weekday = today.toLocaleDateString("en-US", {
	    weekday: "short"
	}).toUpperCase();
	const month = today.toLocaleDateString("en-US", {
	    month: "short"
	}).toUpperCase();
	const date = String(today.getDate()).padStart(2, "0");
	const year = today.getFullYear();
	const todayText = `${weekday} · ${month} ${date}, ${year}`;
	
	// 로그인 버튼.
	const loginBtn = () => {
		setIsLoginOpen(true);
	};
	// 로그인 닫힘 버튼.
	const closeLoginBtn = () => {
        setIsLoginOpen(false);
    };
	// 회원가입 버튼
	const signUpBtn = () => {
		setIsJoinOpen(true);
	};
	// 회원가입 닫힘 버튼.
	const closeSignUpBtn = () => {
        setIsJoinOpen(false);
    };
	
	// 전환
	// 회원가입 → 로그인
	const goToLogin = () => {
	    setIsJoinOpen(false);
	    setIsLoginOpen(true);
	};
	// 로그인 → 회원가입
	const goToJoin = () => {
	    setIsLoginOpen(false);
	    setIsJoinOpen(true);
	};
	const goToFindPassword = () => {
	    setIsLoginOpen(false);
	    setIsPasswordFindOpen(true);
	};
	
	const logoutBtn = () => {
		navigate('/logout');
		alert('로그아웃 되었습니다.');
	};
	
	return (
		<>
            <header className="header">
                <a className="brand" href="/">
                    <span className="brand-mark">🍴</span>
                    점심 뭐
                </a>
                <div className="header-date">
                    <span>{todayText}</span>
                    <span className="dot">•</span>
                    <span className="weather">{temperature}° {weatherIcon}</span>
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
			<LoginPopup isOpen={isLoginOpen} onClose={closeLoginBtn} onJoin={goToJoin} onFindPassword={goToFindPassword}/>
			<JoinPopup isOpen={isJoinOpen} onClose={closeSignUpBtn} onLogin={goToLogin}/>
			<PasswordFindPopup isOpen={isPasswordFindOpen} onClose={() => setIsPasswordFindOpen(false)}/>
		</>
	);
}

export default Header;