package com.sugarware.seedlings.gamestate;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.ScrollSelector;
import com.sugarware.seedlings.entities.VanillaCharacter;
import com.sugarware.seedlings.entities.Water;

public class Level6 extends PlayGameState {

	boolean zoomedOut = false;
	float defaultW, defaultH;

	float targetlight = 0.8f;
	float light = 0.8f;

	public Level6(GameStateManager gsm) {
		super(gsm, "tilemaps/level6.tmx");
		ArrayList<TextureRegion> icons = new ArrayList<TextureRegion>();
		int i = 0;
		while (i < 2) {
			icons.add(GameStateManager.rm.icons.get(i));
			++i;
		}
		this.ss = new ScrollSelector(icons, 2.0f, 1.5f, 1.5f, this);
		this.defaultH = this.cam.viewportHeight;
		this.defaultW = this.cam.viewportWidth;
		GameStateManager.rm.playSong(Resources.Sounds.Songs.MEADOW2);

	}

	@Override
	public void init() {
		super.init();
		this.p = new VanillaCharacter(this, 3.0f, h / 16);
		this.gsm.setNextState(GameStateManager.MM);
		for (int i = 0; i < this.w; i += 128) {
			entities.add(new Water(this, i, 0, 128, 3.0f));
		}
		entities.add(new Water(this, 0, 64f, 25, 1.5f));
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
		System.out.println("Player Coords: " + this.p.body.getPosition());
	}

	@Override
	public void touchUp(Vector3 coords, int pointer) {
	}

	@Override
	public void keyPressed(int k) {
		super.keyPressed(k);
		this.p.keyDown(k);
		if (k == Keys.I) {
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
			return;
		}

		if (this.p.body.getPosition().y < -5.0f) {
			this.init();
		}

		targetlight = p.body.getPosition().y < 60f && p.body.getPosition().x < 83 ? 0.3f : 0.8f;

		if (light > targetlight) {
			light -= 0.009f;
		}
		if (light < targetlight) {
			light += 0.009f;
		}

		rh.setAmbientLight(light, light, light, 1.0f - light);

	}

	@Override
	public void draw() {
		super.draw();
	}
}
