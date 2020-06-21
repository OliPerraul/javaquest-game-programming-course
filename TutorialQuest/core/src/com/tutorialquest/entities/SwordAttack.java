package com.tutorialquest.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.Collider;
import com.tutorialquest.Game;
import com.tutorialquest.IAttack;
import com.tutorialquest.utils.AnimatedSprite;
import com.tutorialquest.utils.Utils;

import java.util.LinkedList;
import java.util.List;

public class SwordAttack extends PhysicalObject implements IAttack
{
	public class SpriteUtils
	{
		public static final float FRAME_LENGTH = 0.1f;

		public static final int ANIM_SLASH_FORWARD = 1;
		public static final int ANIM_SLASH_SIDE = 3;
	}


	public static final float WIDTH = 8;
	public static final float HEIGHT = 12;

	public static final float SPRITE_WIDTH = 32;
	public static final float SPRITE_HEIGHT = 32;

	private List<PhysicalObject> hits = new LinkedList<>();
	private List<PhysicalObject> collisionResults = new LinkedList<>();
	private AnimatedSprite sprite;

	private float timeLimit = 0.4f;
	private float elapsedTime = 0;
	protected Vector2 direction;
	private float damage = 10;
	private float knockback = 40f;
	private int mask = 0;
	public boolean finished = false;

	public float getDamage() {return damage;}
	public float getKnockback() {return knockback; }
	public Vector2 getDirection() { return direction;}

	public void initSprite()
	{
		this.collider = new Collider(
			Collider.DEFAULT_SIZE,
			Collider.FLAG_NONE);
		this.collider.origin = new Vector2(WIDTH/2, HEIGHT/2);

		this.sprite = new AnimatedSprite(
			"objects/effect_spritesheet.png",
			new Vector2(
				SPRITE_WIDTH,
				SPRITE_HEIGHT));

		this.sprite.origin = new Vector2(SPRITE_WIDTH/2, SPRITE_HEIGHT/2);

		sprite.addAnimation(
			SpriteUtils.ANIM_SLASH_FORWARD,
			SpriteUtils.FRAME_LENGTH,
			Animation.PlayMode.NORMAL,
			sprite.frames[0][0],
			sprite.frames[0][1],
			sprite.frames[0][2]
		);

		sprite.addAnimation(
			SpriteUtils.ANIM_SLASH_SIDE,
			SpriteUtils.FRAME_LENGTH,
			Animation.PlayMode.NORMAL,
			sprite.frames[1][0],
			sprite.frames[1][1],
			sprite.frames[1][2]
		);
	}

	public void resize(Vector2 size)
	{
		collider.rect.setSize(size.x, size.y);
		collider.origin = new Vector2(size.x/2, size.y/2);
	}

	public SwordAttack(
		Vector2 position,
		Vector2 direction,
		float damage,
		float knockback,
		int mask)
	{
		super(position);

		this.knockback = knockback;
		this.damage = damage;
		this.direction = direction;
		this.mask = mask;

		initSprite();

		Utils.Direction fixedDirection = Utils.toDirection(direction);
		switch (fixedDirection) {
			case LEFT:
			case RIGHT:
				sprite.flipX = fixedDirection == Utils.Direction.RIGHT;
				sprite.flipY = false;
				sprite.play(SpriteUtils.ANIM_SLASH_SIDE, true);
				break;

			case UP:
			case DOWN:
				sprite.flipX = false;
				sprite.flipY = fixedDirection == Utils.Direction.UP;
				sprite.play(SpriteUtils.ANIM_SLASH_FORWARD, true);
				break;
		}

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		collider.update(position);
		sprite.update(deltaTime);

		List<PhysicalObject> collisionResults = new LinkedList<>();
		collider.getObjectCollisions(
			this,
			0,
			0,
			mask,
			collisionResults);

		for(PhysicalObject result : collisionResults)
		{
			if(hits.contains(result))
				return;

			result.onAttacked(this);
			hits.add(result);
		}

		elapsedTime += deltaTime;
		if(elapsedTime > timeLimit) {
			Game.level.remove(this);
			finished = true;
		}
	}


	@Override
	public void render(SpriteBatch spriteBatch){
		sprite.render(spriteBatch, position);
		super.render(spriteBatch);
	}
}
