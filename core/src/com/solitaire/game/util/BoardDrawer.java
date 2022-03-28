package com.solitaire.game.util;

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

    public void drawTime(SpriteBatch batch, float timer) {
        String timerString = Integer.toString(Math.round(timer));
        font.draw(batch, timerString, 650, 450);
    }

    public void drawScore(SpriteBatch batch, int score) {
        font.draw(batch, "Score: " + score, 650, 470);
    }

    public void drawDeck(SpriteBatch batch, ArrayList<Card> deck) {
        Sprite card_back = new Sprite(cardBackImage);
        card_back.setSize(40, 63);
        card_back.setPosition(498, 400);
        if (!deck.isEmpty()) {
            card_back.draw(batch);
        }
    }

    public void drawInitTableau(SpriteBatch batch, ArrayList<ArrayList<Card>> tableau) {
        boolean initial = false;
        int counterX = 240;
        int counterY = 300;
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
                card.draw(batch, counterX, counterY);
                counterY -= 20;
            }
            counterX += 43;
            counterY = 300;
            initial = true;
        }
    }

    public void drawTableau(SpriteBatch batch, ArrayList<ArrayList<Card>> tableau) {
        int counterX = 240;
        int counterY = 300;
        for (ArrayList<Card> cards : tableau) {
            placeholderTemplate.draw(batch, counterX, counterY);
            for (Card card : cards) {
                card.draw(batch, counterX, counterY);
                counterY -= 20;
            }
            counterX += 43;
            counterY = 300;
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
            x += 43;
        }
    }

    public void dispose() {
        font.dispose();
        cardBackImage.dispose();
    }
}