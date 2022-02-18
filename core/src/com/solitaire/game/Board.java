package com.solitaire.game;

import java.util.ArrayList;
import java.util.Stack;

public class Board {
    private final ArrayList<ArrayList<Card>> tableau;
    private final ArrayList<ArrayList<Card>> foundation;
    private final ArrayList<Card> deck;
    private final Stack<Card> wastePile;
    private final CardManager cardManager;

    public Board() {
        this.cardManager = new CardManager();
        this.deck = cardManager.MakeCards();
        this.tableau = new ArrayList<>();
        this.foundation = new ArrayList<>();
        this.wastePile = new Stack<>();
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

    public void pickCard(Stack<Card> wastePile, ArrayList<Card> deck) {
        cardManager.pickCard(wastePile, deck);
    }

    public boolean moveToFoundation(ArrayList<ArrayList<Card>> foundation, Card card) {
        System.out.println("E");
        return cardManager.moveToFoundation(foundation, card);
    }

    public boolean moveToTableau(ArrayList<ArrayList<Card>> tableau, Card card) {
        return cardManager.moveToTableau(tableau, card);
    }

    public boolean moveWithinTableau(ArrayList<ArrayList<Card>> tableau, Card selectedCard, int selectedCardIndex, int selectedPileIndex) {
        return cardManager.moveWithinTableau(tableau, selectedCard, selectedCardIndex, selectedPileIndex);
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

    public Stack<Card> getWastePile() {
        return wastePile;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }
}
