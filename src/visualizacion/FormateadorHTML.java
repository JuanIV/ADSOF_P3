package visualizacion;

public class FormateadorHTML implements Formateador {

	@Override
	public String getDocumentoFormateado(IDocumento documento) {
		StringBuilder str = new StringBuilder();
		str.append("<!DOCTYPE html>\n");
		str.append("<html lang=\"es\">\n");
		str.append("<head>\n<title>" + documento.getTitulo() + "</title>\n</head>\n");
		str.append("<body>\n");
		str.append("<h1>" + documento.getHeaderSeccionPrincipal() +"</h1>\n");
		for(String p : documento.getParrafos()) {
			str.append("<p>"+p + "</p>\n");
		}
		for(ListaConTitulo l : documento.getListas()) {
			str.append("<h2>" + l.getTitulo() +"</h2>\n");
			str.append("<ul>");
			for(Object o : l.getLista()) {
				str.append("<li>"+o.toString() + "</li>\n");
			}
			str.append("</ul>");
		}
		str.append("</body>\n</html>");
		
		return str.toString();
	}

}
