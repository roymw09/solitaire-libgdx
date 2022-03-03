package com.solitaire.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Board {
    private ArrayList<Card> deck;
    private final ArrayList<ArrayList<Card>> tableau;
    private final ArrayList<ArrayList<Card>> foundation;
    private final Stack<Card> wastePile;
    private int score;
    private final Sprite card_back;
    private final Texture cardBackImage = new Texture("card_back.png");
    private final int[] tableauDefaultPosition = {240, 115, 43, -20};
    private boolean initial = false;
    private boolean standardMode;

    public Board() {
        this.deck = makeCards();
        this.tableau = new ArrayList<>();
        this.foundation = new ArrayList<>();
        this.wastePile = new Stack<>();
        this.card_back = new Sprite(cardBackImage);
        this.card_back.setSize(40, 63);
        this.card_back.setPosition(498, 400);
    }

    public ArrayList<Card> makeCards() {
        deck = new ArrayList<>();
        Texture front_img;
        Texture back_img = new Texture("card_back.png");
        Sprite frontCards;
        Sprite backCards = new Sprite(back_img);
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
                    if (standardMode) {
                        score+=3; // 3 points for card moved from one tableau pile to another
                    }
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

    public void drawScore(SpriteBatch batch) {
        BitmapFont font = new BitmapFont();
        font.draw(batch, "Score: " + score, 600, 470);
    }

    public void drawDeck(SpriteBatch batch) {
        if (!deck.isEmpty()) {
            card_back.draw(batch);
        }
    }

    public void drawInitTableau(SpriteBatch batch) {
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

    public void drawTableau(SpriteBatch batch) {
        int counterX = 240;
        int counterY = 300;
        for (ArrayList<Card> cards : tableau) {
            new Placeholder(new Sprite(new Texture("PlaceholderTemplate.png"))).draw(batch, counterX, counterY);
            for (Card card : cards) {
                card.draw(batch, counterX, counterY);
                counterY -= 20;
            }
            counterX += 43;
            counterY = 300;
        }
    }

    public void drawWastePile(SpriteBatch batch) {
        int x = 434;
        int y = 400;
        new Placeholder(new Sprite(new Texture("PlaceholderWaste.png"))).draw(batch, x, y);
        if (!wastePile.isEmpty()) {
            Card card = wastePile.lastElement();
            card.draw(batch, x, y);
        }
    }

    public void drawFoundation(SpriteBatch batch) {
        int x = 100;
        int y = 400;

        // draw the last card in the foundation
        for (ArrayList<Card> cards : foundation) {
            new Placeholder(new Sprite(new Texture("PlaceholderAce.png"))).draw(batch, x, y);
            if (!cards.isEmpty()) {
                Card card = cards.get(cards.size()-1);
                card.draw(batch, x, y);
            }
            x += 43;
        }
    }

    public void moveCard(OrthographicCamera camera) {
        int spriteLocationX = 498;
        int spriteLocationY = 400;
        int[] currentPosition = {tableauDefaultPosition[0], tableauDefaultPosition[1]};

        // Changed it to unproject to get accurate hit boxes on the cards
        Vector3 touchPoint = new Vector3();
        touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPoint);

        // Loop through the coordinates to find the column then the row
        int column = -1;
        for (int i = 0; i < 7; i++) {
            if (touchPoint.x > (currentPosition[0] + (tableauDefaultPosition[2] * i)) && touchPoint.x < (currentPosition[0] + 40 + (tableauDefaultPosition[2] * i))) {
                column = i;
                break;
            }
        }
        if (column != -1){
            for (int i = tableau.get(column).size() - 1; i >= 0; i-= 1){
                Card card = tableau.get(column).get(i);
                if(card.getFrontImage().getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
                    // places card in the foundation
                    if (moveToFoundation(foundation, card)) {
                        tableau.get(column).remove(card);
                        if (i != 0) {
                            tableau.get(column).get(i - 1).setFaceUp(true);
                        }
                    } else if (moveWithinTableau(tableau, card, i, column)) {
                        if (i != 0) {
                            tableau.get(column).get(i - 1).setFaceUp(true);
                        }
                    }
                    break;
                }
            }
        }

        // move card to wastePile when the deck is clicked
        Rectangle2D bounds = new Rectangle2D.Float(spriteLocationX, spriteLocationY, 40, 63);
        System.out.println(bounds);
        System.out.println(touchPoint.x + ", " + touchPoint.y);
        if (bounds.contains(touchPoint.x, touchPoint.y)) {
            pickCard(wastePile, deck);
        }

        // move cards from waste pile to foundation or tableau
        if (!wastePile.isEmpty()) {
            Card card = wastePile.lastElement();
            boolean cardWasClicked = card.getFrontImage().getBoundingRectangle().contains(touchPoint.x, touchPoint.y);
            if (cardWasClicked) {
                if (moveToFoundation(foundation, card) || moveToTableau(tableau, card)) {
                    wastePile.remove(card);
                }
            }
        }
    }

    public void initBoard() {
        initTableau();
        initFoundation();
    }

    public void setStandardMode(boolean standardMode) {
        if (standardMode) {
            this.score = 0;
        } else {
            this.score = -52;
        }
        this.standardMode = standardMode;
    }
}