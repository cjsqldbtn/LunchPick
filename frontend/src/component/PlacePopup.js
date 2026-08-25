import { useEffect, useState } from "react";
import "../css/placePopup.css";

const PlacePopup = ({ place, onClose }) => {
	const [selectedMenu, setSelectedMenu] = useState(null);
	
	const handleAddMenu = () => {
		// 메뉴 최신 PICKS에 넣고 추천++
	};
	
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
                            className={`menu-card ${selectedMenu?.menuId === menu.menuId ? "selected" : ""
                                }`}
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
                <footer className="popup-actions">
                    <button 
						className={selectedMenu ? "btn-action-pri" : "btn-action-sec"}
					    disabled={!selectedMenu}
					    onClick={handleAddMenu}
					>
                        점심 후보에 넣기
                    </button>
                </footer>
            </article>
        </div>
    );
};

export default PlacePopup;