package sensores;

import sensores.unidades.*;
import sensores.estrategias.*;

public class SensorHumedad extends Sensor {
	private static int count = 0;
	
	public SensorHumedad(UnidadHumedad ud, double offset) {
		super(String.format("HUM-%04d", (count++)), ud, offset, new EstrategiaAnterior(0, (ud.getMax()+ud.getMin())/2));
	}
	
	public SensorHumedad(UnidadHumedad ud, double offset, Estrategia estrategia) {
		super(String.format("HUM-%04d", (count++)), ud, offset, estrategia);
	}
	
	public double simularMedicion() {
		return 0;
	}
	
	@Override
	public String toString() {
		return "Sensor Humedad (" + this.valorUltimaLectura + this.unidad.getSimbolo() + ") última lectura: " + this.fechaUltimaLectura;
	}
}
