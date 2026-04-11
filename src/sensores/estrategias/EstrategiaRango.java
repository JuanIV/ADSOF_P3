package sensores.estrategias;

/**
 * Clase EstrategiaRango.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class EstrategiaRango implements Estrategia {
	
	/** Probabilidad de fallo en la medicion. */
	private double probabilidadFallo;
	
	/** Valor minimo de la medicion. */
	private double min;
	
	/** Valor maximo de la medicion. */
	private double max;
	
	/**
	 * Inicializa un nuevo objeto de la clase estrategia rango.
	 *
	 * @param prob Probabilidad de fallo
	 * @param min Cota inferior
	 * @param max Cota superior
	 */
	public EstrategiaRango(double prob, double min, double max) {
		this.probabilidadFallo = prob;
		this.max = max;
		this.min = min;
	}
	
	/**
	 * Getter de probabilidad fallo.
	 *
	 * @return probabilidad fallo
	 */
	public double getProbabilidadFallo() {
		return probabilidadFallo;
	}

	/**
	 * Setter de probabilidad fallo.
	 *
	 * @param probabilidadFallo nuevo probabilidad fallo
	 */
	public void setProbabilidadFallo(double probabilidadFallo) {
		this.probabilidadFallo = probabilidadFallo;
	}

	/**
	 * Getter de min.
	 *
	 * @return min
	 */
	public double getMin() {
		return min;
	}

	/**
	 * Getter de max.
	 *
	 * @return max
	 */
	public double getMax() {
		return max;
	}

	/**
	 * Simula una medicion tomando un número aleatorio entre min y max, con probabilidad de fallar
	 *
	 * @return the double
	 */
	@Override
	public double simularLectura() {
		if((Math.random() * 100) > probabilidadFallo)
			return (Math.random() * (max - min) + min);
		else
			return (Math.random() * (max - min) + max);
	}
}
