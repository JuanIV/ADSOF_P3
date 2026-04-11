package tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

import excepciones.*;
import sistema.*;
import unidades.*;
import visualizacion.*;

/**
 * Tests para el Apartado 5: Visualización de Datos.
 *
 * <p>
 * Verifica que los formateadores HTML y Markdown generan la estructura correcta
 * a partir de un objeto {@link IDocumento}, y que la estación meteorológica
 * implementa correctamente la interfaz {@link IDocumento}.
 * </p>
 *
 * @author Nombre Apellido
 */
class TestApartado5 {

	private EstacionMetereologica estacion;

	/**
	 * Prepara una estación con dos sensores y algunas mediciones para cada test.
	 */
	@BeforeEach
	public void setUp() throws IncompatibleUnitsException, DuplicatedSensorIdException {
		estacion = new EstacionMetereologica("Madrid Centro", -3.7038, 40.4168);
		estacion.anadirSensor(UnidadTemperatura.CELSIUS, 0, () -> 20.5);
		estacion.anadirSensor(UnidadHumedad.HUMEDAD, 0, () -> 65.0);
		estacion.tomarMediciones();
	}

	// =========================================================================
	// IDocumento
	// =========================================================================

	/**
	 * Verifica que {@link EstacionMetereologica} implementa correctamente
	 * {@link IDocumento} devolviendo datos no nulos ni vacíos.
	 */
	@Test
	@DisplayName("EstacionMetereologica implementa IDocumento correctamente")
	public void testIDocumentoImplementado() {
		assertNotNull(estacion.getTitulo(), "getTitulo no debe ser null");
		assertNotNull(estacion.getHeaderSeccionPrincipal(), "getHeader no debe ser null");
		assertNotNull(estacion.getParrafos(), "getParrafos no debe ser null");
		assertNotNull(estacion.getListas(), "getListas no debe ser null");
		assertTrue(estacion.getParrafos().length > 0, "Debe haber al menos un párrafo");
		assertTrue(estacion.getListas().length > 0, "Debe haber al menos una lista");
	}

	/**
	 * Verifica que los párrafos contienen la información de ubicación y sensores.
	 */
	@Test
	@DisplayName("getParrafos contiene informacion de ubicacion y sensores instalados")
	public void testParrafosContienenInfoEstacion() {
		String[] parrafos = estacion.getParrafos();
		String contenido = String.join(" ", parrafos);
		assertTrue(contenido.contains("40.4168") || contenido.contains("Sensores"),
				"Los párrafos deben contener información de la estación");
	}

	/**
	 * Verifica que las listas incluyen una sección de sensores activos y otra de
	 * alertas.
	 */
	@Test
	@DisplayName("getListas incluye sensores activos y alertas")
	public void testListasIncluyenSensoresYAlertas() {
		ListaConTitulo[] listas = estacion.getListas();
		assertTrue(listas.length >= 2, "Debe haber al menos 2 listas");
		boolean haySensores = false, hayAlertas = false;
		for (ListaConTitulo l : listas) {
			if (l.getTitulo().toLowerCase().contains("sensor"))
				haySensores = true;
			if (l.getTitulo().toLowerCase().contains("alerta"))
				hayAlertas = true;
		}
		assertTrue(haySensores, "Debe haber una lista de sensores");
		assertTrue(hayAlertas, "Debe haber una lista de alertas");
	}

	// =========================================================================
	// FormateadorHTML
	// =========================================================================

	/**
	 * Verifica que el formateador HTML genera un documento con la estructura HTML
	 * básica correcta.
	 */
	@Test
	@DisplayName("FormateadorHTML genera estructura HTML válida")
	public void testFormateadorHTMLEstructura() {
		Formateador fmt = new FormateadorHTML();
		String resultado = fmt.getDocumentoFormateado(estacion);

		assertTrue(resultado.contains("<!DOCTYPE html>"), "Debe contener DOCTYPE");
		assertTrue(resultado.contains("<html"), "Debe contener etiqueta html");
		assertTrue(resultado.contains("<body>"), "Debe contener etiqueta body");
		assertTrue(resultado.contains("</html>"), "Debe cerrarse correctamente");
	}

	/**
	 * Verifica que el formateador HTML incluye el nombre de la estación en el
	 * título y en el h1.
	 */
	@Test
	@DisplayName("FormateadorHTML incluye el nombre de la estacion en titulo y h1")
	public void testFormateadorHTMLContenido() {
		Formateador fmt = new FormateadorHTML();
		String resultado = fmt.getDocumentoFormateado(estacion);

		assertTrue(resultado.contains("Madrid Centro"), "Debe contener el nombre de la estación");
		assertTrue(resultado.contains("<h1>"), "Debe contener encabezado h1");
		assertTrue(resultado.contains("<ul>"), "Debe contener listas");
	}

	// =========================================================================
	// FormateadorMarkDown
	// =========================================================================

	/**
	 * Verifica que el formateador Markdown genera la estructura correcta con
	 * títulos de nivel 1, 2 y 3.
	 */
	@Test
	@DisplayName("FormateadorMarkDown genera estructura Markdown válida")
	public void testFormateadorMarkdownEstructura() {
		Formateador fmt = new FormateadorMarkDown();
		String resultado = fmt.getDocumentoFormateado(estacion);

		assertTrue(resultado.contains("# "), "Debe contener título de nivel 1");
		assertTrue(resultado.contains("## "), "Debe contener título de nivel 2");
		assertTrue(resultado.contains("### "), "Debe contener título de nivel 3");
		assertTrue(resultado.contains("- "), "Debe contener elementos de lista");
	}

	/**
	 * Verifica que el formateador Markdown incluye el nombre de la estación.
	 */
	@Test
	@DisplayName("FormateadorMarkDown incluye el nombre de la estacion")
	public void testFormateadorMarkdownContenido() {
		Formateador fmt = new FormateadorMarkDown();
		String resultado = fmt.getDocumentoFormateado(estacion);
		assertTrue(resultado.contains("Madrid Centro"), "Debe contener el nombre de la estación");
	}

	// =========================================================================
	// Extensibilidad — nuevo formateador
	// =========================================================================

	/**
	 * Verifica la extensibilidad del sistema de formateo implementando un
	 * formateador personalizado de texto plano mediante una clase anónima.
	 */
	@Test
	@DisplayName("se puede crear un formateador personalizado implementando Formateador")
	public void testFormateadorPersonalizado() {
		Formateador txtPlano = doc -> "TITULO: " + doc.getTitulo() + "\n" + doc.getHeaderSeccionPrincipal();
		String resultado = txtPlano.getDocumentoFormateado(estacion);

		assertTrue(resultado.startsWith("TITULO:"), "El formateador personalizado debe funcionar");
		assertTrue(resultado.contains("Madrid Centro"), "Debe incluir el nombre de la estación");
	}
}