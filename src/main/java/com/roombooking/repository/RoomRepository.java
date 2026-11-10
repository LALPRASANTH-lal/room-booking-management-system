
package com.roombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.roombooking.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

}
