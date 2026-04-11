package sensores;

import java.time.*;
import java.util.*;
import sensores.estrategias.*;
import unidades.*;
import procesadores.*;
import excepciones.*;

/**
 * Clase Sensor.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public abstract class Sensor {
	
	/** id del sensor. */
	protected final String id;
	
	/** offset del sensor. */
	protected double offset;
	
	/** Unidades de medición del sensor. */
	protected Unidad unidad;
	
	/** Fecha de la ultima lectura. */
	protected LocalDateTime fechaUltimaLectura;
	
	/** Fecha del ultimo calibrado. */
	protected LocalDateTime fechaUltimoCalibrado;
	
	/** Valor de la ultima lectura. */
	protected double valorUltimaLectura;
	
	/** Duracion del calibrado. */
	private Duration duracionCalibrado;
	
	/** Estrategia que sigue el sensor. */
	private Estrategia estrategia;
	
	/** Procesador del sensor. */
	private Procesador procesador;
	
	/**
	 * Inicializa un nuevo objeto de la clase sensor.
	 *
	 * @param id ID del sensor
	 * @param unidad Unidades del sensor
	 * @param offset Offset del sensor
	 * @param estrategia Estrategia del sensor
	 * @param procesador Procesador del sensor
	 * @param duracionCalibrado Duracion del calibrado del sensor
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
	 * @param id ID del sensor
	 * @param unidad Unidades del sensor
	 * @param offset Offset del sensor
	 * @param estrategia Estrategia del sensor
	 * @param procesador Procesador del sensor
	 */
	public Sensor(String id, Unidad unidad, double offset, Estrategia estrategia, Procesador procesador) {
		this(id, unidad, offset, estrategia, procesador, Duration.ofDays(365));
	}
	
	/*********************Getters y setters*****************************/
	
	
	/**
	 * Getter de la id del sensor
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Getter de offset.
	 *
	 * @return offset del sensor
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
	 * Getter de la media de los valores leidos.
	 *
	 * @return media
	 */
	public double getMedia() {
		return procesador.getMedia();
	}
	
	/**
	 * Getter del minimo valor medido.
	 *
	 * @return minimo
	 */
	public double getMinimo() {
		return procesador.getMinimo();
	}
	
	/**
	 * Getter del maximo valor medido.
	 *
	 * @return maximo
	 */
	public double getMaximo() {
		return procesador.getMaximo();
	}
	
	/****************************Métodos**********************************/
	
	/**
	 * Comprueba si el sensor está calibrado
	 *
	 * @return true, si está calibrado, false si no
	 */
	public boolean estaCalibrado() {
		return (LocalDateTime.now().isBefore(fechaUltimoCalibrado.plus(duracionCalibrado)));
	}
	
	/**
	 * Calibrar el sensor.
	 *
	 * @param nuevoOffset nuevo offset
	 * @param duracionCalibrado duracion calibrado
	 */
	public void calibrar(double nuevoOffset, Duration duracionCalibrado) {
		offset = nuevoOffset;
		this.duracionCalibrado = duracionCalibrado;
		fechaUltimoCalibrado = LocalDateTime.now();
	}
	
	/**
	 * Calibrar.
	 *
	 * @param nuevoOffset nuevo offset
	 */
	public void calibrar(double nuevoOffset) {
		calibrar(nuevoOffset, this.duracionCalibrado);
	}
	
	/**
	 * Calibrar.
	 *
	 * @param duracionCalibrado duracion calibrado
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
	 * @throws SensorDescalibrado si el sensor obtiene una medicion fuera de rango
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
