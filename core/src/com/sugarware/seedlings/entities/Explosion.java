package com.sugarware.seedlings.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

import box2dLight.PointLight;

public class Explosion extends Entity implements Luminous {
	private Animation animation;
	int width = 48;
	int height = 48;
	float x;
	float y;
	int t = 0;
	PointLight light;
	static final Color lightCol = FireCharacter.lightCol;
	float size = 1.0f;

	public Explosion(PlayGameState gs, Body b, PointLight l, boolean big) {
		super(gs);
		this.body = b;
		this.light = l;
		this.animation = new Animation();
		GameStateManager.rm.loadImages(7);
		this.animation.setFrames(GameStateManager.rm.getImages(big ? 7 : 8).get(0));
		this.animation.setDelay(30);
		if (big) {
			this.size = 2.0f;
		}
		GameStateManager.rm.playSound(Resources.Sounds.GENERAL, 1, 0.12f);
	}

	public Explosion(PlayGameState gs, Body b, PointLight l) {
		this(gs, b, l, false);
	}

	@Override
	public void destroy() {
		this.gs.getWorld().destroyBody(this.body);
		if (this.light != null) {
			this.light.remove();
		}
	}

	@Override
	public void updateLight() {
		if (this.light == null) {
			this.light = new PointLight(this.gs.rh, 100, lightCol, this.size * 4.0f, this.body.getPosition().x,
					this.body.getPosition().y);
			this.light.attachToBody(this.body);
		}
		++this.t;
		this.light.setDistance(this.size * 8.0f + (float) Math.cos(this.t / 16) / 3.5f);
	}

	@Override
	public void update() {
		this.updateLight();
		this.destroy = this.animation.hasPlayedOnce();
		if (!this.destroy) {
			this.animation.update();
		}
	}

	@Override
	public void draw(SpriteBatch g) {
		float ix = this.body.getPosition().x - this.size * 1.5f;
		float iy = this.body.getPosition().y + this.size * 1.5f;
		float iw = 3.0f * this.size;
		float ih = -3.0f * this.size;
		if (this.animation.getImage() != null) {
			g.draw(this.animation.getImage(), ix, iy, iw, ih);
		}
	}

	@Override
	public void collide(Entity e) {
	}
}
