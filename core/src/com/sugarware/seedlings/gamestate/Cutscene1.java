package com.sugarware.seedlings.gamestate;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.ScrollSelector;
import com.sugarware.seedlings.entities.Bulldozer;
import com.sugarware.seedlings.entities.Entity;
import com.sugarware.seedlings.entities.FlowerCharacter;
import com.sugarware.seedlings.entities.Hut;
import com.sugarware.seedlings.entities.Player;
import com.sugarware.seedlings.entities.VanillaCharacter;

public class Cutscene1 extends PlaybackState {
	boolean reset;
	int hutn = 3;
	Player p2;
	Player p3;
	Player p4;
	Player p5;
	int intensecountdown = 360;
	boolean serene = true;
	boolean approach = false;
	boolean bad = false;
	int t1 = 120;

	public Cutscene1(GameStateManager gsm) {
		super(gsm, "tilemaps/cutscene1.tmx", "scripts/cut1.sav");
		this.initialized = false;
		GameStateManager.rm.loadWaterImages();
		ArrayList<TextureRegion> icons = new ArrayList<TextureRegion>();
		this.ss = new ScrollSelector(icons, 32.0f, 24.0f, 24.0f, this);
	}

	@Override
	public void init() {
		super.init();
		this.getEntities().add(new Hut(this, 20.75f, 3.125f));
		this.getEntities().add(new Hut(this, 14.5f, 3.125f));
		this.getEntities().add(new Hut(this, 8.25f, 3.125f));
		System.out.println("called init");
		this.gsm.setNextState(6);
		GameStateManager.rm.playSong(5);
		GameStateManager.rm.loadSoundPack(Resources.Sounds.GENERAL);
		for (Sound s : GameStateManager.rm.getSoundPack(Resources.Sounds.GENERAL)) {
			s.stop();
		}
		GameStateManager.rm.getSound(Resources.Sounds.GENERAL, 2).loop();
		this.p = new VanillaCharacter(this, 4.0f, 3.125f);
		this.p2 = new VanillaCharacter(this, 12.0f, 3.125f);
		this.p3 = new FlowerCharacter(this, 5.0f, 3.125f);
		this.p4 = new VanillaCharacter(this, 10.0f, 3.125f);
		this.p4.setScale(0.75f);
		this.p2.setScale(0.75f);
		this.p4.jumpPower *= 0.7f;
		this.p4.keyDown(32);
		this.p5 = new VanillaCharacter(this, 16.75f, 3.125f);
		this.getEntities().add(this.p2);
		this.getEntities().add(this.p3);
		this.getEntities().add(this.p4);
		this.getEntities().add(this.p5);
		this.rh.setAmbientLight(1.0f, 1.0f, 1.0f, 0.0f);
		this.initialized = true;
	}

	@Override
	public void draw() {
		if (this.initialized) {
			super.draw();
		}
	}

	@Override
	public void dispose() {
		GameStateManager.rm.unloadImages(5);
		GameStateManager.rm.unloadImages(1);
		GameStateManager.rm.unloadImages(4);

		GameStateManager.rm.unloadImages(6);

		GameStateManager.rm.unloadImages(8);
		GameStateManager.rm.stopSong();
	}

	@Override
	public void update() {
		if (!GameStateManager.rm.getMusic().isPlaying()) {
			--this.intensecountdown;
			if (this.intensecountdown == 0) {
				GameStateManager.rm.playSong(1);
			}
		}
		super.update();
		if (this.initialized) {
			--this.t1;
			if (this.serene) {
				if (this.p4.isGrounded()) {
					this.p4.keyDown(62);
					this.p4.keyUp(62);
					if (this.p4.facingRight) {
						this.p4.keyUp(32);
						this.p4.keyDown(29);
					} else {
						this.p4.keyUp(29);
						this.p4.keyDown(32);
					}
				}
				if (this.t1 % 40 == 0 && this.p2.body.getLinearVelocity().x == 0.0f) {
					if (this.p2.facingRight) {
						this.p2.keyDown(29);
					} else {
						this.p2.keyDown(32);
					}
				}
				if (this.t1 % 18 == 0 && this.p2.body.getLinearVelocity().x != 0.0f) {
					if (this.p2.facingRight) {
						this.p2.keyUp(32);
					} else {
						this.p2.keyUp(29);
					}
				}
				if (this.t1 % 60 == 0 && this.p5.body.getLinearVelocity().x == 0.0f) {
					if (this.p5.facingRight) {
						this.p5.keyDown(29);
					} else {
						this.p5.keyDown(32);
					}
				}
				if (this.t1 % 24 == 0 && this.p5.body.getLinearVelocity().x != 0.0f) {
					if (this.p5.facingRight) {
						this.p5.keyUp(32);
					} else {
						this.p5.keyUp(29);
					}
				}
				if (this.t1 <= 0) {
					this.t1 = 120;
				}
			} else if (this.approach) {
				this.p2.LEFT = false;
				this.p3.LEFT = false;
				this.p4.LEFT = false;
				this.p5.LEFT = false;
				this.p2.RIGHT = false;
				this.p3.RIGHT = false;
				this.p4.RIGHT = false;
				this.p5.RIGHT = false;
				this.p2.facingRight = false;
				this.p3.facingRight = false;
				this.p5.facingRight = false;
				this.p4.facingRight = false;
				if (this.p2.body.getLinearVelocity().y == 0.0f && this.t1 % 30 == 0) {
					this.p2.keyDown(62);
					this.p2.keyUp(62);
				}
				if (this.p3.body.getLinearVelocity().y == 0.0f && this.t1 % 20 == 0) {
					this.p3.keyDown(62);
					this.p3.keyUp(62);
				}
				if (this.p4.body.getLinearVelocity().y == 0.0f && this.t1 % 40 == 0) {
					this.p4.keyDown(62);
					this.p4.keyUp(62);
				}
				if (this.p5.body.getLinearVelocity().y == 0.0f && this.t1 % 10 == 0) {
					this.p5.keyDown(62);
					this.p5.keyUp(62);
				}
				if (this.t1 == 0) {
					this.t1 = 60;
				}
			} else if (this.bad) {
				if (!this.p2.RIGHT) {
					this.p2.keyDown(32);
				}
				if (!this.p3.RIGHT) {
					this.p3.keyDown(32);
				}
				if (!this.p4.RIGHT) {
					this.p4.keyDown(32);
				}
				if (!this.p5.RIGHT) {
					this.p5.keyDown(32);
				}
				for (Entity e : this.getEntities()) {
					if (!(e instanceof Bulldozer))
						continue;
					this.gsm.setNextState(6);
					if (e.body.getPosition().x <= (float) (this.w - 80) / 16.0f)
						continue;
					System.out.println("218");
					this.gsm.nextState();
				}
			}
		}
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
	protected void keyPressed(int k) {
		super.keyPressed(k);
		this.p.keyDown(k);
		if (k == 7) {
			this.serene = false;
			this.approach = true;
			this.getEntities().add(new Bulldozer(this, -32.0f, 8.0f));
			GameStateManager.rm.stopSong();
			GameStateManager.rm.playSound(Resources.Sounds.GENERAL, 4);
			for (Entity e : this.getEntities()) {
				if (!(e instanceof Player))
					continue;
				((Player) e).unhappy = true;
			}
			this.p.unhappy = true;
		}
		if (k == 9) {
			this.approach = false;
			this.bad = true;
		}
		if (k == 8) {
			int c = this.hutn--;
			for (Entity e : this.getEntities()) {
				if (!(e instanceof Hut))
					continue;
				GameStateManager.rm.playSound(Resources.Sounds.GENERAL, 5);
				if (--c != 0)
					continue;
				((Hut) e).demolished = true;
			}
		}
	}

	@Override
	protected void keyReleased(int k) {
		this.p.keyUp(k);
	}

	@Override
	public void gatherResources() {
		GameStateManager.rm.loadSoundPack(Resources.Sounds.GENERAL);
	}
}
