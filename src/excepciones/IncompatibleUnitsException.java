package excepciones;

/**
 * Excepción IncompatibleUnitsException.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class IncompatibleUnitsException extends Exception {
	
	/** Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Inicializa un nuevo objeto de la clase incompatible units exception.
	 *
	 * @param message Mensaje de la excepción
	 */
	public IncompatibleUnitsException(String message) {
		super(message);
	}
}
