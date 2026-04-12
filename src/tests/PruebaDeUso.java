package tests;

import conversores.*;
import excepciones.*;
import sensores.*;
import sensores.estrategias.*;
import sistema.*;
import unidades.*;
import visualizacion.*;

import java.time.Duration;

/**
 * Clase PruebaDeUso.
 *
 * <p>
 * Clase ejecutable que demuestra el funcionamiento completo del sistema de
 * estación meteorológica, cubriendo las funcionalidades de los cinco apartados
 * de la práctica.
 * </p>
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class PruebaDeUso {

	public static void main(String[] args) throws Exception {

		separador("APARTADO 1 — Sensores y Estación Meteorológica");

		// Creamos la estación con un umbral de cambio brusco del 30%
		EstacionMetereologica estacion = new EstacionMetereologica("Madrid Centro", -3.7038, 40.4168, 30.0);

		// Añadimos sensores de los tres tipos con offset y estrategia por defecto
		estacion.anadirSensor(UnidadTemperatura.CELSIUS, 0.5, new EstrategiaAnterior(10, 50));
		estacion.anadirSensor(UnidadHumedad.HUMEDAD, 0.0, new EstrategiaMedia(1, 50));
		estacion.anadirSensor(UnidadPresion.HECTOPASCAL, 2.0, new EstrategiaRango(0, 500, 800));

		// Recuperamos sensores por tipo
		System.out.println("Sensores de temperatura: " + estacion.getSensoresPorTipo(SensorTemperatura.class).size());
		System.out.println("Sensores de humedad:     " + estacion.getSensoresPorTipo(SensorHumedad.class).size());
		System.out.println("Sensores de presión:     " + estacion.getSensoresPorTipo(SensorPresion.class).size());

		// Tomamos 3 mediciones puntuales
		estacion.tomarMediciones();
		estacion.tomarMediciones();
		estacion.tomarMediciones();

		System.out.println("\nEstado de la estación tras 3 mediciones:");
		System.out.println(estacion);

		// Recuperamos un sensor por ID y consultamos su historial
		Sensor<?> sensorTemp = estacion.getSensoresPorTipo(SensorTemperatura.class).get(0);
		System.out.println("\nHistorial del sensor de temperatura: " + sensorTemp.getHistorial());
		System.out.printf("  MIN: %.2f  MAX: %.2f  AVG: %.2f%n", sensorTemp.getMinimo(), sensorTemp.getMaximo(),
				sensorTemp.getMedia());

		// Comprobamos que getSensor con ID inexistente devuelve null
		Sensor<?> noExiste = estacion.getSensor("TEMP-9999");
		System.out.println("\nBúsqueda de TEMP-9999: " + (noExiste == null ? "null (correcto)" : "ERROR"));

		separador("APARTADO 2 — Estrategias de simulación");

		// EstrategiaRango: siempre en rango, nunca falla
		EstrategiaRango sinFallo = new EstrategiaRango(0, 15.0, 25.0);
		System.out.print("EstrategiaRango (prob=0, rango [15,25]): ");
		for (int i = 0; i < 5; i++)
			System.out.printf("%.1f ", sinFallo.simularLectura());
		System.out.println();

		// EstrategiaRango: siempre falla (fuera de rango)
		EstrategiaRango siempreFalla = new EstrategiaRango(100, 15.0, 25.0);
		System.out.print("EstrategiaRango (prob=100, fuera de rango): ");
		for (int i = 0; i < 5; i++)
			System.out.printf("%.1f ", siempreFalla.simularLectura());
		System.out.println();

		// EstrategiaAnterior: oscila alrededor del anterior
		EstrategiaAnterior anterior = new EstrategiaAnterior(0.1, 20.0);
		System.out.print("EstrategiaAnterior (rango=10%, inicio=20): ");
		for (int i = 0; i < 5; i++)
			System.out.printf("%.2f ", anterior.simularLectura());
		System.out.println();

		// EstrategiaMedia: oscila alrededor de la media histórica
		EstrategiaMedia media = new EstrategiaMedia(0.05, 65.0);
		System.out.print("EstrategiaMedia (rango=5%, inicio=65):    ");
		for (int i = 0; i < 5; i++)
			System.out.printf("%.2f ", media.simularLectura());
		System.out.println();

		// Añadimos un sensor con estrategia explícita
		estacion.anadirSensor(UnidadTemperatura.FAHRENHEIT, 0.0, new EstrategiaMedia(0.02, 68.0));
		System.out.println("\nSensor de temperatura en °F con EstrategiaMedia añadido.");

		separador("APARTADO 3 — Conversores y Procesadores");

		// Conversores atómicos
		Conversor ck = ConversorFactory.crear(UnidadTemperatura.CELSIUS, UnidadTemperatura.KELVIN);
		Conversor cf = ConversorFactory.crear(UnidadTemperatura.CELSIUS, UnidadTemperatura.FAHRENHEIT);
		System.out.printf("0°C -> Kelvin:     %.2f K%n", ck.aplicarConversion(0.0));
		System.out.printf("100°C -> Kelvin:   %.2f K%n", ck.aplicarConversion(100.0));
		System.out.printf("0°C -> Fahrenheit: %.2f °F%n", cf.aplicarConversion(0.0));

		// Conversor inverso
		Conversor kc = ck.obtenerConversorInverso();
		System.out.printf("273.15 K -> Celsius: %.2f °C%n", kc.aplicarConversion(273.15));

		// Conversor concatenado: Celsius -> Kelvin -> Fahrenheit
		Conversor kf = ConversorFactory.crear(UnidadTemperatura.KELVIN, UnidadTemperatura.FAHRENHEIT);
		Conversor cf2 = Conversor.concatenar(ck, kf);
		System.out.printf("100°C -> °F (concatenado): %.2f °F%n", cf2.aplicarConversion(100.0));

		// Conversor incompatible — debe lanzar excepción
		try {
			ConversorFactory.crear(UnidadTemperatura.CELSIUS, UnidadPresion.HECTOPASCAL);
			System.out.println("ERROR: debería haber lanzado excepción");
		} catch (IncompatibleUnitsException e) {
			System.out.println("IncompatibleUnitsException correctamente lanzada: " + e.getMessage());
		}

		// Sensor con conversor a Kelvin añadido a la estación
		Conversor convKelvin = ConversorFactory.crear(UnidadTemperatura.CELSIUS, UnidadTemperatura.KELVIN);
		estacion.anadirSensor(UnidadTemperatura.CELSIUS, 0.0, convKelvin);
		estacion.tomarMediciones();

		Sensor<?> sensorKelvin = estacion.getSensoresPorTipo(SensorTemperatura.class).stream()
				.filter(s -> s.getUnidadEscritura().equals(UnidadTemperatura.KELVIN)).findFirst().orElseThrow();
		System.out.printf("%nSensor °C con conversor a K — última lectura: %.2f K%n",
				sensorKelvin.getHistorial().get(0).getMedicion());

		separador("APARTADO 4 — Alertas");

		EstacionMetereologica estacionAlertas = new EstacionMetereologica("Lab Alertas", 0, 0, 10.0);

		// --- Alerta por calibración caducada ---
		estacionAlertas.anadirSensor(UnidadTemperatura.CELSIUS, 0.0, new EstrategiaAnterior(0.05, 20.0));
		Sensor<?> sensorCaducado = estacionAlertas.getSensoresPorTipo(SensorTemperatura.class).get(0);
		sensorCaducado.setDuracionCalibrado(Duration.ofNanos(1)); // caduca inmediatamente
		estacionAlertas.tomarMediciones();
		System.out.println("Alerta calibración caducada: "
				+ (estacionAlertas.getAlertas().stream().anyMatch(a -> a instanceof SensorDescalibrado)
						? "SensorDescalibrado generado"
						: "ERROR"));

		// --- Alerta por lectura fuera de rango ---
		estacionAlertas.anadirSensor(UnidadHumedad.HUMEDAD, 0.0, new EstrategiaAnterior(0, 110.0), // 110% siempre fuera
																									// de rango
				Conversor.identidad(UnidadHumedad.HUMEDAD));
		estacionAlertas.tomarMediciones();
		System.out.println("Alerta lectura fuera de rango: "
				+ (estacionAlertas.getAlertas().stream().anyMatch(a -> a instanceof SensorDescalibrado)
						? "SensorDescalibrado generado"
						: "ERROR"));

		// --- Alerta por cambio brusco ---
		double[] valorBrusco = { 500.0 };
		estacionAlertas.anadirSensor(UnidadPresion.HECTOPASCAL, 0.0, () -> valorBrusco[0],
				Conversor.identidad(UnidadPresion.HECTOPASCAL));
		estacionAlertas.tomarMediciones(); // primera lectura: referencia
		valorBrusco[0] = 1000.0; // cambio del 100% > umbral 10%
		estacionAlertas.tomarMediciones();
		System.out.println("Alerta cambio brusco: "
				+ (estacionAlertas.getAlertas().stream().anyMatch(a -> a instanceof CambioBruscoException)
						? "CambioBruscoException generado"
						: "ERROR"));

		// --- Sensor descalibrado no bloquea los demás ---
		long sensoresConLecturas = estacionAlertas.getSensores().stream().filter(s -> !s.getHistorial().isEmpty())
				.count();
		System.out.println("Sensores con lecturas (pese a alertas): " + sensoresConLecturas + "/"
				+ estacionAlertas.getSensores().size());

		// --- Mostrar alertas activas ---
		System.out.println("\nAlertas activas:");
		estacionAlertas.getAlertas().forEach(a -> System.out.println("  " + a.getMessage()));

		// --- Calibrar elimina alertas ---
		estacionAlertas.calibrarSensor(sensorCaducado.getId(), Duration.ofDays(365));
		long alertasTras = estacionAlertas.getAlertas().stream()
				.filter(a -> a.getSensor().getId().equals(sensorCaducado.getId()) && a instanceof SensorDescalibrado)
				.count();
		System.out.println("\nAlertas SensorDescalibrado del sensor calibrado tras calibrar: " + alertasTras
				+ " (esperado: 0) " + (alertasTras == 0 ? "✓" : "ERROR"));

		separador("APARTADO 5 — Visualización");

		// Estación con datos para visualizar
		EstacionMetereologica estVis = new EstacionMetereologica("Sevilla Este", -5.9845, 37.3886);
		estVis.anadirSensor(UnidadTemperatura.CELSIUS, 0.0, new EstrategiaAnterior(0.05, 28.0),
				ConversorFactory.crear(UnidadTemperatura.CELSIUS, UnidadTemperatura.KELVIN));
		estVis.anadirSensor(UnidadHumedad.HUMEDAD, 0.0, new EstrategiaAnterior(0.03, 55.0));
		estVis.tomarMediciones();
		estVis.tomarMediciones();
		estVis.tomarMediciones();

		// HTML
		Formateador html = new FormateadorHTML();
		System.out.println("--- Formato HTML ---");
		System.out.println(html.getDocumentoFormateado(estVis));

		// Markdown
		Formateador markdown = new FormateadorMarkDown();
		System.out.println("--- Formato Markdown ---");
		System.out.println(markdown.getDocumentoFormateado(estVis));

		// LaTeX
		Formateador latex = new FormateadorLatex();
		System.out.println("--- Formato LaTeX ---");
		System.out.println(latex.getDocumentoFormateado(estVis));

		// Formateador personalizado (lambda) — extensibilidad
		Formateador csv = doc -> {
			StringBuilder sb = new StringBuilder();
			sb.append("TITULO,").append(doc.getTitulo()).append("\n");
			for (String p : doc.getParrafos())
				sb.append("PARRAFO,").append(p).append("\n");
			for (ListaConTitulo l : doc.getListas()) {
				sb.append("LISTA,").append(l.getTitulo()).append("\n");
				l.getLista().forEach(o -> sb.append("ITEM,").append(o).append("\n"));
			}
			return sb.toString();
		};
		System.out.println("--- Formato CSV personalizado ---");
		System.out.println(csv.getDocumentoFormateado(estVis));

		separador("LECTURAS PERIÓDICAS");

		EstacionMetereologica estPeriodica = new EstacionMetereologica("Obs. Periódico", 0, 0);
		estPeriodica.anadirSensor(UnidadTemperatura.CELSIUS, 0.0, new EstrategiaAnterior(0.05, 20.0));
		estPeriodica.anadirSensor(UnidadHumedad.HUMEDAD, 0.0, new EstrategiaAnterior(0.02, 60.0));

		System.out.println("Iniciando 5 lecturas periódicas cada 1 segundo...");
		estPeriodica.iniciarLecturasPeriodicas(1, 5);

		// Esperamos a que terminen
		Thread.sleep(6500);

		Sensor<?> sp = estPeriodica.getSensoresPorTipo(SensorTemperatura.class).get(0);
		System.out.println("Lecturas registradas en sensor temperatura: " + sp.getHistorial().size());
		System.out.println("Historial: " + sp.getHistorial());

		separador("FIN DE LA PRUEBA DE USO");
	}

	/**
	 * Imprime un separador con título para estructurar la salida.
	 *
	 * @param titulo Título de la sección
	 */
	private static void separador(String titulo) {
		System.out.println("\n" + "=".repeat(60));
		System.out.println("  " + titulo);
		System.out.println("=".repeat(60));
	}
}
