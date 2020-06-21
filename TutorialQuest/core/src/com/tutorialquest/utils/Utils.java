package com.tutorialquest.utils;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Utils {

	public static final int WIDTH = 640 * 2;
	public static final int HEIGHT = 480 * 2;

	public static Random random = new Random(System.currentTimeMillis());

	public enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}

	public static Vector2 toVector(String direction) {
		return toVector(toDirection(direction));
	}

	public static Vector2 toVector(Direction direction) {
		switch (direction) {
			case UP:
				return Vector2.Y.cpy();
			case DOWN:
				return Vector2.Y.cpy().scl(-1);
			case LEFT:
				return Vector2.X.cpy().scl(-1);
			default:
			case RIGHT:
				return Vector2.X.cpy();
		}
	}

	public static Direction toDirection(Vector2 direction) {
		if (direction.y < 0) return Direction.DOWN;
		else if (direction.y > 0) return Direction.UP;
		else if (direction.x < 0) return Direction.LEFT;
		else return Direction.RIGHT;
	}

	public static Direction toDirection(String direction) {
		switch (direction) {
			case "Left":
				return Utils.Direction.LEFT;
			case "Right":
				return Utils.Direction.RIGHT;
			case "Up":
				return Utils.Direction.UP;
			default:
			case "Down":
				return Utils.Direction.DOWN;
		}
	}
}
