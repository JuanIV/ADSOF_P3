package conversores;

import java.util.function.DoubleUnaryOperator;
import unidades.*;
import excepciones.*;

// TODO: Auto-generated Javadoc
/**
 * Clase Class Conversor.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class Conversor {
	
	/** The ud entrada. */
	protected Unidad udEntrada;
	
	/** The ud salida. */
	protected Unidad udSalida;
	
	/** The formula. */
	protected DoubleUnaryOperator formula;
	
	/** The formula inversa. */
	protected DoubleUnaryOperator formulaInversa;
	
	/**
	 * Inicializa un nuevo objeto de la clase conversor.
	 *
	 * @param in the in
	 * @param out the out
	 * @param f the f
	 * @param inv the inv
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
	 * @param value the value
	 * @return the double
	 */
	public double aplicarConversion(double value) {
		return formula.applyAsDouble(value);
	}
	
	/**
	 * Aplicar inverso.
	 *
	 * @param value the value
	 * @return the double
	 */
	public double aplicarInverso(double value) {
		return formulaInversa.applyAsDouble(value);
	}
	
	/**
	 * Obtener conversor inverso.
	 *
	 * @return the conversor
	 */
	public Conversor obtenerConversorInverso() {
		return new Conversor(udSalida, udEntrada, formulaInversa, formula);
	}
	
	/**
	 * Concatenar.
	 *
	 * @param c1 the c 1
	 * @param c2 the c 2
	 * @return the conversor
	 * @throws IncompatibleUnitsException the incompatible units exception
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
	 * Identidad.
	 *
	 * @param ud the ud
	 * @return the conversor
	 */
	public static Conversor identidad(Unidad ud) {
		return new Conversor(ud, ud, u->u, u->u);
	}
}
