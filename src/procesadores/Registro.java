package procesadores;

import java.time.LocalDateTime;

public class Registro {
	private double medicion;
	private LocalDateTime fecha;
	
	public Registro(double medicion) {
		this.medicion = medicion;
		this.fecha = LocalDateTime.now();
	}

	public double getMedicion() {
		return medicion;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}
	
	@Override
	public String toString() {
		return String.format("%.2f", medicion);
	}
}
