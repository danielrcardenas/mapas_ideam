package co.gov.ideamredd.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.conexionBD.Parametro;
import co.gov.ideamredd.entities.InformacionReporteInventarios;
import co.gov.ideamredd.reportes.ConsultarReporteInventario;

/**
 * Servlet que responde a la consulta de reporte de inventarios por
 * individuos y por parcelas
 * @author Daniel Rodoríguez
 *
 */
public class ConsultarReporteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private ConsultarReporteInventario reporteInventarioEJB;

	private static final String FORMATO_FECHA = "dd/MM/yyyy";

	private DateFormat dateFormat = new SimpleDateFormat(FORMATO_FECHA);


	/*
	 * public ConsultarReporteServlet() { super(); }
	 */
	
	/**
	 * Método que reponde a la solucitud desde la página por metodo post
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Integer treporte =
			// Integer.valueOf(request.getParameter("treporte"));
			String especie = request.getParameter("especie");
			// Integer propositoParcela =
			// Integer.valueOf(request.getParameter("pparcela"));
			String fechaInicial = request.getParameter("fechaIni");
			String fechaFinal = request.getParameter("fechaFin");
			String parcela = request.getParameter("parcela");
			InformacionReporteInventarios filtro = new InformacionReporteInventarios();
			filtro.setEspecie(especie);
			filtro.setParcela(parcela);
			filtro.setFechaInicialFiltro(new Date(dateFormat
					.parse(fechaInicial).getTime()));
			filtro.setFechaFinalFiltro(new Date(dateFormat.parse(fechaFinal)
					.getTime()));
			String resourcePath = Parametro.getParametro("resourcePath");
			String scriptPath = resourcePath + File.separator + "scripts";
			
			filtro.setrScriptPath(scriptPath);
			filtro.setTemplatePath(resourcePath + File.separator + "templates");

			String tablas = reporteInventarioEJB.generarTablasHTML(filtro);
			System.out.println("Doing post: especie: " +especie +" parcela: " + parcela + " fechaini: "+ fechaInicial + " fechafin: "+ fechaFinal);
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.print(tablas);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
