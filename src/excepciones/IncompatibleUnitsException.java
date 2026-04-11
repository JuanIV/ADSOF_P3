package excepciones;

public class IncompatibleUnitsException extends Exception {
	private static final long serialVersionUID = 1L;

	public IncompatibleUnitsException(String message) {
		super(message);
	}
}
