package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.Room;
import com.bookmystay.app.repository.RoomRepository;
import com.bookmystay.app.service.AwsS3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomManagementServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private AwsS3Service awsS3Service;

    @InjectMocks
    private RoomManagementService roomManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----- addNewRoom tests -----
    @Test
    void addNewRoom_success() {
        MultipartFile photo = mock(MultipartFile.class);
        when(awsS3Service.saveImageToS3(photo)).thenReturn("url-to-image");

        Room room = new Room();
        room.setId(1L);
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Response response = roomManagementService.addNewRoom(photo, "Deluxe", BigDecimal.valueOf(100), "Nice room");

        assertEquals(200, response.getStatusCode());
        assertEquals("successful", response.getMessage());
        assertNotNull(response.getRoom());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void addNewRoom_exception(){
        MultipartFile photo = mock(MultipartFile.class);
        when(awsS3Service.saveImageToS3(photo)).thenThrow(new RuntimeException("S3 error"));

        Response response = roomManagementService.addNewRoom(photo, "Deluxe", BigDecimal.valueOf(100), "Nice room");

        assertEquals(500, response.getStatusCode());
        assertTrue(response.getMessage().contains("Error adding new room"));
    }

    // ----- updateRoom tests -----
    @Test
    void updateRoom_success_withNewPhoto() {
        MultipartFile photo = mock(MultipartFile.class);
        when(photo.isEmpty()).thenReturn(false);
        when(awsS3Service.saveImageToS3(photo)).thenReturn("new-url");

        Room existingRoom = new Room();
        existingRoom.setId(1L);
        existingRoom.setRoomPhotoUrl("old-url");
        when(roomRepository.findById(1L)).thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(any(Room.class))).thenReturn(existingRoom);

        Response response = roomManagementService.updateRoom(1L, "Updated desc", "Suite", BigDecimal.valueOf(150), photo);

        assertEquals(200, response.getStatusCode());
        assertEquals("successful", response.getMessage());
        assertEquals("new-url", existingRoom.getRoomPhotoUrl());
        verify(roomRepository, times(1)).save(existingRoom);
    }

    @Test
    void updateRoom_notFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = roomManagementService.updateRoom(1L, "Updated desc", "Suite", BigDecimal.valueOf(150), null);

        assertEquals(404, response.getStatusCode());
        assertEquals("Room not found!", response.getMessage());
    }

    // ----- deleteRoom tests -----
    @Test
    void deleteRoom_success() {
        Room room = new Room();
        room.setId(1L);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Response response = roomManagementService.deleteRoom(1L);

        assertEquals(200, response.getStatusCode());
        assertEquals("successful", response.getMessage());
        verify(roomRepository, times(1)).delete(room);
    }

    @Test
    void deleteRoom_notFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = roomManagementService.deleteRoom(1L);

        assertEquals(404, response.getStatusCode());
        assertEquals("Room not found!", response.getMessage());
    }
}
