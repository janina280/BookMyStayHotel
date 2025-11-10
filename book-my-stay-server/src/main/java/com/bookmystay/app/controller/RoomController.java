package com.bookmystay.app.controller;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.service.interfac.IRoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final IRoomService roomService;

    public RoomController(IRoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllRooms() {
        return buildResponse(roomService.getAllRooms());
    }

    @GetMapping("/types")
    public List<String> getAllRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewRoom(@RequestParam(value = "photo", required = false) MultipartFile photo,
                                               @RequestParam(value = "roomType", required = false) String roomType,
                                               @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
                                               @RequestParam(value = "roomDescription", required = false) String description) {

        Response invalidResponse = validateRoomData(photo, roomType, roomPrice);
        if (invalidResponse != null) return buildResponse(invalidResponse);

        return buildResponse(roomService.addNewRoom(photo, roomType, roomPrice, description));
    }

    @GetMapping("/room-by-id/{roomId}")
    public ResponseEntity<Response> getRoomById(@PathVariable Long roomId) {
        return buildResponse(roomService.getRoomById(roomId));
    }

    @GetMapping("/all-available-rooms")
    public ResponseEntity<Response> getAllAvailableRooms() {
        return buildResponse(roomService.getAllAvailableRooms());
    }

    @GetMapping("/available-rooms-by-date-and-type")
    public ResponseEntity<Response> getAvailableRoomByDataAndType(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                                                                  @RequestParam(required = false) String roomType) {

        Response invalidResponse = validateAvailabilityParams(checkInDate, checkOutDate, roomType);
        if (invalidResponse != null) return buildResponse(invalidResponse);

        return buildResponse(roomService.getAvailableRoomsByDataAndType(checkInDate, checkOutDate, roomType));
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateRoom(@PathVariable Long roomId,
                                               @RequestParam(value = "photo", required = false) MultipartFile photo,
                                               @RequestParam(value = "roomType", required = false) String roomType,
                                               @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
                                               @RequestParam(value = "roomDescription", required = false) String description) {

        return buildResponse(roomService.updateRoom(roomId, description, roomType, roomPrice, photo));
    }

    @DeleteMapping("/deleteRoom/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteRoom(@PathVariable Long roomId) {
        return buildResponse(roomService.deleteRoom(roomId));
    }

    // ---- HELPER METHODS ----

    private ResponseEntity<Response> buildResponse(Response response) {
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    private Response validateRoomData(MultipartFile photo, String roomType, BigDecimal roomPrice) {
        if (photo == null || photo.isEmpty() || roomType == null || roomType.isBlank() || roomPrice == null) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide values for all fields (photo, roomType, roomPrice)!");
            return response;
        }
        return null;
    }

    private Response validateAvailabilityParams(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        if (checkInDate == null || checkOutDate == null || roomType == null || roomType.isBlank()) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide values for all fields (checkInDate, checkOutDate, roomType)!");
            return response;
        }
        return null;
    }
}
