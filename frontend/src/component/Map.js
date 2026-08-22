import { useEffect, useContext, useState } from "react";
import { MapContext } from "../pages/Home";
import axios from 'axios';

const Map = () => {
	const { setMap, panTo } = useContext(MapContext);
	
    useEffect(() => {
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

	/*const panTo = () => {
	    // 이동할 위도 경도 위치를 생성합니다 
	    var moveLatLon = new window.kakao.maps.LatLng(37.5552, 126.9374);
	    
	    // 지도 중심을 부드럽게 이동시킵니다
	    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
	    mapRef.current.panTo(moveLatLon);            
	}*/
	const handleClick = () => {
		panTo(37.5552,126.9374);
	}
	
    return (
		<section className="map-card">
            <div className="map-toolbar">
                <div>
                    <span className="live-dot"></span>
                    <span>NEARBY PLACES</span>
                </div>
            </div>
            <div id="map" className="map" />
            <button onClick={handleClick}>지도 중심좌표 부드럽게 이동시키기</button>
		</section>
    );
}

export default Map;