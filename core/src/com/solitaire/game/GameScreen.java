package com.solitaire.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

    private final int screenWidth = 800;
    private final int screenHeight = 480;
    private final CardManager cardManager;
    private final SpriteBatch batch;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private boolean tableauIsInitialized = false;
    private int currentTime;

    public GameScreen(CardManager cardManager) {
        this.cardManager = cardManager;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.setToOrtho(false, screenWidth, screenHeight);
        camera.update();
        currentTime = (int) cardManager.getTimer();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        batch.begin();
        Gdx.input.setInputProcessor(null);
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

        // draw timer
        if (cardManager.isTimedGame()) {
            cardManager.drawTime(batch);
        }

        // lose 2 points for every 10 seconds elapsed during a timed game
        int temp = (int) cardManager.getTimer();
        if (temp % 10 == 0 && temp > currentTime) {
            currentTime = temp;
            cardManager.setScore(-2);
        }

        boolean buttonWasClicked = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        if (buttonWasClicked) {
            cardManager.moveCard(camera);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
