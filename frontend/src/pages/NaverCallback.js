import React, { useEffect, useContext } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import axios from 'axios';
import { AuthContext } from '../App';

const NaverCallback = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const { login, setMemberId } = useContext(AuthContext); 

    useEffect(() => {
        const code = searchParams.get('code');
        const state = searchParams.get('state');
       	axios.post('/member/naverLogin', null, { params: { code, state } })
            .then(res => {
				if(res.status==200){
					const jwts = res.headers.authorization;
	                if (jwts) localStorage.setItem('jwt', jwts.replace('Bearer ', ''));
					setMemberId(res.data);
	                login();
	                navigate('/');
                    window.Toastify({
                        text: '로그인되었습니다.',
                        duration: 3000,
                        newWindow: true,
                        close: true,
                        gravity: 'top',
                        position: 'center',
                        stopOnFocus: true,
                        style: {
                            background: 'linear-gradient(to left, #F4A261, #ea580c)',
                        }
                    }).showToast();
				}
            })
            .catch(err => {
                console.error('NaverCallBack 에러! ' + err);
                navigate('/');
            });
    }, []);

    return null;
};

export default NaverCallback; 