package com.solitaire.game;

import java.util.ArrayList;

public class Board {
    ArrayList<ArrayList<Card>> tableau;
    ArrayList<ArrayList<Card>> foundation;
    ArrayList<Card> deck;
    ArrayList<Card> wastePile;

    public Board(ArrayList<Card> deck) {
        this.tableau = new ArrayList<>();
        this.foundation = new ArrayList<>();
        this.wastePile = new ArrayList<>();
        this.deck = deck;
    }

    private void initTableau() {
        for (int i = 0; i < 7; i++){
            tableau.add(new ArrayList<Card>());
            for(int a = 0; a < (i+1); a++){
                tableau.get(i).add(deck.get(a));
                deck.remove(i);
            }
        }
    }

    private void initFoundation() {
        for (int i = 0; i < 4; i++) {
            foundation.add(new ArrayList<Card>());
        }
    }

    public void initBoard() {
        initTableau();
        initFoundation();
    }

    public void discard(int cardIndex) {
        wastePile.add(deck.get(cardIndex));
        deck.remove(cardIndex);
    }

    public ArrayList<ArrayList<Card>> getTableau() {
        return tableau;
    }
}
