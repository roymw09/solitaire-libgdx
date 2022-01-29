package com.solitaire.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Card {
    int value;
    String cardValue;
    String suit;
    TextureRegion frontImage;
    String cardColor;

    public Card(int value, String suit, TextureRegion frontImage) {
        this.value = value;
        this.suit = suit;
        this.frontImage = frontImage;

        if (value == 11) {
            cardValue = "Jack";
        } else if (value == 12) {
            cardValue = "Queen";
        } else if (value == 13) {
            cardValue = "King";
        } else {
            cardValue = Integer.toString(value);
        }

        if (suit.equals("Spades") || suit.equals("Clubs")) {
            this.cardColor = "black";
        } else {
            this.cardColor = "red";
        }
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCardValue() {
        return this.cardValue;
    }

    public void setCardValue(String cardValue) {
        this.cardValue = cardValue;
    }

    public String getSuit() {
        return this.suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getCardColor() {
        return cardColor;
    }
}
