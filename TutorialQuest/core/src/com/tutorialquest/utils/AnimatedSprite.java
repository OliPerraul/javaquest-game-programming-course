package com.tutorialquest.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class AnimatedSprite extends Sprite {

	public static final float DEFAULT_FRAME_LENGTH = 0.2f;

	protected float animationTime = 0;
	protected HashMap<Integer, Animation<TextureRegion>> animations = new HashMap<>();
	private Animation currentAnimation = null;
	public int currentAnimationID;
	public Texture spriteSheet;
	public TextureRegion[][] frames;

	public AnimatedSprite(String texturePath, Vector2 size) {
		super(size);
		spriteSheet = new Texture(texturePath);
		frames = TextureRegion.split(spriteSheet, getWidth(), getHeight());
	}

	public AnimatedSprite(String texturePath, int width, int height) {
		this(texturePath, new Vector2(width, height));
	}

	@Override
	public void dispose() {
		super.dispose();
		spriteSheet.dispose();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		animationTime += deltaTime;
	}

	public void addAnimation(
		int animation,
		float frameLength,
		Animation.PlayMode playmode,
		TextureRegion... frames)
	{
		animations.put(
			animation,
			new Animation(
				frameLength,
				new Array(frames),
				playmode
			));
	}

	public void play(int animation, boolean reset) {
		if (reset)
			animationTime = 0;

		if (animation == currentAnimationID)
			return;

		currentAnimationID = animation;
		currentAnimation = animations.get(currentAnimationID);
	}

	@Override
	public TextureRegion getTexture() {
		if(currentAnimation == null) return  null;
		return (TextureRegion) currentAnimation.getKeyFrame(animationTime);
	}
}
