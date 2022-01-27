package com.solitaire.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

public class SolitaireGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	TextureRegion[][] topFrames;
	CardManager cardManger;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		cardManger = new CardManager();
		cardManger.MakeCards();

	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		//batch.draw(topFrames[0][0], 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
