import React, { useState, createContext, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import Header from '../component/Header';
import RecommendBanner from '../component/RecommendBanner';
import FilterCard from '../component/FilterCard';
import Map from '../component/Map'
import "../css/style.css";

export const MapContext = createContext(null);

const Home = () => {
    const [map, setMap] = useState(null);
	const [placeList, setPlaceList] = useState([]);
	const [markerList, setMarkerList] = useState([]);

    const panTo = (lat, lng) => {
        if (!map) return;

        const position = new window.kakao.maps.LatLng(lat, lng);

        map.panTo(position);
    };
    const getPlaceList = (type, price) => {
        axios.get(`/place/list?type=${type}&price=${price}`)
        .then(res => {
            //console.log(res.data);
            setPlaceList(res.data);
        })
        .catch(err => {
            console.error(err);
        });
    };
    const mapContextValues = {
        map,
        setMap,
        panTo,
		getPlaceList,
		placeList,
		setMarkerList,
		markerList
    };
	
	useEffect(() => {
		getPlaceList("한성대",70000);
	},[]);
		
	return (
        <MapContext.Provider value={mapContextValues}>
            <div className="app-shell">
                <Header />
                <main>
                    <RecommendBanner />
                    <FilterCard />
                    <div className="content-grid">
                        <div className="left-column">
                            <Map />
                        </div>
                        <aside className="recent-card">
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
                                        <strong>오늘의 AI 추천</strong>
                                        <small>새로운 추천 메뉴 보기</small>
                                    </div>
                                </li>
                            </ul>
                        </aside>
                    </div>
                </main>
                <footer>
                    <b>LunchPick</b>
                    <span>오늘의 점심을 더 쉽게. © 2026</span>
                </footer>
            </div>
        </MapContext.Provider>
	);
}

export default Home;