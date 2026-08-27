import React from 'react';
import { Link } from 'react-router-dom';
import Header from '../component/Header';
import Map from '../component/Map'
import "../css/style.css";

const Home = () => {
	return (
		<div>
			<Header/>
			<div className="content-grid">
		        <div className="left-column">
					<section className="map-card">
						<div className="map-toolbar">
			              <div>
			                <span className="live-dot"></span>
			                <span>NEARBY PLACES</span>
			              </div>
			            </div>
						<Map/>
					</section>
				</div>
				<aside className="recent-card">
		          <div className="card-title-row">
		            <div>
		              <p className="eyebrow">HISTORY</p>
		              <h2>RECENT PICKS</h2>
		            </div>
		            <button className="more" type="button">•••</button>
		          </div>

		          <ul className="history-list">
		            <li>
		              <div className="food-icon">🍱</div>
		              <div>
		                <strong>돈우마미</strong>
		                <small>덮밥 · 9,000원</small>
		              </div>
		            </li>
		            <li>
		              <div className="food-icon">🍜</div>
		              <div>
		                <strong>홍두깨</strong>
		                <small>칼국수 · 8,000원</small>
		              </div>
		            </li>
		            <li>
		              <div className="food-icon">🍲</div>
		              <div>
		                <strong>돌솥정식</strong>
		                <small>한식 · 10,000원</small>
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
		</div>
	);
}

export default Home;