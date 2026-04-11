package sensores.estrategias;

/**
 * Clase EstrategiaMedia.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class EstrategiaMedia implements Estrategia {
	
	/** Cuenta del total de mediciones hechas */
	private int count = 0;
	
	/** Media de valores medidos */
	private double media = 0;
	
	/** Rango alrededor de la medicion media sobre el que estará la próxima medicion */
	private double rango;
	
	/**
	 * Inicializa un nuevo objeto de la clase estrategia media.
	 *
	 * @param rango Rango alrededor de la medicion media sobre el que estará la próxima medicion
	 * @param valInicial Valor con el que comienza la estrategia
	 */
	public EstrategiaMedia(double rango, double valInicial) {
		this.rango = rango;
		this.media = valInicial;
	}

	/**
	 * Getter de rango.
	 *
	 * @return rango
	 */
	public double getRango() {
		return rango;
	}

	/**
	 * Setter de rango.
	 *
	 * @param rango nuevo rango
	 */
	public void setRango(double rango) {
		this.rango = rango;
	}

	/**
	 * Simula una lectura tomando un valor entre media-rango y media+rango
	 *
	 * @return double con la medicion obtenida
	 */
	@Override
	public double simularLectura() {
		double max = media + Math.abs(media)*rango;
		double min = media - Math.abs(media)*rango;
		double medicion = (Math.random() * (max - min) + min);
		media = ((media * count) + medicion)/(count + 1);
		count++;
		return medicion;
	}

}
