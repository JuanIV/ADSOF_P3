package visualizacion;

import java.util.*;

public class ListaConTitulo {
	private String titulo;
	private List<Object> lista;
	
	public ListaConTitulo(String titulo, List<Object> lista) {
		this.titulo = titulo;
		this.lista = new ArrayList<Object>(lista);
	}

	public String getTitulo() {
		return titulo;
	}

	public List<Object> getLista() {
		return lista;
	}
}
