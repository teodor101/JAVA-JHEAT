package gheat;


import gheat.datasources.DataManager;
import gheat.graphics.ThemeManager;

import java.awt.image.BufferedImage;

public class HeatMap {


    public static boolean esIniciado = false;


    private HeatMap() {

    }

    public static final int SIZE = 256;
    public static final int MAX_ZOOM = 31;

    public static BufferedImage GetTile(DataManager dataManager,
                                        String colorScheme,
                                        int zoom,
                                        int x,
                                        int y,
                                        boolean changeOpacityWithZoom,
                                        int defaultOpacity) throws Exception {

        if (colorScheme == "") throw new Exception("Se necesita un esquema de color");
        if (dataManager == null) throw new Exception("No hay datamanager");

        return Tile.Generate(ThemeManager.GetColorScheme(colorScheme),
                ThemeManager.GetDot(zoom),
                zoom,
                x,
                y,
                dataManager.GetPointsForTile(x, y, ThemeManager.GetDot(zoom), zoom), changeOpacityWithZoom, defaultOpacity);

    }

    public static BufferedImage GetTile(DataManager dataManager, String colorScheme, int zoom, int x, int y) throws Exception {

        return GetTile(dataManager, colorScheme, zoom, x, y, false, 100);
    }


}