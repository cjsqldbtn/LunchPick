import { useState, useEffect, useContext } from "react";
import axios from 'axios';
import { AuthContext } from '../App';
import { HistoryContext, MapContext } from "../pages/Home";

const RecentCard = () => {
	const { isLogin } = useContext(AuthContext);
	const { historyList, getHistory, setHistoryMenu, deleteHistory } = useContext(HistoryContext);
	const { map, handleSelectedPlace, markerList, setSelectedMarker, setMarkerList } = useContext(MapContext);
	
	useEffect(() => {
        if (isLogin) {
            getHistory();
        }
    }, [isLogin]);
	
    return (
        <aside className="recent-card">
			{
				isLogin ? (<>
                        <div className="card-title-row">
                            <div>
                                <p className="eyebrow">HISTORY</p>
                                <h2>RECENT PICKS</h2>
                            </div>
                        </div>
                        <ul className="history-list login-history">
						{
                            historyList.map((history) => (
                                <li key={history.finalDate} 
									onClick={() => { 
										setHistoryMenu(history.menu);
										const marker = markerList.find(
									        marker => marker.place.placeId === history.place.placeId
									    );

									    if (marker) {
									        setSelectedMarker(marker);
									        handleSelectedPlace(marker.place);
									    } else {
											// 필터링 되어 최근 추천 메뉴가 지도 위에 존재하지 않는 경우
											// 새롭게 마커를 생성하고 팝업
											
											const markers = markerList;
											
											let place = history.place;
											let markerPosition  = new window.kakao.maps.LatLng(place.lat, place.lng);
											
											let marker = new window.kakao.maps.Marker({
											    position: markerPosition,
												clickable: true
											});
											marker.place = place;
											
											marker.setMap(map);
											window.kakao.maps.event.addListener(marker, 'click', function() {
												// 현재 마커 저장
											    setSelectedMarker(marker);
												
												// 장소 팝업
												handleSelectedPlace(place);
								            });
											
											markers.push(marker);
											setMarkerList(markers);
											map.panTo(marker.getPosition());
											setSelectedMarker(marker);
											handleSelectedPlace(place);
										}
									}}>
                                    <div>
                                        <strong>{history.menuName}</strong>
                                        <small>
                                            {history.placeCategory} {history.placeName} · {history.price?.toLocaleString()}원
                                        </small>
                                    </div>
									<button
							            className="history-delete-btn"
							            onClick={(e) => {
							                e.stopPropagation();
							                deleteHistory(history.finalDate);
							            }}
							        >
							            ×
							        </button>
                                </li>
                            ))
                        }
                        </ul>
				</>) : (<>
                        <div className="card-title-row">
                            <div>
                                <p className="eyebrow">JOIN MEMBER</p>
                                <h2>PROVIDED FUNCTIONS</h2>
                            </div>
                        </div>
                        <ul className="history-list guest-history">
                            <li>
                                <div className="food-icon">✨</div>
                                <div>
                                    <strong>AI 추천</strong>
                                    <small>채팅 내역을 기반으로 메뉴 추천 받으세요</small>
                                </div>
                            </li>
                            <li>
                                <div className="food-icon">✌️</div>
                                <div>
                                    <strong>친구들과 채팅</strong>
                                    <small>로그인한 친구들과 채팅을 통해 메뉴를 고르세요</small>
                                </div>
                            </li>
                            <li>
                                <div className="food-icon">❤️</div>
                                <div>
                                    <strong>추천 기록 저장</strong>
                                    <small>메뉴 저장이 가능하고, 중복 추천을 방지합니다</small>
                                </div>
                            </li>
                        </ul>
					</>)
			}
        </aside>
    );
}

export default RecentCard;