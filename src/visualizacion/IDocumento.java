package visualizacion;

// TODO: Auto-generated Javadoc
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
	 * @return titulo
	 */
	public String getTitulo();
	
	/**
	 * Getter de header seccion principal.
	 *
	 * @return header seccion principal
	 */
	public String getHeaderSeccionPrincipal();
	
	/**
	 * Getter de parrafos.
	 *
	 * @return parrafos
	 */
	public String[] getParrafos();
	
	/**
	 * Getter de listas.
	 *
	 * @return listas
	 */
	public ListaConTitulo[] getListas();
}
