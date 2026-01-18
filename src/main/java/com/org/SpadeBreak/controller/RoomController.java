package com.org.SpadeBreak.controller;

import com.org.SpadeBreak.components.otherComponents.MessageType;
import com.org.SpadeBreak.model.Player;
import com.org.SpadeBreak.model.Room;
import com.org.SpadeBreak.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;


    @Autowired
    private GameWebsocketBroadcaster broadcaster;

    @PostMapping
    public ResponseEntity<Room> createRoom(
            @RequestParam String name,
            @RequestParam String nickName) {

        Player host = new Player("1", nickName, true);
        Room room = roomService.createRoom(name,5, host);
        return ResponseEntity.ok(room);
    }


    @GetMapping("/{roomId}")
    public ResponseEntity<Room> getRoom(@PathVariable String roomId) {
        Room r = roomService.getRoom(roomId);
        if (r == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(r);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId, @RequestParam String nickName){
        try {
            Room room = roomService.joinRoom(roomId, nickName);

            broadcaster.broadcastRoomState(room, MessageType.PLAYER_JOINED);

            return ResponseEntity.ok(room);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{roomId}/leave")
    public ResponseEntity<?> leaveRoom(@PathVariable String roomId, @RequestParam String playerId) {
        Room room= roomService.leaveRoom(roomId, playerId);

        broadcaster.playerLeftRoomStateBroadCast(playerId,room);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable String roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }


}
