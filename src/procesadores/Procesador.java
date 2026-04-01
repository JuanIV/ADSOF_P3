package procesadores;

import conversores.*;
import unidades.*;
import java.util.*;

public class Procesador {
	private Conversor conversor;
	private List<Registro> historial = new ArrayList<Registro>();

	public Procesador(Conversor conv) {
		this.conversor = conv;
	}
	
	public Unidad getUnidadEscritura() {
		return conversor.getUdSalida();
	}
	
	public Unidad getUnidadLectura() {
		return conversor.getUdEntrada();
	}
	
	public void registrarMedicion(double value) {
		historial.add(new Registro(conversor.aplicarConversion(value)));
	}
	
	public List<Registro> getHistorial() {
		return historial;
	}
	
	public double getMinimo() {
		return historial.stream().mapToDouble(r -> r.getMedicion()).min().orElseThrow();
	}

	public double getMaximo() {
		return historial.stream().mapToDouble(r -> r.getMedicion()).max().orElseThrow();
	}

	public double getMedia() {
		return historial.stream().mapToDouble(r -> r.getMedicion()).average().orElseThrow();
	}
	
}
