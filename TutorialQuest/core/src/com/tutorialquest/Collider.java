package com.tutorialquest;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.tutorialquest.utils.RectangleUtils;
import com.tutorialquest.utils.TiledUtils;

import com.tutorialquest.entities.Entity;
import com.tutorialquest.entities.PhysicalObject;

import java.util.LinkedList;
import java.util.List;

public class Collider {

	public static final int FLAG_NONE = 0;
	public static final int FLAG_AVATAR = 1 << 0;
	public static final int FLAG_ENEMY = 1 << 1;
	public static final int FLAG_COLLIDABLE = 1 << 2;
	public static final int FLAG_PUSHABLE = 1 << 3;
	public static final int FLAG_COLLECTIBLE = 1 << 4;
	public static final int FLAG_TRANSITION = 1 << 5;
	public static final int FLAG_INTERACTIBLE = 1 << 6;
	public static final int FLAG_DOOR = 1 << 7;

	public static final Vector2 DEFAULT_SIZE = new Vector2(10, 8);
	public Rectangle rect;
	private ShapeRenderer renderer;
	public Vector2 origin = new Vector2();
	public int collisionFlags = FLAG_NONE;
	private List<PhysicalObject> results = new LinkedList<>();

	public Collider() {
		this.rect = new Rectangle(0, 0, DEFAULT_SIZE.x, DEFAULT_SIZE.y);
		this.renderer = new ShapeRenderer();
	}

	public Collider(
		Vector2 size,
		int flags)
	{
		this();
		this.rect.setSize(size.x, size.y);
		this.collisionFlags = flags;
	}

	public boolean isColliding(PhysicalObject object, Vector2 velocity, int collisionMask)
	{
		float vx = MathUtils.ceil(Math.abs(velocity.x)) * Math.signum(velocity.x);
		float vy = MathUtils.ceil(Math.abs(velocity.y)) * Math.signum(velocity.y);

		float horizontalSide = vx > 0 ?
			RectangleUtils.right(rect) :
			RectangleUtils.left(rect);
		float verticalSide = vy > 0 ?
			RectangleUtils.top(rect) :
			RectangleUtils.bottom(rect);

		if (isCollidingTilemapHorizontal(vx, horizontalSide) ||
			isCollidingTilemapVertical(vy, verticalSide) ||
			getObjectCollisions(object, vx, 0, collisionMask, results) ||
			getObjectCollisions(object, 0, vy, collisionMask, results))
		{
			return true;
		}

		return false;
	}


	public boolean isCollidingTilemapHorizontal(float xvelocity, float side) {
		return TiledUtils.worldToCell(
			Level.collisionLayer,
			side + xvelocity,
			RectangleUtils.top(rect)) != null ||
			TiledUtils.worldToCell(
				Level.collisionLayer,
				side + xvelocity,
				RectangleUtils.bottom(rect)) != null;
	}

	public boolean isCollidingHorizontal(PhysicalObject source, float xvelocity, float side, int collisionMask) {
		return
			isCollidingTilemapHorizontal(xvelocity, side) ||
				getObjectCollisions(source, xvelocity, 0, collisionMask, results);
	}

	public boolean isCollidingTilemapVertical(float yvelocity, float side) {
		return
			TiledUtils.worldToCell(
				Level.collisionLayer,
				RectangleUtils.left(rect),
				side + yvelocity) != null ||
				TiledUtils.worldToCell(
					Level.collisionLayer,
					RectangleUtils.right(rect),
					side + yvelocity) != null;
	}


	public boolean isCollidingVertical(PhysicalObject object, float yvelocity, float side, int collisionMask) {
		return
			isCollidingTilemapVertical(yvelocity, side) ||
				getObjectCollisions(object, 0, yvelocity, collisionMask, results);
	}

	public boolean getObjectCollisions(
		PhysicalObject object,
		float ofsx,
		float ofsy,
		int collisionMask,
		List<PhysicalObject> results)
	{
		results.clear();
		PhysicalObject result;

		if(collisionMask == 0) return false;

		for (Entity ent : Game.level.entities) {
			if (ent == object)
				continue;

			// Nous sommes seulement concerne par les objets physiques
			result = ent instanceof PhysicalObject ? (PhysicalObject) ent : null;

			if(result == null)
				continue;

			if(result.collider == null)
				continue;

			// AJOUT: Si flag du masque n'est pas actif, on ignore la collision
			if((result.collider.collisionFlags & collisionMask) == 0)
				continue;

			if (Intersector.overlaps(
				new Rectangle(
					rect.x + MathUtils.round(ofsx),
					rect.y + MathUtils.round(ofsy),
					rect.width,
					rect.height),
				result.collider.rect))
			{
				results.add(result);
			}
		}

		return !results.isEmpty();
	}

	private Rectangle intersection = new Rectangle();
	private Vector2 overlap = new Vector2();
	private float PUSH_SPEED = 1f;
	public Vector2 updateObject(
		PhysicalObject object,
		int collisionMask)
	{
		//--- Horizontal Collision ---//
		// Calcul de collision effectue a l'aide de l'arrondi
		// afin d'eviter 'sub-pixel movement' qui pourrait empecher de collisionner avec le mur
		float sx = Math.signum(object.velocity.x);
		float sy = Math.signum(object.velocity.y);
		float cvx = MathUtils.ceil(Math.abs(object.velocity.x)) * sx;
		float cvy = MathUtils.ceil(Math.abs(object.velocity.y)) * sy;

		// Determine si la gauche ou la droite va rentrer en collision
		// en fonction de la vitesse actuelle
		float horizontalSide = cvx > 0 ?
			RectangleUtils.right(rect) :
			RectangleUtils.left(rect);
		float verticalSide = cvy > 0 ?
			RectangleUtils.top(rect) :
			RectangleUtils.bottom(rect);

		if(getObjectCollisions(object, 0, 0, collisionMask, results))
		{
			for(PhysicalObject res : results)
			{
				float distx = Math.signum(object.position.x - res.position.x) * PUSH_SPEED;
				if (!isCollidingTilemapHorizontal(0, horizontalSide)) {
					object.position.x += distx;
				}
				float disty = Math.signum(object.position.y - res.position.y) * PUSH_SPEED;
				if (!isCollidingTilemapVertical(0, verticalSide)) {
					object.position.y += disty;
				}
			}
		}

		// Si l'objet s'appretais a collisioner dans le 'update' courrant
		if (isCollidingHorizontal(object, cvx, horizontalSide, collisionMask)) {
			// Tant que l'object n'entre pas en contact avec le mur
			for (int i = 0; i < Math.abs(cvx); i++) {
				// Je deplace l'objet pixel par pixel jusqu'a temps qu'il soit juste a cote du mur.
				if (!isCollidingHorizontal(object, sx, horizontalSide, collisionMask)) {
					object.position.x += sx;
				}
			}

			// J'annule la velocite de l'objet afin qu'il ne puisse pas depasser le mur
			object.velocity.x = 0;
		}

		//--- Vertical Collision ---//
		if (isCollidingVertical(object, cvy, verticalSide, collisionMask)) {
			for (int i = 0; i < Math.abs(cvy); i++) {
				if (!isCollidingVertical(object, sy, verticalSide, collisionMask)) {
					object.position.y +=  sy;
				}
			}

			object.velocity.y = 0;
		}

		return object.velocity;
	}

	public void update(Vector2 position) {
		// Je met a jour la position du 'Collider' en fonction de celle de l'objet
		rect.setPosition(
			position.x - origin.x,
			position.y - origin.y);
	}

	public void render() {
		renderer.setProjectionMatrix(Game.camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.setColor(Color.GREEN);
		renderer.rect(rect.x, rect.y, rect.width, rect.height);
		renderer.end();
	}
}
