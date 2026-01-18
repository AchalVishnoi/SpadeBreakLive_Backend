package com.org.SpadeBreak.service;

import com.org.SpadeBreak.components.otherComponents.MessageType;
import com.org.SpadeBreak.components.otherComponents.Status;
import com.org.SpadeBreak.controller.GameWebsocketBroadcaster;
import com.org.SpadeBreak.model.Game;
import com.org.SpadeBreak.model.Player;
import com.org.SpadeBreak.model.Room;
import com.org.SpadeBreak.model.RoundState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;



@Service
public class RoomService {


    @Autowired
    private  RedisTemplate<String,Object> redisTemplate;
    private final Duration ROOM_TTL = Duration.ofMinutes(30);

    @Autowired
    private GameWebsocketBroadcaster broadcaster;


    private String key(String roomId) {
        return "room:" + roomId;
    }

    public Room createRoom(String name, int rounds, Player host) {
        String id = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Room room = new Room(
                id,
                name,
                Status.OPEN,
                new ArrayList<>()
        );

        host.setHost(true);
        room.getPlayers().add(host);

        redisTemplate.opsForValue().set(key(id), room, ROOM_TTL);
        return room;
    }

    public Room getRoom(String id) {
        return (Room) redisTemplate.opsForValue().get(key(id));
    }

    public Room joinRoom(String roomId, String nickName){

        Room room = getRoom(roomId);
        if(room==null) throw new IllegalStateException("Room not found");
        if (room.getPlayers().size() >= 4) throw new IllegalStateException("Room full");

        List<Player> players=room.getPlayers();

        int playerId = Integer.parseInt(players.get(players.size()-1).getId())+1;
        Player player =new Player(String.valueOf(playerId),nickName,false);
        players.add(player);
        if(players.size()==4){
            room.setStatus(Status.READY);
        }

        saveRoom(room);

        return room;

    }

    public void saveRoom(Room room){
        redisTemplate.opsForValue().set(key(room.getId()),room,ROOM_TTL);
    }

    public void deleteRoom(String roomId) {
        redisTemplate.delete(key(roomId));
    }

    public Room leaveRoom(String roomId, String playerId) {
        Room room = getRoom(roomId);
        if (room == null) throw new IllegalStateException("room not exist!!");

        room.getPlayers().removeIf(p -> p.getId().equals(playerId));


        if(room.getPlayers().size()<4){
            room.setStatus(Status.OPEN);
            room.setGame(null);
        }

        if (room.getPlayers().isEmpty()) {
            deleteRoom(roomId);
        } else {
            saveRoom(room);
        }

        return room;
    }

    public Room playerIsReady(String playerId,String roomId){

        Room room=getRoom(roomId);
        if (room == null) throw new IllegalStateException("Room not found");

        Player player=room.getPlayers().stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Player not found"));

        player.setReady(true);

        boolean allReady=room.getPlayers().stream()
                .allMatch(Player::isReady);

        saveRoom(room);

        broadcaster.broadcastRoomState(room, MessageType.PLAYER_IS_READY);
        return room;
    }

}
