package co.gov.ideamredd.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DetallarInventarioSueloServlet.java Servlet que comunica organiza los datos
 * para la presentacion en detalle de un inventario de suelo.
 * 
 * SMBC Proyecto: Sistema de Monitoreo de Bosques y Carbono
 * 
 * @author Julio Cesar Sanchez Torres Oct 18 de 2013
 */
public class DetallarInventarioSueloServlet extends HttpServlet {

    private ArrayList<Integer> indicesInvSuelos;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	indicesInvSuelos = new ArrayList<Integer>();
	indicesInvSuelos.clear();
	String selected = request.getParameter("ISseleccionados");

	for (int i = 0; i < selected.length(); i++) {
	    if (selected.charAt(i) != ',') {
		indicesInvSuelos.add(Integer.parseInt(String.valueOf(selected
			.charAt(i))));
	    }
	}

	request.getSession().setAttribute("indicesInvSuelos", indicesInvSuelos);
	response.sendRedirect("reg/detallarInventarioSuelo.jsp");
    }

}
