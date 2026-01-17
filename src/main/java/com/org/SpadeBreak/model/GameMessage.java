package com.org.SpadeBreak.model;


import lombok.Data;

@Data
public class GameMessage {
    private String type ;
    private String playerId;
    private Object playLoad;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Object getPlayLoad() {
        return playLoad;
    }

    public void setPlayLoad(Object playLoad) {
        this.playLoad = playLoad;
    }
}
