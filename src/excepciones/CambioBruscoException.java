package excepciones;

import sensores.*;

// TODO: Auto-generated Javadoc
/**
 * Clase Class CambioBruscoException.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class CambioBruscoException extends AlertaSensor {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The ultima. */
	private double ultima;
	
	/** The anterior. */
	private double anterior;

	/**
	 * Inicializa un nuevo objeto de la clase cambio brusco exception.
	 *
	 * @param sensor the sensor
	 * @param ultima the ultima
	 * @param anterior the anterior
	 */
	public CambioBruscoException(Sensor sensor, double ultima, double anterior) {
		super(sensor, "Cambio brusco");
		this.ultima = ultima;
		this.anterior = anterior;
	}
	
	/**
	 * Getter de message.
	 *
	 * @return message
	 */
	@Override
	public String getMessage() {
		return "Cambio brusco en " + getSensor().getId() + ": " + ultima + getSensor().getUnidadEscritura().getSimbolo() + "(anterior: " + anterior + getSensor().getUnidadEscritura().getSimbolo() + ")";
	}
}
