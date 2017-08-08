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
import co.gov.ideamredd.reportes.ConsultarReporteInventarioEstComp;
/**
 * 
 * @author Daniel Rodríguez
 *
 *Clase servlet que responde a la petición del reporte
 *de estructura y composición. Genera código HTML con el resultado
 */
public class ConsultarReporteEstCompServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private ConsultarReporteInventarioEstComp reporteInventarioEJB;

	/**
	 * Formato de la fecha con la que se toma el periodo de muestreo
	 */
	private static final String FORMATO_FECHA = "dd/MM/yyyy";

	private DateFormat dateFormat = new SimpleDateFormat(FORMATO_FECHA);

	/*
	 * public ConsultarReporteServlet() { super(); }
	 */

	/**
	 * Metodo que responde a la petición post desde la página de usuario
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			int tBosque = Integer.parseInt(request.getParameter("tBosque"));

			String fechaInicial = request.getParameter("fechaIni");
			String fechaFinal = request.getParameter("fechaFin");
			int deptosAux = Integer.parseInt(request.getParameter("deptosAux"));
			InformacionReporteInventarios filtro = new InformacionReporteInventarios();
			filtro.setDepartamento(deptosAux);
			filtro.setTipoBosque(tBosque);
			filtro.setFechaInicialFiltro(new Date(dateFormat
					.parse(fechaInicial).getTime()));
			filtro.setFechaFinalFiltro(new Date(dateFormat.parse(fechaFinal)
					.getTime()));
			String resourcePath = getServletContext().getRealPath("");
			resourcePath = resourcePath.substring(0,
					resourcePath.lastIndexOf(File.separator));
			//resourcePath = "/etc/SMBC/ReportesInventario";
			resourcePath = Parametro.getParametro("resourcePath");

			String scriptPath = resourcePath + File.separator + "scripts";
			
			filtro.setrScriptPath(scriptPath);
			filtro.setTemplatePath(resourcePath + File.separator + "templates");

			String tablas = reporteInventarioEJB.generarTablasHTML(filtro);

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.print(tablas);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
