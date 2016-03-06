package com.sugarware.seedlings.entities;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sugarware.seedlings.GdxGame;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class FlowerCharacter extends Player {
	public static final int IDLE = 0;
	public static final int RUN = 1;
	public static final int JUMP = 2;
	public static final int FALL = 3;
	public static final int SLOWFALL = 4;
	boolean FLOAT = false;
	float floatSpeed = 2.7f;

	public FlowerCharacter(PlayGameState ps, float x, float y) {
		super(x, y, ps);
		this.animation = new Animation();
		this.currentAction = 0;
		this.unhappy_index = 16;
		GameStateManager.rm.loadImages(1);
		this.frames = GameStateManager.rm.getImages(1);
		this.animation.setFrames((TextureRegion[]) this.frames.get(this.currentAction));
	}

	public FlowerCharacter(PlayGameState ps, Player pl) {
		super(ps, pl);
		GameStateManager.rm.loadImages(1);
		this.frames = GameStateManager.rm.getImages(1);
		this.animation.setFrames((TextureRegion[]) this.frames.get(this.currentAction));
		this.transition = new Animation();
		GameStateManager.rm.loadLongImage(12);
		this.transition.setFrames(GameStateManager.rm.getImages(12).get(0));
		this.transition.setDelay(18);
	}

	@Override
	public void alphaUpdate() {
		if (this.oldanim != null) {
			if (this.oldalpha > 0.0f && this.transition != null && this.transition.getFrame() == 34) {
				this.oldalpha = 0.0f;
			}
			if (this.oldalpha <= 0.0f) {
				this.oldanim = null;
			}
		}
		if (this.transition != null) {
			if (this.transition.hasPlayedOnce()) {
				this.transition = null;
			} else {
				this.transition.update();
			}
		}
		if (this.alpha < 1.0f && !this.dead && this.transition != null && this.transition.getFrame() == 34) {
			this.alpha = 1.0f;
		}
		if (this.alpha > 1.0f) {
			this.alpha = 1.0f;
		}
	}

	@Override
	public void update() {
		super.update();
		this.FLOAT = false;
		boolean touchSpace = false;
		if (Gdx.app.getType() == Application.ApplicationType.Android
				|| Gdx.app.getType() == Application.ApplicationType.iOS) {
			touchSpace = GdxGame.getInstance().tc.isDown(62);
		}
		if ((Gdx.input.isKeyPressed(38) || Gdx.input.isKeyPressed(62) || touchSpace)
				&& this.body.getLinearVelocity().y < -this.floatSpeed) {
			this.FLOAT = true;
			this.body.setLinearVelocity(this.body.getLinearVelocity().x, -this.floatSpeed);
		}
		float dx = this.body.getLinearVelocity().x;
		float dy = this.body.getLinearVelocity().y;
		boolean grounded = this.isGrounded();
		if ((double) Math.abs(dx) <= 0.01 && grounded) {
			if (this.currentAction != 0) {
				this.currentAction = 0;
				this.animation.setFrames((TextureRegion[]) this.frames.get(this.currentAction));
				this.animation.setDelay(-1);
			}
		} else if (dx != 0.0f && grounded && (this.LEFT || this.RIGHT) && !this.jumping) {
			if (this.currentAction != 1) {
				this.currentAction = 1;
				this.animation.setFrames((TextureRegion[]) this.frames.get(this.currentAction));
				this.animation.setDelay(16);
			}
		} else if (this.jumping && dy >= 0.0f) {
			if (this.currentAction != 2) {
				this.currentAction = 2;
				this.animation.setFrames((TextureRegion[]) this.frames.get(2));
				this.animation.setDelay(5);
			}
		} else if (this.FLOAT) {
			if (this.currentAction != 4) {
				this.currentAction = 4;
				this.animation.setFrames((TextureRegion[]) this.frames.get(4));
				this.animation.setDelay(40);
			}
		} else if (dy < 0.0f && !grounded) {
			if (this.currentAction != 3) {
				this.currentAction = 3;
				this.animation.setFrames((TextureRegion[]) this.frames.get(3));
				this.animation.setDelay(-1);
			}
		} else if (this.currentAction != 0) {
			this.currentAction = 0;
			this.animation.setFrames((TextureRegion[]) this.frames.get(this.currentAction));
			this.animation.setDelay(-1);
		}
		if (this.currentAction == 2 || this.currentAction == 4) {
			if (!this.animation.hasPlayedOnce()) {
				this.animation.update();
			} else {
				this.animation.setFrame(this.animation.getNumFrames() - 1);
			}
		} else {
			this.animation.update();
		}
	}

	@Override
	public void keyDown(int k) {
		super.keyDown(k);
	}

	@Override
	public void keyUp(int k) {
		super.keyUp(k);
	}

	public void onTouchDown(float x, float y) {
	}

	@Override
	public void destroy() {
	}
}
