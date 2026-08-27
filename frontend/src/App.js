import React, {useState, createContext } from 'react';
import { Route, Routes } from 'react-router-dom';
//보통 div를 많이 쓰는데 React.Fragment도 가능하다. 임포트 필요. 그냥 <></> 빈태그도 쓴다.
import Home from './pages/Home';

export const AuthContext = createContext(null);

function App() {
    return (
        <Routes>
            <Route path="/" element={<Home />} />
        </Routes>
    );
}

export default App;
