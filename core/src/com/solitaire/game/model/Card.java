package com.solitaire.game.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Card {
    private int value;
    private String cardValue;
    private String suit;
    private final String cardColor;
    private boolean faceUp;
    private Sprite frontImage;
    private Sprite backImage;

    public Card(int value, String suit, Sprite frontImage, Sprite backImage) {
        this.value = value;
        this.suit = suit;
        this.frontImage = frontImage;
        this.backImage = backImage;
        this.faceUp = true;

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
        } else if (suit.equals("Hearts") || suit.equals("Diamonds")) {
            this.cardColor = "red";
        } else {
            this.cardColor = null;
        }
    }

    public void draw(SpriteBatch batch, int x, int y) {
        if (faceUp) {
            frontImage.setPosition(x, y);
            frontImage.draw(batch);
            frontImage.setSize(61, 79);
        } else {
            backImage.setPosition(x, y);
            backImage.setSize(61, 79);
            backImage.draw(batch);
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

    public void setFaceUp(boolean bool) {
        this.faceUp = bool;
    }

    public boolean getFaceUp() {
        return faceUp;
    }

    public Sprite getFrontImage(){
        return this.frontImage;
    }

    public void setFrontImage(Sprite frontImage){
        this.frontImage = frontImage;
    }

    public Sprite getBackImage(){
        return this.backImage;
    }

    public void setBackImage(Sprite backImage){
        this.backImage = backImage;
    }
}
