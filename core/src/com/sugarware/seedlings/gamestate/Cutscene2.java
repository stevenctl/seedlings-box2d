package com.sugarware.seedlings.gamestate;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.ScrollSelector;
import com.sugarware.seedlings.entities.Bulldozer;
import com.sugarware.seedlings.entities.VanillaCharacter;
import com.sugarware.seedlings.entities.Water;

public class Cutscene2 extends PlaybackState {
	float shift = -50.0f;

	public Cutscene2(GameStateManager gsm) {
		super(gsm, "tilemaps/cutscene2.tmx", "scripts/cut2.sav");
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
		this.p = new VanillaCharacter(this, 0.0f, 31.0f);
		entities.add(new Bulldozer(this, -20.0f, 31.0f));
		entities.add(new Water(this, -1.0f, -1.0f, this.w / 16 + 2, 8.0f));
		this.p.unhappy = true;
		this.gsm.setNextState(8);
		GameStateManager.rm.playSound(Resources.Sounds.GENERAL, 4);
		this.rh.setAmbientLight(0.8f, 0.8f, 0.8f, 0.2f);
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
		GameStateManager.rm.loadSoundPack(Resources.Sounds.GENERAL);
	}

	@Override
	public void update() {
		super.update();
		if (this.p.body.getPosition().x > (float) (this.w / 16 + 5)) {
			this.gsm.nextState();
			return;
		}
	}

	@Override
	public void draw() {
		super.draw();
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
