const MapLoading = () => {

    return (
        <div className="map-loading-overlay">
            <div className="map-loading-card">
                <div className="food-loader">
                    <span>🍜</span>
                </div>
                <strong>
                    딱 맞는 점심을 찾는 중...
                </strong>
                <p>
                    날씨와 메뉴를 비교하고 있어요
                </p>
                <div className="loading-bar">
                    <div className="loading-bar-inner" />
                </div>
            </div>
        </div>
    );
};

export default MapLoading;