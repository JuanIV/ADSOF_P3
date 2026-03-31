package sensores;

import java.time.*;

public abstract class Sensor {
	protected final String id;
	protected double offset;
	protected Unidad unidad;
	protected LocalDateTime fechaUltimaLectura;
	protected double valorUltimaLectura;
	private Duration duracionCalibrado;
	
	public Sensor(String id, Unidad unidad, double offset) {
		this.id = id;
		this.unidad = unidad;
		this.offset = offset;
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
	
	public abstract double simularMedicion();

	public void tomarMedicion() {
	    registrarMedicion(simularMedicion());
	}
	
	public void registrarMedicion(double value) {
		this.fechaUltimaLectura = LocalDateTime.now();
		this.valorUltimaLectura = value - offset;
	}
}
