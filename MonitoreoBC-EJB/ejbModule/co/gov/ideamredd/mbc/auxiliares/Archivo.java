// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.auxiliares;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Archivo {

	private static Auxiliar aux = new Auxiliar();
    
    /**
     * Método que dice si un archivo ya existe o no en la ruta especificada.
     * @param ruta_archivo
     * @return boolean: verdadero si ya existe, falso si no
     */
    public static boolean existeArchivo(String ruta_archivo) {
        File arch = new File(ruta_archivo);
    	
        return arch.exists();
    }
	
    /**
     * Método para obtener la última fecha de modificación de un archivo
     * 
     * @param ruta_archivo
     * @return String fecha de modificación en formato yyyy-MM-dd HH:mm:ss
     */
    public static String fechaModificacion(String ruta_archivo) {
    	File file = new File(ruta_archivo);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String str_fecha = "";
    	
    	if (file.exists()) {
    		str_fecha = sdf.format(file.lastModified()); 
    	}
    	
    	return str_fecha; 
    }
    
	/**
	 * Método recursivo que responde si un archivo existe 
	 * en una carpeta o sus subcarpetas
	 * @param nombre_archivo
	 * @param carpeta
	 * @return boolean: existe o no el archivo en la carpeta o sus subcarpetas?
	 */
    @SuppressWarnings("unused")
	private static boolean existeArchivoEnSubdirectorios(String nombre_archivo, File carpeta) {
        boolean resultado = false;
 
        if (new File (carpeta, nombre_archivo).exists ()) {
        	resultado = true;
        } 
        else {
            File[] subcarpetas = carpeta.listFiles ();
 
            int i = 0;
            int n = (subcarpetas == null) ? 0 : subcarpetas.length;
 
            while ((i < n) && ! resultado) {
                File subcarpeta = subcarpetas[i];
 
                if (subcarpeta.isDirectory ()) {
                	resultado = existeArchivoEnSubdirectorios (nombre_archivo, subcarpeta);
                }
 
                i ++;
            }
        }
 
        return resultado;
    }	

    
    /**
     * Retorna el contenido de un archivo de texto como un String
     * @param archivo
     * @param n_lineas número de lineas a leer
     * @return String: contenido de archivo
     */	
    public static String leerNLineasDelArchivo(String nombre_archivo, Integer n_lineas) {
    	String resultado = "";
    	Integer n_linea = 0;
    	
    	if (n_lineas == null) {
    		n_lineas = -1;
    	}
    	
    	try	{
    		//StringBuilder contents = new StringBuilder();
    		StringBuffer contents = new StringBuffer();
    		File f = new File(nombre_archivo);
    		
    		if (f.exists())	{
    			BufferedReader input =  new BufferedReader(new FileReader(nombre_archivo));
    			try {
    				String linea = null;
    				
    				while (( linea = input.readLine()) != null) {
						if (n_linea < n_lineas || n_lineas == -1) {
							contents.append(linea);
						}
    					n_linea++;
    					//contents.append(System.getProperty("line.separator"));
    				}
    				
    				resultado = contents.toString();
    			}
    			finally	{
    				input.close();
    			}
    		}
    		else
    		{
    			resultado = "ERROR - El archivo " + nombre_archivo + " no existe... ";
    		}
    	}
    	catch (Exception e)
    	{
    		resultado = "Problemas al leer del archivo " + nombre_archivo;
    	}
    	
    	return resultado;
    }	
    
	/**
	 * Retorna el contenido de un archivo de texto como un String
	 * @param archivo
	 * @return String: contenido de archivo
	 */	
	public static String leerArchivoSeparado (String nombre_archivo, String separador) {
		String resultado = "";

		try	{
			File f = new File(nombre_archivo);
			
			long i = 0;
			
			if (f.exists())	{
				BufferedReader input =  new BufferedReader(new FileReader(nombre_archivo));
				try {
					String linea = null;
			        while (( linea = input.readLine()) != null) {
			        	if (i > 0) resultado += separador;
			        	
			        	resultado += linea.toString();
			        	i++;
			        }
				}
				finally	{
					input.close();
				}
			}
		}
		catch (Exception e)
		{
			aux.mensajeImpersonal("error", "Error al leer del archivo " + nombre_archivo);
		}
		
		return resultado;
	}	

	
	/**
	 * Método que retorna la extensión de un archivo.
	 * 
	 * @param nombre_archivo: nombre del archivo
	 * @return extensión del archivo
	 */
	public static String extensionArchivo(String nombre_archivo) {
		String r = "";
		
		if (nombre_archivo == null) return "";
		
		if (nombre_archivo.equals("")) return "";
		
		String [] a_partes = null; 
			
		a_partes = nombre_archivo.split("\\.");
		
		int n_partes = a_partes.length;
		
		if (n_partes > 0) {
			r = a_partes[n_partes -1];
		}
		
		return r;
	}

	/**
	 * Método que retorna el nombre base de un archivo.
	 * 
	 * @param nombre_archivo: nombre del archivo
	 * @return extensión del archivo
	 */
	public static String nombreBaseArchivo(String ruta) {
		String r = "";
		
		if (ruta == null) return "";
		
		if (ruta.equals("")) return "";
		
		String [] a_partes_ruta = null;
		a_partes_ruta= ruta.split("/");
		int n_partes_ruta = a_partes_ruta.length;
		String nombre_archivo = a_partes_ruta[n_partes_ruta -1];
		
		String [] a_partes_nombre = null; 		
		a_partes_nombre = nombre_archivo.split("\\.");
		int n_partes_nombre = a_partes_nombre.length;
		if (n_partes_nombre >= 0) {
			r = a_partes_nombre[0];
		}
		
		return r;
	}
	
	/**
	 * Metodo para crear una carpeta.  Retorna verdadero si ya existe.
	 * @param ruta_carpeta
	 * @throws IOException
	 */
	public static boolean crearCarpeta(String ruta_carpeta) 
	throws IOException {
		// SI LA CARPETA YA EXISTE RETORNA VERDADERO
		
		if (existeArchivo(ruta_carpeta)) return true;

		return (new File(ruta_carpeta)).mkdirs();
	}


	public static boolean eliminarArchivo(String ruta_archivo) 
			throws IOException {
		// SI LA CARPETA YA EXISTE RETORNA VERDADERO
		
		if (!existeArchivo(ruta_archivo)) return true;
		
		return (new File(ruta_archivo)).delete();
	}
	
	
	
	
	/**
	 * Método filasArchivo
	 * 
	 *  
	 * @param ruta_archivo
	 * @return numero de filas de un archivo de texto
	 * @throws IOException
	 */
	public static int filasArchivo(String ruta_archivo) 
		throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(ruta_archivo));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count-1;
	    } finally {
	        is.close();
	    }
	}	

	/**
	 * Método filasArchivoImportacion
	 * 
	 * Retorna el número de registros a importar.
	 * 
	 * @param ruta_archivo: debe existir.
	 * @return lineas: número de registros a importar.
	 */
	public static int filasArchivoImportacion(String ruta_archivo) {
		int lineas = 0;

		try {
			InputStream inputStream = new FileInputStream(ruta_archivo);
			Reader reader = new InputStreamReader(inputStream, Charset.forName("windows-1252"));
			BufferedReader br = new BufferedReader(reader);
			String linea = "";
		
		    while ((linea = br.readLine()) != null) {
	            String lini = linea.substring(0, 5);	            
	            if (lini.equals("Poner") || lini.equals("Put E") || lini.equals("ACCIO")) {
	            	continue;
	            }
	            lineas++;
		    }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return lineas;

	}

	/**
	 * Método escribirArchivo.  Crea un archivo de texto con el contenido en la ruta.
	 * 
	 * @param ruta
	 * @param contenido
	 * @return
	 * @throws IOException
	 */
	public static boolean escribirArchivo(String ruta, String contenido) 
	throws IOException {
		BufferedWriter writer = null;
        try {
            //String instante = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            File archivo = new File(ruta);
            System.out.println(archivo.getCanonicalPath());
            writer = new BufferedWriter(new FileWriter(archivo));
            writer.write(contenido);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }		
        
        return existeArchivo(ruta);
	}
	
	
	/**
	 * Método archivosEnCarpeta
	 * 
	 * Devuelve arreglo de archivos en carpeta
	 * 
	 * @param ruta
	 * @return
	 * @throws IOException
	 */
	public static String[] archivosEnCarpeta(String ruta)
	throws IOException {
		List<String> a_lista_archivos = new ArrayList<String>();
		
		File carpeta = new File(ruta);
		
		try {
			if (carpeta != null) {
				for (File archivo: carpeta.listFiles()) {
					if (archivo != null) {
						if (archivo.isFile()) {
							a_lista_archivos.add(archivo.getName());
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		String [] a_archivos = new String[a_lista_archivos.size()];
		a_lista_archivos.toArray(a_archivos);
		
		return a_archivos;
	}
	
}
//338
