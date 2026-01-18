package com.org.SpadeBreak.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.SpadeBreak.components.otherComponents.MessageType;
import com.org.SpadeBreak.model.GameMessage;
import com.org.SpadeBreak.model.Player;
import com.org.SpadeBreak.model.Room;
import com.org.SpadeBreak.service.GameService;
import com.org.SpadeBreak.service.RoomService;
import com.org.SpadeBreak.service.RoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GameWebsocketController {

    @Autowired
    private  SimpMessagingTemplate messagingTemplate;
    @Autowired
    private GameService gameService;
    @Autowired
    private RoomService roomService;

    @Autowired
    private RoundService roundService;

    @Autowired
    private GameWebsocketBroadcaster broadcaster;

    private final ObjectMapper objectMapper=new ObjectMapper();

    @MessageMapping("/game/ready")
    public void playerReady(GameMessage <Void> msg){
        Room room=roomService.playerIsReady(
                msg.getPlayerId(),
                msg.getRoomId()
        );

        List<Player> playes=room.getPlayers();

        if(room!=null&&
                room.getPlayers().size()==4&&
                playes.get(0).isReady()&&
                playes.get(1).isReady()&&
                playes.get(2).isReady()&&
                playes.get(3).isReady()){
            gameService.startNewGame(room.getId());
        }
    }

    @MessageMapping("/game/startgame")
    public void startNewGame(GameMessage<Void> msg){
        Room room=roomService.getRoom(msg.getRoomId());
        List<Player> playes=room.getPlayers();

        if(room!=null&&
                room.getPlayers().size()==4&&
                playes.get(0).isReady()&&
                playes.get(1).isReady()&&
                playes.get(2).isReady()&&
                playes.get(3).isReady()){
            gameService.startNewGame(room.getId());
        }

    }

    @MessageMapping("/game/placeBet")
    public void placeBet(GameMessage<Integer> msg){
        Room room = roundService.updatePlayRoundScoreBet(
                msg.getRoomId(),
                msg.getPlayerId(),
                msg.getPlayLoad()
        );

    }

    @MessageMapping("/game/playCard")
    public void playCard(GameMessage<String> msg){
        Room room =roundService.playTrick(
                msg.getRoomId(),
                msg.getPlayerId(),
                msg.getPlayLoad()
        );
    }

    @MessageMapping("/game/chat")
    public void chatMessage(GameMessage<String> msg){
        broadcaster.broadcastMessage(msg);
    }

    @MessageMapping("/game/reaction")
    public void reaction(GameMessage<String> msg){
        broadcaster.broadcastMessage(msg);
    }







}
