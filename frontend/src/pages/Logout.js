import React, { useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../App';

const Logout = () => {
	const navigate = useNavigate();
	const { logout } = useContext(AuthContext); // AuthContext에 빨대 꽂기 
	
	useEffect(() => {
		localStorage.removeItem('jwt');
		logout();
		navigate('/', {replace: true});
	}, []);
	
	return null; // 사용자에게 보여줄 화면이 없는 컴포넌트이르모, (로그아웃 로직만 실행하고 / login으러 redirect함.)
	
};

export default Logout;