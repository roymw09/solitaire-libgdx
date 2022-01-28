package com.solitaire.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

public class SolitaireGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	TextureRegion[][] topFrames;
	CardManager cardManger;
	ArrayList<Card> deck;
	Board board;
	ArrayList<ArrayList<Card>> tableau;
	Texture card_back;

	@Override
	public void create () {
		batch = new SpriteBatch();
		cardManger = new CardManager();
		cardManger.MakeCards();
		deck = cardManger.GetDeck();
		board = new Board(deck);
		board.initBoard();
		tableau = board.getTableau();
		card_back = new Texture("card_back.png");
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		//batch.draw(topFrames[0][0], 0, 0);
		// this is meant to represent the stock pile (deck)
		batch.draw(card_back, 498, 400, 40, 40);
		// Draw tableau
		drawTableau();
		batch.end();
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
				if (j == i ) {
					batch.draw(tableau.get(i).get(j).frontImage, counterX, counterY);
				} else {
					batch.draw(card_back, counterX, counterY, 40, 40);
				}
				counterY -= 20;
			}
			counterX += 43;
			counterY = 300;
		}
	}
}
