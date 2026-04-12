package unidades;

/**
 * Enum UnidadPresion.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public enum UnidadPresion implements Unidad {
	
	/** hectopascal. */
	HECTOPASCAL(300, 1100, " hPa"),
	
	/** pascal. */
	PASCAL(30000, 110000, " Pa"),
	
	/** milibar. */
	MILIBAR(300, 1100, " mbar");
	
	/** Valor minimo del rango. */
	private double min;
	
	/** Valor maximo del rango. */
	private double max;
	
	/** Símbolo de escritura de la unidad. */
	private String simbolo;
	
	/**
	 * Inicializa un nuevo objeto de la clase unidad presion.
	 *
	 * @param min Minimo del rango permitido para la unidad
	 * @param max Maximo del rango permitido para la unidad
	 * @param simbolo Símbolo de escritura de la unidad
	 */
	private UnidadPresion(double min, double max, String simbolo) {
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
