/*
 * Decompiled with CFR 0_110.
 */
package com.sugarware.seedlings.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.ResourceManager;
import com.sugarware.seedlings.Shaders;
import com.sugarware.seedlings.entities.Entity;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class Water
extends Entity {
    boolean d = true;
    float width;
    float height;
    int c = 0;
    float x;
    float y;
    TextureRegion scr;
    ShapeRenderer s;
    float time;
    float time2;
    ShaderProgram waterShader;
    ShaderProgram waterShader2;
    float scaleX;
    float scaleY;
    Vector3 coords;

    public Water(PlayGameState gs, float x, float y, float f, float g) {
        super(gs);
        this.scaleX = (float)Gdx.graphics.getWidth() / this.gs.cam.viewportWidth;
        this.scaleY = (float)Gdx.graphics.getHeight() / this.gs.cam.viewportHeight;
        this.x = x;
        this.y = y;
        this.width = f;
        this.height = g;
        this.coords = gs.cam.project(new Vector3(x, y, 0.0f));
        this.waterShader = new ShaderProgram(Shaders.vertexShader, Shaders.waterFragmentShader);
        this.waterShader2 = new ShaderProgram(Shaders.vertexShader, Shaders.water2FragmentShader);
        this.s = new ShapeRenderer();
        this.scr = new TextureRegion();
    }

    @Override
    public void update() {
    }

    public void updateshaders() {
        float dt = Gdx.graphics.getDeltaTime();
        if (this.waterShader != null && this.waterShader2 != null) {
            float angle2;
            this.time2 += dt / 2.0f;
            this.time += dt;
            float angle = this.time * 6.2831855f;
            if (angle > 6.2831855f) {
                angle -= 6.2831855f;
            }
            if ((angle2 = this.time2 * 6.2831855f) > 6.2831855f) {
                angle2 -= 6.2831855f;
            }
            Gdx.gl20.glBlendFunc(770, 771);
            Gdx.gl20.glEnable(3042);
            this.waterShader.setUniformMatrix("u_projTrans", this.gs.cam.combined);
            this.waterShader2.setUniformMatrix("u_projTrans", this.gs.cam.combined);
            this.waterShader.begin();
            this.waterShader.setUniformf("timedelta", - angle);
            this.waterShader.setUniformf("ampdelta", - angle2);
            this.waterShader.end();
            this.waterShader2.begin();
            this.waterShader2.setUniformf("timedelta", - angle);
            this.waterShader2.end();
            Gdx.gl20.glDisable(3042);
        }
    }

    @Override
    public void draw(SpriteBatch g) {
        g.end();
        this.updateshaders();
        this.coords.set(this.x, this.y, 0.0f);
        this.coords = this.gs.cam.project(this.coords);
        this.scr.setTexture((Texture)this.gs.fbo.getColorBufferTexture());
        int vshift = (int)(0.13f * (float)Gdx.graphics.getHeight());
        this.scr.setRegion((int)this.coords.x, (int)this.coords.y + vshift, (int)(this.width * this.scaleX), (int)(this.height * this.scaleY));
        this.scr.flip(false, true);
        float toph = (float)GameStateManager.rm.water_top.getRegionHeight() / 16.0f;
        if (this.scr != null) {
            g.setProjectionMatrix(this.gs.cam.combined);
            g.setShader(this.waterShader2);
            g.begin();
            g.draw(this.scr, (float)((int)this.x), (float)((int)this.y), (float)((int)this.width), (float)((int)this.height));
            g.end();
        }
        g.setShader(this.waterShader);
        g.begin();
        g.draw(GameStateManager.rm.water_top, this.x, this.y + this.height - toph, this.width, toph);
        g.end();
        g.setShader(null);
        g.begin();
        g.draw(GameStateManager.rm.water, this.x, this.y, this.width, this.height - toph);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void collide(Entity e) {
    }
}

