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
	String parametros_encriptados = "";
	
	String idParcela = request.getParameter("parcela_hidden");//+";1";
	String lenguaje = request.getParameter("lenguaje");//+";1";
	
	Integer dir = Integer.valueOf(nz(request.getParameter("dir"), "0"));
	String id_usuario = request.getParameter("id_usuario");
	if (id_usuario.equals("")) id_usuario = "123";
	
	String id_usuario_encriptado = Util.encriptar(id_usuario);
	
	request.getSession().setAttribute("parcela", idParcela);

	parametros_encriptados = Util.encriptar(id_usuario + "--" + lenguaje + "--" + idParcela);
	//parametros_encriptados = id_usuario + "--" + lenguaje + "--" + idParcela;

	
	if (dir == 1)
	    response.sendRedirect("pub/detallarParcela.jsp");
	else if (dir == 2)
	    response.sendRedirect("reg/modificarParcela.jsp");
	else if (dir == 3){
	    response.sendRedirect(request.getScheme() + "://"
		    + request.getServerName() + ":" + request.getServerPort()
		    + "/BiomasaYCarbono-Web/reg/consultarBiomasa.jsp?id=\""+Util.encriptar(idParcela)+"\"");
	}
	else if (dir == 4) response.sendRedirect("pub/detallarParcela.jsp");
	else if (dir == 5) {
	    //response.sendRedirect("../Importador/busqueda_individuos.jsp?parametros=" + id_usuario + "--" + lenguaje + "--" + idParcela );
	    response.sendRedirect("pub/individuos.jsp?id=" + id_usuario_encriptado + "&u=" + id_usuario_encriptado );
	}
	else if (dir == 6) { 
	    //response.sendRedirect("../Importador/menu.jsp?parametros=" + id_usuario + "--" + lenguaje + "--" + idParcela );
	    response.sendRedirect("pub/cargue.jsp?id=" + id_usuario_encriptado + "&u=" + id_usuario_encriptado  );
	}
    }
    
	/**
	 * Método que retorna un valor especificado si el String dado es null 
	 * @param s
	 * @param valorSiEsNulo
	 * @return valorSiEsNulo si s==null, s de lo contrario
	 */
	private String nz(String s, String valorSiEsNulo)
	{
		String resultado = "";
		
		if (s == null) resultado = valorSiEsNulo;
		else resultado = s;
		
		return resultado;
	}

}
