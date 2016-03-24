
package com.sugarware.seedlings;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ResourceManager {
    float volAdj;
    Music music;
    ArrayList<ArrayList<Sound>> soundpacks;
    ArrayList<ArrayList<String>> sndPaths;
    ArrayList<String> sngPaths;
    public ArrayList<TextureRegion> icons;
    protected static final int GENERAL = 0;
    protected static final int WHOOSH1 = 0;
    protected static final int EXPL1 = 1;
    protected static final int CRICKETS = 2;
    protected static final int BULLDOZER_LOOP = 3;
    protected static final int TREE_FALL = 4;
    protected static final int CRUNCH = 5;
    protected static final int WATER = 6;
    protected static final int MEADOW = 0;
    protected static final int INTENSE = 1;
    protected static final int BOSS1 = 2;
    protected static final int FUNKYFOREST = 3;
    protected static final int MEADOW2 = 4;
    protected static final int PERIWINKLE = 5;
    private ArrayList<ArrayList<TextureRegion[]>> images;
    private ArrayList<String> imgpaths;
    private ArrayList<int[]> frameCounts;
    private ArrayList<int[]> dimensions;
    protected static final int VANILLA_SEED = 0;
    protected static final int FLOWER_SEED = 1;
    protected static final int PLATFORM_1 = 2;
    protected static final int TESTSEED = 3;
    protected static final int ROBOT = 4;
    protected static final int FIRE_SEED = 5;
    protected static final int FIREBALL = 6;
    protected static final int EXPLOSION_1 = 7;
    protected static final int EXPLOSION_2 = 8;
    protected static final int FIRETRANSITION = 9;
    protected static final int ICE_SEED = 10;
    protected static final int ICE_PLAT = 11;
    protected static final int VANILLA_TRANSITION = 12;
    protected static final int BULLDOZER = 13;
    protected static final int HUT = 14;
    protected static final int VANILLA_UNHAPPY = 15;
    protected static final int FLOWER_UNHAPPY = 16;
    protected static final int ROCKBLOCK = 17;
    protected static final int LEAF2 = 19;
    protected static final int LONEGUY = 18;
    public TextureRegion loadbg;
    public TextureRegion water_top;
    public TextureRegion water;
    TextureRegion btnUp;
    TextureRegion btnAction;
    TextureRegion btnPause;
    TextureRegion btnLight;
    public TextureRegion[] healthIcon;

    public ResourceManager() {
        System.out.println("Resource Manager Initialized");
        this.loadbg = new TextureRegion(new Texture(Gdx.files.internal("loadbg.jpg")));
        this.soundpacks = new ArrayList<ArrayList<Sound>>();
        this.sndPaths = new ArrayList<ArrayList<String>>();
        this.sngPaths = new ArrayList<String>();
        this.soundpacks.add(null);
        ArrayList<String> sp = new ArrayList<String>();
        sp.add("sounds/whoosh.wav");
        sp.add("sounds/explosion.wav");
        sp.add("sounds/crickets.mp3");
        sp.add("sounds/tractor.mp3");
        sp.add("sounds/tree_falling.wav");
        sp.add("sounds/crunch.ogg");
        this.sndPaths.add(sp);
        this.sngPaths.add("songs/lost_in_meadows.mp3");
        this.sngPaths.add("songs/intense.mp3");
        this.sngPaths.add("songs/lastmin.mp3");
        this.sngPaths.add("songs/funkymeadow.mp3");
        this.sngPaths.add("songs/meadow2.ogg");
        this.sngPaths.add("songs/periwinkle.mp3");
        this.images = new ArrayList<ArrayList<TextureRegion[]>>();
        this.imgpaths = new ArrayList<String>();
        this.frameCounts = new ArrayList<int[]>();
        this.dimensions = new ArrayList<int[]>();
        this.frameCounts.add(new int[]{1, 16, 5, 1, 2});
        this.dimensions.add(new int[]{64, 64});
        this.imgpaths.add("sprites/vanilla_seed.png");
        this.images.add(null);
        this.frameCounts.add(new int[]{1, 16, 5, 1, 4,2});
        this.dimensions.add(new int[]{64, 64});
        this.imgpaths.add("sprites/flower_seed.png");
        this.images.add(null);
        this.frameCounts.add(new int[]{1});
        this.imgpaths.add("sprites/platform.png");
        this.dimensions.add(new int[]{48, 24});
        this.images.add(null);
        this.frameCounts.add(new int[]{1});
        this.imgpaths.add("testseed.png");
        this.dimensions.add(new int[]{64, 64});
        this.images.add(null);
        this.frameCounts.add(new int[]{1, 16, 1});
        this.imgpaths.add("sprites/robot.png");
        this.dimensions.add(new int[]{64, 64});
        this.images.add(null);
        this.frameCounts.add(new int[]{10, 16, 4, 10, 10, 10, 10, 16});
        this.imgpaths.add("sprites/fire_seed.png");
        this.dimensions.add(new int[]{64, 64});
        this.images.add(null);
        this.frameCounts.add(new int[]{10});
        this.imgpaths.add("sprites/fireball.png");
        this.dimensions.add(new int[]{32, 32});
        this.images.add(null);
        this.frameCounts.add(new int[]{7});
        this.imgpaths.add("sprites/exp1.png");
        this.dimensions.add(new int[]{64, 64});
        this.images.add(null);
        this.frameCounts.add(new int[]{7});
        this.imgpaths.add("sprites/exp2.png");
        this.dimensions.add(new int[]{32, 32});
        this.images.add(null);
        this.frameCounts.add(new int[]{38});
        this.imgpaths.add("sprites/fire_trans.png");
        this.dimensions.add(new int[]{128, 128});
        this.images.add(null);
        this.frameCounts.add(new int[]{1, 16, 5, 1});
        this.dimensions.add(new int[]{64, 64});
        this.imgpaths.add("sprites/snow_seed.png");
        this.images.add(null);
        this.frameCounts.add(new int[]{1});
        this.imgpaths.add("sprites/ice_plat.png");
        this.dimensions.add(new int[]{192, 96});
        this.images.add(null);
        this.frameCounts.add(new int[]{64});
        this.imgpaths.add("sprites/vanilla_trans.png");
        this.dimensions.add(new int[]{128, 128});
        this.images.add(null);
        this.frameCounts.add(new int[]{4});
        this.imgpaths.add("sprites/bulldozer.png");
        this.dimensions.add(new int[]{256, 256});
        this.images.add(null);
        this.frameCounts.add(new int[]{6});
        this.imgpaths.add("sprites/hut.png");
        this.dimensions.add(new int[]{256, 256});
        this.images.add(null);
        this.frameCounts.add(new int[]{1, 16, 5, 1, 2});
        this.dimensions.add(new int[]{64, 64});
        this.imgpaths.add("sprites/vanilla_un.png");
        this.images.add(null);
        this.frameCounts.add(new int[]{1, 16, 5, 1, 4});
        this.dimensions.add(new int[]{64, 64});
        this.imgpaths.add("sprites/flower_un.png");
        this.images.add(null);
        this.frameCounts.add(new int[]{1});
        this.dimensions.add(new int[]{85, 85});
        this.imgpaths.add("sprites/rockBlock.png");
        this.images.add(null);
        this.frameCounts.add(new int[]{1, 16, 5, 1, 2});
        this.dimensions.add(new int[]{64, 64});
        this.imgpaths.add("lsprites/sillou.png");
        this.images.add(null);
        this.frameCounts.add(new int[]{1});
        this.dimensions.add(new int[]{32, 32});
        this.imgpaths.add("sprites/leaf2.png");
        this.images.add(null);
    }

    public void loadSoundPack(int index) {
        if (this.soundpacks.get(index) == null) {
            ArrayList<Sound> pack = new ArrayList<Sound>();
            int i = 0;
            while (i < this.sndPaths.get(index).size()) {
                Sound snd = Gdx.audio.newSound(Gdx.files.internal(this.sndPaths.get(index).get(i)));
                System.out.println(this.sndPaths.get(index).get(i));
                pack.add(snd);
                ++i;
            }
            this.soundpacks.set(index, pack);
        }else{
        	System.out.println("pack already loaded");
        }
    }

    public void unloadSoundPack(int index) {
        try {
            this.soundpacks.set(index, null);
        }
        catch (Exception var2_2) {
            // empty catch block
        }
    }

    public void playSound(int packindex, int soundindex) {
        this.soundpacks.get(packindex).get(soundindex).play();
    }

    public void playSound(int packindex, int soundindex, float volume) {
        this.soundpacks.get(packindex).get(soundindex).play(volume);
    }

    public Sound getSound(int packindex, int soundindex) {
        return this.soundpacks.get(packindex).get(soundindex);
    }

    public void playSong(int index) {
        this.stopSong();
        this.music = Gdx.audio.newMusic(Gdx.files.internal(this.sngPaths.get(index)));
        this.music.setLooping(true);
        this.music.play();
    }

    public void stopSong() {
        if (this.music != null) {
            this.music.stop();
            this.music.dispose();
        }
    }

    public Music getMusic() {
        return this.music;
    }

    public void loadLongImage(int index) {
        try {
            if (this.images.get(index) != null) {
                return;
            }
            System.out.println("Loading " + this.imgpaths.get(index));
        }
        catch (Exception e) {
            return;
        }
        int imwidth = this.dimensions.get(index)[0];
        int imheight = this.dimensions.get(index)[1];
        System.out.println(String.valueOf(imwidth) + "x" + imheight);
        int numFrames = this.frameCounts.get(index)[0];
        ArrayList<TextureRegion[]> sprites = new ArrayList<TextureRegion[]>();
        try {
            Texture spritesheet = new Texture(Gdx.files.internal(this.imgpaths.get(index)));
            TextureRegion[][] sheet = TextureRegion.split(spritesheet, imwidth, imheight);
            TextureRegion[] sprite = new TextureRegion[numFrames];
            int w = spritesheet.getWidth() / imwidth;
            int h = spritesheet.getHeight() / imheight;
            System.out.println(String.valueOf(w) + "<> " + h);
            int c = 0;
            int j = 0;
            while (j < h) {
                int i = 0;
                while (i < w) {
                    if (c >= numFrames) break;
                    sheet[j][i].flip(false, true);
                    sprite[c] = sheet[j][i];
                    ++c;
                    ++i;
                }
                if (c >= numFrames) break;
                ++j;
            }
            sprites.add(sprite);
            this.images.set(index, sprites);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImages(int index) {
        try {
            if (this.images.get(index) != null) {
                return;
            }
            System.out.println("Loading " + this.imgpaths.get(index));
        }
        catch (NullPointerException e) {
            return;
        }
        int imwidth = this.dimensions.get(index)[0];
        int imheight = this.dimensions.get(index)[1];
        System.out.println(String.valueOf(imwidth) + "x" + imheight);
        int[] numFrames = this.frameCounts.get(index);
        int numSprites = numFrames.length;
        ArrayList<TextureRegion[]> sprites = new ArrayList<TextureRegion[]>();
        try {
            Texture spritesheet = new Texture(Gdx.files.internal(this.imgpaths.get(index)));
            TextureRegion[][] sheet = TextureRegion.split(spritesheet, imwidth, imheight);
            int i = 0;
            while (i < numSprites) {
                TextureRegion[] sprite = new TextureRegion[this.frameCounts.get(index)[i]];
                int j = 0;
                while (j < this.frameCounts.get(index)[i]) {
                    sheet[i][j].flip(false, true);
                    sprite[j] = sheet[i][j];
                    ++j;
                }
                sprites.add(sprite);
                ++i;
            }
            this.images.set(index, sprites);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unloadImages(int index) {
        this.images.set(index, null);
    }

    public ArrayList<TextureRegion[]> getImages(int index) {
        return this.images.get(index);
    }

    public void loadWaterImages() {
        if (this.water_top == null) {
            this.water_top = new TextureRegion(new Texture(Gdx.files.internal("tilemaps/tilesets/water_top.png")));
            this.water = new TextureRegion(new Texture(Gdx.files.internal("tilemaps/tilesets/water.png")));
            System.out.println("Loaded tilemaps/tilesets/water.png");
            System.out.println("Loaded tilemaps/tilesets/water_top.png");
        }
    }

    public void loadGuiImages() {
        if (this.btnUp == null) {
            this.btnUp = new TextureRegion(new Texture(Gdx.files.internal("gui/up.png")));
            this.btnAction = new TextureRegion(new Texture(Gdx.files.internal("gui/action.png")));
            this.btnPause = new TextureRegion(new Texture(Gdx.files.internal("gui/pause.png")));
            this.btnLight = new TextureRegion(new Texture(Gdx.files.internal("gui/lit.png")));
            this.healthIcon = new TextureRegion[]{new TextureRegion(new Texture(Gdx.files.internal("gui/health.png"))).split(32, 32)[0][0], new TextureRegion(new Texture(Gdx.files.internal("gui/health.png"))).split(32, 32)[0][1]};
            System.out.println("Loaded gui/up.png");
            System.out.println("gui/action.png");
            System.out.println("Loaded gui/pause.png");
            System.out.println("Loaded gui/lit.png");
        }
    }

    public void loadAltHearts() {
        this.healthIcon = new TextureRegion[]{new TextureRegion(new Texture(Gdx.files.internal("lsprites/h2.png"))).split(170, 170)[0][0], new TextureRegion(new Texture(Gdx.files.internal("lsprites/h2.png"))).split(170, 170)[0][1]};
    }

    public void loadIcons() {
        if (this.icons == null) {
            this.icons = new ArrayList<TextureRegion>();
        } else {
            this.icons.clear();
        }
        this.icons.add(new TextureRegion(new Texture(Gdx.files.internal("icons/v.png"))));
        this.icons.add(new TextureRegion(new Texture(Gdx.files.internal("icons/f.png"))));
        this.icons.add(new TextureRegion(new Texture(Gdx.files.internal("icons/fi.png"))));
        this.icons.add(new TextureRegion(new Texture(Gdx.files.internal("icons/i.png"))));
    }

    public ArrayList<Sound> getSoundPack(int index) {
        return this.soundpacks.get(index);
    }
}

