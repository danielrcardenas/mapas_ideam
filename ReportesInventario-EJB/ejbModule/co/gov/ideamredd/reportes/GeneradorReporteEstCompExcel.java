package co.gov.ideamredd.reportes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
public class GeneradorReporteEstCompExcel {

	public static final String ARCHIVO_REPORTE = "reporte_inventarios.xlsx";
	private static final String[] ENCABEZADOS = { "Especie",
			"Frecuencia Relativa", "Abundancia Relativa", "Dominancia", "IVI" };

	Map<String, CellStyle> styles;

	DecimalFormat format = new DecimalFormat("0.0000");
	private int rowIndex = 7;

	public void generarReporte(List<InformacionReporteInventarios> resultados,
			String pathTemplates,double indiceShannon, double indiceSimpson, double diversidadGamma) throws IOException, DocumentException {

		Workbook workbook = new XSSFWorkbook();
		styles = createStyles(workbook);
		XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Estructura y Comp");

		InputStream inputStream = new FileInputStream(pathTemplates
				+ "EncabezadoRptEstComp.png");
		byte[] bytes = IOUtils.toByteArray(inputStream);
		int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
		inputStream.close();

		CreationHelper helper = workbook.getCreationHelper();
		Drawing drawing = sheet.createDrawingPatriarch();

		ClientAnchor anchor = helper.createClientAnchor();

		anchor.setCol1(1);
		anchor.setRow1(1);

		Picture pict = drawing.createPicture(anchor, pictureIdx);

		insertTable(resultados, sheet);

		Row filaTotales = sheet.createRow(++rowIndex);
		Cell celdaTotal = filaTotales.createCell(3);
		celdaTotal.setCellValue("Diversidad Gamma");
		celdaTotal.setCellStyle(styles.get("formula"));

		celdaTotal = filaTotales.createCell(4);
		celdaTotal.setCellValue(format.format(diversidadGamma));
		celdaTotal.setCellStyle(styles.get("formula_2"));

		filaTotales = sheet.createRow(++rowIndex);
		celdaTotal = filaTotales.createCell(3);
		celdaTotal.setCellValue("Inice Shannon Weaver");
		celdaTotal.setCellStyle(styles.get("formula"));

		celdaTotal = filaTotales.createCell(4);
		celdaTotal.setCellValue(format.format(indiceShannon));
		celdaTotal.setCellStyle(styles.get("formula_2"));

		filaTotales = sheet.createRow(++rowIndex);
		celdaTotal = filaTotales.createCell(3);
		celdaTotal.setCellValue("Indice Simpson");
		celdaTotal.setCellStyle(styles.get("formula"));

		celdaTotal = filaTotales.createCell(4);
		celdaTotal.setCellValue(format.format(indiceSimpson));
		celdaTotal.setCellStyle(styles.get("formula_2"));


		pict.resize();
		FileOutputStream out = new FileOutputStream(pathTemplates
				+ ARCHIVO_REPORTE);
		workbook.write(out);
		out.close();
		rowIndex = 7;
	}

	private void insertTable(List<InformacionReporteInventarios> list,
			XSSFSheet sheet) {
		Row filaTitulo = sheet.createRow(rowIndex);
		filaTitulo.setHeightInPoints(45);
		Cell celdaTitulo = filaTitulo.createCell(0);
		celdaTitulo.setCellValue("Indicadores de Estructura y Composición");
		celdaTitulo.setCellStyle(styles.get("title"));

		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$" + (rowIndex + 1)
				+ ":$F$" + (rowIndex + 1)));

		Row filaHeader = sheet.createRow(++rowIndex);
		filaHeader.setHeightInPoints(40);

		Cell celdaHeader;
		for (int i = 0; i < ENCABEZADOS.length; i++) {
			celdaHeader = filaHeader.createCell(i);
			celdaHeader.setCellValue(ENCABEZADOS[i]);
			celdaHeader.setCellStyle(styles.get("header"));
		}

		for (InformacionReporteInventarios registro : list) {
			rowIndex++;
			String[] informacion = extraerInformacion(registro);
			Row filaRegistro = sheet.createRow(rowIndex);

			for (int i = 0; i < ENCABEZADOS.length; i++) {
				Cell celdaRegistro = filaRegistro.createCell(i);
				celdaRegistro.setCellValue(informacion[i]);
				celdaRegistro.setCellStyle(styles.get("cell"));
			}

		}

		for (int i = 0; i < ENCABEZADOS.length; i++) {
			sheet.setColumnWidth(i, 20 * 256);
		}

	}

	private String[] extraerInformacion(
			InformacionReporteInventarios informacionReporteInventarios) {
		String[] informacion = new String[ENCABEZADOS.length];

		informacion[0] = informacionReporteInventarios.getEspecie();

		informacion[1] = format.format(informacionReporteInventarios
				.getFrecuenciaRelativa());
		informacion[2] = format.format(informacionReporteInventarios
				.getAbundanciaRelativa());
		informacion[3] = format.format(informacionReporteInventarios
				.getDominancia());
		informacion[4] = format.format(informacionReporteInventarios.getIvi());

		return informacion;
	}

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