package com.sugarware.seedlings.gamestate;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.GdxGame;

public class MenuState extends GameState {
	BitmapFont font;
	BitmapFont font2;
	TextureRegion bg;
	final String[] options = new String[] { "Story Mode", "Demo Mode" };
	int sel = Gdx.app.getType() == Application.ApplicationType.Desktop ? 0 : -1;

	public MenuState(GameStateManager gsm2) {
		super(gsm2);
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Monopol.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (int) (.2f * Gdx.graphics.getHeight());
		parameter.color = Color.WHITE;
		this.font = generator.generateFont(parameter);
		parameter.size =  (int) (.1f * Gdx.graphics.getHeight());
		this.font2 = generator.generateFont(parameter);
		generator.dispose();
		this.g = new SpriteBatch();
		GameStateManager.rm.playSong(4);
		this.cam = new OrthographicCamera(GdxGame.WIDTH, GdxGame.HEIGHT);
		this.bg = new TextureRegion(new Texture(Gdx.files.internal("scr1.png")));
	}

	@Override
	public void draw() {

		this.cam.viewportWidth = Gdx.graphics.getWidth();
		this.cam.viewportHeight = Gdx.graphics.getHeight();
		this.cam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0.0f);
		this.cam.update();
		this.g.setProjectionMatrix(this.cam.combined);
		this.g.begin();
		this.g.draw(this.bg, 0, 0, Gdx.graphics.getWidth(),
				(Gdx.graphics.getWidth() / (float) bg.getRegionWidth()) * Gdx.graphics.getHeight());
		this.font.draw(this.g, "Seedlings", 0.0f, (float) Gdx.graphics.getHeight() - 25.0f, 0, "Seedlings".length(),
				Gdx.graphics.getWidth(), 1, true, "...");
		int i = 0;
		while (i < this.options.length) {
			if (i == this.sel) {
				this.font2.setColor(Color.WHITE);
			} else {
				this.font2.setColor(Color.LIGHT_GRAY);
			}
			this.font2.draw(this.g, this.options[i], 0.0f, (float) Gdx.graphics.getHeight() - 40.0f * (float) (i + 4),
					0, this.options[i].length(), Gdx.graphics.getWidth(), 1, true, "...");
			++i;
		}
		this.g.end();
	}

	@Override
	protected void keyPressed(int k) {
		if (k == 51) {
			--this.sel;
		}
		if (k == 47) {
			++this.sel;
		}
		if (k == 66) {
			this.select();
		}
		if (this.sel < 0) {
			this.sel = this.options.length - 1;
		}
		if (this.sel >= this.options.length) {
			this.sel = 0;
		}
	}

	private void select() {
		switch (this.sel) {
		case 0: {
			this.gsm.setState(GameStateManager.L1);
			break;
		}
		case 1: {
			this.gsm.setState(GameStateManager.TEST);
		}
		}
	}

	@Override
	public void update() {
	}

	@Override
	public void dispose() {
		this.g.dispose();
		this.font.dispose();
		this.bg.getTexture().dispose();
	}

	@Override
	public void cursorMoved(Vector3 coords, int pointer) {
		int i = 0;
		while (i < this.options.length) {
			if (coords.y > (float) Gdx.graphics.getHeight() - 40.0f * (float) (i + 5)
					&& coords.y < (float) Gdx.graphics.getHeight() - 40.0f * (float) (i + 5)
							+ this.font2.getLineHeight()) {
				this.sel = i;
				break;
			}
			++i;
		}
	}

	@Override
	public void touchDown(Vector3 coords, int pointer) {
		int i = 0;
		while (i < this.options.length) {
			if (coords.y > (float) Gdx.graphics.getHeight() - 40.0f * (float) (i + 5)
					&& coords.y < (float) Gdx.graphics.getHeight() - 40.0f * (float) (i + 5)
							+ this.font2.getLineHeight()) {
				this.sel = i;
				break;
			}
			++i;
		}
		this.select();
	}

	@Override
	public void touchUp(Vector3 coords, int pointer) {
	}

	@Override
	protected void keyReleased(int k) {
	}

	@Override
	public void gatherResources() {
	}

	@Override
	protected void init() {
	}
}
