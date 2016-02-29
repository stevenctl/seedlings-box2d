/*
 * Decompiled with CFR 0_110.
 */
package com.sugarware.seedlings.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.io.PrintStream;

public class Animation {
    private TextureRegion[] frames;
    private int curFrame;
    private long startTime;
    private long delay;
    private boolean playOnce = false;

    public void setFrames(TextureRegion[] frames) {
        this.frames = frames;
        this.curFrame = 0;
        this.startTime = System.nanoTime();
        this.playOnce = false;
    }

    public void setFrames(TextureRegion[] frames, int f) {
        this.frames = frames;
    }

    public void setDelay(long d) {
        this.delay = d;
    }

    public void setFrame(int i) {
        this.curFrame = i;
    }

    public void update() {
        if (this.delay == -1) {
            return;
        }
        long elapsed = (System.nanoTime() - this.startTime) / 1000000;
        if (elapsed > this.delay) {
            ++this.curFrame;
            this.startTime = System.nanoTime();
        }
        if (this.frames != null) {
            if (this.curFrame == this.frames.length - 1) {
                this.playOnce = true;
            }
            if (this.curFrame == this.frames.length) {
                this.curFrame = 0;
            }
        }
    }

    public TextureRegion[] getFrames() {
        return this.frames;
    }

    public int getFrame() {
        return this.curFrame;
    }

    public int getNumFrames() {
        return this.frames.length;
    }

    public TextureRegion getImage() {
        if (this.frames != null) {
            return this.frames[this.curFrame];
        }
        System.out.println("NO IMAGE!");
        return null;
    }

    public boolean hasPlayedOnce() {
        return this.playOnce;
    }

    public void setPlayedOnce(boolean b) {
        this.playOnce = b;
    }

    public long getDelay() {
        return this.delay;
    }
}

