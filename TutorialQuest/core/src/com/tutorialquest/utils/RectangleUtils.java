package com.tutorialquest.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

public class RectangleUtils {

	public static Vector2 bottomLeft(Rectangle rect) {
		return new Vector2(rect.x, rect.y);
	}

	public static Vector2 bottomRight(Rectangle rect) {
		return new Vector2(rect.x + rect.width, rect.y);
	}

	public static Vector2 topLeft(Rectangle rect) {
		return new Vector2(rect.x, rect.y + rect.height);
	}

	public static Vector2 topRight(Rectangle rect) {
		return new Vector2(rect.x + rect.width, rect.y + rect.height);
	}

	public static float left(Rectangle rect) {
		return rect.x;
	}

	public static float right(Rectangle rect) {
		return rect.x + rect.width;
	}

	public static float top(Rectangle rect) {
		return rect.y + rect.height;
	}

	public static float bottom(Rectangle rect) {
		return rect.y;
	}

}
