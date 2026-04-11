package excepciones;

import sensores.*;

// TODO: Auto-generated Javadoc
/**
 * Clase Class AlertaSensor.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class AlertaSensor extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The sensor. */
	private Sensor sensor;
	
	/**
	 * Inicializa un nuevo objeto de la clase alerta sensor.
	 *
	 * @param sensor the sensor
	 * @param message the message
	 */
	public AlertaSensor(Sensor sensor, String message) {
		super(message);
		this.sensor = sensor;
	}
	
	/**
	 * Getter de message.
	 *
	 * @return message
	 */
	@Override
	public String getMessage() {
		return "Sensor " + this.sensor.getId() + ": " + super.getMessage();
	}

	/**
	 * Getter de sensor.
	 *
	 * @return sensor
	 */
	public Sensor getSensor() {
		return sensor;
	}
}
