// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.descarga.dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;
import oracle.jdbc.OracleTypes;

// import co.gov.ideamredd.mbc.conexion.ConexionBD;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

/**
 * Métodos para generar el reporte de descargas en xls o pdf
 * 
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class GenerarReporte {

	static StringBuffer		stringBufferOfData	= new StringBuffer();
	private String			rutaReporte;
	private XSSFSheet		hoja;
	private XSSFWorkbook	libro;
	private String			nombreReporte;
	private Util			util				= new Util();
	private Connection		conexion;
	private DatosConexion	datosConexion;

	/**
	 * Genera el reporte en archivo de xls
	 * 
	 * @param dataSetsEncabez
	 * @param informacion
	 * @param nombreReporte
	 * @param numColumnas
	 * @return String de la ruta del archivo xls del reporte
	 */
	public String generarReporteXls(String dataSetsEncabez, String informacion, String nombreReporte, int numColumnas) {

		try {
			this.nombreReporte = nombreReporte;
			this.rutaReporte = getRutaTemp();
			generarLibroYEncabezado(dividirLista(dataSetsEncabez, ","));
			cargarInformacion(dividirLista(informacion, ","), numColumnas);
			util.escribirReportes(rutaReporte, nombreReporte, libro);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return rutaReporte + nombreReporte;

	}

	/**
	 * Genera libro y encabezado del reporte xls
	 * 
	 * @param encabezado
	 */
	private void generarLibroYEncabezado(ArrayList<String> encabezado) {
		libro = new XSSFWorkbook();
		hoja = libro.createSheet();
		libro.setSheetName(0, nombreReporte);
		util.estiloTituloReporte(libro);
		XSSFRow headerRow = hoja.createRow((short) 1);
		for (int i = 0; i < encabezado.size(); i++) {
			headerRow.createCell(i).setCellValue(encabezado.get(i));
		}

	}

	/**
	 * Carga la información en el libro
	 * 
	 * @param informacion
	 * @param numColumnas
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
	 * @param linea
	 *            linea a separar
	 * @param delimitador
	 *            delimitador de separacion
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
	 * Obtiene la ruta de reportes temporales
	 * 
	 * @return rutaProviders ruta de los temporales, la obtiene de la base de datos
	 */
	public String getRutaTemp() {
		String rutaTemporales = "";
		try {
			datosConexion = new DatosConexion();
			conexion = DriverManager.getConnection(datosConexion.getPropiedad("url"), datosConexion.getPropiedad("usuario"), datosConexion.getPropiedad("password"));

			CallableStatement consultaRutaProvider = conexion.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?, ?, ?)}");
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
			conexion.close();
		}
		catch (Exception e) {
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

		String rutaTemporales = "";
		try {

			datosConexion = new DatosConexion();
			conexion = DriverManager.getConnection(datosConexion.getPropiedad("url"), datosConexion.getPropiedad("usuario"), datosConexion.getPropiedad("password"));

			CallableStatement consultaRutaProvider = conexion.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?, ?, ?)}");
			// consultaRutaProvider.setString("un_Nombre", "RUTA_TEMPLATES");
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
			conexion.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return rutaTemporales;

	}

	/**
	 * Genera el reporte en PDF
	 * 
	 * @param dataSetsEncabez
	 * @param informacion
	 * @param nombreReporte
	 * @param numColumnas
	 * @return String de la ruta del reporte en PDF
	 */
	public String generarReportePDF(String dataSetsEncabez, String informacion, String nombreReporte, int numColumnas) {

		this.nombreReporte = nombreReporte;
		this.rutaReporte = getRutaTemp();

		try {
			createPdf(rutaReporte + "/" + nombreReporte + ".pdf", dataSetsEncabez, informacion, numColumnas);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rutaReporte + nombreReporte + ".pdf";
	}

	/**
	 * Crea el PDF
	 * 
	 * @param filename
	 * @param dataSetsEncabez
	 * @param informacion
	 * @param numColumnas
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void createPdf(String filename, String dataSetsEncabez, String informacion, int numColumnas) throws IOException, DocumentException {
		ArrayList<String> encabezados = new ArrayList<String>();
		ArrayList<String> informaciones = new ArrayList<String>();
		encabezados = dividirLista(dataSetsEncabez, ",");
		informaciones = dividirLista(informacion, ",");
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		String dato = getRutaTemplates();
		Image image1 = Image.getInstance(dato + "/Encabezado.png");
		image1.scaleAbsolute(500, 50);
		document.add(image1);
		document.add(createFirstTable(encabezados, informaciones, informaciones.size() / numColumnas));
		document.close();
	}

	/**
	 * Crea la primera tabla
	 * 
	 * @param cantidad
	 * @param informaciones
	 * @return our first table
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

	/**
	 * Retorna la ruta del reporte
	 * 
	 * @return String de la ruta del reporte
	 */
	public String getRutaReporte() {
		return rutaReporte;
	}

	/**
	 * Establece la ruta del reporte
	 * 
	 * @param rutaReporte
	 */
	public void setRutaReporte(String rutaReporte) {
		this.rutaReporte = rutaReporte;
	}

	/**
	 * Retorna el nombre del reporte
	 * 
	 * @return String del nombre del reporte
	 */
	public String getNombreReporte() {
		return nombreReporte;
	}

	/**
	 * Establece el nombre del reporte
	 * 
	 * @param reporte
	 */
	public void setNombreReporte(String reporte) {
		this.nombreReporte = reporte;
	}

}
