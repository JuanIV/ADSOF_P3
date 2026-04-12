package procesadores;

import java.time.LocalDateTime;

/**
 * Clase Registro.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class Registro {
	
	/** Valor de la medicion que se registra. */
	private double medicion;
	
	/** Fecha del registro. */
	private LocalDateTime fecha;
	
	/**
	 * Inicializa un nuevo objeto de la clase registro.
	 *
	 * @param medicion Valor de la medicion a registrar
	 */
	public Registro(double medicion) {
		this.medicion = medicion;
		this.fecha = LocalDateTime.now();
	}

	/**
	 * Getter de medicion.
	 *
	 * @return medicion Valor de la medicion registrada
	 */
	public double getMedicion() {
		return medicion;
	}

	/**
	 * Getter de fecha.
	 *
	 * @return fecha Fecha de registro
	 */
	public LocalDateTime getFecha() {
		return fecha;
	}
	
	/**
	 * To string.
	 *
	 * @return String con el valor de la medicion
	 */
	@Override
	public String toString() {
		return String.format("%.2f", medicion);
	}
}
