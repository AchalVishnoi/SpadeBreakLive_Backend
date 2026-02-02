package com.org.SpadeBreak.controller;

import com.org.SpadeBreak.service.GameService;
import com.org.SpadeBreak.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class GameController {


        @Autowired
        private RoomService roomService;

        @Autowired
        private GameService gameService;


        @Autowired
        private GameWebsocketBroadcaster broadcaster;




}
