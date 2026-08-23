import { useEffect, useContext, useState } from "react";
import { MapContext } from "../pages/Home";
import axios from 'axios';

const Map = () => {
	const { map, setMap, panTo, placeList, setMarkerList, markerList } = useContext(MapContext);
	
	const drawMarkers = (markerList, placeList) => {
		// 기존 마커 제거
		for(let i=0;i<markerList.length;i++){
			var marker = markerList[i];
			marker.setMap(null);
		}
		
		// 새로운 마커 배열 생성
		const markers = [];
		
		for(let i=0;i<placeList.length;i++){
			var place = placeList[i];
			// 마커가 표시될 위치입니다
			var markerPosition  = new window.kakao.maps.LatLng(place.lat, place.lng); 
	
			// 마커를 생성합니다
			var marker = new window.kakao.maps.Marker({
			    position: markerPosition,
				clickable: true,
			});
	
			// 마커가 지도 위에 표시되도록 설정합니다
			marker.setMap(map);
	
			// 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
			window.kakao.maps.event.addListener(marker, 'click', function() {
			    alert('마커를 클릭했습니다!' + place.placeId);
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
		drawMarkers(markerList, placeList);
    }, []);
	// 장소가 변경되거나 필터 변경이 발생했을 경우
    useEffect(() => {
		drawMarkers(markerList, placeList);
    }, [placeList]);
	
    return (
		<section className="map-card">
            <div className="map-toolbar">
                <div>
                    <span className="live-dot"></span>
                    <span>NEARBY PLACES</span>
                </div>
            </div>
            <div id="map" className="map" />
		</section>
    );
}

export default Map;