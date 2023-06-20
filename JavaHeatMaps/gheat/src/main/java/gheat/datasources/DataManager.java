package gheat.datasources;


import gheat.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class DataManager {

    private Proyecciones _projection = new MarcadorProyecciones();
    private HeatMapDataSource dataSource;

    public DataManager(HeatMapDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataPoint[] GetPointsForTile(int x, int y, BufferedImage dot, int zoom) throws InterruptedException {
        List<DataPoint> puntos = new ArrayList<DataPoint>();
        tamano maxTileTamano = new tamano(HeatMap.SIZE, HeatMap.SIZE);
        DataPoint adjustedDataPoint;
        DataPoint pixelCoord;


        DataPoint tlb = _projection.fromTileXYToPixel(new DataPoint(x, y));

        DataPoint lrb = new DataPoint((tlb.getX() + maxTileTamano.getAncho()) + dot.getWidth(), (tlb.getY() + maxTileTamano.getAlto()) + dot.getWidth());


        tlb = new DataPoint(tlb.getX() - dot.getWidth(), tlb.getY() - dot.getHeight());

        PointLatLng[] TilePoints = dataSource.GetList(tlb, lrb, zoom, _projection);

        //Convierte los puntos en coordenadas

        for (PointLatLng llDataPoint : TilePoints) {

            pixelCoord = _projection.fromLatLngToPixel(llDataPoint.getLatitud(), llDataPoint.getLongitud(), zoom);


            pixelCoord.setPeso(llDataPoint.getPeso());

            adjustedDataPoint = AjustarPixelesAlTile(new DataPoint(x, y), pixelCoord);

            adjustedDataPoint.setPeso(pixelCoord.getPeso());

            puntos.add(adjustedDataPoint);
        }

        return puntos.toArray(new DataPoint[puntos.size()]);
    }

    private static DataPoint AjustarPixelesAlTile(DataPoint tileXYPoint, DataPoint mapPixelPoint) {
        return new DataPoint(mapPixelPoint.getX() - (tileXYPoint.getX() * HeatMap.SIZE), mapPixelPoint.getY() - (tileXYPoint.getY() * HeatMap.SIZE));
    }


}

