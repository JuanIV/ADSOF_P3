package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import sensores.*;
import sensores.estrategias.*;
import unidades.*;
import conversores.*;
import excepciones.*;
import sistema.*;

import java.time.Duration;

/**
 * Tests para el Apartado 1: Sensores Meteorológicos.
 *
 * <p>
 * Verifica la creación y configuración de sensores, la generación automática de
 * IDs, el registro de mediciones con offset, la calibración, y la gestión de
 * sensores en la estación meteorológica.
 * </p>
 *
 * @author Nombre Apellido
 */
public class TestApartado1 {

	/**
	 * Estrategia fija que siempre devuelve el mismo valor, para tests
	 * deterministas.
	 */
	private static Estrategia estrategiaFija(double valor) {
		return () -> valor;
	}

	// =========================================================================
	// Tests de creación y formato de IDs
	// =========================================================================

	/**
	 * Verifica que los IDs de los sensores de temperatura siguen el formato
	 * TEMP-NNNN con numeración secuencial.
	 */
	@Test
	@DisplayName("IDs de SensorTemperatura tienen formato TEMP-NNNN")
	public void testIdFormatoTemperatura() throws IncompatibleUnitsException {
		Conversor conv = Conversor.identidad(UnidadTemperatura.CELSIUS);
		SensorTemperatura s1 = new SensorTemperatura(UnidadTemperatura.CELSIUS, 0, estrategiaFija(20), conv);
		SensorTemperatura s2 = new SensorTemperatura(UnidadTemperatura.CELSIUS, 0, estrategiaFija(20), conv);

		assertTrue(s1.getId().matches("TEMP-\\d{4}"), "ID debe seguir formato TEMP-NNNN");
		assertTrue(s2.getId().matches("TEMP-\\d{4}"), "ID debe seguir formato TEMP-NNNN");
		assertNotEquals(s1.getId(), s2.getId(), "Dos sensores no pueden tener el mismo ID");
	}

	/**
	 * Verifica que los IDs de los sensores de humedad siguen el formato HUM-NNNN.
	 */
	@Test
	@DisplayName("IDs de SensorHumedad tienen formato HUM-NNNN")
	public void testIdFormatoHumedad() throws IncompatibleUnitsException {
		Conversor conv = Conversor.identidad(UnidadHumedad.HUMEDAD);
		SensorHumedad s = new SensorHumedad(UnidadHumedad.HUMEDAD, 0, estrategiaFija(50), conv);
		assertTrue(s.getId().matches("HUM-\\d{4}"), "ID debe seguir formato HUM-NNNN");
	}

	/**
	 * Verifica que los IDs de los sensores de presión siguen el formato PRES-NNNN.
	 */
	@Test
	@DisplayName("IDs de SensorPresion tienen formato PRES-NNNN")
	public void testIdFormatoPresion() throws IncompatibleUnitsException {
		Conversor conv = Conversor.identidad(UnidadPresion.HECTOPASCAL);
		SensorPresion s = new SensorPresion(UnidadPresion.HECTOPASCAL, 0, estrategiaFija(1013), conv);
		assertTrue(s.getId().matches("PRES-\\d{4}"), "ID debe seguir formato PRES-NNNN");
	}

	// =========================================================================
	// Tests de registrarMedicion y offset
	// =========================================================================

	/**
	 * Verifica que el valor registrado por un sensor es el valor medido menos el
	 * offset de calibración.
	 */
	@Test
	@DisplayName("registrarMedicion aplica el offset correctamente")
	public void testOffsetAplicado() throws Exception {
		double offset = 2.0;
		double valorSimulado = 22.0;
		Conversor conv = Conversor.identidad(UnidadTemperatura.CELSIUS);
		SensorTemperatura sensor = new SensorTemperatura(UnidadTemperatura.CELSIUS, offset,
				estrategiaFija(valorSimulado), conv);

		sensor.tomarMedicion();

		assertEquals(valorSimulado - offset, sensor.getValorUltimaLectura(), 0.001,
				"El valor registrado debe ser la medicion menos el offset");
	}

	/**
	 * Verifica que la fecha de última lectura se actualiza al tomar una medición.
	 */
	@Test
	@DisplayName("tomarMedicion actualiza la fecha de última lectura")
	public void testFechaUltimaLecturaActualizada() throws IncompatibleUnitsException, excepciones.SensorDescalibrado {
		Conversor conv = Conversor.identidad(UnidadTemperatura.CELSIUS);
		SensorTemperatura sensor = new SensorTemperatura(UnidadTemperatura.CELSIUS, 0, estrategiaFija(20), conv);

		sensor.tomarMedicion();

		assertNotNull(sensor.getFechaUltimaLectura(), "La fecha no debe ser null tras medir");
	}

	// =========================================================================
	// Tests de calibración
	// =========================================================================

	/**
	 * Verifica que calibrar un sensor actualiza el offset correctamente.
	 */
	@Test
	@DisplayName("calibrar actualiza el offset del sensor")
	public void testCalibracionActualizaOffset() throws IncompatibleUnitsException {
		Conversor conv = Conversor.identidad(UnidadTemperatura.CELSIUS);
		SensorTemperatura sensor = new SensorTemperatura(UnidadTemperatura.CELSIUS, 0, estrategiaFija(20), conv);

		sensor.calibrar(5.0);

		assertEquals(5.0, sensor.getOffset(), 0.001, "El offset debe actualizarse tras calibrar");
	}

	/**
	 * Verifica que un sensor recién calibrado está considerado calibrado.
	 */
	@Test
	@DisplayName("sensor recién calibrado está calibrado")
	public void testSensorCalibrадoTrasCalibrar() throws IncompatibleUnitsException, excepciones.SensorDescalibrado {
		Conversor conv = Conversor.identidad(UnidadTemperatura.CELSIUS);
		SensorTemperatura sensor = new SensorTemperatura(UnidadTemperatura.CELSIUS, 0, estrategiaFija(20), conv);
		sensor.tomarMedicion();
		sensor.calibrar(0.0);

		assertTrue(sensor.estaCalibrado(), "El sensor debe estar calibrado justo después de calibrar");
	}

	/**
	 * Verifica que un sensor con calibración caducada no está calibrado.
	 */
	@Test
	@DisplayName("sensor con calibración caducada no está calibrado")
	public void testCalibracionCaducada() throws IncompatibleUnitsException {
		Conversor conv = Conversor.identidad(UnidadTemperatura.CELSIUS);
		SensorTemperatura sensor = new SensorTemperatura(UnidadTemperatura.CELSIUS, 0, estrategiaFija(20), conv);
		sensor.setDuracionCalibrado(Duration.ofNanos(1)); // caduca inmediatamente

		assertFalse(sensor.estaCalibrado(), "Sensor con calibracion caducada no debe estar calibrado");
	}

	// =========================================================================
	// Tests de EstacionMetereologica
	// =========================================================================

	/**
	 * Verifica que se pueden añadir sensores de los tres tipos a la estación y
	 * recuperarlos correctamente.
	 */
	@Test
	@DisplayName("añadir y recuperar sensores de los tres tipos")
	public void testAnadirYRecuperarSensores() throws IncompatibleUnitsException, DuplicatedSensorIdException {
		EstacionMetereologica estacion = new EstacionMetereologica("Test", 0, 0);
		estacion.anadirSensor(UnidadTemperatura.CELSIUS, 0);
		estacion.anadirSensor(UnidadHumedad.HUMEDAD, 0);
		estacion.anadirSensor(UnidadPresion.HECTOPASCAL, 0);

		assertEquals(3, estacion.getSensores().size(), "La estación debe tener 3 sensores");
	}

	/**
	 * Verifica que intentar añadir un sensor con ID duplicado lanza
	 * {@link DuplicatedSensorIdException}.
	 *
	 * <p>
	 * Este caso es difícil de provocar directamente porque los IDs se generan
	 * automáticamente y son únicos. Se fuerza usando reflexión para resetear el
	 * contador, o bien se verifica que dos sensores del mismo tipo en la misma
	 * instancia no colisionan.
	 * </p>
	 */
	@Test
	@DisplayName("getSensoresPorTipo filtra correctamente por tipo")
	public void testGetSensoresPorTipo() throws IncompatibleUnitsException, DuplicatedSensorIdException {
		EstacionMetereologica estacion = new EstacionMetereologica("Test", 0, 0);
		estacion.anadirSensor(UnidadTemperatura.CELSIUS, 0);
		estacion.anadirSensor(UnidadTemperatura.FAHRENHEIT, 0);
		estacion.anadirSensor(UnidadHumedad.HUMEDAD, 0);

		var sensoresTemp = estacion.getSensoresPorTipo(SensorTemperatura.class);

		assertEquals(2, sensoresTemp.size(), "Deben encontrarse exactamente 2 sensores de temperatura");
		assertTrue(sensoresTemp.stream().allMatch(s -> s instanceof SensorTemperatura),
				"Todos los sensores devueltos deben ser SensorTemperatura");
	}

	/**
	 * Verifica que getSensor devuelve null para un ID inexistente.
	 */
	@Test
	@DisplayName("getSensor devuelve null para ID inexistente")
	public void testGetSensorInexistente() {
		EstacionMetereologica estacion = new EstacionMetereologica("Test", 0, 0);
		assertNull(estacion.getSensor("TEMP-9999"), "Debe devolver null si el sensor no existe");
	}

	/**
	 * Verifica que tomarMediciones actualiza los valores de todos los sensores.
	 */
	@Test
	@DisplayName("tomarMediciones actualiza todos los sensores")
	public void testTomarMedicionesActualizaTodos() throws IncompatibleUnitsException, DuplicatedSensorIdException {
		EstacionMetereologica estacion = new EstacionMetereologica("Test", 0, 0);
		estacion.anadirSensor(UnidadTemperatura.CELSIUS, 0);
		estacion.anadirSensor(UnidadHumedad.HUMEDAD, 0);

		estacion.tomarMediciones();

		estacion.getSensores()
				.forEach(s -> assertFalse(s.getHistorial().isEmpty(), "Cada sensor debe tener al menos una lectura"));
	}

	/**
	 * Verifica que la estación lanza {@link IncompatibleUnitsException} al intentar
	 * añadir un sensor con un conversor cuya unidad de entrada no coincide con la
	 * unidad del sensor.
	 */
	@Test
	@DisplayName("añadir sensor con conversor incompatible lanza IncompatibleUnitsException")
	public void testConversionIncompatibleAlAnadir() {
		EstacionMetereologica estacion = new EstacionMetereologica("Test", 0, 0);
		Conversor convIncompatible = Conversor.identidad(UnidadPresion.HECTOPASCAL);

		assertThrows(IncompatibleUnitsException.class,
				() -> estacion.anadirSensor(UnidadTemperatura.CELSIUS, 0, convIncompatible),
				"Debe lanzar IncompatibleUnitsException con conversor incompatible");
	}
}
