package co.gov.ideamredd.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import co.gov.ideamredd.inventarioSuelo.dao.ModificarInventarioSuelo;
import co.gov.ideamredd.entities.Metodologia;

/**
 * DatosAModificarISueloServlet.java Servlet que se encarga de proporcionar
 * los datos para inicializar la ventana de modificacion
 * 
 * SMBC Proyecto: Sistema de Monitoreo de Bosques y Carbono
 * 
 * @author Julio Cesar Sanchez Torres Oct 22 de 2013
 */
public class DatosAModificarISueloServlet extends HttpServlet{
	
	@EJB
	ModificarInventarioSuelo mis;
	
	private String metodologias;
	private int indice;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String selected = request.getParameter("ISseleccionados");
		indice=Integer.parseInt(selected);
		
		mis.clearMetodologias();
		metodologias=mis.consultarMetodologias();
		
		request.getSession().setAttribute("metodologias", metodologias);
		request.getSession().setAttribute("indice", indice);
		response.sendRedirect("reg/modificarInventarioSuelo.jsp");
	}

}
