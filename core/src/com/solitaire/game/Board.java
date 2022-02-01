package com.solitaire.game;

import java.util.ArrayList;

public class Board {
    private final ArrayList<ArrayList<Card>> tableau;
    private final ArrayList<ArrayList<Card>> foundation;
    private final ArrayList<Card> deck;
    private final ArrayList<Card> wastePile;
    private final CardManager cardManager;

    public Board() {
        this.cardManager = new CardManager();
        this.deck = cardManager.MakeCards();
        this.tableau = new ArrayList<>();
        this.foundation = new ArrayList<>();
        this.wastePile = new ArrayList<>();
    }

    private void initTableau() {
        for (int i = 0; i < 7; i++){
            tableau.add(new ArrayList<Card>());
            for(int a = 0; a < (i+1); a++){
                Card card = deck.get(a);
                tableau.get(i).add(card);
                deck.remove(card);
            }
        }
        System.out.println(deck.size());
    }

    private void initFoundation() {
        for (int i = 0; i < 4; i++) {
            foundation.add(new ArrayList<Card>());
        }
    }

    public void pickCard(ArrayList<Card> wastePile, ArrayList<Card> deck) {
        cardManager.pickCard(wastePile, deck);
    }

    public void initBoard() {
        initTableau();
        initFoundation();
    }

    public ArrayList<ArrayList<Card>> getTableau() {
        return tableau;
    }

    public ArrayList<ArrayList<Card>> getFoundation() {
        return foundation;
    }

    public ArrayList<Card> getWastePile() {
        return wastePile;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }
}
