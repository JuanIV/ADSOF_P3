package procesadores;

import java.time.LocalDateTime;

// TODO: Auto-generated Javadoc
/**
 * Clase Class Registro.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class Registro {
	
	/** The medicion. */
	private double medicion;
	
	/** The fecha. */
	private LocalDateTime fecha;
	
	/**
	 * Inicializa un nuevo objeto de la clase registro.
	 *
	 * @param medicion the medicion
	 */
	public Registro(double medicion) {
		this.medicion = medicion;
		this.fecha = LocalDateTime.now();
	}

	/**
	 * Getter de medicion.
	 *
	 * @return medicion
	 */
	public double getMedicion() {
		return medicion;
	}

	/**
	 * Getter de fecha.
	 *
	 * @return fecha
	 */
	public LocalDateTime getFecha() {
		return fecha;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return String.format("%.2f", medicion);
	}
}
