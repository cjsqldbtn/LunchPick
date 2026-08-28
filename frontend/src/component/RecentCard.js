import { useEffect, useContext } from "react";
import { AuthContext } from '../App';

const RecentCard = () => {
	const { isLogin } = useContext(AuthContext);
	
    return (
        <aside className="recent-card">
			{
				isLogin ? (<>
                        <div className="card-title-row">
                            <div>
                                <p className="eyebrow">HISTORY</p>
                                <h2>RECENT PICKS</h2>
                            </div>
                        </div>
                        <ul className="history-list">
                            <li>
                                <div>
                                    <strong>돈우마미</strong>
                                    <small>일식 덮밥 · 9,000원</small>
                                </div>
                            </li>
                            <li>
                                <div>
                                    <strong>홍두깨칼국수</strong>
                                    <small>한식 칼국수 · 8,000원</small>
                                </div>
                            </li>
                            <li>
                                <div>
                                    <strong>홍두깨칼국수</strong>
                                    <small>한식 돌솥정식 · 10,000원</small>
                                </div>
                            </li>
                            <li>
                                <div className="food-icon">✨</div>
                                <div>
                                    <strong>AI 추천</strong>
                                    <small>새로운 추천 메뉴 보기</small>
                                </div>
                            </li>
                        </ul>
				</>) : (<>
                        <div className="card-title-row">
                            <div>
                                <p className="eyebrow">JOIN MEMBER</p>
                                <h2>PROVIDED FUNCTIONS</h2>
                            </div>
                        </div>
                        <ul className="history-list">
                            <li>
                                <div className="food-icon">✌️</div>
                                <div>
                                    <strong>친구들과 채팅</strong>
                                    <small>로그인한 친구들과 채팅</small>
                                </div>
                            </li>
                            <li>
                                <div className="food-icon">✨</div>
                                <div>
                                    <strong>AI 추천</strong>
                                    <small>채팅 내역을 기반으로 메뉴 추천</small>
                                </div>
                            </li>
                            <li>
                                <div className="food-icon">❤️</div>
                                <div>
                                    <strong>추천 기록 저장</strong>
                                    <small>메뉴 저장 기능 제공</small>
                                </div>
                            </li>
                        </ul>
				</>)
			}
        </aside>
    );
}

export default RecentCard;