package com.org.SpadeBreak.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Player {
    private String id;
    private String nickname;
    private boolean host;
    private boolean isReady;
    private String avatar;
    private String reconnectToken;

    public Player(String id, String nickname, boolean host,String avatar) {
        this.id = id;
        this.nickname = nickname;
        this.host = host;
        this.avatar=avatar;
    }

    public String getReconnectToken() {
        return reconnectToken;
    }

    public void setReconnectToken(String reconnectToken) {
        this.reconnectToken = reconnectToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
