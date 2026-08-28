import React, { useState, createContext, useEffect, useRef } from 'react';
import axios from 'axios';
import Header from '../component/Header';
import RecommendBanner from '../component/RecommendBanner';
import FilterCard from '../component/FilterCard';
import Map from '../component/Map'
import ActionBar from '../component/ActionBar'
import PlacePopup from "../component/PlacePopup";
import RecentCard from "../component/RecentCard";
import "../css/style.css";

export const MapContext = createContext(null);

const Home = () => {
    const [map, setMap] = useState(null);
	const [placeList, setPlaceList] = useState([]);
	const [markerList, setMarkerList] = useState([]);
	const [selectedMarker, setSelectedMarker] = useState(null);
	const [selectedPlace, setSelectedPlace] = useState(null);
	const [needMenu, setNeedMenu] = useState(false);
	
    const moveMap = (lat, lng) => {
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
	const handleSelectedPlace = async (place) => {
		//alert(place.placeId);
		await axios.get(`/place/${place.placeId}`)
		.then(res => {
			//console.log(res.data);
			setSelectedPlace(res.data);
		})
		.catch(err => {
			console.error("장소 상세 정보 조회 실패:", err);
		});
	};
	const roulette = () => {
		if(!markerList || markerList.length<1) {
			window.Toastify({
	            text: "지도를 가져오는 중입니다. 잠시만 기다려주세요.",
	            duration: 3000,
				newWindow: true,
			  	close: true,	
	            gravity: 'top', // top or bottom
	            position: 'center', // left, center or right
				stopOnFocus: true,
	            style: {
	                background: 'linear-gradient(to left, #F4A261, #ea580c)',
	            }
	        }).showToast();
			return;
		}
		setNeedMenu(true);
		let listSize = markerList.length;
		for(let i=0;i<100;i++){
			setTimeout(() => {
				const marker = markerList[Math.floor(Math.random() * listSize)];
				
				setSelectedMarker(marker);
				// 마지막 룰렛
	            if (i==99) {
	                // 지도 중심 이동
	                map.panTo(marker.getPosition());
					// 팝업
	                handleSelectedPlace(marker.place);
	            }
			}, 100+50*i);
		}
	};
    const mapContextValues = {
        map,
        setMap,
        moveMap,
		getPlaceList,
		placeList,
		setMarkerList,
		markerList,
		selectedMarker, 
		setSelectedMarker,
		roulette,
		handleSelectedPlace, 
		selectedPlace, 
		setSelectedPlace,
		needMenu,
		setNeedMenu
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
                        <RecentCard />
                    </div>
                </main>
				{selectedPlace && (
	                <PlacePopup
	                    place={selectedPlace}
						needMenu={needMenu}
	                    onClose={() => {
							setSelectedPlace(null);
							setNeedMenu(false);
						}}
	                />
	            )}
				<ActionBar/>
                <footer>
                    <b>LunchPick</b>
                    <span>오늘의 점심을 더 쉽게. © 2026</span>
                </footer>
            </div>
        </MapContext.Provider>
	);
}

export default Home;