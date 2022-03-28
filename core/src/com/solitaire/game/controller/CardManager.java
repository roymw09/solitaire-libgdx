package com.solitaire.game.controller;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.solitaire.game.util.BoardDrawer;
import com.solitaire.game.model.Board;
import com.solitaire.game.model.Card;

import java.util.ArrayList;
import java.util.Stack;

public class CardManager {
    private final Board board;
    private final BoardDrawer boardDrawer;

    public CardManager() {
        board = new Board();
        board.initBoard();
        boardDrawer = new BoardDrawer();
    }

    public void drawDeck(SpriteBatch batch, ArrayList<Card> deck) { boardDrawer.drawDeck(batch, deck); }

    public void drawTime(SpriteBatch batch, float timer) { boardDrawer.drawTime(batch, timer); }

    public void drawScore(SpriteBatch batch, int score) { boardDrawer.drawScore(batch, score); }

    public void drawInitTableau(SpriteBatch batch, ArrayList<ArrayList<Card>> tableau) {
        boardDrawer.drawInitTableau(batch, tableau);
    }

    public void drawTableau(SpriteBatch batch, ArrayList<ArrayList<Card>> tableau) {
        boardDrawer.drawTableau(batch, tableau);
    }

    public void drawWastePile(SpriteBatch batch, Stack<Card> wastePile) {
        boardDrawer.drawWastePile(batch, wastePile);
    }

    public void drawFoundation(SpriteBatch batch, ArrayList<ArrayList<Card>> foundation) {
        boardDrawer.drawFoundation(batch, foundation);
    }

    public void moveCard(OrthographicCamera camera) {
        board.moveCard(camera);
    }

    public void setStandardMode(boolean mode) {
        board.setStandardMode(mode);
    }

    public boolean isDrawThree() { return board.isDrawThree(); }

    public void setDrawThree(boolean drawThree) { board.setDrawThree(drawThree); }

    public boolean isPlaying() { return board.isPlaying(); }

    public void setPlaying(boolean playing) {board.setPlaying(playing); }

    public boolean isTimedGame() { return board.isTimedGame(); }

    public void setTimedGame(boolean timedGame) { board.setTimedGame(timedGame); }

    public int getScore() { return board.getScore(); }

    public void setScore(int score) { board.setScore(score); }

    public ArrayList<Card> getDeck() {
        return board.getDeck();
    }

    public ArrayList<ArrayList<Card>> getTableau() { return board.getTableau(); }

    public Stack<Card> getWastePile() {
        return board.getWastePile();
    }

    public ArrayList<ArrayList<Card>> getFoundation() {
        return board.getFoundation();
    }

    public void dispose() {
        boardDrawer.dispose();
    }
}