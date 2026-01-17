package com.org.SpadeBreak.service;


import com.org.SpadeBreak.model.Game;
import com.org.SpadeBreak.model.PlayerRoundScore;
import com.org.SpadeBreak.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class GameService {

    @Autowired
    private RoomService roomService;

    public Room startNewGame(String roomId){


        Room room=roomService.getRoom(roomId);
        if(room==null) throw new IllegalStateException("Room not found");
        room.setGame(new Game());
        roomService.saveRoom(room);

        return room;
    }

    public Room updateGameScore(String roomId, Map<String, PlayerRoundScore> roundScore){
        Room room=roomService.getRoom(roomId);
        if(room==null) throw new IllegalStateException("Room not found");

        Game game=room.getGame();
        if(game==null) throw  new IllegalStateException("Game not started yet!");

        Map<String,Double> scoreCard = game.getScore();
        for(Map.Entry<String,PlayerRoundScore> score:roundScore.entrySet()){
            String playerId=score.getKey();
            PlayerRoundScore rs=score.getValue();
            Double finalScore;
            if(rs.getBet()<=rs.getCurrPoints())
                finalScore= ((double)rs.getCurrPoints()-(double) rs.getBet())/10+rs.getBet();
            else finalScore= -1*(double)rs.getBet();

            scoreCard.put(playerId,scoreCard.getOrDefault(playerId,0.0)+finalScore);
        }

        roomService.saveRoom(room);

        return room;
    }












}
