package excepciones;

import sensores.*;

public class SensorDescalibrado extends AlertaSensor {
	private static final long serialVersionUID = 1L;

	public SensorDescalibrado(Sensor sensor) {
		super(sensor, "Sensor descalibrado");
	}
}
