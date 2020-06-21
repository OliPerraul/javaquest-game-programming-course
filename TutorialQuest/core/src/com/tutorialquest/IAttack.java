package com.tutorialquest;

import com.badlogic.gdx.math.Vector2;

public interface IAttack {
	float getDamage();
	float getKnockback();
	Vector2 getDirection();
}
