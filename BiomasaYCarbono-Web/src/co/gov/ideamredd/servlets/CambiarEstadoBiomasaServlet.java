package co.gov.ideamredd.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.dao.CambiarEstadoBiomasa;

public class CambiarEstadoBiomasaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private CambiarEstadoBiomasa cambiarEstado;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idBiomasa = request.getParameter("idBiomasa");
		String verificado = request.getParameter("verificado");
		String atipico = request.getParameter("atipico");
		String incluido = request.getParameter("incluido");
		
		cambiarEstado.setVerificado(verificado);
		cambiarEstado.setAtipico(atipico);
		cambiarEstado.setIncluido(incluido);
		cambiarEstado.setIdBiomasa(Integer.valueOf(idBiomasa));
		cambiarEstado.actualizarEstado();
	}

}
