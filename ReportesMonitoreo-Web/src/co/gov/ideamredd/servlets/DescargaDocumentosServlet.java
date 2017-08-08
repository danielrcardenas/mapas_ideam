package co.gov.ideamredd.servlets;

import java.io.BufferedInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import co.gov.ideamredd.conexionBD.Parametro;

import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;

public class DescargaDocumentosServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private Parametro parametro;

	public DescargaDocumentosServlet() {
		super();
	}

	private static final String BUNDLE_NAME = "co/gov/ideamredd/ui/dao/Configuracion";
	private static final ResourceBundle properties = ResourceBundle
			.getBundle(BUNDLE_NAME);
	private String dirDescargas;
	private String dirBirtReportEnguine;
	private String dirBirtLog;
	private String dirBirtPlantillas;
	private String fileNombre;
	private Integer variosReportes;
	private String tipoDivision;
	private Integer treporte;
	private Integer divterritorial;
	private ArrayList<String> subNombresArchivos;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String tipoArchivo = request.getParameter("tipoArchivo");
		treporte = Integer.valueOf(request.getParameter("PDFtiporeporte"));
		divterritorial = Integer.valueOf(request.getParameter("PDFdivision"));
		variosReportes = Integer
				.valueOf(request.getParameter("variosPeriodos"));
		tipoDivision = "";

		dirDescargas = parametro.getParametro("ruta_reportes") + "/";
		dirBirtReportEnguine = parametro.getParametro("birt.reportEnguine");
		dirBirtLog = parametro.getParametro("birt.logDir");
		dirBirtPlantillas = parametro.getParametro("birt.Plantillas") + "/";

		if (tipoArchivo.equals("xlsx")) {
			reporteXLSX(request, response);
		} else if (tipoArchivo.equals("pdf")) {
			reportePDF(request, response);
		}

	}

	public void reporteXLSX(HttpServletRequest request,
			HttpServletResponse response) {
		String uriFile = request.getParameter("path"); // el URL enviado como
		// parametro
		String dato=parametro.getParametro("ruta_reportes");
		String nombre = request.getParameter("path").replaceAll(
				parametro.getParametro("ruta_reportes"), "");

		response.setContentType("application/xlsx");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader("Content-disposition", "attachment; filename="
				+ nombre);
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void reportePDF(HttpServletRequest request,
			HttpServletResponse response) {
		fileNombre = "";
		subNombresArchivos = new ArrayList<String>();
		String periodo = "";
		String[] periodos = null;
		if (variosReportes == 0) {
			periodo = request.getParameter("PDFperiodo");
		} else {
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
				if (periodos != null)
					if (periodos.length > 1) {
						agregarDivisionSubNombres(tipoDivision);
					}

			} else if (treporte > 4 && treporte < 7) {
				fileNombre = fileNombre + "Deforestacion";
				agregarPeriodos(periodo, periodos);
				if (treporte == 5) {
					if (divterritorial == 3) {
						tipoDivision = "AreaHidrografica";
					} else if (divterritorial == 4) {
						tipoDivision = "ConsolidadoNacional";
					}
				} else {
					if (divterritorial == 3) {
						tipoDivision = "AreasHidrograficas";
					} else if (divterritorial == 4) {
						tipoDivision = "ConsolidadoNacional";
					}
				}
				fileNombre = fileNombre + tipoDivision;
				if (periodos != null)
					if (periodos.length > 1) {
						agregarDivisionSubNombres(tipoDivision);
					}

			} else if (treporte == 7) {
				fileNombre = fileNombre + "Biomasa";
				agregarPeriodos(periodo, periodos);
				if (divterritorial == 3) {
					tipoDivision = "AreaHidrografica";
				} else if (divterritorial == 4) {
					tipoDivision = "ConsolidadoNacional";
				}
				fileNombre = fileNombre + tipoDivision;
				if (periodos != null)
					if (periodos.length > 1) {
						agregarDivisionSubNombres(tipoDivision);
					}
			} else {
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
				if (periodos != null)
					if (periodos.length > 1) {
						agregarDivisionSubNombres(tipoDivision);
					}
			}

			String rutaArchivo = dirDescargas + fileNombre + ".pdf";

			File archivo = new File(rutaArchivo);

			if (!archivo.exists()) {
				final EngineConfig config = new EngineConfig();

				config.setEngineHome(dirBirtReportEnguine);
				config.setLogConfig(dirBirtLog, Level.SEVERE);

				Platform.startup(config); // If using RE API in Eclipse/RCP
											// application this is not needed.
				IReportEngineFactory factory = (IReportEngineFactory) Platform
						.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
				IReportEngine engine = factory.createReportEngine(config);
				engine.changeLogLevel(Level.WARNING);

				String nombrePlantilla = "report";
				if (treporte < 3) {
					nombrePlantilla = nombrePlantilla + "BosqueNoBosque";
				} else if (treporte > 4 && treporte < 7) {
					nombrePlantilla = nombrePlantilla + "Deforestacion";
				} else if (treporte == 7) {
					nombrePlantilla = nombrePlantilla + "Biomasa";
				} else {
					nombrePlantilla = nombrePlantilla + "Cobertura";
				}
				if (divterritorial == 4) {
					nombrePlantilla = nombrePlantilla + "Cons.rptdesign";
				} else {
					nombrePlantilla = nombrePlantilla + ".rptdesign";
				}

				IReportRunnable design = null;

				design = engine.openReportDesign(dirBirtPlantillas
						+ nombrePlantilla);

				if (treporte < 3) {
					if (variosReportes == 0) {
						// Create task to run and render the report,
						IRunAndRenderTask task = engine
								.createRunAndRenderTask(design);

						// Set parent classloader for engine
						task.getAppContext().put(
								EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
								DescargaDocumentosServlet.class
										.getClassLoader());

						task.setParameterValue("prm_Periodo", periodo
								.substring(periodo.indexOf("-") + 1,
										periodo.length()));
						task.setParameterValue(
								"prm_IdReporte",
								Integer.valueOf(periodo.substring(0,
										periodo.indexOf("-"))));
						task.setParameterValue("prm_DivisionNombre",
								tipoDivision);

						// Setup rendering to HTML
						PDFRenderOption options = new PDFRenderOption();
						options.setOutputFileName(rutaArchivo);
						options.setOutputFormat("pdf");
						task.setRenderOption(options);
						// run and render report
						task.run();
						task.close();
					} else {
						for (int r = 0; r < periodos.length; r++) {
							if (periodos.length > 1) {
								String auxRutaFile = dirDescargas
										+ subNombresArchivos.get(r);
								File auxFile = new File(auxRutaFile);

								if (!auxFile.exists()) {
									// Create task to run and render the report,
									IRunAndRenderTask task = engine
											.createRunAndRenderTask(design);

									// Set parent classloader for engine
									task.getAppContext()
											.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
													DescargaDocumentosServlet.class
															.getClassLoader());

									task.setParameterValue(
											"prm_Periodo",
											periodos[r].substring(
													periodos[r].indexOf("-") + 1,
													periodos[r].length()));
									task.setParameterValue("prm_IdReporte",
											Integer.valueOf(periodos[r]
													.substring(0, periodos[r]
															.indexOf("-"))));
									task.setParameterValue(
											"prm_DivisionNombre", tipoDivision);

									PDFRenderOption options = new PDFRenderOption();
									options.setOutputFileName(auxRutaFile);
									options.setOutputFormat("pdf");
									task.setRenderOption(options);
									// run and render report
									task.run();
									task.close();

								}
							} else {
								// Create task to run and render the report,
								IRunAndRenderTask task = engine
										.createRunAndRenderTask(design);

								// Set parent classloader for engine
								task.getAppContext()
										.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
												DescargaDocumentosServlet.class
														.getClassLoader());

								task.setParameterValue("prm_Periodo",
										periodos[r].substring(
												periodos[r].indexOf("-") + 1,
												periodos[r].length()));
								task.setParameterValue("prm_IdReporte", Integer
										.valueOf(periodos[r].substring(0,
												periodos[r].indexOf("-"))));
								task.setParameterValue("prm_DivisionNombre",
										tipoDivision);

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
				} else if (treporte > 4 && treporte < 7) {
					if (variosReportes == 0) {
						// Create task to run and render the report,
						IRunAndRenderTask task = engine
								.createRunAndRenderTask(design);

						// Set parent classloader for engine
						task.getAppContext().put(
								EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
								DescargaDocumentosServlet.class
										.getClassLoader());

						task.setParameterValue("prm_Periodo", periodo
								.substring(periodo.indexOf("-") + 1,
										periodo.length()));
						task.setParameterValue(
								"prm_IdReporte",
								Integer.valueOf(periodo.substring(0,
										periodo.indexOf("-"))));

						// Setup rendering to HTML
						PDFRenderOption options = new PDFRenderOption();
						options.setOutputFileName(rutaArchivo);
						options.setOutputFormat("pdf");
						task.setRenderOption(options);
						// run and render report
						task.run();
						task.close();
					} else {
						for (int r = 0; r < periodos.length; r++) {
							if (periodos.length > 1) {
								String auxRutaFile = dirDescargas
										+ subNombresArchivos.get(r);
								File auxFile = new File(auxRutaFile);

								if (!auxFile.exists()) {
									// Create task to run and render the report,
									IRunAndRenderTask task = engine
											.createRunAndRenderTask(design);

									// Set parent classloader for engine
									task.getAppContext()
											.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
													DescargaDocumentosServlet.class
															.getClassLoader());

									task.setParameterValue(
											"prm_Periodo",
											periodos[r].substring(
													periodos[r].indexOf("-") + 1,
													periodos[r].length()));
									task.setParameterValue("prm_IdReporte",
											Integer.valueOf(periodos[r]
													.substring(0, periodos[r]
															.indexOf("-"))));

									PDFRenderOption options = new PDFRenderOption();
									options.setOutputFileName(auxRutaFile);
									options.setOutputFormat("pdf");
									task.setRenderOption(options);
									// run and render report
									task.run();
									task.close();
								}
							} else {
								// Create task to run and render the report,
								IRunAndRenderTask task = engine
										.createRunAndRenderTask(design);

								// Set parent classloader for engine
								task.getAppContext()
										.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
												DescargaDocumentosServlet.class
														.getClassLoader());

								task.setParameterValue("prm_Periodo",
										periodos[r].substring(
												periodos[r].indexOf("-") + 1,
												periodos[r].length()));
								task.setParameterValue("prm_IdReporte", Integer
										.valueOf(periodos[r].substring(0,
												periodos[r].indexOf("-"))));

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
				} else if (treporte == 7) {
					if (variosReportes == 0) {
						// Create task to run and render the report,
						IRunAndRenderTask task = engine
								.createRunAndRenderTask(design);

						// Set parent classloader for engine
						task.getAppContext().put(
								EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
								DescargaDocumentosServlet.class
										.getClassLoader());

						task.setParameterValue("prm_Periodo", periodo
								.substring(periodo.indexOf("-") + 1,
										periodo.length()));
						task.setParameterValue(
								"prm_IdReporte",
								Integer.valueOf(periodo.substring(0,
										periodo.indexOf("-"))));

						// Setup rendering to HTML
						PDFRenderOption options = new PDFRenderOption();

						options.setOutputFileName(rutaArchivo);
						options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
						options.setOption(IPDFRenderOption.PDF_HYPHENATION,
								true);
						options.setOption(IPDFRenderOption.PDF_TEXT_WRAPPING,
								true);
						task.setRenderOption(options);
						// run and render report
						task.run();
						task.close();
					} else {
						for (int r = 0; r < periodos.length; r++) {
							if (periodos.length > 1) {
								String auxRutaFile = dirDescargas
										+ subNombresArchivos.get(r);
								File auxFile = new File(auxRutaFile);

								if (!auxFile.exists()) {
									// Create task to run and render the report,
									IRunAndRenderTask task = engine
											.createRunAndRenderTask(design);

									// Set parent classloader for engine
									task.getAppContext()
											.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
													DescargaDocumentosServlet.class
															.getClassLoader());

									task.setParameterValue(
											"prm_Periodo",
											periodos[r].substring(
													periodos[r].indexOf("-") + 1,
													periodos[r].length()));
									task.setParameterValue("prm_IdReporte",
											Integer.valueOf(periodos[r]
													.substring(0, periodos[r]
															.indexOf("-"))));

									PDFRenderOption options = new PDFRenderOption();
									options.setOutputFileName(auxRutaFile);
									options.setOutputFormat("pdf");
									task.setRenderOption(options);
									// run and render report
									task.run();
									task.close();
								}
							} else {
								// Create task to run and render the report,
								IRunAndRenderTask task = engine
										.createRunAndRenderTask(design);

								// Set parent classloader for engine
								task.getAppContext()
										.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
												DescargaDocumentosServlet.class
														.getClassLoader());

								task.setParameterValue("prm_Periodo",
										periodos[r].substring(
												periodos[r].indexOf("-") + 1,
												periodos[r].length()));
								task.setParameterValue("prm_IdReporte", Integer
										.valueOf(periodos[r].substring(0,
												periodos[r].indexOf("-"))));

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
				} else {
					if (variosReportes == 0) {
						// Create task to run and render the report,
						IRunAndRenderTask task = engine
								.createRunAndRenderTask(design);

						// Set parent classloader for engine
						task.getAppContext().put(
								EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
								DescargaDocumentosServlet.class
										.getClassLoader());

						task.setParameterValue("prm_Periodo", periodo
								.substring(periodo.indexOf("-") + 1,
										periodo.length()));
						task.setParameterValue(
								"prm_IdReporte",
								Integer.valueOf(periodo.substring(0,
										periodo.indexOf("-"))));
						task.setParameterValue("prm_DivisionNombre",
								tipoDivision);
						String periodo1 = periodo.substring(
								periodo.indexOf("-") + 1, periodo.length());
						periodo1 = periodo1.substring(0, periodo1.indexOf("-"));
						String periodo2 = periodo.substring(
								periodo.indexOf("-") + 1, periodo.length());
						periodo2 = periodo2.substring(
								periodo2.indexOf("-") + 1, periodo2.length());
						task.setParameterValue("prm_Periodo1",
								Integer.valueOf(periodo1));
						task.setParameterValue("prm_Periodo2",
								Integer.valueOf(periodo2));

						// Setup rendering to HTML
						PDFRenderOption options = new PDFRenderOption();
						options.setOutputFileName(rutaArchivo);
						options.setOutputFormat("pdf");
						task.setRenderOption(options);
						// run and render report
						task.run();
						task.close();
					} else {
						for (int r = 0; r < periodos.length; r++) {
							if (periodos.length > 1) {
								String auxRutaFile = dirDescargas
										+ subNombresArchivos.get(r);
								File auxFile = new File(auxRutaFile);

								if (!auxFile.exists()) {
									// Create task to run and render the report,
									IRunAndRenderTask task = engine
											.createRunAndRenderTask(design);

									// Set parent classloader for engine
									task.getAppContext()
											.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
													DescargaDocumentosServlet.class
															.getClassLoader());

									task.setParameterValue(
											"prm_Periodo",
											periodos[r].substring(
													periodos[r].indexOf("-") + 1,
													periodos[r].length()));
									task.setParameterValue("prm_IdReporte",
											Integer.valueOf(periodos[r]
													.substring(0, periodos[r]
															.indexOf("-"))));
									task.setParameterValue(
											"prm_DivisionNombre", tipoDivision);
									String auxPeriodo = periodos[r].substring(
											periodos[r].indexOf("-") + 1,
											periodos[r].length());
									String periodo1 = auxPeriodo.substring(0,
											auxPeriodo.indexOf("-"));
									String periodo2 = auxPeriodo.substring(
											auxPeriodo.indexOf("-") + 1,
											auxPeriodo.length());
									task.setParameterValue("prm_Periodo1",
											Integer.valueOf(periodo1));
									task.setParameterValue("prm_Periodo2",
											Integer.valueOf(periodo2));

									PDFRenderOption options = new PDFRenderOption();
									options.setOutputFileName(auxRutaFile);
									options.setOutputFormat("pdf");
									task.setRenderOption(options);
									// run and render report
									task.run();
									task.close();
								}
							} else {
								// Create task to run and render the report,
								IRunAndRenderTask task = engine
										.createRunAndRenderTask(design);

								// Set parent classloader for engine
								task.getAppContext()
										.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
												DescargaDocumentosServlet.class
														.getClassLoader());

								task.setParameterValue("prm_Periodo",
										periodos[r].substring(
												periodos[r].indexOf("-") + 1,
												periodos[r].length()));
								task.setParameterValue("prm_IdReporte", Integer
										.valueOf(periodos[r].substring(0,
												periodos[r].indexOf("-"))));
								task.setParameterValue("prm_DivisionNombre",
										tipoDivision);
								String auxPeriodo = periodos[r].substring(
										periodos[r].indexOf("-") + 1,
										periodos[r].length());
								String periodo1 = auxPeriodo.substring(0,
										auxPeriodo.indexOf("-"));
								String periodo2 = auxPeriodo.substring(
										auxPeriodo.indexOf("-") + 1,
										auxPeriodo.length());
								task.setParameterValue("prm_Periodo1",
										Integer.valueOf(periodo1));
								task.setParameterValue("prm_Periodo2",
										Integer.valueOf(periodo2));

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

				if(periodos != null)
				if (variosReportes == 1 && periodos.length>1) {
					PdfCopyFields merger = new PdfCopyFields(
							new FileOutputStream(rutaArchivo));
					for (int r = 0; r < periodos.length; r++) {
						merger.addDocument(new PdfReader(new FileInputStream(
								new File(dirDescargas
										+ subNombresArchivos.get(r)))));
					}
					merger.close();
				}

			}

			response.setContentType("application/pdf");
			response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
			response.setHeader("Cache-Control", "max-age=0");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fileNombre+".pdf");

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

	public void agregarPeriodos(String periodo, String[] periodos) {
		String fileNombreAux=fileNombre;
		if (variosReportes == 0) {
			fileNombre = fileNombre
					+ periodo.substring(periodo.indexOf("-") + 1,
							periodo.length());
		} else {
			for (int c = 0; c < periodos.length; c++) {
				if (periodos.length > 1) {
					fileNombre = fileNombre
							+ periodos[c].substring(
									periodos[c].indexOf("-") + 1,
									periodos[c].length());

					subNombresArchivos.add(fileNombreAux
							+ periodos[c].substring(
									periodos[c].indexOf("-") + 1,
									periodos[c].length()));

				} else {
					fileNombre = fileNombre
							+ periodos[c].substring(
									periodos[c].indexOf("-") + 1,
									periodos[c].length());
				}
			}
		}
	}

	public void agregarDivisionSubNombres(String nombreDivision) {
		for (int x = 0; x < subNombresArchivos.size(); x++) {
			subNombresArchivos.set(x, subNombresArchivos.get(x)
					+ nombreDivision + ".pdf");
		}
	}
}
