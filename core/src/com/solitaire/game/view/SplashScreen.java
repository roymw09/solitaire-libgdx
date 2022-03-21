package com.solitaire.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {

    private final Game parent;
    private final SpriteBatch batch;
    private final GlyphLayout glyphLayout = new GlyphLayout();

    public SplashScreen(Game parent) {
        this.parent = parent;

        batch = new SpriteBatch();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        float r = 53/255f;
        float g = 133/255f;
        float b = 27/255f;
        float a = 255/255f;
        Gdx.gl.glClearColor(r,g,b,a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Texture logo_img = new Texture("Group_11_Studios-logos_white.png");
        BitmapFont font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(3, 3);
        String str = "Tap To Continue";
        glyphLayout.setText(font, str);
        batch.begin();
        batch.draw(logo_img, Gdx.graphics.getWidth()/2 - logo_img.getWidth()/2, Gdx.graphics.getHeight()/2 - logo_img.getHeight()/3);
        font.draw(batch, str, Gdx.graphics.getWidth()/2 - glyphLayout.width/2, Gdx.graphics.getHeight()/2 - glyphLayout.height*10);
        batch.end();


        boolean buttonWasClicked = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        boolean screenWasTouched = Gdx.input.justTouched();
        if (buttonWasClicked || screenWasTouched) {
            parent.setScreen(new MenuScreen(parent));
        }
    }

    @Override
    public void resize(int width, int height) {

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
