package excepciones;

import sensores.*;

// TODO: Auto-generated Javadoc
/**
 * Clase Class SensorDescalibrado.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class SensorDescalibrado extends AlertaSensor {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Inicializa un nuevo objeto de la clase sensor descalibrado.
	 *
	 * @param sensor the sensor
	 */
	public SensorDescalibrado(Sensor sensor) {
		super(sensor, "Sensor descalibrado");
	}
}
