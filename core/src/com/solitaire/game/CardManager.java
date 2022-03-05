package com.solitaire.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CardManager {
    private final Board board;

    public CardManager() {
        board = new Board();
        board.initBoard();
    }

    public void drawDeck(SpriteBatch batch) { board.drawDeck(batch); }

    public void drawScore(SpriteBatch batch) { board.drawScore(batch); }

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

    public void setStandardMode(boolean mode) {
        board.setStandardMode(mode);
    }

    public boolean isDrawThree() { return board.isDrawThree(); }

    public void setDrawThree(boolean drawThree) { board.setDrawThree(drawThree); }

    public boolean isPlaying() { return board.isPlaying(); }

    public void setPlaying(boolean playing) {board.setPlaying(playing); }
}