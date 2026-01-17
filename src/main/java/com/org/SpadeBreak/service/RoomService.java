package com.org.SpadeBreak.service;

import com.org.SpadeBreak.components.otherComponents.Status;
import com.org.SpadeBreak.model.Game;
import com.org.SpadeBreak.model.Player;
import com.org.SpadeBreak.model.Room;
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

    public void leaveRoom(String roomId, String playerId) {
        Room room = getRoom(roomId);
        if (room == null) return;

        room.getPlayers().removeIf(p -> p.getId().equals(playerId));
        if(room.getPlayers().size()<4) room.setStatus(Status.OPEN);

        if (room.getPlayers().isEmpty()) {
            deleteRoom(roomId);
        } else {
            saveRoom(room);
        }
    }

    public Game startNewGame(String roomId){

        Room room =getRoom(roomId);

        List<Player> players = room.getPlayers();

        HashMap <String,Double> score =new HashMap<>();
        for(Player player:players){
            score.put(player.getId(),0.0);
        }

        Game game =new Game();
        game.setScore(score);


        return game;
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

        if(room.getPlayers().size()==4&&allReady) startNewGame(roomId);
        return room;

    }

}
