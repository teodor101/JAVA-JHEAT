package gheat;


import java.util.ArrayList;
import java.util.List;

public class Opacidad {


    //Alpha = no transparente

    public static final int OPAQUE = 255;

    public static final int TRANSPARENT = 0;


    public final int MAX_ZOOM = 31;
    public final int ZOOM_OPAQUE = -15;
    public final int ZOOM_TRANSPARENT = 15;
    public final int DEFAULT_OPACITY = 50;

    private int _zoomOpaque;
    private int _zoomTransparent;

    public Opacidad(int zoomOpaque, int zoomTransparent) {
        _zoomOpaque = zoomOpaque;
        _zoomTransparent = zoomTransparent;
    }


     //Valores por defecto si no se dan valores

    public Opacidad() {
        _zoomOpaque = ZOOM_OPAQUE;
        _zoomTransparent = ZOOM_TRANSPARENT;
    }

    /*
     devuelve index=zoom y el valor es la opacidad
   */

    public int[] BuildZoomMapping() {
        List<Integer> zoomMapping = new ArrayList<Integer>();
        int numberOfOpacitySteps;
        float opacityStep;

        numberOfOpacitySteps = _zoomTransparent - _zoomOpaque;

        if (numberOfOpacitySteps < 1)
        {
            for (int i = 0; i <= MAX_ZOOM; i++)
                zoomMapping.add(0);
        } else
        {
            opacityStep = ((float) OPAQUE / (float) numberOfOpacitySteps);
            for (int zoom = 0; zoom <= MAX_ZOOM; zoom++) {
                if (zoom <= _zoomOpaque)
                    zoomMapping.add(OPAQUE);
                else if (zoom >= _zoomTransparent)
                    zoomMapping.add(TRANSPARENT);
                else
                    zoomMapping.add((int) ((float) OPAQUE - (((float) zoom - (float) _zoomOpaque) * opacityStep)));
            }
        }
        return toIntArray(zoomMapping);
    }


    int[] toIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        int i = 0;
        for (Integer e : list)
            ret[i++] = e.intValue();
        return ret;
    }
}
