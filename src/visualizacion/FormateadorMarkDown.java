package visualizacion;

/**
 * Clase FormateadorMarkDown.
 *
 * @author Juan Ibáñez y Tiago Oselka
 * @version 1.0
 */
public class FormateadorMarkDown implements Formateador {

	/**
	 * Getter de documento formateado.
	 *
	 * @param documento Documento a formatear
	 * @return documento formateado
	 */
	@Override
	public String getDocumentoFormateado(IDocumento documento) {
		StringBuilder str = new StringBuilder();
		str.append("# "+documento.getTitulo() + "\n\n");
		str.append("## " + documento.getHeaderSeccionPrincipal() +"\n\n");
		for(String p : documento.getParrafos()) {
			str.append(p + "\n\n");
		}
		for(ListaConTitulo l : documento.getListas()) {
			str.append("### " + l.getTitulo() +"\n\n");
			for(Object o : l.getLista()) {
				str.append("- "+o.toString() + "\n\n");
			}
		}
		
		return str.toString();
	}
	
}
