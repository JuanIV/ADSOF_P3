package visualizacion;

/**
 * Clase Interface IDocumento.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public interface IDocumento {
	
	/**
	 * Getter de titulo.
	 *
	 * @return titulo del documento
	 */
	public String getTitulo();
	
	/**
	 * Getter de header seccion principal.
	 *
	 * @return header de la seccion principal
	 */
	public String getHeaderSeccionPrincipal();
	
	/**
	 * Getter de parrafos.
	 *
	 * @return parrafos del documento
	 */
	public String[] getParrafos();
	
	/**
	 * Getter de listas.
	 *
	 * @return listas del documento
	 */
	public ListaConTitulo[] getListas();
}
