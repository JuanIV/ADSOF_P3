package visualizacion;

import java.util.*;

// TODO: Auto-generated Javadoc
/**
 * Clase Class ListaConTitulo.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class ListaConTitulo {
	
	/** The titulo. */
	private String titulo;
	
	/** The lista. */
	private List<Object> lista;
	
	/**
	 * Inicializa un nuevo objeto de la clase lista con titulo.
	 *
	 * @param titulo the titulo
	 * @param lista the lista
	 */
	public ListaConTitulo(String titulo, List<Object> lista) {
		this.titulo = titulo;
		this.lista = new ArrayList<Object>(lista);
	}

	/**
	 * Getter de titulo.
	 *
	 * @return titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * Getter de lista.
	 *
	 * @return lista
	 */
	public List<Object> getLista() {
		return lista;
	}
}
