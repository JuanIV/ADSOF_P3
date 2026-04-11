package sensores;

import conversores.Conversor;
import excepciones.IncompatibleUnitsException;
import procesadores.Procesador;
import sensores.estrategias.*;
import unidades.*;

/**
 * Clase SensorPresion.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class SensorPresion extends Sensor {
	
	/** The count. */
	private static int count = 0;
	
	/**
	 * Inicializa un nuevo objeto de la clase sensor presion.
	 *
	 * @param ud Unidades del sensor
	 * @param offset Offset del sensor
	 * @param estrategia Estrategia que sigue el sensor
	 * @param conv Conversor que se aplica a las mediciones del sensor antes de registrar
	 * @throws IncompatibleUnitsException si las unidades del sensor no son las unidades de entrada del conversor
	 */
	public SensorPresion(UnidadPresion ud, double offset, Estrategia estrategia, Conversor conv) throws IncompatibleUnitsException {
		super(String.format("PRES-%04d", (count++)), ud, offset, estrategia, new Procesador(conv));
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
		return "Sensor Presión (" + this.valorUltimaLectura + this.unidad.getSimbolo() + ") última lectura: " + this.fechaUltimaLectura;
	}
}
