import { useEffect, useContext, useState } from "react";
import { MapContext } from "../pages/Home";

const FilterCard = () => {
	const { panTo, getPlaceList } = useContext(MapContext);
	const [active, setActive] = useState("한성대");
	
	const handleWhere = (location, lat, lng) => {
		getPlaceList(location, 70000);
		setActive(location);
		panTo(lat, lng);
	}
	
	return (
        <section className="filter-card">
            <div className="filter-heading">
                <div className="filter-icon">⚙️</div>
                <div>
                    <h2>점심 조건 설정</h2>
                    <p>위치, 날씨, 예산에 맞춰 맞춤 메뉴를 필터링합니다.</p>
                </div>
            </div>
            <div className="filter-grid">
                <div className="filter-group">
                    <label>WHERE</label>
                    <div className="segmented">
                        <button
                            className={active == "한성대" ? "active" : ""}
                            type="button"
                            onClick={() => handleWhere("한성대", 37.5884, 127.0062)}
                        >
                            한성대입구
                        </button>
                        <button
                            className={active == "신촌" ? "active" : ""}
                            type="button"
                            onClick={() => handleWhere("신촌", 37.5552, 126.9374)}
                        >
                            신촌
                        </button>
                    </div>
                </div>
                <div class="filter-group">
                    <label>WEATHER</label>
                    <label className="switch">
                        <input type="checkbox" checked />
                        <span className="slider"></span>
                        <span className="switch-text">오늘 날씨(27° ☁) 반영</span>
                    </label>
                </div>
                <div className="filter-group price-group">
                    <label>BUDGET</label>
                    <div className="range-wrap">
                        <span>₩5,000</span>
                        <input type="range" min="5000" max="50000" value="20000" step="1000" />
                        <span>₩50,000</span>
                    </div>
                    <div className="price-result">
                        최대 예산: <strong>₩20,000</strong>
                    </div>
                </div>
            </div>
        </section>
	);
}

export default FilterCard;