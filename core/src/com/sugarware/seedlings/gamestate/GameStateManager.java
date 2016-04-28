package com.sugarware.seedlings.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.GdxGame;
import com.sugarware.seedlings.ResourceManager;

public class GameStateManager {
	public boolean paused;
	public GameState currentState;
	public int currentStateIndex;
	public GdxGame game;
	BitmapFont f;
	public static ResourceManager rm;
	OrthographicCamera cam;
	public GameState nextState;
	public int nextStateIndex;
	public static final int MM = 0;
	public static final int L1 = 1;
	public static final int L2 = 2;
	public static final int L3 = 3;
	public static final int C1 = 5;
	public static final int B1 = 6;
	public static final int C2 = 7;
	public static final int C3 = 8;
	public static final int L4 = 9;
	public static final int TEST = 99;
	public static final int D1 = 4;
	public static final int LONE = -1;
	public SpriteBatch g;

	public GameStateManager(GdxGame game, int index) {
		this.game = game;
		rm = new ResourceManager();
		this.currentStateIndex = index;
		this.cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.cam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0.0f);
		this.cam.update();
		this.f = new BitmapFont();
		this.setState(index);
	}

	public void setState(int index) {
		try {
			this.currentStateIndex = index;
			if (this.currentState != null) {
				this.currentState.dispose();
			}
			this.currentState = this.stateLookup(index);
			this.currentState.gatherResources();
			this.currentState.init();
		} catch (Exception e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
	}

	public void draw() {
		if (this.g == null) {
			this.g = new SpriteBatch();
		}
		this.currentState.draw();

		if (this.paused) {
			this.f.draw((Batch) this.g, "PAUSED", this.currentState.cam.position.x - 10.0f,
					this.currentState.cam.position.y);
		}
		if (this.g.isDrawing()) {
			this.g.end();
		}
		this.g.begin();
		this.f.draw((Batch) this.g, "FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()),
				this.currentState.cam.position.x - this.currentState.cam.viewportWidth / 2.0f + 4.0f,
				this.currentState.cam.position.y - this.currentState.cam.viewportHeight / 2.0f + 16.0f);
	}

	public void keyPressed(int k) {
		if (k == Keys.ESCAPE) {
			this.paused = !this.paused;
		}
		if (k == Keys.EQUALS || !this.paused && !(this.currentState instanceof PlaybackState)) {
			this.currentState.keyPressed(k);
		}
	}

	public void cursorMoved(Vector3 coords, int pointer) {
		this.currentState.cursorMoved(coords, pointer);
	}

	public void keyReleased(int k) {
		if (!(this.currentState instanceof PlaybackState)) {
			this.currentState.keyReleased(k);
		}
	}

	public void touchDown(Vector3 coords, int p) {
		this.currentState.touchDown(coords, p);
	}

	public void touchUp(Vector3 coords, int p) {
		this.currentState.touchUp(coords, p);
	}

	public void update() {
		if (this.currentState instanceof PlayGameState) {
			((PlayGameState) this.currentState).alphaUpdate();
		}
		if (!this.paused) {
			this.currentState.update();
		}
	}

	public void resize(int width, int height) {
		if (this.currentState != null) {
			this.currentState.resize(width, height);
		}
	}

	public void nextState() {
		this.nextState = this.stateLookup(this.nextStateIndex);
		this.currentStateIndex = this.nextStateIndex;
		this.nextStateIndex = -1;
		this.currentState.dispose();
		this.currentState = this.nextState;
		this.currentState.gatherResources();
		this.currentState.init();
		this.nextState = null;
	}

	public void setNextState(int i) {
		this.nextStateIndex = i;
	}

	public GameState stateLookup(int index) {
		switch (index) {
		case 0: {
			return new MenuState(this);
		}
		case 1: {
			return new Level1(this);
		}
		case 6: {
			return new Boss1(this);
		}
		case 2: {
			return new Level2(this);
		}
		case 3: {
			return new Level3(this);
		}
		case 7: {
			return new Cutscene2(this);
		}
		case 5: {
			return new Cutscene1(this);
		}
		case 8: {
			return new Cutscene3(this);
		}
		case 9: {
			return new Level4(this);
		}
		case 99: {
			return new Cutscene2(this);
		}
		}
		System.err.println("Invalid state index: " + index);
		return null;
	}
}
