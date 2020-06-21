package com.tutorialquest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tutorialquest.Game;
import com.tutorialquest.utils.Compatibility;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Compatibility.platform = new DesktopCompatibility();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Game(), config);
	}
}
