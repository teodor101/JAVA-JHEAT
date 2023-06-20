package gheat.datasources;

import gheat.DataPoint;
import gheat.PointLatLng;
import gheat.Proyecciones;

public interface HeatMapDataSource {

    PointLatLng[] GetList(DataPoint tlb, DataPoint lrb, int zoom, Proyecciones _projection);
}
