package co.gov.ideamredd.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.entities.Metodologia;
import co.gov.ideamredd.inventarioSuelo.dao.ModificarInventarioSuelo;

/**
 * DatosARegistrarISueloServlet.java Servlet que comunica 
 * los datos a registrar con la pagina de registro de
 * un inventario de suelo.
 * 
 * SMBC Proyecto: Sistema de Monitoreo de Bosques y Carbono
 * 
 * @author Julio Cesar Sanchez Torres Oct 18 de 2013
 */
public class DatosARegistrarISueloServlet extends HttpServlet {

	@EJB
	ModificarInventarioSuelo mis;

	private String metodologias;
	private int indice;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		indice = Integer.parseInt(request.getParameter("ISseleccionados"));

		mis.clearMetodologias();
		metodologias = mis.consultarMetodologias();

		request.getSession().setAttribute("metodologias", metodologias);
		request.getSession().setAttribute("indice", indice);
		response.sendRedirect("reg/registrarInventarioSuelo.jsp");
	}

}
