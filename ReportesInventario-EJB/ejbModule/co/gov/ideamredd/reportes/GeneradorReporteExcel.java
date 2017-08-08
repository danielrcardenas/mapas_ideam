package co.gov.ideamredd.reportes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import co.gov.ideamredd.entities.InformacionReporteInventarios;

import com.itextpdf.text.DocumentException;

@Stateless
public class GeneradorReporteExcel {

	public static final String ARCHIVO_REPORTE = "reporte_inventarios.xlsx";
	private static final String[] ENCABEZADOS = { "N\u00FAmero de \u00E1rbol",
			"G\u00E9nero", "Familia", "Biomasa a\u00E9reo", "Carbono",
			"\u00C1rea basal" };

	Map<String, CellStyle> styles;

	DecimalFormat format = new DecimalFormat("0.0000");
	private int rowIndex = 7;

	public void generarReporte(
			Map<String, List<InformacionReporteInventarios>> mapaResultados,
			String path) throws IOException, DocumentException {

		Iterable<String> especies = mapaResultados.keySet();

		Workbook workbook = new XSSFWorkbook();
		styles = createStyles(workbook);
		XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Resultados");

		InputStream inputStream = new FileInputStream(path
				+ "EncabezadoRptEspecies.png");
		byte[] bytes = IOUtils.toByteArray(inputStream);
		int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
		inputStream.close();

		CreationHelper helper = workbook.getCreationHelper();
		Drawing drawing = sheet.createDrawingPatriarch();

		ClientAnchor anchor = helper.createClientAnchor();

		anchor.setCol1(1);
		anchor.setRow1(1);

		Picture pict = drawing.createPicture(anchor, pictureIdx);

		for (String especie : especies) {
			insertTable(especie, mapaResultados.get(especie), sheet);
			rowIndex++;
		}

		pict.resize();
		FileOutputStream out = new FileOutputStream(path + ARCHIVO_REPORTE);
		workbook.write(out);
		out.close();
		rowIndex = 7;
	}

	private void insertTable(String especie,
			List<InformacionReporteInventarios> list, XSSFSheet sheet) {
		Row filaTitulo = sheet.createRow(rowIndex);
		filaTitulo.setHeightInPoints(45);
		Cell celdaTitulo = filaTitulo.createCell(0);
		celdaTitulo.setCellValue(especie);
		celdaTitulo.setCellStyle(styles.get("title"));

		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$" + (rowIndex + 1)
				+ ":$F$" + (rowIndex + 1)));

		Row filaHeader = sheet.createRow(++rowIndex);
		filaHeader.setHeightInPoints(40);

		Cell celdaHeader;
		for (int i = 0; i < 6; i++) {
			celdaHeader = filaHeader.createCell(i);
			celdaHeader.setCellValue(ENCABEZADOS[i]);
			celdaHeader.setCellStyle(styles.get("header"));
		}

		BigDecimal biomasaTotal = BigDecimal.ZERO;
		BigDecimal carbonoTotal = BigDecimal.ZERO;
		BigDecimal areaTotal = BigDecimal.ZERO;
		BigDecimal cantidadRegistros = new BigDecimal(list.size());

		for (InformacionReporteInventarios registro : list) {
			rowIndex++;
			String[] informacion = extraerInformacion(registro);
			Row filaRegistro = sheet.createRow(rowIndex);

			for (int i = 0; i < 6; i++) {
				Cell celdaRegistro = filaRegistro.createCell(i);
				celdaRegistro.setCellValue(informacion[i]);
				celdaRegistro.setCellStyle(styles.get("cell"));
			}

			biomasaTotal = biomasaTotal.add(registro.getBiomasa());
			carbonoTotal = carbonoTotal.add(registro.getCarbono());
			areaTotal = areaTotal.add(registro.getAreaBasal());

		}

		Row filaTotales = sheet.createRow(++rowIndex);
		Cell celdaTotal = filaTotales.createCell(0);
		celdaTotal.setCellValue("Total");
		celdaTotal.setCellStyle(styles.get("formula"));

		celdaTotal = filaTotales.createCell(3);
		celdaTotal.setCellValue(format.format(biomasaTotal));
		celdaTotal.setCellStyle(styles.get("formula_2"));

		celdaTotal = filaTotales.createCell(4);
		celdaTotal.setCellValue(format.format(carbonoTotal));
		celdaTotal.setCellStyle(styles.get("formula_2"));

		Row filaPromedios = sheet.createRow(++rowIndex);
		Cell celdaPromedios = filaPromedios.createCell(0);
		celdaPromedios.setCellValue("Promedio");
		celdaPromedios.setCellStyle(styles.get("formula"));

		celdaPromedios = filaPromedios.createCell(3);
		celdaPromedios.setCellValue(format.format(biomasaTotal.divide(
				cantidadRegistros, RoundingMode.HALF_EVEN)));
		celdaPromedios.setCellStyle(styles.get("formula_2"));

		celdaPromedios = filaPromedios.createCell(4);
		celdaPromedios.setCellValue(format.format(carbonoTotal.divide(
				cantidadRegistros, RoundingMode.HALF_EVEN)));
		celdaPromedios.setCellStyle(styles.get("formula_2"));

		celdaPromedios = filaPromedios.createCell(5);
		celdaPromedios.setCellValue(format.format(areaTotal.divide(
				cantidadRegistros, RoundingMode.HALF_EVEN)));
		celdaPromedios.setCellStyle(styles.get("formula_2"));

		for (int i = 0; i < 6; i++) {
			sheet.setColumnWidth(i, 20 * 256);
		}

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
	 * (ResultSet) consultaRutaProvider.getObject("una_Ruta");
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
	 * (ResultSet) consultaRutaProvider.getObject("una_Ruta");
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

	/**
	 * Create a library of cell styles
	 */
	private Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		CellStyle style;
		Font titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 18);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFont(titleFont);
		styles.put("title", style);

		Font monthFont = wb.createFont();
		monthFont.setFontHeightInPoints((short) 11);
		monthFont.setColor(IndexedColors.WHITE.getIndex());
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(monthFont);
		style.setWrapText(true);
		styles.put("header", style);

		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styles.put("cell", style);

		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
		styles.put("formula", style);

		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
		styles.put("formula_2", style);

		return styles;
	}
}