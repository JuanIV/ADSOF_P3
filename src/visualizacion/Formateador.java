package visualizacion;

/**
 * Clase Interface Formateador.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public interface Formateador {
	
	/**
	 * Getter de documento formateado.
	 *
	 * @param documento the documento
	 * @return documento formateado
	 */
	public String getDocumentoFormateado(IDocumento documento);
}
