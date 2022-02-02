package com.solitaire.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;
import java.util.Collections;

public class CardManager {
    Texture front_img;
    Texture back_img = new Texture("card_back.png");
    ArrayList<ArrayList<Card>> tableCards = new ArrayList<ArrayList<Card>>();
    Sprite frontCards;
    Sprite backCards = new Sprite(back_img);
    ArrayList<Card> deck = new ArrayList<Card>();

    public ArrayList<Card> MakeCards() {
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
        Collections.shuffle(deck);
        return deck;
    }

    // pick the first card from the deck
    public boolean pickCard(ArrayList<Card> wastePile, ArrayList<Card> deck) {
        if (deck.size() > 0) {
            Card card = deck.get(0);
            card.setFaceUp(true);
            deck.remove(card);
            wastePile.add(card);
            return true;
        }
        return false;
    }

    public boolean moveToFoundation(ArrayList<ArrayList<Card>> foundation, Card card) {
        for (int i = 0; i < 4; i++) {
            if (!foundation.get(i).isEmpty()) {
                // get the last card in the current foundation pile
                int lastCard = foundation.get(i).get(foundation.get(i).size()-1).getValue();
                // get the first card in the current foundation pile so we can compare its suit to the card being moved
                boolean suitMatches = card.getSuit().equals(foundation.get(i).get(0).getSuit());
                // make sure the suits match and the card being moved to the foundation is in the correct order
                if (suitMatches && card.getValue() == lastCard+1) {
                    foundation.get(i).add(card);
                    return true;
                }
            }
            // if the card is an ace, add it to the empty foundation pile
            else if (card.getValue() == 1) {
                foundation.get(i).add(card);
                return true;
            }
        }

        return false;
    }

    public boolean moveToTableau(ArrayList<ArrayList<Card>> tableau, Card card) {
        for (int i = 0; i < 7; i++) {
            if (!tableau.get(i).isEmpty()) {
                // get the last card in the current tableau pile
                int lastCard = tableau.get(i).get(tableau.get(i).size()-1).getValue();
                // get the first card in the current tableau pile so we can compare its colours
                boolean matchColour = card.getCardColor().equals(tableau.get(i).get(tableau.get(i).size()-1).getCardColor());
                // make sure the colours are different and the card being moved to the tableau is in the correct order
                if (!matchColour && card.getValue() == lastCard-1) {
                    tableau.get(i).add(card);
                    return true;
                }
            }             // if the card is a king, add it to the empty tableau pile if there is one
            else if (card.getValue() == 13) {
                tableau.get(i).add(card);
                return true;
            }
        }
        return false;
    }
}