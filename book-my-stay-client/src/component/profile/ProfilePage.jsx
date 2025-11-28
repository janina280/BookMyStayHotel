import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../service/ApiService';
import './ProfilePage.css'; 

const ProfilePage = () => {
    const [user, setUser] = useState(null);
    const [error, setError] = useState(null);
    const [editingBooking, setEditingBooking] = useState({});
    const [ratingModal, setRatingModal] = useState({ visible: false, booking: null, rating: 0 });
    const navigate = useNavigate();

useEffect(() => {
    const fetchUserProfile = async () => {
        try {
            const response = await ApiService.getUserProfile();
            const userPlusBookings = await ApiService.getUserBookings(response.user.id);
            setUser(userPlusBookings.user);
        } catch (error) {
            setError(error.response?.data?.message || error.message);
        }
    };
    fetchUserProfile();
}, []);

const handleLogout = () => {
    ApiService.logout();
    navigate('/home');
};

const handleEditProfile = () => {
    navigate('/edit-profile');
};

const handleUpdateBooking = async (bookingId, newCheckIn, newCheckOut) => {
    try {
        await ApiService.updateBookingDates(bookingId, newCheckIn, newCheckOut);
        const updatedBookings = await ApiService.getUserBookings(user.id);
        setUser(updatedBookings.user);
    } catch (err) {
        console.error(err);
        alert(err.response?.data?.message || err.message);
    }
};

const openRatingModal = (booking) => {
    setRatingModal({ visible: true, booking, rating: 0 });
};

const submitRating = async () => {
    if (!ratingModal.booking) return;
    try {
        await ApiService.rateRoom(ratingModal.booking.room.id, ratingModal.rating);
        alert(`Thanks! Current rating: ${ratingModal.rating}`);
        // Actualizează local state să nu mai afișeze butonul
        const updatedBookings = user.bookings.map((b) =>
            b.id === ratingModal.booking.id ? { ...b, rated: true } : b
        );
        setUser({ ...user, bookings: updatedBookings });
    } catch (err) {
        alert(err.response?.data?.message || err.message);
    } finally {
        setRatingModal({ visible: false, booking: null, rating: 0 });
    }
};

return (
    <div className="profile-page">
        {user && <h2>Welcome, {user.name}</h2>}
        <div className="profile-actions">
            <button className="edit-profile-button" onClick={handleEditProfile}>Edit Profile</button>
            <button className="logout-button" onClick={handleLogout}>Logout</button>
        </div>

        {error && <p className="error-message">{error}</p>}
        {user && (
            <div className="profile-details">
                <h3>My Profile Details</h3>
                <p><strong>Email:</strong> {user.email}</p>
                <p><strong>Phone Number:</strong> {user.phoneNumber}</p>
            </div>
        )}

        <div className="bookings-section">
            <h3>My Booking History</h3>
            <div className="booking-list">
                {user && user.bookings.length > 0 ? (
                    user.bookings.map((booking) => {
                        const isEditing = editingBooking[booking.id]?.isEditing || false;
                        const checkIn = editingBooking[booking.id]?.checkIn || booking.checkInDate;
                        const checkOut = editingBooking[booking.id]?.checkOut || booking.checkOutDate;

                        const handleEditClick = () => {
                            setEditingBooking({
                                ...editingBooking,
                                [booking.id]: { checkIn, checkOut, isEditing: true }
                            });
                        };

                        const handleCancel = () => {
                            setEditingBooking({
                                ...editingBooking,
                                [booking.id]: { ...editingBooking[booking.id], isEditing: false }
                            });
                        };

                        const handleCheckInChange = (value) => {
                            setEditingBooking({
                                ...editingBooking,
                                [booking.id]: { ...editingBooking[booking.id], checkIn: value }
                            });
                        };

                        const handleCheckOutChange = (value) => {
                            setEditingBooking({
                                ...editingBooking,
                                [booking.id]: { ...editingBooking[booking.id], checkOut: value }
                            });
                        };

                        const handleSave = () => {
                            handleUpdateBooking(booking.id, checkIn, checkOut);
                            handleCancel();
                        };

                        const now = new Date();
                        const checkoutDate = new Date(booking.checkOutDate);
                        const showRatingButton = checkoutDate < now && !booking.rated;

                        return (
                            <div key={booking.id} className="booking-item">
                                <p><strong>Booking Code:</strong> {booking.bookingConfirmationCode}</p>
                                {isEditing ? (
                                    <>
                                        <label>
                                            Check-in Date:
                                            <input type="date" value={checkIn} onChange={(e) => handleCheckInChange(e.target.value)} />
                                        </label>
                                        <label>
                                            Check-out Date:
                                            <input type="date" value={checkOut} onChange={(e) => handleCheckOutChange(e.target.value)} />
                                        </label>
                                        <button onClick={handleSave}>Save</button>
                                        <button onClick={handleCancel}>Cancel</button>
                                    </>
                                ) : (
                                    <>
                                        <p><strong>Check-in Date:</strong> {booking.checkInDate}</p>
                                        <p><strong>Check-out Date:</strong> {booking.checkOutDate}</p>
                                        <button onClick={handleEditClick}>Edit Dates</button>
                                    </>
                                )}
                                <p><strong>Total Guests:</strong> {booking.totalNumOfGuest}</p>
                                <p><strong>Room Type:</strong> {booking.room.roomType}</p>
                                <p><strong>Average Rating:</strong> {booking.room.averageRating || 'Not rated yet'}</p>
                                <img src={booking.room.roomPhotoUrl} alt="Room" className="room-photo" />
                                {showRatingButton && (
                                    <button onClick={() => openRatingModal(booking)}>Rate Room</button>
                                )}
                            </div>
                        );
                    })
                ) : (
                    <p>No bookings found.</p>
                )}
            </div>
        </div>

        {ratingModal.visible && (
            <div className="rating-modal-overlay">
                <div className="rating-modal">
                    <h3>Rate your stay: {ratingModal.booking?.room.roomType}</h3>
                    <div className="rating-stars">
                        {[1,2,3,4,5].map((star) => (
                            <span
                                key={star}
                                className={`star ${star <= ratingModal.rating ? 'selected' : ''}`}
                                onClick={() => setRatingModal({ ...ratingModal, rating: star })}
                            >
                                ★
                            </span>
                        ))}
                    </div>
                    <button onClick={submitRating}>Submit</button>
                    <button onClick={() => setRatingModal({ visible: false, booking: null, rating: 0 })}>Cancel</button>
                </div>
            </div>
        )}
    </div>
);


};

export default ProfilePage;
