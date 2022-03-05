package com.solitaire.game;

import com.badlogic.gdx.Game;

public class SolitaireGame extends Game {

	@Override
	public void create () {
		setScreen(new MenuScreen(this));
	}
}