package com.sugarware.seedlings.gamestate;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.sugarware.seedlings.GdxGame;
import com.sugarware.seedlings.gamestate.GameStateManager;

public abstract class GameState implements Disposable {
	SpriteBatch g;
	int w;
	int h;
	public OrthographicCamera cam;
	public GameStateManager gsm;

	public GameState(int w, int h, GameStateManager gsm) {
		this.h = h;
		this.w = w;
		this.gsm = gsm;
		this.cam = new OrthographicCamera(GdxGame.WIDTH, GdxGame.HEIGHT);
		this.cam.position.set(this.cam.viewportWidth / 2.0f, this.cam.viewportHeight / 2.0f, 0.0f);
		this.cam.update();
	}

	public GameState(GameStateManager gsm2) {
		this.cam = new OrthographicCamera(GdxGame.WIDTH, GdxGame.HEIGHT);
		this.cam.position.set(this.cam.viewportWidth / 2.0f, this.cam.viewportHeight / 2.0f, 0.0f);
		this.cam.update();
		this.gsm = gsm2;
	}

	public abstract void cursorMoved(Vector3 var1, int var2);

	public abstract void touchDown(Vector3 var1, int var2);

	public abstract void touchUp(Vector3 var1, int var2);

	protected abstract void keyPressed(int var1);

	protected abstract void keyReleased(int var1);

	public abstract void gatherResources();

	protected void resize(int w, int h) {
		this.cam = new OrthographicCamera(GdxGame.WIDTH, GdxGame.HEIGHT);
		this.cam.update();
	}

	protected abstract void init();

	public abstract void update();

	public abstract void draw();

	public void pause() {
		this.gsm.paused = !this.gsm.paused;
	}
}
