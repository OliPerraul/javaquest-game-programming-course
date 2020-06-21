package com.tutorialquest.entities;

import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.Collider;
import com.tutorialquest.Game;
import com.tutorialquest.utils.StateMachine;

import java.util.Comparator;

public class BossStateUtils {

	public static final int STATE_IDLE = 0;
	public static final int STATE_DECIDE = 1;
	public static final int STATE_WAYPOINT = 2;
	public static final int STATE_SHOOT_SPIRAL = 3;
	public static final int STATE_MINIONS = 4;
	public static final int STATE_BOUNCE = 5;
	public static final int STATE_CHASE = 6;

	public static class State extends StateMachine.State {
		protected Boss boss;

		public State(Boss boss, StateMachine stateMachine, int id, float probability, float timeLimit, int timeoutState) {
			super(stateMachine, id, probability, timeLimit, timeoutState);
			this.boss = boss;
		}
	}

	public static class IdleState extends State {

		public IdleState(Boss boss, StateMachine stateMachine, int id, float probability, float timeLimit, int timeoutState) {
			super(boss, stateMachine, id, probability, timeLimit, timeoutState);
		}

		@Override
		public String getName() {
			return "Idle";
		}

		@Override
		public void enter() {
			super.enter();
			boss.locomotionVelocity.setZero();
			boss.controlAxes.setZero();
		}
	}

	public static class WaypointState extends State {
		private static final float SPEED = 45f;

		public WaypointState(Boss boss, StateMachine stateMachine, int id, float probability, float timeLimit, int timeoutState) {
			super(boss, stateMachine, id, probability, timeLimit, timeoutState);
		}

		@Override
		public String getName() {
			return "Waypoint";
		}

		@Override
		public void enter() {
			super.enter();
			boss.destination
				.set(boss.waypoints.isEmpty() ?
					boss.position :
					boss.waypoints
						.stream()
						.min(Comparator.comparing(x -> x.dst(boss.position)))
						.get());
		}

		@Override
		public boolean update(float deltaTime) {

			if (!super.update(deltaTime)) return false;

			if (boss.position.epsilonEquals(boss.destination, Boss.DESTINATION_EPSILON)) {
				stateMachine.setCurrentState(STATE_IDLE);
				return true;
			}

			boss.direction
				.set(boss.destination)
				.sub(boss.position)
				.nor();
			boss.controlAxes.set(boss.direction);
			boss.locomotionVelocity
				.set(boss.direction)
				.scl(SPEED);

			return true;
		}
	}


	public static class ShootState extends State {
		public float fireRateTime = 0;

		@Override
		public String getName() {
			return "Shoot";
		}

		public ShootState(Boss boss, StateMachine stateMachine, int id, float probability, float timeLimit, int timeoutState) {
			super(boss, stateMachine, id, probability, timeLimit, timeoutState);
		}
	}

	public static class ShootSpiralState extends ShootState {

		public static final float ROTATION_SPEED = 30f;

		@Override
		public String getName() {
			return "Shoot Spiral";
		}

		public ShootSpiralState(Boss boss, StateMachine stateMachine, int id, float probability, float timeLimit, int timeoutState) {
			super(boss, stateMachine, id, probability, timeLimit, timeoutState);
		}

		@Override
		public boolean update(float deltaTime) {
			if (!super.update(deltaTime)) return false;

			fireRateTime += deltaTime;
			if (fireRateTime >= Boss.FIRE_RATE) {
				fireRateTime = 0;
				Game.level.add(
					new ProjectileAttack(
						new Vector2()
							.add(boss.direction)
							.scl(Boss.PROJECTILE_DISTANCE)
							.add(boss.position),
						boss.direction.cpy(),
						Boss.PROJECTILE_DAMAGE,
						Boss.PROJECTILE_KNOCKBACK,
						Boss.PROJECTILE_SPEED)
				);

				boss.direction
					.rotate(ROTATION_SPEED)
					.nor();
			}

			return true;
		}

		@Override
		public void enter() {
			super.enter();
			boss.direction = new Vector2(1, 0);
			boss.playAnimation(Boss.SpriteUtils.SHOOT, true, true);
		}
	}

	public static class BounceState extends State {

		public static final float SPEED = 100f;

		public static final int NUM_BOUNCE = 5;
		public static final float BOUNCE_FACTOR_DELTA = 1.1f;
		public float bounceFactor = 1;
		public int numBounce = 0;

		public BounceState(Boss boss, StateMachine stateMachine, int id, float probability, float timeLimit, int timeoutState) {
			super(boss, stateMachine, id, probability, timeLimit, timeoutState);
		}


		@Override
		public String getName() {
			return "Bounce";
		}

		@Override
		public void enter() {
			super.enter();
			numBounce = 0;
			bounceFactor = 1;
			boss.direction.setToRandomDirection();
			boss.controlAxes.set(boss.direction);
			boss.locomotionVelocity
				.set(boss.direction)
				.scl(SPEED * bounceFactor);
		}

		@Override
		public boolean update(float deltaTime) {
			if (!super.update(deltaTime)) return false;

			if (boss.collider.isColliding(boss, boss.velocity, Collider.FLAG_NONE)) {
				if (numBounce >= NUM_BOUNCE) {
					stateMachine.setCurrentState(STATE_WAYPOINT);
					return true;
				}

				for (int i = 0; i < 100; i++) {
					boss.direction.setToRandomDirection();
					boss.controlAxes.set(boss.direction);
					boss.locomotionVelocity
						.set(boss.direction)
						.scl(SPEED * bounceFactor);
					boss.updateVelocity(deltaTime);

					if (!boss.collider.isColliding(boss, boss.velocity, Collider.FLAG_NONE)) {
						break;
					}
				}

				bounceFactor += BOUNCE_FACTOR_DELTA;
				numBounce++;
			}

			return true;
		}
	}

	public static class ChaseState extends State {

		private float timeLimitChase = 0.5f;
		public static final float SPEED = 60f;

		public ChaseState(Boss boss, StateMachine stateMachine, int id, float probability, float timeLimit, int timeoutState) {
			super(boss, stateMachine, id, probability, timeLimit, timeoutState);
		}

		@Override
		public String getName() {
			return "Chase";
		}

		@Override
		public void enter() {

		}

		@Override
		public boolean update(float deltaTime) {
			if (!super.update(deltaTime)) return false;

			if(Game.level.avatar == null) {
				boss.locomotionVelocity.setZero();
				boss.controlAxes.setZero();
				return false;
			}

			boss.direction
				.set(Game.level.avatar.position)
				.sub(boss.position)
				.nor();
			boss.controlAxes.set(boss.direction);
			boss.locomotionVelocity
				.set(boss.direction)
				.scl(SPEED);

			return true;
		}
	}

	public static class MinionsState extends State {

		public static final float PROJECTILE_DISTANCE = 4f;
		public static final int NUM_MINIONS = 3;

		public MinionsState(Boss boss, StateMachine stateMachine, int id, float probability, float timeLimit, int timeoutState) {
			super(boss, stateMachine, id, probability, timeLimit, timeoutState);
		}

		@Override
		public String getName() {
			return "Minions";
		}

		@Override
		public void enter() {
			super.enter();
			boss.playAnimation(Boss.SpriteUtils.SHOOT, true, true);

			for (int i = 0; i < NUM_MINIONS; i++) {
				boss.direction.setToRandomDirection();
				Game.level.add(
					new Enemy(
						new Vector2()
							.add(boss.direction)
							.scl(PROJECTILE_DISTANCE)
							.add(boss.position))
				);
			}
		}
	}
}
