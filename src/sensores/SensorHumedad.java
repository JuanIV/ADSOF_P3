package sensores;

import sensores.estrategias.*;
import unidades.*;
import conversores.*;
import excepciones.IncompatibleUnitsException;
import procesadores.*;

// TODO: Auto-generated Javadoc
/**
 * Clase Class SensorHumedad.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class SensorHumedad extends Sensor {
	
	/** The count. */
	private static int count = 0;
	
	/**
	 * Inicializa un nuevo objeto de la clase sensor humedad.
	 *
	 * @param ud the ud
	 * @param offset the offset
	 * @param estrategia the estrategia
	 * @param conv the conv
	 * @throws IncompatibleUnitsException the incompatible units exception
	 */
	public SensorHumedad(UnidadHumedad ud, double offset, Estrategia estrategia, Conversor conv) throws IncompatibleUnitsException {
		super(String.format("HUM-%04d", (count++)), ud, offset, estrategia, new Procesador(conv));
		if(!ud.equals(conv.getUdEntrada())) {
			count--;
			throw new IncompatibleUnitsException("Las unidades del sensor y el conversor no son compatibles");
		}
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Sensor Humedad (" + this.valorUltimaLectura + this.unidad.getSimbolo() + ") última lectura: " + this.fechaUltimaLectura;
	}
}
