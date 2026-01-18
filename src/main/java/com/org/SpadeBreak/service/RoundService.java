package com.org.SpadeBreak.service;

import com.org.SpadeBreak.components.cardComponent.Card;
import com.org.SpadeBreak.components.cardComponent.Suit;
import com.org.SpadeBreak.components.otherComponents.MessageType;
import com.org.SpadeBreak.components.otherComponents.Status;
import com.org.SpadeBreak.controller.GameWebsocketBroadcaster;
import com.org.SpadeBreak.controller.GameWebsocketController;
import com.org.SpadeBreak.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static reactor.core.publisher.Mono.delay;

@Service
public class RoundService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private GameWebsocketBroadcaster broadcaster;

    @Autowired
    private GameService gameService;


    public Room updatePlayRoundScoreBet(String roomId,String playerId,int bet){
        Room room = roomService.getRoom(roomId);
        if(room==null) throw  new IllegalStateException("Room not found!!");
        Game game=room.getGame();
        RoundState rs=game.getRoundState();
        Map<String, PlayerRoundScore> playRoundScore=rs.getScore();
        PlayerRoundScore ps=new PlayerRoundScore();
        ps.setBet(bet);
        playRoundScore.put(playerId,ps);

        roomService.saveRoom(room);

        broadcaster.broadcastRoomState(room,MessageType.BET);

        return  room;
    }

    public Room playTrick(String roomId,String playerId,String cardId){
        Room room = roomService.getRoom(roomId);
        if(room==null) throw  new IllegalStateException("Room not found!!");
        Game game=room.getGame();
        if(game==null) throw new IllegalStateException("Game not started yet!!");

        if(room.getPlayers().size()<4){
            room.setStatus(Status.OPEN);
            room.setGame(null);
            roomService.saveRoom(room);
            throw new IllegalStateException("players are not full");
        }


        RoundState rs=game.getRoundState();
        Map<String,String> centerPlayedCard=rs.getCenterTrickedCard();

        if (centerPlayedCard.isEmpty()) {
            rs.setTrickLeaderId(playerId);
        } else if (rs.getTrickLeaderId().equals(playerId)) {
            throw new IllegalStateException("Trick leader already played");
        }
        else if(!rs.getPlayerTurn().equals(playerId)){
            throw new IllegalStateException("Its not your turn!!");
        }


        List<Player> players= room.getPlayers();
        List<String> playerIds= players.stream()
                .map(Player::getId)
                .toList();

        int idx=playerIds.indexOf(playerId);
        if(idx==-1) throw new IllegalStateException("player with playerId not found");

        List<String> playerHandCards=rs.getHandCards().get(playerId);
        playerHandCards.remove(cardId);
        centerPlayedCard.put(playerId,cardId);

        String nextPlayerId= playerIds.get((idx+1)%4);

        rs.setPlayerTurn(nextPlayerId);







        roomService.saveRoom(room);

        broadcaster.broadcastRoomStatePlayCard(playerId,room);

        if(centerPlayedCard.size()==4){
            String winnerId=decideTrickWinner(rs);
            System.out.println("Winner: "+winnerId);
            updatePlayRoundScorePoints(room,winnerId);

            rs.setPlayerTurn(winnerId);

            centerPlayedCard.clear();
            roomService.saveRoom(room);
            delay(Duration.ofSeconds(2));
            broadcaster.broadcastRoomState(room,MessageType.ROUND_SCORE_UPDATED);

            if(playerHandCards.isEmpty()){
                gameService.updateGameScore(roomId,rs.getScore());
            }
        }


        return room;
    }

    public Room updatePlayRoundScorePoints(Room room,String playerId){
        if(room==null) throw  new IllegalStateException("Room not found!!");
        Game game=room.getGame();
        RoundState rs=game.getRoundState();
        Map<String, PlayerRoundScore> score=rs.getScore();
        PlayerRoundScore playerRoundScore=score.get(playerId);
        playerRoundScore.setCurrPoints(playerRoundScore.getCurrPoints()+1);
        score.put(playerId,playerRoundScore);
        System.out.println("Winner score updated: "+playerId+" to "+ playerRoundScore.getCurrPoints() );

        roomService.saveRoom(room);
        return room;
    }

    public String decideTrickWinner(RoundState rs){

        Map<String,String> centerPlayedCard=rs.getCenterTrickedCard();

        if(rs.getTrickLeaderId()==null) throw new IllegalStateException("no Trick Leader found");

        String winningId= rs.getTrickLeaderId();
        Card winningCard= Card.findCardById(centerPlayedCard.get(winningId));
        Suit trickSuit= winningCard.getSuit();

        for(Map.Entry<String,String> entry:centerPlayedCard.entrySet()){

            Card card=Card.findCardById(entry.getValue());
            String playerId=entry.getKey();

            boolean cardIsTrump= card.getSuit()==Suit.SPADES;
            boolean winningCardIsTrump= winningCard.getSuit()==Suit.SPADES;

            if(cardIsTrump&&!winningCardIsTrump){
                winningCard=card;
                winningId=playerId;
            }
            else if(cardIsTrump&&winningCardIsTrump&&(card.getRank().getValue()>winningCard.getRank().getValue())){
                winningCard=card;
                winningId=playerId;
            }
            else if (!cardIsTrump &&
                    !winningCardIsTrump &&
                    card.getSuit()==trickSuit &&
                    winningCard.getSuit()==trickSuit &&
                    card.getRank().getValue()>winningCard.getRank().getValue()) {
                winningCard=card;
                winningId=playerId;
            }





        }


        return winningId;




    }


}
