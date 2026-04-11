package unidades;

// TODO: Auto-generated Javadoc
/**
 * Clase Enum UnidadTemperatura.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public enum UnidadTemperatura implements Unidad {
	
	/** The celsius. */
	CELSIUS(-273.15, 1000, "ºC"),
	
	/** The fahrenheit. */
	FAHRENHEIT(-459.67, 1832, "ºF"),
	
	/** The kelvin. */
	KELVIN(0, 1273.15, " K");
	
	/** The min. */
	private double min;
	
	/** The max. */
	private double max;
	
	/** The simbolo. */
	private String simbolo;
	
	/**
	 * Inicializa un nuevo objeto de la clase unidad temperatura.
	 *
	 * @param min the min
	 * @param max the max
	 * @param simbolo the simbolo
	 */
	private UnidadTemperatura(double min, double max, String simbolo) {
		this.min = min;
		this.max = max;
		this.simbolo = simbolo;
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
	 * Getter de simbolo.
	 *
	 * @return simbolo
	 */
	public String getSimbolo() {
		return simbolo;
	}
	
	/**
	 * In range.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	public boolean inRange(double value) {
		return (value >= min)&&(value <= max);
	}
}
