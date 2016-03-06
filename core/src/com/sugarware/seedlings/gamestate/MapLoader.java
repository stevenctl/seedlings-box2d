package com.sugarware.seedlings.gamestate;

import java.util.ArrayList;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.sugarware.seedlings.entities.Box;
import com.sugarware.seedlings.entities.Entity;
import com.sugarware.seedlings.entities.Hint;
import com.sugarware.seedlings.entities.LeafCoin;
import com.sugarware.seedlings.entities.Rope;

public class MapLoader {
	private static float ppt = 16.0f;

	public static Array<Body> load(TiledMap map, PlayGameState gs) {
		ppt = ((Integer) map.getProperties().get("tilewidth", Integer.class)).intValue();
		World world = gs.getWorld();
		MapObjects objects = map.getLayers().get("Collisions").getObjects();
		Array<Body> bodies = new Array<Body>();
		for (MapObject object : objects) {
			Shape shape;
			if (object instanceof TextureMapObject)
				continue;
			Object[] userdata = new Object[2];
			userdata[0] = "world";
			if (object instanceof RectangleMapObject) {
				shape = MapLoader.getRectangle((RectangleMapObject) object);
			} else if (object instanceof PolygonMapObject) {
				shape = MapLoader.getPolygon((PolygonMapObject) object);
			} else if (object instanceof PolylineMapObject) {
				shape = MapLoader.getPolyline((PolylineMapObject) object);
				Vector2 v2 = new Vector2();
				((ChainShape) shape).getVertex(0, v2);
				if (object.getName() != null && !object.getName().equals("slope")) {
					userdata[0] = "worldf";
				}
				userdata[1] = Float.valueOf(v2.y);
			} else {
				if (!(object instanceof CircleMapObject))
					continue;
				shape = MapLoader.getCircle((CircleMapObject) object);
			}
			BodyDef bd = new BodyDef();
			bd.type = BodyDef.BodyType.StaticBody;
			Body body = world.createBody(bd);
			body.createFixture((Shape) shape, 1.0f);
			body.setUserData(userdata);
			bodies.add(body);
			shape.dispose();
		}
		ArrayList<Entity> entities = gs.getEntities();
		objects = map.getLayers().get("Entities").getObjects();
		for (MapObject ob : objects) {
			float y;
			float h;
			RectangleMapObject rect;
			float x;
			if (ob.getName() == null)
				continue;
			if (ob.getName().equals("vine")) {
				PolylineMapObject poly = (PolylineMapObject) ob;
				float x2 = poly.getPolyline().getX() / ppt;
				float y2 = poly.getPolyline().getY() / ppt;
				float[] vertices = poly.getPolyline().getVertices();
				float x22 = x2 + vertices[2] / ppt;
				float y22 = y2 + vertices[3] / ppt;
				float l = MapLoader.dist(x2, y2, x22, y22);
				Rope r = new Rope(gs, x2, y2, l);
				entities.add(r);
			}
			if (ob.getName().equals("block")) {
				rect = (RectangleMapObject) ob;
				x = rect.getRectangle().getX() / ppt;
				y = rect.getRectangle().getY() / ppt;
				float w = rect.getRectangle().getWidth() / ppt / 2.0f;
				h = rect.getRectangle().getHeight() / ppt / 2.0f;
				entities.add(new Box(gs, x, y, w, h));
			} else if (ob.getName().equals("coin")) {
				rect = (RectangleMapObject) ob;
				x = rect.getRectangle().getX() / ppt;
				y = rect.getRectangle().getY() / ppt;
				entities.add(new LeafCoin(gs, x, y));
			} else if (ob.getName().contains("hint")) {
				rect = (RectangleMapObject) ob;
				x = rect.getRectangle().getX() / ppt;
				y = rect.getRectangle().getY() / ppt;
				float w = rect.getRectangle().getWidth() / ppt / 2.0f;
				h = rect.getRectangle().getHeight() / ppt / 2.0f;
				x += w;
				y += h;
				String text = "";
				String[] pts = ob.getName().split(" ");
				int i = 1;
				while (i < pts.length) {
					text = String.valueOf(text) + pts[i] + " ";
					++i;
				}
				entities.add(new Hint(gs, x, y, w, h, text));
			}
		}
		return bodies;
	}

	static float dist(float x, float y, float x2, float y2) {
		return (float) Math.sqrt(Math.pow(x - x2, 2.0) + Math.pow(y - y2, 2.0));
	}

	private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
		Rectangle rectangle = rectangleObject.getRectangle();
		PolygonShape polygon = new PolygonShape();
		Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / ppt,
				(rectangle.y + rectangle.height * 0.5f) / ppt);
		polygon.setAsBox(rectangle.width * 0.5f / ppt, rectangle.height * 0.5f / ppt, size, 0.0f);
		return polygon;
	}

	private static CircleShape getCircle(CircleMapObject circleObject) {
		Circle circle = circleObject.getCircle();
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(circle.radius / ppt);
		circleShape.setPosition(new Vector2(circle.x / ppt, circle.y / ppt));
		return circleShape;
	}

	private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
		PolygonShape polygon = new PolygonShape();
		float[] vertices = polygonObject.getPolygon().getTransformedVertices();
		float[] worldVertices = new float[vertices.length];
		int i = 0;
		while (i < vertices.length) {
			System.out.println(vertices[i]);
			worldVertices[i] = vertices[i] / ppt;
			++i;
		}
		polygon.set(worldVertices);
		return polygon;
	}

	private static ChainShape getPolyline(PolylineMapObject polylineObject) {
		float[] vertices = polylineObject.getPolyline().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];
		int i = 0;
		while (i < vertices.length / 2) {
			worldVertices[i] = new Vector2();
			worldVertices[i].x = vertices[i * 2] / ppt;
			worldVertices[i].y = vertices[i * 2 + 1] / ppt;
			++i;
		}
		ChainShape chain = new ChainShape();
		chain.createChain(worldVertices);
		return chain;
	}
}
