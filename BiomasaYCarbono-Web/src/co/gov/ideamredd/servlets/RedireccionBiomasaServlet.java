package co.gov.ideamredd.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.util.Util;

public class RedireccionBiomasaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	String idBiomasa = request.getParameter("biomasa_hidden");
	Integer dir = Integer.valueOf(request.getParameter("dir"));
	if (dir == 1)
	    response.sendRedirect("reg/definirEstadoBiomasa.jsp?bio="+Util.encriptar(idBiomasa));
	else if (dir == 2)
	    response.sendRedirect("reg/registrarBiomasa.jsp?id="+Util.encriptar(idBiomasa));
	else if (dir == 3){
	    response.sendRedirect(request.getScheme() + "://"
		    + request.getServerName() + ":" + request.getServerPort()
		    + "/BiomasaYCarbono-Web/reg/consultarBiomasa.jsp?id=\""+Util.encriptar(idBiomasa)+"\"");
	}else if (dir == 4)
	    response.sendRedirect("pub/consultaReporteProyecto.jsp");
    }

}