package com.tutorialquest;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tutorialquest.entities.*;
import com.tutorialquest.entities.Character;
import com.tutorialquest.utils.FixOrthogonalTiledMapRenderer;
import com.tutorialquest.utils.Utils;

import java.util.*;

public class Level {

	public static final String LAYER_OBJECT = "Objects";
	public static final String LAYER_COLLISION = "Collision";

	public static final String OBJECT_PROP_X = "x";
	public static final String OBJECT_PROP_Y = "y";
	public static final String OBJECT_PROP_NAME = "name";
	public static final String OBJECT_PROP_TYPE = "type";
	public static final String OBJECT_PROP_TEXT = "Text";

	public static final String OBJECT_AVATAR = "Avatar";
	public static final String OBJECT_SLIME = "Slime";

	public static final String OBJECT_TRANSITION = "Transition";
	public static final String OBJECT_PROP_TRANSITION_LEVEL = "Level";
	public static final String OBJECT_PROP_TRANSITION_DIRECTION = "Direction";
	public static final String OBJECT_PROP_TRANSITION_ID = "TransitionID";

	public static final String OBJECT_NPC = "NPC";
	public static final String OBJECT_PROP_NPC_TYPE = "NPCType";
	public static final String OBJECT_SIGNPOST = "Sign";

	public static final String OBJECT_ITEM = "Item";
	public static final String OBJECT_PROP_ITEM_TYPE = "ItemType";
	public static final String OBJECT_PROP_ITEM_SPRITE = "Sprite";
	public static final String OBJECT_PROP_ITEM_COST = "Cost";
	public static final String OBJECT_PROP_ITEM_IS_SHOP_ITEM = "IsShopItem";

	public static final String OBJECT_DOOR = "Door";
	public static final String OBJECT_PROP_DOOR_LEVEL = "Level";
	public static final String OBJECT_PROP_DOOR_DIRECTION = "Direction";
	public static final String OBJECT_PROP_DOOR_LOCKED = "Locked";
	public static final String OBJECT_PROP_DOOR_BOSS = "IsBoss";
	public static final String OBJECT_PROP_DOOR_TRANSITION_ID = "TransitionID";

	public static final String OBJECT_BOSS = "Boss";
	public static final String OBJECT_BOSS_WAYPOINT = "BossWaypoint";

	public static final int TILE_SIZE = 16;

	public List<Entity> entities = new ArrayList<Entity>();
	private TiledMap tiledMap;
	private String tiledMapPath = "";
	private FixOrthogonalTiledMapRenderer tiledMapRenderer;
	public static TiledMapTileLayer collisionLayer;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	public Avatar avatar;
	private List<Entity> added = new LinkedList<>();
	private List<Entity> removed = new LinkedList<>();

	public <T extends Entity> List<T> find(Class<T> type) {
		List<T> found = new LinkedList<>();
		for (Entity ent : entities) {
			if (type == ent.getClass()) found.add((T) ent);
		}
		return found;
	}


	public void drop(LootTable lootTable, int amount, Vector2 position, float range)
	{
		if (lootTable == null)
			return;

		for (LootTable.Loot loot : lootTable.Take(amount)) {
			Vector2 destination =
				new Vector2()
					.setToRandomDirection()
					.scl(range)
					.add(position);

			switch (loot.type) {
				case Money:
					Game.level.add(new Money(
						destination,
						loot.value));
					break;
				case Health:
					Game.level.add(new Health(
						destination,
						loot.value));
					break;
				case Item:
					Game.level.add(
						new PhysicalItem(
							destination,
							loot.item));
					break;
			}
		}
	}

	public Boss boss = null;

	public Level(String tilemapPath){
		TmxMapLoader.Parameters parameter = new TmxMapLoader.Parameters() {{
			generateMipMaps = false;
			textureMagFilter = Texture.TextureFilter.Nearest;
			textureMinFilter = Texture.TextureFilter.Nearest;

		}};

		this.tiledMapPath = tilemapPath;
		tiledMap = new TmxMapLoader().load(tilemapPath, parameter);
		tiledMapRenderer = new FixOrthogonalTiledMapRenderer(tiledMap);
	}

	public void load(int transitionID, Avatar avatar)
	{
		this.avatar = avatar;
		add(avatar);

		for (MapLayer layer : tiledMap.getLayers()) {
			switch (layer.getName()) {
				case LAYER_COLLISION:
					collisionLayer = (TiledMapTileLayer) layer;
					break;

				case LAYER_OBJECT:
					for (int i = 0; i < layer.getObjects().getCount(); i++) {
						MapObject object = layer.getObjects().get(i);
						if (object == null)
							continue;

						switch (object.getProperties().get(OBJECT_PROP_TYPE, String.class)) {

							case OBJECT_DOOR:
								Door door;
								add(door = new Door(new Vector2(
									object.getProperties().get(OBJECT_PROP_X, float.class),
									object.getProperties().get(OBJECT_PROP_Y, float.class)),
									object.getProperties().get(OBJECT_PROP_DOOR_LEVEL, String.class),
									object.getProperties().get(OBJECT_PROP_DOOR_TRANSITION_ID, Integer.class),
									object.getProperties().get(OBJECT_PROP_DOOR_DIRECTION, String.class),
									object.getProperties().containsKey(OBJECT_PROP_DOOR_LOCKED) ?
										object.getProperties().get(OBJECT_PROP_DOOR_LOCKED, boolean.class) :
										true,
									object.getProperties().containsKey(OBJECT_PROP_DOOR_BOSS) ?
										object.getProperties().get(OBJECT_PROP_DOOR_BOSS, boolean.class) :
										false
								));

								if (
									door.transitionID == transitionID &&
										avatar != null)
								{
									door.unlock();
									door.transition.disable(Transition.DISABLE_TIME);
									avatar.position = door.transition.getDestination();
									avatar.direction = Utils.toVector(door.transition.direction);
									avatar.input.disable(.25f);
								}

								break;

							case OBJECT_AVATAR:
								if (avatar == null) {
									add(this.avatar = new Avatar(new Vector2(
										object.getProperties().get(OBJECT_PROP_X, float.class),
										object.getProperties().get(OBJECT_PROP_Y, float.class))));
								}
								break;

							case OBJECT_SLIME:
								add(new Enemy(new Vector2(
									object.getProperties().get(OBJECT_PROP_X, float.class),
									object.getProperties().get(OBJECT_PROP_Y, float.class))));
								break;

							case OBJECT_BOSS:
								List<Vector2> waypoints = new LinkedList<>();
								layer.getObjects().forEach(
									x ->
									{
										if (x.getProperties().get(OBJECT_PROP_TYPE, String.class).equals(
											OBJECT_BOSS_WAYPOINT)) {
											Vector2 waypoint = new Vector2(
												x.getProperties().get(OBJECT_PROP_X, float.class),
												x.getProperties().get(OBJECT_PROP_Y, float.class));

											waypoints.add(waypoint);
										}

									});

								add(boss = new Boss(new Vector2(
									object.getProperties().get(OBJECT_PROP_X, float.class),
									object.getProperties().get(OBJECT_PROP_Y, float.class)),
									waypoints)
								);
								break;

							case OBJECT_TRANSITION:
								Transition transition;
								add(transition = new Transition(new Vector2(
									object.getProperties().get(OBJECT_PROP_X, float.class),
									object.getProperties().get(OBJECT_PROP_Y, float.class)),
									object.getProperties().get(OBJECT_PROP_TRANSITION_LEVEL, String.class),
									object.getProperties().get(OBJECT_PROP_TRANSITION_ID, Integer.class),
									object.getProperties().get(OBJECT_PROP_TRANSITION_DIRECTION, String.class)
								));

								if (
									transition.id == transitionID &&
										avatar != null) {
									transition.disable(Transition.DISABLE_TIME);
									avatar.position = transition.getDestination();
									avatar.direction = Utils.toVector(transition.direction);
									avatar.input.disable(.25f);
								}
								break;
							case OBJECT_NPC: {
								// Assign all text properties of the object
								LinkedList<String> texts = new LinkedList<>();
								object.getProperties()
									.getKeys()
									.forEachRemaining(
										x -> texts.add(
											x.startsWith(OBJECT_PROP_TEXT) ?
												object.getProperties().get(x, String.class) :
												null));
								texts.removeIf(x -> x == null);

								add(new NPC(new Vector2(
									object.getProperties().get(OBJECT_PROP_X, float.class),
									object.getProperties().get(OBJECT_PROP_Y, float.class)),
									object.getProperties().get(OBJECT_PROP_NPC_TYPE, int.class),
									texts.toArray(new String[texts.size()])));
							}
							break;

							case OBJECT_SIGNPOST: {
								// Assign all text properties of the object
								LinkedList<String> texts = new LinkedList<>();
								object.getProperties()
									.getKeys()
									.forEachRemaining(
										x -> texts.add(
											x.startsWith(OBJECT_PROP_TEXT) ?
												object.getProperties().get(x, String.class) :
												null));
								texts.removeIf(x -> x == null);

								add(new SignPost(new Vector2(
									object.getProperties().get(OBJECT_PROP_X, float.class),
									object.getProperties().get(OBJECT_PROP_Y, float.class)),
									texts.toArray(new String[texts.size()])));
							}
							break;

							case OBJECT_ITEM:
								add(new PhysicalItem(
									new Vector2(
										object.getProperties().get(OBJECT_PROP_X, float.class),
										object.getProperties().get(OBJECT_PROP_Y, float.class)),
									object.getName(),
									object.getProperties().get(OBJECT_PROP_ITEM_TYPE, String.class),
									object.getProperties().get(OBJECT_PROP_ITEM_SPRITE, String.class),
									object.getProperties().get(OBJECT_PROP_ITEM_IS_SHOP_ITEM, Boolean.class),
									object.getProperties().get(OBJECT_PROP_ITEM_COST, Float.class),
									object.getProperties()));

								break;
						}
					}
					break;
			}
		}
	}

	public void add(Entity entity) {
		if (entity == null)
			return;
		added.add(entity);
	}

	public void remove(Entity entity) {
		if (entity == null)
			return;
		removed.add(entity);
	}

	public void dispose(Avatar avatar) {
		for (Entity ent : entities) {
			if(ent == avatar) continue;
			ent.dispose();
		}

		tiledMap.dispose();
	}

	public void update(float deltaTime) {
		for (Entity ent : entities) {
			ent.update(deltaTime);
		}

		// Update entities changed from current iteration
		for (Entity ent : added) {
			entities.add(ent);
		}

		for (Entity ent : removed) {
			entities.remove(ent);
		}

		for (Entity ent : added) {
			ent.start();
		}

		for (Entity ent : removed) {
			ent.dispose();
		}

		added.clear();
		removed.clear();
	}

	public void render(SpriteBatch batch) {

		tiledMapRenderer.setView(Game.camera);
		tiledMapRenderer.render();

		entities.sort(Comparator.comparing(x -> -x.position.y));
		for (Entity ent : entities) {
			ent.render(batch);
		}

		// Draw debug grid
		if(!Game.isDebugRenderEnabled) return;
		shapeRenderer.setProjectionMatrix(Game.camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		for(int i = 0; i < collisionLayer.getWidth(); i++)
			for(int j = 0; j < collisionLayer.getHeight(); j++)
				if(collisionLayer.getCell(i, j) != null)
					shapeRenderer.rect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
		shapeRenderer.end();
	}
}
