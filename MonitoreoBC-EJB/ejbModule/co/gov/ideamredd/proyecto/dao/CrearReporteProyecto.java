// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.proyecto.dao;

import java.util.ArrayList;

import javax.ejb.Stateless;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import co.gov.ideamredd.util.Util;

/**
 * Clase con la que es generada la estructura del archivo excel a exportar, con los datos de proyecto.
 */

@Stateless 
public class CrearReporteProyecto {

	private XSSFWorkbook libro;
	private XSSFCellStyle estilo;
	private XSSFCellStyle estilo2;
	private XSSFRow[] filas;
	private String nombreReporte;
	private XSSFSheet hoja;
	private Integer indiceInicioFila;
	private Integer numeroCeldaDivision = 1;
	private Integer numeroCeldaCantidad = 2;
	private Integer numeroCeldaCantidadOtros = 3;
	private ArrayList<String> nombreDivision;
	private ArrayList<Integer> cantidadDivision;
	private ArrayList<String> nombreTipoEstado;
	private ArrayList<String> nombreEstado;
	private ArrayList<Integer> cantidadEstado;
	private ArrayList<String> nombreTipoActividad;
	private ArrayList<String> nombreActividad;
	private ArrayList<Integer> cantidadActividad;
	private ArrayList<String> nombreTipoTenencia;
	private ArrayList<String> nombreTenencia;
	private ArrayList<Integer> cantidadTenencia;

	public XSSFWorkbook construirLibroReporte(Integer tipoReporte,
			String[] periodo) {
		hoja = crearLibro();
		nombreReporte = "BosqueNoBosque";
		estilo = Util.estiloTituloReporte(libro);
		estilo2 = Util.estiloDivisionReporte(libro);
		crearEncabezado(hoja, periodo, tipoReporte);
		return libro;
	}

	private void crearEncabezado(XSSFSheet hoja, String[] periodo,
			Integer tipoReporte) {
		agregarTituloPeriodo(hoja, periodo);
		if (tipoReporte == 1)
			poblarConsolidadoXLSX();
		else
			poblarReporteXLSX();
	}

	private void poblarReporteXLSX() {
		indiceInicioFila++;
		for (int i = 0; i < cantidadDivision.size(); i++) {
			XSSFRow fila1 = hoja.createRow(indiceInicioFila);
			XSSFCell datoNombre = fila1.createCell(numeroCeldaDivision);
			XSSFCell datoCantidad = fila1.createCell(numeroCeldaCantidad);
			datoNombre.setCellValue(nombreDivision.get(i));
			datoNombre.setCellStyle(estilo2);
			datoCantidad.setCellValue(cantidadDivision.get(i));
			datoCantidad.setCellStyle(estilo2);
			indiceInicioFila++;
		}
		indiceInicioFila++;
		agregarTitulos("División de territorio","Actividades REDD",
				"Cantidad de proyectos");
		for (int i = 0; i < nombreActividad.size(); i++) {
			XSSFRow fila1 = hoja.createRow(indiceInicioFila);
			XSSFCell datoNombreTipo = fila1.createCell(numeroCeldaDivision);
			XSSFCell datoNombre = fila1.createCell(numeroCeldaCantidad);
			XSSFCell datoCantidad = fila1.createCell(numeroCeldaCantidadOtros);
			datoNombreTipo.setCellValue(nombreTipoActividad.get(i));
			datoNombreTipo.setCellStyle(estilo2);
			datoNombre.setCellValue(nombreActividad.get(i));
			datoNombre.setCellStyle(estilo2);
			datoCantidad.setCellValue(cantidadActividad.get(i));
			datoCantidad.setCellStyle(estilo2);
			indiceInicioFila++;
		}
		indiceInicioFila++;
		agregarTitulos("División de territorio", "Estado del proyecto","Cantidad de proyectos");
		for (int i = 0; i < nombreEstado.size(); i++) {
			XSSFRow fila1 = hoja.createRow(indiceInicioFila);
			XSSFCell datoNombreTipo = fila1.createCell(numeroCeldaDivision);
			XSSFCell datoNombre = fila1.createCell(numeroCeldaCantidad);
			XSSFCell datoCantidad = fila1.createCell(numeroCeldaCantidadOtros);
			datoNombreTipo.setCellValue(nombreTipoEstado.get(i));
			datoNombreTipo.setCellStyle(estilo2);
			datoNombre.setCellValue(nombreEstado.get(i));
			datoNombre.setCellStyle(estilo2);
			datoCantidad.setCellValue(cantidadEstado.get(i));
			datoCantidad.setCellStyle(estilo2);
			indiceInicioFila++;
		}
		indiceInicioFila++;
		agregarTitulos("División de territorio","Tenencia de la Tierra",
				"Cantidad de proyectos");
		for (int i = 0; i < nombreTenencia.size(); i++) {
			XSSFRow fila1 = hoja.createRow(indiceInicioFila);
			XSSFCell datoNombreTipo = fila1.createCell(numeroCeldaDivision);
			XSSFCell datoNombre = fila1.createCell(numeroCeldaCantidad);
			XSSFCell datoCantidad = fila1.createCell(numeroCeldaCantidadOtros);
			datoNombreTipo.setCellValue(nombreTipoTenencia.get(i));
			datoNombreTipo.setCellStyle(estilo2);
			datoNombre.setCellValue(nombreTenencia.get(i));
			datoNombre.setCellStyle(estilo2);
			datoCantidad.setCellValue(cantidadTenencia.get(i));
			datoCantidad.setCellStyle(estilo2);
			indiceInicioFila++;
		}
	}

	private void agregarTituloPeriodo(XSSFSheet hoja, String[] periodo) {
		String titulo = "Division de territorio";
		String tCantidad = "Cantidad de proyectos";
		indiceInicioFila = 5;
		libro.setSheetName(0, "DivisionTerritorial");
		XSSFRow fila1 = hoja.createRow(indiceInicioFila);
		hoja.setColumnWidth(0, 1000);
		XSSFCell tituloDivision = fila1.createCell(numeroCeldaDivision);
		XSSFCell tituloCantidad = fila1.createCell(numeroCeldaCantidad);
		tituloDivision.setCellValue(titulo);
		tituloDivision.setCellStyle(estilo);
		tituloCantidad.setCellValue(tCantidad);
		tituloCantidad.setCellStyle(estilo);
		hoja.setColumnWidth(numeroCeldaDivision, 4000);
		hoja.setColumnWidth(numeroCeldaCantidad, 4000);
	}

	private void agregarTitulos(String titulo, String tCantidad, String otros) {
		XSSFRow fila1 = hoja.createRow(indiceInicioFila);
		hoja.setColumnWidth(0, 1000);
		if (otros == null) {
			XSSFCell tituloDivision = fila1.createCell(numeroCeldaDivision);
			XSSFCell tituloCantidad = fila1.createCell(numeroCeldaCantidad);
			tituloDivision.setCellValue(titulo);
			tituloDivision.setCellStyle(estilo);
			tituloCantidad.setCellValue(tCantidad);
			tituloCantidad.setCellStyle(estilo);
			indiceInicioFila++;
		} else {
			XSSFCell tituloDivision = fila1.createCell(numeroCeldaDivision);
			XSSFCell tituloCantidad = fila1.createCell(numeroCeldaCantidad);
			XSSFCell tituloCantidadOtros = fila1.createCell(numeroCeldaCantidadOtros);
			hoja.setColumnWidth(numeroCeldaCantidadOtros, 4000);
			tituloDivision.setCellValue(titulo);
			tituloDivision.setCellStyle(estilo);
			tituloCantidad.setCellValue(tCantidad);
			tituloCantidad.setCellStyle(estilo);
			tituloCantidadOtros.setCellValue(otros);
			tituloCantidadOtros.setCellStyle(estilo);
			indiceInicioFila++;
		}
	}

	private void poblarConsolidadoXLSX() {
		indiceInicioFila++;
		for (int i = 0; i < cantidadDivision.size(); i++) {
			XSSFRow fila1 = hoja.createRow(indiceInicioFila);
			XSSFCell datoNombre = fila1.createCell(numeroCeldaDivision);
			XSSFCell datoCantidad = fila1.createCell(numeroCeldaCantidad);
			datoNombre.setCellValue(nombreDivision.get(i));
			datoNombre.setCellStyle(estilo2);
			datoCantidad.setCellValue(cantidadDivision.get(i));
			datoCantidad.setCellStyle(estilo2);
			indiceInicioFila++;
		}
		indiceInicioFila++;
		agregarTitulos("Actividades REDD de los proyectos",
				"Cantidad de proyectos",null);
		for (int i = 0; i < nombreEstado.size(); i++) {
			XSSFRow fila1 = hoja.createRow(indiceInicioFila);
			XSSFCell datoNombre = fila1.createCell(numeroCeldaDivision);
			XSSFCell datoCantidad = fila1.createCell(numeroCeldaCantidad);
			datoNombre.setCellValue(nombreEstado.get(i));
			datoNombre.setCellStyle(estilo2);
			datoCantidad.setCellValue(cantidadEstado.get(i));
			datoCantidad.setCellStyle(estilo2);
			indiceInicioFila++;
		}
		indiceInicioFila++;
		agregarTitulos("Estado del proyecto", "Cantidad de proyectos",null);
		for (int i = 0; i < nombreActividad.size(); i++) {
			XSSFRow fila1 = hoja.createRow(indiceInicioFila);
			XSSFCell datoNombre = fila1.createCell(numeroCeldaDivision);
			XSSFCell datoCantidad = fila1.createCell(numeroCeldaCantidad);
			datoNombre.setCellValue(nombreActividad.get(i));
			datoNombre.setCellStyle(estilo2);
			datoCantidad.setCellValue(cantidadActividad.get(i));
			datoCantidad.setCellStyle(estilo2);
			indiceInicioFila++;
		}
		indiceInicioFila++;
		agregarTitulos("Tenencia de la tierra de los proyecto",
				"Cantidad de proyectos",null);
		for (int i = 0; i < nombreTenencia.size(); i++) {
			XSSFRow fila1 = hoja.createRow(indiceInicioFila);
			XSSFCell datoNombre = fila1.createCell(numeroCeldaDivision);
			XSSFCell datoCantidad = fila1.createCell(numeroCeldaCantidad);
			datoNombre.setCellValue(nombreTenencia.get(i));
			datoNombre.setCellStyle(estilo2);
			datoCantidad.setCellValue(cantidadTenencia.get(i));
			datoCantidad.setCellStyle(estilo2);
			indiceInicioFila++;
		}
	}


	private XSSFSheet crearLibro() {
		libro = new XSSFWorkbook();
		XSSFSheet hoja = libro.createSheet();
		return hoja;
	}

	public XSSFRow[] getFilas() {
		return filas;
	}

	public void setFilas(XSSFRow[] filas) {
		this.filas = filas;
	}

	public String getNombreReporte() {
		return nombreReporte;
	}

	public void setNombreReporte(String nombreReporte) {
		this.nombreReporte = nombreReporte;
	}

	public XSSFSheet getHoja() {
		return hoja;
	}

	public void setHoja(XSSFSheet hoja) {
		this.hoja = hoja;
	}

	public ArrayList<String> getNombreDivision() {
		return nombreDivision;
	}

	public void setNombreDivision(ArrayList<String> nombreDivision) {
		this.nombreDivision = nombreDivision;
	}

	public ArrayList<Integer> getCantidadDivision() {
		return cantidadDivision;
	}

	public void setCantidadDivision(ArrayList<Integer> cantidadDivision) {
		this.cantidadDivision = cantidadDivision;
	}

	public Integer getNumeroCeldaDivision() {
		return numeroCeldaDivision;
	}

	public void setNumeroCeldaDivision(Integer numeroCeldaDivision) {
		this.numeroCeldaDivision = numeroCeldaDivision;
	}

	public Integer getNumeroCeldaCantidad() {
		return numeroCeldaCantidad;
	}

	public void setNumeroCeldaCantidad(Integer numeroCeldaCantidad) {
		this.numeroCeldaCantidad = numeroCeldaCantidad;
	}

	public ArrayList<String> getNombreTipoEstado() {
		return nombreTipoEstado;
	}

	public void setNombreTipoEstado(ArrayList<String> nombreTipoEstado) {
		this.nombreTipoEstado = nombreTipoEstado;
	}

	public ArrayList<String> getNombreEstado() {
		return nombreEstado;
	}

	public void setNombreEstado(ArrayList<String> nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	public ArrayList<Integer> getCantidadEstado() {
		return cantidadEstado;
	}

	public void setCantidadEstado(ArrayList<Integer> cantidadEstado) {
		this.cantidadEstado = cantidadEstado;
	}

	public ArrayList<String> getNombreTipoActividad() {
		return nombreTipoActividad;
	}

	public void setNombreTipoActividad(ArrayList<String> nombreTipoActividad) {
		this.nombreTipoActividad = nombreTipoActividad;
	}

	public ArrayList<String> getNombreActividad() {
		return nombreActividad;
	}

	public void setNombreActividad(ArrayList<String> nombreActividad) {
		this.nombreActividad = nombreActividad;
	}

	public ArrayList<Integer> getCantidadActividad() {
		return cantidadActividad;
	}

	public void setCantidadActividad(ArrayList<Integer> cantidadActividad) {
		this.cantidadActividad = cantidadActividad;
	}

	public ArrayList<String> getNombreTipoTenencia() {
		return nombreTipoTenencia;
	}

	public void setNombreTipoTenencia(ArrayList<String> nombreTipoTenencia) {
		this.nombreTipoTenencia = nombreTipoTenencia;
	}

	public ArrayList<String> getNombreTenencia() {
		return nombreTenencia;
	}

	public void setNombreTenencia(ArrayList<String> nombreTenencia) {
		this.nombreTenencia = nombreTenencia;
	}

	public ArrayList<Integer> getCantidadTenencia() {
		return cantidadTenencia;
	}

	public void setCantidadTenencia(ArrayList<Integer> cantidadTenencia) {
		this.cantidadTenencia = cantidadTenencia;
	}
}
