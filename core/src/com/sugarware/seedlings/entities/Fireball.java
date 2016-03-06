package com.sugarware.seedlings.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

import box2dLight.PointLight;

public class Fireball extends Entity implements Luminous {
	int dir;
	float speed = 10.0f;
	final float size = 0.75f;
	Animation animation;
	Fixture sensorFixture;
	static Class<?>[] ignores = new Class[] { Player.class, Hint.class, LeafCoin.class };
	int t = 0;
	public PointLight light;
	static final Color lightCol = FireCharacter.lightCol;

	public Fireball(PlayGameState gs, float x, float y, boolean right) {
		super(gs);
		this.dir = right ? 1 : -1;
		this.animation = new Animation();
		this.animation.setFrames(GameStateManager.rm.getImages(6).get(0));
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(x, y);
		this.body = gs.getWorld().createBody(def);
		Object[] arrobject = new Object[2];
		arrobject[0] = this;
		this.body.setUserData(arrobject);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.75f, 0.75f);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 1.0f;
		fdef.friction = 0.0f;
		fdef.isSensor = true;
		this.body.setLinearDamping(0.0f);
		fdef.restitution = 0.0f;
		this.sensorFixture = this.body.createFixture(fdef);
		this.body.setFixedRotation(true);
		this.body.setGravityScale(0.0f);
	}

	@Override
	public void draw(SpriteBatch g) {
		float ix = this.body.getPosition().x - 1.125f;
		float iy = this.body.getPosition().y + 1.125f;
		float iw = 2.25f;
		float ih = -2.25f;
		if (this.dir > 0) {
			if (this.animation.getImage() != null) {
				g.draw(this.animation.getImage(), ix, iy, iw, ih);
			}
		} else if (this.animation.getImage() != null) {
			g.draw(this.animation.getImage(), ix + iw, iy, -iw, ih);
		}
	}

	@Override
	public void update() {
		this.isColliding();
		this.updateLight();
		this.body.setLinearVelocity(this.speed * (float) this.dir, 0.0f);
		this.animation.update();
	}

	@Override
	public void destroy() {
		this.body.setLinearVelocity(0.0f, 0.0f);
	}

	public void isColliding() {
		Array<Contact> contactList = this.gs.getWorld().getContactList();
		int i = 0;
		while (i < contactList.size) {
			Contact contact = contactList.get(i);
			if (contact.isTouching()
					&& (contact.getFixtureA() == this.sensorFixture || contact.getFixtureB() == this.sensorFixture)) {
				Object ua = contact.getFixtureA().getBody().getUserData();
				Object ub = contact.getFixtureB().getBody().getUserData();
				ua = ((Object[]) ua)[0];
				ub = ((Object[]) ub)[0];
				boolean ignore = false;
				Class<?>[] arrclass = ignores;
				int n = arrclass.length;
				int n2 = 0;
				while (n2 < n) {
					Class<?> c = arrclass[n2];
					if (c.isInstance(ua) || c.isInstance(ub)) {
						ignore = true;
						break;
					}
					++n2;
				}
				if (!ignore) {
					this.mark();
				}
			}
			++i;
		}
	}

	@Override
	public void updateLight() {
		if (this.light == null) {
			this.light = new PointLight(this.gs.rh, 100, lightCol, 3.0f, this.body.getPosition().x,
					this.body.getPosition().y);
			this.light.attachToBody(this.body);
		}
		++this.t;
		this.light.setDistance(6.0f + (float) Math.cos(this.t / 16) / 3.5f);
	}

	@Override
	public void collide(Entity e) {
	}
}
