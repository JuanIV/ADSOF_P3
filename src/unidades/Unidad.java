package unidades;

// TODO: Auto-generated Javadoc
/**
 * Clase Interface Unidad.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public interface Unidad {
    
    /**
     * Getter de min.
     *
     * @return min
     */
    double getMin();
    
    /**
     * Getter de max.
     *
     * @return max
     */
    double getMax();
    
    /**
     * Getter de simbolo.
     *
     * @return simbolo
     */
    String getSimbolo();
    
    /**
     * In range.
     *
     * @param value the value
     * @return true, if successful
     */
    boolean inRange(double value);
}
