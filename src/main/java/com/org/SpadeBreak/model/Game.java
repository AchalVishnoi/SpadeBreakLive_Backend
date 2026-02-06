package com.org.SpadeBreak.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Game {

       private List<Map<String,PlayerRoundScore>> score=new ArrayList<>();
       private int rounds=5;
       private RoundState roundState=new RoundState();

    public List<Map<String, PlayerRoundScore>> getScore() {
        return score;
    }

    public void setScore(List<Map<String, PlayerRoundScore>> score) {
        this.score = score;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public RoundState getRoundState() {
        return roundState;
    }

    public void setRoundState(RoundState roundState) {
        this.roundState = roundState;
    }


}