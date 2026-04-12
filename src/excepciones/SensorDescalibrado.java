package excepciones;

import sensores.*;

/**
 * Clase SensorDescalibrado.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class SensorDescalibrado extends AlertaSensor {
	
	/** Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Inicializa un nuevo objeto de la clase sensor descalibrado.
	 *
	 * @param sensor Sensor que se descalibró
	 */
	public SensorDescalibrado(Sensor<?> sensor) {
		super(sensor, "Sensor descalibrado");
	}
}
