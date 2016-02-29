/*
 * Decompiled with CFR 0_110.
 */
package com.sugarware.seedlings.desktop;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.Display;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sugarware.seedlings.GdxGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
    
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = GdxGame.WIDTH;
        config.height = GdxGame.HEIGHT;
        config.addIcon("icon16.png", FileType.Internal);
        config.addIcon("icon32.png", FileType.Internal);
        config.addIcon("icon128.png", FileType.Internal);
        if (arg.length > 0) {
            GdxGame.test = arg[0].equals("test");
        }
        new com.badlogic.gdx.backends.lwjgl.LwjglApplication((ApplicationListener)new GdxGame(), config);
    }

    public static ByteBuffer extractBytes (String ImageName) throws IOException {
    	 // open image
    	 File imgPath = new File(ImageName);
    	 BufferedImage bufferedImage = ImageIO.read(imgPath);

    	 // get DataBufferBytes from Raster
    	 WritableRaster raster = bufferedImage .getRaster();
    	 DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

    	 return ( ByteBuffer.wrap(data.getData()) );
    	}
}

