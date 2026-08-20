import React from 'react';
import { Link } from 'react-router-dom';

const Header = () => {
	return (
		<>
			<header class="header">
 				<a class="brand" href="#">
			       <span class="brand-mark">N</span>
			       NOON MENU
			     </a>
			     
			     <div class="header-date">
			       <span>TUE · AUG 04, 2026</span>
			       <span class="dot">•</span>
			       <span class="weather">27° ☁</span>
			     </div>
	
			     <div class="header-actions">
			       <button class="social kakao" type="button" aria-label="카카오로그인">K</button>
			       <button class="social naver" type="button" aria-label="네이버로그인">N</button>
			       <button class="header-btn" type="button">로그인</button>
			       <button class="header-btn" type="button">로그아웃</button>
			     </div>
			</header>
		</>
	);
}

export default Header;