// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif
 * Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import java.io.File;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

 
/**
 * Clase RCliente
 * 
 * Ejecuta script en R y retorna los resultados
 * 
 * @author © 2015 Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 *
 */
public class RCliente{

	public static String yo = "BMC.";
	boolean siga = false;

	/**
	 * Constructor
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public RCliente() 
	throws ClassNotFoundException, Exception {
	}

	/**
	 * Ejecuta comandos en R
	 * 
	 * @param commandos
	 * @param host
	 * @param sport
	 * @param id_usuario
	 * @param clave
	 * @return List<String[]> de comandos
	 */
	public List<String[]> RCommandos(List<String[]> commandos, String host, String sport, String id_usuario, String clave) {
		//Auxiliar aux = new Auxiliar();

		int port = 6311;
		if (!Auxiliar.tieneAlgo(host)) host = "localhost";
		if (Auxiliar.tieneAlgo(sport)) port = Integer.valueOf(sport);

        RConnection c;
		try {
			c = new RConnection(host, port);
	        if(c.isConnected()) {
	            Auxiliar.mensajeImpersonal("nota", "Conectado a Rserve...");
	            if(c.needLogin()) {
	                System.out.println("Providing Login");
	                c.login(id_usuario, clave);
	            }

	            REXP x;
	            
            	int n = commandos.size();
            	
            	String [] a_commando;
            	String commando = "";
            	int i = 0;
            	for (i=0; i<n; i++) {
				    a_commando = commandos.get(i);
				    commando = a_commando[0];
				    try {
		                x = c.eval(commando);
		                if (x != null) {
		                	a_commando[1] = Auxiliar.nz(x.toString(), "");
		                }
				    }
				    catch (Exception e) {
		                a_commando[1] = e.toString();
				    }
			    	commandos.set(i, a_commando);
	                Auxiliar.mensajeImpersonal("nota", commando + " ==> " + a_commando[1]);    // prints result
    			}
	        }
		} catch (RserveException e) {
			Auxiliar.mensajeImpersonal("error", e.toString());
			e.printStackTrace();
		}
        
		return commandos;
	}
	
	/**
	 * Ejecuta un script sobre R
	 * 
	 * @param script
	 * @param desde_archivo
	 * @param host
	 * @param sport
	 * @param id_usuario
	 * @param clave
	 * @return String resultado de la ejecución
	 * @throws RserveException
	 * @throws REXPMismatchException
	 */
	public String RString(String script, boolean desde_archivo, String host, String sport, String id_usuario, String clave) 
			throws RserveException, REXPMismatchException {
		
		//Auxiliar aux = new Auxiliar();
		
		if (!Auxiliar.tieneAlgo(script) && false) {
			return Auxiliar.mensajeImpersonal("advertencia", "Se debe especificar un script o su ruta absoluta");
		}
		
		int port = 6311;
		
		if (!Auxiliar.tieneAlgo(host)) host = "localhost";
		if (Auxiliar.tieneAlgo(sport)) port = Integer.valueOf(sport);
		
		String R = "";
		
        RConnection c = new RConnection(host, port);
		//RConnection c = new RConnection();

        if(c.isConnected()) {
            Auxiliar.mensajeImpersonal("nota", "Conectado a Rserve...");
            if(c.needLogin()) {
                System.out.println("Providing Login");
                c.login(id_usuario, clave);
            }

            REXP x;
            String linea = "";

            if (desde_archivo) {
	            Auxiliar.mensajeImpersonal("nota", "Procesando script desde archivo: " + script);
	            
	            try {
	                File f = new File(script);
	
	    			if (f.exists())	{
		            	BufferedReader input =  new BufferedReader(new FileReader(script));
		
					    while (( linea = input.readLine()) != null) {
		                    x = c.eval(linea);         // evaluates line in R
		                    Auxiliar.mensajeImpersonal("nota", linea + " ==> R="+x);    // prints result
		                }
					    
					    input.close();
	    			}
	            	else {
	            		x = c.eval("R.version.string");
	            		Auxiliar.mensajeImpersonal("nota", "No encontré el script "+script+". Versión de R: " + x.asString());
	            	}
	            }
				catch (Exception e) {
					Auxiliar.mensajeImpersonal("error", e.toString());
				}
            }
            else {
	            Auxiliar.mensajeImpersonal("nota", "Procesando script desde BD");

	            if (Auxiliar.tieneAlgo(script)) {
		            try {
		            	String [] a_lineas = script.split("\n");
		            	int n_lineas = a_lineas.length;
		            	
		            	int i = 0;
		            	for (i = 0; i < n_lineas; i++) {
						    linea = a_lineas[i];
			                x = c.eval(linea);         // evaluates line in R
			                Auxiliar.mensajeImpersonal("nota", linea + " ==> R="+x);    // prints result
		    			}
		            }
					catch (Exception e) {
						Auxiliar.mensajeImpersonal("error", e.toString());
					}
	            }
	            else {
	            	R = "";
	            }
            }
        } 
        else {
            Auxiliar.mensajeImpersonal("error", "No me pude conectar a Rserve");
        }

        c.close();
        Auxiliar.mensajeImpersonal("nota", "Conexión a Rserve cerrada.");
		
		
		return R;
	}
	
}
