package co.gov.ideamredd.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.entities.Especie;

public class IrAConsultarEspecieServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ArrayList<Especie> listaEspecies=new ArrayList<Especie>();
		request.getSession().setAttribute("listaEspecies", listaEspecies);
		response.sendRedirect("reg/consultaEspecies.jsp");	
	}

}
