package excepciones;

import sensores.*;

public class CambioBruscoException extends Exception {
	private Sensor sensor;

	public CambioBruscoException(String message, Sensor sensor) {
		super(message);
		this.sensor = sensor;
	}
	
	public Sensor getSensor() {
		return sensor;
	}
	
	public String getMessage() {
		return super.getMessage() + " Sensor: " + sensor.getId();
	}
}
