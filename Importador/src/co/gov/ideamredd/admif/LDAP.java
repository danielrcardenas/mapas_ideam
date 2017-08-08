// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif
 * Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import java.security.GeneralSecurityException;

import javax.net.ssl.SSLSocketFactory;

import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.RootDSE;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.SimpleBindRequest;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

/**
 * Métodos para administrar la comunicación de autenticación LDAP
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 *
 */
public class LDAP {
	
	public static String depuracion = "";
	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	public String ultimoerror = "";
	public String clase = "LDAP";
	
	//Auxiliar aux = new Auxiliar();
	
	String servidorLDAP = "";
	int puertoLDAP = 389;
	String usuarioBuscadorLDAP = "";
	String claveUsuarioBuscadorLDAP = "";
	String baseBusquedaLDAP = "";
	String atributoBusquedaLDAP = "";
	
	/**
	 * Establece el host
	 * 
	 * @param v
	 */
	public void set_Host(String v) {
		this.servidorLDAP = v;
	}

	/**
	 * Establece el puerto
	 * 
	 * @param v
	 */
	public void set_puertoLDAP(int v) {
		this.puertoLDAP = v;
	}
	
	/**
	 * Establece el ID del usuario buscador
	 * 
	 * @param v
	 */
	public void set_ID(String v) {
		this.usuarioBuscadorLDAP = v;
	}

	/**
	 * Establece el password del usuario buscador
	 * 
	 * @param v
	 */
	public void set_Pass(String v) {
		this.claveUsuarioBuscadorLDAP = v;
	}
	
	/**
	 * Establece la base de búsqueda en el árbol del directorio LDAP
	 * 
	 * @param v
	 */
	public void set_SearchBase(String v) {
		this.baseBusquedaLDAP = v;
	}

	/**
	 * Establece el atributo de búsqueda LDAP
	 * 
	 * @param v
	 */
	public void set_atributoBusquedaLDAP(String v) {
		this.atributoBusquedaLDAP = v;
	}
	
	/**
	 * Retorna el host
	 * 
	 * @param v
	 * @return String host LDAP
	 */
	public String get_Host(String v) {
		return this.servidorLDAP;
	}
	
	/**
	 * Retorna el puerto
	 * 
	 * @param v
	 * @return String puerto LDAP
	 */
	public int get_puertoLDAP(int v) {
		return this.puertoLDAP;
	}
	
	/**
	 * Retorna el ID del usuario buscador
	 * 
	 * @param v
	 * @return String ID del usuario buscador LDAP
	 */
	public String get_ID(String v) {
		return this.usuarioBuscadorLDAP;
	}
	
	/**
	 * Retorna la clave del usuario buscador LDAP
	 * 
	 * @param v
	 * @return String clave del usuario
	 */
	public String get_Pass(String v) {
		return this.claveUsuarioBuscadorLDAP;
	}
	
	/**
	 * Retorna la base de búsqueda LDAP en el árbol del directorio
	 * 
	 * @param v
	 * @return String base de búsqueda LDAP
	 */
	public String get_SearchBase(String v) {
		return this.baseBusquedaLDAP;
	}
	
	/**
	 * Retorna el atributo usado para buscar en LDAP
	 * 
	 * @param v
	 * @return String nombre del atributo
	 */
	public String get_atributoBusquedaLDAP(String v) {
		return this.atributoBusquedaLDAP;
	}
	
	/**
	 * Autentica al usuario
	 * 
	 * @param usuarioLDAP
	 * @param claveUsuarioLDAP
	 * @return String[] con el código y mensaje resultado de autenticación
	 */
	public String [] autenticar(String usuarioLDAP, String claveUsuarioLDAP) {
		
		String debugstr = "";
		boolean ok_connect = false;
		boolean ok_bind = false;
		boolean ok_search = false;
		
		String [] a_resultado = new String[2];
		a_resultado = new String[]{"0", "Retorno inicial."};

		try {
			BD dbREDD = new BD();
			if (dbREDD != null) {
				if (!Auxiliar.tieneAlgo(servidorLDAP)) {
				    Auxiliar.mensajeImpersonal("nota", "Obteniendo  servidorLDAP... ");
					servidorLDAP = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='servidorLDAP'", "");
				}
				if (!Auxiliar.tieneAlgo(servidorLDAP)) {
					debugstr = "Falto el dato servidorLDAP";
				    Auxiliar.mensajeImpersonal("advertencia", debugstr);
					a_resultado = new String[]{"0", debugstr};
					return a_resultado;
				}
			    Auxiliar.mensajeImpersonal("nota", "servidorLDAP=" + servidorLDAP);
				
			    Auxiliar.mensajeImpersonal("nota", "Obteniendo  puertoLDAP_bd... ");
				String puertoLDAP_bd = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='puertoLDAP'", "389");
				if (!Auxiliar.esPuerto(puertoLDAP_bd)) {
					puertoLDAP = 389;
				}
				else {
					puertoLDAP = Integer.valueOf(puertoLDAP_bd);
				}
			    Auxiliar.mensajeImpersonal("nota", "PuertoLDAP=" + String.valueOf(puertoLDAP));

				if (!Auxiliar.tieneAlgo(usuarioBuscadorLDAP)) {
				    Auxiliar.mensajeImpersonal("nota", "Obteniendo  puertoLDAP_bd... ");
					usuarioBuscadorLDAP = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='usuarioBuscadorLDAP'", "");
				}
				if (!Auxiliar.tieneAlgo(usuarioBuscadorLDAP)) {
					debugstr = "Falto el dato usuarioBuscadorLDAP";
				    Auxiliar.mensajeImpersonal("advertencia", debugstr);
					a_resultado = new String[]{"0", debugstr};
					return a_resultado;
				}
			    Auxiliar.mensajeImpersonal("nota", "usuarioBuscadorLDAP=" + usuarioBuscadorLDAP);

				if (!Auxiliar.tieneAlgo(claveUsuarioBuscadorLDAP)) {
				    Auxiliar.mensajeImpersonal("nota", "Obteniendo  claveUsuarioBuscadorLDAP... ");
					claveUsuarioBuscadorLDAP = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='claveUsuarioBuscadorLDAP'", "");
				}
				if (!Auxiliar.tieneAlgo(claveUsuarioBuscadorLDAP)) {
					debugstr = "Falto el dato claveUsuarioBuscadorLDAP";
				    Auxiliar.mensajeImpersonal("advertencia", debugstr);
					a_resultado = new String[]{"0", debugstr};
					return a_resultado;
				}
			    Auxiliar.mensajeImpersonal("nota", "claveUsuarioBuscadorLDAP=" + claveUsuarioBuscadorLDAP);

				if (!Auxiliar.tieneAlgo(atributoBusquedaLDAP)) {
				    Auxiliar.mensajeImpersonal("nota", "Obteniendo  atributoBusquedaLDAP... ");
					atributoBusquedaLDAP = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='atributoBusquedaLDAP'", "");
				}
				if (!Auxiliar.tieneAlgo(atributoBusquedaLDAP)) {
					debugstr = "Falto el dato atributoBusquedaLDAP";
				    Auxiliar.mensajeImpersonal("advertencia", debugstr);
					a_resultado = new String[]{"0", debugstr};
					return a_resultado;
				}
			    Auxiliar.mensajeImpersonal("nota", "atributoBusquedaLDAP=" + atributoBusquedaLDAP);

				if (!Auxiliar.tieneAlgo(baseBusquedaLDAP)) {
				    Auxiliar.mensajeImpersonal("nota", "Obteniendo  baseBusquedaLDAP... ");
					baseBusquedaLDAP = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='baseBusquedaLDAP'", "");
				}
				if (!Auxiliar.tieneAlgo(baseBusquedaLDAP)) {
					debugstr = "Falto el dato baseBusquedaLDAP";
				    Auxiliar.mensajeImpersonal("advertencia", debugstr);
					a_resultado = new String[]{"0", debugstr};
					return a_resultado;
				}
			    Auxiliar.mensajeImpersonal("nota", "baseBusquedaLDAP=" + baseBusquedaLDAP);
			}
			if (dbREDD != null) dbREDD.desconectarse();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			debugstr = "Excepción:" + e1.toString();
		    Auxiliar.mensajeImpersonal("error", debugstr);
			a_resultado = new String[]{"-1", debugstr};
			return a_resultado;
		} catch (Exception e1) {
			e1.printStackTrace();
			debugstr = "Excepción:" + e1.toString();
		    Auxiliar.mensajeImpersonal("error", debugstr);
			a_resultado = new String[]{"-1", debugstr};
			return a_resultado;
		}

		
		SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager()); 
		SSLSocketFactory sslSocketFactory = null;
		try {
		    sslSocketFactory = sslUtil.createSSLSocketFactory();
		}
		catch (GeneralSecurityException e1) {
		    e1.printStackTrace();
		}
		
		LDAPConnection adminConnection = new LDAPConnection(sslSocketFactory);
		try {
		    Auxiliar.mensajeImpersonal("nota", "Intentando conectarse al servidor LDAP " + servidorLDAP + ":" + puertoLDAP);
		    adminConnection = new LDAPConnection(servidorLDAP, puertoLDAP);
		    Auxiliar.mensajeImpersonal("confirmacion", "Conexión LDAP establecida con: " + servidorLDAP + ":" + puertoLDAP);
		    ok_connect = true;
		}
		catch (LDAPException e) {
		    e.printStackTrace();
			a_resultado = new String[]{"-1", "Excepción al conectarse como admin con el servidor LDAP"};
			return a_resultado;
		}

		if (!ok_connect) {
			debugstr = "No fue posible conectarse al servidor LDAP " + servidorLDAP + " en el puerto " + puertoLDAP;
		    Auxiliar.mensajeImpersonal("advertencia", debugstr);
			a_resultado = new String[]{"-2", debugstr};
			return a_resultado;
		}
		
		SimpleBindRequest adminBindRequest = new SimpleBindRequest(usuarioBuscadorLDAP, claveUsuarioBuscadorLDAP);
		try {
			adminConnection.bind(adminBindRequest);
			Auxiliar.mensajeImpersonal("confirmacion", "Bind de LDAP exitoso con: " + usuarioBuscadorLDAP);
			ok_bind = true;
		}
		catch (LDAPException e) {
			e.printStackTrace();
			a_resultado = new String[]{"-3", "No se pudo hacer el bind de admin."};
			return a_resultado;
		}
		
		if (!ok_bind) {
			debugstr = "No fue posible realizar el bind de admin con " + usuarioBuscadorLDAP + " y clave " + claveUsuarioBuscadorLDAP;
		    Auxiliar.mensajeImpersonal("advertencia", debugstr);
			a_resultado = new String[]{"-4", debugstr};
			return a_resultado;
		}

	    Auxiliar.mensajeImpersonal("nota", "Creando filtro con el atributo de búsqueda " + atributoBusquedaLDAP + " para el usuario " + usuarioLDAP);
		Filter findUserfilter = null;
		findUserfilter = Filter.createEqualityFilter(atributoBusquedaLDAP, usuarioLDAP);
		
		if (findUserfilter == null) {
			debugstr = "No se pudo crear el filtro con el atributo de búsqueda " + atributoBusquedaLDAP + " para el usuario " + usuarioLDAP;
		    Auxiliar.mensajeImpersonal("error", debugstr);
			a_resultado = new String[]{"-5", debugstr};
			return a_resultado;
		}
		
	    Auxiliar.mensajeImpersonal("nota", "Creando solicitud de búsqueda sobre la base " + baseBusquedaLDAP + " para el usuario " + usuarioLDAP);
		SearchRequest searchRequest = new SearchRequest(baseBusquedaLDAP, SearchScope.SUB, findUserfilter);
	    
		Auxiliar.mensajeImpersonal("nota", "Estableciendo el límite de tamaño de búsqueda...");
		searchRequest.setSizeLimit(1); 
		SearchResult searchResult = null;

		try	{
		    Auxiliar.mensajeImpersonal("nota", "Realizando la búsqueda...");
		    searchResult = adminConnection.search(searchRequest);
		    ok_search = true;
		}
		catch (LDAPSearchException e) {
		    e.printStackTrace();
			debugstr = "Excepción al realizar la búsqueda: " + e.toString();
		    Auxiliar.mensajeImpersonal("error", debugstr);
			a_resultado = new String[]{"-6", debugstr};
			return a_resultado;
		}
		
		if (!ok_search) {
			debugstr = "No fue posible realizar la búsqueda de " + usuarioLDAP + " sobre la base de " + baseBusquedaLDAP;
		    Auxiliar.mensajeImpersonal("advertencia", debugstr);
			a_resultado = new String[]{"-7", debugstr};
			return a_resultado;
		}
		
		String userDN = null;
		if (searchResult.getEntryCount() > 1) {
			debugstr = "Hay más de un resultado relacionado con:" + searchRequest.getFilter();
			Auxiliar.mensajeImpersonal("advertencia", debugstr);
			a_resultado = new String[]{"-8", debugstr};
			return a_resultado;
		}
		if (searchResult.getEntryCount() == 0) {
			debugstr = "No se encontró resultado alguno para:" + searchRequest.getFilter();
			Auxiliar.mensajeImpersonal("advertencia", debugstr);
			a_resultado = new String[]{"3", debugstr};
			return a_resultado;
		}
		
		boolean ok_existe = false;
		
		Auxiliar.mensajeImpersonal("nota", "Iterando sobre registros encontrados...");
		for (SearchResultEntry entry : searchResult.getSearchEntries()) {
		    userDN = entry.getDN();
		    Auxiliar.mensajeImpersonal("confirmacion", "Encontré al usuario LDAP: " + userDN);
		    ok_existe = true;
		}
		
		if (ok_existe) {
		
			LDAPConnection userConnection = new LDAPConnection(sslSocketFactory);
			try {
			    userConnection = new LDAPConnection(servidorLDAP, puertoLDAP);
			    Auxiliar.mensajeImpersonal("confirmacion", "Conexión LDAP de usuario exitosa con:" + servidorLDAP + ":" + puertoLDAP);
			}
			catch (LDAPException e) {
			    e.printStackTrace();
				debugstr = "Excepción al intentar realizar la conexión LDAP con:" + servidorLDAP + ":" + puertoLDAP;
				Auxiliar.mensajeImpersonal("error", debugstr);
				a_resultado = new String[]{"-10", debugstr};
				return a_resultado;
			}
	
			SimpleBindRequest userBindRequest = new SimpleBindRequest(userDN, claveUsuarioLDAP);
			if (userBindRequest.getBindDN() == null) {
				debugstr = "Enlace de usuario a nivel de DN es nulo por lo cual la conexión es anónima.";
				Auxiliar.mensajeImpersonal("advertencia", debugstr);
				a_resultado = new String[]{"-11", debugstr};
				return a_resultado;
			}
			if (userBindRequest.getPassword() == null) {
				debugstr = "Enlace de usuario a nivel de clave es nulo por lo cual la conexión es anónima.";
				Auxiliar.mensajeImpersonal("advertencia", debugstr);
				a_resultado = new String[]{"-12", debugstr};
				return a_resultado;
			}
			try {
			    userConnection.bind(userDN, claveUsuarioLDAP);
				debugstr = "Usuario "+userDN+" autenticado por LDAP.";
			    Auxiliar.mensajeImpersonal("confirmacion", debugstr);
				a_resultado = new String[]{"1", debugstr};
				return a_resultado;
			}
			catch (LDAPException e) {
			    e.printStackTrace();
				debugstr = "No se pudo autenticar el usuario LDAP " + userDN + " con la clave suministrada.";
				Auxiliar.mensajeImpersonal("error", debugstr);
				a_resultado = new String[]{"2", debugstr};
				return a_resultado;

			}
		}
			
		return a_resultado;
	}

	/**
	 * Obtiene el dato de un atributo LDAP para el usuario
	 * 
	 * @param usuarioLDAP
	 * @param claveUsuarioLDAP
	 * @param atributo
	 * @return String con el dato del atributo
	 */
	public String dato(String usuarioLDAP, String claveUsuarioLDAP, String atributo) {
		String dato = "";
		
		try {
			BD dbREDD = new BD();
			if (dbREDD != null) {
				if (!Auxiliar.tieneAlgo(servidorLDAP)) {
					servidorLDAP = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='servidorLDAP'", "");
				}
				if (!Auxiliar.tieneAlgo(servidorLDAP)) {
					return "";
				}
				
				String puertoLDAP_bd = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='puertoLDAP'", "389");
				if (!Auxiliar.esPuerto(puertoLDAP_bd)) {
					puertoLDAP = 389;
				}
				else {
					puertoLDAP = Integer.valueOf(puertoLDAP_bd);
				}
				
				if (!Auxiliar.tieneAlgo(usuarioBuscadorLDAP)) {
					usuarioBuscadorLDAP = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='usuarioBuscadorLDAP'", "");
				}
				if (!Auxiliar.tieneAlgo(usuarioBuscadorLDAP)) {
					return "";
				}
				
				if (!Auxiliar.tieneAlgo(claveUsuarioBuscadorLDAP)) {
					claveUsuarioBuscadorLDAP = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='claveUsuarioBuscadorLDAP'", "");
				}
				if (!Auxiliar.tieneAlgo(claveUsuarioBuscadorLDAP)) {
					return "";
				}
				
				if (!Auxiliar.tieneAlgo(atributoBusquedaLDAP)) {
					atributoBusquedaLDAP = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='atributoBusquedaLDAP'", "");
				}
				if (!Auxiliar.tieneAlgo(atributoBusquedaLDAP)) {
					return "";
				}
				
				if (!Auxiliar.tieneAlgo(baseBusquedaLDAP)) {
					baseBusquedaLDAP = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='baseBusquedaLDAP'", "");
				}
				if (!Auxiliar.tieneAlgo(baseBusquedaLDAP)) {
					return "";
				}
			}
			if (dbREDD != null) dbREDD.desconectarse();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			Auxiliar.mensajeImpersonal("error", "Excepción:" + e1.toString());
		} catch (Exception e1) {
			e1.printStackTrace();
			Auxiliar.mensajeImpersonal("error", "Excepción:" + e1.toString());
		}
		
		
		SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager()); 
		SSLSocketFactory sslSocketFactory = null;
		try {
			sslSocketFactory = sslUtil.createSSLSocketFactory();
		}
		catch (GeneralSecurityException e1) {
			e1.printStackTrace();
		}
		SimpleBindRequest adminBindRequest = new SimpleBindRequest(usuarioBuscadorLDAP, claveUsuarioBuscadorLDAP);
		LDAPConnection adminConnection = new LDAPConnection(sslSocketFactory);
		try {
			adminConnection = new LDAPConnection(servidorLDAP, puertoLDAP);
			Auxiliar.mensajeImpersonal("confirmacion", "Conexión LDAP establecida con :" + servidorLDAP + ":" + puertoLDAP);
			adminConnection.bind(adminBindRequest);
			Auxiliar.mensajeImpersonal("confirmacion", "Bind de LDAP exitoso con:" + usuarioBuscadorLDAP);
		}
		catch (LDAPException e) {
			e.printStackTrace();
			return "";
		}
				
		Filter findUserfilter = null;
		findUserfilter = Filter.createEqualityFilter(atributoBusquedaLDAP, usuarioLDAP);
		
		SearchRequest searchRequest = new SearchRequest(baseBusquedaLDAP, SearchScope.SUB, findUserfilter);
		searchRequest.setSizeLimit(1); 
		SearchResult searchResult = null;
		try	{
			searchResult = adminConnection.search(searchRequest);
		}
		catch (LDAPSearchException e) {
			e.printStackTrace();
		}
		String userDN = null;
		if (searchResult != null) {
			if (searchResult.getEntryCount() > 1) {
				Auxiliar.mensajeImpersonal("advertencia", "Hay más de un resultado relacionado con:" + searchRequest.getFilter());
			}
			if (searchResult.getEntryCount() == 0) {
				Auxiliar.mensajeImpersonal("nota", "No se encontró resultado alguno para:" + searchRequest.getFilter());
			}
			for (SearchResultEntry entry : searchResult.getSearchEntries()) {
				userDN = entry.getDN();
				Auxiliar.mensajeImpersonal("confirmacion", "Encontré un usuario LDAP: " + userDN);
				
				dato = entry.getAttributeValue(atributo);
			}
		}
		
		return dato;
	}
}
