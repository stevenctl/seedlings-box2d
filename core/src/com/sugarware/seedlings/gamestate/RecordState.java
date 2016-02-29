/*
 * Decompiled with CFR 0_110.
 */
package com.sugarware.seedlings.gamestate;

import com.greenpipestudios.seedpeople.Script;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Set;

public abstract class RecordState
extends PlayGameState {
    long ticks = 0;
    HashMap<Long, Integer> keyUp = new HashMap();
    HashMap<Long, Integer> keyDown = new HashMap();

    public RecordState(GameStateManager gsm, String map_path) {
        super(gsm, map_path);
    }

    @Override
    public void init() {
        super.init();
        this.ticks = 0;
    }

    @Override
    public void update() {
        super.update();
        ++this.ticks;
    }

    @Override
    protected void keyPressed(int k) {
        super.keyPressed(k);
        this.keyDown.put(this.ticks, k);
    }

    @Override
    protected void keyReleased(int k) {
        this.keyUp.put(this.ticks, k);
    }

    @Override
    public void dispose() {
        super.dispose();
        try {
            this.write();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void write() throws IOException {
        FileOutputStream saveFile = new FileOutputStream("script.sav");
        System.out.println("writing");
        ObjectOutputStream save = new ObjectOutputStream(saveFile);
        for (Long l : this.keyDown.keySet()) {
            System.out.println(l + " " + this.keyDown.get(l));
        }
        Script script = new Script(this.keyUp, this.keyDown);
        save.writeObject(script);
        save.close();
        System.out.println("done writing");
    }
}

