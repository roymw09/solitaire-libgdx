package com.solitaire.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PauseScreen implements Screen {

    private final Game parent;
    private final TextureAtlas textTextureAtlas;
    protected Skin buttonSkin;
    protected Stage stage;
    protected BitmapFont font;
    private final SpriteBatch batch;
    protected TextButton.TextButtonStyle textButtonStyle;
    protected Screen previousScreen;
    private OrthographicCamera camera;
    private FitViewport viewport;

    public PauseScreen(Game parent) {
        this.parent = parent;
        batch = new SpriteBatch();
        font = new BitmapFont();
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textTextureAtlas = new TextureAtlas("button.atlas");
        buttonSkin = new Skin(textTextureAtlas);
        textButtonStyle.up = buttonSkin.getDrawable("button");
        previousScreen = parent.getScreen();
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        stage = new Stage(viewport, batch);
    }

    @Override
    public void show() {
        final TextButton resumeButton = new TextButton("Resume", textButtonStyle);
        final TextButton restartButton = new TextButton("Restart", textButtonStyle);

        resumeButton.setX(300);
        resumeButton.setY(200);

        restartButton.setX(300);
        restartButton.setY(50);

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.setScreen(previousScreen);
            }
        });

        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.setScreen(new MenuScreen(parent));
            }
        });

        stage.addActor(resumeButton);
        stage.addActor(restartButton);
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
        batch.dispose();
        stage.dispose();
        textTextureAtlas.dispose();
        buttonSkin.dispose();
        font.dispose();
        previousScreen.dispose();
        parent.dispose();
    }
}
