package unidades;

/**
 * Enum UnidadTemperatura.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public enum UnidadTemperatura implements Unidad {
	
	/** Grados celsius. */
	CELSIUS(-273.15, 1000, "ºC"),
	
	/** Grados fahrenheit. */
	FAHRENHEIT(-459.67, 1832, "ºF"),
	
	/** Kelvin. */
	KELVIN(0, 1273.15, " K");
	
	/** Valor minimo del rango. */
	private double min;
	
	/** Valor maximo del rango. */
	private double max;
	
	/** Símbolo de escritura de la unidad. */
	private String simbolo;
	
	/**
	 * Inicializa un nuevo objeto de la clase unidad temperatura.
	 *
	 * @param min Minimo del rango permitido para la unidad
	 * @param max Maximo del rango permitido para la unidad
	 * @param simbolo Símbolo de escritura de la unidad
	 */
	private UnidadTemperatura(double min, double max, String simbolo) {
		this.min = min;
		this.max = max;
		this.simbolo = simbolo;
	}
	
	/**
	 * Getter de min.
	 *
	 * @return min Minimo del rango
	 */
	public double getMin() {
		return min;
	}
	
	/**
	 * Getter de max.
	 *
	 * @return max Maximo del rango
	 */
	public double getMax() {
		return max;
	}
	
	/**
	 * Getter de simbolo.
	 *
	 * @return simbolo Simbolo de la unidad
	 */
	public String getSimbolo() {
		return simbolo;
	}
	
	/**
	 * Comprueba si una medicion está dentro del rango de la unidad
     *
     * @param value Valor a comprobar
     * @return true, si está en rango, false si no
	 */
	public boolean inRange(double value) {
		return (value >= min)&&(value <= max);
	}
}
