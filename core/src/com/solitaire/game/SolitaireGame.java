package com.solitaire.game;

import com.badlogic.gdx.Game;
import com.solitaire.game.view.MenuScreen;
import com.solitaire.game.view.SplashScreen;

public class SolitaireGame extends Game {

	@Override
	public void create () {
		setScreen(new SplashScreen(this));
	}
}