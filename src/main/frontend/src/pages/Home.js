import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
	return (
		<div>
			홈~ <br/>
			<Link to="/about">소개</Link>
		</div>
	);
}

export default Home;