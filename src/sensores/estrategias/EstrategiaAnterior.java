package sensores.estrategias;

/**
 * Clase EstrategiaAnterior.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class EstrategiaAnterior implements Estrategia{
	
	/** Rango alrededor de la medicion anterior sobre el que estará la próxima */
	private double rango;
	
	/** Medición anterior */
	private double anterior;
	
	/**
	 * Inicializa un nuevo objeto de la clase estrategia anterior.
	 *
	 * @param rango Rango alrededor de la medicion anterior sobre el que estará la próxima
	 * @param valInicial Valor incial con el que comienza la estrategia
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
	 * Simula una lectura tomando un valor entre anterior-rango y anterior+rango
	 *
	 * @return double con la medición obtenida
	 */
	@Override
	public double simularLectura() {
		double max = anterior + Math.abs(anterior)*rango/100;
		double min = anterior - Math.abs(anterior)*rango/100;
		double medicion = (Math.random() * (max - min) + min);
		anterior = medicion;
		return medicion;
	}
}
