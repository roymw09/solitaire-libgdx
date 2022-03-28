package com.solitaire.game.button;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ReviewButton extends ImageButton {

    public ReviewButton(TextureRegionDrawable textureRegionDrawable) {
        super(textureRegionDrawable);
        setPosition(15, 25);
        setSize(48, 48);
    }
}
