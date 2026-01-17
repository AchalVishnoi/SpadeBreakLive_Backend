package com.org.SpadeBreak.model;
import lombok.Data;

@Data
public class PlayerRoundScore {
    private int bet;
    private int currPoints;

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getCurrPoints() {
        return currPoints;
    }

    public void setCurrPoints(int currPoints) {
        this.currPoints = currPoints;
    }
}
