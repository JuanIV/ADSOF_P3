package conversores;

import unidades.*;
import excepciones.*;

/**
 * Clase fábrica para crear converosres entre unidades
 */
public class ConversorFactory {
	
	/** Conversor celsius-kelvin. */
	private static Conversor CelsiusKelvin = new Conversor(UnidadTemperatura.CELSIUS, UnidadTemperatura.KELVIN, u-> u+273.15, u-> u-273.15);
	
	/** Conversor celsius-fahrenheit. */
	private static Conversor CelsiusFahrenheit = new Conversor(UnidadTemperatura.CELSIUS, UnidadTemperatura.FAHRENHEIT, u-> u*(9.0/5.0) + 32, u-> (u-32)*(5.0/9.0));
	
	/** Conversor hPa-Pascal*/
	private static Conversor hPaPascal = new Conversor(UnidadPresion.HECTOPASCAL, UnidadPresion.PASCAL, u->u*100, u->u/100.0);
	
	/** Conversor hPa-Mbar */
	private static Conversor hPaMbar = new Conversor(UnidadPresion.HECTOPASCAL, UnidadPresion.MILIBAR, u->u, u->u);
	
	/**
	 * Método para crear un conversor a partir de los que se tiene concatenando e invirtiendo
	 *
	 * @param in Unidades de entrada
	 * @param out Unidades de salida
	 * @return Conversor de las unidades de entrada a las unidades de salida
	 * @throws IncompatibleUnitsException si las unidades de entrada y salida no son del mismo tipo
	 */
	public static Conversor crear(Unidad in, Unidad out) throws IncompatibleUnitsException {
		switch(in) {
		case UnidadTemperatura.CELSIUS:
			switch(out) {
				case UnidadTemperatura.CELSIUS: 	return Conversor.identidad(UnidadTemperatura.CELSIUS);
				case UnidadTemperatura.KELVIN: 		return CelsiusKelvin;
				case UnidadTemperatura.FAHRENHEIT:	return CelsiusFahrenheit;
				default:	throw new IncompatibleUnitsException("Las unidades deben ser del mismo tipo");
			}
		case UnidadTemperatura.KELVIN:
			switch(out) {
				case UnidadTemperatura.CELSIUS:		return CelsiusKelvin.obtenerConversorInverso();
				case UnidadTemperatura.KELVIN:		return Conversor.identidad(UnidadTemperatura.KELVIN);
				case UnidadTemperatura.FAHRENHEIT:	return Conversor.concatenar(CelsiusKelvin.obtenerConversorInverso(), CelsiusFahrenheit);
				default:	throw new IncompatibleUnitsException("Las unidades deben ser del mismo tipo");
			}
		case UnidadTemperatura.FAHRENHEIT:
			switch(out) {
				case UnidadTemperatura.CELSIUS:		return CelsiusFahrenheit.obtenerConversorInverso();
				case UnidadTemperatura.KELVIN:		return Conversor.concatenar(CelsiusFahrenheit.obtenerConversorInverso(), CelsiusKelvin);
				case UnidadTemperatura.FAHRENHEIT:	return Conversor.identidad(UnidadTemperatura.FAHRENHEIT);
				default:	throw new IncompatibleUnitsException("Las unidades deben ser del mismo tipo");
			}
			
			
		case UnidadPresion.HECTOPASCAL:
			switch (out) {
            	case UnidadPresion.HECTOPASCAL:	return Conversor.identidad(in);
            	case UnidadPresion.PASCAL:		return hPaPascal;
            	case UnidadPresion.MILIBAR:		return hPaMbar;
            	default: throw new IncompatibleUnitsException("Las unidades deben ser del mismo tipo");
			}
		case UnidadPresion.PASCAL:
			switch (out) {
				case UnidadPresion.PASCAL:		return Conversor.identidad(in);
				case UnidadPresion.HECTOPASCAL:	return hPaPascal.obtenerConversorInverso();
				case UnidadPresion.MILIBAR:		return Conversor.concatenar(hPaPascal.obtenerConversorInverso(), hPaMbar);
				default: throw new IncompatibleUnitsException("Las unidades deben ser del mismo tipo");
			}
		case UnidadPresion.MILIBAR:
			switch (out) {
				case UnidadPresion.MILIBAR:     return Conversor.identidad(in);
				case UnidadPresion.HECTOPASCAL: return hPaMbar.obtenerConversorInverso();
				case UnidadPresion.PASCAL:      return Conversor.concatenar(hPaMbar.obtenerConversorInverso(), hPaPascal);
				default: throw new IncompatibleUnitsException("Las unidades deben ser del mismo tipo");
			}
            
            
		case UnidadHumedad.HUMEDAD:
			switch (out) {
				case UnidadHumedad.HUMEDAD: return Conversor.identidad(in);
				default: throw new IncompatibleUnitsException("Las unidades deben ser del mismo tipo");
			}
		default:
			throw new IncompatibleUnitsException("Unidad de entrada desconocida");
		}
	}
	
}
