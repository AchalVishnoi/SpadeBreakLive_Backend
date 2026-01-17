package com.org.SpadeBreak.components.cardComponent;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Card {
    CLUBS_2("2_of_clubs", Suit.CLUBS, Rank.TWO),
    CLUBS_3("3_of_clubs", Suit.CLUBS, Rank.THREE),
    CLUBS_4("4_of_clubs", Suit.CLUBS, Rank.FOUR),
    CLUBS_5("5_of_clubs", Suit.CLUBS, Rank.FIVE),
    CLUBS_6("6_of_clubs", Suit.CLUBS, Rank.SIX),
    CLUBS_7("7_of_clubs", Suit.CLUBS, Rank.SEVEN),
    CLUBS_8("8_of_clubs", Suit.CLUBS, Rank.EIGHT),
    CLUBS_9("9_of_clubs", Suit.CLUBS, Rank.NINE),
    CLUBS_10("10_of_clubs", Suit.CLUBS, Rank.TEN),
    CLUBS_J("jack_of_clubs", Suit.CLUBS, Rank.JACK),
    CLUBS_Q("queen_of_clubs", Suit.CLUBS, Rank.QUEEN),
    CLUBS_K("king_of_clubs", Suit.CLUBS, Rank.KING),
    CLUBS_A("ace_of_clubs", Suit.CLUBS, Rank.ACE),

    DIAMONDS_2("2_of_diamonds", Suit.DIAMONDS, Rank.TWO),
    DIAMONDS_3("3_of_diamonds", Suit.DIAMONDS, Rank.THREE),
    DIAMONDS_4("4_of_diamonds", Suit.DIAMONDS, Rank.FOUR),
    DIAMONDS_5("5_of_diamonds", Suit.DIAMONDS, Rank.FIVE),
    DIAMONDS_6("6_of_diamonds", Suit.DIAMONDS, Rank.SIX),
    DIAMONDS_7("7_of_diamonds", Suit.DIAMONDS, Rank.SEVEN),
    DIAMONDS_8("8_of_diamonds", Suit.DIAMONDS, Rank.EIGHT),
    DIAMONDS_9("9_of_diamonds", Suit.DIAMONDS, Rank.NINE),
    DIAMONDS_10("10_of_diamonds", Suit.DIAMONDS, Rank.TEN),
    DIAMONDS_J("jack_of_diamonds", Suit.DIAMONDS, Rank.JACK),
    DIAMONDS_Q("queen_of_diamonds", Suit.DIAMONDS, Rank.QUEEN),
    DIAMONDS_K("king_of_diamonds", Suit.DIAMONDS, Rank.KING),
    DIAMONDS_A("ace_of_diamonds", Suit.DIAMONDS, Rank.ACE),

    HEARTS_2("2_of_hearts", Suit.HEARTS, Rank.TWO),
    HEARTS_3("3_of_hearts", Suit.HEARTS, Rank.THREE),
    HEARTS_4("4_of_hearts", Suit.HEARTS, Rank.FOUR),
    HEARTS_5("5_of_hearts", Suit.HEARTS, Rank.FIVE),
    HEARTS_6("6_of_hearts", Suit.HEARTS, Rank.SIX),
    HEARTS_7("7_of_hearts", Suit.HEARTS, Rank.SEVEN),
    HEARTS_8("8_of_hearts", Suit.HEARTS, Rank.EIGHT),
    HEARTS_9("9_of_hearts", Suit.HEARTS, Rank.NINE),
    HEARTS_10("10_of_hearts", Suit.HEARTS, Rank.TEN),
    HEARTS_J("jack_of_hearts", Suit.HEARTS, Rank.JACK),
    HEARTS_Q("queen_of_hearts", Suit.HEARTS, Rank.QUEEN),
    HEARTS_K("king_of_hearts", Suit.HEARTS, Rank.KING),
    HEARTS_A("ace_of_hearts", Suit.HEARTS, Rank.ACE),

    SPADES_2("2_of_spades", Suit.SPADES, Rank.TWO),
    SPADES_3("3_of_spades", Suit.SPADES, Rank.THREE),
    SPADES_4("4_of_spades", Suit.SPADES, Rank.FOUR),
    SPADES_5("5_of_spades", Suit.SPADES, Rank.FIVE),
    SPADES_6("6_of_spades", Suit.SPADES, Rank.SIX),
    SPADES_7("7_of_spades", Suit.SPADES, Rank.SEVEN),
    SPADES_8("8_of_spades", Suit.SPADES, Rank.EIGHT),
    SPADES_9("9_of_spades", Suit.SPADES, Rank.NINE),
    SPADES_10("10_of_spades", Suit.SPADES, Rank.TEN),
    SPADES_J("jack_of_spades", Suit.SPADES, Rank.JACK),
    SPADES_Q("queen_of_spades", Suit.SPADES, Rank.QUEEN),
    SPADES_K("king_of_spades", Suit.SPADES, Rank.KING),
    SPADES_A("ace_of_spades", Suit.SPADES, Rank.ACE);

    private final String id;
    private final Suit suit;
    private final Rank rank;

    Card(String id, Suit suit, Rank rank) {
        this.id = id;
        this.suit = suit;
        this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    private static final Map<String,Card> ById= Arrays.stream(values())
            .collect(Collectors.toMap(Card::getId,c->c));

    public static Card findCardById(String id){
        return  ById.get(id);
    }
}

