package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.Room;
import com.bookmystay.app.exception.OurException;
import com.bookmystay.app.repository.RoomRepository;
import com.bookmystay.app.utils.Utils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomQueryService {

    private final RoomRepository roomRepository;
    private static final String SUCCESS = "successful";
    private static final String ROOM_NOT_FOUND = "Room not found!";

    public RoomQueryService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Response getAllRooms() {
        Response response = new Response();
        try {
            List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            response.setRoomList(Utils.mapRoomListEntityToRoomDTO(rooms));
            response.setStatusCode(200);
            response.setMessage(SUCCESS);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all rooms: " + e.getMessage());
        }
        return response;
    }

    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    public Response getRoomById(Long roomId) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new OurException(ROOM_NOT_FOUND));
            response.setRoom(Utils.mapRoomEntityToRoomDTOPlusBookings(room));
            response.setStatusCode(200);
            response.setMessage(SUCCESS);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting room by id: " + e.getMessage());
        }
        return response;
    }

    public Response getAllAvailableRooms() {
        Response response = new Response();
        try {
            List<Room> rooms = roomRepository.getAllAvailableRooms();
            response.setRoomList(Utils.mapRoomListEntityToRoomDTO(rooms));
            response.setStatusCode(200);
            response.setMessage(SUCCESS);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all available rooms: " + e.getMessage());
        }
        return response;
    }

    public Response getAvailableRoomsByDataAndType(LocalDate checkIn, LocalDate checkOut, String roomType) {
        Response response = new Response();
        try {
            List<Room> rooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkIn, checkOut, roomType);
            response.setRoomList(Utils.mapRoomListEntityToRoomDTO(rooms));
            response.setStatusCode(200);
            response.setMessage(SUCCESS);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting available rooms by date/type: " + e.getMessage());
        }
        return response;
    }
}
