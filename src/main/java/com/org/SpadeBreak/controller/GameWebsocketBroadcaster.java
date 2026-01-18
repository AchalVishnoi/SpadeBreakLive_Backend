package com.org.SpadeBreak.controller;


import com.org.SpadeBreak.components.otherComponents.MessageType;
import com.org.SpadeBreak.model.GameMessage;
import com.org.SpadeBreak.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class GameWebsocketBroadcaster {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void broadcastRoomState(Room room,MessageType type){

        messagingTemplate.convertAndSend(
                "/topic/room/"+room.getId(),
                new GameMessage<>(
                        type,
                        null,
                        room.getId(),
                        room,
                        System.currentTimeMillis()
                )


        );

    }

    public void playerJoinedRoomStateBroadCast(String playerId,Room room){
        messagingTemplate.convertAndSend(
                "/topic/room/"+room.getId(),
                new GameMessage<>(
                        MessageType.PLAYER_JOINED,
                        playerId,
                        room.getId(),
                        room,
                        System.currentTimeMillis()
                )


        );
    }


    public void playerLeftRoomStateBroadCast(String playerId,Room room){
        messagingTemplate.convertAndSend(
                "/topic/room/"+room.getId(),
                new GameMessage<>(
                        MessageType.PLAYER_LEFT,
                        playerId,
                        room.getId(),
                        room,
                        System.currentTimeMillis()
                )


        );
    }

    public void broadcastRoomStatePlayCard(String playerId,Room room){

        messagingTemplate.convertAndSend(
                "/topic/room/"+room.getId(),
                new GameMessage<Room>(
                        MessageType.PLAY_CARD,
                        playerId,
                        room.getId(),
                        room,
                        System.currentTimeMillis()
                )
        );

    }

    public void broadcastMessage(GameMessage<?> msg){
        messagingTemplate.convertAndSend(
                "/topic/room/"+msg.getRoomId(),
                msg
        );
    }
}
