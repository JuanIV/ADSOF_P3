package sensores;

import sensores.estrategias.*;
import unidades.*;
import conversores.*;
import excepciones.IncompatibleUnitsException;
import procesadores.*;

/**
 * Clase SensorHumedad.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class SensorHumedad extends Sensor<UnidadHumedad> {
	
	/** Cuenta estática de los sensores de humedad para poder escribir el número en la id. */
	private static int count = 0;
	
	/** Etiqueta que se añade al comienzo de la id de los sensores de esta clase */
	private static final String tag = "HUM";
	
	/**
	 * Inicializa un nuevo objeto de la clase sensor humedad.
	 *
	 * @param ud Unidades del sensor
	 * @param offset Offset del sensor
	 * @param estrategia Estrategia que sigue el sensor
	 * @param conv Conversor que se aplica a las mediciones del sensor antes de registrar
	 * @throws IncompatibleUnitsException si las unidades del sensor no son las unidades de entrada del conversor
	 */
	public SensorHumedad(UnidadHumedad ud, double offset, Estrategia estrategia, Conversor conv) throws IncompatibleUnitsException {
		super(String.format("%s-%04d", tag, (count++)), ud, offset, estrategia, new Procesador(conv));
		if(!ud.equals(conv.getUdEntrada())) {
			count--;
			throw new IncompatibleUnitsException("Las unidades del sensor y el conversor no son compatibles");
		}
	}
	
	/**
	 * To string.
	 *
	 * @return String con la información del sensor
	 */
	@Override
	public String toString() {
		return "Sensor Humedad (" + this.valorUltimaLectura + this.unidad.getSimbolo() + ") última lectura: " + this.fechaUltimaLectura;
	}
}
