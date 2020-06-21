package com.tutorialquest.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.Collider;
import com.tutorialquest.Game;
import com.tutorialquest.Item;
import com.tutorialquest.LootTable;
import com.tutorialquest.utils.AnimatedSprite;

import java.util.List;

public class Enemy extends Character {

	public class SpriteUtils
	{
		public static final int IDLE_FRONT = Character.SpriteUtils.IDLE_FRONT;
		public static final int WALK_FRONT = Character.SpriteUtils.WALK_FRONT;
		public static final int IDLE_SIDE = Character.SpriteUtils.IDLE_SIDE;
		public static final int WALK_SIDE = Character.SpriteUtils.WALK_SIDE;
		public static final int IDLE_BACK = Character.SpriteUtils.IDLE_BACK;
		public static final int WALK_BACK = Character.SpriteUtils.WALK_BACK;
	}

	public class StateUtils
	{
		public static final float IDLE_TIME_LIMIT = 2f;
		public static final float WANDER_DISTANCE_RANGE = 96f;
		public static final float WANDER_TIME_LIMIT = 5f;
		public static final float CHASE_DETECTION_RANGE = 128f;

		public static final int IDLE = 0;
		public static final int CHASE = 1;
		public static final int WANDER = 2;
	}

	private static final float MAX_HEALTH = 10f;
	private static final float DAMAGE = 25f;
	private static final float KNOCKBACK = 100f;
	private static final float SPEED = 40;
	private static final float PUSH_FORCE = 0.6f;

	private PhysicalObject chaseTarget = null;
	private Vector2 wanderTarget = new Vector2();
	public int state = StateUtils.IDLE;
	private float stateTime = 0;

	public void setState(int state)
	{
		stateTime = 0;
		switch (state)
		{
			case StateUtils.CHASE:
				controlAxes.setZero();
				locomotionVelocity.setZero();
				break;

			case StateUtils.IDLE:
				controlAxes.setZero();
				locomotionVelocity.setZero();
				break;

			case StateUtils.WANDER:
				wanderTarget
					.set(position)
					.add(new Vector2()
						.setToRandomDirection()
						.scl(StateUtils.WANDER_DISTANCE_RANGE));
				break;
		}

		this.state = state;
	}

	public void updateState(float deltaTime)
	{
		stateTime += deltaTime;
		switch (state)
		{
			case StateUtils.CHASE:
				if(
					chaseTarget == null ||
					position.dst(chaseTarget.position) > StateUtils.CHASE_DETECTION_RANGE)
				{
					setState(StateUtils.IDLE);
					return;
				}

				controlAxes
					.set(chaseTarget.position)
					.sub(position)
					.nor();

				locomotionVelocity
					.set(controlAxes)
					.scl(speed);

				break;

			case StateUtils.IDLE:

				if(
					chaseTarget != null &&
					position.dst(chaseTarget.position) < StateUtils.CHASE_DETECTION_RANGE)
				{
					setState(StateUtils.CHASE);
					return;
				}

				if(stateTime >= StateUtils.IDLE_TIME_LIMIT)
				{
					setState(StateUtils.WANDER);
					return;
				}

				break;

			case StateUtils.WANDER:

				if(
					chaseTarget != null &&
					position.dst(chaseTarget.position) < StateUtils.CHASE_DETECTION_RANGE)
				{
					setState(StateUtils.CHASE);
					return;
				}

				if(
					stateTime >= StateUtils.WANDER_TIME_LIMIT ||
					position.epsilonEquals(wanderTarget))
				{
					setState(StateUtils.IDLE);
					return;
				}

				controlAxes
					.set(wanderTarget)
					.sub(position)
					.nor();

				locomotionVelocity
					.set(controlAxes)
					.scl(speed);

				break;

		}
	}

	public void initLootTable()
	{
		lootTable = new LootTable();
		lootTable.add(
			new LootTable.Loot()
			{{
				type = Collectible.Type.Money;
				probability = 0.8f;
				value = Money.COPPER_COIN_VALUE;
			}},
			new LootTable.Loot()
			{{
				type = Collectible.Type.Money;
				probability = 0.5f;
				value = Money.SILVER_COIN_VALUE;
			}},
			new LootTable.Loot()
			{{
				type = Collectible.Type.Money;
				probability = 0.3f;
				value = Money.GOLD_COIN_VALUE;
			}},
			new LootTable.Loot()
			{{
				type = Collectible.Type.Money;
				probability = 0.08f;
				value = Money.DIAMOND_VALUE;
			}}
//			new LootTable.Loot()
//			{{
//				type = Collectible.Type.Item;
//				probability = 0.1f;
//				item = new Item() {{
//					name = "Boss Key";
//					type = Item.TYPE_KEY;
//					texturePath = "objects/boss_key.png";
//					properties.put(Item.PROP_TRANSITION_ID, 0);
//				}};
//			}}
		);
	}


	public void initSprite()
	{
		collider = new Collider(
			Collider.DEFAULT_SIZE,
			Collider.FLAG_ENEMY | Collider.FLAG_COLLIDABLE | Collider.FLAG_PUSHABLE);

		collider.origin = new Vector2(Collider.DEFAULT_SIZE.x/2, Collider.DEFAULT_SIZE.y/4);

		sprite = new AnimatedSprite("objects/monster_spritesheet.png", new Vector2(WIDTH,HEIGHT));
		sprite.origin = new Vector2(WIDTH/2, HEIGHT/4);
		sprite.addAnimation(
			SpriteUtils.WALK_FRONT,
			AnimatedSprite.DEFAULT_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[0][0],
			sprite.frames[0][1],
			sprite.frames[0][2]);

		sprite.addAnimation(
			SpriteUtils.IDLE_FRONT,
			AnimatedSprite.DEFAULT_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[0][1]);

		sprite.addAnimation(
			SpriteUtils.WALK_SIDE,
			AnimatedSprite.DEFAULT_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[0][3],
			sprite.frames[0][4],
			sprite.frames[0][5]
		);

		sprite.addAnimation(
			SpriteUtils.IDLE_SIDE,
			AnimatedSprite.DEFAULT_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[0][4]
		);

		sprite.addAnimation(
			SpriteUtils.WALK_BACK,
			AnimatedSprite.DEFAULT_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[0][6],
			sprite.frames[0][7],
			sprite.frames[0][8]
		);

		sprite.addAnimation(
			SpriteUtils.IDLE_BACK,
			AnimatedSprite.DEFAULT_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[0][7]
		);


		sprite.play(SpriteUtils.IDLE_FRONT, true);
	}


	public Enemy(Vector2 position) {
		super(position, DAMAGE, KNOCKBACK, MAX_HEALTH, SPEED, PUSH_FORCE);
		initSprite();
		initLootTable();
		setState(StateUtils.IDLE);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		updateState(deltaTime);
		turn();
		updateVelocity(deltaTime);
		collisionAttack(Collider.FLAG_AVATAR);
		push(Collider.FLAG_ENEMY);
		move();
	}

	@Override
	public void start() {
		List<Avatar> results = Game.level.find(Avatar.class);
		chaseTarget =  results.isEmpty() ? null : results.iterator().next();
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		sprite.render(spriteBatch, position);
		super.render(spriteBatch);
	}
}