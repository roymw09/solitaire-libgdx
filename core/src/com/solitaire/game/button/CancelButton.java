package com.solitaire.game.button;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CancelButton extends ImageButton {

    public CancelButton(TextureRegionDrawable textureRegionDrawable) {
        super(textureRegionDrawable);
        setPosition(500, 100);
        setSize(100, 100);
    }
}
