package sensores;

import sensores.estrategias.*;
import unidades.*;
import conversores.*;
import excepciones.IncompatibleUnitsException;
import procesadores.*;

public class SensorHumedad extends Sensor {
	private static int count = 0;
	
	public SensorHumedad(UnidadHumedad ud, double offset, Estrategia estrategia, Conversor conv) throws IncompatibleUnitsException {
		super(String.format("HUM-%04d", (count++)), ud, offset, estrategia, new Procesador(conv));
		if(!ud.equals(conv.getUdEntrada())) {
			count--;
			throw new IncompatibleUnitsException("Las unidades del sensor y el conversor no son compatibles");
		}
	}
	
	@Override
	public String toString() {
		return "Sensor Humedad (" + this.valorUltimaLectura + this.unidad.getSimbolo() + ") última lectura: " + this.fechaUltimaLectura;
	}
}
