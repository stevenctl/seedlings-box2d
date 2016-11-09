package com.sugarware.seedlings;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.gamestate.PlayGameState;

/**
 * Wheel that appears in top right corner for selecting character.
 *
 */
public class ScrollSelector {
	public float theta;
	float startangle;
	float endangle;
	public boolean scrolling;
	float r;
	public ArrayList<TextureRegion> list;
	float iw;
	float ih;
	Vector3 coords;
	PlayGameState gs;
	public int sel = 0;
	public int cd = -1;

	public ScrollSelector(ArrayList<TextureRegion> list, float r, float f, float g, PlayGameState gs) {
		this.list = list;
		
		this.coords = new Vector3();
		this.gs = gs;
		if (list.size() < 6) {
			int s;
			int i = s = list.size();
			while (i < 2 * s) {
				list.add(list.get(i - s));
				++i;
			}
		}
	}

	public void draw(SpriteBatch g) {
		this.coords.x = this.gs.scrCam.viewportWidth;
		this.coords.y =  this.gs.scrCam.viewportHeight;
		this.r = this.ih =  this.iw = this.gs.scrCam.viewportHeight / 12;
		if (this.cd > 0) {
			--this.cd;
		}
		if (this.scrolling) {
			this.theta = (float) ((double) this.theta - 6.283185307179586 / (double) this.list.size() / 16.0);
			if ((double) this.theta >= 6.283185307179586) {
				this.theta = (float) ((double) this.theta - 6.283185307179586);
			}
			if (this.theta < 0.0f) {
				this.theta = (float) ((double) this.theta + 6.283185307179586);
			}
			if (this.theta >= this.endangle - 0.02f && this.theta <= this.endangle + 0.02f) {
				this.scrolling = false;
				this.cd = 30;
			}
		}
		float angle = 0.7853982f;
		int i = 0;
		while (i < this.list.size()) {
			float x = (float) (Math.cos(angle + this.theta) * (double) this.r);
			float y = (float) (Math.sin(angle + this.theta) * (double) this.r);
			g.draw(this.list.get(i), this.coords.x + x - this.iw / 2.0f, this.coords.y + y - this.ih / 2.0f, this.iw,
					this.ih);
			angle = (float) ((double) angle + 6.283185307179586 / (double) this.list.size());
			if ((double) angle >= 6.283185307179586) {
				angle = 0.0f;
			}
			++i;
		}
	}

	public void scroll() {
		this.cd = -1;
		if (!this.scrolling) {
			++this.sel;
			if (this.sel >= this.list.size() / 2) {
				this.sel = 0;
			}
			System.out.println(this.sel);
			this.scrolling = true;
			this.startangle = this.theta;
			this.endangle = (float) ((double) this.theta - 6.283185307179586 / (double) this.list.size());
			if ((double) this.endangle >= 6.283185307179586) {
				this.endangle = (float) ((double) this.endangle - 6.283185307179586);
			}
			if (this.endangle < 0.0f) {
				this.endangle = (float) ((double) this.endangle + 6.283185307179586);
			}
		}
	}
}
