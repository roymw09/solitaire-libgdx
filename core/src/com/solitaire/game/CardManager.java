package com.solitaire.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CardManager {
    Texture img;
    TextureRegion[][] frontCards;
    int cardWidth;
    int cardHeight;
    ArrayList<Card> deck = new ArrayList<Card>();

    ArrayList<ArrayList<Card>> tableCards = new ArrayList<ArrayList<Card>>();

    public void MakeCards() {
        img = new Texture("cards_front.png");
        frontCards = TextureRegion.split(img, 40, 40);
        String[] suits = { "Spades", "Hearts", "Diamonds", "Clubs" };
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < suits.length; i++) {
            for (int a = 0; a < 13; a++) {
                deck.add(new Card(a, suits[i], frontCards[i * cardHeight][i * cardWidth]));
            }
        }
        deck = shuffle(cards);
    }

    public ArrayList<Card> shuffle(ArrayList<Card> deck) {

        ArrayList<Card> copy = new ArrayList<Card>();
        Random rand = new Random();
        while (deck.size() > 0) {
            int randomInt = rand.nextInt(deck.size());
            copy.add(deck.get(randomInt));
            deck.remove(randomInt);
        }
        return copy;
    }

    public void DealCards(){
        for (int i = 0; i < 7; i++){
            tableCards.add(new ArrayList<Card>());
            for(int a = 0; a < (i+1); a++){
                tableCards.get(i).add(deck.get(i*13 + a));
                deck.remove(i*13+a);
            }
        }
    }

}

