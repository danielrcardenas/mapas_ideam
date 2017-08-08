package co.gov.ideamredd.parametros.servlets;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import co.gov.ideamredd.dao.ActualizarParametro;


public class EditarParametroServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@EJB
	ActualizarParametro actualizarParametro;
	private static Logger log=Logger.getLogger("SMBCLog");
	private Integer numRutas;
	private String ruta;
	private Integer idParametro;
	boolean actualizado;
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info(EditarParametroServlet.class+" Inicio edición parametros");
		numRutas = Integer.parseInt(req.getParameter("numRutas"));
		actualizado = true;
		for(int i = 0; i<numRutas && actualizado==true; i++){
			actualizado=false;
			if(i==0){
				ruta=req.getParameter("textRuta"+i);
				 actualizado = actualizarParametro.editarRutaMetadato(actualizarParametro.getRutaProviders(), ruta);
				//editar la ruta del metadato
			}else if(i==1){
				ruta=req.getParameter("textRuta"+i);
				 actualizado = actualizarParametro.editarRutaThumbnail(actualizarParametro.getRutaProviders(), ruta);
				//editar la ruta del thumbnail
			}else{
				ruta=req.getParameter("textRuta"+i);
				//se decrementa en uno debido a que el indice de la base de datos comienza en 1 y el textRuta del primer campo corresponde a 2
				idParametro=i-1;
				actualizarParametro.setUna_ruta(ruta);
				actualizarParametro.setIdParametro(idParametro);
			    actualizado = actualizarParametro.modificarParametro();
			}
			
		}
		if(!actualizado)
		{
			req.getSession().setAttribute("errorRegistro", "Yes");
			resp.sendRedirect("reg/home.jsp");
			//resp.sendRedirect("reg/modificarUsuario.jsp");
		}else{
			
			req.getSession().setAttribute("errorRegistro", "No");
			resp.sendRedirect("reg/home.jsp");
		}
		log.info(EditarParametroServlet.class+" Termino edición parametros");

	}
	

}
