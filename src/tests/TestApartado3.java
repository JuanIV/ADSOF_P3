package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import conversores.*;
import procesadores.*;
import sensores.*;
import sensores.estrategias.*;
import sistema.*;
import unidades.*;
import excepciones.*;

/**
 * Tests para el Apartado 3: Procesamiento de Datos.
 *
 * <p>
 * Verifica la correcta conversión de unidades (conversores atómicos, inversos y
 * concatenados), el almacenamiento del historial en el procesador, las
 * estadísticas (mínimo, máximo, media), y la compatibilidad entre unidades del
 * sensor y el conversor.
 * </p>
 *
 * @author Nombre Apellido
 */
public class TestApartado3 {

	// =========================================================================
	// Conversores atómicos
	// =========================================================================

	/**
	 * Verifica la conversión de Celsius a Kelvin.
	 */
	@Test
	@DisplayName("conversor Celsius → Kelvin es correcto")
	public void testCelsiusAKelvin() throws IncompatibleUnitsException {
		Conversor conv = ConversorFactory.crear(UnidadTemperatura.CELSIUS, UnidadTemperatura.KELVIN);
		assertEquals(273.15, conv.aplicarConversion(0.0), 0.001, "0°C debe ser 273.15 K");
		assertEquals(373.15, conv.aplicarConversion(100.0), 0.001, "100°C debe ser 373.15 K");
	}

	/**
	 * Verifica la conversión de Kelvin a Celsius.
	 */
	@Test
	@DisplayName("conversor Kelvin → Celsius es correcto")
	public void testKelvinACelsius() throws IncompatibleUnitsException {
		Conversor conv = ConversorFactory.crear(UnidadTemperatura.KELVIN, UnidadTemperatura.CELSIUS);
		assertEquals(0.0, conv.aplicarConversion(273.15), 0.001, "273.15 K debe ser 0°C");
	}

	/**
	 * Verifica la conversión de Celsius a Fahrenheit.
	 */
	@Test
	@DisplayName("conversor Celsius → Fahrenheit es correcto")
	public void testCelsiusAFahrenheit() throws IncompatibleUnitsException {
		Conversor conv = ConversorFactory.crear(UnidadTemperatura.CELSIUS, UnidadTemperatura.FAHRENHEIT);
		assertEquals(32.0, conv.aplicarConversion(0.0), 0.001, "0°C debe ser 32°F");
		assertEquals(212.0, conv.aplicarConversion(100.0), 0.001, "100°C debe ser 212°F");
	}

	/**
	 * Verifica la conversión de hectopascales a pascales.
	 */
	@Test
	@DisplayName("conversor hPa → Pa es correcto")
	public void testHPaAPascal() throws IncompatibleUnitsException {
		Conversor conv = ConversorFactory.crear(UnidadPresion.HECTOPASCAL, UnidadPresion.PASCAL);
		assertEquals(101325.0, conv.aplicarConversion(1013.25), 0.1, "1013.25 hPa debe ser 101325 Pa");
	}

	// =========================================================================
	// Conversor identidad
	// =========================================================================

	/**
	 * Verifica que el conversor identidad no modifica el valor.
	 */
	@Test
	@DisplayName("conversor identidad no modifica el valor")
	public void testConversorIdentidad() {
		Conversor conv = Conversor.identidad(UnidadTemperatura.CELSIUS);
		assertEquals(25.0, conv.aplicarConversion(25.0), 0.001, "La identidad no debe cambiar el valor");
	}

	// =========================================================================
	// Conversor inverso
	// =========================================================================

	/**
	 * Verifica que aplicar un conversor y luego su inverso recupera el valor
	 * original.
	 */
	@Test
	@DisplayName("aplicar conversor y su inverso recupera el valor original")
	public void testConversorInverso() throws IncompatibleUnitsException {
		Conversor celsiusKelvin = ConversorFactory.crear(UnidadTemperatura.CELSIUS, UnidadTemperatura.KELVIN);
		Conversor kelvinCelsius = celsiusKelvin.obtenerConversorInverso();

		double original = 25.0;
		double convertido = celsiusKelvin.aplicarConversion(original);
		double recuperado = kelvinCelsius.aplicarConversion(convertido);

		assertEquals(original, recuperado, 0.001, "El inverso debe recuperar el valor original");
	}

	// =========================================================================
	// Conversor concatenado
	// =========================================================================

	/**
	 * Verifica que concatenar Celsius→Kelvin y Kelvin→Fahrenheit produce el mismo
	 * resultado que el conversor directo Celsius→Fahrenheit.
	 */
	@Test
	@DisplayName("concatenar Celsius→Kelvin y Kelvin→Fahrenheit equivale a Celsius→Fahrenheit")
	public void testConversorConcatenado() throws IncompatibleUnitsException {
		Conversor ck = ConversorFactory.crear(UnidadTemperatura.CELSIUS, UnidadTemperatura.KELVIN);
		Conversor kf = ConversorFactory.crear(UnidadTemperatura.KELVIN, UnidadTemperatura.FAHRENHEIT);
		Conversor cf_directo = ConversorFactory.crear(UnidadTemperatura.CELSIUS, UnidadTemperatura.FAHRENHEIT);
		Conversor cf_concat = Conversor.concatenar(ck, kf);

		double celsius = 100.0;
		assertEquals(cf_directo.aplicarConversion(celsius), cf_concat.aplicarConversion(celsius), 0.001,
				"La conversión concatenada debe coincidir con la directa");
	}

	/**
	 * Verifica que concatenar conversores incompatibles lanza
	 * {@link IncompatibleUnitsException}.
	 */
	@Test
	@DisplayName("concatenar conversores incompatibles lanza IncompatibleUnitsException")
	public void testConcatenarIncompatibles() throws IncompatibleUnitsException {
		Conversor ck = ConversorFactory.crear(UnidadTemperatura.CELSIUS, UnidadTemperatura.KELVIN);
		Conversor hPaPa = ConversorFactory.crear(UnidadPresion.HECTOPASCAL, UnidadPresion.PASCAL);

		assertThrows(IncompatibleUnitsException.class, () -> Conversor.concatenar(ck, hPaPa),
				"Concatenar unidades de distinto tipo debe lanzar IncompatibleUnitsException");
	}

	// =========================================================================
	// ConversorFactory — unidades incompatibles
	// =========================================================================

	/**
	 * Verifica que intentar crear un conversor entre tipos distintos (temperatura y
	 * presión) lanza {@link IncompatibleUnitsException}.
	 */
	@Test
	@DisplayName("ConversorFactory lanza excepcion con unidades de distinto tipo")
	public void testFactoryUnidadesDistintoTipo() {
		assertThrows(IncompatibleUnitsException.class,
				() -> ConversorFactory.crear(UnidadTemperatura.CELSIUS, UnidadPresion.HECTOPASCAL),
				"No debe existir conversor entre temperatura y presión");
	}

	// =========================================================================
	// Procesador — historial y estadísticas
	// =========================================================================

	/**
	 * Verifica que el procesador almacena las mediciones en el historial y aplica
	 * la conversión correctamente.
	 */
	@Test
	@DisplayName("procesador almacena mediciones convertidas en el historial")
	public void testProcesadorHistorial() throws IncompatibleUnitsException {
		Conversor conv = ConversorFactory.crear(UnidadTemperatura.CELSIUS, UnidadTemperatura.KELVIN);
		Procesador procesador = new Procesador(conv);

		procesador.registrarMedicion(0.0); // 0°C → 273.15 K
		procesador.registrarMedicion(100.0); // 100°C → 373.15 K

		assertEquals(2, procesador.getHistorial().size(), "El historial debe tener 2 registros");
		assertEquals(273.15, procesador.getHistorial().get(0).getMedicion(), 0.01);
		assertEquals(373.15, procesador.getHistorial().get(1).getMedicion(), 0.01);
	}

	/**
	 * Verifica que el procesador calcula correctamente el mínimo, máximo y media.
	 */
	@Test
	@DisplayName("procesador calcula correctamente minimo, maximo y media")
	public void testProcesadorEstadisticas() {
		Procesador procesador = new Procesador(Conversor.identidad(UnidadTemperatura.CELSIUS));
		procesador.registrarMedicion(10.0);
		procesador.registrarMedicion(20.0);
		procesador.registrarMedicion(30.0);

		assertEquals(10.0, procesador.getMinimo(), 0.001, "Mínimo incorrecto");
		assertEquals(30.0, procesador.getMaximo(), 0.001, "Máximo incorrecto");
		assertEquals(20.0, procesador.getMedia(), 0.001, "Media incorrecta");
	}

	/**
	 * Verifica que las estadísticas del procesador son accesibles desde el sensor.
	 */
	@Test
	@DisplayName("estadísticas del procesador accesibles desde el sensor")
	public void testEstadisticasDesdeElSensor() throws IncompatibleUnitsException, SensorDescalibrado {
		Conversor conv = Conversor.identidad(UnidadTemperatura.CELSIUS);
		Estrategia fija = () -> 25.0;
		SensorTemperatura sensor = new SensorTemperatura(UnidadTemperatura.CELSIUS, 0, fija, conv);

		sensor.tomarMedicion();
		sensor.tomarMedicion();
		sensor.tomarMedicion();

		assertEquals(25.0, sensor.getMinimo(), 0.001);
		assertEquals(25.0, sensor.getMaximo(), 0.001);
		assertEquals(25.0, sensor.getMedia(), 0.001);
	}

	/**
	 * Verifica que añadir un sensor con conversor a la estación almacena las
	 * lecturas en la unidad de salida del conversor.
	 */
	@Test
	@DisplayName("sensor con conversor en la estacion almacena lecturas en unidad de salida")
	public void testSensorConConversorEnEstacion() throws IncompatibleUnitsException, DuplicatedSensorIdException {
		EstacionMetereologica estacion = new EstacionMetereologica("Test", 0, 0);
		Conversor conv = ConversorFactory.crear(UnidadTemperatura.CELSIUS, UnidadTemperatura.KELVIN);
		estacion.anadirSensor(UnidadTemperatura.CELSIUS, 0, conv);

		estacion.tomarMediciones();

		Sensor<?> sensor = estacion.getSensores().get(0);
		assertEquals(UnidadTemperatura.KELVIN, sensor.getUnidadEscritura(),
				"La unidad de escritura debe ser Kelvin tras usar el conversor");
		assertFalse(sensor.getHistorial().isEmpty(), "Debe haber lecturas en el historial");
		// Las lecturas deben estar en rango Kelvin (> 0)
		assertTrue(sensor.getHistorial().get(0).getMedicion() > 0, "Valor en Kelvin debe ser positivo");
	}
}
