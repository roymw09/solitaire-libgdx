package com.solitaire.game.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.solitaire.game.model.Card;
import java.util.ArrayList;
import java.util.Stack;

public class BoardDrawer {

    private static final Placeholder placeholderTemplate = new Placeholder(new Sprite(new Texture("PlaceholderTemplate.png")));
    private static final Placeholder placeholderWaste = new Placeholder(new Sprite(new Texture("PlaceholderWaste.png")));
    private static final Placeholder placeholderAce = new Placeholder(new Sprite(new Texture("PlaceholderAce.png")));
    private static final BitmapFont font = new BitmapFont();
    private static final Texture cardBackImage = new Texture("card_back.png");
    private static final int cardWidth = 61;
    private static final int cardHeight = 79;
    private OrthographicCamera camera;

    public BoardDrawer(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void drawTime(SpriteBatch batch, float timer) {
        String timerString = Integer.toString(Math.round(timer));
        font.draw(batch, timerString, 650, 450);
    }

    public void drawScore(SpriteBatch batch, int score) {
        font.draw(batch, "Score: " + score, 650, 470);
    }

    public void drawDeck(SpriteBatch batch, ArrayList<Card> deck) {
        Sprite card_back = new Sprite(cardBackImage);
        card_back.setSize(61, 79);
        card_back.setPosition(498, 400);
        if (!deck.isEmpty()) {
            card_back.draw(batch);
        }
    }

    public void drawInitTableau(SpriteBatch batch, ArrayList<ArrayList<Card>> tableau) {
        boolean initial = false;
        float counterX = camera.viewportWidth/2 - (cardWidth*7)/2;
        float counterY = camera.viewportHeight/2 - (cardHeight*-1.5f)/2;
        for (int i = 0; i < tableau.size(); i++) {
            int secondSize;
            if (initial) {
                secondSize = tableau.get(i).size();
            } else {
                secondSize = i+1;
            }
            for (int j = 0; j < secondSize; j++) {
                // draw each card in the tableau
                Card card = tableau.get(i).get(j);
                card.setFaceUp(j == i); // set the last card of each pile face up and draw the card's front image
                card.draw(batch, Math.round(counterX), Math.round(counterY));
                counterY -= 20;
            }
            counterX += cardWidth;
            counterY = camera.viewportHeight/2 - (cardHeight*-1.5f)/2;
            initial = true;
        }
    }

    public void drawTableau(SpriteBatch batch, ArrayList<ArrayList<Card>> tableau) {
        float counterX = camera.viewportWidth/2 - (cardWidth*7)/2;
        float counterY = camera.viewportHeight/2 - (cardHeight*-1.5f)/2;
        for (ArrayList<Card> cards : tableau) {
            placeholderTemplate.draw(batch, Math.round(counterX), Math.round(counterY));
            for (Card card : cards) {
                card.draw(batch, Math.round(counterX), Math.round(counterY));
                counterY -= 20;
            }
            counterX += cardWidth;
            counterY = camera.viewportHeight/2 - (cardHeight*-1.5f)/2;
        }
    }

    public void drawWastePile(SpriteBatch batch, Stack<Card> wastePile) {
        int x = 434;
        int y = 400;
        placeholderWaste.draw(batch, x, y);
        if (!wastePile.isEmpty()) {
            Card card = wastePile.lastElement();
            card.draw(batch, x, y);
        }
    }

    public void drawFoundation(SpriteBatch batch, ArrayList<ArrayList<Card>> foundation) {
        int x = 100;
        int y = 400;
        // draw the last card in the foundation
        for (ArrayList<Card> cards : foundation) {
            placeholderAce.draw(batch, x, y);
            if (!cards.isEmpty()) {
                Card card = cards.get(cards.size()-1);
                card.draw(batch, x, y);
            }
            x += 61;
        }
    }

    public void dispose() {
        font.dispose();
        cardBackImage.dispose();
    }
}