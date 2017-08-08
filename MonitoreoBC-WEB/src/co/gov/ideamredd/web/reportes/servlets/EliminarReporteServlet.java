// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.reportes.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.reportes.dao.EliminaReportes;

/**
 * Servlet usado para eliminar un reporte
 */
public class EliminarReporteServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Integer reporteId = Integer.parseInt(req.getParameter("idReporte"));
		
		EliminaReportes elimrep= new EliminaReportes();
		
		elimrep.eliminarReporte(reporteId);
		
		resp.sendRedirect("admin/consultaReportes.jsp");
	}

}
