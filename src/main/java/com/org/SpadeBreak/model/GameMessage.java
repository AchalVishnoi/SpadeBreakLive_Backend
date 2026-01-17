package com.org.SpadeBreak.model;


import com.org.SpadeBreak.components.otherComponents.MessageType;
import lombok.Data;

@Data
public class GameMessage<T> {
    private MessageType type ;
    private String playerId;
    private String roomId;

    private T playLoad;
    private long timeStamp;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public T getPlayLoad() {
        return playLoad;
    }

    public void setPlayLoad(T playLoad) {
        this.playLoad = playLoad;
    }
}
