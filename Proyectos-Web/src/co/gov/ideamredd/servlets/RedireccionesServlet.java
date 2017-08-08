package co.gov.ideamredd.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.util.Util;

public class RedireccionesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	String idProyecto = request.getParameter("proyecto_hidden");
	String usuario=request.getParameter("usuario");
	
	Integer dir = Integer.valueOf(request.getParameter("dir"));
	if (dir == 1)
	{
		if(!usuario.equals(""))
	    response.sendRedirect("pub/detallarProyecto.jsp?id="+Util.encriptar(idProyecto)+"&us="+Util.encriptar(usuario));
		else
			response.sendRedirect("pub/detallarProyecto.jsp?id="+Util.encriptar(idProyecto));
	}
	else if (dir == 2)
	    response.sendRedirect("/pub/modificarParcela.jsp");
	else if (dir == 3){
	    response.sendRedirect(request.getScheme() + "://"
		    + request.getServerName() + ":" + request.getServerPort()
		    + "/BiomasaYCarbono-Web/reg/consultarBiomasa.jsp?id=\""+Util.encriptar(idProyecto)+"\"");
	}else if (dir == 4)
	    response.sendRedirect("pub/consultaReporteProyecto.jsp");
    }

}
