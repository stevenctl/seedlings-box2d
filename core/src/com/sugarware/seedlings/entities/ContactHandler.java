/*
 * Decompiled with CFR 0_110.
 */
package com.sugarware.seedlings.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.sugarware.seedlings.entities.Entity;
import com.sugarware.seedlings.entities.Player;
import com.sugarware.seedlings.entities.Rope;

public class ContactHandler
implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();
        Object da = ((Object[])a.getUserData())[0];
        Object db = ((Object[])b.getUserData())[0];
        if (da instanceof Entity && db instanceof Entity) {
            ((Entity)da).collide((Entity)db);
            ((Entity)db).collide((Entity)da);
        }
        if (da instanceof Player && db instanceof Rope || db instanceof Player && da instanceof Rope) {
            Rope r = (Rope)(da instanceof Rope ? da : db);
            Player p = (Player)(da instanceof Player ? da : db);
            Object[] rd = (Object[])(da instanceof Rope ? a.getUserData() : b.getUserData());
            if (((Boolean)rd[1]).booleanValue() && p.rope == null) {
                p.rope = da == r ? a : b;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();
        Object da = ((Object[])a.getUserData())[0];
        Object db = ((Object[])b.getUserData())[0];
        if (da instanceof Player && db instanceof Player) {
            contact.setEnabled(false);
        }
        if (da.equals("worldf") || db.equals("worldf")) {
            Body ob = da.equals("worldf") ? b : a;
            Body plat = da.equals("worldf") ? a : b;
            float radius = Float.MAX_VALUE;
            if (((Object[])ob.getUserData())[0] instanceof Player) {
                radius = 1.0f;
            }
            if (ob.getPosition().y - ((Float)((Object[])plat.getUserData())[1]).floatValue() < radius) {
                contact.setEnabled(false);
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}

