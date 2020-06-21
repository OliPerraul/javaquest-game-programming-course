package com.tutorialquest.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class VectorUtils {
	public static Vector3 toVector3(Vector2 vector2) {
		return new Vector3(vector2.x, vector2.y, 0);
	}
}
