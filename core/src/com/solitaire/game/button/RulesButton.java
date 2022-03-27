package com.solitaire.game.button;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class RulesButton extends ImageButton {

    public RulesButton(TextureRegionDrawable textureRegionDrawable) {
        super(textureRegionDrawable);
        setPosition(735, 25);
        setSize(48, 48);
    }
}
