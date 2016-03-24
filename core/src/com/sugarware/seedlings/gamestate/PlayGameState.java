package com.sugarware.seedlings.gamestate;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
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

public abstract class PlayGameState extends GameState {
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
	protected ArrayList<Entity> entities;
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
		backlayer = arrn;
		frontlayer = new int[] { 2, 3 };
		scored = 0;
		drawGUI = true;
		tilemap = new TmxMapLoader().load(mapPath);
		MapProperties prop = tilemap.getProperties();
		setEntities(new ArrayList<Entity>());
		ppt = (Integer) prop.get("tilewidth", Integer.class);
		w = (Integer) prop.get("width", Integer.class) * ppt;
		h = (Integer) prop.get("height", Integer.class) * ppt;
		fbo = new FrameBuffer(Gdx.app.getType() == ApplicationType.Desktop ? Format.RGBA4444 : Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		world = new World(new Vector2(0.0f, -31.6f), false);
		ch = new ContactHandler();
		world.setContactListener(ch);
		rh = new RayHandler(world);
		bg = new TextureRegion(new Texture(Gdx.files.internal("bg2.jpg")));
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
		chchcool = 40;
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
		initialized = false;
		initcount = 154;
		if (world != null) {
			world.dispose();
		}
		world = null;
		for (Entity e : entities) {
			e.destroy();
		}
		RayHandler.useDiffuseLight(true);
		world = new World(new Vector2(0.0f, -31.6f), false);
		world.setContactListener(ch);
		ss.theta = 0.0f;
		ss.sel = 0;
		if (p != null) {
			p.destroy();
		}
		entities.clear();
		MapLoader.load(tilemap, this);
	}

	@Override
	public void update() {
		if (p.transition != null || p.dead) {
			return;
		}
		world.step(Gdx.graphics.getDeltaTime(), 30, 30);
		rh.setWorld(world);
		p.update();
		cam.position.set(p.body.getPosition().x, p.body.getPosition().y, 0.0f);
		if (cam.position.x - cam.viewportWidth / 2.0f < 0.0f) {
			cam.position.x = cam.viewportWidth / 2.0f;
		} else if (cam.position.x + cam.viewportWidth / 2.0f > (float) (w / ppt)) {
			cam.position.x = (float) (w / ppt) - cam.viewportWidth / 2.0f;
		}
		if (cam.position.y - cam.viewportHeight / 2.0f < 0.0f) {
			cam.position.y = cam.viewportHeight / 2.0f;
		} else if (cam.position.y + cam.viewportHeight / 2.0f > (float) (h / ppt)) {
			cam.position.y = (float) (h / ppt) - cam.viewportHeight / 2.0f;
		}
		cam.update();
		Hint.nhints = 0;
		int i = 0;
		while (i < entities.size()) {
			Entity e = entities.get(i);
			e.update();
			if (e.shouldDestroy()) {
				if (e instanceof Fireball) {
					entities.add(new Explosion(this, e.body, ((Fireball) e).light));
				}
				e.destroy();
				entities.remove(i);
				--i;
			}
			++i;
		}
	}

	void alphaUpdate() {
		if (ss.cd == 0 && !ss.scrolling && ss.list.size() > 0 && ss.list.size() > 0) {
			Player op = p;
			if (ss.list.get(ss.sel).getTexture() == GameStateManager.rm.icons.get(0).getTexture()
					&& !(p instanceof VanillaCharacter)) {
				p = new VanillaCharacter(this, p);
			}
			if (ss.list.get(ss.sel).getTexture() == GameStateManager.rm.icons.get(1).getTexture()
					&& !(p instanceof FlowerCharacter)) {
				p = new FlowerCharacter(this, p);
			}
			if (ss.list.get(ss.sel).getTexture() == GameStateManager.rm.icons.get(2).getTexture()
					&& !(p instanceof FireCharacter)) {
				p = new FireCharacter(this, p);
			}
			if (p != op) {
				op.destroy();
			}
			ss.cd = -1;
		}
		if (!p.dead) {
			p.alphaUpdate();
		} else {
			p.alpha = (float) ((double) p.alpha - 0.025);
			if (p.alpha <= 0.0f) {
				gsm.currentState.init();
			}
		}
	}

	@Override
	protected void keyPressed(int k) {
		if (k == Keys.F2) {
			saveScreenshot();
		}
	}

	@Override
	public void draw() {

		if (g == null) {
			g = new SpriteBatch();
		}
		g.setProjectionMatrix(cam.combined);
		if (maprenderer == null) {
			maprenderer = new OrthogonalTiledMapRenderer(tilemap, 1.0f / (float) ppt, g);
		}
		if (s == null && debugCollisions) {
			s = new Box2DDebugRenderer();
		} else if (!debugCollisions && s != null) {
			s.dispose();
			s = null;
		}
		maprenderer.setView(cam);
		fbo.begin();
		g.begin();
		g.draw(bg, 0.0f, 0.0f, (float) (w / ppt), (float) (h / ppt));
		g.end();
		maprenderer.render(backlayer);
		if (debugCollisions) {
			s.render(getWorld(), cam.combined);
		}
		g.begin();
		for (Entity e22 : getEntities()) {
			if (e22 instanceof Water || e22 instanceof Hint)
				continue;
			e22.draw(g);
		}
		p.draw(g);
		g.end();
		fbo.end();
		g.setProjectionMatrix(idt);
		g.begin();
		g.draw((Texture) fbo.getColorBufferTexture(), -1.0f, 1.0f, 2.0f, -2.0f);
		g.end();
		g.setProjectionMatrix(cam.combined);
		g.begin();
		for (Entity e22 : getEntities()) {
			if (!(e22 instanceof Water))
				continue;
			e22.draw(g);
		}
		g.end();
		maprenderer.render(frontlayer);
		rh.setCombinedMatrix(cam);
		rh.updateAndRender();
		g.begin();
		if (ss.list.size() > 2) {
			ss.draw(g);
		}
		for (Entity e22 : getEntities()) {
			if (!(e22 instanceof Hint))
				continue;
			e22.draw(g);
		}

		g.end();
		scrCam.viewportHeight = Gdx.graphics.getHeight();
		scrCam.viewportWidth = Gdx.graphics.getWidth();
		scrCam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
		scrCam.update();
		g.setProjectionMatrix(scrCam.combined);
		g.begin();
		if (score > 0 && !(this instanceof PlaybackState)) {
			g.draw(LeafCoin.img, 2, scrCam.viewportHeight - 50, 48, 48);
			if (scoreFont == null) {
				FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Monopol.ttf"));
				FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
				parameter.size = 48;
				parameter.color = Color.WHITE;
				scoreFont = generator.generateFont(parameter);
			}
			scoreFont.draw(g, "" + score, 2, scrCam.viewportHeight, 0, ("" + score).length(), 48, Align.center, false,
					"...");
		}
		g.end();
		g.setProjectionMatrix(cam.combined);

	}

	public World getWorld() {
		return world;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}

	@Override
	public void dispose() {
		for (Entity e : entities) {
			e.destroy();
		}
		world.dispose();
		rh.dispose();
		if (g != null) {
			g.dispose();
		}
		fbo.dispose();
		if (s != null)
			s.dispose();
	}

	public static void saveScreenshot() {
		try {
			FileHandle fh;
			while ((fh = new FileHandle(
					String.valueOf(Gdx.files.getLocalStoragePath()) + "screenshot" + counter++ + ".png")).exists()) {
			}
			Pixmap pixmap = PlayGameState.getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
			PixmapIO.writePNG(fh, pixmap);
			pixmap.dispose();
		} catch (Exception e) {
			e.printStackTrace();
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

	public void score() {
		score++;
		scored++;
	}
	
	public void resize(int w, int h){
		fbo.dispose();
		fbo = new FrameBuffer(Gdx.app.getType() == ApplicationType.Desktop ? Format.RGBA4444 : Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		System.out.println("FBO: " + fbo.getWidth() + "x" + fbo.getHeight());
	}
}
