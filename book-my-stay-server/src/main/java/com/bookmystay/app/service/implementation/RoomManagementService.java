package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.Room;
import com.bookmystay.app.exception.OurException;
import com.bookmystay.app.repository.RoomRepository;
import com.bookmystay.app.service.AwsS3Service;
import com.bookmystay.app.utils.Utils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Service
public class RoomManagementService {

    private final RoomRepository roomRepository;
    private final AwsS3Service awsS3Service;
    private static final String SUCCESS = "successful";
    private static final String ROOM_NOT_FOUND = "Room not found!";

    public RoomManagementService(RoomRepository roomRepository, AwsS3Service awsS3Service) {
        this.roomRepository = roomRepository;
        this.awsS3Service = awsS3Service;
    }

    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        Response response = new Response();
        try {
            String imageUrl = awsS3Service.saveImageToS3(photo);
            Room room = new Room();
            room.setRoomDescription(description);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomPhotoUrl(imageUrl);

            Room saved = roomRepository.save(room);
            response.setRoom(Utils.mapRoomEntityToRoomDTO(saved));
            response.setStatusCode(200);
            response.setMessage(SUCCESS);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error adding new room: " + e.getMessage());
        }
        return response;
    }

    public Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new OurException(ROOM_NOT_FOUND));

            String imageUrl = (photo != null && !photo.isEmpty()) ? awsS3Service.saveImageToS3(photo) : room.getRoomPhotoUrl();

            if (roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setRoomPrice(roomPrice);
            if (description != null) room.setRoomDescription(description);
            room.setRoomPhotoUrl(imageUrl);

            Room updated = roomRepository.save(room);
            response.setRoom(Utils.mapRoomEntityToRoomDTO(updated));
            response.setStatusCode(200);
            response.setMessage(SUCCESS);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating room: " + e.getMessage());
        }
        return response;
    }

    public Response deleteRoom(Long roomId) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new OurException(ROOM_NOT_FOUND));
            roomRepository.delete(room);
            response.setStatusCode(200);
            response.setMessage(SUCCESS);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting room: " + e.getMessage());
        }
        return response;
    }

    public Response rateRoom(Long roomId, int rating) {
        Response response = new Response();
        try {
            if (rating < 1 || rating > 5) {
                throw new OurException("Rating must be between 1 and 5");
            }

            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new OurException("Room not found"));

            double total = room.getAverageRating() * room.getNumberOfRatings();
            room.setNumberOfRatings(room.getNumberOfRatings() + 1);
            room.setAverageRating((total + rating) / room.getNumberOfRatings());

            roomRepository.save(room);

            response.setStatusCode(200);
            response.setMessage(SUCCESS);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        return response;
    }

}
