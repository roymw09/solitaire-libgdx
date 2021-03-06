package com.solitaire.game.model;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.solitaire.game.view.WinScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Board {
    private ArrayList<Card> deck;
    private final ArrayList<ArrayList<Card>> tableau;
    private final ArrayList<ArrayList<Card>> foundation;
    private final Stack<Card> wastePile;
    private int score;
    private boolean standardMode;
    private int passedThroughDeck = 0;
    private boolean drawThree;
    private boolean playing;
    private boolean timedGame;
    private OrthographicCamera camera;
    private Game parent;
    private Vector3 touchPoint = new Vector3();
    private final Sound shuffleCardSound = Gdx.audio.newSound(Gdx.files.internal("shuffle-cards.wav"));

    public Board(OrthographicCamera camera, Game parent) {
        this.deck = makeCards();
        this.tableau = new ArrayList<>();
        this.foundation = new ArrayList<>();
        this.wastePile = new Stack<>();
        this.camera = camera;
        this.parent = parent;
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

    public boolean pickCard(ArrayList<Card> deck) {
        // Remove score after 1 or 4 passes through deck
        if (standardMode && deck.isEmpty() && !wastePile.isEmpty()) {
            if (drawThree && passedThroughDeck >= 4) {
                score-=20;
            } else if (!drawThree && passedThroughDeck >= 1) {
                score-=100;
            }
        }
        touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPoint);

        if (drawThree) {
            FlipCard(false);
            FlipCard(false);
            if (FlipCard(true)) {
                return true;
            }
        } else {
            if (FlipCard(true)){
                return true;
            }
        }

        return false;
    }

    private boolean FlipCard(boolean last){
        if (deck.size() > 0) {
            Card card = deck.get(0);
            card.setFaceUp(true);
            deck.remove(card);
            wastePile.push(card);
            return true;
        } else if (!wastePile.isEmpty() && last) {
            shuffleCardSound.play();
            passedThroughDeck++;
            deck.addAll(wastePile);
            wastePile.clear();
        }
        return false;
    }

    public boolean movedToTableau() {
        // Changed it to unproject to get accurate hit boxes on the cards
        touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPoint);
        for (ArrayList<Card> cards: tableau) {
            for (Card card : cards) {
                if (card.getFrontImage().getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
                    // places card in the foundation
                    boolean moveFoundation = cards.indexOf(card) == cards.size()-1 && moveToFoundation(card);
                    if (moveFoundation) {
                        cards.remove(card);
                        if (cards.size() != 0 && !cards.get(cards.size()-1).getFaceUp()) {
                            cards.get(cards.size()-1).setFaceUp(true);
                            // 5 points for every card turned face up
                            if (standardMode) score+=5;
                        }
                        return true;
                        // move card within tableau
                    } else if (moveWithinTableau(card, cards.indexOf(card), tableau.indexOf(cards))) {
                        if (cards.size() != 0 && !cards.get(cards.size()-1).getFaceUp()) {
                            cards.get(cards.size()-1).setFaceUp(true);
                            // 5 points for every card turned face up
                            if (standardMode) score+=5;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean movedToWastePile() {
        // move card to wastePile when the deck is clicked
        int spriteLocationX = 498;
        int spriteLocationY = 400;
        touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPoint);
        Rectangle bounds = new Rectangle(spriteLocationX, spriteLocationY, 61, 79);
        System.out.println(touchPoint.x + ", " + touchPoint.y);
        if (bounds.contains(touchPoint.x, touchPoint.y)) {
            return pickCard(deck);
        }
        return false;
    }

    public boolean movedFromFoundationToTableau() {
        // Changed it to unproject to get accurate hit boxes on the cards
        touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPoint);
        for (ArrayList<Card> cards : foundation) {
            if (cards.size() > 0) {
                Card card = cards.get(cards.size()-1);
                boolean isClicked = card.getFrontImage().getBoundingRectangle().contains(touchPoint.x, touchPoint.y);
                if (isClicked && moveFromFoundationToTableau(card)) {
                    cards.remove(card);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean movedFromWastePile() {
        touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPoint);
        if (!wastePile.isEmpty()) {
            Card card = wastePile.lastElement();
            boolean cardWasClicked = card.getFrontImage().getBoundingRectangle().contains(touchPoint.x, touchPoint.y);
            if (cardWasClicked && moveToFoundation(card)){
                wastePile.remove(card);
                // 5 points for moving from the wastepile to the foundation
                if (standardMode) {
                    score+=5;
                }
                return true;
            }
            else if (cardWasClicked && moveToTableau(card)) {
                wastePile.remove(card);
                // 5 points for moving from the wastepile to the tableau
                if (standardMode) {
                    score+=5;
                }
                return true;
            }
        }
        return false;
    }

    public boolean moveToFoundation(Card card) {
        for (int i = 0; i < 4; i++) {
            // get the last card in the current foundation pile
            int lastCard = (!foundation.get(i).isEmpty()) ? foundation.get(i).get(foundation.get(i).size()-1).getValue() : 0;
            // get the first card in the current foundation pile so we can compare its suit to the card being moved
            boolean suitMatches = foundation.get(i).isEmpty() || card.getSuit().equals(foundation.get(i).get(0).getSuit());
            // make sure the suits match and the card being moved to the foundation is in the correct order
            if (suitMatches && card.getValue() == lastCard+1) {
                foundation.get(i).add(card);
                // 10 points for moving card to foundation in standard mode
                if (standardMode) {
                    score+=10;
                } else {
                    // 5 points in vegas mode
                    score+=5;
                }
                CheckWin();
                return true;
            }
        }
        return false;
    }

    public boolean CheckWin(){
        int kings = 0;
        for (int i = 0; i < 4; i++){
            System.out.println("Test ------ " + ((!foundation.get(i).isEmpty()) ? foundation.get(i).get(foundation.get(i).size()-1).getValue() : 0));
            int lastCard = (!foundation.get(i).isEmpty()) ? foundation.get(i).get(foundation.get(i).size()-1).getValue() : 0;
            if (lastCard == 13) {
                kings++;
            }
        }
        if (kings == 4){
            System.out.println("WIN");
            parent.setScreen(new WinScreen(parent));
            return true;
        }
        return false;
    }

    public boolean moveFromFoundationToTableau(Card card) {
        if (moveToTableau(card)) {
            // lose score when card comes out of foundation
            if (standardMode) {
                score-=10;
            } else {
                score-=5;
            }
            return true;
        }
        return false;
    }

    public boolean moveToTableau(Card card) {
        for (int i = 0; i < 7; i++) {
            // get the last card in the current tableau pile
            int lastCard = (!tableau.get(i).isEmpty()) ? tableau.get(i).get(tableau.get(i).size() - 1).getValue() : 0;
            // get the first card in the current tableau pile so we can compare its colours
            boolean matchColour = !tableau.get(i).isEmpty() &&
                    card.getCardColor().equals(tableau.get(i).get(tableau.get(i).size() - 1).getCardColor());
            // check if the tableau is empty and the current card is a king
            boolean isKing = tableau.get(i).isEmpty() && card.getValue() == 13;
            // make sure the colours are different and the card being moved to the tableau is in the correct order
            if (!matchColour && card.getValue() == lastCard - 1 || isKing) {
                tableau.get(i).add(card);
                return true;
            }
        }
        return false;
    }

    /* if the colors do not match and the card being moved adhere to the logical order of the cards,
    append all selected cards to the tableau */
    public boolean moveWithinTableau(Card selectedCard, int selectedCardIndex, int selectedPileIndex) {
        ArrayList<Card> cardsToMove = getSelectedCards(tableau, selectedCardIndex, selectedPileIndex);
        for (int i = 0; i < 7; i++) {
            int lastCardValue = (!tableau.get(i).isEmpty()) ? tableau.get(i).get(tableau.get(i).size() - 1).getValue() : 0;
            boolean colourMatch = !tableau.get(i).isEmpty() &&
                    selectedCard.getCardColor().equals(tableau.get(i).get(tableau.get(i).size() - 1).getCardColor());
            // check if the tableau is empty and the current card is a king
            boolean isKing = tableau.get(i).isEmpty() && selectedCard.getValue() == 13;
            if (!colourMatch && selectedCard.getValue() == lastCardValue-1 || isKing) {
                tableau.get(i).addAll(cardsToMove);
                tableau.get(selectedPileIndex).removeAll(cardsToMove);
                // 3 points for a card moved from one tableau pile to another
                Card lastCard = tableau.get(i).get(tableau.get(i).size() - 1);
                if (standardMode && !isKing && !selectedCard.hasCardBeenStacked(lastCard)) {
                    score+=3;
                    selectedCard.getCardsIveBeenStackedOn().add(lastCard);
                }
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

    public boolean isDrawThree() {
        return drawThree;
    }

    public void setDrawThree(boolean drawThree) {
        this.drawThree = drawThree;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isTimedGame() {
        return timedGame;
    }

    public void setTimedGame(boolean timedGame) {
        this.timedGame = timedGame;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public ArrayList<Card> getDeck() {
        return this.deck;
    }

    public ArrayList<ArrayList<Card>> getTableau() {
        return this.tableau;
    }

    public Stack<Card> getWastePile() {
        return this.wastePile;
    }

    public ArrayList<ArrayList<Card>> getFoundation() {
        return this.foundation;
    }

    public void dispose() {
        shuffleCardSound.dispose();
    }
}