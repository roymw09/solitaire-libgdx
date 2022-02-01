package com.solitaire.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;
import java.util.Random;

public class CardManager {
    Texture front_img;
    Texture back_img = new Texture("card_back.png");
    ArrayList<Card> deck = new ArrayList<Card>();
    ArrayList<ArrayList<Card>> tableCards = new ArrayList<ArrayList<Card>>();
    Sprite frontCards;
    Sprite backCards = new Sprite(back_img);

    public void MakeCards() {
        front_img = new Texture("cards_front.png");
        int x = 0;
        int y = 0;
        int width = 43;
        int height = 63;
        String[] suits = { "Spades", "Clubs", "Hearts", "Diamonds" };
        for (int i = 0; i < suits.length; i++) {
            for (int a = 1; a <= 13; a++) {
                if (a == 1) {
                    frontCards = new Sprite(front_img, 515, y, width, height);
                }
                else {
                    frontCards = new Sprite(front_img, x, y, width, height);
                    x += 43;
                }
                deck.add(new Card(a, suits[i], frontCards, backCards));
            }
            y += 62;
            x = 0;
        }
        // Shuffle function currently produces an empty deck
        //deck = shuffle(cards);
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

    // pick card from the top of the deck
    public boolean pickCard(ArrayList<Card> wastePile) {
        if (deck.size() > 0) {
            wastePile.add(deck.get(0));
            deck.remove(0);
            return true;
        }
        return false;
    }

    public boolean moveToFoundation(ArrayList<ArrayList<Card>> foundation, Card card) {
        for (int i = 0; i < 4; i++) {
            // get the last card in the current foundation pile
            int lastCard = foundation.get(i).get(foundation.get(i).size()-1).getValue();
            // get the first card in the current foundation pile so we can compare its suit to the card being moved
            boolean suitMatches = card.getSuit().equals(foundation.get(i).get(0).getSuit());
            if (!foundation.get(i).isEmpty()) {
                // make sure the suits match and the card being moved to the foundation is in the correct order
                if (suitMatches && card.getValue() == lastCard+1) {
                    foundation.get(i).add(card);
                    return true;
                }
            }
            // if the card is an ace, add it to the empty foundation pile
            if (card.getValue() == 1) {
                foundation.get(i).add(card);
                return true;
            }
        }

        return false;
    }

    public ArrayList<Card> GetDeck() {
        return deck;
    }
}