package sensores.estrategias;

// TODO: Auto-generated Javadoc
/**
 * Clase Class EstrategiaRango.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class EstrategiaRango implements Estrategia {
	
	/** The probabilidad fallo. */
	private double probabilidadFallo;
	
	/** The min. */
	private double min;
	
	/** The max. */
	private double max;
	
	/**
	 * Inicializa un nuevo objeto de la clase estrategia rango.
	 *
	 * @param prob the prob
	 * @param min the min
	 * @param max the max
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
	 * Simular lectura.
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
