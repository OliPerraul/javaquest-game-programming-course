package com.tutorialquest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.entities.Enemy;
import com.tutorialquest.ui.HUD;
import com.tutorialquest.utils.Compatibility;
import com.tutorialquest.utils.GLUtils;
import com.tutorialquest.entities.Avatar;
import com.tutorialquest.utils.VectorUtils;


public class Game extends ApplicationAdapter {

	public static final int VIEWPORT_WIDTH = 640 * 2;
	public static final int VIEWPORT_HEIGHT = 360 * 2;
	public static final Color CORNFLOWER_BLUE = new Color(0.39f, 0.58f, 0.92f, 1);

	public static OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	public static Level level;
	public static HUD hud;
	private InputManager input;
	public static boolean isDebugRenderEnabled = false;

	@Override
	public void create() {
		Gdx.graphics.setWindowedMode(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		hud = new HUD();
		input = new InputManager();
		camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		camera.zoom = 0.25f;
		spriteBatch = new SpriteBatch();
		load("levels/overworld.tmx");
	}

	public static void load(String tilemapPath) {
		load(tilemapPath, -1, null);
	}

	public static void load(String tilemapPath, int id, Avatar avatar) {
		if (level != null) level.dispose(avatar);
		level = new Level(tilemapPath);
		level.load(id, avatar);
	}

	@Override
	public void render() {
		GLUtils.glClearColor(CORNFLOWER_BLUE);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(input.isDebugRenderJustPressed()) isDebugRenderEnabled = !isDebugRenderEnabled;

		level.update(Gdx.graphics.getDeltaTime());
		if(Game.level.avatar != null)
			camera.position.set(Game.level.avatar.position, 0);

		camera.update();
		hud.update(Gdx.graphics.getDeltaTime());

		spriteBatch.setProjectionMatrix(camera.combined);
		level.render(spriteBatch);
		hud.render();
	}
}
