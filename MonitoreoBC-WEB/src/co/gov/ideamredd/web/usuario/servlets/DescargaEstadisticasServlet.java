// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.usuario.servlets;

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
import co.gov.ideamredd.usuario.dao.GenerarReporteXLSUsuarios;

/**
 * Servlet usado para descargar estadisticas
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
	private Parametro parametro;

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
		dirBirtPlantillas = parametro.getParametro("birt.Plantillas") + "/";

		if (tipoDescarga.equals("XLS")) {
			generarReporteExcel(resp);
		}
		if (tipoDescarga.equals("PDF")) {
			generarReportePDF(resp);
		}

	}

	public void generarReporteExcel(HttpServletResponse response) {
		GenerarReporteXLSUsuarios genRepoXLS = new GenerarReporteXLSUsuarios();

		genRepoXLS.generarReporte(response, dirDescargas, totales, fechaIni,
				fechaFin, tipopersona, departamento);
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
					if (fechaFin != null) {
						nombrePlantilla = "reportEstadisticUsFIFF.rptdesign";
					} else {
						nombrePlantilla = "reportEstadisticUsFI.rptdesign";
					}
				}

				// Open the report design
				IReportRunnable design = null;

				design = engine.openReportDesign(dirBirtPlantillas
						+ nombrePlantilla);
				// Create task to run and render the report,
				IRunAndRenderTask task = engine.createRunAndRenderTask(design);

				// Set parent classloader for engine
				task.getAppContext().put(
						EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
						DescargaEstadisticasServlet.class.getClassLoader());
				if (fechaIni == null) {
				} else {
					if (fechaFin != null) {
						task.setParameterValue("pr_FechaInicial", fechaIni);
						task.setParameterValue("pr_FechaFinal", fechaFin);
					} else {
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
