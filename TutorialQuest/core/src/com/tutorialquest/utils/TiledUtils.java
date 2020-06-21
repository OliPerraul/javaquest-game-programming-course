package com.tutorialquest.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class TiledUtils {

	public static Vector2 worldToCellPosition(TiledMapTileLayer layer, Vector2 worldPosition) {
		return new Vector2((int) (worldPosition.x / layer.getTileWidth()),
			(int) (worldPosition.y / layer.getTileHeight()));
	}

	public static TiledMapTileLayer.Cell worldToCell(TiledMapTileLayer layer, float x, float y) {
		Vector2 pos = worldToCellPosition(layer, new Vector2(x, y));
		return layer.getCell((int) pos.x, (int) pos.y);
	}

	public static TiledMapTileLayer.Cell worldToCell(TiledMapTileLayer layer, Vector2 worldPosition) {
		Vector2 pos = worldToCellPosition(layer, worldPosition);
		return layer.getCell((int) pos.x, (int) pos.y);
	}

	public static Vector2 cellToWorldPosition(TiledMapTileLayer layer, Vector2 cellPosition) {
		return cellPosition.cpy().scl(layer.getTileWidth(), layer.getTileHeight());
	}


}
