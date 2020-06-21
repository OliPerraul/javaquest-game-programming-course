package com.tutorialquest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputManager {

	public static final float DISABLE_TIME = 1f;

	private float disabledTime = 0;
	private float disabledTimeLimit = 0;
	private boolean enabled = true;

	public boolean isDebugRenderJustPressed() {
		if (!enabled) return false;
		return Gdx.input.isKeyJustPressed(Input.Keys.F1);

	}

	public boolean isRollJustPressed() {
		if (!enabled) return false;
		return
			Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) ||
				Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT) ||
				Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) ||
				Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT);
	}

	public boolean isAnyJustPressed() {
		if (!enabled) return false;
		return Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY);
	}

	public boolean isAttackJustPressed() {
		if (!enabled) return false;
		return Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
	}

	public boolean isMenuJustPressed() {
		if (!enabled) return false;
		return Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
	}


	public boolean isInteractJustPressed() {
		if (!enabled) return false;
		return Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
	}

	public boolean isInteractPressed() {
		if (!enabled) return false;
		return Gdx.input.isKeyPressed(Input.Keys.ENTER);
	}

	public boolean isUpJustPressed() {
		if (!enabled) return false;
		return Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP);
	}

	public boolean isUpPressed() {
		if (!enabled) return false;
		return Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
	}

	public boolean isDownJustPressed() {
		if (!enabled) return false;
		return Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN);
	}

	public boolean isDownPressed() {
		if (!enabled) return false;
		return Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
	}

	public boolean isLeftPressed() {
		if (!enabled) return false;
		return Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
	}

	public boolean isLeftJustPressed() {
		if (!enabled) return false;
		return Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT);
	}

	public boolean isRightJustPressed() {
		if (!enabled) return false;
		return Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT);
	}

	public boolean isRightPressed() {
		if (!enabled) return false;
		return Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
	}

	public void enable() {
		enabled = true;
	}

	public void disable(float disableTime) {
		enabled = false;
		disabledTime = 0;
		disabledTimeLimit = disableTime;
	}

	public void update(float deltaTime) {
		if (enabled) return;
		if (disabledTimeLimit < 0) return;
		if (disabledTime < disabledTimeLimit) disabledTime += deltaTime;
		else enabled = true;
	}
}
