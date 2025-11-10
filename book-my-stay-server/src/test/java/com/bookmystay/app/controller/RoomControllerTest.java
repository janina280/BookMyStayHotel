package com.bookmystay.app.controller;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.service.interfac.IRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoomControllerTest {

    @Mock
    private IRoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private Response response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        response = new Response();
        response.setStatusCode(200);
        response.setMessage("Success");
    }

    @Test
    void getAllRooms_shouldReturnResponseFromService() {
        when(roomService.getAllRooms()).thenReturn(response);

        ResponseEntity<Response> result = roomController.getAllRooms();

        verify(roomService, times(1)).getAllRooms();
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }

    @Test
    void getAllRoomTypes_shouldReturnListFromService() {
        List<String> types = List.of("Single", "Double");
        when(roomService.getAllRoomTypes()).thenReturn(types);

        List<String> result = roomController.getAllRoomTypes();

        verify(roomService, times(1)).getAllRoomTypes();
        assertEquals(2, result.size());
        assertEquals("Single", result.get(0));
    }

    @Test
    void addNewRoom_shouldReturnResponseFromService_whenValidInput() {
        MultipartFile photo = mock(MultipartFile.class);
        when(photo.isEmpty()).thenReturn(false);
        when(roomService.addNewRoom(photo, "Single", BigDecimal.valueOf(100), "Nice room")).thenReturn(response);

        ResponseEntity<Response> result = roomController.addNewRoom(photo, "Single", BigDecimal.valueOf(100), "Nice room");

        verify(roomService, times(1)).addNewRoom(photo, "Single", BigDecimal.valueOf(100), "Nice room");
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }

    @Test
    void addNewRoom_shouldReturn400_whenInvalidInput() {
        ResponseEntity<Response> result = roomController.addNewRoom(null, "", null, "desc");

        assertEquals(400, result.getStatusCode().value());
        assertEquals("Please provide values for all fields (photo, roomType, roomPrice)!", result.getBody().getMessage());
        verifyNoInteractions(roomService);
    }

    @Test
    void getRoomById_shouldReturnResponseFromService() {
        when(roomService.getRoomById(1L)).thenReturn(response);

        ResponseEntity<Response> result = roomController.getRoomById(1L);

        verify(roomService, times(1)).getRoomById(1L);
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }

    @Test
    void getAllAvailableRooms_shouldReturnResponseFromService() {
        when(roomService.getAllAvailableRooms()).thenReturn(response);

        ResponseEntity<Response> result = roomController.getAllAvailableRooms();

        verify(roomService, times(1)).getAllAvailableRooms();
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }

    @Test
    void getAvailableRoomByDataAndType_shouldReturnResponseFromService_whenValidInput() {
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(2);
        when(roomService.getAvailableRoomsByDataAndType(checkIn, checkOut, "Single")).thenReturn(response);

        ResponseEntity<Response> result = roomController.getAvailableRoomByDataAndType(checkIn, checkOut, "Single");

        verify(roomService, times(1)).getAvailableRoomsByDataAndType(checkIn, checkOut, "Single");
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }

    @Test
    void getAvailableRoomByDataAndType_shouldReturn400_whenInvalidInput() {
        ResponseEntity<Response> result = roomController.getAvailableRoomByDataAndType(null, null, "");

        assertEquals(400, result.getStatusCode().value());
        assertEquals("Please provide values for all fields (checkInDate, checkOutDate, roomType)!", result.getBody().getMessage());
        verifyNoInteractions(roomService);
    }

    @Test
    void updateRoom_shouldReturnResponseFromService() {
        MultipartFile photo = mock(MultipartFile.class);
        when(roomService.updateRoom(1L, "desc", "Single", BigDecimal.valueOf(100), photo)).thenReturn(response);

        ResponseEntity<Response> result = roomController.updateRoom(1L, photo, "Single", BigDecimal.valueOf(100), "desc");

        verify(roomService, times(1)).updateRoom(1L, "desc", "Single", BigDecimal.valueOf(100), photo);
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }

    @Test
    void deleteRoom_shouldReturnResponseFromService() {
        when(roomService.deleteRoom(1L)).thenReturn(response);

        ResponseEntity<Response> result = roomController.deleteRoom(1L);

        verify(roomService, times(1)).deleteRoom(1L);
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }
}
