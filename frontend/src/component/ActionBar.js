import React, { useState, useContext, useRef, useEffect } from 'react';
import { AuthContext } from '../App';
import axios from 'axios';

const ActionBar = () => {
	const { isLogin, memberId } = useContext(AuthContext);
	//console.log("현재 memberId:", memberId);
	const token = localStorage.getItem('jwt');
	
	const [chatKey, setChatKey] = useState('');
	const [nickName, setNickName] = useState('');
	const [isJoined, setIsJoined] = useState(false); // 체팅에 입장 됐는지. 
	const [messageInput, setMessageInput] = useState(''); // 입력창 텍스트
	
	const socketRef = useRef(null);
	
	// 채팅방 만들기 
	const createChat = () => {
		axios.post('/member/createChatKey', null, {	headers: { Authorization: `Bearer ${token}` }})
		.then(res => {
			if(res.status === 200) {
				//console.log('(/createChatKey) res : ',res);
				setChatKey(res.data);
				
				if (navigator.clipboard) {
	                navigator.clipboard.writeText(res.data)
	                    .then(() => {
							window.Toastify({
					            text: `채팅키가 생성되어 클립보드에 복사되었습니다!\n[키: ${res.data}]`,
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
	                    })
	                    .catch(err => {
	                        console.error('클립보드 복사 실패:', err);
							window.Toastify({
					            text: `채팅키가 생성되었습니다: ${res.data}`,
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
            window.Toastify({
                text: '채팅 키를 입력해 주세요.',
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
	        return;
        } if (!nickName) {
            window.Toastify({
                text: '닉네임을 입력해 주세요.',
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
	        return;
	    }
		
		// 이미 연결되어 있으면 중복 연결 방지
	    if (
	        socketRef.current &&
	        (socketRef.current.readyState === WebSocket.OPEN || socketRef.current.readyState === WebSocket.CONNECTING)
	    ) {
	        alert("이미 채팅방에 입장해 있습니다.");
	        return;
	    }
		
		const wsUrl = `ws://localhost:9090/LunchPick/broadcasting?roomKey=${chatKey}&memberId=${memberId}&nickName=${encodeURIComponent(nickName)}`;
		const ws = new WebSocket(wsUrl);
        socketRef.current = ws; // ref에 저장
		
		ws.onopen = () => {
            //console.log("WebSocket 연결 성공");
			setIsJoined(true);
        };
		
		// 서버로부터 메세지가 도착했을 때
        ws.onmessage = (e) => {
            const data = JSON.parse(e.data);
            //console.log("서버로부터 도착한 메시지: ", e.data);
			if (data.type === "LEAVE") {
		        window.Toastify({
		            text: data.message,
		            duration: 3000,
		            gravity: "bottom",
		            position: "center",
		        }).showToast();

		        return;
		    }
            handleReceiveMessage(data.senderId,data.senderNick,data.message);
        };
		
		// 서버로 부터 에러가 났을 떄.
        ws.onerror = (err) => {
            console.error("WebSocket 에러:", err); alert("채팅 서버 연결 실패!");
        };

		// 채팅방을 나갔을 떄. 
        ws.onclose = (e) => {
            //console.log("WebSocket 연결 종료");
            setIsJoined(false);
			socketRef.current = null;

			if (e.reason === "IS_NOT_EXIST_CHAT_KEY") {
                window.Toastify({
                    text: '존재하지 않거나 유효하지 않은 채팅 키입니다.',
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
	        } else if (e.reason === "EMPTY_ROOM_KEY") {
				window.Toastify({
                    text: '채팅 키가 비어있습니다.',
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
        };
	};
	
	// 채팅 보내기 
	const sendMessage = () => {
		if (!messageInput.trim()) { return; }
		
		if (socketRef.current && socketRef.current.readyState === WebSocket.OPEN) {
			const messageData = {
				type: "CHAT",
				message: messageInput 
	        };
			
			socketRef.current.send(JSON.stringify(messageData));
			//handleReceiveMessage(memberId ,nickName, messageInput);
			setMessageInput('');
		} else {
			alert("채팅방 연결이 끊어져 있습니다. 입장을 다시 시도해주세요.");
		}
	};
	const handleKeyDown = (e) => {
		if (e.nativeEvent.isComposing) {
	        return;
	    }
	    if (e.key === 'Enter') {
			e.preventDefault();
			sendMessage();
	    }
  	};
	
	// 채팅 나가기
	const leaveChat = () => {
	    if (socketRef.current) {
	        socketRef.current.close();
	        socketRef.current = null;
	    }
	    setIsJoined(false);
	};
	
	// ai 추천 받기 
	const recommendAI = () => {
		alert('ai추천받기.');
	}
	
	// 메시지를 받았을 떄.
	const handleReceiveMessage = (senderId, sender, message) => {
	    const isMe = Boolean(senderId && memberId && Number(senderId) === Number(memberId));
		
		//console.log(isMe);
	    window.Toastify({
	        text: `${sender}: ${message}`,
	        duration: 30000, 
	        close: true,
	        gravity: "bottom", // 아래에서 위로 쌓이게 연출
	        position: isMe ? "right" : "left", 
	        stopOnFocus: true,
	        style: {
	            background: isMe 
	                ? 'linear-gradient(to right, #F4A261, #ffe600)'
	                : 'linear-gradient(to right, #F4A261, #ea580c)',
	            borderRadius: '12px',
	            color: '#fff',
	            boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
	        }
	    }).showToast();
	};
	
	useEffect(() => {
	    return () => {
	        if (socketRef.current) {
	            socketRef.current.close();
	        }
	    };
	}, []);
	
    return (
        <>
            <section className="action-toolbar">
                <div className="action-group primary-actions">
                    <button className="action-btn share-btn" type="button">
                        <span className="btn-icon">💬</span>
                        <span>카카오톡 공유</span>
                    </button>
                </div>
				{(isLogin && !isJoined) && (
				  <div className="action-group chat-actions">
				    <button className="action-btn" type="button" onClick={createChat}>
				      <span className="btn-icon">➕</span>
				      <span>채팅 만들기</span>
				    </button>
				    <div className="input-badge-wrap">
				      <input type="text" className="action-input" placeholder="채팅 키 입력" value={chatKey} onChange={(e) => setChatKey(e.target.value)} />
				      <input type="text" className="action-input nick-input" placeholder="닉네임" value={nickName} onChange={(e) => setNickName(e.target.value)} />
				      <button className="action-btn join-btn" onClick={enterChat}>입장</button>
				    </div>
				  </div>
				)}
				{(isJoined) && (
					<button className="action-btn join-btn" onClick={leaveChat}>채팅방 나가기</button>
				)}
                <div className="action-group secondary-actions">
                    <button className="action-btn ai-btn" type="button">
                        <span className="btn-icon">🤖</span>
                        <span>AI 챗봇</span>
                    </button>
                    <button className="action-btn report-btn" type="button" title="신고하기">
                        <span className="btn-icon">🚨</span>
                        <span>신고</span>
                    </button>
                </div>
            </section>
			{isJoined &&(
	            <div className="chat-input-toolbar">
	                <input
	                    type="text"
	                    className="chat-message-input"
	                    placeholder="채팅 입력 창"
						value={messageInput}
						onChange={(e) => setMessageInput(e.target.value)}
						onKeyDown={handleKeyDown}
	                />
	                <button type="button" className="chat-send-btn" onClick={sendMessage}>보내기</button>
	                <button type="button" className="chat-ai-recommend-btn" onClick={recommendAI}>AI 추천 받기</button>
	            </div>
			)}
        </>
    );
};

export default ActionBar;