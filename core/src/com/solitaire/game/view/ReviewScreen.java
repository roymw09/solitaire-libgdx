package com.solitaire.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.solitaire.game.button.CancelButton;
import com.solitaire.game.button.ReviewButton;
import com.solitaire.game.button.SubmitButton;

public class ReviewScreen implements Screen {

    private final Game parent;
    private final SpriteBatch batch;
    private final int screenWidth = 800;
    private final int screenHeight = 480;
    private final OrthographicCamera camera;
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private final BitmapFont font = new BitmapFont();
    private final Viewport viewport;
    private SubmitButton submitButton;
    private CancelButton cancelButton;
    protected Stage stage;
    protected Skin skin;

    public ReviewScreen(Game parent) {
        this.parent = parent;

        camera = new OrthographicCamera();
        viewport = new FitViewport(screenWidth, screenHeight, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        batch = new SpriteBatch();
    }

    @Override
    public void show() {

        stage = new Stage(viewport, batch);
        skin = new Skin(Gdx.files.internal("default/skin/uiskin.json"));
        final TextArea txtf = new TextArea("", skin);
        txtf.setWidth(400);
        txtf.setHeight(90);
        txtf.setX(screenWidth/2 - (txtf.getWidth()/2));
        txtf.setY(screenHeight/2);
        stage.addActor(txtf);

        Label text = new Label("Feedback", new Label.LabelStyle(font, Color.WHITE));
        text.setX(screenWidth/2 - (text.getWidth()/2));
        text.setY(screenHeight/2 + txtf.getHeight() + 20);
        stage.addActor(text);

        Texture submitTexture = new Texture(Gdx.files.internal("submit_button2.png"));
        TextureRegionDrawable submitRegionDrawable = new TextureRegionDrawable(submitTexture);
        submitButton = new SubmitButton(submitRegionDrawable);
        submitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println(txtf.getText());
                parent.setScreen(new ThankScreen(parent));
            }
        });
        stage.addActor(submitButton);

        Texture cancelTexture = new Texture(Gdx.files.internal("cancel_button2.png"));
        TextureRegionDrawable cancelRegionDrawable = new TextureRegionDrawable(cancelTexture);
        cancelButton = new CancelButton(cancelRegionDrawable);
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println(txtf.getText());
                parent.setScreen(new MenuScreen(parent));
            }
        });
        stage.addActor(cancelButton);
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
        font.setColor(Color.WHITE);
        font.getData().setScale(6, 6);
        String str = "Thank Yougggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg";
        glyphLayout.setText(font, str);
        batch.begin();
        font.draw(batch, str, Gdx.graphics.getWidth()/2 - glyphLayout.width/2, Gdx.graphics.getHeight()/2 - glyphLayout.height/2);
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
        skin.dispose();
        font.dispose();
    }
}
