package visualizacion;

public interface IDocumento {
	public String getTitulo();
	public String getHeaderSeccionPrincipal();
	public String[] getParrafos();
	public ListaConTitulo[] getListas();
}
