package gheat;


public class PointLatLng implements Comparable {


    private double peso;
    private double longitud;
    private double latitud;
    private Object opt_value;

    public Object getValue() {
        return opt_value;
    }

    public void setValue(Object opt_value) {
        this.opt_value = opt_value;
    }

    public PointLatLng(double longitud, double latitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public PointLatLng(double longitud, double latitud, double peso) {
        this.latitud = latitud;
        this.longitud = longitud;

    }

    public PointLatLng(double longitud, double latitud, Object opt_value) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.opt_value = opt_value;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    @Override
    public String toString() {
        return "(" + this.longitud + ", " + this.latitud + ")";
    }

    @Override
    public int compareTo(Object o) {
        PointLatLng tmp = (PointLatLng) o;
        if (this.longitud < tmp.longitud) {
            return -1;
        } else if (this.longitud > tmp.longitud) {
            return 1;
        } else {
            if (this.latitud < tmp.latitud) {
                return -1;
            } else if (this.latitud > tmp.latitud) {
                return 1;
            }
            return 0;
        }

    }
}
