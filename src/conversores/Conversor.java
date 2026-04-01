package conversores;

import java.util.function.DoubleUnaryOperator;
import unidades.*;
import excepciones.*;

public class Conversor {
	protected Unidad udEntrada;
	protected Unidad udSalida;
	protected DoubleUnaryOperator formula;
	protected DoubleUnaryOperator formulaInversa;
	
	public Conversor(Unidad in, Unidad out, DoubleUnaryOperator f, DoubleUnaryOperator inv) {
		udEntrada = in;
		udSalida = out;
		formula = f;
		formulaInversa = inv;
	}
	
	public Unidad getUdEntrada() {
		return udEntrada;
	}

	public Unidad getUdSalida() {
		return udSalida;
	}

	public double aplicarConversion(double value) {
		return formula.applyAsDouble(value);
	}
	
	public double aplicarInverso(double value) {
		return formulaInversa.applyAsDouble(value);
	}
	
	public Conversor obtenerConversorInverso() {
		return new Conversor(udSalida, udEntrada, formulaInversa, formula);
	}
	
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
	
	public static Conversor identidad(Unidad ud) {
		return new Conversor(ud, ud, u->u, u->u);
	}
}
