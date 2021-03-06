package com.solitaire.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.solitaire.game.button.RulesButton;
import com.solitaire.game.controller.CardManager;
import com.solitaire.game.util.Placeholder;

public class GameScreen implements Screen {

    private static boolean GAME_PAUSED = false;
    private final Game parent;
    private final int screenWidth = 800;
    private final int screenHeight = 480;
    private final CardManager cardManager;
    private final SpriteBatch batch;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private boolean tableauIsInitialized;
    private int currentTime;
    private final Stage stage;
    private float timer;
    private RulesWindow rulesWindow;
    private final Placeholder placeholder = new Placeholder(new Sprite(new Texture("PlaceholderDeck.png")));
    private final Texture pauseButtonTexture = new Texture(Gdx.files.internal("pausebutton.png"));
    private final Texture rulesTexture = new Texture(Gdx.files.internal("rules_button.png"));
    // initGameSound does not work unless type is Music, not sure why
    private final Music initGameSound = Gdx.audio.newMusic(Gdx.files.internal("init-game.wav"));
    private final Music timerSound = Gdx.audio.newMusic(Gdx.files.internal("timer.wav"));
    private final Sound moveCardSound = Gdx.audio.newSound(Gdx.files.internal("card-move.wav"));
    private final Sound dealCardSound = Gdx.audio.newSound(Gdx.files.internal("dealing-card.wav"));

    public GameScreen(Game parent, CardManager cardManager) {
        this.parent = parent;
        this.cardManager = cardManager;
        tableauIsInitialized = false;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(screenWidth, screenHeight, camera);
        viewport.apply();
        stage = new Stage(viewport, batch);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.setToOrtho(false, screenWidth, screenHeight);
        camera.update();
        timer = 0;
        currentTime = (int) timer;
    }

    @Override
    public void show() {
        rulesWindow = new RulesWindow(parent, stage);
        TextureRegionDrawable rulesRegionDrawable = new TextureRegionDrawable(rulesTexture);
        RulesButton rulesButton = new RulesButton(rulesRegionDrawable);
        rulesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                rulesWindow.setVisible(true);
                timerSound.pause();
                pause();
            }
        });
        TextureRegion pauseButtonTextureRegion = new TextureRegion(pauseButtonTexture);
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(pauseButtonTextureRegion);

        ImageButton pauseButton = new ImageButton(textureRegionDrawable);
        pauseButton.setX(735);
        pauseButton.setY(430);

        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.setScreen(new PauseScreen(parent));
                timerSound.pause();
            }
        });

        stage.addActor(pauseButton);
        stage.addActor(rulesWindow);
        stage.addActor(rulesButton);
        stage.addActor(pauseButton);

        if (cardManager.isTimedGame() && !timerSound.isLooping() && !timerSound.isPlaying()) {
            timerSound.setLooping(true);
            timerSound.play();
        }
    }

    @Override
    public void render(float delta) {
        batch.begin();
        Gdx.input.setInputProcessor(stage);
        ScreenUtils.clear(34, 139, 34, 1);

        float r = 53/255f;
        float g = 133/255f;
        float b = 27/255f;
        float a = 255/255f;
        Gdx.gl.glClearColor(r,g,b,a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        placeholder.draw(batch, 498, 400);

        // draw the deck if it still contains cards
        cardManager.drawDeck(batch, cardManager.getDeck());

        // draw tableau
        if (tableauIsInitialized) {
            cardManager.drawTableau(batch, cardManager.getTableau());
        } else {
            cardManager.drawInitTableau(batch, cardManager.getTableau());
            tableauIsInitialized = true;
            initGameSound.play();
        }

        // draw wastePile
        cardManager.drawWastePile(batch, cardManager.getWastePile());

        // draw foundation
        cardManager.drawFoundation(batch, cardManager.getFoundation());

        // draw score
        cardManager.drawScore(batch, cardManager.getScore());

        // draw timer
        float deltaTime = GAME_PAUSED ? 0 : Gdx.graphics.getDeltaTime();
        if (cardManager.isTimedGame()) {
            timer += deltaTime;
            cardManager.drawTime(batch, Math.round(timer));
        }

        // lose 2 points for every 10 seconds elapsed during a timed game
        int temp = (int) timer;
        if (temp % 10 == 0 && temp > currentTime) {
            currentTime = temp;
            cardManager.setScore(-2);
        }

        boolean buttonWasClicked = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        boolean screenWasTouched = Gdx.input.justTouched();
        if (buttonWasClicked || screenWasTouched) {
            if (cardManager.movedFromFoundationToTableau() || cardManager.movedToTableau() || cardManager.movedFromWastePile()) {
                moveCardSound.play();
            } else if (cardManager.movedToWastePile()) {
                dealCardSound.play();
            }
        }
        batch.end();
        stage.draw();
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {
        GAME_PAUSED = true;
    }

    @Override
    public void resume() {
        GAME_PAUSED = false;
        if (cardManager.isTimedGame()) {
            timerSound.play();
        }
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        pauseButtonTexture.dispose();
        rulesTexture.dispose();
        cardManager.dispose();
        moveCardSound.dispose();
        dealCardSound.dispose();
        initGameSound.dispose();
        timerSound.dispose();
    }
}
