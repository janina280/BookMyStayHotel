package com.bookmystay.app.utils;


import com.bookmystay.app.dto.BookingDTO;
import com.bookmystay.app.dto.RoomDTO;
import com.bookmystay.app.dto.UserDTO;
import com.bookmystay.app.entity.Booking;
import com.bookmystay.app.entity.Room;
import com.bookmystay.app.entity.User;

import java.security.SecureRandom;
import java.util.List;

public class Utils {
    private Utils() {
        // private constructor to prevent instantiation
    }

    private  static final String ALPHANUMERIC_STRING="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom=new SecureRandom();
    public static String generateRandomConfirmationCode(int length){
        StringBuilder stringBuilder=new StringBuilder();
        for (int i=0; i<length;i++){
            int randomIndex= secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar=ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO= new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        return  userDTO;
    }

    public static UserDTO mapUserEntityToUserDTOPlusUserBookingsAndRoom(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        if (user.getBookings() != null && !user.getBookings().isEmpty()) {
            userDTO.setBookings(
                    user.getBookings().stream()
                            .filter(booking -> booking != null && booking.getRoom() != null)
                            .map(booking -> mapBookingEntityToBookingDTOPlusBookedRooms(booking, false))
                            .toList()
            );
        }

        return userDTO;
    }


    public static RoomDTO mapRoomEntityToRoomDTO(Room room) {
        RoomDTO roomDTO= new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setRoomDescription(room.getRoomDescription());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());

        return  roomDTO;
    }

    public static RoomDTO mapRoomEntityToRoomDTOPlusBookings(Room room) {
        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setRoomDescription(room.getRoomDescription());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());

        if (room.getBookings() != null) {
            roomDTO.setBookings(
                    room.getBookings().stream()
                            .map(Utils::mapBookingEntityToBookingDTO)
                            .toList()
            );
        }

        return roomDTO;
    }


    public static BookingDTO mapBookingEntityToBookingDTO (Booking booking){
        BookingDTO bookingDTO=new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        bookingDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        return  bookingDTO;
    }

    public static BookingDTO mapBookingEntityToBookingDTOPlusBookedRooms(Booking booking, boolean mapUser) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        bookingDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());

        if (mapUser && booking.getUser() != null) {
            bookingDTO.setUser(Utils.mapUserEntityToUserDTO(booking.getUser()));
        }

        if (booking.getRoom() != null) {
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setId(booking.getRoom().getId());
            roomDTO.setRoomDescription(booking.getRoom().getRoomDescription());
            roomDTO.setRoomType(booking.getRoom().getRoomType());
            roomDTO.setRoomPrice(booking.getRoom().getRoomPrice());
            roomDTO.setRoomPhotoUrl(booking.getRoom().getRoomPhotoUrl());
            bookingDTO.setRoom(roomDTO);
        }

        return bookingDTO;
    }


    public static List<UserDTO> mapUserListEntityToUserDTO(List<User> userList){
        return userList.stream()
                .map(Utils::mapUserEntityToUserDTO)
                .toList();
    }

    public static List<RoomDTO> mapRoomListEntityToRoomDTO(List<Room> roomList){
        return roomList.stream()
                .map(Utils::mapRoomEntityToRoomDTO)
                .toList();
    }

    public static List<BookingDTO> mapBookingListEntityToBookingDTO(List<Booking> bookingList){
        return bookingList.stream()
                .map(Utils::mapBookingEntityToBookingDTO)
                .toList();
    }

}
