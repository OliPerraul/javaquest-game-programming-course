
package com.tutorialquest.entities;

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.*;
import com.tutorialquest.utils.AnimatedSprite;
import com.tutorialquest.utils.Sprite;
import com.tutorialquest.utils.Utils;

import java.util.LinkedList;
import java.util.List;


public class Avatar extends Character {

	public static class SpriteUtils {

		public static final float WALK_FRAME_LENGTH = 0.1f;

		public static final int IDLE_FRONT = Character.SpriteUtils.IDLE_FRONT;
		public static final int WALK_FRONT = Character.SpriteUtils.WALK_FRONT;
		public static final int IDLE_SIDE = Character.SpriteUtils.IDLE_SIDE;
		public static final int WALK_SIDE = Character.SpriteUtils.WALK_SIDE;
		public static final int IDLE_BACK = Character.SpriteUtils.IDLE_BACK;
		public static final int WALK_BACK = Character.SpriteUtils.WALK_BACK;

		public static final float ATTACK_FRAME_LENGTH = 0.1f;
		public static final int SLASH_FRONT = 13;
		public static final int SLASH_BACK = 14;
		public static final int SLASH_SIDE = 15;

		public static final float ROLL_FRAME_LENGTH = 0.1f;
		public static final int ROLL_FRONT = 10;
		public static final int ROLL_SIDE = 11;
		public static final int ROLL_BACK = 12;
	}

	protected static int WIDTH = 32;
	protected static int HEIGHT = 32;

	private static final float INTERACTION_RANGE = 8f;
	private static final float ROLL_TIME_LIMIT = 0.4f;
	protected static final float ROLL_SPEED = 65f;
	private static final float ATTACK_RANGE = 8f;
	protected static final float SPEED = 45f;
	protected static final float DAMAGE = 4f;
	protected static final float MAX_HEALTH = 2000f;
	protected static final float KNOCKBACK = 200;
	protected static final float PUSH_FORCE = 0.5f;

	private float rollTime = ROLL_TIME_LIMIT;
	private Vector2 rollVelocity = new Vector2();
	public InputManager input = new InputManager();
	public Inventory inventory = new Inventory();
	private SwordAttack attack;

	public float getDamage() {
		return
			inventory.sword != null &&
				inventory.sword.hasProperty(Item.PROP_DAMAGE)
				?
				inventory.sword.getFloat(Item.PROP_DAMAGE) :
				damage;
	}

	public float getKnockback() {
		return
			inventory.sword != null &&
				inventory.sword.hasProperty(Item.PROP_KNOCKBACK)
				?
				inventory.sword.getFloat(Item.PROP_KNOCKBACK) :
				knockback;
	}


	public void initInventory() {
		inventory.add(
			new Item() {{
				name = "Gold Sword";
				type = Item.TYPE_EQUIP;
				properties.put(Item.PROP_EQUIP_TYPE, Item.EQUIP_TYPE_SWORD);
				properties.put(Item.PROP_DAMAGE, 5f);
				properties.put(Item.PROP_KNOCKBACK, 4f);
			}},
			new Item() {{
				name = "Medium Potion";
				type = Item.TYPE_CONSUMABLE;
				properties.put(Item.PROP_HEALTH, 5f);
			}}
//			new Item() {{
//				name = "Boss Key";
//				type = Item.TYPE_KEY;
//				properties.put(Item.PROP_TRANSITION_ID, 2);
//			}}
		);
	}

	public void initSprite()
	{
		collider = new Collider(
			Collider.DEFAULT_SIZE,
			Collider.FLAG_AVATAR | Collider.FLAG_COLLIDABLE | Collider.FLAG_PUSHABLE);
		collider.origin = new Vector2(Collider.DEFAULT_SIZE.x / 2, Collider.DEFAULT_SIZE.y / 2);

		sprite = new AnimatedSprite("objects/avatar_spritesheet.png", WIDTH, HEIGHT);
		sprite.origin = new Vector2(WIDTH/2, HEIGHT/8);
		sprite.addAnimation(
			SpriteUtils.WALK_FRONT,
			SpriteUtils.WALK_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[3][0],
			sprite.frames[3][1],
			sprite.frames[3][2],
			sprite.frames[3][3],
			sprite.frames[3][4],
			sprite.frames[3][5]);

		sprite.addAnimation(
			SpriteUtils.IDLE_FRONT,
			SpriteUtils.WALK_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[3][0]);

		sprite.addAnimation(
			SpriteUtils.WALK_SIDE,
			SpriteUtils.WALK_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[0][0],
			sprite.frames[0][1],
			sprite.frames[0][2],
			sprite.frames[0][3],
			sprite.frames[0][4],
			sprite.frames[0][5]
		);

		sprite.addAnimation(
			SpriteUtils.IDLE_SIDE,
			SpriteUtils.WALK_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[0][0]
		);

		sprite.addAnimation(
			SpriteUtils.WALK_BACK,
			SpriteUtils.WALK_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[1][0],
			sprite.frames[1][1],
			sprite.frames[1][2],
			sprite.frames[1][3],
			sprite.frames[1][4],
			sprite.frames[1][5]
		);

		sprite.addAnimation(
			SpriteUtils.IDLE_BACK,
			SpriteUtils.WALK_FRAME_LENGTH,
			Animation.PlayMode.LOOP,
			sprite.frames[1][0]
		);


		sprite.addAnimation(
			SpriteUtils.SLASH_FRONT,
			SpriteUtils.ATTACK_FRAME_LENGTH,
			Animation.PlayMode.NORMAL,
			sprite.frames[7][0],
			sprite.frames[7][1],
			sprite.frames[7][2],
			sprite.frames[7][3]
		);

		sprite.addAnimation(
			SpriteUtils.SLASH_SIDE,
			SpriteUtils.ATTACK_FRAME_LENGTH,
			Animation.PlayMode.NORMAL,
			sprite.frames[4][0],
			sprite.frames[4][1],
			sprite.frames[4][2],
			sprite.frames[4][3]
		);

		sprite.addAnimation(
			SpriteUtils.SLASH_BACK,
			SpriteUtils.ATTACK_FRAME_LENGTH,
			Animation.PlayMode.NORMAL,
			sprite.frames[5][0],
			sprite.frames[5][1],
			sprite.frames[5][2],
			sprite.frames[5][3]
		);

		sprite.addAnimation(
			SpriteUtils.ROLL_FRONT,
			SpriteUtils.ROLL_FRAME_LENGTH,
			Animation.PlayMode.NORMAL,
			sprite.frames[11][0],
			sprite.frames[11][1],
			sprite.frames[11][2],
			sprite.frames[11][3],
			sprite.frames[11][4]
		);

		sprite.addAnimation(
			SpriteUtils.ROLL_SIDE,
			SpriteUtils.ROLL_FRAME_LENGTH,
			Animation.PlayMode.NORMAL,
			sprite.frames[8][0],
			sprite.frames[8][1],
			sprite.frames[8][2],
			sprite.frames[8][3],
			sprite.frames[8][4]
		);

		sprite.addAnimation(
			SpriteUtils.ROLL_BACK,
			SpriteUtils.ROLL_FRAME_LENGTH,
			Animation.PlayMode.NORMAL,
			sprite.frames[9][0],
			sprite.frames[9][1],
			sprite.frames[9][2],
			sprite.frames[9][3],
			sprite.frames[9][4]
		);

		sprite.play(SpriteUtils.IDLE_FRONT, true);
	}



	@Override
	public void start() {
		super.start();
		onHealthChangedHandler.subscribe(Game.hud::onAvatarHealthChanged);
		onMoneyChangedHandler.subscribe(Game.hud::onAvatarMoneyChanged);
	}

	public Avatar(Vector2 position) {
		super(position, DAMAGE, KNOCKBACK, MAX_HEALTH, SPEED, PUSH_FORCE);
		initSprite();
		initInventory();
	}

	public void updateMoney(float value) {
		money += value;
		onMoneyChangedHandler.invoke(this);
		if (money < 0)
			money = 0;
	}

	public boolean hasEnoughMoney(Item item) {
		return item.cost <= Game.level.avatar.money;
	}

	public void purchaseItem(Item item) {
		Game.level.avatar.updateMoney(-item.cost);
		inventory.add(item);
	}

	public boolean isEquipped(Item item)
	{
		return
			Game.level.avatar.inventory.sword == item ||
			Game.level.avatar.inventory.shield == item;
	}

	public void menu() {
		if (input.isMenuJustPressed()) {
			Game.hud.inventoryDialog.open(inventory);
		}
	}

	public void control(float deltaTime) {
		controlAxes.setZero();
		if (input.isRightPressed() && !input.isLeftPressed()) controlAxes.set(1f, controlAxes.y);
		else if (input.isLeftPressed() && !input.isRightPressed()) controlAxes.set(-1f, controlAxes.y);
		else controlAxes.set(0, controlAxes.y);

		if (input.isUpPressed() && !input.isDownPressed()) controlAxes.set(controlAxes.x, 1f);
		else if (input.isDownPressed() && !input.isUpPressed()) controlAxes.set(controlAxes.x, -1f);
		else controlAxes.set(controlAxes.x, 0);

		controlAxes.clamp(0f, 1f);
		locomotionVelocity
			.set(controlAxes)
			.scl(speed);
	}

	@Override
	public void playAnimation(int anim, boolean reset, boolean force) {
		if (!force) {
			if (attack != null) return;
			if (rollTime < ROLL_TIME_LIMIT) return;
		}

		super.playAnimation(anim, reset, force);
	}

	public void attack() {

		if (attack != null && !attack.direction.epsilonEquals(Utils.toVector(fixedDirection)))
			cancelAttack();

		if (rollTime < ROLL_TIME_LIMIT) return;

		Vector2 attackOffset =
			fixedDirection == Utils.Direction.LEFT ||
				fixedDirection == Utils.Direction.RIGHT ?
				new Vector2(ATTACK_RANGE, 0)
					.scl(fixedDirection == Utils.Direction.RIGHT ? 2 : -2) :
				new Vector2(0, ATTACK_RANGE)
					.scl(fixedDirection == Utils.Direction.UP ? 2 : -2);

		Vector2 attackSize =
			fixedDirection == Utils.Direction.LEFT ||
				fixedDirection == Utils.Direction.RIGHT ?
				new Vector2(SwordAttack.HEIGHT, SwordAttack.WIDTH) :
				new Vector2(SwordAttack.WIDTH, SwordAttack.HEIGHT);

		if (input.isAttackJustPressed()) {
			cancelAttack();
			Game.level.add(
				attack = new SwordAttack(
					new Vector2(position).add(attackOffset),
					Utils.toVector(fixedDirection),
					getDamage(),
					getKnockback(),
					Collider.FLAG_ENEMY
				));

			switch (fixedDirection) {
				case LEFT:
				case RIGHT:
					playAnimation(
						SpriteUtils.SLASH_SIDE,
						true,
						true);
					break;

				case UP:
				case DOWN:
					playAnimation(
						fixedDirection == Utils.Direction.UP ?
							SpriteUtils.SLASH_BACK :
							SpriteUtils.SLASH_FRONT,
						true,
						true);
					break;
			}
		}

		if (attack != null) {
			if (attack.finished) {
				cancelAttack();
				return;
			}

			attack.position
				.set(position)
				.add(attackOffset);

			attack.resize(attackSize);
		}
	}

	public void cancelAttack() {
		if (attack != null) {
			Game.level.remove(attack);
			attack = null;
		}
	}

	public void roll(float deltaTime) {

		if (rollTime < ROLL_TIME_LIMIT) rollTime += deltaTime;
		else if (!input.isRollJustPressed()) rollVelocity.setZero();
		else {
			rollVelocity
				.set(direction)
				.scl(ROLL_SPEED);
			switch (fixedDirection) {
				case RIGHT:
					sprite.flipX = false;
					playAnimation(SpriteUtils.ROLL_SIDE, true, true);
					break;

				case LEFT:
					sprite.flipX = true;
					playAnimation(SpriteUtils.ROLL_SIDE, true, true);
					break;

				case DOWN:
					sprite.flipX = false;
					playAnimation(SpriteUtils.ROLL_FRONT, true, true);
					break;

				case UP:
					sprite.flipX = false;
					playAnimation(SpriteUtils.ROLL_BACK, true, true);
					break;
			}

			rollTime = 0;
		}
	}

	@Override
	public void updateVelocity(float deltaTime) {
		velocity.setZero()
			.add(locomotionVelocity)
			.add(pushedVelocity)
			.add(rollVelocity)
			.scl(deltaTime);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		control(deltaTime);
		turn();
		roll(deltaTime);
		updateVelocity(deltaTime);
		push(Collider.FLAG_PUSHABLE);
		move();
		attack();
		interact();
		menu();

		input.update(deltaTime);
	}

	public boolean unlock(int transitionID) {
		List<PhysicalObject> results = new LinkedList<>();
		Vector2 interactionOffset = new Vector2(direction).scl(INTERACTION_RANGE);

		if (!collider.getObjectCollisions(
			this,
			interactionOffset.x,
			interactionOffset.y,
			Collider.FLAG_DOOR,
			results)) return false;

		PhysicalObject next = results.iterator().next();
		Door door = next instanceof Door ? (Door) next : null;
		if (door == null) return false;
		if (door.transitionID != transitionID) return false;

		door.unlock();
		return true;
	}

	public void interact() {
		if (
			input.isInteractJustPressed()) {
			Vector2 interactionOffset = new Vector2(direction).scl(INTERACTION_RANGE);
			List<PhysicalObject> results = new LinkedList<>();
			collider.getObjectCollisions(
				this,
				interactionOffset.x,
				interactionOffset.y,
				Collider.FLAG_INTERACTIBLE,
				results);

			for (PhysicalObject interactible : results) {
				((IInteractible) interactible).interact(this);
			}
		}
	}


	@Override
	public void render(SpriteBatch spriteBatch) {
		sprite.render(spriteBatch, position);
		super.render(spriteBatch);
	}
}