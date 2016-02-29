/*
 * Decompiled with CFR 0_110.
 */
package com.sugarware.seedlings.gamestate;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.sugarware.seedlings.ResourceManager;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.ScrollSelector;
import com.sugarware.seedlings.entities.Entity;
import com.sugarware.seedlings.entities.FlowerCharacter;
import com.sugarware.seedlings.entities.Player;
import com.sugarware.seedlings.entities.Robot;
import com.sugarware.seedlings.entities.VanillaCharacter;
import com.sugarware.seedlings.entities.Water;
import com.sugarware.seedlings.gamestate.GameState;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;
import java.util.ArrayList;

public class TestingLevel
extends PlayGameState {
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
        this.ss = new ScrollSelector(icons, 2.0f, 1.5f, 1.5f, this);
    }

    @Override
    public void init() {
        super.init();
        this.p = new VanillaCharacter(this, this.spawn.x, this.spawn.y);
        this.rh.setAmbientLight(this.light, this.light, this.light, 1.0f - this.light);
        this.getEntities().add(new Robot(this, 116.0f, 16.0f, true));
        this.getEntities().add(new Water(this, 52.7f, 8.3f, 10.0f, 3.0f));
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
        int i;
        ArrayList<TextureRegion> icons;
        super.update();
        this.rh.setAmbientLight(this.light, this.light, this.light, 1.0f - this.light);
        if (this.light > this.targetlight) {
            this.light -= 0.009f;
        }
        if (this.light < this.targetlight) {
            this.light += 0.009f;
        }
        if (this.p.body.getPosition().x > 60.0f && this.ss.list.size() < 4) {
            this.spawn.set(this.p.body.getPosition());
            icons = new ArrayList<TextureRegion>();
            i = 0;
            while (i < 2) {
                icons.add(GameStateManager.rm.icons.get(i));
                ++i;
            }
            this.ss = new ScrollSelector(icons, 2.0f, 1.5f, 1.5f, this);
        }
        if (this.p.body.getPosition().x > 86.0f && this.ss.list.size() < 6) {
            this.spawn.set(this.p.body.getPosition());
            icons = new ArrayList();
            i = 0;
            while (i < 3) {
                icons.add(GameStateManager.rm.icons.get(i));
                ++i;
            }
            this.ss = new ScrollSelector(icons, 2.0f, 1.5f, 1.5f, this);
            if (this.p instanceof FlowerCharacter) {
                this.ss.scroll();
            }
        }
        float f = this.targetlight = this.p.body.getPosition().x > 96.0f ? 0.1f : 0.8f;
        if (this.p.body.getPosition().x > (float)(this.w / 16)) {
            this.gsm.setState(0);
        }
        if (this.p.body.getPosition().y < -5.0f) {
            this.init();
        }
    }

    @Override
    public void draw() {
        super.draw();
    }
}

