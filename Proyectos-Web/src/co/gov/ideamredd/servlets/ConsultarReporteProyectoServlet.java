package co.gov.ideamredd.servlets;

import java.io.IOException;


import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.dao.ConsultarReporteProyecto;

public class ConsultarReporteProyectoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	private ConsultarReporteProyecto consultarReporteProyecto;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String tipoBosque = request.getParameter("tipoBosque");
		String fechaInicial = request.getParameter("fInicial");
		String fechaFinal = request.getParameter("ffin");
		
		if(fechaInicial.equals(""))
			fechaInicial = "01/01/1970";
		if(fechaFinal.equals(""))
			fechaFinal = "31/12/2500";
		
		consultarReporteProyecto.setTipoBosque(tipoBosque);
		consultarReporteProyecto.setFechaInicial(fechaInicial);
		consultarReporteProyecto.setFechaFinal(fechaFinal);
		
		consultarReporteProyecto.consultarReporte();
	}

}
