package excepciones;

import sensores.*;

/**
 * Clase DuplicatedSensorIdException.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class DuplicatedSensorIdException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** sensor 1. */
	private Sensor<?> sens1;
	
	/** sensor 2. */
	private Sensor<?> sens2;

	/**
	 * Inicializa un nuevo objeto de la clase duplicated sensor id exception.
	 *
	 * @param message the message
	 * @param sens1 the sens 1
	 * @param sens2 the sens 2
	 */
	public DuplicatedSensorIdException(String message, Sensor<?> sens1, Sensor<?> sens2) {
		super(message);
		this.sens1 = sens1;
		this.sens2 = sens2;
	}

	/**
	 * Getter de sens 1.
	 *
	 * @return sens 1 Sensor 1
	 */
	public Sensor<?> getSens1() {
		return sens1;
	}

	/**
	 * Getter de sens 2.
	 *
	 * @return sens 2 Senor 2
	 */
	public Sensor<?> getSens2() {
		return sens2;
	}
	
}
