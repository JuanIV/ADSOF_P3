package conversores;

import java.util.function.DoubleUnaryOperator;
import unidades.*;
import excepciones.*;

/**
 * Clase Conversor.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class Conversor {
	
	/** Unidad de entrada. */
	protected Unidad udEntrada;
	
	/** Unidad de salida. */
	protected Unidad udSalida;
	
	/** Fórmula de conversión de unidades. */
	protected DoubleUnaryOperator formula;
	
	/** Fórmula inversa. */
	protected DoubleUnaryOperator formulaInversa;
	
	/**
	 * Inicializa un nuevo objeto de la clase conversor.
	 *
	 * @param in Unidades de entrada
	 * @param out Unidades de salida
	 * @param f Fórmula de conversión
	 * @param inv Fórmula inversa
	 */
	public Conversor(Unidad in, Unidad out, DoubleUnaryOperator f, DoubleUnaryOperator inv) {
		udEntrada = in;
		udSalida = out;
		formula = f;
		formulaInversa = inv;
	}
	
	/**
	 * Getter de ud entrada.
	 *
	 * @return ud entrada
	 */
	public Unidad getUdEntrada() {
		return udEntrada;
	}

	/**
	 * Getter de ud salida.
	 *
	 * @return ud salida
	 */
	public Unidad getUdSalida() {
		return udSalida;
	}

	/**
	 * Aplicar conversion.
	 *
	 * @param value Valor a convertir
	 * @return Valor convertido
	 */
	public double aplicarConversion(double value) {
		return formula.applyAsDouble(value);
	}
	
	/**
	 * Aplicar inverso.
	 *
	 * @param value Valor a convertir
	 * @return Valor convertido
	 */
	public double aplicarInverso(double value) {
		return formulaInversa.applyAsDouble(value);
	}
	
	/**
	 * Obtener conversor inverso.
	 *
	 * @return Conversor inverso a este
	 */
	public Conversor obtenerConversorInverso() {
		return new Conversor(udSalida, udEntrada, formulaInversa, formula);
	}
	
	/**
	 * Concatenar conversores para obtener uno nuevo.
	 *
	 * @param c1 Primer conversor (cuya entrada será la entrada del nuevo)
	 * @param c2 Segundo conversor (cuya salida será la salida del nuevo)
	 * @return Conversor obtenido de concatenar c1 y c2
	 * @throws IncompatibleUnitsException si la salida de c1 no tiene las mismas unidades que la entrada de c2
	 */
	public static Conversor concatenar(Conversor c1, Conversor c2) throws IncompatibleUnitsException {
		if (!c1.udSalida.equals(c2.udEntrada)) {
			throw new IncompatibleUnitsException("Conversores incompatibles");
		}
		return new Conversor(
				c1.udEntrada, c2.udSalida,
				v -> c2.formula.applyAsDouble(c1.formula.applyAsDouble(v)),
				v -> c1.formulaInversa.applyAsDouble(c2.formulaInversa.applyAsDouble(v))
				);
	}
	
	/**
	 * Método para obtener el conversor identidad.
	 *
	 * @param ud Unidades de las que se quiere la identidad.
	 * @return Conversor identidad que no transforma las unidades.
	 */
	public static Conversor identidad(Unidad ud) {
		return new Conversor(ud, ud, u->u, u->u);
	}
}
