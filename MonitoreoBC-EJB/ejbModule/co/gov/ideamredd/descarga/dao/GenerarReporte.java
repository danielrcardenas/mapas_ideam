// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.descarga.dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;
import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.util.Util;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@Stateless
public class GenerarReporte {

	@EJB
	ConexionBD				conexion;
	private Connection		conn;
	static StringBuffer		stringBufferOfData	= new StringBuffer();
	private String			rutaReporte;
	private XSSFSheet		hoja;
	private XSSFWorkbook	libro;
	private String			nombreReporte;
	private static Logger	log					= Logger.getLogger("SMBCLog");

	/**
	 * Edita la ruta de los thumnails del archivo providers.fac
	 * 
	 * @param rutaProviders ruta en la cual se encuentra el archivo providers.fac
	 * @param nuevaRutaThumbnail ruta en la cual se encuentra se van a almacenar los thumbnails
	 */
	public String generarReporteXls(String dataSetsEncabez, String informacion, String nombreReporte, int numColumnas) {
		log.info(GenerarReporte.class + " Inicio creacion reporte");

		try {
			this.nombreReporte = nombreReporte;
			this.rutaReporte = getRutaTemp();
			generarLibroYEncabezado(dividirLista(dataSetsEncabez, ","));
			cargarInformacion(dividirLista(informacion, ","), numColumnas);
			Util.escribirReportes(rutaReporte, nombreReporte, libro);
			log.info(GenerarReporte.class + "[GenerarReporte] Termino");
		}
		catch (Exception e) {
			log.error(GenerarReporte.class + "[GenerarReporte] Fallo, no se puede generar el reporte de inventario de imagenes", e);
			e.printStackTrace();
		}
		return rutaReporte + nombreReporte;

	}

	/**
	 * Genera el archivo de excel con el encabeado
	 * 
	 * @param encabezado encabezados de la tabla
	 */
	private void generarLibroYEncabezado(ArrayList<String> encabezado) {
		libro = new XSSFWorkbook();
		hoja = libro.createSheet();
		libro.setSheetName(0, nombreReporte);
		Util.estiloTituloReporte(libro);
		XSSFRow headerRow = hoja.createRow((short) 1);
		for (int i = 0; i < encabezado.size(); i++) {
			headerRow.createCell(i).setCellValue(encabezado.get(i));
		}

	}

	/**
	 * Carga el contenido de la tabla
	 * 
	 * @param informacion información a ingresar en la tabla
	 * @param numColumnas número de columnas de la tabla
	 */
	private void cargarInformacion(ArrayList<String> informacion, int numColumnas) {
		XSSFRow cantidadItems = hoja.createRow((short) 0);
		XSSFRow info;
		int contador = 0;
		for (int i = 0; i < informacion.size() / numColumnas; i++) {
			info = hoja.createRow((short) i + 2);
			for (int j = 0; j < numColumnas; j++) {
				info.createCell(j).setCellValue(informacion.get(contador));
				contador++;
			}
		}
		cantidadItems.createCell(0).setCellValue("Cantidad Items:");
		cantidadItems.createCell(1).setCellValue(informacion.size() / numColumnas);

	}

	/**
	 * Convierte un String en una lista
	 * 
	 * @param linea linea a separar
	 * @param delimitador delimitador de separacion
	 * @return ArrayList con los elementos separados
	 */
	public static ArrayList<String> dividirLista(String linea, String delimitador) {
		ArrayList<String> lista = new ArrayList<String>();
		StringTokenizer tok = new StringTokenizer(linea, delimitador, true);
		boolean expectDelim = false;
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if (delimitador.equals(token)) {
				if (expectDelim) {
					expectDelim = false;
					continue;
				}
				else {
					token = null;
				}
			}
			lista.add(token);
			expectDelim = true;
		}
		return lista;
	}

	/**
	 * Obtiene la ruta de reportes temporales
	 * 
	 * @return rutaProviders ruta de los temporales, la obtiene de la base de datos
	 */
	public String getRutaTemp() {
		conn = conexion.establecerConexion();
		String rutaTemporales = "";
		try {

			CallableStatement consultaRutaProvider = conn.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?, ?, ?)}");
			consultaRutaProvider.setString("un_Nombre", "REPORTES_TEMP");
			consultaRutaProvider.registerOutParameter("una_Ruta", OracleTypes.CURSOR);
			consultaRutaProvider.registerOutParameter("sentencia", OracleTypes.VARCHAR);
			consultaRutaProvider.execute();
			ResultSet r = (ResultSet) consultaRutaProvider.getObject("una_Ruta");

			while (r.next()) {
				rutaTemporales = r.getString(1);
			}

			r.close();
			consultaRutaProvider.close();
			conn.close();
		}
		catch (Exception e) {
			log.error("[generarReporte] Fallo, en la consulta te ruta REPORTES_TEMP, verifique que dicha ruta se encuentra registrada en la tabla RED_PARAMETROS", e);
			e.printStackTrace();
		}
		return rutaTemporales;

	}

	/**
	 * Obtiene la ruta de reportes temporales
	 * 
	 * @return rutaProviders ruta de los temporales, la obtiene de la base de datos
	 */
	public String getRutaTemplates() {
		conn = conexion.establecerConexion();
		String rutaTemporales = "";
		try {

			CallableStatement consultaRutaProvider = conn.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?, ?, ?)}");
			consultaRutaProvider.setString("un_Nombre", "templates");
			consultaRutaProvider.registerOutParameter("una_Ruta", OracleTypes.CURSOR);
			consultaRutaProvider.registerOutParameter("sentencia", OracleTypes.VARCHAR);
			consultaRutaProvider.execute();
			ResultSet r = (ResultSet) consultaRutaProvider.getObject("una_Ruta");

			while (r.next()) {
				rutaTemporales = r.getString(1);
			}

			r.close();
			consultaRutaProvider.close();
			conn.close();
		}
		catch (Exception e) {
			log.error("[generarReporte] Fallo, en la consulta te ruta REPORTES_TEMP, verifique que dicha ruta se encuentra registrada en la tabla RED_PARAMETROS", e);
			e.printStackTrace();
		}
		return rutaTemporales;

	}

	/**
	 * Genera El reporte en PDF
	 * 
	 * @param dataSetsEncabez Lista de encabezados de la tabla separados por coma
	 * @param informacion Información de la tabla del reporte a generar
	 * @param nombreReporte Nombre del reporte a generar
	 * @param numColumnas Numero de Columnas de la tabla
	 * @return Ubicacion y nombre del reporte generado
	 */
	public String generarReportePDF(String dataSetsEncabez, String informacion, String nombreReporte, int numColumnas) {

		this.nombreReporte = nombreReporte;
		this.rutaReporte = getRutaTemp();

		String ruta = rutaReporte + "/" + nombreReporte + ".pdf";

		try {
			createPdf(ruta, dataSetsEncabez, informacion, numColumnas);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (DocumentException e) {
			e.printStackTrace();
		}

		return ruta;
	}

	/**
	 * Crea el PDF
	 * 
	 * @param filename nombre del archivo
	 * @param dataSetsEncabez lista de encabezados de la tabla a generar en el pdf separada por coma
	 * @param informacion datos a incluir en a tabla del reporte
	 * @param numColumnas numero de columnas de a tabla
	 * @throws IOException exepcion lazada si se genera error guardando el pdf
	 * @throws DocumentException exepcion lazada si se genera error creando el pdf
	 */
	public void createPdf(String filename, String dataSetsEncabez, String informacion, int numColumnas) throws IOException, DocumentException {
		ArrayList<String> encabezados = new ArrayList<String>();
		ArrayList<String> informaciones = new ArrayList<String>();
		encabezados = dividirLista(dataSetsEncabez, ",");
		informaciones = dividirLista(informacion, ",");
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		Image image1 = Image.getInstance(getRutaTemplates() + "Encabezado.png");
		image1.scaleAbsolute(500, 50);
		document.add(image1);
		document.add(createFirstTable(encabezados, informaciones, informaciones.size() / numColumnas));
		document.close();

	}

	/**
	 * Crea la tabla del reporte pdf
	 * 
	 * @param cantidad número de elementos a colocar en la tabla
	 * @param informaciones información del pdf
	 * @return tabla del pdf
	 */
	public static PdfPTable createFirstTable(ArrayList<String> encabezado, ArrayList<String> informaciones, int cantidad) {

		PdfPTable table = new PdfPTable(1);

		PdfPCell cell;
		cell = new PdfPCell(new Phrase("Cantidad de Elementos: " + cantidad));
		cell.setColspan(1);
		cell.setBackgroundColor(new BaseColor(245, 245, 245));
		cell.setBorderColor(new BaseColor(204, 204, 204));
		table.addCell(cell);

		int contador = 0;
		int contColor = 0;
		String contenido;
		while (contador < informaciones.size()) {
			PdfPCell celltmp;
			contenido = "";
			for (int i = 0; i < encabezado.size(); i++) {
				contenido = contenido + encabezado.get(i) + ": " + informaciones.get(contador) + "\n";
				contador++;
			}
			celltmp = new PdfPCell(new Phrase("\n" + contenido + "\n", FontFactory.getFont("Serif", 9, new BaseColor(102, 102, 102))));
			contColor++;
			if (contColor % 2 == 0) {
				celltmp.setBackgroundColor(new BaseColor(245, 245, 245));
			}
			else {
				celltmp.setBackgroundColor(new BaseColor(221, 221, 221));
			}
			celltmp.setBorderColor(new BaseColor(204, 204, 204));

			celltmp.setBorderWidthLeft(2f);
			celltmp.setBorderWidthRight(1f);
			table.addCell(celltmp);
		}
		return table;
	}

	public String getRutaReporte() {
		return rutaReporte;
	}

	public void setRutaReporte(String rutaReporte) {
		this.rutaReporte = rutaReporte;
	}

	public String getNombreReporte() {
		return nombreReporte;
	}

	public void setNombreReporte(String reporte) {
		this.nombreReporte = reporte;
	}

}
