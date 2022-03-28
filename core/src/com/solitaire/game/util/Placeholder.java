package com.solitaire.game.util;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Placeholder {
    private Sprite frontImage;

    public Placeholder(Sprite frontImage) {
        this.frontImage = frontImage;
        this.frontImage.setSize(61, 79);
    }

    public void draw(SpriteBatch batch, int x, int y) {
            frontImage.setPosition(x, y);
            frontImage.draw(batch);
    }

    public Sprite getFrontImage(){
        return this.frontImage;
    }

    public void setFrontImage(Sprite frontImage){
        this.frontImage = frontImage;
    }
}
