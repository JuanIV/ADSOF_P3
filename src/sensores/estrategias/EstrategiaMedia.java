package sensores.estrategias;

// TODO: Auto-generated Javadoc
/**
 * Clase Class EstrategiaMedia.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class EstrategiaMedia implements Estrategia {
	
	/** The count. */
	private int count = 0;
	
	/** The media. */
	private double media = 0;
	
	/** The rango. */
	private double rango;
	
	/**
	 * Inicializa un nuevo objeto de la clase estrategia media.
	 *
	 * @param rango the rango
	 * @param valInicial the val inicial
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
	 * Simular lectura.
	 *
	 * @return the double
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
