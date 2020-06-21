package com.tutorialquest.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;


public class GLUtils {
	public static void glClearColor(Color color) {
		Gdx.gl.glClearColor(
			color.r,
			color.g,
			color.b,
			color.a);
	}
}
