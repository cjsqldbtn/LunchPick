import { useEffect, useState, useContext } from "react";
import "../css/placePopup.css";
import { AuthContext } from '../App';
import { HistoryContext } from "../pages/Home";

const PlacePopup = ({ place, menu, needMenu, onClose }) => {
	const { isLogin } = useContext(AuthContext);
	const { addHistory } = useContext(HistoryContext);
	const [selectedMenu, setSelectedMenu] = useState(null);
	
	const token = localStorage.getItem('jwt');
	
    const handleAddMenu = () => {
        if (!selectedMenu) return;

        addHistory(selectedMenu.menuId);
        onClose();
    };
	
    useEffect(() => {
		let recommandMenu = null;
		
		// 초기값으로 menu가 들어오거나 룰렛으로 들어온 경우
		if(menu) {
			recommandMenu = menu;
		} else if(needMenu) {
			// 메뉴 룰렛 (시간 남으면 효과 넣어주기)
			recommandMenu = place.menuList[Math.floor(Math.random()*place.menuList.length)];
		}
		
		// 둘 다 아닌 경우 초기 메뉴를 선택하지 않음
		if(!recommandMenu) return;
		
		setSelectedMenu(recommandMenu);
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
					<div className="menu-section-header">
					  <div>
					    <span className="menu-section-label">MENU</span>
					    <strong>오늘 뭐 먹지?</strong>
					  </div>
					  <span className="menu-section-count">
					    {place.menuList.length}가지
					  </span>
					</div>
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