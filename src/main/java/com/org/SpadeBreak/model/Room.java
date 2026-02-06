package com.org.SpadeBreak.model;

import com.org.SpadeBreak.components.otherComponents.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
public class Room {

    private String id;
    private String name;
    private Status status;
    private List<Player> players= new ArrayList<>();
    private Game game= null;

    public Room(String id, String name, Status status, List<Player> players) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.players = players;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}