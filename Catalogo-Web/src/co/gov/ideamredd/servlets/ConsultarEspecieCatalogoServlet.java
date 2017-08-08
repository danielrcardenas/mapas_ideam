package co.gov.ideamredd.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.Catalogo.ConsultarEspecieCatalogo;
import co.gov.ideamredd.entities.Especie;

public class ConsultarEspecieCatalogoServlet extends HttpServlet{
	
	@EJB
	private ConsultarEspecieCatalogo cic;
	
	private String cadena;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		cadena = request.getParameter("palabraClave");
		
		ArrayList<Especie> listaEspecies=new ArrayList<Especie>();
		cic.consultaBasicaEspecie(cadena, listaEspecies);
		
		request.getSession().setAttribute("listaEspecies", listaEspecies);
		response.sendRedirect("reg/consultaEspecies.jsp");
	}

}
