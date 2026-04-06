package sensores;

import java.time.*;
import java.util.*;
import sensores.estrategias.*;
import unidades.*;
import procesadores.*;
import excepciones.*;

public abstract class Sensor {
	protected final String id;
	protected double offset;
	protected Unidad unidad;
	protected LocalDateTime fechaUltimaLectura;
	protected LocalDateTime fechaUltimoCalibrado;
	protected double valorUltimaLectura;
	private Duration duracionCalibrado;
	private Estrategia estrategia;
	private Procesador procesador;
	
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
	
	public Sensor(String id, Unidad unidad, double offset, Estrategia estrategia, Procesador procesador) {
		this(id, unidad, offset, estrategia, procesador, Duration.ofDays(365));
	}
	
	/**********************Getters y setters******************************/

	public String getId() {
		return id;
	}

	public double getOffset() {
		return offset;
	}

	public Unidad getUnidadLectura() {
		return unidad;
	}
	
	public Estrategia getEstrategia() {
		return estrategia;
	}

	public LocalDateTime getFechaUltimaLectura() {
		return fechaUltimaLectura;
	}

	public double getValorUltimaLectura() {
		return valorUltimaLectura;
	}
	
	public Duration getDuracionCalibrado() {
		return duracionCalibrado;
	}
	
	public void setDuracionCalibrado(Duration duracion) {
		duracionCalibrado = duracion;
	}
	
	public Unidad getUnidadEscritura() {
		return procesador.getUnidadEscritura();
	}
	
	public List<Registro> getHistorial() {
		return procesador.getHistorial();
	}
	
	public double getMedia() {
		return procesador.getMedia();
	}
	
	public double getMinimo() {
		return procesador.getMinimo();
	}
	
	public double getMaximo() {
		return procesador.getMaximo();
	}
	
	/****************************Métodos***********************************/
	
	public boolean estaCalibrado() {
		return (LocalDateTime.now().isBefore(fechaUltimoCalibrado.plus(duracionCalibrado))&&(unidad.inRange(valorUltimaLectura)));
	}
	
	public void calibrar(double nuevoOffset, Duration duracionCalibrado) {
		offset = nuevoOffset;
		this.duracionCalibrado = duracionCalibrado;
		fechaUltimoCalibrado = LocalDateTime.now();
	}
	
	public void calibrar(double nuevoOffset) {
		calibrar(nuevoOffset, this.duracionCalibrado);
	}
	
	public void calibrar(Duration duracionCalibrado) {
		calibrar(offset, duracionCalibrado);
	}
	
	public void calibrar() {
		calibrar(offset, duracionCalibrado);
	}
	
	public void tomarMedicion() throws SensorDescalibrado {
		double medicion = estrategia.simularLectura() - offset;
		if(!(unidad.inRange(medicion)) || !estaCalibrado())
			throw new SensorDescalibrado(this);
		this.fechaUltimaLectura = LocalDateTime.now();
		this.valorUltimaLectura = medicion;
	    procesador.registrarMedicion(medicion);
	}

}
