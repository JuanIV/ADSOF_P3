package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import conversores.*;
import excepciones.*;
import sensores.*;
import sensores.estrategias.*;
import sistema.*;
import unidades.*;

import java.time.Duration;

/**
 * Tests para el Apartado 4: Alertas.
 *
 * <p>
 * Verifica la generación y almacenamiento de alertas por sensor descalibrado
 * (calibración caducada o lectura fuera de rango), alertas por cambio brusco,
 * la eliminación de alertas al calibrar, y que los sensores descalibrados no
 * bloquean la medición de los demás.
 * </p>
 *
 * @author Nombre Apellido
 */
public class TestApartado4 {

	/** Estrategia que siempre devuelve el valor dado. */
	private static Estrategia fija(double valor) {
		return () -> valor;
	}

	// =========================================================================
	// Alertas por sensor descalibrado — calibración caducada
	// =========================================================================

	/**
	 * Verifica que medir con un sensor cuya calibración ha caducado genera una
	 * alerta de tipo {@link SensorDescalibrado} en la estación.
	 */
	@Test
	@DisplayName("sensor con calibración caducada genera alerta SensorDescalibrado")
	public void testAlertaSensorCalibradoCaducado() throws IncompatibleUnitsException, DuplicatedSensorIdException {
		EstacionMetereologica estacion = new EstacionMetereologica("Test", 0, 0);
		estacion.anadirSensor(UnidadTemperatura.CELSIUS, 0);

		// Forzar calibración caducada
		Sensor sensor = estacion.getSensores().get(0);
		sensor.setDuracionCalibrado(Duration.ofNanos(1));

		estacion.tomarMediciones();

		assertFalse(estacion.getAlertas().isEmpty(), "Debe haber al menos una alerta");
		assertTrue(estacion.getAlertas().stream().anyMatch(a -> a instanceof SensorDescalibrado),
				"Debe haber una alerta de tipo SensorDescalibrado");
	}

	/**
	 * Verifica que medir con un valor fuera del rango válido genera una alerta de
	 * tipo {@link SensorDescalibrado}.
	 */
	@Test
	@DisplayName("lectura fuera de rango genera alerta SensorDescalibrado")
	public void testAlertaLecturaFueraDeRango() throws IncompatibleUnitsException, DuplicatedSensorIdException {
		EstacionMetereologica estacion = new EstacionMetereologica("Test", 0, 0);
		// Humedad válida: 0-100%. Valor 105 está fuera de rango
		Conversor conv = Conversor.identidad(UnidadHumedad.HUMEDAD);
		estacion.anadirSensor(UnidadHumedad.HUMEDAD, 0, fija(105.0), conv);

		estacion.tomarMediciones();

		assertTrue(estacion.getAlertas().stream().anyMatch(a -> a instanceof SensorDescalibrado),
				"Una lectura de 105% debe generar alerta de descalibrado");
	}

	// =========================================================================
	// Alertas por cambio brusco
	// =========================================================================

	/**
	 * Verifica que un cambio superior al umbral configurado genera una alerta de
	 * tipo {@link CambioBruscoException}.
	 *
	 * <p>
	 * Se usa un umbral del 10% y un cambio del 100% (de 10 a 20).
	 * </p>
	 */
	@Test
	@DisplayName("cambio brusco genera alerta CambioBruscoException")
	public void testAlertaCambioBrusco()
			throws IncompatibleUnitsException, DuplicatedSensorIdException, SensorDescalibrado {
		EstacionMetereologica estacion = new EstacionMetereologica("Test", 0, 0, 10.0); // umbral 10%
		double[] valor = { 500.0 }; // valor mutable para la lambda
		Conversor conv = Conversor.identidad(UnidadPresion.HECTOPASCAL);
		estacion.anadirSensor(UnidadPresion.HECTOPASCAL, 0, () -> valor[0], conv);

		// Primera lectura: establece el valor de referencia
		estacion.tomarMediciones();
		// Segunda lectura: cambio del 100% (500 → 1000), supera el umbral del 10%
		valor[0] = 1000.0;
		estacion.tomarMediciones();

		assertTrue(estacion.getAlertas().stream().anyMatch(a -> a instanceof CambioBruscoException),
				"Un cambio del 100% con umbral del 10% debe generar alerta de cambio brusco");
	}

	/**
	 * Verifica que un cambio menor que el umbral no genera alerta de cambio brusco.
	 */
	@Test
	@DisplayName("cambio dentro del umbral no genera alerta CambioBruscoException")
	public void testSinAlertaCambioDentroUmbral() throws IncompatibleUnitsException, DuplicatedSensorIdException {
		EstacionMetereologica estacion = new EstacionMetereologica("Test", 0, 0, 50.0); // umbral 50%
		double[] valor = { 500.0 };
		Conversor conv = Conversor.identidad(UnidadPresion.HECTOPASCAL);
		estacion.anadirSensor(UnidadPresion.HECTOPASCAL, 0, () -> valor[0], conv);

		estacion.tomarMediciones();
		valor[0] = 510.0; // cambio del 2%, muy por debajo del 50%
		estacion.tomarMediciones();

		assertTrue(estacion.getAlertas().stream().noneMatch(a -> a instanceof CambioBruscoException),
				"Un cambio del 2% con umbral del 50% no debe generar alerta de cambio brusco");
	}

	// =========================================================================
	// Sensor descalibrado no bloquea los demás
	// =========================================================================

	/**
	 * Verifica que cuando un sensor está descalibrado, los demás sensores siguen
	 * tomando mediciones correctamente.
	 */
	@Test
	@DisplayName("sensor descalibrado no impide medir los demás sensores")
	public void testSensorDescalibradoNoBloqueaOtros() throws IncompatibleUnitsException, DuplicatedSensorIdException {
		EstacionMetereologica estacion = new EstacionMetereologica("Test", 0, 0);

		// Sensor que se descalibrará (fuera de rango)
		Conversor conv1 = Conversor.identidad(UnidadHumedad.HUMEDAD);
		estacion.anadirSensor(UnidadHumedad.HUMEDAD, 0, fija(200.0), conv1); // 200% fuera de rango

		// Sensor normal
		Conversor conv2 = Conversor.identidad(UnidadTemperatura.CELSIUS);
		estacion.anadirSensor(UnidadTemperatura.CELSIUS, 0, fija(20.0), conv2);

		estacion.tomarMediciones();

		Sensor sensorNormal = estacion.getSensoresPorTipo(SensorTemperatura.class).get(0);
		assertFalse(sensorNormal.getHistorial().isEmpty(),
				"El sensor normal debe tener lecturas aunque otro esté descalibrado");
	}

	// =========================================================================
	// Calibración limpia las alertas
	// =========================================================================

	/**
	 * Verifica que calibrar un sensor descalibrado elimina sus alertas de
	 * descalibrado de la estación.
	 */
	@Test
	@DisplayName("calibrar un sensor elimina sus alertas de descalibrado")
	public void testCalibracionEliminaAlertas() throws IncompatibleUnitsException, DuplicatedSensorIdException {
		EstacionMetereologica estacion = new EstacionMetereologica("Test", 0, 0);
		Conversor conv = Conversor.identidad(UnidadTemperatura.CELSIUS);
		estacion.anadirSensor(UnidadTemperatura.CELSIUS, 0, fija(20.0), conv);

		// Forzar descalibración
		Sensor sensor = estacion.getSensores().get(0);
		sensor.setDuracionCalibrado(Duration.ofNanos(1));
		estacion.tomarMediciones();

		assertFalse(estacion.getAlertas().isEmpty(), "Debe haber alertas antes de calibrar");

		// Calibrar
		estacion.calibrarSensor(sensor.getId());

		assertTrue(estacion.getAlertas().stream().noneMatch(a -> a instanceof SensorDescalibrado),
				"Las alertas de descalibrado deben eliminarse tras calibrar");
	}
}
