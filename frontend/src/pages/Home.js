import React from 'react';
import { Link } from 'react-router-dom';
import Header from '../component/Header';
import "../css/style.css";

const Home = () => {
	return (
		<div>
			<Header/>
			홈~ <br/>
			<Link to="/about">소개</Link>
		</div>
	);
}

export default Home;