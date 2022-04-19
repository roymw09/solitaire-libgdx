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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.solitaire.game.button.ReviewButton;
import com.solitaire.game.button.RulesButton;
import com.solitaire.game.controller.CardManager;

public class StandardModeOptionsScreen implements Screen {

    private final Game parent;
    private final int screenWidth = 800;
    private final int screenHeight = 480;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final CardManager cardManager;
    private final TextureAtlas buttonTextureAtlas;
    private final TextureAtlas uncheckTextureAtlas;
    private final TextureAtlas checkedTextureAtlas;
    protected Skin buttonSkin;
    protected Skin checkedSkin;
    protected Skin uncheckedSkin;
    protected Stage stage;
    protected BitmapFont font;
    protected TextButton.TextButtonStyle textButtonStyle;
    protected CheckBox.CheckBoxStyle checkBoxStyle;
    private RulesButton rulesButton;
    private ReviewButton reviewButton;
    private RulesWindow rulesWindow;

    public StandardModeOptionsScreen(Game parent, CardManager cardManager) {
        this.parent = parent;

        font = new BitmapFont();

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        buttonTextureAtlas = new TextureAtlas("button.atlas");
        buttonSkin = new Skin(buttonTextureAtlas);
        textButtonStyle.up = buttonSkin.getDrawable("button");

        uncheckTextureAtlas = new TextureAtlas("unchecked.atlas");
        checkedTextureAtlas = new TextureAtlas("checked.atlas");

        checkedSkin = new Skin(checkedTextureAtlas);
        uncheckedSkin = new Skin(uncheckTextureAtlas);

        checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = font;
        checkBoxStyle.checkboxOn = checkedSkin.getDrawable("checked");
        checkBoxStyle.checkboxOff = uncheckedSkin.getDrawable("unchecked");

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(screenWidth, screenHeight, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);

        this.cardManager = cardManager;
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


        final TextButton startGameButton = new TextButton("Start Game", textButtonStyle);
        final CheckBox drawOne = new CheckBox("Draw One", checkBoxStyle);
        final CheckBox drawThree = new CheckBox("Draw Three", checkBoxStyle);
        final CheckBox timedGame = new CheckBox("Timed Game", checkBoxStyle);

        //startGameButton.setX(200);
        //startGameButton.setY(200);
        startGameButton.setX(Gdx.graphics.getWidth()/2 - startGameButton.getWidth()/2);
        startGameButton.setY(200);

        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (drawOne.isChecked() || drawThree.isChecked()) {
                    cardManager.setPlaying(true);
                }
            }
        });


        drawOne.setX(350);
        drawOne.setY(175);

        drawThree.setX(350);
        drawThree.setY(125);

        timedGame.setX(350);
        timedGame.setY(75);

        drawOne.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (drawOne.isChecked() && !drawThree.isChecked()) {
                    cardManager.setDrawThree(false);
                } else {
                    drawOne.setChecked(false);
                }
            }
        });

        drawThree.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (drawThree.isChecked() && !drawOne.isChecked()) {
                    cardManager.setDrawThree(true);
                } else {
                    drawThree.setChecked(false);
                }
            }
        });

        timedGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                cardManager.setTimedGame(timedGame.isChecked());
            }
        });

        stage.addActor(startGameButton);
        stage.addActor(drawOne);
        stage.addActor(drawThree);
        stage.addActor(timedGame);
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
        checkedSkin.dispose();
        uncheckedSkin.dispose();
        buttonTextureAtlas.dispose();
        checkedTextureAtlas.dispose();
        uncheckTextureAtlas.dispose();
    }
}
