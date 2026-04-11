package procesadores;

import conversores.*;
import unidades.*;
import java.util.*;

// TODO: Auto-generated Javadoc
/**
 * Clase Class Procesador.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class Procesador {
	
	/** The conversor. */
	private Conversor conversor;
	
	/** The historial. */
	private List<Registro> historial = new ArrayList<Registro>();

	/**
	 * Inicializa un nuevo objeto de la clase procesador.
	 *
	 * @param conv the conv
	 */
	public Procesador(Conversor conv) {
		this.conversor = conv;
	}
	
	/**
	 * Getter de unidad escritura.
	 *
	 * @return unidad escritura
	 */
	public Unidad getUnidadEscritura() {
		return conversor.getUdSalida();
	}
	
	/**
	 * Getter de unidad lectura.
	 *
	 * @return unidad lectura
	 */
	public Unidad getUnidadLectura() {
		return conversor.getUdEntrada();
	}
	
	/**
	 * Registrar medicion.
	 *
	 * @param value the value
	 */
	public void registrarMedicion(double value) {
		historial.add(new Registro(conversor.aplicarConversion(value)));
	}
	
	/**
	 * Getter de historial.
	 *
	 * @return historial
	 */
	public List<Registro> getHistorial() {
		return historial;
	}
	
	/**
	 * Getter de minimo.
	 *
	 * @return minimo
	 */
	public double getMinimo() {
		return historial.stream().mapToDouble(r -> r.getMedicion()).min().orElseThrow();
	}

	/**
	 * Getter de maximo.
	 *
	 * @return maximo
	 */
	public double getMaximo() {
		return historial.stream().mapToDouble(r -> r.getMedicion()).max().orElseThrow();
	}

	/**
	 * Getter de media.
	 *
	 * @return media
	 */
	public double getMedia() {
		return historial.stream().mapToDouble(r -> r.getMedicion()).average().orElseThrow();
	}
	
}
