
package com.sugarware.seedlings;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class GdxGame implements ApplicationListener, InputProcessor {
	public static float camZoom = 17.0f;
	static boolean rec;
	static int c;
	static int f;
	public static int WIDTH;
	public static int HEIGHT;
	public static boolean test;
	public GameStateManager gsm;
	public TouchController tc;
	public long st;
	static GdxGame inst;
	Vector3 coords;

	static {
		c = 0;
		f = 0;
		WIDTH = 640;
		HEIGHT = 420;
		test = false;
	}

	public GdxGame() {
		inst = this;
	}

	public static GdxGame getInstance() {
		return inst;
	}

	@Override
	public void create() {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		float scale = camZoom / (float) HEIGHT;
		WIDTH = (int) ((float) WIDTH * scale);
		HEIGHT = (int) ((float) HEIGHT * scale);
		System.out.println("Cam is " + WIDTH + " x " + HEIGHT + " " + scale);
		this.gsm = !test ? new GameStateManager(this, 0) : new GameStateManager(this, 99);
		if (Gdx.app.getType() == Application.ApplicationType.Android
				|| Gdx.app.getType() == Application.ApplicationType.iOS) {
			this.tc = new TouchController(this);
		} else {
			Gdx.input.setInputProcessor(this);
		}
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		Gdx.graphics.setTitle("Seedlings " + Gdx.graphics.getFramesPerSecond());
		this.gsm.update();
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(16384);
		this.gsm.draw();
		if (this.gsm.currentState instanceof PlayGameState && this.tc != null
				&& ((PlayGameState) this.gsm.currentState).drawGUI) {
			this.tc.draw(this.gsm.g);
		}
	}

	@Override
	public void resize(int width, int height) {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		if ((float) HEIGHT > camZoom) {
			float scale = camZoom / (float) HEIGHT;
			WIDTH = (int) ((float) WIDTH * scale);
			HEIGHT = (int) ((float) HEIGHT * scale);
			System.out.println("Cam is " + WIDTH + " x " + HEIGHT + " " + scale);
		}
		this.gsm.resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public boolean keyDown(int k) {
		if (k == 3) {
			rec = !rec;
		}
		this.gsm.keyPressed(k);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		this.gsm.keyReleased(keycode);
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		System.out.println("Screen: " + screenX + "," + screenY);
		if (this.coords == null) {
			this.coords = new Vector3();
		}
		this.coords.set(screenX, screenY, 0.0f);
		this.gsm.currentState.cam.unproject(this.coords);
		System.out.println("World: " + this.coords.x + "," + this.coords.y);
		this.gsm.touchDown(this.coords, pointer);
		this.gsm.touchUp(this.coords, pointer);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		mouseMoved(screenX,screenY);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (this.coords == null) {
			this.coords = new Vector3();
		}
		this.coords.set(screenX, screenY, 0.0f);
		this.gsm.currentState.cam.unproject(this.coords);
		this.gsm.cursorMoved(this.coords, -1);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}

