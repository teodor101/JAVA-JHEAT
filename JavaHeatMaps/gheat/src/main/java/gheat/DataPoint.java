package gheat;


public class DataPoint {

    private double x;
    private double y;
    private double peso;

    public DataPoint(double x, double y, double peso) {
        this.x = x;
        this.y = y;
        this.peso = peso;
    }

    public DataPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


}