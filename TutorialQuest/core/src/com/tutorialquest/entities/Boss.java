package com.tutorialquest.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.tutorialquest.Collider;
import com.tutorialquest.Game;
import com.tutorialquest.LootTable;
import com.tutorialquest.utils.AnimatedSprite;
import com.tutorialquest.utils.StateMachine;

import java.util.List;

public class Boss extends Character {

	public class SpriteUtils {
		public static final int IDLE_FRONT = Character.SpriteUtils.IDLE_FRONT;
		public static final int WALK_FRONT = Character.SpriteUtils.WALK_FRONT;
		public static final int IDLE_SIDE = Character.SpriteUtils.IDLE_SIDE;
		public static final int WALK_SIDE = Character.SpriteUtils.WALK_SIDE;
		public static final int IDLE_BACK = Character.SpriteUtils.IDLE_BACK;
		public static final int WALK_BACK = Character.SpriteUtils.WALK_BACK;
		public static final int SHOOT = 20;
	}

	public static final float FIRE_RATE = 0.05f;
	public static final float DESTINATION_EPSILON = 2;
	public static final float PROJECTILE_DISTANCE = 2.5f;
	public static final int WIDTH = 64;
	public static final int HEIGHT = 64;
	public static final float MAX_HEALTH = 10;
	public static final float DAMAGE = 15;
	public static final float PROJECTILE_DAMAGE = 5;
	public static final float PROJECTILE_SPEED = 40;
	public static final float KNOCKBACK = 200f;
	public static final float PROJECTILE_KNOCKBACK = 150f;
	public static final float SPEED = 40f;
	public static final float PUSH_FORCE = 0.8f;

	public Vector2 destination = new Vector2();
	public List<Vector2> waypoints;
	private ShapeRenderer renderer = new ShapeRenderer();
	private StateMachine stateMachine = new StateMachine();
	private BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/player2_small.fnt"));

	public void initStateMachine(){
		stateMachine.addState(new StateMachine.DecisionState(stateMachine, BossStateUtils.STATE_DECIDE, -1, -1, -1), true);
		stateMachine.addState(new BossStateUtils.WaypointState(this, stateMachine, BossStateUtils.STATE_WAYPOINT, -1, 5f, BossStateUtils.STATE_DECIDE), false);
		stateMachine.addState(new BossStateUtils.IdleState(this, stateMachine, BossStateUtils.STATE_IDLE, 0.5f, 1f, BossStateUtils.STATE_DECIDE), false);
		stateMachine.addState(new BossStateUtils.BounceState(this, stateMachine, BossStateUtils.STATE_BOUNCE, 0.5f, 10f, BossStateUtils.STATE_WAYPOINT), false);
		stateMachine.addState(new BossStateUtils.ChaseState(this, stateMachine, BossStateUtils.STATE_CHASE, 0.5f, 5f, BossStateUtils.STATE_WAYPOINT), false);
		stateMachine.addState(new BossStateUtils.MinionsState(this, stateMachine, BossStateUtils.STATE_MINIONS, 0.5f, 1f, BossStateUtils.STATE_IDLE), false);
		stateMachine.addState(new BossStateUtils.ShootSpiralState(this, stateMachine, BossStateUtils.STATE_SHOOT_SPIRAL, 0.5f, 1f, BossStateUtils.STATE_IDLE), false);
	}

	public void initSprite() {
		sprite = new AnimatedSprite("objects/boss_spritesheet.png", new Vector2(WIDTH, HEIGHT));
		sprite.addAnimation(
			SpriteUtils.IDLE_FRONT,
			AnimatedSprite.DEFAULT_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[0][1]
		);

		sprite.addAnimation(
			SpriteUtils.WALK_FRONT,
			AnimatedSprite.DEFAULT_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[0][0],
			sprite.frames[0][1],
			sprite.frames[0][2]
		);

		sprite.addAnimation(
			SpriteUtils.IDLE_SIDE,
			AnimatedSprite.DEFAULT_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[2][1]
		);

		sprite.addAnimation(
			SpriteUtils.WALK_SIDE,
			AnimatedSprite.DEFAULT_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[2][0],
			sprite.frames[2][1],
			sprite.frames[2][2]
		);

		sprite.addAnimation(
			SpriteUtils.IDLE_BACK,
			AnimatedSprite.DEFAULT_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[1][1]
		);

		sprite.addAnimation(
			SpriteUtils.WALK_BACK,
			AnimatedSprite.DEFAULT_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[1][0],
			sprite.frames[1][1],
			sprite.frames[1][2]
		);

		sprite.addAnimation(
			SpriteUtils.SHOOT,
			AnimatedSprite.DEFAULT_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[3][0],
			sprite.frames[3][1],
			sprite.frames[3][2],
			sprite.frames[3][3],
			sprite.frames[3][4],
			sprite.frames[3][5]
		);

		sprite.play(SpriteUtils.WALK_FRONT, true);
		sprite.origin = new Vector2(WIDTH/2, 0);
		collider = new Collider(
			new Vector2(WIDTH/2, 16),
			Collider.FLAG_COLLIDABLE | Collider.FLAG_ENEMY);
		collider.origin = new Vector2(WIDTH/4, 0);
	}

	public void initLootTable()
	{
		lootTable = new LootTable();
		lootTable.add(
			new LootTable.Loot()
			{{
				type = Collectible.Type.Money;
				probability = 1f;
				value = Money.GOLD_STACK_VALUE;
			}},
			new LootTable.Loot()
			{{
				type = Collectible.Type.Money;
				probability = 1f;
				value = Money.DIAMOND_VALUE;
			}}
		);
	}

	public Boss(
		Vector2 position,
		List<Vector2> waypoints)
	{
		super(position, DAMAGE, KNOCKBACK, MAX_HEALTH, SPEED, PUSH_FORCE);
		initSprite();
		initLootTable();
		initStateMachine();
		this.waypoints = waypoints;
	}

	@Override
	public void onDefeated() {
		super.onDefeated();
//		Game.finish(false);
	}

	@Override
	public void start() {
		super.start();
//		onHealthChangedHandler.subscribe(Game.hud::onBossHealthChanged);
//		Game.hud.bossHealthBar.enable(true);
//		Game.hud.onBossHealthChanged(this);
	}

	@Override
	public void playAnimation(int anim, boolean reset, boolean force) {
		if(!force)
		{
			if (anim != SpriteUtils.SHOOT &&
				(stateMachine.currentState.id == BossStateUtils.STATE_SHOOT_SPIRAL ||
					stateMachine.currentState.id == BossStateUtils.STATE_MINIONS)) return;
		}

		sprite.play(anim, reset);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		stateMachine.update(deltaTime);
		updateVelocity(deltaTime);
		turn();
		push(Collider.FLAG_NONE);
		collisionAttack(Collider.FLAG_AVATAR);
		move();
		collider.update(position);
	}

	@Override
	public void render(SpriteBatch spriteBatch) {

		sprite.render(spriteBatch, position);

		if(!Game.isDebugRenderEnabled) return;

		renderer.setProjectionMatrix(Game.camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		for (Vector2 wp : waypoints) {
			if (wp == null) continue;
			renderer.setColor(Color.GREEN);
			renderer.circle(wp.x, wp.y, 4f);
		}
		renderer.end();

		spriteBatch.begin();
		font.draw(
			spriteBatch,
			stateMachine.getStateName(),
			position.x,
			position.y,
			1,
			Align.left,
			true);

		spriteBatch.flush();
		spriteBatch.end();

		collider.render();

		super.render(spriteBatch);
	}
}
