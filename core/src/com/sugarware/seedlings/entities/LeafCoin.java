package com.sugarware.seedlings.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class LeafCoin extends Entity {
	Fixture playerSensorFixture;

	static Filter filter;
	public static TextureRegion img;

	private float width, height, pwidth, pheight;

	public LeafCoin(PlayGameState gs, float x, float y) {
		super(gs);

		this.width = .7f;
		this.height = .7f;
		this.pwidth = 2.0f * this.width;
		this.pheight = 2.0f * this.height;
		BodyDef bdef = new BodyDef();
		if (filter == null) {
			filter = new Filter();
			LeafCoin.filter.categoryBits = 4;
			LeafCoin.filter.maskBits = 13;
		}
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set(x, y);
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width, height);
		fdef.shape = shape;
		fdef.isSensor = true;
		this.body = gs.getWorld().createBody(bdef);
		this.body.setUserData(new Object[] { this, null });
		this.playerSensorFixture = this.body.createFixture(fdef);
		this.playerSensorFixture.setFilterData(filter);

	}

	public LeafCoin(PlayGameState gs, Vector2 position) {
		this(gs, position.x, position.y);
	}

	@Override
	public void draw(SpriteBatch g) {
		if (img == null) {
			img = new TextureRegion(new Texture(Gdx.files.internal("sprites/leaf2.png")));
		}
		g.draw(LeafCoin.img, this.body.getPosition().x - this.pwidth / 2.0f,
				this.body.getPosition().y - this.pheight / 2.0f, this.pwidth / 2.0f, this.pheight / 2.0f, this.pwidth,
				this.pheight, 1.0f, 1.0f, (float) ((double) (180.0f * this.body.getAngle()) / 3.141592653589793));

	}

	@Override
	public void update() {
	}

	@Override
	public void collide(Entity e) {
		if (e instanceof Player) {
			if (!this.destroy)
				gs.score();
			this.mark();

		}
	}

	@Override
	public void destroy() {
		if (gs.getWorld() != null)
			gs.getWorld().destroyBody(body);
	}
}
