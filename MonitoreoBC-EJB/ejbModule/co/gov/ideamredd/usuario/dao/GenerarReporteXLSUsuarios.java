// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.usuario.dao;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Clase para generar reporte de usuarios
 */
public class GenerarReporteXLSUsuarios {

	public void generarReporte(HttpServletResponse response,
			String dirDescargas, ArrayList<Integer> totales, String fechaIni,
			String fechaFin, ArrayList<String[]> tipopersona,
			ArrayList<String[]> departamento) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-k");
			String fechaConFormato = sdf.format(new Date());
			String fileNombre = "EstadisticUs";
			if (fechaIni == null) {
				fileNombre = fileNombre + fechaConFormato + "N";
			} else {
				fileNombre = fileNombre + fechaIni.replace("/", "-");
				if (fechaFin != null) {
					fileNombre = fileNombre + "--";
					fileNombre = fileNombre + fechaFin.replace("/", "-");
				}
			}

			String rutaArchivo = dirDescargas + fileNombre + ".xlsx";

			File archivo = new File(rutaArchivo);

			if (!archivo.exists()) {
				FileOutputStream fileOut = new FileOutputStream(rutaArchivo);
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet worksheet = workbook.createSheet("Estadisticas");
				XSSFRow rowTitulo, rowFechas1;
				XSSFCell cellTitulo, cellFechas1, cellFechas2, cellFechas3, cellFechas4;

				// Estilos
				XSSFCellStyle estiloSubTitulos = workbook.createCellStyle();
				estiloSubTitulos
						.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
								.getIndex());
				estiloSubTitulos.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

				XSSFCellStyle estiloTitulo = workbook.createCellStyle();
				estiloTitulo.setFillForegroundColor(IndexedColors.AQUA
						.getIndex());
				estiloTitulo.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

				XSSFCellStyle estiloDatos = workbook.createCellStyle();
				estiloDatos.setFillForegroundColor(IndexedColors.LIGHT_GREEN
						.getIndex());
				estiloDatos.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

				// Datos
				if (fechaIni == null) {
					worksheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 5));
					rowTitulo = worksheet.createRow(3);
					cellTitulo = rowTitulo.createCell(2);
					cellTitulo.setCellValue("ESTADISTICAS DE USUARIOS");
					cellTitulo.setCellStyle(estiloTitulo);
				} else {
					worksheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 5));
					rowTitulo = worksheet.createRow(1);
					cellTitulo = rowTitulo.createCell(2);
					cellTitulo.setCellValue("ESTADISTICAS DE USUARIOS");
					cellTitulo.setCellStyle(estiloTitulo);

					rowFechas1 = worksheet.createRow(2);
					cellFechas1 = rowFechas1.createCell(1);
					cellFechas1.setCellValue("DESDE");
					cellFechas1.setCellStyle(estiloSubTitulos);
					worksheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 3));
					cellFechas2 = rowFechas1.createCell(2);
					cellFechas2.setCellValue(fechaIni);
					cellFechas2.setCellStyle(estiloDatos);
					if (fechaFin != null) {
						cellFechas3 = rowFechas1.createCell(4);
						cellFechas3.setCellValue("HASTA");
						cellFechas3.setCellStyle(estiloSubTitulos);
						worksheet.addMergedRegion(new CellRangeAddress(2, 2, 5,
								6));
						cellFechas4 = rowFechas1.createCell(5);
						cellFechas4.setCellValue(fechaFin);
						cellFechas4.setCellStyle(estiloDatos);
					}
				}

				worksheet.addMergedRegion(new CellRangeAddress(5, 5, 5, 6));
				worksheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 4));
				XSSFRow rowTotal = worksheet.createRow(5);
				XSSFCell cellTotal1 = rowTotal.createCell(1);
				cellTotal1.setCellValue("TOTAL USUARIOS REGISTRADOS");
				cellTotal1.setCellStyle(estiloSubTitulos);
				XSSFCell cellTotal2 = rowTotal.createCell(5);
				cellTotal2.setCellValue(totales.get(0));
				cellTotal2.setCellStyle(estiloDatos);

				worksheet.addMergedRegion(new CellRangeAddress(7, 7, 5, 6));
				worksheet.addMergedRegion(new CellRangeAddress(7, 7, 1, 4));
				XSSFRow rowTotalPub = worksheet.createRow(7);
				XSSFCell cellTotalPub1 = rowTotalPub.createCell(1);
				cellTotalPub1.setCellValue("USUARIOS PUBLICOS REGISTRADOS");
				cellTotalPub1.setCellStyle(estiloSubTitulos);
				XSSFCell cellTotalPub2 = rowTotalPub.createCell(5);
				cellTotalPub2.setCellValue(totales.get(1));
				cellTotalPub2.setCellStyle(estiloDatos);

				int filaFinal = 0;
				worksheet.addMergedRegion(new CellRangeAddress(10, 10, 1, 4));
				worksheet.addMergedRegion(new CellRangeAddress(10, 10, 5, 6));
				XSSFRow rowTipoT = worksheet.createRow(10);
				XSSFCell cellTipoT1 = rowTipoT.createCell(1);
				cellTipoT1.setCellValue("TIPO DE USUARIO");
				cellTipoT1.setCellStyle(estiloSubTitulos);
				XSSFCell cellTipoT2 = rowTipoT.createCell(5);
				cellTipoT2.setCellValue("CANTIDAD");
				cellTipoT2.setCellStyle(estiloSubTitulos);
				for (int i = 0; i < tipopersona.size(); i++) {
					worksheet.addMergedRegion(new CellRangeAddress(11 + i,
							11 + i, 1, 4));
					worksheet.addMergedRegion(new CellRangeAddress(11 + i,
							11 + i, 5, 6));
					XSSFRow rowTipoPers = worksheet.createRow(11 + i);
					XSSFCell cellTipoPers = rowTipoPers.createCell(1);
					cellTipoPers.setCellValue(tipopersona.get(i)[1]);
					cellTipoPers.setCellStyle(estiloDatos);
					XSSFCell cellTipoPers2 = rowTipoPers.createCell(5);
					cellTipoPers2.setCellValue(Integer.valueOf(tipopersona
							.get(i)[0]));
					cellTipoPers2.setCellStyle(estiloDatos);

					filaFinal = 11 + i;
				}

				worksheet.addMergedRegion(new CellRangeAddress(filaFinal + 2,
						filaFinal + 2, 1, 4));
				worksheet.addMergedRegion(new CellRangeAddress(filaFinal + 2,
						filaFinal + 2, 5, 6));
				XSSFRow rowDeptoT = worksheet.createRow(filaFinal + 2);
				XSSFCell cellDeptoT1 = rowDeptoT.createCell(1);
				cellDeptoT1.setCellValue("USUARIOS POR DEPARTAMENTO");
				cellDeptoT1.setCellStyle(estiloSubTitulos);
				XSSFCell cellDeptoT2 = rowDeptoT.createCell(5);
				cellDeptoT2.setCellValue("CANTIDAD");
				cellDeptoT2.setCellStyle(estiloSubTitulos);
				for (int i = 0; i < departamento.size(); i++) {
					worksheet.addMergedRegion(new CellRangeAddress(
							(filaFinal + 3) + i, (filaFinal + 3) + i, 1, 4));
					worksheet.addMergedRegion(new CellRangeAddress(
							(filaFinal + 3) + i, (filaFinal + 3) + i, 5, 6));
					XSSFRow rowUsDepto = worksheet.createRow((filaFinal + 3)
							+ i);
					XSSFCell cellUsDepto = rowUsDepto.createCell(1);
					cellUsDepto.setCellValue(departamento.get(i)[1]);
					cellUsDepto.setCellStyle(estiloDatos);
					XSSFCell cellUsDepto2 = rowUsDepto.createCell(5);
					cellUsDepto2.setCellValue(Integer.valueOf(departamento
							.get(i)[0]));
					cellUsDepto2.setCellStyle(estiloDatos);
				}

				workbook.write(fileOut);
				fileOut.flush();
				fileOut.close();
			}

			response.setContentType("application/xlsx");
			response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
			response.setHeader("Cache-Control", "max-age=0");
			response.setHeader("Content-disposition",
					"attachment; filename=EstadisticasUsuarios.xlsx");

			ServletOutputStream stream = response.getOutputStream();
			FileInputStream input = new FileInputStream(rutaArchivo);
			BufferedInputStream buf = new BufferedInputStream(input);
			int readBytes = 0;

			while ((readBytes = buf.read()) != -1) {
				stream.write(readBytes);
			}
			stream.flush();
			buf.close();
			stream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
