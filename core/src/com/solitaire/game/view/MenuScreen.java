package com.solitaire.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.solitaire.game.button.ReviewButton;
import com.solitaire.game.button.RulesButton;
import com.solitaire.game.controller.CardManager;

public class MenuScreen implements Screen {

    private final Game parent;
    private final int screenWidth = 800;
    private final int screenHeight = 480;
    private final SpriteBatch batch;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final CardManager cardManager;
    private final TextureAtlas buttonTextureAtlas;
    protected Skin buttonSkin;
    protected Stage stage;
    protected BitmapFont font;
    protected TextButtonStyle textButtonStyle;
    private RulesButton rulesButton;
    private ReviewButton reviewButton;
    private RulesWindow rulesWindow;

    public MenuScreen(Game parent) {
        this.parent = parent;

        font = new BitmapFont();

        textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        buttonTextureAtlas = new TextureAtlas("button.atlas");
        buttonSkin = new Skin(buttonTextureAtlas);
        textButtonStyle.up = buttonSkin.getDrawable("button");


        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(screenWidth, screenHeight, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);

        cardManager = new CardManager(camera, parent);
    }

    @Override
    public void show() {
        rulesWindow = new RulesWindow(parent, stage);
        Texture rulesTexture = new Texture(Gdx.files.internal("rules_button.png"));
        TextureRegionDrawable rulesRegionDrawable = new TextureRegionDrawable(rulesTexture);
        rulesButton = new RulesButton(rulesRegionDrawable);
        rulesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                rulesWindow.setVisible(true);
            }
        });

        Texture reviewTexture = new Texture(Gdx.files.internal("review_button.png"));
        TextureRegionDrawable reviewRegionDrawable = new TextureRegionDrawable(reviewTexture);
        reviewButton = new ReviewButton(reviewRegionDrawable);
        reviewButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.setScreen(new ReviewScreen(parent));
            }
        });


        final TextButton standardButton = new TextButton("Standard", textButtonStyle);
        final TextButton vegasButton = new TextButton("Vegas", textButtonStyle);

        standardButton.setX(200);
        standardButton.setY(camera.viewportHeight/2 - standardButton.getHeight()/2);

        vegasButton.setX(400);
        vegasButton.setY(camera.viewportHeight/2 - vegasButton.getHeight()/2);

        standardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                cardManager.setStandardMode(true);
                parent.setScreen(new StandardModeOptionsScreen(parent, cardManager));
            }
        });

        vegasButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                cardManager.setPlaying(true);
                cardManager.setStandardMode(false);
            }
        });

        stage.addActor(standardButton);
        stage.addActor(vegasButton);
        stage.addActor(rulesWindow);
        stage.addActor(rulesButton);
        stage.addActor(reviewButton);
    }

    @Override
    public void render(float delta) {
        float r = 53/255f;
        float g = 133/255f;
        float b = 27/255f;
        float a = 255/255f;
        Gdx.gl.glClearColor(r,g,b,a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.input.setInputProcessor(stage);
        stage.act();
        stage.draw();
        if (cardManager.isPlaying()) {
            parent.setScreen(new GameScreen(parent, cardManager));
        }
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

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        buttonSkin.dispose();
        buttonTextureAtlas.dispose();
    }
}
