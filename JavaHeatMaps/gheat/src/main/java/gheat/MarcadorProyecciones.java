package gheat;

public class MarcadorProyecciones implements Proyecciones {


    static final double MinLatitud = -85.05112878;
    static final double MaxLatitud = 85.05112878;
    static final double MinLongitud = -180;
    static final double MaxLongitud = 180;

    private static double clip(double n, double minValue, double maxValue) {
        return Math.min(Math.max(n, minValue), maxValue);
    }

    public tamano getTileMatrixSizePixel(int zoom) {
        tamano s = getTileMatrixSizeXY(zoom);
        return new tamano(s.getAncho() * HeatMap.SIZE, s.getAlto() * HeatMap.SIZE);
    }

    public tamano getTileMatrixSizeXY(int zoom) {
        tamano sMin = getTileMatrixMinXY(zoom);
        tamano sMax = getTileMatrixMaxXY(zoom);
        return new tamano(sMax.getAncho() - sMin.getAncho() + 1, sMax.getAlto() - sMin.getAlto() + 1);
    }

    @Override
    public tamano getTileMatrixMaxXY(int zoom) {
        long xy = (1 << zoom);
        return new tamano(xy - 1, xy - 1);
    }

    @Override
    public tamano getTileMatrixMinXY(int zoom) {
        return new tamano(0, 0);
    }

    @Override
    public DataPoint fromLatLngToPixel(PointLatLng centro, int zoom) {
        DataPoint ret = new DataPoint(0, 0);

        centro.setLatitud(clip(centro.getLatitud(), MinLatitud, MaxLatitud));
        centro.setLongitud(clip(centro.getLongitud(), MinLongitud, MaxLongitud));

        double x = (centro.getLongitud() + 180) / 360;
        double sinLatitude = Math.sin(centro.getLatitud() * Math.PI / 180);
        double y = 0.5 - Math.log((1 + sinLatitude) / (1 - sinLatitude)) / (4 * Math.PI);

        tamano s = getTileMatrixSizePixel(zoom);
        long mapSizeX = (long) s.getAncho();
        long mapSizeY = (long) s.getAlto();

        ret.setX((long) clip(x * mapSizeX + 0.5, 0, mapSizeX - 1));
        ret.setY((long) clip(y * mapSizeY + 0.5, 0, mapSizeY - 1));

        return ret;
    }

    @Override
    public DataPoint fromLatLngToPixel(double latitud, double longitud, int zoom) {
        DataPoint ret = new DataPoint(0, 0);

        latitud = clip(latitud, MinLatitud, MaxLatitud);
        longitud = clip(longitud, MinLongitud, MaxLongitud);

        double x = (longitud + 180) / 360;
        double sinLatitude = Math.sin(latitud * Math.PI / 180);
        double y = 0.5 - Math.log((1 + sinLatitude) / (1 - sinLatitude)) / (4 * Math.PI);

        tamano s = getTileMatrixSizePixel(zoom);
        long mapSizeX = (long) s.getAncho();
        long mapSizeY = (long) s.getAlto();

        ret.setX((long) clip(x * mapSizeX + 0.5, 0, mapSizeX - 1));
        ret.setY((long) clip(y * mapSizeY + 0.5, 0, mapSizeY - 1));

        return ret;
    }

    @Override
    public PointLatLng fromPixelToLatLng(DataPoint tlb, int zoom) {
        PointLatLng ret = new PointLatLng(0, 0, 0);

        tamano s = getTileMatrixSizePixel(zoom);
        double mapSizeX = s.getAncho();
        double mapSizeY = s.getAlto();

        double xx = (clip(tlb.getX(), 0, mapSizeX - 1) / mapSizeX) - 0.5;
        double yy = 0.5 - (clip(tlb.getY(), 0, mapSizeY - 1) / mapSizeY);

        ret.setLatitud(90 - 360 * Math.atan(Math.exp(-yy * 2 * Math.PI)) / Math.PI);
        ret.setLongitud(360 * xx);

        return ret;
    }

    @Override
    public DataPoint fromPixelToTileXY(DataPoint pixelCoord) {
        return new DataPoint((long) (pixelCoord.getX() / HeatMap.SIZE), (long) (pixelCoord.getY() / HeatMap.SIZE));
    }

    @Override
    public DataPoint fromTileXYToPixel(DataPoint dataPoint) {
        return new DataPoint((dataPoint.getX() * HeatMap.SIZE), (dataPoint.getY() * HeatMap.SIZE));
    }
}

