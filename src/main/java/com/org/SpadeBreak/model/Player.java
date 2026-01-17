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

    public Player(String id, String nickname, boolean host) {
        this.id = id;
        this.nickname = nickname;
        this.host = host;
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
}
