package co.gov.ideamredd.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.entities.InventarioSuelo;
import co.gov.ideamredd.inventarioSuelo.dao.ConsultaInventarioSuelo;

/**
 * ConsultaInventarioSueloServlet.java Servlet que usa el bean de consulta de
 * datos
 * 
 * SMBC Proyecto: Sistema de Monitoreo de Bosques y Carbono
 * 
 * @author Julio Cesar Sanchez Torres Oct 18 de 2013
 */

public class ConsultaInventarioSueloServlet extends HttpServlet {

	@EJB
	ConsultaInventarioSuelo cis;
	
	private String parcelaID;
	private String auxParcelaID;
	private char auxChar;
	private ArrayList<InventarioSuelo> invSuelos;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		parcelaID = request.getParameter("parcelasIDs");
		auxParcelaID = "";
		
		cis.clearinvSueloLista();
		for (int i = 0; i < parcelaID.length(); i++) {
			auxChar = parcelaID.charAt(i);
			if (auxChar == ',') {
				InventarioSuelo invSuelo = new InventarioSuelo();
				cis.consultaNombreParcela(Integer.parseInt(auxParcelaID),
						invSuelo);
				cis.consultaMunicipioP(Integer.parseInt(auxParcelaID), invSuelo);
				cis.consultaDepartamentoP(Integer.parseInt(auxParcelaID),
						invSuelo);
				cis.consultaDatosMetodologia(Integer.parseInt(auxParcelaID),
						invSuelo);
				cis.consultaDatosContacto(Integer.parseInt(auxParcelaID),
						invSuelo);
				cis.consultaInventSuelosP(Integer.parseInt(auxParcelaID),
						invSuelo);
				auxParcelaID = "";
			} else {
				auxParcelaID = auxParcelaID + auxChar;
			}
		}

		invSuelos = cis.getListaInvSuelos();
		request.getSession().setAttribute("invSuelos", invSuelos);
		response.sendRedirect("reg/verConsultaInventarioSuelos.jsp");
	}

}
