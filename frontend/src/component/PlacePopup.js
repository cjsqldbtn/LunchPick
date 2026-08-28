import { useEffect, useState, useContext } from "react";
import "../css/placePopup.css";
import axios from 'axios';
import { AuthContext } from '../App';

const PlacePopup = ({ place, menu, needMenu, onClose }) => {
	const { isLogin } = useContext(AuthContext);
	const [selectedMenu, setSelectedMenu] = useState(null);
	
	const handleAddMenu = () => {
		// 메뉴 최신 PICKS에 넣기, cnt++
	};
	
    useEffect(() => {
		let recommandMenu = null;
		
		// 초기값으로 menu가 들어오거나 룰렛으로 들어온 경우
		if(menu) {
			recommandMenu = menu;
		} else if(needMenu) {
			recommandMenu = place.menuList[Math.floor(Math.random()*place.menuList.length)];
		}
		if(!recommandMenu) return;
		
		setSelectedMenu(recommandMenu);
		
		// menuCnt++
		axios.put(`/menu/${recommandMenu.menuId}`)
		.then(res => {
			console.log("cnt++");
		})
		.catch(err => {
			console.error("menu cnt++ 실패:", err);
		});
    }, []);
	
    if (!place) return null;

    return (     
        <div className="place-popup-container">
            <div className="modal-backdrop" onClick={onClose}/>
            <article className="place-popup-card" onClick={(e) => e.stopPropagation()}>
                <button
                    className="btn-close"
                    aria-label="닫기"
                    onClick={onClose}
                >
                    ✕
                </button>

                <div className="place-hero"
                    style={{
                        backgroundImage: `url(${place.img})`
                    }}>
                    <div className="hero-overlay"></div>

                    <div className="hero-tags">
                        <span className="badge-fire">🔥 인기 급상승</span>
                        <span className="badge-ai">AI 추천 98%</span>
                    </div>

                    <div className="hero-photo-credit">
                        PHOTO · KAKAO PLACE
                    </div>
                </div>

                <div className="popup-body">
                    <header className="place-header">
                        <div className="title-row">
                            <h2>{place.placeName}</h2>
							<span className="category-chip">{place.category}</span>
                        </div>
                    </header>
                    {place.menuList.map((menu) => (
                        <div
                            key={menu.menuId}
                            className={`menu-card ${selectedMenu?.menuId === menu.menuId ? "selected" : ""}`}
                            onClick={() => setSelectedMenu(menu)}
                        >
                            <div className="menu-info">
                                <div className="menu-name">
                                    <strong>{menu.name}</strong>
                                </div>
                            </div>
                            <div className="menu-price">
                                {menu.price.toLocaleString()}원
                            </div>
                            {selectedMenu?.menuId === menu.menuId && (
                                <span className="menu-check">✓</span>
                            )}
                        </div>
                    ))}
                </div>
                {
                    isLogin ? <footer className="popup-actions">
                        <button
                            className={selectedMenu ? "btn-action-pri" : "btn-action-sec"}
                            disabled={!selectedMenu}
                            onClick={handleAddMenu}
                        >
                            점심 후보에 넣기
                        </button>
                    </footer> : null
                }
            </article>
        </div>
    );
};

export default PlacePopup;