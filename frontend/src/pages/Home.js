import React, { useState, createContext, useEffect } from 'react';
import axios from 'axios';
import Header from '../component/Header';
import RecommendBanner from '../component/RecommendBanner';
import FilterCard from '../component/FilterCard';
import Map from '../component/Map'
import ActionBar from '../component/ActionBar'
import PlacePopup from "../component/PlacePopup";
import MapLoading from "../component/MapLoading";
import RecentCard from "../component/RecentCard";
import "../css/style.css";

export const MapContext = createContext(null);
export const WeatherContext = createContext(null);
export const HistoryContext = createContext(null);
export const FilterContext = createContext(null);
export const ChatContext = createContext(null);

const Home = () => {
    const [map, setMap] = useState(null);
	const [placeList, setPlaceList] = useState([]);
	const [markerList, setMarkerList] = useState([]);
	const [selectedMarker, setSelectedMarker] = useState(null);
	const [selectedPlace, setSelectedPlace] = useState(null);
	const [placeLoading, setPlaceLoading] = useState(false);
	const [needMenu, setNeedMenu] = useState(false);
	
	const [weatherIcon, setWeatherIcon] = useState("");
	const [temperature, setTemperature] = useState("");
	
	const [historyList, setHistoryList] = useState([]);
	const [historyMenu, setHistoryMenu] = useState(null);
	
	const [active, setActive] = useState("한성대");
	const [weatherOn, setWeatherOn] = useState(false);
	const [budget, setBudget] = useState(50000);
	
	const [isJoined, setIsJoined] = useState(false); // 채팅에 입장 됐는지. 
	
	const token = localStorage.getItem('jwt');
	// 세션 테스트
	//axios.get("/test");
	
	// ***************** 날씨 ***********************
	// 날씨 아이콘
	const weatherIconMap = {
	    0: "☀️",
	    1: "🌤️",
	    2: "⛅",
	    3: "☁️",
	    45: "🌫️",
	    48: "🌫️",

	    51: "🌧️", 53: "🌧️", 55: "🌧️",
	    56: "🌧️", 57: "🌧️",
	    61: "🌧️", 63: "🌧️", 65: "🌧️",
	    66: "🌧️", 67: "🌧️",
	    80: "🌧️", 81: "🌧️", 82: "🌧️",

	    71: "❄️", 73: "❄️", 75: "❄️",
	    77: "❄️", 85: "❄️", 86: "❄️",
	    
	    95: "⛈️", 96: "⛈️", 99: "⛈️"
	};
	// 날씨 정보 가져오기
	const getWeather = async () => {
        await axios.get(
            "https://api.open-meteo.com/v1/forecast",
            {
                params: {
                    latitude: 37.5665,
                    longitude: 126.9780,
                    current: "temperature_2m,weather_code",
                    timezone: "Asia/Seoul"
                }
            }
        )
		.then(res => {
			//console.log(res.data);
			//console.log(res.data.current.weather_code);
			//console.log(res.data.current.temperature_2m);
            setWeatherIcon(weatherIconMap[res.data.current.weather_code]);
            setTemperature(res.data.current.temperature_2m);
        })
        .catch(err => {
            console.error(err);
        });
	};
	
	// ******************* 맵 ********************
    // 부드럽게 맵 이동
	const moveMap = (lat, lng) => {
        if (!map) return;

        const position = new window.kakao.maps.LatLng(lat, lng);

        map.panTo(position);
    };
	// 위치, 가격으로 필터링 된 장소 가져오기
    const getPlaceList = async (type, price, weatherOn, temperature, weatherIcon) => {
		let loadingTimer;

	    // 날씨 필터일 때만,
	    // 400ms 이상 걸리면 로딩 UI 표시
	    if (weatherOn) {
	        loadingTimer = setTimeout(() => {
	            setPlaceLoading(true);
	        }, 400);
	    }
		
		await axios.get("/place/list", {
		        params: {
		            type,
		            price,
		            weatherOn,
		            ...(weatherOn && {
		                temperature,
		                weatherIcon
		            })
		        }
			})
        .then(res => {
            //console.log(res.data);
            setPlaceList(res.data);
        })
        .catch(err => {
            console.error(err);
        })
		.finally(() => {
			clearTimeout(loadingTimer);
			setPlaceLoading(false);
		});
    };
	// 장소 팝업 띄우기(세부 정보 가져오기)
	const handleSelectedPlace = async (place) => {
		//alert(place.placeId);
		// 쿠키를 서버에 같이 전송
		await axios.get(`/place/${place.placeId}`, { withCredentials: true })
		.then(res => {
			//console.log(res.data);
			// List<placeInfoDto>
			setSelectedPlace(res.data);
		})
		.catch(err => {
			console.error("장소 상세 정보 조회 실패:", err);
		});
	};
	// 여러가지 이유로 마커와 팝업이 연계가 잘 안되는 경우 마커 생성까지 보장하는 함수
	const showPlaceOnMap = (place) => {

	    const marker = markerList.find(
	        marker => marker.place.placeId === place.placeId
	    );

	    // 이미 지도에 마커가 있는 경우
	    if (marker) {
	        setSelectedMarker(marker);
	        map.panTo(marker.getPosition());
	        handleSelectedPlace(marker.place);
	        return;
	    }

	    // 필터 때문에 현재 지도에 없는 장소
	    const markerPosition = new window.kakao.maps.LatLng(place.lat, place.lng);

	    const newMarker = new window.kakao.maps.Marker({
	        position: markerPosition,
	        clickable: true
	    });

	    newMarker.place = place;
	    newMarker.setMap(map);

	    window.kakao.maps.event.addListener(newMarker, "click", function () {
	            setSelectedMarker(newMarker);
	            handleSelectedPlace(place);
	        }
	    );

	    setMarkerList(prev => [ ...prev, newMarker ]);

	    map.panTo(newMarker.getPosition());
	    setSelectedMarker(newMarker);
	    handleSelectedPlace(place);
	};
	// 룰렛
	const roulette = async () => {
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
		
		// 룰렛으로 메뉴를 추천 받는 경우, 메뉴까지 골라줘야함을 PlacePopup에 보냄
		setNeedMenu(true);
		
		try {
	        // 현재 지도에 있는 장소들의 placeId
	        const placeIds = markerList.map(marker => marker.place.placeId);

	        // 서버가 실제 당첨 장소를 결정
	        const response = await axios.post(
	            '/roulette',
	            placeIds,
	            { headers: token ? { Authorization: `Bearer ${token}` } : {} }
	        );

	        const selectedPlaceId = response.data;

	        // 서버가 고른 실제 marker 찾기
	        const selectedMarker = markerList.find(
	            marker => marker.place.placeId === selectedPlaceId
	        );

	        if (!selectedMarker) {
	            console.error('서버가 선택한 장소의 마커가 없습니다.');
	            return;
	        }
		
			// 장소 룰렛 애니메이션
			let listSize = markerList.length;
			for(let i=0;i<30;i++){
				const delay = i * i * 3;
				
				setTimeout(() => {
					const marker = (i === 29) ? selectedMarker 
						: markerList[Math.floor(Math.random() * listSize)];
					
					setSelectedMarker(marker);
					// 마지막 룰렛
		            if (i==29) {
		                // 지도 중심 이동
		                map.panTo(marker.getPosition());
						// 팝업
						setTimeout(() => {
		                	handleSelectedPlace(marker.place);
						}, 600);
		            }
				}, delay);
			}
		} catch (error) {
	        console.error('룰렛 실패:', error);
	    }
	};
	
	// ************ 히스토리 ***************
	const getHistory = () => {
		axios.get(`/history/list`, { headers: { Authorization: `Bearer ${token}` }})
		.then(res => {
			console.log(res.data);
			setHistoryList(res.data);
		})
		.catch(err => {
	        console.error(err);
	    });
	};
	const addHistory = (menuId) => {
        axios.put(`/history/${menuId}`, null, { headers: { Authorization: `Bearer ${token}` }})
        .then(() => {
            getHistory();
        })
		.catch(err => {
	        console.error(err);
	    });
    };
	const deleteHistory = (finalDate) => {
	    const token = localStorage.getItem('jwt');

	    return axios.delete(`/history/delete/${encodeURIComponent(finalDate)}`, { headers: { Authorization: `Bearer ${token}` } })
	    .then(() => {
	        getHistory();
	    })
		.catch(err => {
	        console.error(err);
	    });
	};
	
	// ContextValues
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
		setNeedMenu,
		showPlaceOnMap,
    };
	const weatherContextValues = {
		weatherIcon,
		temperature
	};
	const historyContextValues = {
		historyList,
		getHistory,
		addHistory,
		setHistoryMenu,
		deleteHistory
	};
	const filterContextValues = {
		active,
        setActive,
        weatherOn,
        setWeatherOn,
        budget,
        setBudget
	};
	const chatContextValues = {
		isJoined,
		setIsJoined
	};
	
	// 최초 렌더링
	useEffect(() => {
		getWeather();
		getPlaceList("한성대",70000, false, null, null);
	},[]);
		
	return (
		<FilterContext.Provider value={filterContextValues}>
	        <MapContext.Provider value={mapContextValues}>
				<ChatContext.Provider value={chatContextValues}>
		            <div className="app-shell">
						<WeatherContext.Provider value={weatherContextValues}>
							<HistoryContext.Provider value={historyContextValues}>
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
										menu={historyMenu}
										needMenu={needMenu}
					                    onClose={() => {
											setSelectedPlace(null);
											setNeedMenu(false);
											setHistoryMenu(null);
										}}
					                />
					            )}
								{placeLoading && (
							        <MapLoading />
							    )}
								<ActionBar/>
							</HistoryContext.Provider>
						</WeatherContext.Provider>
		                <footer>
		                    <b>LunchPick</b>
		                    <span>오늘의 점심을 더 쉽게. © 2026</span>
		                </footer>
		            </div>
				</ChatContext.Provider>
	        </MapContext.Provider>
		</FilterContext.Provider>
	);
}

export default Home;