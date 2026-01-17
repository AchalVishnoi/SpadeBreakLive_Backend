package com.org.SpadeBreak.model;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Game {

       private Map<String,Double> score=new HashMap<>();
       private int rounds=5;
       private RoundState roundState=new RoundState();




    public Map<String, Double> getScore() {
        return score;
    }

    public void setScore(Map<String, Double> score) {
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
