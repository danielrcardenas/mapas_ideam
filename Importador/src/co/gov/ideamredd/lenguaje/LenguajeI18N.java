// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete de traducción
 */
package co.gov.ideamredd.lenguaje;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Métodos para poder traducir al inglés
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 *
 */
public class LenguajeI18N {

	private String lenguaje;
	private String pais;
	private ResourceBundle mensajes;
	public String ultimo_error = "";

	/**
	 * Obtiene un mensaje en cierto idioma
	 * 
	 * @return
	 */
	public ResourceBundle obtenerMensajeIdioma() {
		mensajes = null;
		try {
			Locale locale = new Locale(lenguaje, pais);
			mensajes = ResourceBundle.getBundle("co/gov/ideamredd/lenguaje/Idioma", locale);
			//mensajes = ResourceBundle.getBundle("Idioma", locale);
		}
		catch (Exception e) {
			ultimo_error = e.toString();
		}
		return mensajes;
	}
	
	/**
	 * Método main de iniciación de la clase
	 * 
	 * @param a
	 */
	public static void main(String[] a){
		LenguajeI18N l = new LenguajeI18N();
		l.setLenguaje("es");
		l.setPais("CO");
		ResourceBundle msj = l.obtenerMensajeIdioma();
		System.out.println(msj.getString("home.carbono"));
	}

	/**
	 * Retorna el lenguaje
	 * 
	 * @return String de lenguaje
	 */
	public String getLenguaje() {
		return lenguaje;
	}

	/**
	 * Establece el lenguaje
	 * 
	 * @param lenguaje
	 */
	public void setLenguaje(String lenguaje) {
		this.lenguaje = lenguaje;
	}

	/**
	 * Retorna el país
	 * 
	 * @return String del país
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * Establece el código de país
	 * 
	 * @param pais
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

}
