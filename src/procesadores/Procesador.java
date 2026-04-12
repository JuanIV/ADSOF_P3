package procesadores;

import conversores.*;
import unidades.*;
import java.util.*;

/**
 * Clase Procesador.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class Procesador {
	
	/** Conversor previo a la escritura. */
	private Conversor conversor;
	
	/** Historial de registros de mediciones. */
	private List<Registro> historial = new ArrayList<Registro>();

	/**
	 * Inicializa un nuevo objeto de la clase procesador.
	 *
	 * @param conv Conversor que se aplica al procesador
	 */
	public Procesador(Conversor conv) {
		this.conversor = conv;
	}
	
	/**
	 * Getter de unidad de escritura.
	 *
	 * @return unidad de escritura
	 */
	public Unidad getUnidadEscritura() {
		return conversor.getUdSalida();
	}
	
	/**
	 * Getter de unidad de lectura.
	 *
	 * @return unidad de lectura
	 */
	public Unidad getUnidadLectura() {
		return conversor.getUdEntrada();
	}
	
	/**
	 * Registrar medicion.
	 *
	 * @param value Valor obtenido de la medicion
	 */
	public void registrarMedicion(double value) {
		historial.add(new Registro(conversor.aplicarConversion(value)));
	}
	
	/**
	 * Getter de historial.
	 *
	 * @return historial Lista de registros del procesador
	 */
	public List<Registro> getHistorial() {
		return historial;
	}
	
	/**
	 * Getter de minimo.
	 *
	 * @return minimo Minimo valor de entre los registros
	 */
	public double getMinimo() {
		return historial.stream().mapToDouble(r -> r.getMedicion()).min().orElseThrow();
	}

	/**
	 * Getter de maximo.
	 *
	 * @return maximo Maximo valor de entre los registros
	 */
	public double getMaximo() {
		return historial.stream().mapToDouble(r -> r.getMedicion()).max().orElseThrow();
	}

	/**
	 * Getter de media.
	 *
	 * @return media Media de los valores del historial
	 */
	public double getMedia() {
		return historial.stream().mapToDouble(r -> r.getMedicion()).average().orElseThrow();
	}
	
}
