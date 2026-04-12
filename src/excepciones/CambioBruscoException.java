package excepciones;

import sensores.*;

/**
 * Clase CambioBruscoException.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class CambioBruscoException extends AlertaSensor {
	
	/** Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** Ultima medicion (que provocó la alerta). */
	private double ultima;
	
	/** Anterior medicion a la que causó la alerta. */
	private double anterior;

	/**
	 * Inicializa un nuevo objeto de la clase cambio brusco exception.
	 *
	 * @param sensor Sensor que activó la alerta
	 * @param ultima Ultima medición
	 * @param anterior Medicion anterior a la ultima
	 */
	public CambioBruscoException(Sensor<?> sensor, double ultima, double anterior) {
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
