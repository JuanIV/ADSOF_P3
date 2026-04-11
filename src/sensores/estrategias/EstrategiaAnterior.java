package sensores.estrategias;

// TODO: Auto-generated Javadoc
/**
 * Clase Class EstrategiaAnterior.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class EstrategiaAnterior implements Estrategia{
	
	/** The rango. */
	private double rango;
	
	/** The anterior. */
	private double anterior;
	
	/**
	 * Inicializa un nuevo objeto de la clase estrategia anterior.
	 *
	 * @param rango the rango
	 * @param valInicial the val inicial
	 */
	public EstrategiaAnterior(double rango, double valInicial) {
		this.rango = rango;
		this.anterior = valInicial;
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
		double max = anterior + Math.abs(anterior)*rango;
		double min = anterior - Math.abs(anterior)*rango;
		double medicion = (Math.random() * (max - min) + min);
		anterior = medicion;
		return medicion;
	}
}
