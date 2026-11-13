/*
 * Decompiled with CFR 0.152.
 */
package quesheng.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import quesheng.util.CacheMap;

public final class ImageKit {
    static final Map<String, Image> imageMap = new CacheMap(1000, 1001);

    public static Image rotate(Image image, int rotate) {
        if (rotate < 0) {
            rotate = 360 + rotate;
        }
        switch (rotate) {
            case 0: {
                return image;
            }
            case 90: {
                return ImageKit.rotate90DX(image);
            }
            case 180: {
                return ImageKit.rotate180DX(image);
            }
            case 270: {
                return ImageKit.rotate90SX(image);
            }
        }
        return image;
    }

    public static Image rotate180DX(Image image) {
        String hascode = image.hashCode() + "_180";
        if (imageMap.containsKey(hascode)) {
            return imageMap.get(hascode);
        }
        PixelReader pixelReader = image.getPixelReader();
        WritableImage wImage = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();
        int w = (int)image.getWidth();
        int h = (int)image.getHeight();
        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                Color color = pixelReader.getColor(i, j);
                pixelWriter.setColor(w - i - 1, h - 1 - j, color);
            }
        }
        imageMap.put(hascode, wImage);
        return wImage;
    }

    public static Image rotate90DX(Image image) {
        String hascode = image.hashCode() + "_90";
        if (imageMap.containsKey(hascode)) {
            return imageMap.get(hascode);
        }
        PixelReader pixelReader = image.getPixelReader();
        WritableImage wImage = new WritableImage((int)image.getHeight(), (int)image.getWidth());
        PixelWriter pixelWriter = wImage.getPixelWriter();
        int w = (int)image.getWidth();
        int h = (int)image.getHeight();
        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                Color color = pixelReader.getColor(i, j);
                pixelWriter.setColor(h - 1 - j, i, color);
            }
        }
        imageMap.put(hascode, wImage);
        return wImage;
    }

    public static Image rotate90SX(Image image) {
        String hascode = image.hashCode() + "_-90";
        if (imageMap.containsKey(hascode)) {
            return imageMap.get(hascode);
        }
        PixelReader pixelReader = image.getPixelReader();
        WritableImage wImage = new WritableImage((int)image.getHeight(), (int)image.getWidth());
        PixelWriter pixelWriter = wImage.getPixelWriter();
        int w = (int)image.getWidth();
        int h = (int)image.getHeight();
        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                Color color = pixelReader.getColor(i, j);
                pixelWriter.setColor(j, w - 1 - i, color);
            }
        }
        imageMap.put(hascode, wImage);
        return wImage;
    }

    public static Image coverImage(Image baseWritableImage, Image coverWritableImage) {
        String hascode = baseWritableImage.hashCode() + "_" + coverWritableImage.hashCode();
        if (imageMap.containsKey(hascode)) {
            return imageMap.get(hascode);
        }
        BufferedImage baseBufferedImage = SwingFXUtils.fromFXImage(baseWritableImage, null);
        BufferedImage coverBufferedImage = SwingFXUtils.fromFXImage(coverWritableImage, null);
        Graphics2D g2d = baseBufferedImage.createGraphics();
        g2d.drawImage(coverBufferedImage, 0, 0, baseBufferedImage.getWidth(), baseBufferedImage.getHeight(), null);
        g2d.dispose();
        imageMap.put(hascode, SwingFXUtils.toFXImage(baseBufferedImage, null));
        return imageMap.get(hascode);
    }

    public static Image drawBorder(Image image, Color rbg) {
        int i;
        int j;
        String hascode = image.hashCode() + "_" + rbg.hashCode();
        if (imageMap.containsKey(hascode)) {
            return imageMap.get(hascode);
        }
        PixelReader pixelReader = image.getPixelReader();
        WritableImage wImage = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();
        int w = (int)image.getWidth();
        int h = (int)image.getHeight();
        for (int readY = 0; readY < h; ++readY) {
            for (int readX = 0; readX < w; ++readX) {
                Color color = pixelReader.getColor(readX, readY);
                color = color.brighter();
                pixelWriter.setColor(readX, readY, color);
            }
        }
        for (j = 0; j < 1; ++j) {
            for (i = 0; i < w; ++i) {
                pixelWriter.setColor(i, j, rbg);
            }
        }
        for (j = h - 4; j < h; ++j) {
            for (i = 0; i < w; ++i) {
                pixelWriter.setColor(i, j, rbg);
            }
        }
        for (j = 0; j < 1; ++j) {
            for (i = 0; i < h; ++i) {
                pixelWriter.setColor(j, i, rbg);
            }
        }
        for (j = w - 2; j < w; ++j) {
            for (i = 0; i < h; ++i) {
                pixelWriter.setColor(j, i, rbg);
            }
        }
        imageMap.put(hascode, wImage);
        return wImage;
    }

    public static Image drawBorder(Image image) {
        String hascode = image.hashCode() + "_\u52a0\u8fb9\u6846";
        if (imageMap.containsKey(hascode)) {
            return imageMap.get(hascode);
        }
        Color rbg = Color.RED;
        PixelReader pixelReader = image.getPixelReader();
        WritableImage wImage = new WritableImage((int)image.getWidth(), (int)image.getHeight() + 2);
        PixelWriter pixelWriter = wImage.getPixelWriter();
        int y = 2;
        int w = (int)image.getWidth();
        int h = (int)image.getHeight();
        for (int readY = 0; readY < h; ++readY) {
            int wy = readY + y;
            for (int readX = 0; readX < w; ++readX) {
                Color color = pixelReader.getColor(readX, readY);
                pixelWriter.setColor(readX, wy, color);
            }
        }
        for (int j = 0; j < 2; ++j) {
            int i = 0;
            while ((double)i < image.getWidth()) {
                pixelWriter.setColor(i, j, rbg);
                ++i;
            }
        }
        imageMap.put(hascode, wImage);
        return wImage;
    }

    public static Image drawImage(Image image) {
        String hascode = image.hashCode() + "_\u8fd4\u663e";
        if (imageMap.containsKey(hascode)) {
            return imageMap.get(hascode);
        }
        PixelReader pixelReader = image.getPixelReader();
        WritableImage wImage = new WritableImage((int)image.getWidth(), (int)image.getHeight() + 2);
        PixelWriter pixelWriter = wImage.getPixelWriter();
        int w = (int)image.getWidth();
        int h = (int)image.getHeight();
        for (int readY = 0; readY < h; ++readY) {
            for (int readX = 0; readX < w; ++readX) {
                Color color = pixelReader.getColor(readX, readY);
                color = color.darker();
                pixelWriter.setColor(readX, readY, color);
            }
        }
        imageMap.put(hascode, wImage);
        return wImage;
    }
}

