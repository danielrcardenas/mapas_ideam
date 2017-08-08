// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.dao;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import co.gov.ideamredd.util.Util;

public class LecturaArchivo {
	private static final int Number_Cell_type = 0;
	private static final int String_Cell_type = 1;
	private static final int Formula_Cell_type = 2;
	private static String tabla = "";

	public static XSSFSheet lecturaArchivo(String archivo) {
		XSSFSheet hssfSheet = null;
		try {
			tabla = "";
			InputStream ExcelFileToRead = new FileInputStream(archivo);
			XSSFWorkbook workBook = new XSSFWorkbook(ExcelFileToRead);
			hssfSheet = workBook.getSheetAt(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hssfSheet;
	}

	public static String getTablaReporte(XSSFSheet hssfSheet) {
		tabla = "<thead>";
		boolean cabeceraFin = false;
		Iterator<Row> rowIterator = hssfSheet.rowIterator();
		while (rowIterator.hasNext()) {
			XSSFRow hssfRow = (XSSFRow) rowIterator.next();
			Iterator<Cell> iterator = hssfRow.cellIterator();
			tabla += "<tr>";
			while (iterator.hasNext()) {
				XSSFCell hssfCell = (XSSFCell) iterator.next();
				if (hssfCell.getRowIndex() > 0) {
					if (!cabeceraFin) {
						tabla = tabla + "</thead>";
						cabeceraFin = true;
					}
					tabla += "<td>" + getDato(hssfCell) + "</td>";
				} else
					tabla += "<th>" + getDato(hssfCell) + "</th>";
			}
			tabla += "</tr>";
		}
		// System.out.println(tabla);
		return tabla;
	}

	public static String getTablaReporteBNB(XSSFSheet hssfSheet) {
		tabla = "<thead>";
		boolean cabeceraFin = false;
		Iterator<Row> rowIterator = hssfSheet.rowIterator();
		while (rowIterator.hasNext()) {
			XSSFRow hssfRow = (XSSFRow) rowIterator.next();
			Iterator<Cell> iterator = hssfRow.cellIterator();
			tabla += "<tr>";
			while (iterator.hasNext()) {
				XSSFCell hssfCell = (XSSFCell) iterator.next();
				if (hssfCell.getRowIndex() > 2) {
					if (!cabeceraFin) {
						tabla = tabla + "</thead>";
						cabeceraFin = true;
					}
					tabla += "<td>" + getDato(hssfCell) + "</td>";
				} else
					tabla += "<th>" + getDato(hssfCell) + "</th>";
			}
			tabla += "</tr>";
		}
		// System.out.println(tabla);
		return tabla;
	}

	public static String getTablaReporteDeforestacion(XSSFSheet hssfSheet) {
		tabla = "<thead>";
		boolean cabeceraFin = false;
		Iterator<Row> rowIterator = hssfSheet.rowIterator();
		while (rowIterator.hasNext()) {
			XSSFRow hssfRow = (XSSFRow) rowIterator.next();
			Iterator<Cell> iterator = hssfRow.cellIterator();
			tabla += "<tr>";
			while (iterator.hasNext()) {
				XSSFCell hssfCell = (XSSFCell) iterator.next();
				if (hssfCell.getRowIndex() > 1) {
					if (!cabeceraFin) {
						tabla = tabla + "</thead>";
						cabeceraFin = true;
					}
					tabla += "<td>" + getDato(hssfCell) + "</td>";

				} else
					tabla += "<th>" + getDato(hssfCell) + "</th>";
			}
			tabla += "</tr>";
		}
		// System.out.println(tabla);
		return tabla;
	}

	public static String getTablaReporteBiomasa(XSSFSheet hssfSheet) {
		
		if (hssfSheet == null) {
			return "<thead><th></th></thead>";
		}
		
		tabla = "<thead>";
		boolean cabeceraFin = false;
		Iterator<Row> rowIterator = hssfSheet.rowIterator();
		while (rowIterator.hasNext()) {
			XSSFRow hssfRow = (XSSFRow) rowIterator.next();
			Iterator<Cell> iterator = hssfRow.cellIterator();
			tabla += "<tr>";
			while (iterator.hasNext()) {
				XSSFCell hssfCell = (XSSFCell) iterator.next();
				if (hssfCell.getRowIndex() > 2) {
					if (!cabeceraFin) {
						tabla = tabla + "</thead>";
						cabeceraFin = true;
					}
					tabla += "<td>" + getDato(hssfCell) + "</td>";

				} else
					tabla += "<th>" + getDato(hssfCell) + "</th>";
			}
		}
		// System.out.println(tabla);
		return tabla;
	}

	public static String getTablaReporteCobertura(XSSFSheet hssfSheet) {
		tabla = "<thead>";
		boolean cabeceraFin = false;
		Iterator<Row> rowIterator = hssfSheet.rowIterator();
		while (rowIterator.hasNext()) {
			XSSFRow hssfRow = (XSSFRow) rowIterator.next();
			Iterator<Cell> iterator = hssfRow.cellIterator();
			tabla += "<tr>";
			while (iterator.hasNext()) {
				XSSFCell hssfCell = (XSSFCell) iterator.next();
				if (hssfCell.getRowIndex() > 2) {
					if (!cabeceraFin) {
						tabla = tabla + "</thead>";
						cabeceraFin = true;
					}
					tabla += "<td>" + getDato(hssfCell) + "</td>";

				} else
					tabla += "<th>" + getDato(hssfCell) + "</th>";
			}
			tabla += "</tr>";
		}
		// System.out.println(tabla);
		return tabla;
	}

	private static String getDato(XSSFCell hssfCell) {
		String informacion = "";
		DecimalFormat df = new DecimalFormat("########.##");
		switch (hssfCell.getCellType()) {
		case String_Cell_type:
			informacion = hssfCell.getStringCellValue();
			break;
		case Number_Cell_type:
			informacion = df.format(new BigDecimal(hssfCell
					.getNumericCellValue()));
			informacion = Util.formatoValores(informacion);
			break;
		case Formula_Cell_type:
			try {
				informacion = String.valueOf(hssfCell.getNumericCellValue());
			} catch (Exception e) {
			}
			break;
		}
		return informacion;
	}

	private static String getDatoCobertura(XSSFCell hssfCell) {
		String informacion = "";
		DecimalFormat df = new DecimalFormat("########.##");
		switch (hssfCell.getCellType()) {
		case String_Cell_type:
			informacion = hssfCell.getStringCellValue();
			break;
		case Number_Cell_type:
			// if(hssfCell.getNumericCellValue()<9999)
			// informacion = df.format(new BigDecimal(hssfCell
			// .getNumericCellValue()/100));
			// else
			// informacion = df.format(new BigDecimal(hssfCell
			// .getNumericCellValue()/10000));
			informacion = df.format(hssfCell.getNumericCellValue());
			informacion = Util.formatoValores(informacion);
			break;
		case Formula_Cell_type:
			try {
				informacion = String.valueOf(hssfCell.getNumericCellValue());
			} catch (Exception e) {
			}
			break;
		}
		return informacion;
	}

	public String getTabla() {
		return tabla;
	}

}
