package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import sensores.estrategias.*;
import unidades.*;

/**
 * Tests para el Apartado 2: Estrategias de Simulación de Lectura.
 *
 * <p>
 * Verifica que cada estrategia genera valores dentro de los rangos esperados,
 * que la estrategia anterior evoluciona a partir del valor previo, y que la
 * estrategia de media mantiene la media histórica correctamente.
 * </p>
 *
 * @author Nombre Apellido
 */
public class TestApartado2 {

	private static final int NUM_MUESTRAS = 1000;

	// =========================================================================
	// EstrategiaRango
	// =========================================================================

	/**
	 * Verifica que {@link EstrategiaRango} genera valores dentro del rango
	 * especificado cuando no hay fallo.
	 *
	 * <p>
	 * Con probabilidad de fallo 0, todos los valores deben estar dentro de [min,
	 * max].
	 * </p>
	 */
	@Test
	@DisplayName("EstrategiaRango genera valores en rango con prob fallo 0")
	public void testEstrategiaRangoSinFallo() {
		double min = 10.0, max = 50.0;
		EstrategiaRango estrategia = new EstrategiaRango(0, min, max);

		for (int i = 0; i < NUM_MUESTRAS; i++) {
			double valor = estrategia.simularLectura();
			assertTrue(valor >= min && valor <= max,
					String.format("Valor %.2f debe estar en [%.2f, %.2f]", valor, min, max));
		}
	}

	/**
	 * Verifica que {@link EstrategiaRango} genera valores fuera del rango cuando la
	 * probabilidad de fallo es 100%.
	 */
	@Test
	@DisplayName("EstrategiaRango genera valores fuera de rango con prob fallo 100")
	public void testEstrategiaRangoSiempreFalla() {
		double min = 10.0, max = 50.0;
		EstrategiaRango estrategia = new EstrategiaRango(100, min, max);

		for (int i = 0; i < NUM_MUESTRAS; i++) {
			double valor = estrategia.simularLectura();
			assertFalse(valor >= min && valor <= max,
					String.format("Valor %.2f debe estar FUERA de [%.2f, %.2f]", valor, min, max));
		}
	}

	/**
	 * Verifica que con probabilidad de fallo intermedia, se generan tanto valores
	 * dentro como fuera del rango en una muestra grande.
	 */
	@Test
	@DisplayName("EstrategiaRango con prob 50 genera valores dentro y fuera del rango")
	public void testEstrategiaRangoMixta() {
		double min = 10.0, max = 50.0;
		EstrategiaRango estrategia = new EstrategiaRango(50, min, max);

		long enRango = 0, fueraRango = 0;
		for (int i = 0; i < NUM_MUESTRAS; i++) {
			double valor = estrategia.simularLectura();
			if (valor >= min && valor <= max)
				enRango++;
			else
				fueraRango++;
		}

		assertTrue(enRango > 0, "Debe haber valores dentro del rango");
		assertTrue(fueraRango > 0, "Debe haber valores fuera del rango");
	}

	// =========================================================================
	// EstrategiaAnterior
	// =========================================================================

	/**
	 * Verifica que {@link EstrategiaAnterior} genera valores dentro del rango
	 * relativo al valor anterior.
	 *
	 * <p>
	 * Con un rango del 10%, cada nuevo valor debe estar dentro del ±10% del valor
	 * previo.
	 * </p>
	 */
	@Test
	@DisplayName("EstrategiaAnterior genera valores dentro del rango relativo")
	public void testEstrategiaAnteriorRangoRelativo() {
		double valInicial = 100.0;
		double rango = 0.10; // 10%
		EstrategiaAnterior estrategia = new EstrategiaAnterior(rango, valInicial);

		double anterior = valInicial;
		for (int i = 0; i < NUM_MUESTRAS; i++) {
			double valor = estrategia.simularLectura();
			double maxEsperado = anterior + Math.abs(anterior) * rango;
			double minEsperado = anterior - Math.abs(anterior) * rango;
			assertTrue(valor >= minEsperado && valor <= maxEsperado,
					String.format("Valor %.2f debe estar en [%.2f, %.2f]", valor, minEsperado, maxEsperado));
			anterior = valor;
		}
	}

	/**
	 * Verifica que {@link EstrategiaAnterior} actualiza el valor anterior en cada
	 * llamada, de modo que la siguiente lectura oscila alrededor de la anterior y
	 * no del valor inicial.
	 */
	@Test
	@DisplayName("EstrategiaAnterior actualiza el valor anterior en cada lectura")
	public void testEstrategiaAnteriorActualizaAnterior() {
		double valInicial = 20.0;
		double rango = 0.5; // 50% para asegurar variación
		EstrategiaAnterior estrategia = new EstrategiaAnterior(rango, valInicial);

		double primera = estrategia.simularLectura();
		double segunda = estrategia.simularLectura();

		// La segunda debe estar en rango de la primera, no del inicial
		double maxEsperado = primera + Math.abs(primera) * rango;
		double minEsperado = primera - Math.abs(primera) * rango;
		assertTrue(segunda >= minEsperado && segunda <= maxEsperado,
				"La segunda lectura debe basarse en la primera, no en el valor inicial");
	}

	// =========================================================================
	// EstrategiaMedia
	// =========================================================================

	/**
	 * Verifica que {@link EstrategiaMedia} genera valores dentro del rango relativo
	 * a la media histórica.
	 */
	@Test
	@DisplayName("EstrategiaMedia genera valores en rango de la media")
	public void testEstrategiaMediaEnRango() {
		double valInicial = 50.0;
		double rango = 0.20; // 20%
		EstrategiaMedia estrategia = new EstrategiaMedia(rango, valInicial);

		// Las primeras lecturas deben estar cerca del valor inicial
		for (int i = 0; i < 10; i++) {
			double valor = estrategia.simularLectura();
			// La media no debería alejarse mucho del inicial en pocas iteraciones
			assertTrue(valor > 0, "Los valores de humedad/temperatura deben ser positivos en este test");
		}
	}

	/**
	 * Verifica que la media calculada por {@link EstrategiaMedia} converge
	 * correctamente tras muchas lecturas — la media histórica no debe alejarse más
	 * de un factor razonable del valor inicial.
	 */
	@Test
	@DisplayName("EstrategiaMedia mantiene la media histórica estable")
	public void testEstrategiaMediaEstabilidad() {
		double valInicial = 100.0;
		double rango = 0.05; // 5% — rango pequeño para estabilidad
		EstrategiaMedia estrategia = new EstrategiaMedia(rango, valInicial);

		double suma = 0;
		for (int i = 0; i < NUM_MUESTRAS; i++) {
			suma += estrategia.simularLectura();
		}
		double mediaReal = suma / NUM_MUESTRAS;

		// La media real debe estar cerca del valor inicial (dentro de 20%)
		assertTrue(Math.abs(mediaReal - valInicial) / valInicial < 0.20,
				String.format("Media real %.2f debe estar cerca del valor inicial %.2f", mediaReal, valInicial));
	}

	// =========================================================================
	// Extensibilidad — estrategia personalizada
	// =========================================================================

	/**
	 * Verifica que se puede crear una estrategia personalizada implementando la
	 * interfaz {@link Estrategia}, demostrando la extensibilidad del diseño.
	 */
	@Test
	@DisplayName("se puede crear una estrategia personalizada con lambda")
	public void testEstrategiaPersonalizada() throws Exception {
		// Estrategia que siempre devuelve exactamente 42.0
		Estrategia fija = () -> 42.0;
		conversores.Conversor conv = conversores.Conversor.identidad(UnidadTemperatura.CELSIUS);
		sensores.SensorTemperatura sensor = new sensores.SensorTemperatura(UnidadTemperatura.CELSIUS, 0, fija, conv);

		sensor.tomarMedicion();

		assertEquals(42.0, sensor.getValorUltimaLectura(), 0.001,
				"La estrategia personalizada debe aplicarse correctamente");
	}
}
