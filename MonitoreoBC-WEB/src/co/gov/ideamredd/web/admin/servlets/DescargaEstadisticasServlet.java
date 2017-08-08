// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.servlets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;

import co.gov.ideamredd.mbc.conexion.Parametro;

/**
 * Servlet usado para descargar Estadisticas
 */
public class DescargaEstadisticasServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private String tipoDescarga;
	private String fechaIni;
	private String fechaFin;
	private String dirDescargas;
	private String dirBirtReportEnguine;
	private String dirBirtLog;
	private String dirBirtPlantillas;
	private ArrayList<String[]> tipopersona;
	private ArrayList<String[]> departamento;
	private ArrayList<Integer> totales;
	
	@EJB
	public Parametro parametro;

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		tipoDescarga = req.getParameter("tipoDescarga");
		fechaIni = (String) req.getSession().getAttribute("fechaIni");
		fechaFin = (String) req.getSession().getAttribute("fechaFin");
		tipopersona = (ArrayList<String[]>) req.getSession().getAttribute(
				"estadistica1");
		departamento = (ArrayList<String[]>) req.getSession().getAttribute(
				"estadistica2");
		totales = (ArrayList<Integer>) req.getSession().getAttribute(
				"estadistica3");

		dirDescargas = parametro.getParametro("usuarios.EstadisticasDir") + "/";
		dirBirtReportEnguine = parametro.getParametro("birt.reportEnguine");
		dirBirtLog = parametro.getParametro("birt.logDir");
		dirBirtPlantillas = parametro.getParametro("birt.Plantillas")+"/";

		if (tipoDescarga.equals("XLS")) {
			generarReporteExcel(resp);
		}
		if (tipoDescarga.equals("PDF")) {
			generarReportePDF(resp);
		}

	}

	public void generarReporteExcel(HttpServletResponse response) {
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
						.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				estiloSubTitulos.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

				XSSFCellStyle estiloTitulo = workbook.createCellStyle();
				estiloTitulo.setFillForegroundColor(IndexedColors.AQUA.getIndex());
				estiloTitulo.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

				XSSFCellStyle estiloDatos = workbook.createCellStyle();
				estiloDatos.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
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
					cellTipoPers2.setCellValue(Integer.valueOf(tipopersona.get(i)[0]));
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
					cellUsDepto2.setCellValue(Integer.valueOf(departamento.get(i)[0]));
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

	@SuppressWarnings("unchecked")
	public void generarReportePDF(HttpServletResponse response) {
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

			String rutaArchivo = dirDescargas + fileNombre + ".pdf";

			File archivo = new File(rutaArchivo);

			if (!archivo.exists()) {
				final EngineConfig config = new EngineConfig();
				// delete the following line if using BIRT 3.7 (or later) POJO
				// runtime
				// As of 3.7.2, BIRT now provides an OSGi and a POJO Runtime.

				config.setEngineHome(dirBirtReportEnguine);
				config.setLogConfig(dirBirtLog, Level.SEVERE);

				Platform.startup(config); // If using RE API in Eclipse/RCP
											// application this is not needed.
				IReportEngineFactory factory = (IReportEngineFactory) Platform
						.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
				IReportEngine engine = factory.createReportEngine(config);
				engine.changeLogLevel(Level.WARNING);
				
				String nombrePlantilla;
				// Datos
				if (fechaIni == null) {
					nombrePlantilla = "reportEstadisticUsN.rptdesign";
				} else {
					if(fechaFin !=null)
					{
						nombrePlantilla = "reportEstadisticUsFIFF.rptdesign";
					}else{
						nombrePlantilla = "reportEstadisticUsFI.rptdesign";
					}
				}

				// Open the report design
				IReportRunnable design = null;
				
				design = engine
						.openReportDesign(dirBirtPlantillas +nombrePlantilla);
				// Create task to run and render the report,
				IRunAndRenderTask task = engine.createRunAndRenderTask(design);

				// Set parent classloader for engine
				task.getAppContext().put(
						EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
						DescargaEstadisticasServlet.class.getClassLoader());
				if (fechaIni == null) {
				} else {
					if(fechaFin !=null)
					{
						task.setParameterValue("pr_FechaInicial", fechaIni);
						task.setParameterValue("pr_FechaFinal", fechaFin);
					}else{
						task.setParameterValue("pr_FechaInicial", fechaIni);
					}
				}

				// Setup rendering to HTML
				PDFRenderOption options = new PDFRenderOption();
				options.setOutputFileName(rutaArchivo);
				options.setOutputFormat("pdf");
				// Setting this to true removes html and body tags
				// options.setEmbeddable(false);
				task.setRenderOption(options);
				// run and render report
				task.run();
				task.close();
			}

			response.setContentType("application/pdf");
			response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
			response.setHeader("Cache-Control", "max-age=0");
			response.setHeader("Content-disposition",
					"attachment; filename=EstadisticasUsuarios.pdf");

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
