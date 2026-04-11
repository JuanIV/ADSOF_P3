package excepciones;

import sensores.*;

public class AlertaSensor extends Exception {
	private static final long serialVersionUID = 1L;
	private Sensor sensor;
	
	public AlertaSensor(Sensor sensor, String message) {
		super(message);
		this.sensor = sensor;
	}
	
	@Override
	public String getMessage() {
		return "Sensor " + this.sensor.getId() + ": " + super.getMessage();
	}

	public Sensor getSensor() {
		return sensor;
	}
}
