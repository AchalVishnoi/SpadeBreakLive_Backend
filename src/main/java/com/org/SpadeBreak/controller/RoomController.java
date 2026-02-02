package com.org.SpadeBreak.controller;

import com.org.SpadeBreak.components.otherComponents.MessageType;
import com.org.SpadeBreak.model.JoinRoomResponse;
import com.org.SpadeBreak.model.Player;
import com.org.SpadeBreak.model.Room;
import com.org.SpadeBreak.service.GameService;
import com.org.SpadeBreak.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;


    @Autowired
    private GameWebsocketBroadcaster broadcaster;
    @Autowired
    private GameService gameService;

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

    @GetMapping("/{reconnectToken}/leave")
    public ResponseEntity<?> leaveRoom(@PathVariable String reconnectToken) {
            String arr[]= reconnectToken.split(":");
            Room room= roomService.leaveRoom(arr[0], arr[1]);

            broadcaster.playerLeftRoomStateBroadCast(arr[1],room);

            return ResponseEntity.ok().build();


    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable String roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }


        @PostMapping("/ready")
        public ResponseEntity<?> playerIsReady(
                @RequestParam String roomId,
                @RequestParam String playerId
        ){

            try{
                Room room = roomService.playerIsReady(playerId,roomId);
                broadcaster.broadcastRoomState(room, MessageType.PLAYER_IS_READY);
                List<Player> playes=room.getPlayers();

                if(room!=null&&
                        room.getPlayers().size()==4&&
                        playes.get(0).isReady()&&
                        playes.get(1).isReady()&&
                        playes.get(2).isReady()&&
                        playes.get(3).isReady()){
                    gameService.startNewGame(room.getId());}
                return ResponseEntity.ok(room);
            }
            catch (IllegalStateException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }


        }


}
