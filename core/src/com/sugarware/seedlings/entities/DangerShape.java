package com.sugarware.seedlings.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class DangerShape extends Entity {

	static Filter filter;

	public DangerShape(PlayGameState gs, float x, float y, Shape shape) {
		super(gs);
		BodyDef def = new BodyDef();

		if (filter == null) {
			filter = new Filter();
			DangerShape.filter.categoryBits = CollisionBits.NOLIGHT_CATEGORY;
			DangerShape.filter.maskBits = CollisionBits.NOLIGHT_MASK;
		}

		def.type = BodyDef.BodyType.StaticBody;
		def.position.set(x, y);
		this.body = gs.getWorld().createBody(def);
		Object[] arrobject = new Object[2];
		arrobject[0] = this;
		this.body.setUserData(arrobject);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.isSensor = true;
		this.body.createFixture(fdef).setFilterData(filter);
		;
		this.body.setFixedRotation(true);
		this.body.setBullet(true);

	}

	@Override
	public void draw(SpriteBatch var1) {
		// nothing gets drawn for this as it is for making pieces of environment
		// deadly
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
	}

	@Override
	public void collide(Entity var1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
