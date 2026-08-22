import { useEffect, useContext } from "react";
import { MapContext } from "../pages/Home";

const RecommendBanner = () => {
    return (
        <section className="recommend-banner">
            <div>
                <p className="eyebrow">LUNCH CURATION</p>
                <h1>오늘 점심,<br /><strong>뭐가 당겨?</strong></h1>
                <p className="hero-copy">오늘의 날씨, 예산을 바탕으로 가장 먹고 싶은 한 끼를 찾아볼게요.</p>
            </div>
            <button className="refresh-btn" type="button">
                점심 추천받기 <span>↻</span>
            </button>
        </section>
    );
}

export default RecommendBanner;