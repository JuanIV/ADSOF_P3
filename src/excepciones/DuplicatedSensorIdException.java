package excepciones;

import sensores.*;

public class DuplicatedSensorIdException extends Exception {
	Sensor<?> sens1;
	Sensor<?> sens2;

	public DuplicatedSensorIdException(String message, Sensor<?> sens1, Sensor<?> sens2) {
		super(message);
		this.sens1 = sens1;
		this.sens2 = sens2;
	}
}
