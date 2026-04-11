package visualizacion;

public class FormateadorLatex implements Formateador {

	@Override
	public String getDocumentoFormateado(IDocumento documento) {
		StringBuilder str = new StringBuilder();
		str.append("\\documentclass{article}\n"
				+ "\\usepackage[spanish]{babel}\n"
				+ "\\usepackage[utf8]{inputenc}\n");
		str.append("\\begin{document}\n");
		str.append("\\title{" + documento.getTitulo().replace("_", "\\_").replace("%", "\\%") + "}\n\\maketitle\n");
		str.append("\\section{" + documento.getHeaderSeccionPrincipal().replace("_", "\\_").replace("%", "\\%") +"}\n");
		for(String p : documento.getParrafos()) {
			str.append(p.replace("_", "\\_").replace("%", "\\%") + "\n\n");
		}
		for(ListaConTitulo l : documento.getListas()) {
			str.append("\\subsection{" + l.getTitulo().replace("_", "\\_").replace("%", "\\%") +"}\n");
			str.append("\\begin{itemize}\n");
			for(Object o : l.getLista()) {
				str.append("\\item "+ o.toString().replace("_", "\\_").replace("%", "\\%") + "\n");
			}
			str.append("\\end{itemize}\n");
		}
		str.append("\\end{document}");
		
		return str.toString();
	}

}
