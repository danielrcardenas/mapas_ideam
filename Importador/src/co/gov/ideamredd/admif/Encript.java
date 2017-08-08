// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif
 * Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import org.jboss.security.auth.spi.Util;

/**
 * Métodos para encriptar y generar clave
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 *
 */
public class Encript {

	/**
	 * Encripta clave con MD5 y codificada en base 64
	 * 
	 * @param clearTextPassword
	 * @return String clave encriptada y codificada
	 */
	public static String getEncodedPassword(String clearTextPassword) {

		String passwordHash = Util.createPasswordHash("MD5", "base64", null, null, clearTextPassword);
		return passwordHash;
	}

	/**
	 * Genera una nueva clave
	 * 
	 * @return nueva clave
	 */
	public static String getNewPassword() {
		// 48 a 57 numeros
		// 65 a 90 mayusculas
		// 97 a 122 minusculas
		char tmp;
		int ascii, cont = 0;
		String pass = "";
		while (cont < 7) {
			ascii = 48 + (int) (Math.random() * 74);
			if (ascii < 58 || (ascii > 64 && ascii < 91) || ascii > 96) {
				tmp = (char) ascii;
				pass = pass + tmp;
				cont++;
			}
		}
		return pass;
	}

}
