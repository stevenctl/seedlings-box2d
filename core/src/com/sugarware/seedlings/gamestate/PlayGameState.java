/*
 * Decompiled with CFR 0_110.
 */
package com.sugarware.seedlings.gamestate;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sugarware.seedlings.ScrollSelector;
import com.sugarware.seedlings.entities.ContactHandler;
import com.sugarware.seedlings.entities.Entity;
import com.sugarware.seedlings.entities.Explosion;
import com.sugarware.seedlings.entities.FireCharacter;
import com.sugarware.seedlings.entities.Fireball;
import com.sugarware.seedlings.entities.FlowerCharacter;
import com.sugarware.seedlings.entities.Hint;
import com.sugarware.seedlings.entities.LeafCoin;
import com.sugarware.seedlings.entities.Player;
import com.sugarware.seedlings.entities.VanillaCharacter;
import com.sugarware.seedlings.entities.Water;

import box2dLight.RayHandler;

public abstract class PlayGameState
extends GameState {
    Box2DDebugRenderer s;
    public FrameBuffer fbo;
    Matrix4 idt = new Matrix4();
    int chchcool;
    int ppt;
    boolean debugCollisions;
    int[] backlayer;
    int[] frontlayer;
    public static int score;
    public Player p;
    private int scored;
    private ArrayList<Entity> entities;
    public ScrollSelector ss;
    final float gravity = -31.6f;
    World world;
    public TiledMap tilemap;
    OrthogonalTiledMapRenderer maprenderer;
    TextureRegion bg;
    boolean initialized;
    public RayHandler rh;
    ContactHandler ch;
    int initcount;
    public boolean drawGUI;
    private static int counter = 1;
    BitmapFont scoreFont;
    OrthographicCamera scrCam;
    public PlayGameState(GameStateManager gsm, String mapPath) {
        super(gsm);
        int[] arrn = new int[2];
        arrn[1] = 1;
        this.backlayer = arrn;
        this.frontlayer = new int[]{2, 3};
        scored = 0;
        this.drawGUI = true;
        this.tilemap = new TmxMapLoader().load(mapPath);
        MapProperties prop = this.tilemap.getProperties();
        this.setEntities(new ArrayList<Entity>());
        this.ppt = (Integer)prop.get("tilewidth", Integer.class);
        this.w = (Integer)prop.get("width", Integer.class) * this.ppt;
        this.h = (Integer)prop.get("height", Integer.class) * this.ppt;
        this.fbo = new FrameBuffer(Pixmap.Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        this.world = new World(new Vector2(0.0f, -31.6f), false);
        this.ch = new ContactHandler();
        this.world.setContactListener(this.ch);
        this.rh = new RayHandler(this.world);
        this.bg = new TextureRegion(new Texture(Gdx.files.internal("bg2.jpg")));
        this.cam.position.set(this.cam.viewportWidth / 2.0f, this.cam.viewportHeight / 2.0f, 0.0f);
        this.cam.update();
        this.chchcool = 40;
        ShaderProgram.pedantic = false;
        GameStateManager.rm.loadIcons();
        scrCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scrCam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        scrCam.update();
    }

    @Override
    protected void init() {
        score -= scored;
        scored = 0;
        this.initialized = false;
        this.initcount = 154;
        if (this.world != null) {
            this.world.dispose();
        }
        this.world = null;
        for (Entity e : this.entities) {
            e.destroy();
        }
        RayHandler.useDiffuseLight(true);
        this.world = new World(new Vector2(0.0f, -31.6f), false);
        this.world.setContactListener(this.ch);
        this.ss.theta = 0.0f;
        this.ss.sel = 0;
        if (this.p != null) {
            this.p.destroy();
        }
        this.entities.clear();
        MapLoader.load(this.tilemap, this);
    }

    @Override
    public void update() {
        if (this.p.transition != null || this.p.dead) {
            return;
        }
        this.world.step(Gdx.graphics.getDeltaTime(), 30, 30);
        this.rh.setWorld(this.world);
        this.p.update();
        this.cam.position.set(this.p.body.getPosition().x, this.p.body.getPosition().y, 0.0f);
        if (this.cam.position.x - this.cam.viewportWidth / 2.0f < 0.0f) {
            this.cam.position.x = this.cam.viewportWidth / 2.0f;
        } else if (this.cam.position.x + this.cam.viewportWidth / 2.0f > (float)(this.w / this.ppt)) {
            this.cam.position.x = (float)(this.w / this.ppt) - this.cam.viewportWidth / 2.0f;
        }
        if (this.cam.position.y - this.cam.viewportHeight / 2.0f < 0.0f) {
            this.cam.position.y = this.cam.viewportHeight / 2.0f;
        } else if (this.cam.position.y + this.cam.viewportHeight / 2.0f > (float)(this.h / this.ppt)) {
            this.cam.position.y = (float)(this.h / this.ppt) - this.cam.viewportHeight / 2.0f;
        }
        this.cam.update();
        Hint.nhints = 0;
        int i = 0;
        while (i < this.entities.size()) {
            Entity e = this.entities.get(i);
            e.update();
            if (e.shouldDestroy()) {
                if (e instanceof Fireball) {
                    this.entities.add(new Explosion(this, e.body, ((Fireball)e).light));
                }
                e.destroy();
                this.entities.remove(i);
                --i;
            }
            ++i;
        }
    }

    void alphaUpdate() {
        if (this.ss.cd == 0 && !this.ss.scrolling && this.ss.list.size() > 0 && this.ss.list.size() > 0) {
            Player op = this.p;
            if (this.ss.list.get(this.ss.sel).getTexture() == GameStateManager.rm.icons.get(0).getTexture() && !(this.p instanceof VanillaCharacter)) {
                this.p = new VanillaCharacter(this, this.p);
            }
            if (this.ss.list.get(this.ss.sel).getTexture() == GameStateManager.rm.icons.get(1).getTexture() && !(this.p instanceof FlowerCharacter)) {
                this.p = new FlowerCharacter(this, this.p);
            }
            if (this.ss.list.get(this.ss.sel).getTexture() == GameStateManager.rm.icons.get(2).getTexture() && !(this.p instanceof FireCharacter)) {
                this.p = new FireCharacter(this, this.p);
            }
            if (this.p != op) {
                op.destroy();
            }
            this.ss.cd = -1;
        }
        if (!this.p.dead) {
            this.p.alphaUpdate();
        } else {
            this.p.alpha = (float)((double)this.p.alpha - 0.025);
            if (this.p.alpha <= 0.0f) {
                this.gsm.currentState.init();
            }
        }
    }

    @Override
    protected void keyPressed(int k) {
        if (k == 70) {
            boolean bl = this.debugCollisions = !this.debugCollisions;
        }
        if (k == 245) {
            PlayGameState.saveScreenshot();
        }
    }

    @Override
    public void draw() {
    	 
    	
        if (this.g == null) {
            this.g = new SpriteBatch();
        }
        this.g.setProjectionMatrix(this.cam.combined);
        if (this.maprenderer == null) {
            this.maprenderer = new OrthogonalTiledMapRenderer(this.tilemap, 1.0f / (float)this.ppt, this.g);
        }
        if (this.s == null && this.debugCollisions) {
            this.s = new Box2DDebugRenderer();
        } else if (!this.debugCollisions && this.s != null) {
            this.s.dispose();
            this.s = null;
        }
        this.maprenderer.setView(this.cam);
        this.fbo.begin();
        this.g.begin();
        this.g.draw(this.bg, 0.0f, 0.0f, (float)(this.w / this.ppt), (float)(this.h / this.ppt));
        this.g.end();
        this.maprenderer.render(this.backlayer);
        if (this.debugCollisions) {
            this.s.render(this.getWorld(), this.cam.combined);
        }
        this.g.begin();
        for (Entity e22 : this.getEntities()) {
            if (e22 instanceof Water || e22 instanceof Hint) continue;
            e22.draw(this.g);
        }
        this.p.draw(this.g);
        this.g.end();
        this.fbo.end();
        this.g.setProjectionMatrix(this.idt);
        this.g.begin();
        this.g.draw((Texture)this.fbo.getColorBufferTexture(), -1.0f, 1.0f, 2.0f, -2.0f);
        this.g.end();
        this.g.setProjectionMatrix(this.cam.combined);
        this.g.begin();
        for (Entity e22 : this.getEntities()) {
            if (!(e22 instanceof Water)) continue;
            e22.draw(this.g);
        }
        this.g.end();
        this.maprenderer.render(this.frontlayer);
        this.rh.setCombinedMatrix(this.cam);
        this.rh.updateAndRender();
        this.g.begin();
        if (this.ss.list.size() > 2) {
            this.ss.draw(this.g);
        }
        for (Entity e22 : this.getEntities()) {
            if (!(e22 instanceof Hint)) continue;
            e22.draw(this.g);
        }
        
        this.g.end();
        scrCam.viewportHeight = Gdx.graphics.getHeight();
   	 scrCam.viewportWidth = Gdx.graphics.getWidth();
   	 scrCam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        scrCam.update();
        g.setProjectionMatrix(scrCam.combined);
        g.begin();
        if(score > 0 && !(this instanceof PlaybackState)){
        	g.draw(LeafCoin.img, 2, 
        						 scrCam.viewportHeight  - 50,
        						 48,48);
        	if(scoreFont == null){
        		   FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Monopol.ttf"));
                   FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
                   parameter.size = 48;
                   parameter.color = Color.WHITE;
                   scoreFont = generator.generateFont(parameter);
        	}
        	scoreFont.draw(g, "" + score,  2, 
					 scrCam.viewportHeight,0, ("" + score).length(), 48, Align.center, false, "...");
        }
        g.end();
        g.setProjectionMatrix(cam.combined);
        
    }

    public World getWorld() {
        return this.world;
    }

    public ArrayList<Entity> getEntities() {
        return this.entities;
    }

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    @Override
    public void dispose() {
        for (Entity e : this.entities) {
            e.destroy();
        }
        this.world.dispose();
        this.rh.dispose();
        if (this.g != null) {
            this.g.dispose();
        }
    }

    public static void saveScreenshot() {
        try {
            FileHandle fh;
            while ((fh = new FileHandle(String.valueOf(Gdx.files.getLocalStoragePath()) + "screenshot" + counter++ + ".png")).exists()) {
            }
            Pixmap pixmap = PlayGameState.getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
            PixmapIO.writePNG(fh, pixmap);
            pixmap.dispose();
        }
        catch (Exception fh) {
            // empty catch block
        }
    }

    private static Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown) {
        Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);
        if (yDown) {
            ByteBuffer pixels = pixmap.getPixels();
            int numBytes = w * h * 4;
            byte[] lines = new byte[numBytes];
            int numBytesPerLine = w * 4;
            int i = 0;
            while (i < h) {
                pixels.position((h - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
                ++i;
            }
            pixels.clear();
            pixels.put(lines);
        }
        return pixmap;
    }
    
    public void score(){
    	score++;
    	scored++;
    }
}

