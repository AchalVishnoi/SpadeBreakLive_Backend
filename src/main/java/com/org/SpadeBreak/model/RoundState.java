package com.org.SpadeBreak.model;


import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RoundState {
     private int trickNum=0;
     private Map<String,PlayerRoundScore> score=new HashMap<>();
     private Map<String, List<String>> handCards=new HashMap<>();
     private Map<String,String> centerTrickedCard=new HashMap<>();
     private String playerTurn;
     private String trickLeaderId;

    public String getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(String playerTurn) {
        this.playerTurn = playerTurn;
    }

    public int getTrickNum() {
        return trickNum;
    }

    public void setTrickNum(int trickNum) {
        this.trickNum = trickNum;
    }

    public Map<String, PlayerRoundScore> getScore() {
        return score;
    }

    public void setScore(Map<String, PlayerRoundScore> score) {
        this.score = score;
    }

    public Map<String, List<String>> getHandCards() {
        return handCards;
    }

    public void setHandCards(Map<String, List<String>> handCards) {
        this.handCards = handCards;
    }

    public Map<String, String> getCenterTrickedCard() {
        return centerTrickedCard;
    }

    public void setCenterTrickedCard(Map<String, String> centerTrickedCard) {
        this.centerTrickedCard = centerTrickedCard;
    }


    public String getTrickLeaderId() {
        return trickLeaderId;
    }

    public void setTrickLeaderId(String trickLeaderId) {
        this.trickLeaderId = trickLeaderId;
    }
}
