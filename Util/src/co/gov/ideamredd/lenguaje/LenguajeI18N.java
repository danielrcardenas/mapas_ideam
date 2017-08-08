// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.lenguaje;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Clase principal de traducción
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class LenguajeI18N {

	private String			lenguaje;
	private String			pais;
	private ResourceBundle	mensajes;

	/**
	 * Obtiene un diccionario en el idioma establecido
	 * 
	 * @return diccionario resourcebundle a partir del archivo de properties de la locale especificada
	 */
	public ResourceBundle obtenerMensajeIdioma() {
		Locale locale = new Locale(lenguaje, pais);
		mensajes = ResourceBundle.getBundle("co/gov/ideamredd/lenguaje/Idioma", locale);
		return mensajes;
	}

	/**
	 * Método main principal
	 * 
	 * @param a
	 */
	public static void main(String[] a) {
		LenguajeI18N l = new LenguajeI18N();
		l.setLenguaje("es");
		l.setPais("CO");
		ResourceBundle msj = l.obtenerMensajeIdioma();
		System.out.println(msj.getString("home.carbono"));
	}

	/**
	 * Retorna el código del lenguaje
	 * 
	 * @return String del código del lenguaje
	 */
	public String getLenguaje() {
		return lenguaje;
	}

	/**
	 * Establece el código del lenguaje
	 * 
	 * @param lenguaje
	 */
	public void setLenguaje(String lenguaje) {
		this.lenguaje = lenguaje;
	}

	/**
	 * Retorna el código del país
	 * 
	 * @return String del código del país
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * Establece el código del país
	 * 
	 * @param pais
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

}
