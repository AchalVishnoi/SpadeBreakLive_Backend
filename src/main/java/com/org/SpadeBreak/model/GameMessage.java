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

    public GameMessage(MessageType type, String playerId, String roomId, T playLoad, long timeStamp) {
        this.type = type;
        this.playerId = playerId;
        this.roomId = roomId;
        this.playLoad = playLoad;
        this.timeStamp = timeStamp;
    }

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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
