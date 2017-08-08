package co.gov.ideamredd.ui.dao;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class Security {
	
	public static String encriptar(String cadena) {
		StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
		s.setPassword("uniquekey");
		return s.encrypt(cadena);
	}

	public static String desencriptar(String cadena) {
		StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
		s.setPassword("uniquekey");
		String contenido = "";
		for (int i = 0; i < cadena.length(); i++) {
			contenido += (cadena.charAt(i) == ' ') ? "+" : cadena.charAt(i);
		}// fin del for
			// cadena.replace("/\s/g","_");F
		String devuelve = "";
		try {
			devuelve = s.decrypt(contenido);
		} catch (Exception e) {
		}
		return devuelve;
	}

}
