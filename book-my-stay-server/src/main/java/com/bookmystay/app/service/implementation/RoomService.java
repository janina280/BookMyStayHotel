package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.service.interfac.IRoomService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService implements IRoomService {

    private final RoomQueryService queryService;
    private final RoomManagementService managementService;

    public RoomService(RoomQueryService queryService, RoomManagementService managementService) {
        this.queryService = queryService;
        this.managementService = managementService;
    }

    @Override
    public Response getAllRooms() {
        return queryService.getAllRooms();
    }

    @Override
    public List<String> getAllRoomTypes() {
        return queryService.getAllRoomTypes();
    }

    @Override
    public Response getRoomById(Long roomId) {
        return queryService.getRoomById(roomId);
    }

    @Override
    public Response getAllAvailableRooms() {
        return queryService.getAllAvailableRooms();
    }

    @Override
    public Response getAvailableRoomsByDataAndType(LocalDate checkIn, LocalDate checkOut, String roomType) {
        return queryService.getAvailableRoomsByDataAndType(checkIn, checkOut, roomType);
    }

    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        return managementService.addNewRoom(photo, roomType, roomPrice, description);
    }

    @Override
    public Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        return managementService.updateRoom(roomId, description, roomType, roomPrice, photo);
    }

    @Override
    public Response deleteRoom(Long roomId) {
        return managementService.deleteRoom(roomId);
    }
}
