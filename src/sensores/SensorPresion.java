package sensores;

import sensores.estrategias.*;
import sensores.unidades.*;

public class SensorPresion extends Sensor {
	private static int count = 0;

	public SensorPresion(UnidadPresion ud, double offset) {
		super(String.format("PRES-%04d", (count++)), ud, offset, new EstrategiaAnterior(0, (ud.getMax()+ud.getMin())/2));
	}
	
	public SensorPresion(UnidadPresion ud, double offset, Estrategia estrategia) {
		super(String.format("PRES-%04d", (count++)), ud, offset, estrategia);
	}
	
	public double simularMedicion() {
		return 0;
	}
	
	@Override
	public String toString() {
		return "Sensor Presión (" + this.valorUltimaLectura + this.unidad.getSimbolo() + ") última lectura: " + this.fechaUltimaLectura;
	}
}
