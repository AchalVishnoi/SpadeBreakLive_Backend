package com.org.SpadeBreak.components.otherComponents;

public enum MessageType {

    //fool room state based message type
    ROOM_STATE,
    PLAYER_JOINED,
    PLAYER_LEFT,
    PLAYER_IS_READY,
    NEW_ROUND_STARTED,
    START,
    BET,
    PLAY_CARD,
    ROUND_SCORE_UPDATED,
    GAME_SCORE_UPDATED,
    GAME_COMPLETED,
    ROUND_COMPLETED,

    //for chats and reactions
    TEXT_MESSAGE,
    REACTION
}
