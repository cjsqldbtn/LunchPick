import React, { useEffect, useContext } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import axios from 'axios';
import { AuthContext } from '../App';

const NaverCallback = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const { login } = useContext(AuthContext); 

    useEffect(() => {
        const code = searchParams.get('code');
        const state = searchParams.get('state');
       	axios.post('/member/naverLogin', null, { params: { code, state } })
            .then(res => {
                const jwts = res.headers.authorization;
                if (jwts) localStorage.setItem('jwt', jwts.replace('Bearer ', ''));
                login();
                navigate('/');
            })
            .catch(err => {
                console.error('NaverCallBack 에러! ' + err);
                navigate('/');
            });
    }, []);

    return null;
};

export default NaverCallback; 