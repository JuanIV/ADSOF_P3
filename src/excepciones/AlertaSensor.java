package excepciones;

import sensores.*;

/**
 * Clase AlertaSensor.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class AlertaSensor extends Exception {
	
	/** Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** Sensor que activó la alerta. */
	private Sensor<?> sensor;
	
	/**
	 * Inicializa un nuevo objeto de la clase alerta sensor.
	 *
	 * @param sensor Sensor que activó la alerta
	 * @param message Mensaje de la alerta 
	 */
	public AlertaSensor(Sensor<?> sensor, String message) {
		super(message);
		this.sensor = sensor;
	}
	
	/**
	 * Getter del mensaje.
	 *
	 * @return message Mensaje de la alerta
	 */
	@Override
	public String getMessage() {
		return "Sensor " + this.sensor.getId() + ": " + super.getMessage();
	}

	/**
	 * Getter de sensor.
	 *
	 * @return sensor Sensor que activó la alerta
	 */
	public Sensor<?> getSensor() {
		return sensor;
	}
	
	/**
	 * Método toString
	 */
	@Override
	public String toString() {
		return this.getMessage();
	}
}
