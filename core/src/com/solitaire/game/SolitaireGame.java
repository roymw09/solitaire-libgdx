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
	TextureRegion[][] topFrames;
	CardManager cardManger;
	ArrayList<Card> deck;
	ArrayList<Card> wastePile;
	Board board;
	ArrayList<ArrayList<Card>> tableau;
	Sprite card_back;

	@Override
	public void create () {
		batch = new SpriteBatch();
		cardManger = new CardManager();
		cardManger.MakeCards();
		deck = cardManger.GetDeck();
		board = new Board(deck);
		board.initBoard();
		wastePile = board.getWastePile();
		tableau = board.getTableau();
		// TODO - Refactor
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
		//batch.draw(topFrames[0][0], 0, 0);

		// draw the deck if it still contains cards
		if (!deck.isEmpty()) {
			card_back.draw(batch);
		}
		// draw tableau
		drawTableau();
		// draw wastePile
		drawWastePile();
		batch.end();
		// move card to wastePile when the deck is clicked
		boolean buttonWasClicked = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
		if (buttonWasClicked) {
			int spriteLocationX = 498;
			int spriteLocationY = 15;
			Vector3 touchPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			Rectangle2D bounds = new Rectangle2D.Float(spriteLocationX, spriteLocationY, 40, 63);
			if (bounds.contains(touchPoint.x, touchPoint.y)) {
				cardManger.pickCard(wastePile);
			}
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	private void drawTableau() {
		int counterX = 240;
		int counterY = 300;
		for (int i = 0; i < tableau.size(); i++) {
			for (int j = 0; j < i+1; j++) {
				// draw each card in the tableau
				Card card = tableau.get(i).get(j);
				card.setFaceUp(j == i); // set the last card of each pile face up and draw the card's front image
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
				card.setFaceUp(true);
				card.draw(batch, x, y);
				x -= 43;
			}
		}
	}
}
