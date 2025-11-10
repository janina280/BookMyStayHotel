package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomQueryService queryService;

    @Mock
    private RoomManagementService managementService;

    @InjectMocks
    private RoomService roomService;

    private Response mockResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockResponse = new Response();
        mockResponse.setMessage("OK");
    }

    @Test
    void testGetAllRooms_delegatesToQueryService() {
        when(queryService.getAllRooms()).thenReturn(mockResponse);

        Response result = roomService.getAllRooms();

        verify(queryService).getAllRooms();
        verifyNoInteractions(managementService);
        assertEquals("OK", result.getMessage());
    }

    @Test
    void testGetAllRoomTypes_delegatesToQueryService() {
        List<String> roomTypes = List.of("Single", "Double");
        when(queryService.getAllRoomTypes()).thenReturn(roomTypes);

        List<String> result = roomService.getAllRoomTypes();

        verify(queryService).getAllRoomTypes();
        verifyNoInteractions(managementService);
        assertEquals(roomTypes, result);
    }

    @Test
    void testGetRoomById_delegatesToQueryService() {
        when(queryService.getRoomById(1L)).thenReturn(mockResponse);

        Response result = roomService.getRoomById(1L);

        verify(queryService).getRoomById(1L);
        verifyNoInteractions(managementService);
        assertEquals("OK", result.getMessage());
    }

    @Test
    void testGetAllAvailableRooms_delegatesToQueryService() {
        when(queryService.getAllAvailableRooms()).thenReturn(mockResponse);

        Response result = roomService.getAllAvailableRooms();

        verify(queryService).getAllAvailableRooms();
        verifyNoInteractions(managementService);
        assertEquals("OK", result.getMessage());
    }

    @Test
    void testGetAvailableRoomsByDataAndType_delegatesToQueryService() {
        LocalDate checkIn = LocalDate.of(2025, 1, 1);
        LocalDate checkOut = LocalDate.of(2025, 1, 10);
        String roomType = "Suite";

        when(queryService.getAvailableRoomsByDataAndType(checkIn, checkOut, roomType)).thenReturn(mockResponse);

        Response result = roomService.getAvailableRoomsByDataAndType(checkIn, checkOut, roomType);

        verify(queryService).getAvailableRoomsByDataAndType(checkIn, checkOut, roomType);
        verifyNoInteractions(managementService);
        assertEquals("OK", result.getMessage());
    }

    @Test
    void testAddNewRoom_delegatesToManagementService() {
        MultipartFile photo = mock(MultipartFile.class);
        when(managementService.addNewRoom(photo, "Suite", BigDecimal.TEN, "Nice room")).thenReturn(mockResponse);

        Response result = roomService.addNewRoom(photo, "Suite", BigDecimal.TEN, "Nice room");

        verify(managementService).addNewRoom(photo, "Suite", BigDecimal.TEN, "Nice room");
        verifyNoInteractions(queryService);
        assertEquals("OK", result.getMessage());
    }

    @Test
    void testUpdateRoom_delegatesToManagementService() {
        MultipartFile photo = mock(MultipartFile.class);
        when(managementService.updateRoom(1L, "Updated desc", "Deluxe", BigDecimal.ONE, photo))
                .thenReturn(mockResponse);

        Response result = roomService.updateRoom(1L, "Updated desc", "Deluxe", BigDecimal.ONE, photo);

        verify(managementService).updateRoom(1L, "Updated desc", "Deluxe", BigDecimal.ONE, photo);
        verifyNoInteractions(queryService);
        assertEquals("OK", result.getMessage());
    }

    @Test
    void testDeleteRoom_delegatesToManagementService() {
        when(managementService.deleteRoom(10L)).thenReturn(mockResponse);

        Response result = roomService.deleteRoom(10L);

        verify(managementService).deleteRoom(10L);
        verifyNoInteractions(queryService);
        assertEquals("OK", result.getMessage());
    }
}
