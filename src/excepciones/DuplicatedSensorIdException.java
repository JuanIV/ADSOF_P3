package excepciones;

import sensores.*;

public class DuplicatedSensorIdException extends Exception {
	private static final long serialVersionUID = 1L;
	private Sensor sens1;
	private Sensor sens2;

	public DuplicatedSensorIdException(String message, Sensor sens1, Sensor sens2) {
		super(message);
		this.sens1 = sens1;
		this.sens2 = sens2;
	}

	public Sensor getSens1() {
		return sens1;
	}

	public Sensor getSens2() {
		return sens2;
	}
	
}
