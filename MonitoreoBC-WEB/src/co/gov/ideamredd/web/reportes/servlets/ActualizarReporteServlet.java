// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.reportes.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.reportes.dao.ActualizaReportes;

/**
 * Servlet usado para actualizar un reporte
 */
public class ActualizarReporteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Integer reporteId = Integer.parseInt(req.getParameter("idReporte"));
		Integer reporteEstado = Integer.parseInt(req.getParameter("estadoReporte"));
		String[] paramTiposReporte = req.getParameterValues("tr");
		String str_paramTiposReporte = "&tr=";
		if (paramTiposReporte != null) {
			if (paramTiposReporte.length > 0) {
				str_paramTiposReporte = "";
				for (int r=0; r<paramTiposReporte.length; r++) {
					str_paramTiposReporte += "&tr="+paramTiposReporte[r];
				}
			}
		}

		ActualizaReportes actrep = new ActualizaReportes();
		actrep.actualizarReporte(reporteId, reporteEstado);

		resp.sendRedirect("admin/consultaReportes.jsp?q2rd0=h014"+str_paramTiposReporte);

	}

}
