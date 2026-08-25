import { useEffect, useContext, useState } from "react";
import { MapContext } from "../pages/Home";
import PlacePopup from "./PlacePopup";
import axios from 'axios';

const Map = () => {
	const { map, setMap, panTo, placeList, setMarkerList, markerList } = useContext(MapContext);
	const [selectedPlace, setSelectedPlace] = useState(null);
	
	const drawMarkers = (markerList, placeList) => {
		// 기존 마커 제거
		for(let i=0;i<markerList.length;i++){
			let marker = markerList[i];
			marker.setMap(null);
		}
		
		// 새로운 마커 배열 생성
		const markers = [];
		
		for(let i=0;i<placeList.length;i++){
			let place = placeList[i];
			// 마커가 표시될 위치
			let markerPosition  = new window.kakao.maps.LatLng(place.lat, place.lng); 
	
			// 마커를 생성
			let marker = new window.kakao.maps.Marker({
			    position: markerPosition,
				clickable: true,
			});
	
			// 마커가 지도 위에 표시되도록
			marker.setMap(map);
	
            // 마커에 클릭 이벤트(우클릭 : rightclick)
            window.kakao.maps.event.addListener(marker, 'click', async function() {
				//alert(place.placeId);
				await axios.get(`/place/${place.placeId}`)
				.then(res => {
					console.log(res.data);
					setSelectedPlace(res.data);
				})
				.catch(err => {
					console.error("장소 상세 정보 조회 실패:", err);
				})
            });
			
			// 마커 리스트에 추가
			markers.push(marker);
		}
		
		setMarkerList(markers);
	};
	
	// 최초 렌더링
    useEffect(() => {
		// 지도 가져오기
        window.kakao.maps.load(() => {
            const container = document.getElementById("map");

            const options = {
                center: new window.kakao.maps.LatLng(37.5884, 127.0062),
                level: 3,
            };

            const map = new window.kakao.maps.Map(container, options);
			
			// map을 저장
			setMap(map);
        });
    }, []);
	// 장소가 변경되거나 필터 변경이 발생했을 경우
    useEffect(() => {
		if(!map) return;
		
		drawMarkers(markerList, placeList);
    }, [map, placeList]);
	
    return (
        <>
            <section className="map-card">
                <div className="map-toolbar">
                    <div>
                        <span className="live-dot"></span>
                        <span>NEARBY PLACES</span>
                    </div>
                </div>
                <div className="map-container">
                    {/* 카카오맵 */}
                    <div id="map" className="map"></div>
                    {/* 지도 위 UI */}
                    <div className="map-overlay">
                        <div className="map-tooltip">
                            <strong>주변 추천 장소</strong>
                            <span>총 {placeList.length}곳 탐색됨</span>
                        </div>
                        <button className="map-go" type="button">
                            GO <span>↗</span>
                        </button>
                    </div>
                </div>
            </section>
            {selectedPlace && (
                <PlacePopup
                    place={selectedPlace}
                    onClose={() => setSelectedPlace(null)}
                />
            )}
        </>
    );
}

export default Map;