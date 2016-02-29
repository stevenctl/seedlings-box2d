/*
 * Decompiled with CFR 0_110.
 */
package com.sugarware.seedlings;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.gamestate.GameState;
import java.io.PrintStream;
import java.util.ArrayList;

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
    Vector3 coordsin;
    GameState gs;
    public int sel = 0;
    public int cd = -1;

    public ScrollSelector(ArrayList<TextureRegion> list, float r, float f, float g, GameState gs) {
        this.list = list;
        this.r = r;
        this.iw = f;
        this.ih = g;
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
        this.coords.x = this.gs.cam.position.x + this.gs.cam.viewportWidth / 2.0f;
        this.coords.y = this.gs.cam.position.y + this.gs.cam.viewportHeight / 2.0f;
        if (this.cd > 0) {
            --this.cd;
        }
        if (this.scrolling) {
            this.theta = (float)((double)this.theta - 6.283185307179586 / (double)this.list.size() / 16.0);
            if ((double)this.theta >= 6.283185307179586) {
                this.theta = (float)((double)this.theta - 6.283185307179586);
            }
            if (this.theta < 0.0f) {
                this.theta = (float)((double)this.theta + 6.283185307179586);
            }
            if (this.theta >= this.endangle - 0.02f && this.theta <= this.endangle + 0.02f) {
                this.scrolling = false;
                this.cd = 30;
            }
        }
        float angle = 0.7853982f;
        int i = 0;
        while (i < this.list.size()) {
            float x = (float)(Math.cos(angle + this.theta) * (double)this.r);
            float y = (float)(Math.sin(angle + this.theta) * (double)this.r);
            g.draw(this.list.get(i), this.coords.x + x - this.iw / 2.0f, this.coords.y + y - this.ih / 2.0f, this.iw, this.ih);
            angle = (float)((double)angle + 6.283185307179586 / (double)this.list.size());
            if ((double)angle >= 6.283185307179586) {
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
            this.endangle = (float)((double)this.theta - 6.283185307179586 / (double)this.list.size());
            if ((double)this.endangle >= 6.283185307179586) {
                this.endangle = (float)((double)this.endangle - 6.283185307179586);
            }
            if (this.endangle < 0.0f) {
                this.endangle = (float)((double)this.endangle + 6.283185307179586);
            }
        }
    }
}

