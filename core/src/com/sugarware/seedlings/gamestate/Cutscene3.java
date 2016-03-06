package com.sugarware.seedlings.gamestate;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.ScrollSelector;
import com.sugarware.seedlings.entities.FlowerCharacter;
import com.sugarware.seedlings.entities.Player;
import com.sugarware.seedlings.entities.VanillaCharacter;
import com.sugarware.seedlings.entities.Water;

public class Cutscene3 extends PlaybackState {
	Player p2;
	float light = 0.0f;
	boolean excite = false;
	boolean run = false;
	int jt = 30;
	int rt = 10;
	float maxlight = 0.5f;
	float shift = -50.0f;

	public Cutscene3(GameStateManager gsm) {
		super(gsm, "tilemaps/cutscene3.tmx", "scripts/cut3.sav");
		GameStateManager.rm.getSound(Resources.Sounds.GENERAL, Resources.Sounds.General.BULLDOZER_LOOP).stop();
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
		GameStateManager.rm.playSong(0);
		this.p = new VanillaCharacter(this, 0.625f, (float) (this.h - 30) / 16.0f);
		this.p2 = new FlowerCharacter(this, 27.5f, (float) (this.h - 100) / 16.0f);
		this.p2.facingRight = false;
		this.p2.unhappy = true;
		entities.add(new Water(this, 0.0f, 0.0f, 11.25f, 4.375f));
		entities.add(this.p2);
		this.p.unhappy = true;
		this.gsm.setNextState(9);
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
		if (k == 7) {
			this.excite = true;
		} else if (k == 8) {
			this.run = true;
			this.excite = false;
			this.maxlight = 1.0f;
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
		this.rh.setAmbientLight(this.light, this.light, this.light, 1.0f - this.light);
		if (this.light < this.maxlight && this.ticks % 10 == 0) {
			this.light += 0.03f;
		}
		if (this.excite) {
			this.p.unhappy = false;
			this.p2.unhappy = false;
			--this.jt;
			if (this.jt < 0) {
				this.jt = 40;
				this.p2.keyDown(62);
			}
			if (this.rt > 0) {
				--this.rt;
				if (this.rt <= 0) {
					this.rt = 10;
					this.p2.keyUp(62);
				}
			}
		} else if (this.run) {
			this.p2.keyDown(32);
		}
		if (this.p.body.getPosition().x > (float) (this.w / 16 + 5)) {
			this.gsm.nextState();
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
