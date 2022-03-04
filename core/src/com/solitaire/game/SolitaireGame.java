package com.solitaire.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class SolitaireGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private CardManager cardManager;
	boolean tableauIsInitialized = false;
	private boolean playing = false;
	private final int screenWidth = 800;
	private final int screenHeight = 480;

	Stage stage;
	BitmapFont font;
	TextButtonStyle textButtonStyle;
	TextButton standardButton;
	TextButton vegasButton;

	@Override
	public void create () {
		batch = new SpriteBatch();
		cardManager = new CardManager();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenWidth, screenHeight);


		// TODO
		stage = new Stage();
		font = new BitmapFont();
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;

		standardButton = new TextButton("Standard", textButtonStyle);
		standardButton.setX(300);
		standardButton.setY(250);

		vegasButton = new TextButton("Vegas", textButtonStyle);
		vegasButton.setX(430);
		vegasButton.setY(250);

		standardButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("Button clicked");
				playing = true;
				cardManager.setStandardMode(true);
			}
		});
		vegasButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				playing = true;
				cardManager.setStandardMode(false);
			}
		});

		stage.addActor(standardButton);
		stage.addActor(vegasButton);

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render () {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		if (playing) {
			playGame();
		} else {
			showMenu();
		}
		batch.end();
	}

	public void playGame() {
		ScreenUtils.clear(34, 139, 34, 1);

		float r = 53/255f;
		float g = 133/255f;
		float b = 27/255f;
		float a = 255/255f;
		Gdx.gl.glClearColor(r,g,b,a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		new Placeholder(new Sprite(new Texture("PlaceholderDeck.png"))).draw(batch, 498, 400);

		// draw the deck if it still contains cards
		cardManager.drawDeck(batch);

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

		// draw score
		cardManager.drawScore(batch);

		boolean buttonWasClicked = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
		if (buttonWasClicked) {
			cardManager.moveCard(camera);
		}
	}

	public void showMenu() {
		font.draw(batch, "Choose a game mode", (325), 350);
		stage.draw();
	}

	@Override
	public void dispose () {
		batch.dispose();
		stage.dispose();
	}
}