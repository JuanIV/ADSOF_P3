package sensores;

import java.time.*;
import sensores.estrategias.*;
import sensores.unidades.*;

public abstract class Sensor {
	protected final String id;
	protected double offset;
	protected Unidad unidad;
	protected LocalDateTime fechaUltimaLectura;
	protected double valorUltimaLectura;
	private Duration duracionCalibrado;
	private Estrategia estrategia;
	
	public Sensor(String id, Unidad unidad, double offset, Estrategia estrategia) {
		this.id = id;
		this.unidad = unidad;
		this.offset = offset;
		this.estrategia = estrategia;
		
		fechaUltimaLectura = LocalDateTime.now();
		valorUltimaLectura = 0;
		duracionCalibrado = Duration.ofDays(365);
	}

	public String getId() {
		return id;
	}

	public double getOffset() {
		return offset;
	}

	public Unidad getUnidad() {
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
	    registrarMedicion(estrategia.simularLectura());
	}
	
	private void registrarMedicion(double value) {
		this.fechaUltimaLectura = LocalDateTime.now();
		this.valorUltimaLectura = value - offset;
	}
}
