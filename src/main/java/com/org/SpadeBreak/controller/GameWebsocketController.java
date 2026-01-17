package com.org.SpadeBreak.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.SpadeBreak.model.GameMessage;
import com.org.SpadeBreak.service.GameService;
import com.org.SpadeBreak.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameWebsocketController {

    @Autowired
    private  SimpMessagingTemplate messagingTemplate;
    @Autowired
    private GameService gameService;
    @Autowired
    private RoomService roomService;

    private final ObjectMapper objectMapper=new ObjectMapper();

    @MessageMapping("/rooms/{roomId}/action")
    public void onAction(@DestinationVariable String roomId, @Payload GameMessage message){

    }



}
