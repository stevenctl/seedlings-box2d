/*
 * Decompiled with CFR 0_110.
 */
package com.sugarware.seedlings.entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.sugarware.seedlings.ResourceManager;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.entities.Animation;
import com.sugarware.seedlings.entities.Entity;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;
import java.util.ArrayList;

public class Bulldozer
extends Entity {
    public float speed = 3.67f;
    boolean bl = false;
    boolean br = false;
    boolean tl = false;
    boolean tr = false;
    final float w = 8.0f;
    final float h = 8.0f;
    int muscountdown;
    ArrayList<TextureRegion[]> frames;
    Animation animation = new Animation();
   

    public Bulldozer(PlayGameState gs, float x, float y) {
        super(gs);
        GameStateManager.rm.loadImages(13);
        this.animation.setFrames(GameStateManager.rm.getImages(13).get(0));
        this.animation.setDelay(100);
        this.muscountdown = 140;
       
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.gravityScale = 0.0f;
        bdef.type = BodyDef.BodyType.DynamicBody;
        PolygonShape pshape = new PolygonShape();
        pshape.setAsBox(6.0f, 4.0f, new Vector2(0.0f, -1.0f), 0.0f);
        this.body = gs.getWorld().createBody(bdef);
        Object[] arrobject = new Object[2];
        arrobject[0] = this;
        this.body.setUserData(arrobject);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = pshape;
        fdef.friction = 0.0f;
        fdef.density = 1.0f;
        fdef.isSensor = true;
        this.body.createFixture(fdef);
        pshape.dispose();
    }

    @Override
    public void update() {
        if (this.muscountdown == 0) {
        	 GameStateManager.rm.getSound(Resources.Sounds.GENERAL, 3).loop(0.7f);
            this.muscountdown = -1;
        } else if (this.muscountdown > 0) {
            --this.muscountdown;
        }
        this.body.setLinearVelocity(this.speed, this.body.getLinearVelocity().y);
        this.animation.update();
    }

    @Override
    public void draw(SpriteBatch g) {
        float ix = this.body.getPosition().x - 8.0f;
        float iy = this.body.getPosition().y + 12.0f - 1.0f;
        float iw = 16.0f;
        float ih = -16.0f;
        g.draw(this.animation.getImage(), ix, iy, iw, ih);
    }

    @Override
    public void collide(Entity e) {
    }

    @Override
    public void destroy() {
    	 GameStateManager.rm.getSound(Resources.Sounds.GENERAL, 3).stop();
    }
}

