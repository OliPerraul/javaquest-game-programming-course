package com.tutorialquest.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

public class Sprite {

	private final static float FLASH_TIME_LIMIT = 0.1f;
	public static final ShaderProgram WHITE_SHADER = new ShaderProgram(
		Gdx.files.internal(
			"shaders/white.vert"),
		Gdx.files.internal(
			"shaders/white.frag"));

	private float flashTime = FLASH_TIME_LIMIT;
	public boolean flipX = false;
	public boolean flipY = false;
	public Vector2 size = new Vector2();
	public Vector2 origin = new Vector2();
	protected TextureRegion texture;

	protected int getWidth() {
		return (int) size.x;
	}
	protected int getHeight() {
		return (int) size.y;
	}
	public Vector2 getSize() {
		return size;
	}
	public TextureRegion getTexture() { return texture; }

	public Sprite(Vector2 size) {
		this.size = size;
	}

	public Sprite(String texturePath, int width, int height, int x, int y) {
		this.size = new Vector2(width, height);
		TextureRegion[][] frames = TextureRegion.split(new Texture(texturePath), width, height);
		this.texture = frames[y][x];
	}

	public Sprite(String texturePath, Vector2 size) {
		this.size = size;
		this.texture = new TextureRegion(new Texture(texturePath));
	}

	public Sprite(String texturePath, int width, int height) {
		this(texturePath, new Vector2(width, height));
	}

	public void dispose()
	{
		if(texture != null)
		texture.getTexture().dispose();
	}

	public void flash() { flashTime = 0; }

	public void update(float deltaTime) {
		if(flashTime < FLASH_TIME_LIMIT) flashTime += deltaTime;
	}

	public void render(SpriteBatch batch, Vector2 position) {

		if(getTexture() == null) return;

		batch.begin();
		if(flashTime < FLASH_TIME_LIMIT) {
			batch.setShader(WHITE_SHADER);
		}

		batch.draw(
			getTexture(),
			position.x - origin.x,
			position.y - origin.y,
			origin.x,
			origin.y,
			getWidth(),
			getHeight(),
			(flipX ? -1 : 1),
			(flipY ? -1 : 1),
			0);

		batch.setShader(null);
		batch.end();
	}
}