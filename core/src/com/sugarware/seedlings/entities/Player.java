package com.sugarware.seedlings.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;
import com.sugarware.seedlings.gamestate.PlaybackState;

public abstract class Player extends Entity {
	public float defaultaccel = 10.0f;
	public float swingaccel = 0.5f;
	public float swingmax = 10.0f;
	public float defaultmax;
	public float maxvel;
	float accel;
	final float damp1 = 5.0f;
	final float damp2 = 1.0f;
	Vector2 impulse;
	public Animation transition;
	public Animation oldanim;
	protected float oldalpha;
	float ox;
	float oy;
	public float alpha;
	public boolean facingRight;
	public float jumpPower;
	public Fixture playerSensorFixture;
	Vector2 sensorPos;
	boolean jumping;
	private int jumpCool;
	int health;
	public Animation animation;
	public ArrayList<TextureRegion[]> frames;
	public int currentAction;
	protected TextureRegion[] transframes;
	public boolean unhappy;
	private float w;
	private float h;
	public float size;
	static Filter filter;
	public RevoluteJoint swingjoint;
	public Body rope;
	ArrayList<TextureRegion[]> unhappyFrames;
	int unhappy_index;
	boolean ropeswing;
	Vector2 jumpVector;
	boolean UP;
	boolean DOWN;
	public boolean LEFT;
	public boolean RIGHT;
	boolean SPACE;
	boolean J;
	public boolean dead;
	static final Class<?>[] danger;

	static {
		danger = new Class[] { Bulldozer.class, Robot.class, DangerShape.class };
	}

	public Player(float x, float y, PlayGameState gs) {
		super(gs);
		this.maxvel = this.defaultmax = 6.0f;
		this.accel = this.defaultaccel;
		this.facingRight = true;
		this.jumpPower = 25.0f;
		this.jumping = false;
		this.jumpCool = 20;
		this.size = 1.0f;
		this.ropeswing = false;
		this.jumpVector = new Vector2();
		this.UP = false;
		this.DOWN = false;
		this.LEFT = false;
		this.RIGHT = false;
		this.SPACE = false;
		this.J = false;
		if (filter == null) {
			filter = new Filter();
			Player.filter.categoryBits = 4;
			Player.filter.maskBits = 13;
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
		shape.setAsBox(0.05f, h - 0.01f, new Vector2(-w, 0.11f), 0.0f);
		sdef.shape = shape;
		f = this.body.createFixture(sdef);
		f.setFilterData(filter);
		shape.setAsBox(0.05f, h - 0.01f, new Vector2(w, 0.11f), 0.0f);
		f = this.body.createFixture(sdef);
		f.setFilterData(filter);
		shape.dispose();
		PolygonShape pgon = new PolygonShape();
		this.sensorPos = new Vector2(0.0f, -h);
		pgon.setAsBox(w, h / 4.0f, this.sensorPos, 0.0f);
		this.playerSensorFixture = this.body.createFixture(pgon, 0.0f);
		this.playerSensorFixture.setSensor(true);
		this.playerSensorFixture.setFilterData(filter);
		pgon.dispose();
		this.alpha = 1.0f;
	}

	public void alphaUpdate() {
		if (this.oldanim != null) {
			if (this.oldalpha > 0.0f) {
				this.oldalpha -= 0.02f;
			}
			if (this.oldalpha <= 0.0f) {
				this.oldanim = null;
			}
		}
		if (this.transition != null) {
			if (this.transition.hasPlayedOnce()) {
				this.transition = null;
			} else {
				this.transition.update();
			}
		}
		if (this.alpha < 1.0f && !this.dead) {
			this.alpha += 0.015f;
		}
		if (this.alpha > 1.0f) {
			this.alpha = 1.0f;
		}
	}

	public Player(PlayGameState ps, Player pl) {
		super(ps);
		this.maxvel = this.defaultmax = 6.0f;
		this.accel = this.defaultaccel;
		this.facingRight = true;
		this.jumpPower = 25.0f;
		this.jumping = false;
		this.jumpCool = 20;
		this.size = 1.0f;
		this.ropeswing = false;
		this.jumpVector = new Vector2();
		this.UP = false;
		this.DOWN = false;
		this.LEFT = false;
		this.RIGHT = false;
		this.SPACE = false;
		this.J = false;
		this.animation = new Animation();
		this.oldanim = pl.animation;
		this.LEFT = pl.LEFT;
		this.RIGHT = pl.RIGHT;
		this.UP = pl.UP;
		this.DOWN = pl.DOWN;
		this.jumping = pl.jumping;
		this.facingRight = pl.facingRight;
		this.w = pl.w;
		this.h = pl.h;
		this.swingjoint = pl.swingjoint;
		this.rope = pl.rope;
		this.ropeswing = pl.ropeswing;
		this.UP = pl.UP;
		this.LEFT = pl.LEFT;
		this.RIGHT = pl.RIGHT;
		this.DOWN = pl.DOWN;
		this.SPACE = pl.SPACE;
		this.J = pl.J;
		this.alpha = 0.0f;
		this.oldalpha = 1.0f;
		this.health = pl.health;
		this.body = pl.body;
		Object[] arrobject = new Object[2];
		arrobject[0] = this;
		this.body.setUserData(arrobject);
		this.playerSensorFixture = pl.playerSensorFixture;
	}

	@Override
	public void draw(SpriteBatch g) {
		float w = this.w * 1.25f;
		float ix = this.body.getPosition().x - 1.5f * w;
		float iy = this.body.getPosition().y + this.h * 2.0f * this.h;
		float iw = 3.0f * w;
		float ih = -3.0f * this.h;
		this.draw1(g, ix, iy, ih, iw);
		if (this.unhappy) {
			if (this.unhappyFrames == null) {
				GameStateManager.rm.loadImages(this.unhappy_index);
				this.unhappyFrames = GameStateManager.rm.getImages(this.unhappy_index);
			}
			this.animation.setFrames(this.unhappyFrames.get(this.currentAction), this.animation.getFrame());
		} else {
			this.animation.setFrames(this.frames.get(this.currentAction), this.animation.getFrame());
		}
		this.draw2(g, ix, iy, ih, iw);
	}

	protected void draw1(SpriteBatch g, float ix, float iy, float ih, float iw) {
		if (this.oldanim != null) {
			g.setColor(1.0f, 1.0f, 1.0f, this.oldalpha);
			if (this.facingRight) {
				if (this.oldanim.getImage() != null) {
					g.draw(this.oldanim.getImage(), ix, iy, iw, ih);
				}
			} else if (this.oldanim.getImage() != null) {
				g.draw(this.oldanim.getImage(), ix + iw, iy, -iw, ih);
			}
		}
	}

	protected void draw2(SpriteBatch g, float ix, float iy, float ih, float iw) {
		g.setColor(1.0f, 1.0f, 1.0f, this.alpha);
		if (this.facingRight) {
			if (this.animation.getImage() != null) {
				g.draw(this.animation.getImage(), ix, iy, iw, ih);
			}
		} else if (this.animation.getImage() != null) {
			g.draw(this.animation.getImage(), ix + iw, iy, -iw, ih);
		}
		g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		if (this.transition != null) {
			if (this.facingRight) {
				g.draw(this.transition.getImage(), ix - iw / 2.0f, iy - ih, iw * 2.0f, ih * 2.0f);
			} else {
				g.draw(this.transition.getImage(), ix + 3.0f * iw / 2.0f, iy - ih, (-iw) * 2.0f, ih * 2.0f);
			}
		}
	}

	@Override
	public void update() {
		if (this.impulse == null) {
			this.impulse = new Vector2();
		}
		if (this.isGrounded()) {
			this.body.setLinearDamping(5.0f);
		} else {
			this.body.setLinearDamping(1.0f);
		}
		if (this.rope != null && this.SPACE && this.swingjoint == null) {
			RevoluteJointDef jdef = new RevoluteJointDef();
			jdef.bodyA = this.body;
			jdef.bodyB = this.rope;
			jdef.collideConnected = false;
			jdef.enableLimit = true;
			jdef.lowerAngle = -0.7853982f;
			jdef.upperAngle = 0.7853982f;
			this.swingjoint = (RevoluteJoint) this.gs.getWorld().createJoint(jdef);
			this.ropeswing = true;
		} else if (this.swingjoint != null && !this.SPACE) {
			this.gs.getWorld().destroyJoint(this.swingjoint);
			this.swingjoint = null;
			if (this.body.getLinearVelocity().y > -1.0f) {
				this.impulse.set(0.0f, 3.25f * (this.body.getLinearVelocity().y + 2.0f));
				this.body.applyLinearImpulse(this.impulse, this.body.getPosition(), true);
			}
		}
		this.rope = null;
		if (this.jumping) {
			if (this.jumpCool <= 0) {
				if (this.isGrounded()) {
					this.jumping = false;
				}
			} else {
				--this.jumpCool;
			}
		}
		this.maxvel = this.ropeswing ? this.swingmax : this.defaultmax;
		this.accel = this.swingjoint != null ? this.swingaccel : this.defaultaccel;
		if (this.RIGHT) {
			this.impulse.set(this.accel * this.body.getMass(), 0.0f);
			if (!this.ropeswing || this.body.getLinearVelocity().x >= 0.0f) {
				// empty if block
			}
			this.body.applyLinearImpulse(this.impulse, this.body.getPosition(), true);
		} else if (this.LEFT) {
			this.impulse.set((-this.accel) * this.body.getMass(), 0.0f);
			if (!this.ropeswing || this.body.getLinearVelocity().x <= 0.0f) {
				// empty if block
			}
			this.body.applyLinearImpulse(this.impulse, this.body.getPosition(), true);
		}
		if (this.gs.getWorld() != null) {
			if (this.body.getLinearVelocity().x < -this.maxvel) {
				this.body.setLinearVelocity(-this.maxvel, this.body.getLinearVelocity().y);
			} else if (this.body.getLinearVelocity().x > this.maxvel) {
				this.body.setLinearVelocity(this.maxvel, this.body.getLinearVelocity().y);
			}
		}
		if (this.ropeswing && this.isGrounded()) {
			this.ropeswing = false;
		}
	}

	public boolean isGrounded() {
		Array<Contact> contactList = this.gs.getWorld().getContactList();
		int i = 0;
		while (i < contactList.size) {
			Contact contact = contactList.get(i);
			if (contact.isTouching() && (contact.getFixtureA() == this.playerSensorFixture
					|| contact.getFixtureB() == this.playerSensorFixture)) {
				Object ua = contact.getFixtureA().getBody().getUserData();
				Object ub = contact.getFixtureB().getBody().getUserData();
				ua = ((Object[]) ua)[0];
				ub = ((Object[]) ub)[0];
				if (!(contact.getFixtureA() == this.playerSensorFixture ? !(ub + "").contains("world")
						: !(ua + "").contains("world"))) {
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
		case Keys.W: {
			this.UP = false;
			break;
		}
		case Keys.S: {
			this.DOWN = false;
			break;
		}
		case Keys.A: {
			this.LEFT = false;
			break;
		}
		case Keys.D: {
			this.RIGHT = false;
			break;
		}
		case Keys.SPACE: {
			this.SPACE = false;
			break;
		}
		case Keys.J: {
			this.J = false;
			break;
		}

		}
	}

	public void keyDown(int k) {
		switch (k) {
		case Keys.W: {
			this.UP = true;
			break;
		}
		case Keys.S: {
			this.DOWN = true;
			break;
		}
		case Keys.A: {
			this.LEFT = true;
			if (this.transition != null)
				break;
			this.facingRight = false;
			break;
		}
		case Keys.D: {
			this.RIGHT = true;
			if (this.transition != null)
				break;
			this.facingRight = true;
			break;
		}
		case Keys.J: {
			this.J = true;
			break;
		}
		case Keys.SPACE: {
			this.SPACE = true;
			if (!this.isGrounded())
				break;
			this.jumpVector.set(0.0f, this.jumpPower);
			this.jumping = true;
			this.jumpCool = 20;
			this.body.setLinearVelocity(this.jumpVector);
		}
		}
	}

	public void setScale(float scale) {
		this.h = scale;
		this.w = this.h * 0.8f;
	}

	public float getWidth() {
		return this.w;
	}

	public float getHeight() {
		return this.h;
	}

	@Override
	public void collide(Entity e) {
		if (!(this.gs instanceof PlaybackState)) {
			Class<?>[] dangerousEntityClasses = danger;
			int numCollisions = dangerousEntityClasses.length;
			int i = 0;
			while (i < numCollisions) {
				Class<?> c = dangerousEntityClasses[i];
				if (c.isInstance(e)) {
					this.dead = true;
				}
				++i;
			}
		}
	}
}
