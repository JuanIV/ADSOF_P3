package sensores.estrategias;

/**
 * Interface Estrategia.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public interface Estrategia {
	
	/**
	 * Simular lectura.
	 *
	 * @return double con la medicion obtenida
	 */
	public double simularLectura();
}
