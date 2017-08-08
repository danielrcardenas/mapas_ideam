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
import co.gov.ideamredd.reportes.ConsultarReporteSimilitud;

/**
 * Clase servlet que responde a la solicitud de reportes por similitud
 * conecta a la clase encargada de ejecutar el procesamiento
 * y devolver una respuesta al jsp que lo solicita generando
 * HTML con la respuesta
 * @author developer
 *
 */
public class ConsultarReporteSimilitudServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private ConsultarReporteSimilitud reporteInventarioEJB;

	private static final String FORMATO_FECHA = "dd/MM/yyyy";

	private DateFormat dateFormat = new SimpleDateFormat(FORMATO_FECHA);

	/*
	 * public ConsultarReporteServlet() { super(); }
	 */

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int tBosque1 = 0, tBosque2 = 0;
		String fechaInicial1, fechaInicial2, fechaFinal1, fechaFinal2;
		int deptosAux1 = 0, deptosAux2 = 0;
		String parcelas1, parcelas2;
		String especies1, especies2;
		InformacionReporteInventarios filtro1 = new InformacionReporteInventarios();
		InformacionReporteInventarios filtro2 = new InformacionReporteInventarios();

		try {
			tBosque1 = Integer.parseInt(request.getParameter("tBosque1"));
		} catch (NumberFormatException e) {
		}
		try {
			tBosque2 = Integer.parseInt(request.getParameter("tBosque2"));
		} catch (NumberFormatException e) {
		}
		try {
			deptosAux1 = Integer.parseInt(request.getParameter("deptosAux1"));
		} catch (NumberFormatException e) {
		}
		try {
			deptosAux2 = Integer.parseInt(request.getParameter("deptosAux2"));
		} catch (NumberFormatException e) {
		}

		try {
			fechaInicial1 = request.getParameter("fechaIni1");
			fechaFinal1 = request.getParameter("fechaFin1");
			fechaInicial2 = request.getParameter("fechaIni2");
			fechaFinal2 = request.getParameter("fechaFin2");
			parcelas1 = request.getParameter("parcela1");
			parcelas2 = request.getParameter("parcela2");
			especies1 = request.getParameter("especie1");
			especies2 = request.getParameter("especie2");

			filtro1.setEspecie(especies1);
			filtro2.setEspecie(especies2);

			filtro1.setParcela(parcelas1);
			filtro2.setParcela(parcelas2);

			filtro1.setDepartamento(deptosAux1);
			filtro2.setDepartamento(deptosAux2);

			filtro1.setTipoBosque(tBosque1);
			filtro2.setTipoBosque(tBosque2);

			filtro1.setFechaInicialFiltro(new Date(dateFormat.parse(
					fechaInicial1).getTime()));
			filtro1.setFechaFinalFiltro(new Date(dateFormat.parse(fechaFinal1)
					.getTime()));

			filtro2.setFechaInicialFiltro(new Date(dateFormat.parse(
					fechaInicial2).getTime()));
			filtro2.setFechaFinalFiltro(new Date(dateFormat.parse(fechaFinal2)
					.getTime()));

			String resourcePath = getServletContext().getRealPath("");
			resourcePath = resourcePath.substring(0,
					resourcePath.lastIndexOf(File.separator));
			//resourcePath = "/etc/SMBC/ReportesInventario";
			resourcePath = Parametro.getParametro("resourcePath");

			String scriptPath = resourcePath + File.separator + "scripts";
			
			filtro1.setrScriptPath(scriptPath);
			filtro1.setTemplatePath(resourcePath + File.separator + "templates");

			filtro2.setrScriptPath(scriptPath);
			filtro2.setTemplatePath(resourcePath + File.separator + "templates");

			String tablas = reporteInventarioEJB.generarTablasHTML(filtro1,
					filtro2);

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.print(tablas);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
