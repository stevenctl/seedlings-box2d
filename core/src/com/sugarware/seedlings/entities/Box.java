package com.sugarware.seedlings.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class Box extends Entity {
	float width;
	float height;
	float pwidth;
	float pheight;
	TextureRegion img;

	public Box(PlayGameState gs, float x, float y, float w, float h) {
		this(gs, x, y, w, h, "sprites/rockBlock.png");
	}

	public Box(PlayGameState gs, float x, float y, float w, float h, String p) {
		super(gs);
		this.img = new TextureRegion(new Texture(Gdx.files.internal(p)));
		this.width = w;
		this.height = h;
		this.pwidth = 2.0f * this.width;
		this.pheight = 2.0f * this.height;
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(x, y);
		this.body = gs.getWorld().createBody(def);
		Object[] arrobject = new Object[2];
		arrobject[0] = "world";
		this.body.setUserData(arrobject);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(this.width, this.height);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 1.3f;
		fdef.friction = 0.55f;
		fdef.restitution = 0.1f;
		this.body.createFixture(fdef);
		this.body.setFixedRotation(false);
		this.body.setBullet(true);
		shape.dispose();
	}

	@Override
	public void draw(SpriteBatch g) {
		g.setProjectionMatrix(this.gs.cam.combined);
		g.draw(this.img, this.body.getPosition().x - this.pwidth / 2.0f,
				this.body.getPosition().y - this.pheight / 2.0f, this.pwidth / 2.0f, this.pheight / 2.0f, this.pwidth,
				this.pheight, 1.0f, 1.0f, (float) ((double) (180.0f * this.body.getAngle()) / 3.141592653589793));
	}

	@Override
	public void update() {
	}

	@Override
	public void collide(Entity e) {
	}

	@Override
	public void destroy() {
	}
}
