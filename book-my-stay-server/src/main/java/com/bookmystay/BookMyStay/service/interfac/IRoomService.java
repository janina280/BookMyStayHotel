package com.bookmystay.BookMyStay.service.interfac;

import com.bookmystay.BookMyStay.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IRoomService {
    Response getAllRooms();

    List<String> getAllRoomTypes();

    Response getRoomById(Long roomId);

    Response deleteRoom(Long roomId);

    Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo);

    Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description);

    Response getAvailableRoomsByDataAndType(LocalDate checkInData, LocalDate checkOutData, String roomType);

    Response getAllAvailableRooms();

}
