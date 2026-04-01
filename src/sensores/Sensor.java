package sensores;

import java.time.*;
import java.util.*;
import sensores.estrategias.*;
import unidades.*;
import procesadores.*;

public abstract class Sensor {
	protected final String id;
	protected double offset;
	protected Unidad unidad;
	protected LocalDateTime fechaUltimaLectura;
	protected double valorUltimaLectura;
	private Duration duracionCalibrado;
	private Estrategia estrategia;
	private Procesador procesador;
	
	public Sensor(String id, Unidad unidad, double offset, Estrategia estrategia, Procesador procesador) {
		this.id = id;
		this.unidad = unidad;
		this.offset = offset;
		this.estrategia = estrategia;
		this.procesador = procesador;
		
		fechaUltimaLectura = LocalDateTime.now();
		valorUltimaLectura = 0;
		duracionCalibrado = Duration.ofDays(365);
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
		return (LocalDateTime.now().isBefore(fechaUltimaLectura.plus(duracionCalibrado))&&(unidad.inRange(valorUltimaLectura)));
	}
	
	public void calibrar(double nuevoOffset) {
		offset = nuevoOffset;
		fechaUltimaLectura = LocalDateTime.now();
	}
	
	public void calibrar() {
		calibrar(offset);
	}
	
	public void tomarMedicion() {
		double medicion = estrategia.simularLectura() - offset;
		this.fechaUltimaLectura = LocalDateTime.now();
		this.valorUltimaLectura = medicion;
	    procesador.registrarMedicion(medicion);
	}

}
