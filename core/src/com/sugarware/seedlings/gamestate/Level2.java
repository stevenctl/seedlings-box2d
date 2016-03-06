package com.sugarware.seedlings.gamestate;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.ScrollSelector;
import com.sugarware.seedlings.entities.VanillaCharacter;

public class Level2 extends PlayGameState {
	float shift = -50.0f;

	public Level2(GameStateManager gsm) {
		super(gsm, "tilemaps/level2.tmx");
		ArrayList<TextureRegion> icons = new ArrayList<TextureRegion>();
		int i = 0;
		while (i < 1) {
			icons.add(GameStateManager.rm.icons.get(i));
			++i;
		}
		this.ss = new ScrollSelector(icons, 2.0f, 1.5f, 1.5f, this);
	}

	@Override
	public void init() {
		super.init();
		this.p = new VanillaCharacter(this, 3.0f, 22.01f);
		this.gsm.setNextState(3);
		this.rh.setAmbientLight(1.0f, 1.0f, 1.0f, 0.0f);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void cursorMoved(Vector3 coords, int pointer) {
	}

	@Override
	public void touchDown(Vector3 coords, int pointer) {
	}

	@Override
	public void touchUp(Vector3 coords, int pointer) {
	}

	@Override
	public void keyPressed(int k) {
		super.keyPressed(k);
		this.p.keyDown(k);
		if (k == 37) {
			this.ss.scroll();
		}
	}

	@Override
	public void keyReleased(int k) {
		this.p.keyUp(k);
	}

	@Override
	public void gatherResources() {
		GameStateManager.rm.loadWaterImages();
		GameStateManager.rm.loadImages(0);
		GameStateManager.rm.loadImages(1);
		GameStateManager.rm.loadImages(8);
		GameStateManager.rm.loadImages(6);
		GameStateManager.rm.loadSoundPack(Resources.Sounds.GENERAL);
	}

	@Override
	public void update() {
		super.update();
		if (this.p.body.getPosition().x > (float) (this.w / 16)) {
			this.gsm.nextState();
		}
		if (this.p.body.getPosition().y < -5.0f) {
			this.init();
		}
	}

	@Override
	public void draw() {
		super.draw();
	}
}
