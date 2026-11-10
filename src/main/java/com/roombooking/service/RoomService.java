
package com.roombooking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.roombooking.entity.Room;
import com.roombooking.repository.RoomRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // Add Room
    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }

    // Get All Rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Get Room By ID
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Room not found with ID: " + id));
    }

    // Update Room
    public Room updateRoom(Long id, Room updatedRoom) {

        Room existingRoom = getRoomById(id);

        existingRoom.setRoomName(updatedRoom.getRoomName());
        existingRoom.setLocation(updatedRoom.getLocation());
        existingRoom.setCapacity(updatedRoom.getCapacity());
        existingRoom.setPricePerHour(updatedRoom.getPricePerHour());
        existingRoom.setDescription(updatedRoom.getDescription());
        existingRoom.setAvailable(updatedRoom.isAvailable());

        return roomRepository.save(existingRoom);
    }

    // Delete Room
    public void deleteRoom(Long id) {

        Room room = getRoomById(id);

        roomRepository.delete(room);
    }
}

