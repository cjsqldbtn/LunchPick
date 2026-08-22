import { useEffect, useContext } from "react";
import { MapContext } from "../pages/Home";

const FilterCard = () => {
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
                        <button className="active" type="button">한성대입구</button>
                        <button type="button">신촌</button>
                    </div>
                </div>
                <div class="filter-group">
                    <label>WEATHER</label>
                    <label class="switch">
                        <input type="checkbox" checked />
                        <span class="slider"></span>
                        <span class="switch-text">오늘 날씨(27° ☁) 반영</span>
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