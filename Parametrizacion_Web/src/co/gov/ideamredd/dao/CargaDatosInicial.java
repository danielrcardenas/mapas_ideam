package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import java.sql.Connection;
import java.util.ArrayList;
import javax.sql.DataSource;
import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.entities.Noticias;
import co.gov.ideamredd.entities.Rutas;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.apache.log4j.Logger;


/**
 * Carga los datos iniciales de las rutas de configuraci�n. La ruta de Metadatos y Thumbnails vienen del archivo providers.fac de apollo, mientras que el resto vienen de la base de datos
 */
public class CargaDatosInicial {

	private static Connection conn;
	private static DataSource dataSource = ConexionBD.getConnection();
	private static ArrayList<Rutas> listaRutasApollo;
	private static Logger log=Logger.getLogger("SMBCLog");

	
	public static ArrayList<Noticias> getNoticias() {
		ArrayList<Noticias> noticias = new ArrayList<Noticias>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaNoticias = conn
					.prepareCall("{call RED_PK_TABLASTIPO.consultaTodasNoticias(?)}");
			consultaNoticias.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaNoticias.execute();
			ResultSet r = (ResultSet) consultaNoticias
					.getObject("un_Resultado");
			while (r.next()) {
				Noticias n = new Noticias();
				n.setConsecutivo(r.getInt(1));
				n.setTipo(r.getInt(2));
				n.setFecha(r.getDate(3));
				n.setNombre(r.getString(4));
				n.setDescripcion(r.getString(5));
				n.setPathImagen(r.getString(7));
				noticias.add(n);
			}
			r.close();
			consultaNoticias.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return noticias;
	}

	public static ArrayList<Noticias> getEventos() {
		ArrayList<Noticias> eventos = new ArrayList<Noticias>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaEventos = conn
					.prepareCall("{call RED_PK_TABLASTIPO.consultaTodosEventos(?)}");
			consultaEventos.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaEventos.execute();
			ResultSet r = (ResultSet) consultaEventos
					.getObject("un_Resultado");
			while (r.next()) {
				Noticias n = new Noticias();
				n.setConsecutivo(r.getInt(1));
				n.setTipo(r.getInt(2));
				n.setFecha(r.getDate(3));
				n.setNombre(r.getString(4));
				n.setDescripcion(r.getString(5));
				n.setLugar(r.getString(6));
				n.setPathImagen(r.getString(7));
				// n.setCoordenadas(r.getString(8));
				n.setHora(r.getString(9));
				eventos.add(n);
			}
			r.close();
			consultaEventos.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventos;
	}
	 /**
		 * Obtiene las rutas de configuraci�n. La ruta de Metadatos y Thumbnails vienen del archivo providers.fac de apollo, mientras que el resto vienen de la base de datos
		 * 
		 */
	
	public static ArrayList<Rutas> getArrayRutasApollo() {
		log.info(CargaDatosInicial.class+" Termino la consulta de datos");
		//carga rutas de el providers.dat
		    listaRutasApollo = new ArrayList<Rutas>();
			Rutas rutaMetadato = new Rutas();
			rutaMetadato.setNombre("Metadatos");
			rutaMetadato.setRuta(consultarRutaMetadatos(getRutaProviders()));
			rutaMetadato.setDescripcion("Ruta donde se almacenan los metadatos generados por Apollo");
			listaRutasApollo.add(rutaMetadato);
			Rutas rutaThumbnails = new Rutas();
			rutaThumbnails.setNombre("Thumbnail");
			rutaThumbnails.setRuta(consultarRutaThumbnails(getRutaProviders()));
			rutaThumbnails.setDescripcion("Ruta donde se almacenan los thumbnails generados por Apollo");
			listaRutasApollo.add(rutaThumbnails);
		//carga rutas de la tabla
			
			try {
				conn = dataSource.getConnection();
				CallableStatement consultaParametros = conn
						.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta(?, ?)}");
		
				consultaParametros.registerOutParameter("un_Resultado",
						OracleTypes.CURSOR);
				consultaParametros.registerOutParameter("sentencia",
						OracleTypes.VARCHAR);
				consultaParametros.execute();
				
				//System.out.println(consultaParametros.getObject("sentencia"));
				ResultSet r = (ResultSet) consultaParametros.getObject("un_Resultado");
				
				while (r.next()) {
					Rutas ruta = new Rutas();
					ruta.setConsecutivo(r.getInt(1));
					ruta.setNombre(r.getString(2));
					ruta.setRuta(r.getString(3));
					ruta.setDescripcion(r.getString(4));
					listaRutasApollo.add(ruta);
				}
				r.close();
				consultaParametros.close();
				conn.close();
				log.info(CargaDatosInicial.class+"[Get Rutas Parametros] termino");
				
			} catch (Exception e) {
				log.error(CargaDatosInicial.class+"[Get Rutas Parametros] fallo en la consulta de rutas de la tabla de parametros",e);
				e.printStackTrace();
			}
		return listaRutasApollo;
	}
	 /**
		 * Obtiene la ruta de los metadatos del archivo providers.fac
		 * @param rutaProviders ruta en la cual se encuentra el archivo
		 */

	 
	public static String consultarRutaMetadatos(String rutaProviders) {
		String rutaMetadato = null;
		Scanner fileToRead = null;
			try {
				 fileToRead = new Scanner(new File(rutaProviders)); //point the scanner method to a file
			
				//check if there is a next line and it is not null and then read it in
				 for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null; ) {
					 if(line.contains("<METADATA TEMPLATE")){
						 rutaMetadato=line.substring(54,line.indexOf("/>")-2);
						 
					 }
					 
				 }
				 fileToRead.close();
				 log.info(CargaDatosInicial.class+"[Get Ruta Metadato] Termino");
				 fileToRead.close();
				 return rutaMetadato;
				 
			 } catch (FileNotFoundException e) {
				 log.error(CargaDatosInicial.class+"[Get Ruta Metadato] no se encuentra el archivo provider.dat en la ruta"+ rutaProviders+" especificada o no cuenta con los permisos suficientes, porfavor configure la ruta en la tabla PRTR_RUTA asignando PRTR_NOMBRE = PROVIDERS ", e);
				 
			 return "no se encuentra el archivo provider.dat en la ruta"+ rutaProviders+" especificada, porfavor configure la ruta en la tabla PRTR_RUTA asignando PRTR_NOMBRE = PROVIDERS";
			 } 
	 }
	 
	 
	 /**
		 * Obtiene la ruta de los thumnails del archivo providers.fac
		 * @param rutaProviders ruta en la cual se encuentra el archivo
		 */
	 
	public static String consultarRutaThumbnails(String rutaProviders) {
		String rutaThumbnail = null;
		Scanner fileToRead = null;
			try {
				 fileToRead = new Scanner(new File(rutaProviders)); 
				 for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null; ) {
					 if(line.contains("<LEGEND TEMPLATE")){
						 rutaThumbnail=line.substring(60,line.indexOf("/>")-2);
						 
					 }
					 
				 }
				 fileToRead.close();
				 log.info(CargaDatosInicial.class+"[Get Ruta Thumbnail] Termino");
				 return rutaThumbnail;
			 } catch (FileNotFoundException e) {
				 log.error(CargaDatosInicial.class+"[Get Ruta Thumbnail] no se encuentra el archivo provider.dat en la ruta"+ rutaProviders+" especificada o no cuenta con los permisos suficientes, porfavor configure la ruta en la tabla PRTR_RUTA asignando PRTR_NOMBRE = PROVIDERS ", e);
				 return "no se encuentra el archivo provider.dat en la ruta"+ rutaProviders+" especificada, porfavor configure la ruta en la tabla PRTR_RUTA asignando PRTR_NOMBRE = PROVIDERS";
			 } 
	 }

	 /**
		 * Obtiene la ruta del archivo providers.fac
		 * @return rutaProviders ruta en la cual se encuentra el archivo providers.fac, la obtiene de la base de datos
		 */
	 public static String getRutaProviders(){
		 String rutaProviders = "";
			try {
				conn = dataSource.getConnection();
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
				log.info(CargaDatosInicial.class+"[Get Ruta Providers] Termino");
			} catch (Exception e) {
				log.error(CargaDatosInicial.class+"[Get Ruta Providers] Fallo", e);
				e.printStackTrace();
				
			}
			return rutaProviders;
		 
	 }

}
