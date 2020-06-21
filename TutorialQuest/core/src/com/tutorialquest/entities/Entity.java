
package com.tutorialquest.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.Game;


public class Entity {
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;

	protected ShapeRenderer renderer = new ShapeRenderer();
	public Vector2 position = new Vector2();

	public Entity(Vector2 position) {
		this.position = position;
	}

	public void start() {

	}

	public void dispose() {
	}

	public void update(float deltaTime) {
	}

	public void render(SpriteBatch spriteBatch) {
		// Draw origin
		if(!Game.isDebugRenderEnabled) return;
		renderer.setProjectionMatrix(Game.camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.circle(position.x, position.y, 2f);
		renderer.end();
	}
}