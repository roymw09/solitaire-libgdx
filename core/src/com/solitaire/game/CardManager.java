package com.solitaire.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.Stack;

public class CardManager {
    ArrayList<ArrayList<Card>> tableCards = new ArrayList<>();
    Board board = new Board();
    ArrayList<ArrayList<Card>> tableau;
    ArrayList<ArrayList<Card>> foundation;
    ArrayList<Card> deck;
    Stack<Card> wastePile;

    public CardManager() {
        board.initBoard();
        deck = board.getDeck();
        tableau = board.getTableau();
        foundation = board.getFoundation();
        wastePile = board.getWastePile();
    }

    // pick the first card from the deck
    public void pickCard(Stack<Card> wastePile, ArrayList<Card> deck) {
        board.pickCard(wastePile, deck);
    }

    public boolean moveToFoundation(ArrayList<ArrayList<Card>> foundation, Card card) {
        System.out.println("E");
        return board.moveToFoundation(foundation, card);
    }

    public boolean moveToTableau(ArrayList<ArrayList<Card>> tableau, Card card) {
        return board.moveToTableau(tableau, card);
    }

    public boolean moveWithinTableau(ArrayList<ArrayList<Card>> tableau, Card selectedCard, int selectedCardIndex, int selectedPileIndex) {
        return board.moveWithinTableau(tableau, selectedCard, selectedCardIndex, selectedPileIndex);
    }

    public void drawInitTableau(SpriteBatch batch) {
        board.drawInitTableau(batch);
    }

    public void drawTableau(SpriteBatch batch) {
        board.drawTableau(batch);
    }

    public void drawWastePile(SpriteBatch batch) {
        board.drawWastePile(batch);
    }

    public void drawFoundation(SpriteBatch batch) {
        board.drawFoundation(batch);
    }

    public void moveCard(OrthographicCamera camera) {
        board.moveCard(camera);
    }
}