package co.gov.ideamredd.reportes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import co.gov.ideamredd.entities.InformacionReporteInventarios;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Stateless
public class GeneradorReportePDF {

	public static final String ARCHIVO_REPORTE = "reporte_inventarios.pdf";
	private static final String[] ENCABEZADOS = { "N\u00FAmero de \u00E1rbol",
			"G\u00E9nero", "Familia", "Biomasa a\u00E9rea", "Carbono",
			"\u00C1rea basal" };

	DecimalFormat format = new DecimalFormat("0.0000");


	// public File generarReporte(String html) throws Exception {
	//
	// Document documento = new Document();
	//
	// PdfWriter writer = PdfWriter.getInstance(documento, new
	// FileOutputStream(ARCHIVO_REPORTE));
	// documento.open();
	// InputStream stream = new
	// ByteArrayInputStream(html.getBytes(StandardCharsets.ISO_8859_1));
	// CSSResolver cssResolver =
	// XMLWorkerHelper.getInstance().getDefaultCssResolver(false);
	// cssResolver.addCss(ESTILOS_TABLAS, true);
	// XMLWorkerHelper.getInstance().parseXHtml(writer, documento, stream);
	// documento.close();
	// stream.close();
	// return null;
	// }

	//TODO: Generar un nombre unico de reporte

	public void generarReporte(
			Map<String, List<InformacionReporteInventarios>> mapaResultados,
			String path) throws IOException, DocumentException {

		Iterable<String> especies = mapaResultados.keySet();
		System.out.println("Generando PDF con listado de especies");
		Document document = new Document();
		String pdf_name=path + ARCHIVO_REPORTE;
		PdfWriter.getInstance(document, new FileOutputStream(pdf_name));
		System.out.println("Creando PDF: "+pdf_name);
		document.open();
		Image image1 = Image.getInstance(path + "EncabezadoRptEspecies.png");
		image1.scaleAbsolute(500, 50);
		document.add(image1);
		for (String especie : especies) {
			document.add(createFirstTable(especie, mapaResultados.get(especie)));
			System.out.println("Llenado de PDF reporte con especie: " + especie);
		}
		document.close();

	}

	private PdfPTable createFirstTable(String especie,
			List<InformacionReporteInventarios> resultados) {

		PdfPTable table = new PdfPTable(6);

		PdfPCell cell;
		cell = new PdfPCell(new Phrase(especie));
		cell.setColspan(6);
		cell.setBackgroundColor(new BaseColor(245, 245, 245));
		cell.setBorderColor(new BaseColor(204, 204, 204));
		table.addCell(cell);

		PdfPCell cellEncabezado, cellContenido;
		for (int i = 0; i < 6; i++) {
			cellEncabezado = new PdfPCell(new Phrase(ENCABEZADOS[i],
					FontFactory.getFont("Serif", 11, new BaseColor(102, 102,
							102))));
			cellEncabezado.setBorderColor(new BaseColor(204, 204, 204));
			cellEncabezado.setBorderWidthLeft(2f);
			cellEncabezado.setBorderWidthRight(1f);
			cellEncabezado.setBackgroundColor(new BaseColor(160, 160, 160));
			table.addCell(cellEncabezado);
		}

		int contColor = 0;

		BigDecimal biomasaTotal = BigDecimal.ZERO;
		BigDecimal carbonoTotal = BigDecimal.ZERO;
		BigDecimal areaTotal = BigDecimal.ZERO;

		BigDecimal cantidadRegistros = new BigDecimal(resultados.size());

		for (InformacionReporteInventarios informacionReporteInventarios : resultados) {
			contColor++;
			String[] informacion = extraerInformacion(informacionReporteInventarios);
			for (int i = 0; i < ENCABEZADOS.length; i++) {

				cellContenido = new PdfPCell(new Phrase(informacion[i],
						FontFactory.getFont("Serif", 9, new BaseColor(102, 102,
								102))));

				if (contColor % 2 == 0) {
					cellContenido.setBackgroundColor(new BaseColor(245, 245,
							245));
				} else {
					cellContenido.setBackgroundColor(new BaseColor(221, 221,
							221));
				}
				cellContenido.setBorderColor(new BaseColor(204, 204, 204));

				cellContenido.setBorderWidthLeft(2f);
				cellContenido.setBorderWidthRight(1f);
				table.addCell(cellContenido);
			}

			biomasaTotal = biomasaTotal.add(informacionReporteInventarios
					.getBiomasa());
			carbonoTotal = carbonoTotal.add(informacionReporteInventarios
					.getCarbono());
			areaTotal = areaTotal.add(informacionReporteInventarios
					.getAreaBasal());
		}

		PdfPCell celdaEmpty = new PdfPCell(new Phrase(" ", FontFactory.getFont(
				"Serif", 10, new BaseColor(102, 102, 102))));
		celdaEmpty.setBorderColor(new BaseColor(204, 204, 204));
		celdaEmpty.setBorderWidthLeft(2f);
		celdaEmpty.setBorderWidthRight(1f);
		celdaEmpty.setBackgroundColor(new BaseColor(160, 160, 160));

		PdfPCell celdaTotal = new PdfPCell(new Phrase("Total",
				FontFactory.getFont("Serif", 10, new BaseColor(102, 102, 102))));
		celdaTotal.setBorderColor(new BaseColor(204, 204, 204));
		celdaTotal.setBorderWidthLeft(2f);
		celdaTotal.setBorderWidthRight(1f);
		celdaTotal.setBackgroundColor(new BaseColor(160, 160, 160));
		PdfPCell celdaTotalBiomasa = new PdfPCell(new Phrase(
				format.format(biomasaTotal), FontFactory.getFont("Serif", 10,
						new BaseColor(102, 102, 102))));
		celdaTotalBiomasa.setBorderColor(new BaseColor(204, 204, 204));
		celdaTotalBiomasa.setBorderWidthLeft(2f);
		celdaTotalBiomasa.setBorderWidthRight(1f);
		celdaTotalBiomasa.setBackgroundColor(new BaseColor(160, 160, 160));
		PdfPCell celdaTotalCarbono = new PdfPCell(new Phrase(
				format.format(carbonoTotal), FontFactory.getFont("Serif", 10,
						new BaseColor(102, 102, 102))));
		celdaTotalCarbono.setBorderColor(new BaseColor(204, 204, 204));
		celdaTotalCarbono.setBorderWidthLeft(2f);
		celdaTotalCarbono.setBorderWidthRight(1f);
		celdaTotalCarbono.setBackgroundColor(new BaseColor(160, 160, 160));

		table.addCell(celdaTotal);
		table.addCell(celdaEmpty);
		table.addCell(celdaEmpty);
		table.addCell(celdaTotalBiomasa);
		table.addCell(celdaTotalCarbono);
		table.addCell(celdaEmpty);

		PdfPCell celdaPromedio = new PdfPCell(new Phrase("Promedio",
				FontFactory.getFont("Serif", 10, new BaseColor(102, 102, 102))));
		celdaPromedio.setBorderColor(new BaseColor(204, 204, 204));
		celdaPromedio.setBorderWidthLeft(2f);
		celdaPromedio.setBorderWidthRight(1f);
		celdaPromedio.setBackgroundColor(new BaseColor(160, 160, 160));
		PdfPCell celdaPromedioBiomasa = new PdfPCell(new Phrase(
				format.format(biomasaTotal.divide(cantidadRegistros,
						RoundingMode.HALF_EVEN)), FontFactory.getFont("Serif",
						10, new BaseColor(102, 102, 102))));
		celdaPromedioBiomasa.setBorderColor(new BaseColor(204, 204, 204));
		celdaPromedioBiomasa.setBorderWidthLeft(2f);
		celdaPromedioBiomasa.setBorderWidthRight(1f);
		celdaPromedioBiomasa.setBackgroundColor(new BaseColor(160, 160, 160));
		PdfPCell celdaPromedioCarbono = new PdfPCell(new Phrase(
				format.format(carbonoTotal.divide(cantidadRegistros,
						RoundingMode.HALF_EVEN)), FontFactory.getFont("Serif",
						10, new BaseColor(102, 102, 102))));
		celdaPromedioCarbono.setBorderColor(new BaseColor(204, 204, 204));
		celdaPromedioCarbono.setBorderWidthLeft(2f);
		celdaPromedioCarbono.setBorderWidthRight(1f);
		celdaPromedioCarbono.setBackgroundColor(new BaseColor(160, 160, 160));
		PdfPCell celdaPromedioArea = new PdfPCell(new Phrase(
				format.format(areaTotal.divide(cantidadRegistros,
						RoundingMode.HALF_EVEN)), FontFactory.getFont("Serif",
						10, new BaseColor(102, 102, 102))));
		celdaPromedioArea.setBorderColor(new BaseColor(204, 204, 204));
		celdaPromedioArea.setBorderWidthLeft(2f);
		celdaPromedioArea.setBorderWidthRight(1f);
		celdaPromedioArea.setBackgroundColor(new BaseColor(160, 160, 160));

		table.addCell(celdaPromedio);
		table.addCell(celdaEmpty);
		table.addCell(celdaEmpty);
		table.addCell(celdaPromedioBiomasa);
		table.addCell(celdaPromedioCarbono);
		table.addCell(celdaPromedioArea);

		return table;
	}

	private String[] extraerInformacion(
			InformacionReporteInventarios informacionReporteInventarios) {
		String[] informacion = new String[6];

		informacion[0] = informacionReporteInventarios.getNumeroArbol();
		informacion[1] = informacionReporteInventarios.getGenero();
		informacion[2] = informacionReporteInventarios.getFamilia();
		informacion[3] = format.format(informacionReporteInventarios
				.getBiomasa());
		informacion[4] = format.format(informacionReporteInventarios
				.getCarbono());
		informacion[5] = format.format(informacionReporteInventarios
				.getAreaBasal());

		return informacion;
	}

	/*
	 * public String getRutaTemp() { conn = conexion.establecerConexion();
	 * String rutaTemporales = ""; try {
	 * 
	 * CallableStatement consultaRutaProvider = conn
	 * .prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?, ?, ?)}");
	 * consultaRutaProvider.setString("un_Nombre", "templates");
	 * consultaRutaProvider.registerOutParameter("una_Ruta",
	 * OracleTypes.CURSOR);
	 * consultaRutaProvider.registerOutParameter("sentencia",
	 * OracleTypes.VARCHAR); consultaRutaProvider.execute(); ResultSet r =
	 * (ResultSet) consultaRutaProvider .getObject("una_Ruta");
	 * 
	 * while (r.next()) { rutaTemporales = r.getString(1); }
	 * 
	 * r.close(); consultaRutaProvider.close(); conn.close(); } catch (Exception
	 * e) { log.error(
	 * "[generarReporte] Fallo, en la consulta te ruta REPORTES_TEMP, verifique que dicha ruta se encuentra registrada en la tabla RED_PARAMETROS"
	 * , e); e.printStackTrace(); } return rutaTemporales;
	 * 
	 * }
	 * 
	 * public String getRutaTemplates() { conn = conexion.establecerConexion();
	 * String rutaTemporales = ""; try {
	 * 
	 * CallableStatement consultaRutaProvider = conn
	 * .prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?, ?, ?)}");
	 * consultaRutaProvider.setString("un_Nombre", "templates");
	 * consultaRutaProvider.registerOutParameter("una_Ruta",
	 * OracleTypes.CURSOR);
	 * consultaRutaProvider.registerOutParameter("sentencia",
	 * OracleTypes.VARCHAR); consultaRutaProvider.execute(); ResultSet r =
	 * (ResultSet) consultaRutaProvider .getObject("una_Ruta");
	 * 
	 * while (r.next()) { rutaTemporales = r.getString(1); }
	 * 
	 * r.close(); consultaRutaProvider.close(); conn.close(); } catch (Exception
	 * e) { log.error(
	 * "[generarReporte] Fallo, en la consulta te ruta REPORTES_TEMP, verifique que dicha ruta se encuentra registrada en la tabla RED_PARAMETROS"
	 * , e); e.printStackTrace(); }
	 * 
	 * return rutaTemporales;
	 * 
	 * }
	 */

}
