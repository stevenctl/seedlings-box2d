/*
 * Decompiled with CFR 0_110.
 */
package com.sugarware.seedlings.entities;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.sugarware.seedlings.ResourceManager;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.entities.Animation;
import com.sugarware.seedlings.entities.Entity;
import com.sugarware.seedlings.entities.Fireball;
import com.sugarware.seedlings.entities.Luminous;
import com.sugarware.seedlings.entities.Player;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;
import java.util.ArrayList;

public class FireCharacter
extends Player
implements Luminous {
    public static final int IDLE = 0;
    public static final int RUN = 1;
    public static final int JUMP = 2;
    public static final int FALL = 3;
    public static final int SHOOT = 4;
    public static final int JUMPSHOOT = 5;
    public static final int FALLSHOOT = 6;
    public static final int RUNSHOOT = 7;
    public boolean shooting;
    int shootcooldown;
    boolean canshoot;
    float t = 0.0f;
    static final Color lightCol = new Color(0.956f, 0.345f, 0.208f, 0.7f);
    PointLight light;

    public FireCharacter(PlayGameState ps, float x, float y) {
        super(x, y, ps);
        this.animation = new Animation();
        this.currentAction = 0;
        this.unhappy_index = 16;
        this.frames = GameStateManager.rm.getImages(5);
        this.animation.setFrames((TextureRegion[])this.frames.get(this.currentAction));
        this.transition = new Animation();
        GameStateManager.rm.loadLongImage(9);
        this.transframes = GameStateManager.rm.getImages(9).get(0);
        this.transition.setFrames(this.transframes, 11);
        this.transition.setDelay(13);
    }

    public FireCharacter(PlayGameState ps, Player pl) {
        super(ps, pl);
        GameStateManager.rm.loadImages(5);
        this.frames = GameStateManager.rm.getImages(5);
        this.animation.setFrames((TextureRegion[])this.frames.get(this.currentAction));
        this.transition = new Animation();
        GameStateManager.rm.loadLongImage(9);
        this.transition.setFrames(GameStateManager.rm.getImages(9).get(0));
        this.transition.setDelay(13);
    }

    @Override
    public void update() {
        super.update();
        float dx = this.body.getLinearVelocity().x;
        float dy = this.body.getLinearVelocity().y;
        boolean grounded = this.isGrounded();
        if (!this.canshoot) {
            --this.shootcooldown;
            if (this.shootcooldown < 0) {
                this.canshoot = true;
            }
        }
        if (dx == 0.0f && grounded && !this.jumping) {
            if (this.shooting) {
                if (this.currentAction != 4) {
                    boolean k = this.currentAction == 7 || this.currentAction == 6 || this.currentAction == 4;
                    int f = this.animation.getFrame();
                    this.currentAction = 4;
                    this.animation.setFrames((TextureRegion[])this.frames.get(this.currentAction));
                    if (k) {
                        this.animation.setFrame(f);
                    }
                    this.animation.setDelay(15);
                }
            } else if (this.currentAction != 0) {
                boolean k = this.currentAction == 4;
                int f = this.animation.getFrame();
                this.currentAction = 0;
                this.animation.setFrames((TextureRegion[])this.frames.get(this.currentAction));
                if (k) {
                    this.animation.setFrame(f);
                }
                this.animation.setDelay(35);
            }
        } else if (dx != 0.0f && grounded && (this.LEFT || this.RIGHT) && !this.jumping) {
            if (this.shooting) {
                if (this.currentAction != 7) {
                    boolean k = this.currentAction == 6 || this.currentAction == 4;
                    int f = this.animation.getFrame();
                    this.currentAction = 7;
                    this.animation.setFrames((TextureRegion[])this.frames.get(this.currentAction));
                    if (k) {
                        this.animation.setFrame(f);
                    }
                    this.animation.setDelay(16);
                }
            } else if (this.currentAction != 1) {
                boolean k = this.currentAction == 7;
                int f = this.animation.getFrame();
                this.currentAction = 1;
                this.animation.setFrames((TextureRegion[])this.frames.get(this.currentAction));
                if (k) {
                    this.animation.setFrame(f);
                }
                this.animation.setDelay(16);
            }
        } else if (this.jumping) {
            if (this.shooting) {
                if (this.currentAction != 5) {
                    boolean k = this.currentAction == 7 || this.currentAction == 4;
                    int f = this.animation.getFrame();
                    this.currentAction = 5;
                    this.animation.setFrames((TextureRegion[])this.frames.get(this.currentAction));
                    if (k) {
                        this.animation.setFrame(f);
                    }
                    this.animation.setDelay(5);
                }
            } else if (this.currentAction != 2) {
                boolean k = this.currentAction == 5;
                int f = this.animation.getFrame();
                this.currentAction = 2;
                this.animation.setFrames((TextureRegion[])this.frames.get(2));
                if (k) {
                    this.animation.setFrame(f);
                }
                this.animation.setDelay(10);
            }
        } else if (dy < 0.0f && !grounded) {
            if (this.shooting) {
                if (this.currentAction != 6) {
                    boolean k = this.currentAction == 5 || this.currentAction == 7 || this.currentAction == 4 || this.currentAction == 3;
                    int f = this.animation.getFrame();
                    this.currentAction = 6;
                    this.animation.setFrames((TextureRegion[])this.frames.get(this.currentAction));
                    if (k) {
                        this.animation.setFrame(f);
                    }
                    this.animation.setDelay(5);
                }
            } else if (this.currentAction != 3) {
                boolean k = this.currentAction == 6;
                int f = this.animation.getFrame();
                this.currentAction = 3;
                this.animation.setFrames((TextureRegion[])this.frames.get(3));
                if (k) {
                    this.animation.setFrame(f);
                }
                this.animation.setDelay(35);
            }
        } else if (this.shooting) {
            if (this.currentAction != 4) {
                boolean k = this.currentAction == 7 || this.currentAction == 6 || this.currentAction == 4;
                int f = this.animation.getFrame();
                this.currentAction = 4;
                this.animation.setFrames((TextureRegion[])this.frames.get(this.currentAction));
                if (k) {
                    this.animation.setFrame(f);
                }
                this.animation.setDelay(15);
            }
        } else if (this.currentAction != 0) {
            boolean k = this.currentAction == 4;
            int f = this.animation.getFrame();
            this.currentAction = 0;
            this.animation.setFrames((TextureRegion[])this.frames.get(this.currentAction));
            if (k) {
                this.animation.setFrame(f);
            }
            this.animation.setDelay(35);
        }
        if ((this.animation.hasPlayedOnce() || this.animation.getFrame() > 9) && this.shooting) {
            this.shooting = false;
        }
        if (this.currentAction == 2) {
            if (!this.animation.hasPlayedOnce()) {
                this.animation.update();
            } else {
                this.jumping = false;
            }
        } else {
            this.animation.update();
        }
    }

    @Override
    public void alphaUpdate() {
        this.updateLight();
        super.alphaUpdate();
    }

    @Override
    public void keyDown(int k) {
        super.keyDown(k);
        if (k == 38 && this.canshoot) {
            this.shooting = true;
            this.canshoot = false;
            this.shootcooldown = 25;
            this.gs.getEntities().add(new Fireball(this.gs, this.body.getPosition().x, this.body.getPosition().y, this.facingRight));
            GameStateManager.rm.playSound(Resources.Sounds.GENERAL, 0, 0.07f);
        }
    }

    @Override
    public void updateLight() {
        if (this.light == null) {
            this.light = new PointLight(this.gs.rh, 500, lightCol, this.getWidth() * 4.0f, this.body.getPosition().x, this.body.getPosition().y);
            this.light.setContactFilter((short)2,(short) 0, (short)11);
            this.light.attachToBody(this.body);
        }
        this.t += 1.0f;
        this.light.setDistance(this.getWidth() * 15.0f + (float)Math.cos(this.t / 16.0f) / 1.5f);
    }

    @Override
    public void destroy() {
        this.light.remove();
    }
}

