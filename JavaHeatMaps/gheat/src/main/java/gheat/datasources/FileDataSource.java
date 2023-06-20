package gheat.datasources;


import gheat.DataPoint;
import gheat.PointLatLng;
import gheat.Proyecciones;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileDataSource implements HeatMapDataSource {

    static List<PointLatLng> _pointList = null;


    public FileDataSource(String filePath, int longitudIndex, int latitudIndex, int pesoIndex) {

        _pointList = new ArrayList<PointLatLng>();
        cargaPuntosDesdeDocumento(filePath, longitudIndex, latitudIndex, pesoIndex);
    }

    private void cargaPuntosDesdeDocumento(String source, int longitudIndex, int latitudIndex, int pesoIndex) {
        String[] item;
        String[] lines = readAllTextFileLines(source);
        for (String line : lines) {
            item = line.split(",");
            _pointList.add(new PointLatLng(Double.parseDouble(item[longitudIndex]), Double.parseDouble(item[latitudIndex]), Double.parseDouble(item[latitudIndex])));
        }
    }

    private static String[] readAllTextFileLines(String fileName) {
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

    @Override
    public PointLatLng[] GetList(DataPoint tlb, DataPoint lrb, int zoom, Proyecciones _projection) {

        List<PointLatLng> llList = null;

        PointLatLng ptlb;
        PointLatLng plrb;

        ptlb = _projection.fromPixelToLatLng(tlb, zoom);
        plrb = _projection.fromPixelToLatLng(lrb, zoom);
        System.out.println(ptlb + ", " + plrb);

        //Encuentra todos los puntos

        llList = new ArrayList<PointLatLng>();
        for (PointLatLng point : _pointList) {
            if (point.getLatitud() <= ptlb.getLatitud() &&
                    point.getLongitud() >= ptlb.getLongitud()
                    && point.getLatitud() >= plrb.getLatitud() &&
                    point.getLongitud() <= plrb.getLongitud()) {
                llList.add(point);
            }


        }

        return llList.toArray(new PointLatLng[llList.size()]);
    }


}