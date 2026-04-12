package visualizacion;

import java.util.*;

/**
 * Clase ListaConTitulo.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class ListaConTitulo {
	
	/** Titulo de la lista. */
	private String titulo;
	
	/** Lista. */
	private List<Object> lista;
	
	/**
	 * Inicializa un nuevo objeto de la clase lista con titulo.
	 *
	 * @param titulo Titulo de la lista
	 * @param lista Lista de objetos
	 */
	public ListaConTitulo(String titulo, List<Object> lista) {
		this.titulo = titulo;
		this.lista = new ArrayList<Object>(lista);
	}

	/**
	 * Getter de titulo.
	 *
	 * @return titulo de la lista
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
