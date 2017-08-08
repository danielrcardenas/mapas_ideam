// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.reportes.servlets;

import java.io.BufferedInputStream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;

import co.gov.ideamredd.web.reportes.servlets.StreamGobbler;
import co.gov.ideamredd.lenguaje.LenguajeI18N;
import co.gov.ideamredd.mbc.auxiliares.Auxiliar;
import co.gov.ideamredd.mbc.conexion.Parametro;
import co.gov.ideamredd.reportes.dao.ExcelReporteGeoproceso;
import co.gov.ideamredd.proyecto.dao.ExcelReporteProyectos;

import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;

import co.gov.ideamredd.reportes.dao.ConsultaReportes;
import co.gov.ideamredd.mbc.auxiliares.Archivo;

/**
 * Servlet usado para descargar un documento de reporte
 */
public class DescargaDocumentosServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@EJB
	private Parametro			parametro;

	public DescargaDocumentosServlet() {
		super();
	}

	private String				dirDescargas;
	private String				dirBirtReportEnguine;
	private String				dirBirtLog;
	private String				dirBirtPlantillas;
	private String				fileNombre;
	private Integer				variosReportes;
	private String				tipoDivision;
	private Integer				treporte;
	private Integer				divterritorial;
	private ArrayList<String>	subNombresArchivos;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String id_reporte = Auxiliar.nzObjStr(request.getParameter("id_reporte"), "");
		String tipoArchivo = request.getParameter("tipoArchivo");
		String str_desde = "";
		String str_hasta = "";

		dirDescargas = parametro.getParametro("ruta_reportes") + "/";

		if (Auxiliar.tieneAlgo(id_reporte)) {
			if (tipoArchivo.equals("xlsx")) {
				String identImagen = "";
				String cx = "";
				String cy = "";
				String zoom = "";
				String idioma = "";

				try {
					identImagen = request.getParameter("identImagen").toString();
					cx = request.getParameter("cx").toString();
					cy = request.getParameter("cy").toString();
					zoom = request.getParameter("zoom").toString();
					idioma = Auxiliar.nzObjStr(request.getParameter("idioma"), "").toString();
					if (idioma.equals("")) {
						LenguajeI18N i18n = (LenguajeI18N) request.getSession().getAttribute("i18nAux");
						if (i18n != null) {
							idioma = i18n.getLenguaje();
						}
						else {
							idioma = "es";
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();

				String nombre = "Reporte-Geoproceso--" + dateFormat.format(date);
	
				try {
					String nombre_archivo = nombre + ".xlsx";
					String ruta_libro_reporte = dirDescargas + nombre_archivo;
	
					File reporte = null;
					reporte = new File(ruta_libro_reporte);
					if (!reporte.exists() || true) {
						try {
							ExcelReporteGeoproceso excelReporteGeoproceso = new ExcelReporteGeoproceso();
							XSSFWorkbook libro = excelReporteGeoproceso.construirLibroReporte(id_reporte, idioma);
							
							FileOutputStream elFichero = new FileOutputStream(ruta_libro_reporte);
							libro.write(elFichero);
							elFichero.close();
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					response.setContentType("application/xlsx");
					response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
					response.setHeader("Cache-Control", "max-age=0");
					response.setHeader("Content-disposition", "attachment; filename=" + nombre_archivo);
					try {
						ServletOutputStream stream = response.getOutputStream();
						FileInputStream input = new FileInputStream(ruta_libro_reporte);
						BufferedInputStream buf = new BufferedInputStream(input);
						int readBytes = 0;
	
						while ((readBytes = buf.read()) != -1) {
							stream.write(readBytes);
						}
						stream.flush();
						buf.close();
						stream.close();
	
					}
					catch (Exception e) {
						e.printStackTrace();
					}
	
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (tipoArchivo.equals("pdf")) {
				exportarReportePDF(request, response);
			}
			//DGR Exportar Mapa
			else if (tipoArchivo.equals("mapa")) {
				exportarMAPA(request, response);
			}	

		}
		else {
			if (tipoArchivo.equals("xlsx")) {
				treporte = Integer.valueOf(request.getParameter("PDFtiporeporte"));
				divterritorial = Integer.valueOf(request.getParameter("PDFdivision"));
				variosReportes = Integer.valueOf(request.getParameter("variosPeriodos"));
				tipoDivision = "";
				dirBirtReportEnguine = parametro.getParametro("birt.reportEnguine");
				dirBirtLog = parametro.getParametro("birt.logDir");
				dirBirtPlantillas = parametro.getParametro("birt.Plantillas") + "/";
				reporteXLSX(request, response);
			}
			else if (tipoArchivo.equals("pdf")) {
				treporte = Integer.valueOf(request.getParameter("PDFtiporeporte"));
				divterritorial = Integer.valueOf(request.getParameter("PDFdivision"));
				variosReportes = Integer.valueOf(request.getParameter("variosPeriodos"));
				tipoDivision = "";
				dirBirtReportEnguine = parametro.getParametro("birt.reportEnguine");
				dirBirtLog = parametro.getParametro("birt.logDir");
				dirBirtPlantillas = parametro.getParametro("birt.Plantillas") + "/";
				reportePDF(request, response);
			}
			else if (tipoArchivo.equals("pdfproyectos")) {
				String tipo = nz(request.getParameter("tipo"), "NAL");
				String desde = nz(request.getParameter("desde"), "");
				String hasta = nz(request.getParameter("hasta"), "");
				if (tieneAlgo(desde)) str_desde = "-Desde" + desde;
				if (tieneAlgo(hasta)) str_hasta = "-Hasta" + hasta;
				String nombre = "Consolidado-Proyectos" + "-" + tipo + str_desde + str_hasta;
	
				String base_url = request.getScheme() + "://" + request.getServerName() + ("http".equals(request.getScheme()) && request.getServerPort() == 80 || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "/MonitoreoBC-WEB" : ":" + request.getServerPort() + "/MonitoreoBC-WEB");
	
				String url = base_url + "/proyectos/reporteProyectos.jsp?modoPDF=1&tipo=" + tipo + "&desde=" + desde + "&hasta=" + hasta;
	
				try {
					generarPDFWkhtmltopdf(request, response, url, dirDescargas, nombre);
				}
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (tipoArchivo.equals("xlsproyectos")) {
				String tipo = nz(request.getParameter("tipo"), "NAL");
				String desde = nz(request.getParameter("desde"), "");
				String hasta = nz(request.getParameter("hasta"), "");
				if (tieneAlgo(desde)) str_desde = "-Desde" + desde;
				if (tieneAlgo(hasta)) str_hasta = "-Hasta" + hasta;
				String nombre = "Consolidado-Proyectos" + "-" + tipo + str_desde + str_hasta;
	
				try {
					String nombre_archivo = nombre + ".xlsx";
					String ruta_libro_reporte = dirDescargas + nombre_archivo;
	
					File reporte = null;
					reporte = new File(ruta_libro_reporte);
					if (!reporte.exists() || true) {
						try {
							ExcelReporteProyectos excelReporteProyectos = new ExcelReporteProyectos();
							XSSFWorkbook libro = excelReporteProyectos.construirLibroReporte(tipo, desde, hasta);
							
							FileOutputStream elFichero = new FileOutputStream(ruta_libro_reporte);
							libro.write(elFichero);
							elFichero.close();
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					response.setContentType("application/xlsx");
					response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
					response.setHeader("Cache-Control", "max-age=0");
					response.setHeader("Content-disposition", "attachment; filename=" + nombre_archivo);
					try {
						ServletOutputStream stream = response.getOutputStream();
						FileInputStream input = new FileInputStream(ruta_libro_reporte);
						BufferedInputStream buf = new BufferedInputStream(input);
						int readBytes = 0;
	
						while ((readBytes = buf.read()) != -1) {
							stream.write(readBytes);
						}
						stream.flush();
						buf.close();
						stream.close();
	
					}
					catch (Exception e) {
						e.printStackTrace();
					}
	
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Método que traduce a post el get si recibió un request get
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void reporteXLSX(HttpServletRequest request, HttpServletResponse response) {
		String uriFile = request.getParameter("path"); // el URL enviado como
		// parametro
		String dato = Parametro.getParametro("ruta_reportes");
		String nombre = request.getParameter("path").replaceAll(Parametro.getParametro("ruta_reportes"), "");

		response.setContentType("application/xlsx");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader("Content-disposition", "attachment; filename=" + nombre);
		try {
			ServletOutputStream stream = response.getOutputStream();
			FileInputStream input = new FileInputStream(uriFile);
			BufferedInputStream buf = new BufferedInputStream(input);
			int readBytes = 0;

			while ((readBytes = buf.read()) != -1) {
				stream.write(readBytes);
			}
			stream.flush();
			buf.close();
			stream.close();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void exportarReportePDF(HttpServletRequest request, HttpServletResponse response) {
		String id_reporte = Auxiliar.nzObjStr(request.getParameter("id_reporte"), "");

		String identImagen = "";
		String cx = "";
		String cy = "";
		String zoom = "";

		try {
			identImagen = request.getParameter("identImagen").toString();
			cx = request.getParameter("cx").toString();
			cy = request.getParameter("cy").toString();
			zoom = request.getParameter("zoom").toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();

		fileNombre = "Reporte--";
		
		String rutaArchivo = dirDescargas + fileNombre + dateFormat.format(date) + ".pdf";

		String uri = request.getScheme() + "://" + request.getServerName() + ("http".equals(request.getScheme()) && request.getServerPort() == 80 || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "/MonitoreoBC-WEB" : ":" + request.getServerPort() + "/MonitoreoBC-WEB");
		try {
			generarReportePDF(rutaArchivo, uri, id_reporte, "0", cx, cy, zoom);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			response.setContentType("application/pdf");
			response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
			response.setHeader("Cache-Control", "max-age=0");
			response.setHeader("Content-disposition", "attachment; filename=" + fileNombre + dateFormat.format(date) + ".pdf");
	
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
		}
		catch (Exception e) {
			
		}
	}

	@SuppressWarnings("unchecked")
	public void reportePDF(HttpServletRequest request, HttpServletResponse response) {
			
		fileNombre = "";
		subNombresArchivos = new ArrayList<String>();
		String periodo = "";
		String[] periodos = null;

		if (variosReportes == 0) {
			periodo = request.getParameter("PDFperiodo");
		}
		else {
			periodos = request.getParameter("PDFperiodos").split(",");
		}

		try {
			if (treporte < 3) {
				fileNombre = fileNombre + "BosqueNoBosque";
				agregarPeriodos(periodo, periodos);

				switch (divterritorial) {
					case 1:
						tipoDivision = "CAR";
						break;
					case 2:
						tipoDivision = "Departamento";
						break;
					case 3:
						tipoDivision = "AreasHidrografica";
						break;
					case 4:
						tipoDivision = "ConsolidadoNacional";
						break;
				}

				fileNombre = fileNombre + tipoDivision;

				if (periodos != null) {
					if (periodos.length > 1) {
						agregarDivisionSubNombres(tipoDivision);
					}
				}
			}
			else if (treporte > 4 && treporte < 7) {
				fileNombre = fileNombre + "Deforestacion";
				agregarPeriodos(periodo, periodos);

				if (treporte == 5) {
					if (divterritorial == 3) {
						tipoDivision = "AreaHidrografica";
					}
					else if (divterritorial == 4) {
						tipoDivision = "ConsolidadoNacional";
					}
				}
				else {
					if (divterritorial == 3) {
						tipoDivision = "AreasHidrograficas";
					}
					else if (divterritorial == 4) {
						tipoDivision = "ConsolidadoNacional";
					}
				}

				fileNombre = fileNombre + tipoDivision;

				if (periodos != null) {
					if (periodos.length > 1) {
						agregarDivisionSubNombres(tipoDivision);
					}
				}
			}
			else if (treporte == 7) {
				fileNombre = fileNombre + "Biomasa";
				agregarPeriodos(periodo, periodos);

				if (divterritorial == 3) {
					tipoDivision = "AreaHidrografica";
				}
				else if (divterritorial == 4) {
					tipoDivision = "ConsolidadoNacional";
				}
				else if (divterritorial == 5) {
					tipoDivision = "TipoBosque";
				}

				fileNombre = fileNombre + tipoDivision + "";

				if (periodos != null) {
					if (periodos.length > 1) {
						agregarDivisionSubNombres(tipoDivision);
					}
				}
			}
			else {
				fileNombre = fileNombre + "Cobertura";
				agregarPeriodos(periodo, periodos);

				switch (divterritorial) {
					case 1:
						tipoDivision = "CAR";
						break;
					case 2:
						tipoDivision = "Departamento";
						break;
					case 3:
						tipoDivision = "AreaHidrografica";
						break;
					case 4:
						tipoDivision = "ConsolidadoNacional";
						break;
				}

				fileNombre = fileNombre + tipoDivision;

				if (periodos != null) {
					if (periodos.length > 1) {
						agregarDivisionSubNombres(tipoDivision);
					}
				}
			}

			//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();

			String rutaArchivo = dirDescargas + fileNombre + dateFormat.format(date) + ".pdf";

			File archivo = new File(rutaArchivo);

			if (!archivo.exists()) {
				final EngineConfig config = new EngineConfig();

				config.setEngineHome(dirBirtReportEnguine);
				config.setLogConfig(dirBirtLog, Level.SEVERE);

				Platform.startup(config); // If using RE API in Eclipse/RCP application this is not needed.
				IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
				IReportEngine engine = factory.createReportEngine(config);
				engine.changeLogLevel(Level.WARNING);

				String nombrePlantilla = "report";

				if (treporte < 3) {
					nombrePlantilla = nombrePlantilla + "BosqueNoBosque";
				}
				else if (treporte > 4 && treporte < 7) {
					nombrePlantilla = nombrePlantilla + "Deforestacion";
				}
				else if (treporte == 7) {
					nombrePlantilla = nombrePlantilla + "Biomasa";
				}
				else {
					nombrePlantilla = nombrePlantilla + "Cobertura";
				}

				if (divterritorial == 4) {
					nombrePlantilla = nombrePlantilla + "Cons.rptdesign";
				}
				else {
					nombrePlantilla = nombrePlantilla + ".rptdesign";
				}

				IReportRunnable design = null;

				design = engine.openReportDesign(dirBirtPlantillas + nombrePlantilla);

				String identImagen = "";
				String cx = "";
				String cy = "";
				String zoom = "";

				try {
					identImagen = request.getParameter("identImagen").toString();
					cx = request.getParameter("cx").toString();
					cy = request.getParameter("cy").toString();
					zoom = request.getParameter("zoom").toString();
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				if (treporte < 3) {
					if (variosReportes == 0) {
						// Create task to run and render the report,
						IRunAndRenderTask task = engine.createRunAndRenderTask(design);

						// Set parent classloader for engine
						task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, DescargaDocumentosServlet.class.getClassLoader());

						task.setParameterValue("prm_Periodo", periodo.substring(periodo.indexOf("-") + 1, periodo.length()));
						task.setParameterValue("prm_IdReporte", Integer.valueOf(periodo.substring(0, periodo.indexOf("-"))));
						task.setParameterValue("prm_DivisionNombre", tipoDivision);

						// Setup rendering to HTML
						PDFRenderOption options = new PDFRenderOption();
						options.setOutputFileName(rutaArchivo);
						options.setOutputFormat("pdf");
						task.setRenderOption(options);
						// run and render report
						task.run();
						task.close();
					}
					else {
						for (int r = 0; r < periodos.length; r++) {
							if (periodos.length > 1) {
								String auxRutaFile = dirDescargas + subNombresArchivos.get(r);
								File auxFile = new File(auxRutaFile);

								if (!auxFile.exists()) {
									// Create task to run and render the report,
									IRunAndRenderTask task = engine.createRunAndRenderTask(design);

									// Set parent classloader for engine
									task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, DescargaDocumentosServlet.class.getClassLoader());

									task.setParameterValue("prm_Periodo", periodos[r].substring(periodos[r].indexOf("-") + 1, periodos[r].length()));
									task.setParameterValue("prm_IdReporte", Integer.valueOf(periodos[r].substring(0, periodos[r].indexOf("-"))));
									task.setParameterValue("prm_DivisionNombre", tipoDivision);

									PDFRenderOption options = new PDFRenderOption();
									options.setOutputFileName(auxRutaFile);
									options.setOutputFormat("pdf");
									task.setRenderOption(options);
									// run and render report
									task.run();
									task.close();
								}
							}
							else {
								// Create task to run and render the report,
								IRunAndRenderTask task = engine.createRunAndRenderTask(design);

								// Set parent classloader for engine
								task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, DescargaDocumentosServlet.class.getClassLoader());

								task.setParameterValue("prm_Periodo", periodos[r].substring(periodos[r].indexOf("-") + 1, periodos[r].length()));
								task.setParameterValue("prm_IdReporte", Integer.valueOf(periodos[r].substring(0, periodos[r].indexOf("-"))));
								task.setParameterValue("prm_DivisionNombre", tipoDivision);

								PDFRenderOption options = new PDFRenderOption();
								options.setOutputFileName(rutaArchivo);
								options.setOutputFormat("pdf");
								task.setRenderOption(options);
								// run and render report
								task.run();
								task.close();
							}
						}
					}
				}
				else if (treporte > 4 && treporte < 7) {
					if (variosReportes == 0) {
						// Create task to run and render the report,
						IRunAndRenderTask task = engine.createRunAndRenderTask(design);

						// Set parent classloader for engine
						task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, DescargaDocumentosServlet.class.getClassLoader());

						task.setParameterValue("prm_Periodo", periodo.substring(periodo.indexOf("-") + 1, periodo.length()));
						task.setParameterValue("prm_IdReporte", Integer.valueOf(periodo.substring(0, periodo.indexOf("-"))));

						// Setup rendering to HTML
						PDFRenderOption options = new PDFRenderOption();
						options.setOutputFileName(rutaArchivo);
						options.setOutputFormat("pdf");
						task.setRenderOption(options);
						// run and render report
						task.run();
						task.close();
					}
					else {
						for (int r = 0; r < periodos.length; r++) {

							if (periodos.length > 1) {
								String auxRutaFile = dirDescargas + subNombresArchivos.get(r);
								File auxFile = new File(auxRutaFile);

								if (!auxFile.exists()) {
									// Create task to run and render the report,
									IRunAndRenderTask task = engine.createRunAndRenderTask(design);

									// Set parent classloader for engine
									task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, DescargaDocumentosServlet.class.getClassLoader());

									task.setParameterValue("prm_Periodo", periodos[r].substring(periodos[r].indexOf("-") + 1, periodos[r].length()));
									task.setParameterValue("prm_IdReporte", Integer.valueOf(periodos[r].substring(0, periodos[r].indexOf("-"))));

									PDFRenderOption options = new PDFRenderOption();
									options.setOutputFileName(auxRutaFile);
									options.setOutputFormat("pdf");
									task.setRenderOption(options);
									// run and render report
									task.run();
									task.close();
								}
							}
							else {
								// Create task to run and render the report,
								IRunAndRenderTask task = engine.createRunAndRenderTask(design);

								// Set parent classloader for engine
								task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, DescargaDocumentosServlet.class.getClassLoader());

								task.setParameterValue("prm_Periodo", periodos[r].substring(periodos[r].indexOf("-") + 1, periodos[r].length()));
								task.setParameterValue("prm_IdReporte", Integer.valueOf(periodos[r].substring(0, periodos[r].indexOf("-"))));

								PDFRenderOption options = new PDFRenderOption();
								options.setOutputFileName(rutaArchivo);
								options.setOutputFormat("pdf");
								task.setRenderOption(options);
								// run and render report
								task.run();
								task.close();
							}
						}
					}
				}
				else if (treporte == 7) {
					/*if (variosReportes == 0) { // Create task to run and render the report, IRunAndRenderTask task = engine.createRunAndRenderTask(design);
					 * 
					 * // Set parent classloader for engine task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, DescargaDocumentosServlet.class.getClassLoader());
					 * 
					 * task.setParameterValue("prm_Periodo", periodo.substring(periodo.indexOf("-") + 1, periodo.length())); task.setParameterValue("prm_IdReporte", Integer.valueOf(periodo.substring(0, periodo.indexOf("-"))));
					 * 
					 * // Setup rendering to HTML PDFRenderOption options = new PDFRenderOption();
					 * 
					 * options.setOutputFileName(rutaArchivo); options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF); options.setOption(IPDFRenderOption.PDF_HYPHENATION, true); options.setOption(IPDFRenderOption.PDF_TEXT_WRAPPING, true);
					 * task.setRenderOption(options); // run and render report task.run(); task.close(); } else { for (int r = 0; r < periodos.length; r++) { if (periodos.length > 1) { String auxRutaFile = dirDescargas + subNombresArchivos.get(r); File
					 * auxFile = new File(auxRutaFile);
					 * 
					 * if (!auxFile.exists()) { // Create task to run and render the report, IRunAndRenderTask task = engine.createRunAndRenderTask(design);
					 * 
					 * // Set parent classloader for engine task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, DescargaDocumentosServlet.class.getClassLoader());
					 * 
					 * task.setParameterValue("prm_Periodo", periodos[r].substring(periodos[r].indexOf("-") + 1, periodos[r].length())); task.setParameterValue("prm_IdReporte", Integer.valueOf(periodos[r].substring(0, periodos[r].indexOf("-"))));
					 * 
					 * PDFRenderOption options = new PDFRenderOption(); options.setOutputFileName(auxRutaFile); options.setOutputFormat("pdf"); task.setRenderOption(options); // run and render report task.run(); task.close(); } } else { // Create task to
					 * run and render the report, IRunAndRenderTask task = engine.createRunAndRenderTask(design);
					 * 
					 * // Set parent classloader for engine task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, DescargaDocumentosServlet.class.getClassLoader());
					 * 
					 * task.setParameterValue("prm_Periodo", periodos[r].substring(periodos[r].indexOf("-") + 1, periodos[r].length())); task.setParameterValue("prm_IdReporte", Integer.valueOf(periodos[r].substring(0, periodos[r].indexOf("-"))));
					 * 
					 * PDFRenderOption options = new PDFRenderOption(); options.setOutputFileName(rutaArchivo); options.setOutputFormat("pdf"); task.setRenderOption(options); // run and render report task.run(); task.close(); } } } */

					String uri = request.getScheme() + "://" + request.getServerName() + ("http".equals(request.getScheme()) && request.getServerPort() == 80 || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "/MonitoreoBC-WEB" : ":" + request.getServerPort() + "/MonitoreoBC-WEB");
					generarArchivoReportePDF(rutaArchivo, uri, String.valueOf(treporte), String.valueOf(tipoDivision), periodo, "0", cx, cy, zoom);

				}
				else {
					if (variosReportes == 0) {
						// Create task to run and render the report,
						IRunAndRenderTask task = engine.createRunAndRenderTask(design);

						// Set parent classloader for engine
						task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, DescargaDocumentosServlet.class.getClassLoader());

						task.setParameterValue("prm_Periodo", periodo.substring(periodo.indexOf("-") + 1, periodo.length()));
						task.setParameterValue("prm_IdReporte", Integer.valueOf(periodo.substring(0, periodo.indexOf("-"))));
						task.setParameterValue("prm_DivisionNombre", tipoDivision);
						String periodo1 = periodo.substring(periodo.indexOf("-") + 1, periodo.length());
						periodo1 = periodo1.substring(0, periodo1.indexOf("-"));
						String periodo2 = periodo.substring(periodo.indexOf("-") + 1, periodo.length());
						periodo2 = periodo2.substring(periodo2.indexOf("-") + 1, periodo2.length());
						task.setParameterValue("prm_Periodo1", Integer.valueOf(periodo1));
						task.setParameterValue("prm_Periodo2", Integer.valueOf(periodo2));

						// Setup rendering to HTML
						PDFRenderOption options = new PDFRenderOption();
						options.setOutputFileName(rutaArchivo);
						options.setOutputFormat("pdf");
						task.setRenderOption(options);
						// run and render report
						task.run();
						task.close();
					}
					else {
						for (int r = 0; r < periodos.length; r++) {
							if (periodos.length > 1) {
								String auxRutaFile = dirDescargas + subNombresArchivos.get(r);
								File auxFile = new File(auxRutaFile);

								if (!auxFile.exists()) {
									// Create task to run and render the report,
									IRunAndRenderTask task = engine.createRunAndRenderTask(design);

									// Set parent classloader for engine
									task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, DescargaDocumentosServlet.class.getClassLoader());

									task.setParameterValue("prm_Periodo", periodos[r].substring(periodos[r].indexOf("-") + 1, periodos[r].length()));
									task.setParameterValue("prm_IdReporte", Integer.valueOf(periodos[r].substring(0, periodos[r].indexOf("-"))));
									task.setParameterValue("prm_DivisionNombre", tipoDivision);
									String auxPeriodo = periodos[r].substring(periodos[r].indexOf("-") + 1, periodos[r].length());
									String periodo1 = auxPeriodo.substring(0, auxPeriodo.indexOf("-"));
									String periodo2 = auxPeriodo.substring(auxPeriodo.indexOf("-") + 1, auxPeriodo.length());
									task.setParameterValue("prm_Periodo1", Integer.valueOf(periodo1));
									task.setParameterValue("prm_Periodo2", Integer.valueOf(periodo2));

									PDFRenderOption options = new PDFRenderOption();
									options.setOutputFileName(auxRutaFile);
									options.setOutputFormat("pdf");
									task.setRenderOption(options);
									// run and render report
									task.run();
									task.close();
								}
							}
							else {
								// Create task to run and render the report,
								IRunAndRenderTask task = engine.createRunAndRenderTask(design);

								// Set parent classloader for engine
								task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, DescargaDocumentosServlet.class.getClassLoader());

								task.setParameterValue("prm_Periodo", periodos[r].substring(periodos[r].indexOf("-") + 1, periodos[r].length()));
								task.setParameterValue("prm_IdReporte", Integer.valueOf(periodos[r].substring(0, periodos[r].indexOf("-"))));
								task.setParameterValue("prm_DivisionNombre", tipoDivision);
								String auxPeriodo = periodos[r].substring(periodos[r].indexOf("-") + 1, periodos[r].length());
								String periodo1 = auxPeriodo.substring(0, auxPeriodo.indexOf("-"));
								String periodo2 = auxPeriodo.substring(auxPeriodo.indexOf("-") + 1, auxPeriodo.length());
								task.setParameterValue("prm_Periodo1", Integer.valueOf(periodo1));
								task.setParameterValue("prm_Periodo2", Integer.valueOf(periodo2));

								PDFRenderOption options = new PDFRenderOption();
								options.setOutputFileName(rutaArchivo);
								options.setOutputFormat("pdf");
								task.setRenderOption(options);
								// run and render report
								task.run();
								task.close();
							}
						}
					}
				}

				if (periodos != null) {
					if (variosReportes == 1 && periodos.length > 1) {
						PdfCopyFields merger = new PdfCopyFields(new FileOutputStream(rutaArchivo));
						for (int r = 0; r < periodos.length; r++) {
							merger.addDocument(new PdfReader(new FileInputStream(new File(dirDescargas + subNombresArchivos.get(r)))));
						}
						merger.close();
					}
				}
			}

			//			String archivo_reporte = rutaArchivo;
			//			if (true) {
			//				String copia_archivo_reporte = rutaArchivo + "-copia.pdf";
			//
			//				String identImagen = "";
			//				String cx = "";
			//				String cy = "";
			//				String zoom = "";
			//
			//				try {
			//					identImagen = request.getParameter("identImagen").toString();
			//					cx = request.getParameter("cx").toString();
			//					cy = request.getParameter("cy").toString();
			//					zoom = request.getParameter("zoom").toString();
			//				}
			//				catch (Exception e) {
			//					e.printStackTrace();
			//				}
			//
			//				String archivo_mapa = generarMapaWMS(identImagen, cx, cy, zoom);
			//				boolean concatenar = true;
			//				String r = "";
			//
			//				if (archivo_mapa.equals("")) {
			//					concatenar = false;
			//				}
			//
			//				if (concatenar) {
			//					String commando_copia = "cp " + archivo_reporte + " " + copia_archivo_reporte;
			//					String resultado_copia = commander(commando_copia, commando_copia);
			//					String[] a_resultado_copia = resultado_copia.split("-=-");
			//
			//					if (!nz(a_resultado_copia[0], "").equals("0")) {
			//						r = "No se pudo copiar el reporte: " + resultado_copia;
			//						System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + r);
			//						response.setContentType("text/html");
			//					}
			//					else {
			//						String commando_concatenacion = "gs -sDEVICE=pdfwrite -dCompatibilityLevel=1.4 -dPDFSETTINGS=/prepress -dNOPAUSE -dQUIET -dBATCH -dDetectDuplicateImages -dCompressFonts=true -r150 -sOutputFile=" + archivo_reporte + " " + copia_archivo_reporte + " " + archivo_mapa;
			//						String resultado_concatenacion = commander(commando_concatenacion, commando_concatenacion);
			//						String[] a_resultado_concatenacion = resultado_concatenacion.split("-=-");
			//
			//						if (!nz(a_resultado_concatenacion[0], "").equals("0")) {
			//							r = "No se pudo generar la  imagen: " + resultado_concatenacion;
			//							System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + r);
			//							response.setContentType("text/html");
			//
			//							String commando_deshacer = "mv " + copia_archivo_reporte + " " + archivo_reporte;
			//							String resultado_deshacer = commander(commando_deshacer, commando_deshacer);
			//							String[] a_resultado_deshacer = resultado_deshacer.split("-=-");
			//
			//							if (!nz(a_resultado_deshacer[0], "").equals("0")) {
			//								r = "No se pudo deshacer la copia del reporte: " + resultado_copia;
			//								System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + r);
			//								response.setContentType("text/html");
			//							}
			//						}
			//					}
			//				}
			//			}

			response.setContentType("application/pdf");
			response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
			response.setHeader("Cache-Control", "max-age=0");
			response.setHeader("Content-disposition", "attachment; filename=" + fileNombre + dateFormat.format(date) + ".pdf");

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

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void agregarPeriodos(String periodo, String[] periodos) {
		String fileNombreAux = fileNombre;

		if (variosReportes == 0) {
			fileNombre = fileNombre + periodo.substring(periodo.indexOf("-") + 1, periodo.length());
		}
		else {
			for (int c = 0; c < periodos.length; c++) {
				if (periodos.length > 1) {
					fileNombre = fileNombre + periodos[c].substring(periodos[c].indexOf("-") + 1, periodos[c].length());
					subNombresArchivos.add(fileNombreAux + periodos[c].substring(periodos[c].indexOf("-") + 1, periodos[c].length()));
				}
				else {
					fileNombre = fileNombre + periodos[c].substring(periodos[c].indexOf("-") + 1, periodos[c].length());
				}
			}
		}
	}

	public void agregarDivisionSubNombres(String nombreDivision) {
		for (int x = 0; x < subNombresArchivos.size(); x++) {
			subNombresArchivos.set(x, subNombresArchivos.get(x) + nombreDivision + ".pdf");
		}
	}

	/**
	 * Metodo generarMapaWMS
	 * 
	 * Genera una imagen del mapa de la parcela
	 * 
	 * @return
	 * @throws Exception
	 * @throws ClassNotFoundException
	 */
	public boolean generarArchivoReportePDF(String ruta, String base_url, String tReporte, String divterritorio, String periodo1, String i, String cx, String cy, String zoom) throws ClassNotFoundException, Exception {
		String codigoTerritorio = "";

		try {

			if (divterritorio.equals("CAR"))
				codigoTerritorio = "1";
			if (divterritorio.equals("Departamentos"))
				codigoTerritorio = "2";
			if (divterritorio.equals("AreaHidrografica"))
				codigoTerritorio = "3";
			if (divterritorio.equals("ConsolidadoNacional"))
				codigoTerritorio = "4";
			if (divterritorio.equals("TipoBosque"))
				codigoTerritorio = "5";

			System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + "Archivo PDF del reporte se creará en:" + ruta);

			ruta = ruta.replaceAll("//", "/");

			String commando_generacion = "/usr/bin/wkhtmltopdf --disable-local-file-access --print-media-type --page-size Letter \"" + base_url + "/consultarReporteServlet?treporte=" + tReporte + "&divterritorio=" + codigoTerritorio + "&periodo1=" + periodo1 + "&i=" + i + "&modoPDF=1&zoom=" + zoom + "&cx=" + cx + "&cy=" + cy + "\" " + ruta;
			String resultado_generacion = commander(commando_generacion, commando_generacion);

			String[] a_resultado_generacion = resultado_generacion.split("-=-");

			String r = "";

			if (!nz(a_resultado_generacion[0], "").equals("0")) {
				r = "No se pudo generar el PDF del reporte: " + resultado_generacion;
				System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + r);
				return false;
			}
			else {
				r = "Generación de PDF de reporte exitosa [" + resultado_generacion + "].";
				System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + r);
				if (existeArchivo(ruta)) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		catch (Exception e) {
			System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + e.toString());
		}

		return false;
	}
	
	public boolean generarReportePDF(String ruta, String base_url, String id_reporte, String i, String cx, String cy, String zoom) throws ClassNotFoundException, Exception {
		try {			
			System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + "Archivo PDF del reporte se creará en:" + ruta);

			ruta = ruta.replaceAll("//", "/");
			
			String commando_generacion = "/usr/bin/wkhtmltopdf --disable-local-file-access --print-media-type --page-size Letter \"" + base_url + "/pub/reporteGeoproceso.jsp?id_reporte=" + id_reporte + "&modoPDF=1&zoom=" + zoom + "&cx=" + cx + "&cy=" + cy + "\" " + ruta;
			String resultado_generacion = commander(commando_generacion, commando_generacion);
			
			String[] a_resultado_generacion = resultado_generacion.split("-=-");
			
			String r = "";
			
			if (!nz(a_resultado_generacion[0], "").equals("0")) {
				r = "No se pudo generar el PDF del reporte: " + resultado_generacion;
				System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + r);
				return false;
			}
			else {
				r = "Generación de PDF de reporte exitosa [" + resultado_generacion + "].";
				System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + r);
				if (existeArchivo(ruta)) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		catch (Exception e) {
			System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + e.toString());
		}
		
		return false;
	}

	/**
	 * Metodo generarPDFWkhtmltopdf
	 * 
	 * Genera un PDF de una página web específica
	 * 
	 * @return
	 * @throws Exception
	 * @throws ClassNotFoundException
	 */
	public void generarPDFWkhtmltopdf(HttpServletRequest request, HttpServletResponse response, String url, String carpeta, String nombre) throws ClassNotFoundException, Exception {
		String nombreArchivo = nombre + ".pdf";
		String ruta = carpeta + File.separator + nombreArchivo;

		try {
			System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + "Archivo PDF del reporte se creará en:" + ruta);

			ruta = ruta.replaceAll("//", "/");

			String commando_generacion = "/usr/bin/wkhtmltopdf --disable-local-file-access --print-media-type  --javascript-delay 1000 --no-stop-slow-scripts --page-size Letter " + url + "  " + ruta;
			String resultado_generacion = commander(commando_generacion, commando_generacion);

			String[] a_resultado_generacion = resultado_generacion.split("-=-");

			String r = "";

			if (!nz(a_resultado_generacion[0], "").equals("0")) {
				r = "No se pudo generar el PDF: " + resultado_generacion;
				System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + r);
			}
			else {
				r = "Generación de PDF exitosa [" + resultado_generacion + "].";
				System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + r);
			}
		}
		catch (Exception e) {
			System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + e.toString());
		}

		if (existeArchivo(ruta)) {
			response.setContentType("application/pdf");
			response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
			response.setHeader("Cache-Control", "max-age=0");
			response.setHeader("Content-disposition", "attachment; filename=" + nombreArchivo);

			ServletOutputStream stream = response.getOutputStream();
			FileInputStream input = new FileInputStream(ruta);
			BufferedInputStream buf = new BufferedInputStream(input);
			int readBytes = 0;

			while ((readBytes = buf.read()) != -1) {
				stream.write(readBytes);
			}

			stream.flush();
			buf.close();
			stream.close();
		}
		else {
			request.setAttribute("retorno", "NOGO");
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher("index.jsp");
			dispatcher.forward(request, response);
		}

	}

	/**
	 * Metodo generarMapaWMS
	 * 
	 * Genera una imagen del mapa de la parcela
	 * 
	 * @return
	 * @throws Exception
	 * @throws ClassNotFoundException
	 */
	public String generarMapaWMS(String identImagen, String cx, String cy, String zoom) throws ClassNotFoundException, Exception {

		String html = "";

		try {
			html = "";
			html += "<!DOCTYPE html>";
			html += "\n" + "<html>";
			html += "\n" + "<head>";
			html += "\n" + "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>";

			html += "\n" + "<link rel='stylesheet' href='leaflet.css' />";
			html += "\n" + "</head>";
			html += "\n" + "<body style='text-align: center;'>";

			html += "\n" + "<div id='mapdiv' style='width: 700px; height: 800px'></div>";
			html += "\n" + "<div id='viewparams' style='color: #999; width: 100%; height: 40px; border: 1px solid white; display:none;'></div>";

			html += "\n" + "<script type='text/javascript' src='leaflet.js'></script>";
			html += "\n" + "<script type='text/javascript'>";

			String wms_base = "";
			String wms_pixeles_url = "";
			String wms_pixeles_capas = "";
			String wms_atribucion = "";
			String wms_pixeles_crs = "";
			String wms_pixeles_srs = "";
			String wms_pixeles_format = "";
			String wms_pixeles_version = "";

			String atribucion_defecto = "Map data &copy; <a href='http://openstreetmap.org'>OpenStreetMap</a> contributors. <a href='http://creativecommons.org/licenses/by-sa/2.0/'>CC-BY-SA</a>, Imagery © <a href='http://mapbox.com'>Mapbox</a>, Parcelas/Plots © <a href='http://www.ideam.gov.co'>IDEAM</a> y/o Colaboradores.";

			wms_base = obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_base'", "http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
			wms_atribucion = obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_atribucion'", atribucion_defecto);

			wms_pixeles_crs = obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_pixeles_crs'", "L.CRS.EPSG4326");
			wms_pixeles_srs = obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_pixeles_srs'", "EPSG:4326");
			wms_pixeles_format = obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_pixeles_format'", "image/png");
			wms_pixeles_version = obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_pixeles_version'", "1.3.0");
			String wms_pixeles_latitud = obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_pixeles_latitud'", "4");
			String wms_pixeles_longitud = obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_pixeles_longitud'", "-73");
			String wms_pixeles_zoom = obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_pixeles_zoom'", "5");

			html += "\n" + "var wms_base = '" + wms_base + "';";
			html += "\n" + "var wms_atribucion = '" + wms_atribucion + "';";
			html += "\n" + "var wms_pixeles_crs = '" + wms_pixeles_crs + "';";
			html += "\n" + "var wms_pixeles_srs = '" + wms_pixeles_srs + "';";
			html += "\n" + "var wms_pixeles_format = '" + wms_pixeles_format + "';";
			html += "\n" + "var wms_pixeles_version = '" + wms_pixeles_version + "';";

			html += "\n" + "var CX = '" + wms_pixeles_longitud + "';";
			html += "\n" + "var CY = '" + wms_pixeles_latitud + "';";

			html += "\n" + "if (CX != '') {";
			html += "\n" + "	dCX = CX * 1.0;";
			html += "\n" + "}";
			html += "\n" + "else {";
			html += "\n" + "	dCX = -73;";
			html += "\n" + "}";
			html += "\n" + "if (CY != '') {";
			html += "\n" + "	dCY = CY * 1.0;";
			html += "\n" + "}";
			html += "\n" + "else {";
			html += "\n" + "	dCY = 5;";
			html += "\n" + "}";

			html += "\n" + "var zoom = '" + wms_pixeles_zoom + "';";
			html += "\n" + "if (zoom != '') {";
			html += "\n" + "	dZoom = zoom * 1.0;";
			html += "\n" + "}";
			html += "\n" + "else {";
			html += "\n" + "	dZoom = 5;";
			html += "\n" + "}";

			if (!cx.equals("")) {
				html += "\n" + "dCX=" + cx + ";";
			}

			if (!cy.equals("")) {
				html += "\n" + "dCY=" + cy + ";";
			}

			if (!zoom.equals("")) {
				html += "\n" + "dZoom=" + zoom + ";";
			}

			html += "\n" + "var mapa_wms = L.map('mapdiv', {crs: " + wms_pixeles_crs + "}).setView([dCY, dCX], dZoom);";

			// MAPA BASE
			html += "\n" + "L.tileLayer(wms_base, {srs: wms_pixeles_srs, attribution: wms_atribucion}).addTo(mapa_wms);";

			// WMS Geoproceso
			html += "\n" + "var url='http://seinekan.ideam.gov.co/ApolloCatalogWMSPublic/service.svc/get?service=WMS&request=getLayer';";

			html += "\n" + "var wms = L.tileLayer.wms(url, {";
			html += "\n" + "    layers: '" + identImagen + "',";
			html += "\n" + "    format: wms_pixeles_format,";
			html += "\n" + "    srs: wms_pixeles_srs,";
			html += "\n" + "    transparent: true,";
			html += "\n" + "    version: wms_pixeles_version,";
			html += "\n" + "    attribution: wms_atribucion";
			html += "\n" + "});";
			html += "\n" + "wms.addTo(mapa_wms);";

			html += "\n" + "setTimeout(function(){window.status='ya';}, 1000);";

			html += "\n" + "</script>";

			html += "\n" + "</body>";
			html += "\n" + "</html>";

			String carpeta = obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_ruta_trabajo'", "/opt/mapas_exportados/");
			String instante = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			String ruta_html = carpeta + instante + "--mapa.html";
			String ruta_out = carpeta + instante + "--mapa.pdf";

			boolean ok_crear_archivo_html = escribirArchivo(ruta_html, html);
			System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + "Archivo html creado en:" + ruta_html);

			if (ok_crear_archivo_html) {
				//String commando_generacion = "wkhtmltoimage --width 555 --window-status ya -f png --quality 100 --encoding UTF-8 --javascript-delay 1000 --no-stop-slow-scripts " + ruta_html + " " + ruta_out;
				String commando_generacion = "wkhtmltopdf --encoding UTF-8 --window-status ya --javascript-delay 1000 " + ruta_html + " " + ruta_out;
				String resultado_generacion = commander(commando_generacion, commando_generacion);

				String[] a_resultado_generacion = resultado_generacion.split("-=-");

				String r = "";

				if (!nz(a_resultado_generacion[0], "").equals("0")) {
					r = "No se pudo generar la imagen: " + resultado_generacion;
					System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + r);
					return "";
				}
				else {
					r = "Generación de mapa de la parcela exitosa [" + resultado_generacion + "].";
					System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + r);
					//archie.eliminarArchivo(ruta_html);
					if (existeArchivo(ruta_out)) {
						return ruta_out;
					}
					else {
						return "";
					}
				}
			}
		}
		catch (Exception e) {
			System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + e.toString());
		}

		return "";
	}

	/**
	 * Método escribirArchivo. Crea un archivo de texto con el contenido en la ruta.
	 * 
	 * @param ruta
	 * @param contenido
	 * @return
	 * @throws IOException
	 */
	public boolean escribirArchivo(String ruta, String contenido) throws IOException {
		BufferedWriter writer = null;
		try {
			//String instante = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			File archivo = new File(ruta);
			System.out.println(archivo.getCanonicalPath());
			writer = new BufferedWriter(new FileWriter(archivo));
			writer.write(contenido);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				writer.close();
			}
			catch (Exception e) {
			}
		}

		return existeArchivo(ruta);
	}

	/**
	 * Método que dice si un archivo ya existe o no en la ruta especificada.
	 * 
	 * @param ruta_archivo
	 * @return boolean: verdadero si ya existe, falso si no
	 */
	public boolean existeArchivo(String ruta_archivo) {
		File arch = new File(ruta_archivo);

		return arch.exists();
	}

	/**
	 * Función commander
	 * 
	 * Ejecuta un comando en el sistema.
	 * 
	 * @param commando
	 * @return
	 */
	public String commander(String commandowin, String commandolin) {
		int exitVal = 0;
		Process p = null;
		String os_name = System.getProperty("os.name");
		String mensajes = "";

		if (!tieneAlgo(os_name)) {
			mensajes += mensajeImpersonal("error", "No se logro obtener el nombre del sistema operativo.");
			return "-300-=-" + mensajes;
		}

		mensajes += mensajeImpersonal("nota", "Nombre del sistema operativo: " + os_name);

		try {
			if (os_name.toUpperCase().contains("WIN")) {
				// PARA WINDOWS:
				mensajes += mensajeImpersonal("nota", "Ejecutando comando de windows: " + commandowin);

				p = Runtime.getRuntime().exec("cmd /c " + commandowin);

				StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");

				// any output?
				StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");

				// kick them off
				errorGobbler.start();
				outputGobbler.start();

				// any error???
				exitVal = p.waitFor();
			}
			else {
				// PARA LINUX:
				mensajes += mensajeImpersonal("nota", "Ejecutando comando de UNIX: " + commandolin);
				p = Runtime.getRuntime().exec(commandolin);

				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line = reader.readLine();

				while (line != null) {
					System.out.println(line);
					line = reader.readLine();
				}
				
				exitVal = p.waitFor();

			}
		}
		catch (IOException e) {
			e.printStackTrace();
			exitVal = -100;
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			exitVal = -101;
		}
		catch (Exception e) {
			e.printStackTrace();
			exitVal = -102;
		}

		return String.valueOf(exitVal) + "-=-" + mensajes;
	}

	/**
	 * Método tieneAlgo para averiguar si un string tiene algún valor definido.
	 * 
	 * @param s
	 * @return
	 */
	public boolean tieneAlgo(String s) {
		if (s == null)
			return false;
		if (s.equals(""))
			return false;
		if (s.trim().length() == 0)
			return false;

		return true;
	}

	/**
	 * Retorna un mensaje dentro de tags
	 * <p>
	 * 
	 * @param mensaje
	 * @return String
	 *         <p>
	 *         mensaje
	 *         </p>
	 */
	public String mensaje(String clase, String texto, String usuario, String metodo) {
		//System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "U:" + usuario + " -- M:" + metodo + " -- " + clase + ": " + texto);
		mensajeLog(clase, "U:" + usuario + " -- M:" + metodo + " -- " + clase + ": " + texto);
		return "<p class=\"" + clase + "\">" + texto + "</p>";
	}

	/**
	 * Método para emitir un mensaje en la consola
	 * 
	 * @param str
	 * @return strfinal: mensaje emitido
	 */
	private static String mensajeLog(String clase, String str) {
		Date fecha_actual = null;
		fecha_actual = redondearFecha(new Date(), 0);
		String strfinal = "";

		strfinal = fecha_actual.toString() + " --> " + str;

		if (!clase.equals("error"))
			System.out.println(strfinal);
		else
			System.err.println(strfinal);

		return strfinal;
	}

	/**
	 * Funcion redondearFecha
	 * 
	 * Retorna una fecha con menor nivel de precisión. Esta función se usa para poder comparar dos fechas más fácilmente.
	 * 
	 * @param fecha
	 * @return
	 */
	public static Date redondearFecha(Date fecha, int nivel) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		if (nivel > 0)
			cal.set(Calendar.MILLISECOND, 0);
		if (nivel > 1)
			cal.set(Calendar.SECOND, 0);
		if (nivel > 2)
			cal.set(Calendar.MINUTE, 0);
		if (nivel > 3)
			cal.set(Calendar.HOUR_OF_DAY, 0);
		return cal.getTime();
	}

	/**
	 * Retorna un mensaje dentro de tags
	 * <p>
	 * 
	 * @param mensaje
	 * @return String
	 *         <p>
	 *         mensaje
	 *         </p>
	 */
	public String mensajeImpersonal(String clase, String texto) {
		System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + clase + ": " + texto);
		return "<p class=\"" + clase + "\">" + texto + "</p>";
	}

	/**
	 * Método que retorna un valor especificado si el String dado es null
	 * 
	 * @param s
	 * @param valorSiEsNulo
	 * @return valorSiEsNulo si s==null, s de lo contrario
	 */
	public String nz(String s, String valorSiEsNulo) {
		String resultado = "";

		if (s == null)
			resultado = valorSiEsNulo;
		else
			resultado = s;

		return resultado;
	}

	Connection	conn		= null;
	String		username	= "";
	String		password	= "";
	String		host		= "";
	String		port		= "";
	String		sid			= "";
	String		conexion	= "";

	public String obtenerDato(String sql, String valorSiEsNulo) {

		String[] a_parametros_bd = aParametrosConexionBD();

		this.username = a_parametros_bd[0];
		this.password = a_parametros_bd[1];
		this.host = a_parametros_bd[2];
		this.port = a_parametros_bd[3];
		this.sid = a_parametros_bd[4];

		this.conexion = "jdbc:oracle:thin:@//" + host + ":" + port + "/" + sid;

		Statement stmt = null;
		ResultSet rset = null;

		if (!tieneAlgo(sql))
			return valorSiEsNulo;

		if (valorSiEsNulo == null)
			valorSiEsNulo = "";

		String dato = valorSiEsNulo;

		try {
			if (conn == null) {
				conectarABDEnTransaccion();
			}

			if (conn != null) {
				if (conn.isClosed()) {
					conectarABDEnTransaccion();
				}
			}

			if (conn == null) {
				return "No me pude conectar a la base de datos... Conn:" + conexion + "-User:" + username + "-Pass:" + password + "";
			}

			stmt = conn.createStatement();

			if (stmt.execute(sql)) {
				rset = stmt.getResultSet();

				if (rset.next()) {
					dato = rset.getString(1);
					if (dato == null)
						dato = valorSiEsNulo;
				}

				rset.close();
			}

			stmt.close();

			return dato;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return dato;
	}

	/**
	 * Retorna un arreglo con los parámetros de conexión
	 * 
	 * @return
	 */
	public String[] aParametrosConexionBD() {
		String[] a = { "", "", "", "", "" };

		String username = "";
		String password = "";
		String host = "";
		String port = "";
		String sid = "";

		String directorio_parametros = "/opt/SMBC/conf/AdmIF";
		String parametros_raw = leerArchivoSeparado(directorio_parametros + File.separator + "bd.conf", "\n");

		String[] a_parametros_raw = parametros_raw.split("\n");

		int i = 0;
		for (i = 0; i < a_parametros_raw.length; i++) {
			String[] a_parametro = a_parametros_raw[i].split("=");
			if (a_parametro.length == 2) {
				String nombre_parametro = a_parametro[0];
				String valor_parametro = a_parametro[1];

				if (nombre_parametro.equals("username"))
					username = valor_parametro;
				if (nombre_parametro.equals("password"))
					password = valor_parametro;
				if (nombre_parametro.equals("host"))
					host = valor_parametro;
				if (nombre_parametro.equals("port"))
					port = valor_parametro;
				if (nombre_parametro.equals("sid"))
					sid = valor_parametro;
			}
		}

		a = new String[] { username, password, host, port, sid };

		return a;
	}

	/**
	 * Retorna el contenido de un archivo de texto como un String
	 * 
	 * @param archivo
	 * @return String: contenido de archivo
	 */
	public String leerArchivoSeparado(String nombre_archivo, String separador) {
		String resultado = "";

		try {
			File f = new File(nombre_archivo);

			long i = 0;

			if (f.exists()) {
				BufferedReader input = new BufferedReader(new FileReader(nombre_archivo));
				try {
					String linea = null;
					while ((linea = input.readLine()) != null) {
						if (i > 0)
							resultado += separador;

						resultado += linea.toString();
						i++;
					}
				}
				finally {
					input.close();
				}
			}
		}
		catch (Exception e) {
			mensajeImpersonal("error", "Error al leer del archivo " + nombre_archivo);
		}

		return resultado;
	}

	/**
	 * Método que establece una conexión a la base de datos en transacción (producción o desarrollo).
	 * 
	 * @return conexión JNDI a oracle
	 * @throws NamingException
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unused")
	public boolean conectarABDEnTransaccion() throws NamingException, SQLException, IOException, ClassNotFoundException {
		try {
			@SuppressWarnings("rawtypes")
			Class theClass = null;
			try {
				theClass = Class.forName("oracle.jdbc.driver.OracleDriver", true, Thread.currentThread().getContextClassLoader());
			}
			catch (ClassNotFoundException e) {
				theClass = Class.forName("oracle.jdbc.driver.OracleDriver");
			}

			if (conexion.equals(""))
				return false;
			if (username.equals(""))
				return false;
			if (password.equals(""))
				return false;

			this.conn = DriverManager.getConnection(conexion, username, password);

			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			this.conn = null;
			return false;
		}
	}
	
	//DGR
	
	
	public static String zipArrayFiles(String rutaZip, String sourceFiles) {
		String r = "";
		try {                     
			String commando_transferir = "zip -r " + rutaZip + " " +sourceFiles;
			Auxiliar.commander(commando_transferir, commando_transferir);
			r = Auxiliar.mensajeImpersonal("zipImagenes", "comando: "+commando_transferir);
		}
		catch(Exception ioe) {
			ioe.printStackTrace();
			r = Auxiliar.mensajeImpersonal("error", "zipArrayFiles: No se pudo comprimir la imagen " + ioe.getMessage());
		}	
		return r;
	}
	
	
	/**
	 * Método que exporta el mapa generado por el reporte a un archivo comprimido (producción o desarrollo).
	 * 
	 * @return conexión JNDI a oracle
	 * @throws NamingException
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	
public void exportarMAPA(HttpServletRequest request, HttpServletResponse response) {
		
		String id_reporte = Auxiliar.nzObjStr(request.getParameter("id_reporte"), "");	
		String identImagen = ConsultaReportes.identImagen(id_reporte); 
		String carpeta_extractor = Parametro.getParametro("carpeta_extractor");
		String r = "";
		String carpeta_salida_geoprocesos = Parametro.getParametro("carpeta_salida_geoprocesos");
	
		String ruta_imagen_procesada_zip = carpeta_extractor + "/Mapas/" +  identImagen + ".zip" ;
		
		if (!Archivo.existeArchivo(ruta_imagen_procesada_zip)) {
			String sourceFiles = carpeta_salida_geoprocesos + "/" + identImagen +".tif " + carpeta_salida_geoprocesos + "/" + identImagen +".rrd " + carpeta_salida_geoprocesos + "/" + identImagen +".aux";
			r += zipArrayFiles(ruta_imagen_procesada_zip, sourceFiles);
			
		}
		
		
			try {
				response.setContentType("application/zip");
				response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
				response.setHeader("Cache-Control", "max-age=0");
				response.setHeader("Content-disposition", "attachment; filename=" + identImagen + ".zip");
		
				ServletOutputStream stream = response.getOutputStream();
				FileInputStream input = new FileInputStream(ruta_imagen_procesada_zip);
				BufferedInputStream buf = new BufferedInputStream(input);
				int readBytes = 0;
		
				while ((readBytes = buf.read()) != -1) {
					stream.write(readBytes);
				}
		
				stream.flush();
				buf.close();
				stream.close();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				r += Auxiliar.mensajeImpersonal("error", "exportarMAPA: No se pudo transferir la imagen " +identImagen.replace("_", "") + ".zip ");
			}
		
				
	}
    //DGR
	

}


class StreamGobbler extends Thread {

	InputStream	is;
	String		type;

	StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				System.out.println(type + "> " + line);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}