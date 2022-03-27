package com.solitaire.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class RulesWindow extends Window {

    private static final WindowStyle windowStyle;
    private static final ImageButtonStyle backButtonStyle;
    static {
        Texture backgroundTexture = new Texture(Gdx.files.internal("rules_background.png"));
        TextureRegion backgroundRegion = new TextureRegion(backgroundTexture);
        windowStyle = new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(backgroundRegion));

        Texture backButtonTexture = new Texture(Gdx.files.internal("rules_back_button.png"));
        TextureRegion backButtonRegion = new TextureRegion(backButtonTexture);
        backButtonStyle = new ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(backButtonRegion);
    }

    public RulesWindow(final Game parent) {
        super("", windowStyle);
        final Button backButton = new ImageButton(backButtonStyle);
        backButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               setVisible(false);
               parent.resume();
           }
        });
        getTitleTable().add(backButton).padRight(20).padTop(100);
        setClip(false);
        setTransform(true);
        setSize(450, 392);
        setModal(true);
        setPosition(Gdx.graphics.getWidth()/2 - getWidth()/2, Gdx.graphics.getHeight()/2 - getHeight()/2);
        setVisible(false);
    }
}
