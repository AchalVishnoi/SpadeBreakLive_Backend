package com.org.SpadeBreak.controller;

import com.org.SpadeBreak.components.otherComponents.MessageType;
import com.org.SpadeBreak.model.JoinRoomResponse;
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
    public ResponseEntity<?> createRoom(
            @RequestParam String name,
            @RequestParam String nickName,
            @RequestParam String avatar) {

        try{

            Player host = new Player("1", nickName, true,avatar);
            JoinRoomResponse joinRoomResponse = roomService.createRoom(name,5, host);
            return ResponseEntity.ok(joinRoomResponse);

        } catch (IllegalStateException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }


    @GetMapping("/{roomId}")
    public ResponseEntity<Room> getRoom(@PathVariable String roomId) {
        Room r = roomService.getRoom(roomId);
        if (r == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(r);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId, @RequestParam String nickName,@RequestParam String avatar){
        try {

            JoinRoomResponse joinRoomResponse = roomService.joinRoom(roomId, nickName,avatar);

            broadcaster.broadcastRoomState(joinRoomResponse.getRoom(), MessageType.PLAYER_JOINED);

            return ResponseEntity.ok(joinRoomResponse);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("reconnect/{reconnectToken}")
    public ResponseEntity<?> reconnect(@PathVariable String reconnectToken){

        try{
            JoinRoomResponse joinRoomResponse = roomService.reconnect(reconnectToken);
            return ResponseEntity.ok(joinRoomResponse);
        }catch (IllegalStateException e){
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
