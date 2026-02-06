package com.org.SpadeBreak.model;


import lombok.Data;

@Data
public class JoinRoomResponse {
    private Room room;
    private String playerId;
    private String reconnectToken;

    public JoinRoomResponse(Room room, String playerId, String reconnectToken) {
        this.room = room;
        this.playerId = playerId;
        this.reconnectToken = reconnectToken;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getReconnectToken() {
        return reconnectToken;
    }

    public void setReconnectToken(String reconnectToken) {
        this.reconnectToken = reconnectToken;
    }
}
