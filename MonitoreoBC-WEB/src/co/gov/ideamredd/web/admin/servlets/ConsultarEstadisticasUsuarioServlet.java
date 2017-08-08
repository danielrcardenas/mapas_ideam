// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.admin.dao.ConsultaUsuarioAdmin;

/**
 * Servlet usado para consultar estadisticas de usuarios
 */
public class ConsultarEstadisticasUsuarioServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@EJB
	private ConsultaUsuarioAdmin consultaUsuario;
	
	private String fechaini;
	private String fechafin;
	private ArrayList<Object> estadistica1;
	private ArrayList<Object> estadistica2;
	private ArrayList<Integer> estadistica3;
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		
		fechaini = req.getParameter("fini")==""?null:req.getParameter("fini");
		fechafin = req.getParameter("ffin")==""?null:req.getParameter("ffin");

		estadistica1 = consultaUsuario.consultarEstadisticasTiposPersona(fechaini, fechafin);
		estadistica2 = consultaUsuario.consultarEstadisticasDepartamentos(fechaini, fechafin);
		estadistica3 = consultaUsuario.consultarEstadisticasTotales(fechaini, fechafin);

		req.getSession().setAttribute("estadistica1", estadistica1);
		req.getSession().setAttribute("estadistica2", estadistica2);
		req.getSession().setAttribute("estadistica3", estadistica3);
		req.getSession().setAttribute("fechaIni", fechaini);
		req.getSession().setAttribute("fechaFin", fechafin);
		resp.sendRedirect("admin/generarReportesUsuarios.jsp");
	}

}
