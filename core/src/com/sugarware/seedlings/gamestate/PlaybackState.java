package com.sugarware.seedlings.gamestate;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.greenpipestudios.seedpeople.Script;

public abstract class PlaybackState extends PlayGameState {
	long ticks = 0;
	HashMap<Long, Integer> keyUp;
	HashMap<Long, Integer> keyDown;

	public PlaybackState(GameStateManager gsm, String map_path, String scriptPath) {
		super(gsm, map_path);
		try {
			InputStream scriptFile = Gdx.files.internal(scriptPath).read();
			ObjectInputStream reader = new ObjectInputStream(scriptFile);
			Object obj = reader.readObject();
			Script scr = (Script) obj;
			this.keyUp = scr.keyUp;
			this.keyDown = scr.keyDown;
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		super.init();
		this.ticks = 0;
	}

	@Override
	public void update() {
		super.update();
		++this.ticks;
		try {
			if (this.keyDown.keySet().contains(this.ticks)) {
				this.keyPressed(this.keyDown.get(this.ticks));
			}
			if (this.keyUp.keySet().contains(this.ticks)) {
				this.keyReleased(this.keyUp.get(this.ticks));
			}
		} catch (Exception var1_1) {
			// empty catch block
		}
	}
}
