package com.sugarware.seedlings.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class Hint extends Entity {
	Fixture playerSensorFixture;
	String hintText;
	static BitmapFont font;
	public static int nhints;
	static Filter filter;
	Vector2 oldpos;
	float ow;
	float oh;

	static {
		nhints = 0;
	}

	public Hint(PlayGameState gs, float x, float y, float w, float h, String text) {
		super(gs);
		BodyDef bdef = new BodyDef();
		if (filter == null) {
			filter = new Filter();
			Hint.filter.categoryBits = 4;
			Hint.filter.maskBits = 13;
		}
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set(x, y);
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(w, h);
		fdef.shape = shape;
		fdef.isSensor = true;
		this.body = gs.getWorld().createBody(bdef);
		this.body.setUserData(new Object[] { this, text });
		this.playerSensorFixture = this.body.createFixture(fdef);
		this.playerSensorFixture.setFilterData(filter);
		this.hintText = text;
	}

	@Override
	public void draw(SpriteBatch g) {
		if (this.shouldShow()) {
			if (font == null) {
				FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Monopol.ttf"));
				FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
				parameter.size = 32;
				parameter.color = Color.WHITE;
				font = generator.generateFont(parameter);
			}
			if (this.oldpos == null) {
				this.oldpos = new Vector2();
			}
			this.oldpos.set(this.gs.cam.position.x, this.gs.cam.position.y);
			this.ow = this.gs.cam.viewportWidth;
			this.oh = this.gs.cam.viewportHeight;
			this.gs.cam.viewportWidth = Gdx.graphics.getWidth();
			this.gs.cam.viewportHeight = Gdx.graphics.getHeight();
			this.gs.cam.position.set(this.gs.cam.viewportWidth / 2.0f, this.gs.cam.viewportHeight / 2.0f, 0.0f);
			this.gs.cam.update();
			if (g.isDrawing()) {
				g.end();
			}
			g.setProjectionMatrix(this.gs.cam.combined);
			g.begin();
			font.draw(g, this.hintText, 0.0f,
					(float) Gdx.graphics.getHeight() - font.getLineHeight() - 5.0f
							- font.getLineHeight() * (float) nhints,
					0, this.hintText.length(), Gdx.graphics.getWidth(), 1, true, "...");
			g.end();
			this.gs.cam.viewportWidth = this.ow;
			this.gs.cam.viewportHeight = this.oh;
			this.gs.cam.position.set(this.oldpos, 0.0f);
			this.gs.cam.update();
			if (g.isDrawing()) {
				g.end();
			}
			g.setProjectionMatrix(this.gs.cam.combined);
			g.begin();
			++nhints;
		}
	}

	@Override
	public void update() {
	}

	private boolean shouldShow() {
		Array<Contact> contactList = this.gs.getWorld().getContactList();
		int i = 0;
		while (i < contactList.size) {
			Contact contact = contactList.get(i);
			if (contact.isTouching() && (contact.getFixtureA() == this.playerSensorFixture
					|| contact.getFixtureB() == this.playerSensorFixture)) {
				Object ua = contact.getFixtureA().getBody().getUserData();
				Object ub = contact.getFixtureB().getBody().getUserData();
				ua = ((Object[]) ua)[0];
				ub = ((Object[]) ub)[0];
				if (!(contact.getFixtureA() == this.playerSensorFixture ? !(ub instanceof Player)
						: !(ua instanceof Player))) {
					return true;
				}
			}
			++i;
		}
		return false;
	}

	@Override
	public void collide(Entity e) {
	}

	@Override
	public void destroy() {
	}
}
