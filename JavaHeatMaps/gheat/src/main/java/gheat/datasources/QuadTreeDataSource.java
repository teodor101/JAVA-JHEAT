package gheat.datasources;

import gheat.DataPoint;
import gheat.PointLatLng;
import gheat.Proyecciones;
import gheat.datasources.QuadTree.QuadTree;

import java.io.BufferedReader;
import java.io.FileReader;

public class QuadTreeDataSource implements HeatMapDataSource {

    static QuadTree qt = new QuadTree(-180.000000, -90.000000, 180.000000, 90.000000);

    //el constructor coge latitud, longitus y peso del csv.


    public QuadTreeDataSource(String filePath, int longitudIndex, int latitudIndex, int pesoIndex) {
        cargaPuntosArchivo(filePath, longitudIndex, latitudIndex, pesoIndex);
    }


    @Override
    public PointLatLng[] GetList(DataPoint tlb, DataPoint lrb, int zoom, Proyecciones _projection) {

        PointLatLng ptlb = _projection.fromPixelToLatLng(lrb, zoom);
        PointLatLng plrb = _projection.fromPixelToLatLng(tlb, zoom);
        PointLatLng[] list = qt.searchIntersect(plrb.getLongitud(),
                ptlb.getLatitud(),
                ptlb.getLongitud(),
                plrb.getLatitud());
        return list;
    }


    private void cargaPuntosArchivo(String source, int longitudIndex, int latitudIndex, int pesoIndex) {
        String[] item;
        String[] lines = leerTodasLineas(source);
        for (String line : lines) {
            item = line.split(",");
            qt.set(Double.parseDouble(item[longitudIndex]), Double.parseDouble(item[latitudIndex]), Double.parseDouble(item[latitudIndex]));
        }
    }

    private static String[] leerTodasLineas(String fileName) {
        StringBuilder sb = new StringBuilder();

        try {
            String textLine;

            BufferedReader br = new BufferedReader(new FileReader(fileName));

            while ((textLine = br.readLine()) != null) {
                sb.append(textLine);
                sb.append('\n');
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (sb.length() == 0)
                sb.append("\n");
        }
        return sb.toString().split("\n");
    }


}
