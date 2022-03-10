package com.solitaire.game;

import com.badlogic.gdx.Game;
import com.solitaire.game.view.MenuScreen;

public class SolitaireGame extends Game {

	@Override
	public void create () {
		setScreen(new MenuScreen(this));
	}
}