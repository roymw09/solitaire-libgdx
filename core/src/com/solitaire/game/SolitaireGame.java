package com.solitaire.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class SolitaireGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	SpriteBatch batch;
	Texture img;
	ArrayList<Card> deck;
	ArrayList<Card> wastePile;
	Board board;
	ArrayList<ArrayList<Card>> tableau;
	ArrayList<ArrayList<Card>> foundation;
	Sprite card_back;
	int[] tableauDefaultPosition = {240, 115, 43, -20};
	boolean initial = false;
	boolean tableauIsInitialized = false;



	@Override
	public void create () {
		batch = new SpriteBatch();
		board = new Board();
		board.initBoard();
		deck = board.getDeck();
		tableau = board.getTableau();
		foundation = board.getFoundation();
		wastePile = board.getWastePile();
		Texture cardBackImage = new Texture("card_back.png");
		card_back = new Sprite(cardBackImage);
		card_back.setSize(40, 63);
		card_back.setPosition(498, 400);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
	}

	@Override
	public void render () {
		ScreenUtils.clear(34, 139, 34, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		// draw the deck if it still contains cards
		if (!deck.isEmpty()) {
			card_back.draw(batch);
		}

		// draw tableau
		if (tableauIsInitialized) {
			drawTableau();
		} else {
			drawInitTableau();
			tableauIsInitialized = true;
		}

		// draw wastePile
		drawWastePile();

		// draw foundation
		drawFoundation();
		batch.end();

		boolean buttonWasClicked = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
		if (buttonWasClicked) {
			int spriteLocationX = 498;
			int spriteLocationY = 400;
			int[] currentPosition = {tableauDefaultPosition[0], tableauDefaultPosition[1]};

			//Changed it to unproject to get accurate hit boxes on the cards
			Vector3 touchPoint = new Vector3();
			touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPoint);

			//Loop through the coordinates to find the column then the row
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
						// places card in the
						if (board.moveToFoundation(foundation, card)) {
							tableau.get(column).remove(card);
							if (i != 0) {
								tableau.get(column).get(i - 1).setFaceUp(true);
							}
						} else if (board.moveToTableau(tableau, card)) {
							tableau.get(column).remove(card);
							System.out.println(i);
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
				board.pickCard(wastePile, deck);
			}

			// move cards from waste pile to foundation or tableau
			if (!wastePile.isEmpty()) {
				for (int i = wastePile.size()-1; i >= wastePile.size()-3 && i >= 0; i--) {
					Card card = wastePile.get(i);
					if (card.getFrontImage().getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
						if (board.moveToFoundation(foundation, card)) {
							wastePile.remove(card);
						} else if (board.moveToTableau(tableau, card)) {
							wastePile.remove(card);
						}
					}
				}
			}
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	private void drawInitTableau() {
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

	private void drawTableau() {
		int counterX = 240;
		int counterY = 300;
		for (ArrayList<Card> cards : tableau) {
			for (Card card : cards) {
				card.draw(batch, counterX, counterY);
				counterY -= 20;
			}
			counterX += 43;
			counterY = 300;
		}
	}

	private void drawWastePile() {
		int x = 434;
		int y = 400;
		if (!wastePile.isEmpty()) {
			for (int i = wastePile.size()-1; i >= wastePile.size()-3 && i >= 0; i--) {
				Card card = wastePile.get(i);
				card.draw(batch, x, y);
				x -= 43;
			}
		}
	}

	private void drawFoundation() {
		int x = 100;
		int y = 400;

		// draw the last card in the foundation
		for (ArrayList<Card> cards : foundation) {
			if (!cards.isEmpty()) {
				Card card = cards.get(cards.size()-1);
				card.draw(batch, x, y);
			}
			x += 43;
		}
	}
}