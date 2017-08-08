// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.parametrizacion.servlets;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import co.gov.ideamredd.parametrizacion.dao.ActualizarParametroParametro;

public class EditarParametroServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	ActualizarParametroParametro actualizarParametro;
	private static Logger log = Logger.getLogger("SMBCLog");
	private Integer numRutas;
	private String ruta;
	private Integer idParametro;
	boolean actualizado;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

		try {
			log.info(EditarParametroServlet.class
					+ " Inicio edici�n parametros");
			numRutas = Integer.parseInt(req.getParameter("numRutas"));
			actualizado = true;
			for (int i = 0; i < numRutas && actualizado == true; i++) {
				actualizado = false;
				if (i == 0) {
					ruta = req.getParameter("textRuta" + i);
					actualizado = actualizarParametro.editarRutaMetadato(
							actualizarParametro.getRutaProviders(), ruta);
					// editar la ruta del metadato
				} else if (i == 1) {
					ruta = req.getParameter("textRuta" + i);
					actualizado = actualizarParametro.editarRutaThumbnail(
							actualizarParametro.getRutaProviders(), ruta);
					// editar la ruta del thumbnail
				} else {
					ruta = req.getParameter("textRuta" + i);
					// se decrementa en uno debido a que el indice de la base de
					// datos comienza en 1 y el textRuta del primer campo
					// corresponde a 2
					idParametro = i - 1;
					actualizarParametro.setUna_ruta(ruta);
					actualizarParametro.setIdParametro(idParametro);
					actualizado = actualizarParametro.modificarParametro();
				}

			}
			if (!actualizado) {
				req.getSession().setAttribute("errorRegistro", "Yes");
				resp.sendRedirect("parametrizacion/editarRutasConfiguracion.jsp");
				// resp.sendRedirect("reg/modificarUsuario.jsp");
			} else {
				req.getSession().setAttribute("errorRegistro", "No");
				resp.sendRedirect("parametrizacion/editarRutasConfiguracion.jsp");
			}
			log.info(EditarParametroServlet.class
					+ " Termino edici�n parametros");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
