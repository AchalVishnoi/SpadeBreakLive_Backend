package com.org.SpadeBreak.service;


import com.org.SpadeBreak.components.cardComponent.Card;
import com.org.SpadeBreak.components.cardComponent.Suit;
import com.org.SpadeBreak.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoundService {

    @Autowired
    private RoomService roomService;

    public Room startRound(String roomId){

        Room room = roomService.getRoom(roomId);
        if (room == null) throw new IllegalStateException("Room not found!");

        Game game = room.getGame();
        RoundState round = new RoundState();

        Deck deck=new Deck();

        HashMap<String,List<String>> handCards=new HashMap<>();

        for(Player player:room.getPlayers()){
            handCards.put(player.getId(),deck.deal(13));
        }

        round.setHandCards(handCards);
        Map<String, PlayerRoundScore> score = new HashMap<>();
        for (Player p : room.getPlayers()) {
            score.put(p.getId(), new PlayerRoundScore());
        }
        round.setScore(score);

        round.setPlayerTurn(room.getPlayers().get(0).getId());

        game.setRoundState(round);
        roomService.saveRoom(room);

        return room;
    }



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

        return  room;


    }

    public Room playTrick(String roomId,String playerId,String cardId){
        Room room = roomService.getRoom(roomId);
        if(room==null) throw  new IllegalStateException("Room not found!!");
        Game game=room.getGame();
        RoundState rs=game.getRoundState();
        Map<String,String> centerPlayedCard=rs.getCenterTrickedCard();

        if (centerPlayedCard.isEmpty()) {
            rs.setTrickLeaderId(playerId);
        } else if (rs.getTrickLeaderId().equals(playerId)) {
            throw new IllegalStateException("Trick leader already played");
        }


        centerPlayedCard.put(playerId,cardId);

        List<Player> players= room.getPlayers();
        List<String> playerIds= players.stream()
                .map(Player::getId)
                .toList();

        int idx=playerIds.indexOf(playerId);
        if(idx==-1) throw new IllegalStateException("player with playerId not found");
        String nextPlayerId= playerIds.get((idx+1)%4);

        rs.setPlayerTurn(nextPlayerId);


        if(centerPlayedCard.size()==4){

           String winnerId=decideTrickWinner(rs);
           updatePlayRoundScorePoints(roomId,winnerId);
           rs.setPlayerTurn(winnerId);
        }



        roomService.saveRoom(room);

        return room;
    }

    public Room updatePlayRoundScorePoints(String roomId,String playerId){
        Room room = roomService.getRoom(roomId);
        if(room==null) throw  new IllegalStateException("Room not found!!");
        Game game=room.getGame();
        RoundState rs=game.getRoundState();
        Map<String, PlayerRoundScore> playRoundScore=rs.getScore();
        PlayerRoundScore playerRoundScore=playRoundScore.get(playerId);
        playerRoundScore.setCurrPoints(playerRoundScore.getCurrPoints()+1);

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
