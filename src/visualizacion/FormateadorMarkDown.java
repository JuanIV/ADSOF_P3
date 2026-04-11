package visualizacion;

public class FormateadorMarkDown implements Formateador {

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
