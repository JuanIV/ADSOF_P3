package sensores;

import conversores.Conversor;
import excepciones.IncompatibleUnitsException;
import procesadores.Procesador;
import sensores.estrategias.*;
import unidades.*;

public class SensorPresion extends Sensor {
	private static int count = 0;
	
	public SensorPresion(UnidadPresion ud, double offset, Estrategia estrategia, Conversor conv) throws IncompatibleUnitsException {
		super(String.format("PRES-%04d", (count++)), ud, offset, estrategia, new Procesador(conv));
		if(!ud.equals(conv.getUdEntrada())) {
			count--;
			throw new IncompatibleUnitsException("Las unidades del sensor y el conversor no son compatibles");
		}
	}
	
	@Override
	public String toString() {
		return "Sensor Presión (" + this.valorUltimaLectura + this.unidad.getSimbolo() + ") última lectura: " + this.fechaUltimaLectura;
	}
}
