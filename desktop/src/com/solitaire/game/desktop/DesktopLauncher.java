package com.solitaire.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.solitaire.game.SolitaireGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Solitaire";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new SolitaireGame(), config);
	}
}
