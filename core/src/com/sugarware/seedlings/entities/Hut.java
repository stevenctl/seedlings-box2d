package com.sugarware.seedlings.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class Hut extends Entity {
	static TextureRegion img;
	static final int w = 4;
	static final int h = 4;
	Animation animation;
	TextureRegion[] frames;
	static final int HUT = 14;
	public boolean demolished = false;

	public Hut(PlayGameState gs, float x, float y) {
		super(gs);
		GameStateManager.rm.loadLongImage(14);
		this.animation = new Animation();
		this.frames = GameStateManager.rm.getImages(14).get(0);
		this.animation.setFrames(this.frames);
		this.animation.setDelay(50);
		BodyDef bdef = new BodyDef();
		bdef.position.set(x, y);
		PolygonShape pshape = new PolygonShape();
		pshape.setAsBox(4.0f, 4.0f);
		this.body = gs.getWorld().createBody(bdef);
		Object[] arrobject = new Object[2];
		arrobject[0] = this;
		this.body.setUserData(arrobject);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = pshape;
		fdef.isSensor = true;
		this.body.createFixture(fdef);
		pshape.dispose();
	}

	@Override
	public void draw(SpriteBatch g) {
		float ix = this.body.getPosition().x - 4.0f;
		float iy = this.body.getPosition().y + 8.0f;
		float iw = 8.0f;
		float ih = -8.0f;
		g.draw(this.animation.getImage(), ix, iy, iw, ih);
	}

	@Override
	public void update() {
		if (this.demolished && !this.animation.hasPlayedOnce()) {
			this.animation.update();
		}
	}

	@Override
	public void collide(Entity e) {
	}

	@Override
	public void destroy() {
	}
}
