/*
 * Decompiled with CFR 0_110.
 */
package com.sugarware.seedlings.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;
import com.sugarware.seedlings.ResourceManager;
import com.sugarware.seedlings.entities.Animation;
import com.sugarware.seedlings.entities.Entity;
import com.sugarware.seedlings.entities.Explosion;
import com.sugarware.seedlings.entities.Fireball;
import com.sugarware.seedlings.entities.Rope;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;
import java.util.ArrayList;

public class Robot
extends Entity {
    final float defaultaccel = 10.0f;
    final float defaultmax = 6.0f;
    float maxvel = 6.0f;
    float accel = 10.0f;
    final float damp1 = 5.0f;
    final float damp2 = 1.0f;
    Vector2 impulse;
    public float alpha;
    public boolean facingRight = true;
    final float jumpPower = 25.0f;
    public Fixture groundSensorFixture;
    Vector2 sensorPos;
    boolean jumping = false;
    private int jumpCool = 20;
    int health;
    public Animation animation = new Animation();
    public ArrayList<TextureRegion[]> frames;
    public static final int IDLE = 0;
    public static final int RUN = 1;
    public static final int FALL = 2;
    int currentAction;
    public float w;
    public float h;
    public float size = 1.0f;
    static Filter filter;
    boolean hiding;
    Vector2 jumpVector = new Vector2();
    boolean UP = false;
    boolean DOWN = false;
    boolean LEFT = false;
    boolean RIGHT = false;
    boolean SPACE = false;
    boolean J = false;
    public boolean dead;

    public Robot(PlayGameState gs, float x, float y, boolean r) {
        super(gs);
        GameStateManager.rm.loadImages(4);
        this.frames = GameStateManager.rm.getImages(4);
        this.animation.setFrames(this.frames.get(this.currentAction));
        if (filter == null) {
            filter = new Filter();
            Robot.filter.categoryBits = 4;
            Robot.filter.maskBits = 13;
        }
        float w = 0.8f;
        float h = 1.0f;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x, y);
        this.body = gs.getWorld().createBody(def);
        Object[] arrobject = new Object[2];
        arrobject[0] = this;
        this.body.setUserData(arrobject);
        this.facingRight = r;
        if (r) {
            this.keyDown(32);
        } else {
            this.keyDown(29);
        }
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w, h);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1.0f;
        fdef.friction = 1.5f;
        this.body.setLinearDamping(1.0f);
        fdef.restitution = 0.0f;
        Fixture f = this.body.createFixture(fdef);
        f.setFilterData(filter);
        this.body.setFixedRotation(true);
        shape.dispose();
        this.w = w;
        this.h = h;
        FixtureDef sdef = new FixtureDef();
        shape = new PolygonShape();
        sdef.friction = 0.0f;
        shape.setAsBox(0.05f, h - 0.01f, new Vector2(- w, 0.11f), 0.0f);
        sdef.shape = shape;
        f = this.body.createFixture(sdef);
        f.setFilterData(filter);
        shape.setAsBox(0.05f, h - 0.01f, new Vector2(w, 0.11f), 0.0f);
        f = this.body.createFixture(sdef);
        f.setFilterData(filter);
        shape.dispose();
        PolygonShape pgon = new PolygonShape();
        this.sensorPos = new Vector2(0.0f, - h);
        pgon.setAsBox(w, h / 4.0f, this.sensorPos, 0.0f);
        this.groundSensorFixture = this.body.createFixture(pgon, 0.0f);
        this.groundSensorFixture.setSensor(true);
        this.groundSensorFixture.setFilterData(filter);
        pgon.dispose();
        this.alpha = 1.0f;
    }

    public void alphaUpdate() {
        if (this.alpha < 1.0f && !this.dead) {
            this.alpha += 0.015f;
        }
        if (this.alpha > 1.0f) {
            this.alpha = 1.0f;
        }
    }

    @Override
    public void draw(SpriteBatch g) {
        float ix = this.body.getPosition().x - 1.5f * this.size;
        float iy = this.body.getPosition().y + 2.0f * this.size;
        float iw = 3.0f;
        float ih = -3.0f;
        g.setColor(1.0f, 1.0f, 1.0f, this.alpha);
        if (this.facingRight) {
            if (this.animation.getImage() != null) {
                g.draw(this.animation.getImage(), ix, iy, iw, ih);
            }
        } else if (this.animation.getImage() != null) {
            g.draw(this.animation.getImage(), ix + iw, iy, - iw, ih);
        }
        g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void update() {
        float dx = this.body.getLinearVelocity().x;
        float dy = this.body.getLinearVelocity().y;
        if (dx == 0.0f) {
            if (this.facingRight) {
                this.keyUp(32);
                this.keyDown(29);
            } else {
                this.keyDown(32);
                this.keyUp(29);
            }
        }
        if (this.impulse == null) {
            this.impulse = new Vector2();
        }
        if (this.isGrounded()) {
            this.body.setLinearDamping(5.0f);
        } else {
            this.body.setLinearDamping(1.0f);
        }
        if (this.jumping) {
            if (this.jumpCool <= 0) {
                if (this.isGrounded()) {
                    this.jumping = false;
                }
            } else {
                --this.jumpCool;
            }
        }
        if (this.RIGHT) {
            this.impulse.set(this.accel * this.body.getMass(), 0.0f);
            this.body.applyLinearImpulse(this.impulse, this.body.getPosition(), true);
        } else if (this.LEFT) {
            this.impulse.set((- this.accel) * this.body.getMass(), 0.0f);
            this.body.applyLinearImpulse(this.impulse, this.body.getPosition(), true);
        }
        if (this.body.getLinearVelocity().x < - this.maxvel) {
            this.body.setLinearVelocity(- this.maxvel, this.body.getLinearVelocity().y);
        } else if (this.body.getLinearVelocity().x > this.maxvel) {
            this.body.setLinearVelocity(this.maxvel, this.body.getLinearVelocity().y);
        }
        boolean grounded = this.isGrounded();
        if (dx == 0.0f && grounded) {
            if (this.currentAction != 0) {
                this.currentAction = 0;
                this.animation.setFrames(this.frames.get(this.currentAction));
                this.animation.setDelay(-1);
            }
        } else if (dx != 0.0f && grounded && (this.LEFT || this.RIGHT)) {
            if (this.currentAction != 1) {
                this.currentAction = 1;
                this.animation.setFrames(this.frames.get(this.currentAction));
                this.animation.setDelay(16);
            }
        } else if (dy != 0.0f && !grounded) {
            if (this.currentAction != 2) {
                this.currentAction = 2;
                this.animation.setFrames(this.frames.get(2));
                this.animation.setDelay(-1);
            }
        } else if (this.currentAction != 0) {
            this.currentAction = 0;
            this.animation.setFrames(this.frames.get(this.currentAction));
            this.animation.setDelay(-1);
        }
        if (dx == 0.0f && dy == 0.0f && !this.hiding) {
            this.LEFT = !this.LEFT;
            this.facingRight = this.RIGHT = !this.RIGHT;
        }
        this.animation.update();
    }

    protected boolean isGrounded() {
        Array<Contact> contactList = this.gs.getWorld().getContactList();
        int i = 0;
        while (i < contactList.size) {
            Contact contact = contactList.get(i);
            if (contact.isTouching() && (contact.getFixtureA() == this.groundSensorFixture || contact.getFixtureB() == this.groundSensorFixture)) {
                Object ua = contact.getFixtureA().getBody().getUserData();
                Object ub = contact.getFixtureB().getBody().getUserData();
                ua = ((Object[])ua)[0];
                ub = ((Object[])ub)[0];
                if (!(contact.getFixtureA() == this.groundSensorFixture ? !(ub +"").contains("world") : !(ua +"").contains("world"))) {
                    Vector2 pos = this.body.getPosition();
                    WorldManifold manifold = contact.getWorldManifold();
                    boolean below = true;
                    int j = 0;
                    while (j < manifold.getNumberOfContactPoints()) {
                        below &= manifold.getPoints()[j].y < pos.y - 1.5f;
                        ++j;
                    }
                    if (below) {
                        return true;
                    }
                    return false;
                }
            }
            ++i;
        }
        return false;
    }

    public void keyUp(int k) {
        switch (k) {
            case 51: {
                this.UP = false;
                break;
            }
            case 47: {
                this.DOWN = false;
                break;
            }
            case 29: {
                this.LEFT = false;
                break;
            }
            case 32: {
                this.RIGHT = false;
                break;
            }
            case 62: {
                this.SPACE = false;
                break;
            }
            case 38: {
                this.J = false;
            }
        }
    }

    public void keyDown(int k) {
        switch (k) {
            case 51: {
                this.UP = true;
                break;
            }
            case 47: {
                this.DOWN = true;
                break;
            }
            case 29: {
                this.LEFT = true;
                this.facingRight = false;
                break;
            }
            case 32: {
                this.RIGHT = true;
                this.facingRight = true;
                break;
            }
            case 38: {
                this.J = true;
                break;
            }
            case 62: {
                this.SPACE = true;
                if (!this.isGrounded()) break;
                this.jumpVector.set(0.0f, 25.0f);
                this.jumping = true;
                this.jumpCool = 20;
                this.body.setLinearVelocity(this.jumpVector);
                break;
            }
            case 45: {
                this.gs.getEntities().add(new Rope(this.gs, this.body.getPosition().x, this.body.getPosition().y, 5.0f));
            }
        }
    }

    @Override
    public void collide(Entity e) {
        if (e instanceof Fireball) {
            this.mark();
        }
    }

    @Override
    public void destroy() {
        if (this.gs.getWorld() == null) {
            return;
        }
        this.body.setLinearVelocity(0.0f, 0.0f);
        this.gs.getEntities().add(new Explosion(this.gs, this.body, null, true));
    }

    public String toString() {
        return "world " + super.toString();
    }
}

