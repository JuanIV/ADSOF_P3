package sensores;

import java.time.*;
import java.util.*;
import sensores.estrategias.*;
import unidades.*;
import procesadores.*;
import excepciones.*;

// TODO: Auto-generated Javadoc
/**
 * Clase Class Sensor.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public abstract class Sensor {
	
	/** The id. */
	protected final String id;
	
	/** The offset. */
	protected double offset;
	
	/** The unidad. */
	protected Unidad unidad;
	
	/** The fecha ultima lectura. */
	protected LocalDateTime fechaUltimaLectura;
	
	/** The fecha ultimo calibrado. */
	protected LocalDateTime fechaUltimoCalibrado;
	
	/** The valor ultima lectura. */
	protected double valorUltimaLectura;
	
	/** The duracion calibrado. */
	private Duration duracionCalibrado;
	
	/** The estrategia. */
	private Estrategia estrategia;
	
	/** The procesador. */
	private Procesador procesador;
	
	/**
	 * Inicializa un nuevo objeto de la clase sensor.
	 *
	 * @param id the id
	 * @param unidad the unidad
	 * @param offset the offset
	 * @param estrategia the estrategia
	 * @param procesador the procesador
	 * @param duracionCalibrado the duracion calibrado
	 */
	public Sensor(String id, Unidad unidad, double offset, Estrategia estrategia, Procesador procesador, Duration duracionCalibrado) {
		this.id = id;
		this.offset = offset;
		this.unidad = unidad;
		this.fechaUltimaLectura = LocalDateTime.now();
		this.fechaUltimoCalibrado = LocalDateTime.now();
		this.valorUltimaLectura = 0;
		this.duracionCalibrado = duracionCalibrado;
		this.estrategia = estrategia;
		this.procesador = procesador;
	}
	
	/**
	 * Inicializa un nuevo objeto de la clase sensor.
	 *
	 * @param id the id
	 * @param unidad the unidad
	 * @param offset the offset
	 * @param estrategia the estrategia
	 * @param procesador the procesador
	 */
	public Sensor(String id, Unidad unidad, double offset, Estrategia estrategia, Procesador procesador) {
		this(id, unidad, offset, estrategia, procesador, Duration.ofDays(365));
	}
	
	/**
	 * ********************Getters y setters*****************************.
	 *
	 * @return id
	 */

	public String getId() {
		return id;
	}

	/**
	 * Getter de offset.
	 *
	 * @return offset
	 */
	public double getOffset() {
		return offset;
	}

	/**
	 * Getter de unidad lectura.
	 *
	 * @return unidad lectura
	 */
	public Unidad getUnidadLectura() {
		return unidad;
	}
	
	/**
	 * Getter de estrategia.
	 *
	 * @return estrategia
	 */
	public Estrategia getEstrategia() {
		return estrategia;
	}

	/**
	 * Getter de fecha ultima lectura.
	 *
	 * @return fecha ultima lectura
	 */
	public LocalDateTime getFechaUltimaLectura() {
		return fechaUltimaLectura;
	}

	/**
	 * Getter de valor ultima lectura.
	 *
	 * @return valor ultima lectura
	 */
	public double getValorUltimaLectura() {
		return valorUltimaLectura;
	}
	
	/**
	 * Getter de duracion calibrado.
	 *
	 * @return duracion calibrado
	 */
	public Duration getDuracionCalibrado() {
		return duracionCalibrado;
	}
	
	/**
	 * Setter de duracion calibrado.
	 *
	 * @param duracion nuevo duracion calibrado
	 */
	public void setDuracionCalibrado(Duration duracion) {
		duracionCalibrado = duracion;
	}
	
	/**
	 * Getter de unidad escritura.
	 *
	 * @return unidad escritura
	 */
	public Unidad getUnidadEscritura() {
		return procesador.getUnidadEscritura();
	}
	
	/**
	 * Getter de historial.
	 *
	 * @return historial
	 */
	public List<Registro> getHistorial() {
		return procesador.getHistorial();
	}
	
	/**
	 * Getter de media.
	 *
	 * @return media
	 */
	public double getMedia() {
		return procesador.getMedia();
	}
	
	/**
	 * Getter de minimo.
	 *
	 * @return minimo
	 */
	public double getMinimo() {
		return procesador.getMinimo();
	}
	
	/**
	 * Getter de maximo.
	 *
	 * @return maximo
	 */
	public double getMaximo() {
		return procesador.getMaximo();
	}
	
	/**
	 * **************************Métodos**********************************.
	 *
	 * @return true, if successful
	 */
	
	public boolean estaCalibrado() {
		return (LocalDateTime.now().isBefore(fechaUltimoCalibrado.plus(duracionCalibrado)));
	}
	
	/**
	 * Calibrar.
	 *
	 * @param nuevoOffset the nuevo offset
	 * @param duracionCalibrado the duracion calibrado
	 */
	public void calibrar(double nuevoOffset, Duration duracionCalibrado) {
		offset = nuevoOffset;
		this.duracionCalibrado = duracionCalibrado;
		fechaUltimoCalibrado = LocalDateTime.now();
	}
	
	/**
	 * Calibrar.
	 *
	 * @param nuevoOffset the nuevo offset
	 */
	public void calibrar(double nuevoOffset) {
		calibrar(nuevoOffset, this.duracionCalibrado);
	}
	
	/**
	 * Calibrar.
	 *
	 * @param duracionCalibrado the duracion calibrado
	 */
	public void calibrar(Duration duracionCalibrado) {
		calibrar(offset, duracionCalibrado);
	}
	
	/**
	 * Calibrar.
	 */
	public void calibrar() {
		calibrar(offset, duracionCalibrado);
	}
	
	/**
	 * Tomar medicion.
	 *
	 * @throws SensorDescalibrado the sensor descalibrado
	 */
	public void tomarMedicion() throws SensorDescalibrado {
		double medicion = estrategia.simularLectura() - offset;
		if(!(unidad.inRange(medicion)) || !estaCalibrado())
			throw new SensorDescalibrado(this);
		this.fechaUltimaLectura = LocalDateTime.now();
		this.valorUltimaLectura = medicion;
	    procesador.registrarMedicion(medicion);
	}

}
