import React, { useState, useContext } from 'react';
import { AuthContext } from '../App';
import axios from 'axios';

const ActionBar = () => {
	const { isLogin } = useContext(AuthContext);
	const token = localStorage.getItem('jwt');
	
	const [chatKey, setChatKey] = useState('');
	const [nickName, setNickName] = useState('');
	
	// 채팅방 만들기 
	const createChat = () => {
		axios.post('/member/createChatKey', null, {	headers: { Authorization: `Bearer ${token}` }})
		.then(res => {
			if(res.status === 200) {
				console.log('(/createChatKey) res : ',res);
				setChatKey(res.data);
				
				if (navigator.clipboard) {
	                navigator.clipboard.writeText(res.data)
	                    .then(() => {
	                        alert(`채팅키가 생성되어 클립보드에 복사되었습니다!\n[키: ${res.data}]`);
	                    })
	                    .catch(err => {
	                        console.error('클립보드 복사 실패:', err);
	                        alert(`채팅키가 생성되었습니다: ${res.data}`);
	                    });
	            } else {
	                alert(`채팅키가 생성되었습니다: ${res.data}`);
	            }
			}
		})
		.catch(err => {
			console.error('채팅키 만들기 실패! : ', err);
            alert('채팅키 만들기 실패! 다시 시도해보세여!');
		});
	};
	
	// 입장 
	const enterChat = () => {
		if (!chatKey) {
	        alert("채팅 키를 입력해 주세요.");
	        return;
	    } if (!nickName) {
	        alert("닉네임을 입력해 주세요.");
	        return;
	    }
		
		alert(`[${nickName}] 님, 채팅방(${chatKey})으로 입장합니다!`);
	};
	
    return (
        <>
            <section class="action-toolbar">
                <div class="action-group primary-actions">
                    <button class="action-btn share-btn" type="button">
                        <span class="btn-icon">💬</span>
                        <span>카카오톡 공유</span>
                    </button>
                </div>
				{isLogin && (
				  <div className="action-group chat-actions">
				    <button className="action-btn" type="button">
				      <span className="btn-icon">➕</span>
				      <span onClick={createChat}>채팅 만들기</span>
				    </button>
				    <div className="input-badge-wrap">
				      <input type="text" className="action-input" placeholder="채팅 키 입력" value={chatKey} onChange={(e) => setChatKey(e.target.value)} />
				      <input type="text" className="action-input nick-input" placeholder="닉네임" value={nickName} onChange={(e) => setNickName(e.target.value)} />
				      <button className="action-btn join-btn" onClick={enterChat}>입장</button>
				    </div>
				  </div>
				)}
                <div class="action-group secondary-actions">
                    <button class="action-btn ai-btn" type="button">
                        <span class="btn-icon">🤖</span>
                        <span>AI 챗봇</span>
                    </button>
                    <button class="action-btn report-btn" type="button" title="신고하기">
                        <span class="btn-icon">🚨</span>
                        <span>신고</span>
                    </button>
                </div>
            </section>
        </>
    );
};

export default ActionBar;