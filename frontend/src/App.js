import React, {useState, createContext } from 'react';
import { Route, Routes } from 'react-router-dom';
//보통 div를 많이 쓰는데 React.Fragment도 가능하다. 임포트 필요. 그냥 <></> 빈태그도 쓴다.
import Home from './pages/Home';
import Logout from './pages/Logout';
import KakaoCallback from './pages/KakaoCallback';
import NaverCallback from './pages/NaverCallback';

export const AuthContext = createContext(null);

function App() {
	const [ isLogin, setIsLogin ] = useState(false);
	const [ memberId, setMemberId ] = useState('');
	const login = () => setIsLogin(true);
	const logout = () => setIsLogin(false);
		
	const authContextValues = {
		isLogin,
		login,
		logout
	};
	
    return (
		<AuthContext.Provider value={authContextValues}>
		    <Routes>
		        <Route path="/" element={<Home />} />
				<Route path="/logout" element={<Logout/>}/>
				<Route path="/LunchPick/naverlogin" element={<NaverCallback />} />
				<Route path="/LunchPick/kakaologin" element={<KakaoCallback/>} />
		    </Routes>
		</AuthContext.Provider>
    );
}

export default App;
