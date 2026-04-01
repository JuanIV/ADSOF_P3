package sensores.unidades;

public interface Unidad {
    double getMin();
    double getMax();
    String getSimbolo();
    boolean inRange(double value);
}
