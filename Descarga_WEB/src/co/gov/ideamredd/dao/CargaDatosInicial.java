package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.sql.DataSource;
import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.entities.Dataset;
import co.gov.ideamredd.entities.Noticias;
//import co.gov.ideamredd.entities.Georeferenciacion;
//import co.gov.ideamredd.entities.Proyeccion;
//import co.gov.ideamredd.entities.Resolucion;
//import co.gov.ideamredd.entities.TiposDato;
//import co.gov.ideamredd.entities.TiposImagenMapa;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.apache.log4j.Logger;


/**
 * Carga los datos iniciales de las rutas de configuraci�n. La ruta de Metadatos y Thumbnails vienen del archivo providers.fac de apollo, mientras que el resto vienen de la base de datos
 */
public class CargaDatosInicial {
	
	private static Logger log=Logger.getLogger("SMBCLog");
	private static Connection conn;
	private static DataSource dataSource = ConexionBD.getConnection();
	//private static ArrayList<Georeferenciacion> listaGeorref;
	//private static ArrayList<Proyeccion> listaProyec;
	//private static ArrayList<Resolucion> listaResol;
	//private static ArrayList<TiposDato> listaTipoDatos;
	//private static ArrayList<TiposImagenMapa> listaTipoImgMapa;
	private static ArrayList<Dataset> listaDatasets;
	private static ArrayList<String> listaDatasetsString;
	private static Dataset dataset;
	private static ArrayList<String> listaTiposDoc;
	private static ArrayList<String> listaDetalleDescarga; 
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
	 public static ArrayList<Dataset> getDatasets(){
		 
		 listaDatasets = new ArrayList<Dataset>();
			try {
				log.info( CargaDatosInicial.class+ "Inicio Carga Datos Inicial de Dataset");
				conn = dataSource.getConnection();
				CallableStatement consultaDatasets = conn
						.prepareCall("{call RED_PK_APOLLO2013.Apollo_Consulta(?, ?)}");
				consultaDatasets.registerOutParameter("un_Resultado",
						OracleTypes.CURSOR);
				consultaDatasets.registerOutParameter("sentencia",
						OracleTypes.VARCHAR);
				consultaDatasets.execute();
				ResultSet r = (ResultSet) consultaDatasets.getObject("un_Resultado");
				String rutaMetadato="";
				while (r.next()) {
					dataset = new Dataset();
					dataset.setId(r.getString(1));
					dataset.setNombre(r.getString(2));
					dataset.setFormato(r.getString(3));
					dataset.setFechaAdquisicion(r.getTimestamp(4));
					dataset.setFechaRegistro(r.getTimestamp(5));
					rutaMetadato=r.getString(6).substring(6);
					dataset.setProyeccion(getEPSGMetadatos(rutaMetadato));
					dataset.setNumBandas(getNumBandMetadatos(rutaMetadato));
					dataset.setTamanio(getImageSize(r.getString(7)));
					dataset.setTipoDato(r.getString(8));
					dataset.setResolucion(r.getString(9));
					dataset.setGeoreferenciacion(r.getString(10));
					dataset.setTipoImagenMapa(r.getString(11));
					dataset.setLinkMetadato(getURLPublicacionMetadatoAPOLLO()+r.getString(1)+"/attachment/default");
					listaDatasets.add(dataset);
				}
				
				r.close();
				consultaDatasets.close();
				conn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
				return listaDatasets;
			}
			return listaDatasets;
			
	}
	 public static ArrayList<String> getDatasetsDescargadosString(){
		 
		 listaDetalleDescarga = new ArrayList<String>();
			try {
		
				conn = dataSource.getConnection();
				CallableStatement consultaDatasets = conn
						.prepareCall("{call RED_PK_DETALLEDESCARGA.DetalleDescarga_Consulta(?, ?)}");
				consultaDatasets.registerOutParameter("un_Resultado",
						OracleTypes.CURSOR);
				consultaDatasets.registerOutParameter("sentencia",
						OracleTypes.VARCHAR);
				consultaDatasets.execute();
				ResultSet r = (ResultSet) consultaDatasets.getObject("un_Resultado");
				
				while (r.next()) {
					listaDetalleDescarga.add(
							r.getString(1)+","+
							r.getString(2)+","+
							r.getInt(3)+","+
							r.getString(4)+","+
							r.getString(5)+","+
							new SimpleDateFormat("dd/MM/yyyy").format(r.getTimestamp(6))
							//r.getInt(7)							
							);
				}
				
				r.close();
				consultaDatasets.close();
				conn.close();
				log.info( CargaDatosInicial.class+ "[getListasDescarga] Termino Correctamente");
			} catch (Exception e) {
				e.printStackTrace();
				log.error( CargaDatosInicial.class+ "[getListasDescarga] Fallo" , e);
			}
			return listaDetalleDescarga;
			
	}
	 public static ArrayList<String> getDatasetsString(){
		 
		 listaDatasetsString = new ArrayList<String>();
			try {
				
				conn = dataSource.getConnection();
				CallableStatement consultaDatasets = conn
						.prepareCall("{call RED_PK_APOLLO2013.Apollo_Consulta(?, ?)}");
				consultaDatasets.registerOutParameter("un_Resultado",
						OracleTypes.CURSOR);
				consultaDatasets.registerOutParameter("sentencia",
						OracleTypes.VARCHAR);
				consultaDatasets.execute();
				ResultSet r = (ResultSet) consultaDatasets.getObject("un_Resultado");
				
				while (r.next()) {
					
							
					listaDatasetsString.add(
							r.getString(1)+","+
							r.getString(2)+","+
							r.getString(3)+","+
							new SimpleDateFormat("dd/MM/yyyy").format(r.getTimestamp(4))+","+
							new SimpleDateFormat("dd/MM/yyyy").format(r.getTimestamp(5))+","+
							getEPSGMetadatos(r.getString(6).substring(6))+","+
							getNumBandMetadatos(r.getString(6).substring(6))+","+
							getImageSize(r.getString(7))+","+
							r.getString(8)+","+
							r.getString(9)+","+
							r.getString(10)+","+
							r.getString(11)+","+
							getURLPublicacionMetadatoAPOLLO()+r.getString(1)+"/attachment/default"
							);
				}
				
			r.close();
				consultaDatasets.close();
				conn.close();
				log.info(CargaDatosInicial.class+" [getDatasetsString] Termino correctamente");
			} catch (Exception e) {
				e.printStackTrace();
				log.error( CargaDatosInicial.class+ "[getListasDescarga] Fallo");
				return listaDatasetsString;
			}
			return listaDatasetsString;
			
	}
		public static ArrayList<String> getTiposDocumento() {
			listaTiposDoc = new ArrayList<String>();
			try {
			
				conn = dataSource.getConnection();
				CallableStatement consultaTipoDocumento = conn
						.prepareCall("{call RED_PK_TABLASTIPO.TipoIdentificacion_Consulta(?, ?)}");
				consultaTipoDocumento.setString("un_Nombre", "");
				consultaTipoDocumento.registerOutParameter("un_Resultado",
						OracleTypes.CURSOR);
				consultaTipoDocumento.execute();
				ResultSet resultSet = (ResultSet) consultaTipoDocumento
						.getObject("un_Resultado");
				while (resultSet.next()) {
					//listaTiposDoc.add(resultSet.getInt(1)+"-"+resultSet.getString(2));
					listaTiposDoc.add(resultSet.getString(2));
				}
				log.info(CargaDatosInicial.class+"[getTiposDocumento] Termino");
				resultSet.close();
				consultaTipoDocumento.close();
				conn.close();
			} catch (Exception e) {
				log.error(CargaDatosInicial.class+"[getTiposDocumento] Fallo", e);
				e.printStackTrace();

			}
			return listaTiposDoc;
		}
	public static ArrayList<String> getTipoImgMap(){
		ArrayList<String> listaTiposImag = new ArrayList<String>();
		
		String linea = null;
		Scanner fileToRead = null;
			try {
				 fileToRead = new Scanner(new File(getRutaQueryable())); 
				 for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null; ) {
					 if(line.contains("TipoImagenMapa")){
						 linea=line.substring(line.indexOf("enum=\"")+6,line.indexOf("\" default")); 
						 
						 break;
					 }
					 
				 }
				 fileToRead.close();
				 
			 } catch (FileNotFoundException ex) {
				 System.out.println("The file " + getRutaQueryable() + " could not be found! " + ex.getMessage());
			 return listaTiposImag;
			 }
		
			listaTiposImag=dividirLista(linea, ",");
		return listaTiposImag;
		
	}
	public static ArrayList<String> getResolucion(){
		ArrayList<String> listaResolucion = new ArrayList<String>();
		
		String linea = null;
		Scanner fileToRead = null;
			try {
				 fileToRead = new Scanner(new File(getRutaQueryable())); 
				 for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null; ) {
					 if(line.contains("Resolucion")){
						 linea=line.substring(line.indexOf("enum=\"")+6,line.indexOf("\" default"));
						
						 break;
					 }
					 
				 }
				 fileToRead.close();
				 
			 } catch (FileNotFoundException ex) {
				 System.out.println("The file " + getRutaQueryable() + " could not be found! " + ex.getMessage());
				 return listaResolucion;
			 } 		
			listaResolucion=dividirLista(linea, ",");
		return listaResolucion;
		
	}
	public static ArrayList<String> getTipoDato(){
		ArrayList<String> listaTipoDato = new ArrayList<String>();
		
		String linea = null;
		Scanner fileToRead = null;
			try {
				 fileToRead = new Scanner(new File(getRutaQueryable())); 
				 for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null; ) {
					 if(line.contains("TipoDato")){
						 linea=line.substring(line.indexOf("enum=\"")+6,line.indexOf("\" default"));
						 break;
					 }
					 
				 }
				 fileToRead.close();
				 
			 } catch (FileNotFoundException ex) {
				 System.out.println("The file " + getRutaQueryable() + " could not be found! " + ex.getMessage());
				 return listaTipoDato;
			 } 
		listaTipoDato=dividirLista(linea, ",");
		return listaTipoDato;
		
	}
	public static String getEPSGMetadatos(String rutaMetadata) {
		String linea = "EPSG:NA";
		Scanner fileToRead = null;
			try {
				 fileToRead = new Scanner(new File(rutaMetadata)); 
				  for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null; ) {
					 if(line.contains("EPSG")){
						 linea=line.substring(line.indexOf("EPSG:"),line.indexOf("</gco:"));
						 break;
					 }
					 
				 }
				 fileToRead.close();
				 return linea;
			 } catch (FileNotFoundException ex) {
				 System.out.println("The file " + rutaMetadata + " could not be found! " + ex.getMessage());
				 linea="No available";
				 fileToRead.close();
			 return linea;
			 } finally {
				 //fileToRead.close();
			 return linea;
		 }
	 }
	 public static String getNumBandMetadatos(String rutaMetadata) {
			String linea = "BAND:NA";
			Scanner fileToRead = null;
				try {
					 fileToRead = new Scanner(new File(rutaMetadata)); 
					  for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null; ) {
						 if(line.contains("BAND")){
							 linea=line.substring(line.indexOf("BAND:"),line.indexOf("</gco:"));
							 break;
						 }
					 }
					 fileToRead.close();
					 return linea;
				 } catch (FileNotFoundException ex) {
					 System.out.println("The file " + rutaMetadata + " could not be found! " + ex.getMessage());
					 linea="no available";
					 fileToRead.close();
					 return linea;
				 } finally {
					 //fileToRead.close();
				 return linea;
			 }
		 }
	public static float getImageSize(String pathImage){
		 File file = new File (pathImage);
		 return (float) (file.length()/1048576.0);
	}
	/*
	 * 
	 * 	public static ArrayList<String> getGeorref(){
		ArrayList<String> listaGeorreferenciacion = new ArrayList<String>();
		
		String linea = null;
		Scanner fileToRead = null;
			try {
				 fileToRead = new Scanner(new File(getRutaQueryable())); 
				 for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null; ) {
					 if(line.contains("Georreferenciacion")){
						 linea=line.substring(line.indexOf("enum=\"")+6,line.indexOf("\" default")); 
						 System.out.println(linea);
						 break;
					 }
					 
				 }
				 fileToRead.close();
				 
			 } catch (FileNotFoundException ex) {
				 System.out.println("The file " + getRutaQueryable() + " could not be found! " + ex.getMessage());
			 return listaGeorreferenciacion;
			 }
		
		listaGeorreferenciacion=dividirLista(linea, ",");
		return listaGeorreferenciacion;
		
	}
	
	public static ArrayList<Rutas> getArrayRutasApollo() {
		log = SMBC_Log.Log(CargaDatosInicial.class);
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
				log.info("[Get Rutas Parametros] termino");
				
			} catch (Exception e) {
				log.error("[Get Rutas Parametros] fallo en la consulta de rutas de la tabla de parametros");
				e.printStackTrace();
			}
		return listaRutasApollo;
	}*/
	
	 /**
		 * Obtiene la ruta del archivo Queryables.xml
		 * @return rutaQueryable ruta en la cual se encuentra el archivo Queryables.xml, la obtiene de la base de datos
		 */
	 public static String getRutaQueryable(){
		 String rutaQueryable = "";
			try {
				conn = dataSource.getConnection();
				CallableStatement consultaRutaQueryable = conn
						.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?, ?, ?)}");
				consultaRutaQueryable.setString("un_Nombre", "QUERYABLES");
				consultaRutaQueryable.registerOutParameter("una_Ruta",
						OracleTypes.CURSOR);
				consultaRutaQueryable.registerOutParameter("sentencia",
						OracleTypes.VARCHAR);
				consultaRutaQueryable.execute();
				ResultSet r = (ResultSet) consultaRutaQueryable.getObject("una_Ruta");
				
				while (r.next()) {
					rutaQueryable = r.getString(1);
				}
				
				r.close();
				consultaRutaQueryable.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return rutaQueryable;
		 
	 }
	 /**
		 * Obtiene la URL base de los metadatos publicados por apollo
		 * @return rutaQueryable ruta en la cual se encuentra el archivo Queryables.xml, la obtiene de la base de datos
		 */
	 public static String getURLPublicacionMetadatoAPOLLO(){
		 String URLBaseMetadato = "";
			try {
				conn = dataSource.getConnection();
				CallableStatement consultaRutaQueryable = conn
						.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?, ?, ?)}");
				consultaRutaQueryable.setString("un_Nombre", "URL_METADATO");
				consultaRutaQueryable.registerOutParameter("una_Ruta",
						OracleTypes.CURSOR);
				consultaRutaQueryable.registerOutParameter("sentencia",
						OracleTypes.VARCHAR);
				consultaRutaQueryable.execute();
				ResultSet r = (ResultSet) consultaRutaQueryable.getObject("una_Ruta");
				
				while (r.next()) {
					URLBaseMetadato = r.getString(1);
				}
				
				r.close();
				consultaRutaQueryable.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return URLBaseMetadato;
		 
	 }
	 /**
	  * Convierte un String en una lista
	 * @param linea linea a separar
	 * @param delimitador delimitador de separacion
	 * @return ArrayList con los elementos separados
	 */
	public static ArrayList<String> dividirLista(String linea,String delimitador){
			ArrayList<String> lista= new ArrayList<String>();
			StringTokenizer tok = new StringTokenizer(linea, delimitador, true);
			boolean expectDelim = false;
			while (tok.hasMoreTokens()) {
			    String token = tok.nextToken();
			    if (delimitador.equals(token)) {
			        if (expectDelim) {
			            expectDelim = false;
			            continue;
			        } else {
			            // unexpected delim means empty token
			            token = null;
			        }
			    }
			    lista.add(token);
			    expectDelim = true;
			}
			return lista;
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



}
