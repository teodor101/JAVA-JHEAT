package gheat;


public interface Proyecciones {

    DataPoint fromLatLngToPixel(PointLatLng center, int zoom);

    tamano getTileMatrixMinXY(int zoom);

    tamano getTileMatrixMaxXY(int zoom);

    DataPoint fromLatLngToPixel(double latitud, double longitud, int zoom);

    PointLatLng fromPixelToLatLng(DataPoint tlb, int zoom);

    DataPoint fromPixelToTileXY(DataPoint pixelCoord);

    DataPoint fromTileXYToPixel(DataPoint dataPoint);
}
