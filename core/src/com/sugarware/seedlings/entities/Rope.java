package com.sugarware.seedlings.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class Rope extends Entity {
	static TextureRegion img;
	Array<Body> segments;
	final float size = 0.2f;

	public Rope(PlayGameState gs, float x, float y, float l) {
		super(gs);
		BodyDef rootdef = new BodyDef();
		rootdef.type = BodyDef.BodyType.StaticBody;
		rootdef.position.set(x, y);
		this.body = gs.getWorld().createBody(rootdef);
		this.body.setUserData(new Object[] { this, false });
		FixtureDef rootfixdef = new FixtureDef();
		PolygonShape rect = new PolygonShape();
		rect.setAsBox(0.1f, 0.1f);
		rootfixdef.shape = rect;
		rootfixdef.isSensor = true;
		rootfixdef.filter.categoryBits = 4;
		rootfixdef.filter.maskBits = 13;
		this.body.createFixture(rootfixdef);
		rect.dispose();
		this.segments = new Array<Body>();
		float sl = 0.2f;
		Body lastSeg = this.body;
		RevoluteJointDef jdef = new RevoluteJointDef();
		jdef.localAnchorA.y = (-sl) / 2.0f;
		jdef.localAnchorB.y = sl / 2.0f;
		jdef.collideConnected = true;
		jdef.enableLimit = false;
		FixtureDef fdef = new FixtureDef();
		rect = new PolygonShape();
		rect.setAsBox(0.1f, sl);
		fdef.shape = rect;
		fdef.friction = 0.0f;
		fdef.density = 3.0f;
		fdef.isSensor = true;
		fdef.filter.categoryBits = 4;
		fdef.filter.maskBits = 13;
		BodyDef segdef = new BodyDef();
		segdef.type = BodyDef.BodyType.DynamicBody;
		segdef.position.x = x;
		int n = 0;
		float i = -3.0f * sl;
		while (i > -l) {
			segdef.position.y = y + i;
			Body seg = gs.getWorld().createBody(segdef);
			Object[] arrobject = new Object[2];
			arrobject[0] = this;
			arrobject[1] = ++n % 4 == 0;
			seg.setUserData(arrobject);
			seg.createFixture(fdef);
			this.segments.add(seg);
			if (lastSeg != null) {
				jdef.bodyA = lastSeg;
				jdef.bodyB = seg;
				gs.getWorld().createJoint(jdef);
			}
			lastSeg = seg;
			i -= sl;
		}
		rect.dispose();
	}

	@Override
	public void draw(SpriteBatch g) {
		if (img == null) {
			img = new TextureRegion(new Texture(Gdx.files.internal("roper.png")));
		}
		int i = 0;
		while (i < this.segments.size) {
			Body body = this.segments.get(i);
			float ix = body.getPosition().x - 0.040000003f;
			float iy = body.getPosition().y + 0.040000003f;
			float iw = 0.2f;
			float ih = -0.2f;
			g.draw(img, ix, iy, iw / 2.0f, ih / 2.0f, iw, ih, 3.0f, 3.0f, body.getAngle() * 180.0f / 3.1415927f);
			++i;
		}
	}

	@Override
	public void update() {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void collide(Entity e) {
	}
}
