// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.util;

/**
 * Métodos sobre la ubicación actual
 * 
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class UbicacionActual {

	// private static String base="IDEAMREDD";
	private static String	arbol		= null;
	private static String	separador	= null;

	/**
	 * Retorna el árbol actual de navegación para una página dada
	 * 
	 * @param path
	 * @return String del árbol de navegación
	 */
	public static String getArbol(String path) {
		arbol = "IDEAMREDD";
		separador = "&nbsp;&nbsp;<img src=\"images/ideam/modRuta.jpg\">&nbsp;&nbsp;";
		String secciones[] = path.split("/");
		if (path.contains("pages")) {
			separador = "&nbsp;&nbsp;<img src=\"../images/ideam/modRuta.jpg\">&nbsp;&nbsp;";
			if (path.contains("admin")) {
				separador = "&nbsp;&nbsp;<img src=\"../../images/ideam/modRuta.jpg\">&nbsp;&nbsp;";
				arbol = arbol + separador + "USUARIO ADMINISTRADOR";
			}
			else
				arbol = arbol + separador + "USUARIO REGISTRADO";
		}
		else if (path.contains("loginError")) {
			separador = "&nbsp;&nbsp;<img src=\"../images/ideam/modRuta.jpg\">&nbsp;&nbsp;";
			arbol = arbol + separador + "PUBLICO";
		}
		else
			arbol = arbol + separador + "PUBLICO";
		if (path.contains("inicio")) {
			arbol = arbol + separador + "INICIO";
		}
		else {
			if (secciones[secciones.length - 1].toLowerCase().contains("usuario") || secciones[secciones.length - 1].toLowerCase().contains("clave"))
				arbol = arbol + separador + "USUARIOS" + separador + secciones[secciones.length - 1].toUpperCase().substring(0, (secciones[secciones.length - 1].lastIndexOf(".")));
			else if (secciones[secciones.length - 1].toLowerCase().contains("parcela"))
				arbol = arbol + separador + "PARCELAS" + separador + secciones[secciones.length - 1].toUpperCase().substring(0, (secciones[secciones.length - 1].lastIndexOf(".")));
			else if (secciones[secciones.length - 1].toLowerCase().contains("proyecto")) arbol = arbol + separador + "PROYECTOS" + separador + secciones[secciones.length - 1].toUpperCase().substring(0, (secciones[secciones.length - 1].lastIndexOf(".")));
		}
		return arbol;
	}

}
