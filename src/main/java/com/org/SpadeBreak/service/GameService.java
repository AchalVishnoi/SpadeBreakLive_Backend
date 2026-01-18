package com.org.SpadeBreak.service;


import com.org.SpadeBreak.components.otherComponents.MessageType;
import com.org.SpadeBreak.components.otherComponents.RoundStatus;
import com.org.SpadeBreak.components.otherComponents.Status;
import com.org.SpadeBreak.controller.GameWebsocketBroadcaster;
import com.org.SpadeBreak.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static reactor.core.publisher.Mono.delay;

@Service
public class GameService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private GameWebsocketBroadcaster broadcaster;

    public Room startNewGame(String roomId){


        Room room =roomService.getRoom(roomId);

        if(room==null) throw new IllegalStateException("room not found!!");

        List<Player> players = room.getPlayers();

        if(room.getStatus()!=Status.READY||!(
                        players.get(0).isReady()&&
                        players.get(1).isReady()&&
                        players.get(2).isReady()&&
                        players.get(3).isReady()

                )) throw  new IllegalStateException("Room is not ready!!");

        HashMap<String,Double> score =new HashMap<>();
        for(Player player:players){
            score.put(player.getId(),0.0);
        }

        Game game =new Game();
        game.setScore(score);

        room.setGame(game);

        room.setStatus(Status.IN_GAME);

        game.setRounds(5);



        roomService.saveRoom(room);

        startRound(roomId);

        return room;
    }

    public Room startRound(String roomId){

        Room room = roomService.getRoom(roomId);
        if (room == null) throw new IllegalStateException("Room not found!");

        Game game = room.getGame();
        if(game==null) throw new IllegalStateException("Game not started yet!!");

        if(room.getPlayers().size()<4){
            room.setStatus(Status.OPEN);
            room.setGame(null);
            roomService.saveRoom(room);
            throw new IllegalStateException("players are not full");
        }

        if(game.getRounds()<=0){
            broadcaster.broadcastRoomState(room,MessageType.GAME_COMPLETED);
            room.getPlayers().get(0).setReady(false);
            room.getPlayers().get(1).setReady(false);
            room.getPlayers().get(2).setReady(false);
            room.getPlayers().get(3).setReady(false);
            room.setGame(null);
            roomService.saveRoom(room);
            return room;
        }

        game.setRounds(game.getRounds()-1);

        RoundState round = new RoundState();



        Deck deck=new Deck();

        HashMap<String,List<String>> handCards=new HashMap<>();

        for(Player player:room.getPlayers()){
            handCards.put(player.getId(),deck.deal(13));
        }


        Map<String, PlayerRoundScore> score = new HashMap<>();
        for (Player p : room.getPlayers()) {
            score.put(p.getId(), new PlayerRoundScore());
        }
        round.setScore(score);
        round.setStatus(RoundStatus.BETTING);
        round.setHandCards(handCards);
        round.setPlayerTurn(room.getPlayers().get(0).getId());
        game.setRoundState(round);


        roomService.saveRoom(room);

        broadcaster.broadcastRoomState(room, MessageType.NEW_ROUND_STARTED);

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

        broadcaster.broadcastRoomState(room,MessageType.GAME_SCORE_UPDATED);

        if(game.getRounds()>0){
            startRound(roomId);
        }

        return room;
    }


}
