package com.tutorialquest.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.Game;
import com.tutorialquest.InputManager;
import com.tutorialquest.entities.Character;
import com.tutorialquest.ui.dialogs.*;

public class HUD {

	private OrthographicCamera camera = new OrthographicCamera(Game.VIEWPORT_WIDTH, Game.VIEWPORT_HEIGHT);
	private SpriteBatch spriteBatch = new SpriteBatch();
	private HealthBar healthBar = new HealthBar();
	private MoneyCounter moneyCounter = new MoneyCounter();
	public MessageDialog messageDialog = new MessageDialog();
	public InputManager input = new InputManager();

	public static InventoryDialog inventoryDialog = new InventoryDialog();
	public static PhysicalItemDialog physicalItemDialog = new PhysicalItemDialog();
	public static InventoryItemDialog inventoryItemDialog = new InventoryItemDialog();

	public float getScaledWidth(){
		return Game.VIEWPORT_WIDTH * camera.zoom;
	}

	public float getScaledHeight(){
		return Game.VIEWPORT_HEIGHT * camera.zoom;
	}

	public HUD() {
		camera.zoom = 1f/4f;
	}

	public void update(float deltaTime) {
		input.update(deltaTime);
		camera.update();
		messageDialog.update(deltaTime);

		inventoryDialog.update(deltaTime);
		inventoryItemDialog.update(deltaTime);
		physicalItemDialog.update(deltaTime);
	}

	public void render() {

		Vector2 position;
		spriteBatch.setProjectionMatrix(camera.combined);

		position = new Vector2(
			camera.position.x - (getScaledWidth()/2) + 8 ,
			camera.position.y + (getScaledHeight()/2) - 16 - 4 );
		healthBar.render(
			spriteBatch,
			position);

		position = new Vector2(
			camera.position.x - (getScaledWidth()/2) + 8,
			camera.position.y + (getScaledHeight()/2) - 16  - 4 - 16 );
		moneyCounter.render(
			spriteBatch,
			position);

		position = new Vector2(
			camera.position.x + -Dialog.WIDTH/2,
			camera.position.y + -Dialog.HEIGHT * 1.5f );
		messageDialog.render(
			spriteBatch,
			camera,
			position);

		inventoryDialog.render(spriteBatch, Game.camera, position);
		inventoryItemDialog.render(spriteBatch, Game.camera, position);
		physicalItemDialog.render(spriteBatch, Game.camera, position);
	}

	public void onAvatarHealthChanged(Character character) {
		healthBar.onAvatarHealthChanged(character);
	}

	public void onAvatarMoneyChanged(Character character) {
		moneyCounter.OnAvatarMoneyChanged(character);
	}

}