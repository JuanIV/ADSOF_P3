package excepciones;

import sensores.*;

public class CambioBruscoException extends AlertaSensor {
	private static final long serialVersionUID = 1L;
	private double ultima;
	private double anterior;

	public CambioBruscoException(Sensor sensor, double ultima, double anterior) {
		super(sensor, "Cambio brusco");
		this.ultima = ultima;
		this.anterior = anterior;
	}
	
	@Override
	public String getMessage() {
		return "Cambio brusco en " + getSensor().getId() + ": " + ultima + getSensor().getUnidadEscritura().getSimbolo() + "(anterior: " + anterior + getSensor().getUnidadEscritura().getSimbolo() + ")";
	}
}
