package co.gov.ideamredd.servlets;

import java.io.IOException;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.dao.RegistrarBiomasa;
import co.gov.ideamredd.entities.BiomasaYCarbono;

public class RegistrarBiomasaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private RegistrarBiomasa registrarBiomasa;

	private String opcion;
	private String biomasa;
	private String fechaGeneracion;
	private String metodologia;
	private String nombre;
	private String descripcion;
	private String fechaIni;
	private String fechaFin;
	private String ecuacion;
	private String parcela;
	private String atipicos;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		opcion = request.getParameter("metodo");
		biomasa = request.getParameter("biomasa");
		fechaGeneracion = request.getParameter("fInicial");
		metodologia = request.getParameter("metodologia");
		nombre = request.getParameter("nombre");
		descripcion = request.getParameter("descripcion");
		ecuacion = request.getParameter("ecuacion");
		fechaIni = request.getParameter("fIni");
		fechaFin = request.getParameter("ffin");
		parcela = request.getParameter("idParcela");
		atipicos = request.getParameter("atipicos");
		
		if(fechaGeneracion.equals("")){
			String fi[] = fechaIni.split("/");
			String ff[] = fechaFin.split("/");
			fechaIni = fi[1]+"/"+fi[0]+"/"+fi[2];
			fechaFin = ff[1]+"/"+ff[0]+"/"+ff[2];
		}else{
			String[] fg = fechaGeneracion.split("/");
			fechaGeneracion = fg[1]+"/"+fg[0]+"/"+fg[2];
		}

		registrarBiomasa.setIdParcela(Integer.valueOf(parcela));
//		registrarBiomasa.setCodigoCampo(CargaDatosSelect
//				.getCodigoCampoParcelas(idParcelas));
		if (Integer.valueOf(opcion) == 1) {
			registrarBiomasa.setOpcionGenaracion(Integer.valueOf(opcion));
			registrarBiomasa.setBio(Double.valueOf(biomasa));
			registrarBiomasa.setFechaInicio(fechaGeneracion);
			registrarBiomasa.setMetodologia(Integer.valueOf(metodologia));
			if (Integer.valueOf(metodologia) != 1) {
				registrarBiomasa.setNombreMetodologia(nombre);
				registrarBiomasa.setDescripcionMetodologia(descripcion);
				registrarBiomasa.setEcuacion(ecuacion);
			}
		} else {
			registrarBiomasa.setOpcionGenaracion(Integer.valueOf(opcion));
			registrarBiomasa.setFechaInicio(fechaIni);
			//registrarBiomasa.setAtipicos(Integer.valueOf(atipicos));
		}

		registrarBiomasa.registrarBiomasa();
//		request.getSession().setAttribute("mensaje",
//				"DATO DE BIOMASA REGISTRADO EXITOSAMENTE");

//		response.sendRedirect("limpiardatossesion");

	}
}
