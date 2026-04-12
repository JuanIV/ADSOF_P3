package unidades;

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
     * @return min Valor minimo del rango de la unidad
     */
    double getMin();
    
    /**
     * Getter de max.
     *
     * @return max Valor maximo del rango de la unidad
     */
    double getMax();
    
    /**
     * Getter de simbolo.
     *
     * @return simbolo Símbolo de escritura de la unidad
     */
    String getSimbolo();
    
    /**
     * Comprueba si una medicion está dentro del rango de la unidad
     *
     * @param value Valor a comprobar
     * @return true, si está en rango, false si no
     */
    boolean inRange(double value);
}
