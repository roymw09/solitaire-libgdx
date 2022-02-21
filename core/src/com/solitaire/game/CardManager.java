package com.solitaire.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class CardManager {
    Texture front_img;
    Texture back_img = new Texture("card_back.png");
    ArrayList<ArrayList<Card>> tableCards = new ArrayList<>();
    Sprite frontCards;
    Sprite backCards = new Sprite(back_img);
    ArrayList<Card> deck = new ArrayList<>();

    public ArrayList<Card> MakeCards() {
        front_img = new Texture("cards_front.png");
        int x = 0;
        int y = -2;
        int width = 43;
        int height = 61;
        String[] suits = { "Spades", "Clubs", "Hearts", "Diamonds" };
        for (int i = 0; i < suits.length; i++) {
            if (i==3) y-=1;
            y+=2;
            for (int a = 1; a <= 13; a++) {
                if (a == 1) {
                    frontCards = new Sprite(front_img, 515, y, width, height);
                }
                else {
                    frontCards = new Sprite(front_img, x, y, width, height);
                    x += width;
                }
                deck.add(new Card(a, suits[i], frontCards, backCards));
            }
            y += height;
            x = 0;
        }
        Collections.shuffle(deck);
        return deck;
    }

    // pick the first card from the deck
    public boolean pickCard(Stack<Card> wastePile, ArrayList<Card> deck) {
        if (deck.size() > 0) {
            Card card = deck.get(0);
            card.setFaceUp(true);
            deck.remove(card);
            wastePile.push(card);
            return true;
        } else {
            deck.addAll(wastePile);
            wastePile.clear();
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
                int lastCard = tableau.get(i).get(tableau.get(i).size() - 1).getValue();
                // get the first card in the current tableau pile so we can compare its colours
                boolean matchColour = card.getCardColor().equals(tableau.get(i).get(tableau.get(i).size() - 1).getCardColor());
                // make sure the colours are different and the card being moved to the tableau is in the correct order
                if (!matchColour && card.getValue() == lastCard - 1) {
                    tableau.get(i).add(card);
                    return true;
                }
            }
            // if the card is a king, add it to the empty tableau pile if there is one
            else if (card.getValue() == 13) {
                tableau.get(i).add(card);
                return true;
            }
        }
        return false;
    }

    /* if the colors do not match and the card being moved adhere to the logical order of the cards,
    append all selected cards to the tableau */
    public boolean moveWithinTableau(ArrayList<ArrayList<Card>> tableau, Card selectedCard, int selectedCardIndex, int selectedPileIndex) {
        ArrayList<Card> cardsToMove = getSelectedCards(tableau, selectedCardIndex, selectedPileIndex);
        for (int i = 0; i < 7; i++) {
            if (!tableau.get(i).isEmpty()) {
                int lastCard = tableau.get(i).get(tableau.get(i).size() - 1).getValue();
                boolean colourMatch = selectedCard.getCardColor().equals(tableau.get(i).get(tableau.get(i).size() - 1).getCardColor());
                if (!colourMatch && selectedCard.getValue() == lastCard-1) {
                    tableau.get(i).addAll(cardsToMove);
                    tableau.get(selectedPileIndex).removeAll(cardsToMove);
                    return true;
                }
            }
            else if (selectedCard.getValue() == 13) {
                tableau.get(i).addAll(cardsToMove);
                tableau.get(selectedPileIndex).removeAll(cardsToMove);
                return true;
            }
        }
        return false;
    }

    // get all cards stacked on top of the card that was clicked
    public ArrayList<Card> getSelectedCards(ArrayList<ArrayList<Card>> tableau, int selectedCardIndex, int selectedPileIndex) {
        ArrayList<Card> selectedCards = new ArrayList<>();
        for (int i = selectedCardIndex; i < tableau.get(selectedPileIndex).size(); i++) {
            selectedCards.add(tableau.get(selectedPileIndex).get(i));
        }
        return selectedCards;
    }
}