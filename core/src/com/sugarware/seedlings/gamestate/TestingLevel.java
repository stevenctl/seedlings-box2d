package com.sugarware.seedlings.gamestate;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.ScrollSelector;
import com.sugarware.seedlings.entities.FlowerCharacter;
import com.sugarware.seedlings.entities.Robot;
import com.sugarware.seedlings.entities.VanillaCharacter;
import com.sugarware.seedlings.entities.Water;

public class TestingLevel extends PlayGameState {
	Vector2 spawn = new Vector2(4.0f, 11.0f);
	float targetlight = 0.8f;
	float light = 0.8f;
	float shift = -50.0f;

	public TestingLevel(GameStateManager gsm) {
		super(gsm, "tilemaps/demo.tmx");
		ArrayList<TextureRegion> icons = new ArrayList<TextureRegion>();
		int i = 0;
		while (i < 1) {
			icons.add(GameStateManager.rm.icons.get(i));
			++i;
		}
		ss = new ScrollSelector(icons, 2.0f, 1.5f, 1.5f, this);
	}

	@Override
	public void init() {
		super.init();
		p = new VanillaCharacter(this, spawn.x, spawn.y);
		rh.setAmbientLight(light, light, light, 1.0f - light);
		getEntities().add(new Robot(this, 116.0f, 16.0f, true));
		getEntities().add(new Water(this, 52.7f, 8.3f, 10.0f, 3.0f));
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
		p.keyDown(k);
		if (k == 37) {
			ss.scroll();
		}
	}

	@Override
	public void keyReleased(int k) {
		p.keyUp(k);
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
		int i;
		ArrayList<TextureRegion> icons;
		super.update();
		rh.setAmbientLight(light, light, light, 1.0f - light);
		if (light > targetlight) {
			light -= 0.009f;
		}
		if (light < targetlight) {
			light += 0.009f;
		}
		if (p.body.getPosition().x > 60.0f && ss.list.size() < 4) {
			spawn.set(p.body.getPosition());
			icons = new ArrayList<TextureRegion>();
			i = 0;
			while (i < 2) {
				icons.add(GameStateManager.rm.icons.get(i));
				++i;
			}
			ss = new ScrollSelector(icons, 2.0f, 1.5f, 1.5f, this);
		}
		if (p.body.getPosition().x > 86.0f && ss.list.size() < 6) {
			spawn.set(p.body.getPosition());
			icons = new ArrayList<TextureRegion>();
			i = 0;
			while (i < 3) {
				icons.add(GameStateManager.rm.icons.get(i));
				++i;
			}
			ss = new ScrollSelector(icons, 2.0f, 1.5f, 1.5f, this);
			if (p instanceof FlowerCharacter) {
				ss.scroll();
			}
		}
		targetlight = p.body.getPosition().x > 96.0f ? 0.1f : 0.8f;
		if (p.body.getPosition().x > (float) (w / 16)) {
			gsm.setState(GameStateManager.MM);
			return;
		}
		if (p.body.getPosition().y < -5.0f) {
			init();
		}
	}

	@Override
	public void draw() {
		super.draw();
	}
}
