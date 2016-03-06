package com.sugarware.seedlings.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.sugarware.seedlings.gamestate.PlayGameState;

public abstract class Entity {
	public Body body;
	PlayGameState gs;
	protected boolean destroy;

	public Entity(PlayGameState gs) {
		this.gs = gs;
	}

	public abstract void draw(SpriteBatch var1);

	public abstract void update();

	public abstract void collide(Entity var1);

	public abstract void destroy();

	public boolean shouldDestroy() {
		return this.destroy;
	}

	public void mark() {
		this.mark(true);
	}

	public void mark(boolean d) {
		this.destroy = d;
	}
}
