import { useEffect, useContext, useState } from "react";
import { MapContext } from "../pages/Home";

const FilterCard = () => {
	const { moveMap, getPlaceList } = useContext(MapContext);
	const [active, setActive] = useState("한성대");
	const [weatherOn, setWeatherOn] = useState(false);
	const [budget, setBudget] = useState(50000);
	
	const handleWhere = (location) => {
		setActive(location);
		let lat;
		let lng;
		if(location==="한성대") {
			lat = 37.5884;
			lng = 127.0062;
		} else {
			lat = 37.5552;
			lng = 126.9374;
		}
		moveMap(lat, lng);
	}
	
	useEffect(() => {
		getPlaceList(active, budget);
    }, [active, budget]);
	
	useEffect(() => {
		// 장소 데이터 넘기고 서버에서 날씨 AI 다녀오기
    }, [weatherOn]);
	
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
                            onClick={() => handleWhere("한성대")}
                        >
                            한성대입구
                        </button>
                        <button
                            className={active == "신촌" ? "active" : ""}
                            type="button"
                            onClick={() => handleWhere("신촌")}
                        >
                            신촌
                        </button>
                    </div>
                </div>
                <div className="filter-group">
                    <label>WEATHER</label>
                    <label className="switch">
                        <input type="checkbox" checked={weatherOn} onChange={(e) => setWeatherOn(e.target.checked)}/>
                        <span className="slider"></span>
                        <span className="switch-text">오늘 날씨(27° ☁) 반영</span>
                    </label>
                </div>
                <div className="filter-group price-group">
                    <label>BUDGET</label>
                    <div className="range-wrap">
                        <span>₩5,000</span>
                        <input type="range" min="5000" max="50000" value={budget} step="1000" onChange={(e) => setBudget(Number(e.target.value))}/>
                        <span>₩50,000</span>
                    </div>
                    <div className="price-result">
                        최대 예산: <strong>₩{budget.toLocaleString()}</strong>
                    </div>
                </div>
            </div>
        </section>
	);
}

export default FilterCard;