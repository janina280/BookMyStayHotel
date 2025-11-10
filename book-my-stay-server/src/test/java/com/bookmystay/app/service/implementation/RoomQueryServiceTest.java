package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.Room;
import com.bookmystay.app.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomQueryServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomQueryService roomQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----- getAllRooms tests -----
    @Test
    void getAllRooms_success() {
        Room room = new Room();
        room.setId(1L);
        when(roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id")))
                .thenReturn(List.of(room));

        Response response = roomQueryService.getAllRooms();

        assertEquals(200, response.getStatusCode());
        assertEquals("successful", response.getMessage());
        assertNotNull(response.getRoomList());
        verify(roomRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void getAllRooms_exception() {
        when(roomRepository.findAll(any(Sort.class))).thenThrow(new RuntimeException("DB error"));

        Response response = roomQueryService.getAllRooms();

        assertEquals(500, response.getStatusCode());
        assertTrue(response.getMessage().contains("Error getting all rooms"));
    }

    // ----- getAllRoomTypes tests -----
    @Test
    void getAllRoomTypes_success() {
        when(roomRepository.findDistinctRoomTypes()).thenReturn(List.of("Single", "Double"));

        List<String> types = roomQueryService.getAllRoomTypes();

        assertEquals(2, types.size());
        assertTrue(types.contains("Single"));
        verify(roomRepository, times(1)).findDistinctRoomTypes();
    }

    // ----- getRoomById tests -----
    @Test
    void getRoomById_success() {
        Room room = new Room();
        room.setId(1L);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Response response = roomQueryService.getRoomById(1L);

        assertEquals(200, response.getStatusCode());
        assertEquals("successful", response.getMessage());
        assertNotNull(response.getRoom());
    }

    @Test
    void getRoomById_notFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = roomQueryService.getRoomById(1L);

        assertEquals(404, response.getStatusCode());
        assertEquals("Room not found!", response.getMessage());
    }

    @Test
    void getRoomById_exception() {
        when(roomRepository.findById(1L)).thenThrow(new RuntimeException("DB error"));

        Response response = roomQueryService.getRoomById(1L);

        assertEquals(500, response.getStatusCode());
        assertTrue(response.getMessage().contains("Error getting room by id"));
    }

    // ----- getAllAvailableRooms tests -----
    @Test
    void getAllAvailableRooms_success() {
        Room room = new Room();
        when(roomRepository.getAllAvailableRooms()).thenReturn(List.of(room));

        Response response = roomQueryService.getAllAvailableRooms();

        assertEquals(200, response.getStatusCode());
        assertEquals("successful", response.getMessage());
        assertNotNull(response.getRoomList());
    }

    @Test
    void getAllAvailableRooms_exception() {
        when(roomRepository.getAllAvailableRooms()).thenThrow(new RuntimeException("DB error"));

        Response response = roomQueryService.getAllAvailableRooms();

        assertEquals(500, response.getStatusCode());
        assertTrue(response.getMessage().contains("Error getting all available rooms"));
    }

    // ----- getAvailableRoomsByDataAndType tests -----
    @Test
    void getAvailableRoomsByDataAndType_success() {
        Room room = new Room();
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = LocalDate.now().plusDays(2);

        when(roomRepository.findAvailableRoomsByDatesAndTypes(checkIn, checkOut, "Deluxe"))
                .thenReturn(List.of(room));

        Response response = roomQueryService.getAvailableRoomsByDataAndType(checkIn, checkOut, "Deluxe");

        assertEquals(200, response.getStatusCode());
        assertEquals("successful", response.getMessage());
        assertNotNull(response.getRoomList());
    }

    @Test
    void getAvailableRoomsByDataAndType_exception() {
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = LocalDate.now().plusDays(2);

        when(roomRepository.findAvailableRoomsByDatesAndTypes(checkIn, checkOut, "Deluxe"))
                .thenThrow(new RuntimeException("DB error"));

        Response response = roomQueryService.getAvailableRoomsByDataAndType(checkIn, checkOut, "Deluxe");

        assertEquals(500, response.getStatusCode());
        assertTrue(response.getMessage().contains("Error getting available rooms by date/type"));
    }
}
