package com.sugarware.seedlings.gamestate;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.ScrollSelector;
import com.sugarware.seedlings.entities.Bulldozer;
import com.sugarware.seedlings.entities.VanillaCharacter;

public class Boss1 extends PlayGameState {
	float lightop = 0.0f;
	float shift = -50.0f;

	public Boss1(GameStateManager gsm) {
		super(gsm, "tilemaps/boss1.tmx");
		ArrayList<TextureRegion> icons = new ArrayList<TextureRegion>();
		int i = 0;
		while (i < 1) {
			icons.add(GameStateManager.rm.icons.get(i));
			++i;
		}
		this.ss = new ScrollSelector(icons, 2.0f, 1.5f, 1.5f, this);
		System.out.println("bosscalled");
	}

	@Override
	public void init() {
		super.init();
		this.p = new VanillaCharacter(this, 3.2906256f, 4.2916665f);
		this.p.unhappy = true;
		Bulldozer b = new Bulldozer(this, -25.0f, 8.0f);
		b.speed = 5.35f;
		this.getEntities().add(b);
		GameStateManager.rm.playSound(Resources.Sounds.GENERAL, 4);
		GameStateManager.rm.playSong(2);
		this.gsm.setNextState(7);
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
		if (k == 19) {
			this.lightop += 0.05f;
		}
		if (k == 20) {
			this.lightop -= 0.05f;
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
		this.rh.setAmbientLight(1.0f - this.lightop, 1.0f - this.lightop, 1.0f - this.lightop, this.lightop);
		if (this.p.body.getPosition().y < -5.0f) {
			this.init();
		}
		if (this.p.body.getPosition().x > (float) (this.w / 16)) {
			this.gsm.nextState();
			return;
		}

	}

	@Override
	public void draw() {
		super.draw();
	}
}
