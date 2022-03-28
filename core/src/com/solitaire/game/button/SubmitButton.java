package com.solitaire.game.button;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SubmitButton extends ImageButton {

    public SubmitButton(TextureRegionDrawable textureRegionDrawable) {
        super(textureRegionDrawable);
        setPosition(350, 100);
        setSize(100, 100);
    }
}
