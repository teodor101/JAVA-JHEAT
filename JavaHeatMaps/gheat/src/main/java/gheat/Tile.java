package gheat;


import gheat.graphics.BlendComposite;
import gheat.graphics.GammaCorrection;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    private static int[] _zoomOpacity = new Opacidad().BuildZoomMapping();

    private Tile() {

    }

    public static BufferedImage Generate(BufferedImage colorScheme,
                                         BufferedImage dot,
                                         int zoom,
                                         int tileX,
                                         int tileY,
                                         DataPoint[] points,
                                         boolean changeOpacityWithZoom,
                                         int defaultOpacity) throws Exception {
        int expandirAncho;
        int expandirAlto;

        int x1;
        int x2;
        int y1;
        int y2;

        if (defaultOpacity < Opacidad.TRANSPARENT || defaultOpacity > Opacidad.OPAQUE)
            throw new Exception("La opacidad '" + defaultOpacity + "' no está '" + Opacidad.TRANSPARENT + "' y '" + Opacidad.OPAQUE + "'");

        //Sirve para convertir en coordenadas

        x1 = tileX * HeatMap.SIZE;
        x2 = x1 + 255;
        y1 = tileY * HeatMap.SIZE;
        y2 = y1 + 255;


        int extraPad = dot.getWidth() * 2;


        x1 = x1 - extraPad;
        x2 = x2 + extraPad;
        y1 = y1 - extraPad;
        y2 = y2 + extraPad;
        expandirAncho = x2 - x1;
        expandirAlto = y2 - y1;

        BufferedImage tile;
        if (points.length == 0) {
            if (changeOpacityWithZoom)
                tile = GetEmptyTile(colorScheme, _zoomOpacity[zoom]);
            else
                tile = GetEmptyTile(colorScheme, defaultOpacity);

        } else {
            tile = GetBlankImage(expandirAlto, expandirAncho);
            tile = AddPoints(tile, dot, points);
            tile = Trim(tile, dot);
            if (changeOpacityWithZoom)
                tile = Colorize(tile, colorScheme, _zoomOpacity[zoom]);
            else
                tile = Colorize(tile, colorScheme, defaultOpacity);
        }


        return tile;
    }


    // Aplica el color scheme
    public static BufferedImage Colorize(BufferedImage tile, BufferedImage colorScheme, int zoomOpacity) {
        Color tilePixelColor;
        Color colorSchemePixel;

        for (int x = 0; x < tile.getWidth(); x++) {
            for (int y = 0; y < tile.getHeight(); y++) {

                //Consigue color

                tilePixelColor = new Color(tile.getRGB(x, y));



                colorSchemePixel = new Color(colorScheme.getRGB(0, tilePixelColor.getRed()));

                zoomOpacity = (int) (
                        (
                                ((double) zoomOpacity / 255.0f)
                                        *
                                        ((double) colorSchemePixel.getAlpha() / 255.0f)
                        ) * 255f
                );

                tile.setRGB(x, y, new Color(colorSchemePixel.getRed(), colorSchemePixel.getGreen(), colorSchemePixel.getBlue(), zoomOpacity).getRGB());
            }
        }
        return tile;
    }


    public static BufferedImage Trim(BufferedImage tile, BufferedImage dot) {
        BufferedImage croppedTile = new BufferedImage(HeatMap.SIZE, HeatMap.SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics g = croppedTile.createGraphics();
        int adjPad = dot.getWidth() + (dot.getWidth() / 2);

        g.drawImage(
                tile,
                0, 0, HeatMap.SIZE, HeatMap.SIZE,
                adjPad,
                adjPad,
                HeatMap.SIZE + adjPad,
                HeatMap.SIZE + adjPad,
                null
        );
        g.dispose();
        return croppedTile;
    }


    public static BufferedImage AddPoints(BufferedImage tile, BufferedImage dot, DataPoint[] points) {

        Graphics2D g = tile.createGraphics();
        g.setComposite(BlendComposite.Multiply);

        for (int i = 0; i < points.length; i++) {
            BufferedImage src = points[i].getPeso() != 0 ? ApplyWeightToImage(dot, points[i].getPeso()) : dot;
            g.drawImage(convert(src, BufferedImage.TYPE_INT_RGB),
                    (int) (points[i].getX() + dot.getWidth()),
                    (int) (points[i].getY() + dot.getWidth()),
                    null);

        }

        g.dispose();


        return tile;
    }

    public static BufferedImage convert(BufferedImage src, int bufImgType) {
        BufferedImage img = new BufferedImage(src.getWidth(), src.getHeight(), bufImgType);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(src, 0, 0, null);
        g2d.dispose();
        return img;
    }


    private static BufferedImage ApplyWeightToImage(BufferedImage image, double weight) {
        Graphics2D graphic;
        double tempWeight;
        BufferedImage tempImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        graphic = tempImage.createGraphics();


        tempWeight = weight;

        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
        graphic.setComposite(composite);

        graphic.drawImage(
                image, // Source Image
                0, 0, image.getWidth(), image.getHeight(),
                0,
                0,
                image.getWidth(), image.getHeight(),
                null
        );


        return GammaCorrection.gammaCorrection(tempImage, (tempWeight == 0 ? .1f : (tempWeight * 5)));

    }


    public static BufferedImage GetBlankImage(int height, int width) {
        BufferedImage newImage;
        Graphics2D g;

        newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = newImage.createGraphics();

        //Fondo blanco para que los puntos aparezcan

        g.setBackground(Color.WHITE);
        g.fillRect(0, 0, height, width);
        g.dispose();
        return newImage;
    }


    public static BufferedImage GetEmptyTile(BufferedImage colorScheme, int zoomOpacity) {
        Color colorSchemePixelColor;
        BufferedImage tile;
        Graphics2D graphic;




        if (Cache.hasEmptyTile(colorScheme.hashCode(), zoomOpacity))
            return Cache.getEmptyTile(colorScheme.hashCode(), zoomOpacity);


        tile = new BufferedImage(HeatMap.SIZE, HeatMap.SIZE, BufferedImage.TYPE_INT_ARGB);

        graphic = tile.createGraphics();



        colorSchemePixelColor = new Color(colorScheme.getRGB(0, colorScheme.getHeight() - 1));

        zoomOpacity = (int) ((
                (zoomOpacity / 255.0f)  * (colorSchemePixelColor.getAlpha() / 255.0f)) * 255.0f);

        graphic.setColor(new Color(colorSchemePixelColor.getRed(), colorSchemePixelColor.getGreen(), colorSchemePixelColor.getBlue(), zoomOpacity));

        graphic.fillRect(0, 0, HeatMap.SIZE, HeatMap.SIZE);
        graphic.dispose();



        if (!Cache.hasEmptyTile(colorScheme.hashCode(), zoomOpacity))
            Cache.putEmptyTile(tile, colorScheme.hashCode(), zoomOpacity);


        return tile;
    }

}
