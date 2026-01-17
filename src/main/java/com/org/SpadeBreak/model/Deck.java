package com.org.SpadeBreak.model;


import com.org.SpadeBreak.components.cardComponent.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Deck {
    private final List<String> cards;

    public Deck(){
        this.cards= Arrays.stream(Card.values())
                        .map(Card::getId)
                        .collect(Collectors.toCollection(ArrayList::new));
        shuffle();
        shuffle();
        shuffle();
        shuffle();
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public String draw() throws IllegalStateException{
        if (cards.isEmpty()) throw new IllegalStateException("Deck is empty!");
        return cards.remove(cards.size() - 1);
    }

    public List<String> deal(int count) throws IllegalStateException {
        if (cards.size() < count) throw new IllegalStateException("Not enough cards!");
        List<String> hand = new ArrayList<>(cards.subList(cards.size() - count, cards.size()));
        for (int i = 0; i < count; i++) cards.remove(cards.size() - 1);
        return hand;
    }

    public int remaining() {
        return cards.size();
    }

}
