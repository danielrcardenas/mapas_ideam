
// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.descarga.dao;

import java.io.BufferedWriter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.log4j.Logger;

import co.gov.ideamredd.mbc.conexion.ConexionBD;
import oracle.jdbc.OracleTypes;

@Stateless
public class ActualizarParametro {

	@EJB
	ConexionBD conexion;
	private static Logger log=Logger.getLogger("SMBCLog");
	private Connection conn;
	private Integer idParametro;
	private String una_ruta;
	static StringBuffer stringBufferOfData = new StringBuffer();
	
	 /**
		 * Actualiza las rutas de configuraci�n. La ruta de Metadatos y Thumbnails vienen del archivo providers.fac de apollo, mientras que el resto vienen de la base de datos
		 * @return resultado Indica si la modificaci�n se realizo satisfactoriamente
		 */
	public boolean modificarParametro() {
		boolean resultado = false;
		
		try {
			log.info(ActualizarParametro.class+" Inicio creaci�n reporte");
			conn = conexion.establecerConexion();
			CallableStatement actualizarParametro = conn.prepareCall("{call RED_PK_PARAMETROS.Parametro_Actualiza(?,?,?,?)}");
			actualizarParametro.setInt("una_Llave", idParametro);
			actualizarParametro.setString("una_ruta",una_ruta);
			actualizarParametro.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			actualizarParametro.registerOutParameter("una_Respuesta",
					OracleTypes.NUMBER);
			actualizarParametro.execute();
			//System.out.println(actualizarParametro.getObject("una_Respuesta"));
			
			actualizarParametro.close();
			conn.close();
			log.info(ActualizarParametro.class+"[modificarParametros] Termino");
			resultado=true;
		} catch (Exception e) {
			log.error(ActualizarParametro.class+"[modificarParametros] Fallo, no se puede actualizar la tabla de parametros", e);
			e.printStackTrace();
		}
		return resultado;

	}
	
	/**
	 * Obtiene la ruta del archivo providers.fac
	 * @return rutaProviders ruta en la cual se encuentra el archivo providers.fac, la obtiene de la base de datos
	 */
	 public String getRutaProviders(){
		 String rutaProviders = "";
			try {
				//log = SMBC_Log.Log(ActualizarParametro.class);
				conn = conexion.establecerConexion();
				CallableStatement consultaRutaProvider = conn
						.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?, ?, ?)}");
				consultaRutaProvider.setString("un_Nombre", "PROVIDERS");
				consultaRutaProvider.registerOutParameter("una_Ruta",
						OracleTypes.CURSOR);
				consultaRutaProvider.registerOutParameter("sentencia",
						OracleTypes.VARCHAR);
				consultaRutaProvider.execute();
				ResultSet r = (ResultSet) consultaRutaProvider.getObject("una_Ruta");
				
				while (r.next()) {
					rutaProviders = r.getString(1);
				}
				
				r.close();
				consultaRutaProvider.close();
				conn.close();
				log.info(ActualizarParametro.class+"[getRutaProviders] Termino");
			} catch (Exception e) {
				log.error(ActualizarParametro.class+"[getRutaProviders] Fallo" +e);
				e.printStackTrace();
			}
			return rutaProviders;
	}
	 /**
		 * Obtiene la ruta de los thumnails del archivo providers.fac
		 * @param rutaProviders ruta en la cual se encuentra el archivo
		 */
	 public void escribirArchivo(String rutaProviders) {
		 try {
			 BufferedWriter bufwriter = new BufferedWriter(new FileWriter(rutaProviders));
			 bufwriter.write(stringBufferOfData.toString());
			 bufwriter.close();
			 log.info(ActualizarParametro.class+"[editRutaProviders] Termino");
		 } catch (Exception e) {//if an exception occurs
			 log.error(ActualizarParametro.class+"[editRutaProviders] Fallo"+"Asegurese de contar con los permisos de lencura y escritura, Error occured while attempting to write to file: " + e.getMessage(), e);
			 
		 }finally{stringBufferOfData.delete(0, stringBufferOfData.length());}
		 }
	 /**
		 * Edita la ruta de los Metadatos del archivo providers.fac
		 * @param rutaProviders ruta en la cual se encuentra el archivo providers.fac
		 * @param nuevaRutaMetadato ruta en la cual se encuentra se van a almacenar los Metadatos
		 */
	 public boolean editarRutaMetadato(String rutaProviders, String nuevaRutaMetadato) {
		 boolean resultado = false;
		 Scanner fileToRead = null;
		
		 try {
			fileToRead = new Scanner(new File(rutaProviders));
			 for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null; ) {
				 if(line.contains("<METADATA TEMPLATE")){
					 line=line.substring(0,54).concat(nuevaRutaMetadato).concat(" \"/>");
				 }
				 stringBufferOfData.append(line).append("\r\n");
				 }
				 fileToRead.close();
				 log.error(ActualizarParametro.class+"[editRutaMetadato] Termino");	 
		} catch (FileNotFoundException e) {
			log.error(ActualizarParametro.class+"[editRutaMetadato] Fallo"+"Asegurese de contar con los permisos de lencura y escritura, Error occured while attempting to write to file: " + e.getMessage(), e);
			e.printStackTrace();
			return false;
		} 
		 resultado=true;
		 escribirArchivo(rutaProviders);
		 return resultado;
		 }
	 
	 /**
		 * Edita la ruta de los thumnails del archivo providers.fac
		 * @param rutaProviders ruta en la cual se encuentra el archivo providers.fac
		 * @param nuevaRutaThumbnail ruta en la cual se encuentra se van a almacenar los thumbnails
		 */
	 public boolean editarRutaThumbnail(String rutaProviders, String nuevaRutaThumbnail) {
		 boolean resultado = false;
		 Scanner fileToRead = null;
		
		 try {
			fileToRead = new Scanner(new File(rutaProviders));
			 for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null; ) {
				 if(line.contains("<LEGEND TEMPLATE")){
					 line=line.substring(0,60).concat(nuevaRutaThumbnail).concat(" \"/>");
					 System.out.println(line);
				 }
				 stringBufferOfData.append(line).append("\r\n");
				 }
				 fileToRead.close();
				 resultado=true;
				 log.info(ActualizarParametro.class+"[editRutaMetadato] Termino");
		} catch (FileNotFoundException e) {
			log.error(ActualizarParametro.class+"[editRutaThumbnail] Fallo"+"Asegurese de contar con los permisos de lencura y escritura, Error occured while attempting to write to file: " + e.getMessage(), e);
			e.printStackTrace();
			return false;
		} 

		 escribirArchivo(rutaProviders);
		 return resultado;
		 }
	 
	public Integer getIdParametro() {
		return idParametro;
	}

	public void setIdParametro(Integer idParametro) {
		this.idParametro = idParametro;
	}

	public String getUna_ruta() {
		return una_ruta;
	}

	public void setUna_ruta(String una_ruta) {
		this.una_ruta = una_ruta;
	}
	
}
