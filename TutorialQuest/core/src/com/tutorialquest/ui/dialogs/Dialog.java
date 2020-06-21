package com.tutorialquest.ui.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.tutorialquest.Game;

public class Dialog {

	public static final int WIDTH = 256;
	public static final int HEIGHT = 128/2;
	public static final int TEXTURE_SIZE = 24;
	public static final float OFFSET_X = -WIDTH/2;
	public static final float OFFSET_Y = -HEIGHT/2;
	public static final float MARGIN = 8;
	public static final float ICON_SIZE = 16;
	private static final int FONT_SCALE = 1;

	protected NinePatch ninePatch;
	protected String visibleText = "";
	protected int currentTextIndex = 0;
	protected String currentText;
	protected String[] texts;
	protected Texture arrowTexture;
	protected TextureRegion[][] textures;
	protected BitmapFont font;
	protected final static float SPEED = 10;
	protected final static float FAST_FORWARD = 2.5f;
	protected float currentTextProgress = 0;
	protected int currentTextLength = 0;
	protected boolean finished = false;
	protected boolean enabled = false;

	public void reset(String text)
	{
		finished = false;
		currentTextProgress = 0;
		visibleText = "";
		currentText = new String(text);
		currentTextLength = currentText.length();
	}

	public Dialog()
	{
		arrowTexture = new Texture("ui/arrow_down.png");
		textures = TextureRegion.split(new Texture("ui/rounded.png"), TEXTURE_SIZE/3, TEXTURE_SIZE/3);
		font = new BitmapFont(Gdx.files.internal("fonts/player2_small.fnt"));
		font.setColor(Color.DARK_GRAY);
		font.getData().setScale(FONT_SCALE);

		this.ninePatch = new NinePatch(
			textures[0][0], textures[0][1], textures[0][2],
			textures[1][0], textures[1][1], textures[1][2],
			textures[2][0], textures[2][1], textures[2][2]);
	}

	public void open()
	{
		Game.level.avatar.input.disable(-1);
		Game.hud.input.disable(.25f);
		enabled = true;
	}

	public void close()
	{
		enabled = false;
		Game.level.avatar.input.disable(.25f);
		Game.hud.input.disable(-1);
	}

	public void update(float deltaTime)
	{
		if(!enabled) return;

		if(currentText == null || currentText.equals(""))
			return;

		currentTextProgress += deltaTime * (Game.hud.input.isInteractPressed() ? SPEED * FAST_FORWARD : SPEED);
		currentTextProgress = currentTextProgress < currentTextLength ? currentTextProgress : currentTextLength;
		visibleText = currentText.substring(0, (int) currentTextProgress);
	}

	public void render(
		SpriteBatch spriteBatch,
		Camera camera,
		Vector2 position)
	{
		if(!enabled) return;

		spriteBatch.begin();
		ninePatch.draw(spriteBatch, position.x, position.y, 0, 0, WIDTH, HEIGHT, 1, 1, 0);
		spriteBatch.end();

		// Draw text
		spriteBatch.begin();
		font.draw(
			spriteBatch,
			visibleText,
			position.x + (MARGIN) * 1,
			position.y + (HEIGHT - MARGIN) * 1,
			(WIDTH - MARGIN * 2) * 1,
			Align.left,
			true);

		spriteBatch.flush();
		spriteBatch.end();

	}
}
