package com.solitaire.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class SolitaireGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	SpriteBatch batch;
	Texture img;
	CardManager cardManager;
	Sprite card_back;
	boolean tableauIsInitialized = false;


	@Override
	public void create () {
		batch = new SpriteBatch();
		cardManager = new CardManager();
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
		float r = 53/255f;
		float g = 133/255f;
		float b = 27/255f;
		float a = 255/255f;
		Gdx.gl.glClearColor(r,g,b,a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		new Placeholder(new Sprite(new Texture("PlaceholderDeck.png"))).draw(batch, 498, 400);

		// draw the deck if it still contains cards
		if (!cardManager.deck.isEmpty()) {
			card_back.draw(batch);
		}

		// draw tableau
		if (tableauIsInitialized) {
			cardManager.drawTableau(batch);
		} else {
			cardManager.drawInitTableau(batch);
			tableauIsInitialized = true;
		}

		// draw wastePile
		cardManager.drawWastePile(batch);

		// draw foundation
		cardManager.drawFoundation(batch);
		batch.end();

		boolean buttonWasClicked = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
		if (buttonWasClicked) {
			cardManager.moveCard(camera);
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}