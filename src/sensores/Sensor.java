package sensores;

import java.time.*;

public abstract class Sensor<Unidad> {
	protected final String id;
	protected double offset;
	protected Unidad unidad;
	protected LocalDateTime fechaUltimaLectura;
	protected double valorUltimaLectura;
	
	public Sensor(String id, Unidad unidad, double offset) {
		this.id = id;
		this.unidad = unidad;
		this.offset = offset;
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
	
	public void registrarMedicion(double value) {
		this.fechaUltimaLectura = LocalDateTime.now();
		this.valorUltimaLectura = value;
	}
}
