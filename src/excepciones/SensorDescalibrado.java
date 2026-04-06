package excepciones;

import sensores.*;

public class SensorDescalibrado extends Exception {
	private final Sensor sensor;

	public SensorDescalibrado(Sensor s) {
		sensor = s;
	}
	
	public Sensor getSensor() {
		return sensor;
	}
}
