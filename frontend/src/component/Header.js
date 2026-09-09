import React, { useState, useContext }from 'react';
import { useNavigate } from 'react-router-dom';
import LoginPopup from './LoginPopup';
import { AuthContext } from '../App';
import { WeatherContext } from "../pages/Home";

const Header = () => {
	const { weatherIcon, temperature } = useContext(WeatherContext);
	const navigate = useNavigate();
	
	const { isLogin, logout } = useContext(AuthContext);
	const [isLoginOpen, setIsLoginOpen] = useState(false);
	
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
                    <span className="brand-mark">LP</span>
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
			
			<LoginPopup isOpen={isLoginOpen} onClose={closeLoginBtn} />
		</>
	);
}

export default Header;