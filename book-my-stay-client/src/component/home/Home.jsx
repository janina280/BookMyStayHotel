import React, { useState } from "react";
import RoomSearch from "../common/RoomSearch";
import RoomResult from "../common/RoomResult";

const HomePage = () => {
    const [roomSearchResults, setRoomSearchResults] = useState([]);

    const handleSearchResult = (results) => {
        setRoomSearchResults(results);
    };

    return (
        <div className="home">
            <section>
                <header className="header-banner">
                    <img
                        src="./assets/images/hotel.jpg"
                        alt="Artemis Hotel"
                        className="header-image"
                    />
                    <div className="overlay"></div>
                    <div className="animated-texts overlay-content">
                        <h1>
                            Welcome to <span className="phegon-color">Artemis Hotel</span>
                        </h1>
                        <br/>
                        <h3>Guided by the moon, embraced by comfort.</h3>
                    </div>
                </header>
            </section>

            <RoomSearch handleSearchResult={handleSearchResult}/>
            <RoomResult roomSearchResults={roomSearchResults}/>

            <h4>
                <a className="view-rooms-home" href="/rooms">
                    All Rooms
                </a>
            </h4>

            {/* SERVICES SECTION */}
            <h2 className="home-services">
                Services at <span className="phegon-color">Artemis Hotel</span>
            </h2>

            <section className="service-section">
                <div className="service-card">
                    <img src="./assets/images/air-conditioner.png" alt="Air Conditioning"/>
                    <div className="service-details">
                        <h3 className="service-title">Air Conditioning</h3>
                        <p className="service-description">
                            Stay cool and comfortable throughout your stay with our individually
                            controlled in-room air conditioning.
                        </p>
                    </div>
                </div>
                <div className="service-card">
                    <img src="./assets/images/mini-bar.png" alt="Mini Bar"/>
                    <div className="service-details">
                        <h3 className="service-title">Mini Bar</h3>
                        <p className="service-description">
                            Enjoy a convenient selection of beverages and snacks stocked in your
                            room's mini bar with no additional cost.
                        </p>
                    </div>
                </div>
                <div className="service-card">
                    <img src="./assets/images/parking.png" alt="Parking"/>
                    <div className="service-details">
                        <h3 className="service-title">Parking</h3>
                        <p className="service-description">
                            We offer on-site parking for your convenience. Please inquire about valet
                            parking options if available.
                        </p>
                    </div>
                </div>
                <div className="service-card">
                    <img src="./assets/images/wifi.png" alt="WiFi"/>
                    <div className="service-details">
                        <h3 className="service-title">WiFi</h3>
                        <p className="service-description">
                            Stay connected throughout your stay with complimentary high-speed Wi-Fi
                            access available in all guest rooms and public areas.
                        </p>
                    </div>
                </div>
            </section>
            <br/>
            <br/>

            <section className="about-section">
                <div className="about-content">
                    <h2>About <span className="phegon-color">Artemis Hotel</span></h2>
                    <p>
                        Nestled in the heart of the city, Artemis Hotel offers a unique blend of
                        elegance and comfort. Inspired by the calm and grace of the goddess Artemis,
                        our mission is to provide every guest with a serene experience surrounded by
                        modern luxury.
                    </p>
                    <p>
                        Whether you're traveling for business or leisure, you'll enjoy our spacious
                        rooms, refined dining, and exceptional hospitality designed to make you feel
                        at home.
                    </p>
                </div>
            </section>
            <br/>
            <br/>

            <section className="facilities-section">
                <h2>Facilities & Amenities</h2>
                <div className="service-section">
                    <div className="service-card">
                        <img src="./assets/images/spa.jpeg" alt="Spa & Wellness"/>
                        <div className="service-details">
                            <h3 className="service-title">Spa & Wellness</h3>
                            <p className="service-description">
                                Relax and rejuvenate with our exclusive spa treatments and sauna access.
                            </p>
                        </div>
                    </div>

                    <div className="service-card">
                        <img src="./assets/images/restaurant.jpeg" alt="Restaurant"/>
                        <div className="service-details">
                            <h3 className="service-title">Restaurant & Bar</h3>
                            <p className="service-description">
                                Enjoy fine dining and a wide selection of wines and cocktails.
                            </p>
                        </div>
                    </div>

                    <div className="service-card">
                        <img src="./assets/images/pool.jpeg" alt="Swimming Pool"/>
                        <div className="service-details">
                            <h3 className="service-title">Outdoor Pool</h3>
                            <p className="service-description">
                                Take a refreshing dip in our heated outdoor swimming pool.
                            </p>
                        </div>
                    </div>

                    <div className="service-card">
                        <img src="./assets/images/gym.jpeg" alt="Gym"/>
                        <div className="service-details">
                            <h3 className="service-title">Fitness Center</h3>
                            <p className="service-description">
                                Stay active with state-of-the-art gym equipment available 24/7.
                            </p>
                        </div>
                    </div>
                </div>
            </section>
            <br/>
            <br/>

            <footer className="footer">
                <p>© 2025 Artemis Hotel. All rights reserved.</p>
                <p>Contact: <a href="mailto:info@artemishotel.com">info@artemishotel.com</a> | +40 723 456 789</p>
            </footer>
        </div>
    );
};

export default HomePage;
