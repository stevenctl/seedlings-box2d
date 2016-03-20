package com.sugarware.seedlings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.entities.FireCharacter;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class TouchController implements InputProcessor {
	boolean down;
	private int scheme;
	GdxGame game;
	private Vector3 coords;
	int[] pointer;
	SpriteBatch h;
	TextureRegion btnUp;
	TextureRegion btnAction;
	TextureRegion btnPause;
	TextureRegion btnLight;
	private boolean VU;
	private boolean VD;
	public static boolean inMenu = false;
	public TouchController(GdxGame game) {
		System.out.println("Touch Controller Initialized");
		this.h = new SpriteBatch();
		this.scheme = Gdx.app.getPreferences("SeedPeopleControls").getInteger("scheme", 0);
		this.coords = new Vector3(-1.0f, -1.0f, 0.0f);
		this.pointer = new int[10];
		this.game = game;
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int p, int button) {
		game.touchDown(screenX, screenY, p, button);
		this.coords.set(screenX, screenY, 0.0f);
		int x = (int) this.coords.x;
		int y = Gdx.graphics.getHeight() - (int) this.coords.y;
		switch (this.scheme) {
		case 0: {
			if (x > 0 && x < Gdx.graphics.getWidth() / 10 && y > Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 10
					&& y < Gdx.graphics.getHeight()) {
				this.handle(p, true, 131);
				break;
			}
			if (x > 0 && x <= Gdx.graphics.getWidth() / 4) {
				this.handle(p, true, 29);
				break;
			}
			if (x > Gdx.graphics.getWidth() / 4 && x <= Gdx.graphics.getWidth() / 2) {
				this.handle(p, true, 32);
				break;
			}
			if (x > Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 8 && x < Gdx.graphics.getWidth() && y > 0
					&& y < Gdx.graphics.getWidth() / 8) {
				if (!(this.game.gsm.currentState instanceof PlayGameState))
					break;
				PlayGameState st = (PlayGameState) this.game.gsm.currentState;
				if (st.p instanceof FireCharacter) {
					this.handle(p, true, 38);
					break;
				}
				this.handle(p, true, 62);
				break;
			}
			if (x > Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 10 && x < Gdx.graphics.getWidth()
					&& y > Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 10 && y < Gdx.graphics.getHeight()) {
				this.handle(p, true, 37);
				break;
			}
			if (x >= Gdx.graphics.getWidth() / 2 - 96 && x <= Gdx.graphics.getWidth() / 2 + 32
					&& y > Gdx.graphics.getHeight() - 80) {
				this.handle(p, true, 48);
				break;
			}
			if (x <= Gdx.graphics.getWidth() / 2)
				break;
			this.handle(p, true, 62);
			break;
		}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int p) {
		game.touchDragged(x, y, p);
		y = Gdx.graphics.getHeight() - y;
		switch (this.scheme) {
		case 0: {
			if (x > 0 && x < Gdx.graphics.getWidth() / 10 && y > Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 10
					&& y < Gdx.graphics.getHeight())
				break;
			if (x > 0 && x <= Gdx.graphics.getWidth() / 4 && this.pointer[p] == 32) {
				this.touchUp(x, Gdx.graphics.getHeight() + y, p, -1);
				this.handle(p, true, 29);
				break;
			}
			if (x > Gdx.graphics.getWidth() / 4 && x <= Gdx.graphics.getWidth() / 2 && this.pointer[p] == 29) {
				this.touchUp(x, Gdx.graphics.getHeight() + y, p, -1);
				this.handle(p, true, 32);
				break;
			}
			if (x <= Gdx.graphics.getWidth() / 4 - Gdx.graphics.getWidth() / 8 || x >= Gdx.graphics.getWidth() || y <= 0
					|| y >= Gdx.graphics.getWidth() / 8 || this.pointer[p] != 62)
				break;
			this.touchUp(x, Gdx.graphics.getHeight() + y, p, -1);
			if (!(this.game.gsm.currentState instanceof PlayGameState))
				break;
			PlayGameState st = (PlayGameState) this.game.gsm.currentState;
			if (st.p instanceof FireCharacter) {
				this.handle(p, true, 38);
				break;
			}
			this.handle(p, true, 62);
		}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int p, int button) {
		this.handle(p, false, this.pointer[p]);
		return false;
	}

	public void draw(SpriteBatch g) {
		g.end();
		this.h.begin();
		if (this.btnUp == null) {
			GameStateManager.rm.loadGuiImages();
			this.btnUp = GameStateManager.rm.btnUp;
			this.btnAction = GameStateManager.rm.btnAction;
			this.btnPause = GameStateManager.rm.btnPause;
			this.btnLight = GameStateManager.rm.btnLight;
		}
		if (this.game.gsm.currentState instanceof PlayGameState) {
			PlayGameState st = (PlayGameState) this.game.gsm.currentState;
			switch (this.scheme) {
			case 0: {
				if (st.p instanceof FireCharacter) {
					this.h.draw(this.btnAction, (float) (Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 8), 0.0f,
							(float) (Gdx.graphics.getWidth() / 8), (float) (Gdx.graphics.getWidth() / 8));
				}
				this.h.draw(this.btnPause, 0.0f, (float) (Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 10),
						(float) (Gdx.graphics.getWidth() / 10), (float) (Gdx.graphics.getWidth() / 10));
				this.h.draw(this.btnLight, (float) (Gdx.graphics.getWidth() / 2),
						(float) (Gdx.graphics.getHeight() - 48), 48.0f, 48.0f);
				break;
			}
			}
		}
		this.h.end();
		g.begin();
	}

	public void setScheme(int s) {
		this.scheme = s;
		Gdx.app.getPreferences("SeedPeopleControls").putInteger("scheme", s);
		Gdx.app.getPreferences("SeedPeopleControls").flush();
	}

	public int getScheme() {
		return this.scheme;
	}

	void handle(int p, boolean d, int k) {
		this.pointer[p] = d ? k : -1;
		this.down = d ? this.game.keyDown(k) : this.game.keyUp(k);
	}

	public boolean isDown(int k) {
		int[] arrn = this.pointer;
		int n = arrn.length;
		int n2 = 0;
		while (n2 < n) {
			int c = arrn[n2];
			if (k == c) {
				return true;
			}
			++n2;
		}
		return false;
	}

	@Override
	public boolean keyDown(int k) {
		switch (k) {
		case 24: {
			this.VU = true;
			break;
		}
		case 25: {
			this.VD = true;
		}
		}
		if (this.VU && this.VD) {
			this.game.gsm.setState(99);
			this.VD = false;
			this.VU = false;
		}
		return false;
	}

	@Override
	public boolean keyUp(int k) {
		switch (k) {
		case 24: {
			this.VU = false;
			this.game.keyDown(19);
			break;
		}
		case 25: {
			this.VD = false;
			this.game.keyDown(20);
		}
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}

