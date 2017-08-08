package co.gov.ideamredd.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.reportes.AnalisisResultadosWPSBiomasa;
import co.gov.ideamredd.reportes.AnalisisResultadosWPSBosque;
import co.gov.ideamredd.reportes.AnalisisResultadosWPSCobertura;
import co.gov.ideamredd.reportes.AnalisisResultadosWPSDeforestacion;
import co.gov.ideamredd.util.Util;

public class GenerarReporteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	AnalisisResultadosWPSBosque arBosque;
	@EJB
	AnalisisResultadosWPSCobertura arCobertura;
	@EJB
	AnalisisResultadosWPSBiomasa arBiomasa;
	@EJB
	AnalisisResultadosWPSDeforestacion arDeforestacion;

	public GenerarReporteServlet() {
		super();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Integer treporte = Integer.valueOf(request.getParameter("treporte"));
		Integer divterritorial = Integer.valueOf(request
				.getParameter("divterritorio"));
		String periodo1 = request.getParameter("periodo1");
		String periodo2 = request.getParameter("periodo2");
		String nombreArchivo = "";
		Integer[] vperiodos = null;

		if (divterritorial > 3)
			divterritorial = 3;

		if (!periodo2.equals("-1")) {
			vperiodos = new Integer[2];
			vperiodos[0] = Integer.valueOf(periodo1);
			vperiodos[1] = Integer.valueOf(periodo2);
		} else {
			vperiodos = new Integer[1];
			vperiodos[0] = Integer.valueOf(periodo1);
		}
		String periodo = "";
		if (!periodo2.equals("-1"))
			periodo = periodo1 + periodo2;
		else
			periodo = periodo1;
		if (treporte == 1) {
			nombreArchivo = armarNombre("bnb", divterritorial, periodo1,
					periodo2, "fina");
			if (nombreArchivo != null) {
				arBosque.nuevoAnalisisResultadosWPSBosque(treporte,
						divterritorial, nombreArchivo, vperiodos);
				request.getSession().setAttribute(
						"mensaje",
						"SE HA GENERADO EL REPORTE DE BOSQUE/NO BOSQUE PARA EL PERIODO "
								+ periodo);
			} else {
				// LimpiarDatosSesion
				request.getSession().setAttribute(
						"mensaje",
						"NO SE HA GENERADO EL GEOPROCESO DE BOSQUE/NO BOSQUE PARA EL PERIODO "
								+ periodo);
			}

		} else if (treporte == 2) {
			nombreArchivo = armarNombre("bnb", divterritorial, periodo1,
					periodo2, "gruesa");
			if (nombreArchivo != null) {
				arBosque.nuevoAnalisisResultadosWPSBosque(treporte,
						divterritorial, nombreArchivo, vperiodos);
				request.getSession().setAttribute(
						"mensaje",
						"SE HA GENERADO EL REPORTE DE BOSQUE/NO BOSQUE PARA EL PERIODO "
								+ periodo);
			} else {
				// LimpiarDatosSesion
				request.getSession().setAttribute(
						"mensaje",
						"NO SE HA GENERADO EL GEOPROCESO DE BOSQUE/NO BOSQUE PARA EL PERIODO "
								+ periodo);
			}
		} else if (treporte == 3) {
			nombreArchivo = armarNombre("cambio", divterritorial, periodo1,
					periodo2, "fina");
			if (nombreArchivo != null) {
				arCobertura.nuevoAnalisisResultadosWPSCobertura(treporte,
						divterritorial, nombreArchivo, vperiodos);
				request.getSession().setAttribute(
						"mensaje",
						"SE HA GENERADO EL REPORTE DE CAMBIO DE COBERTURA BOSCOSA PARA EL PERIODO "
								+ periodo);
			} else {
				// LimpiarDatosSesion
				request.getSession()
						.setAttribute(
								"mensaje",
								"NO SE HA GENERADO EL GEOPROCESO DE CAMBIO DE COBERTURA BOSCOSA PARA EL PERIODO "
										+ periodo);
			}
		} else if (treporte == 4) {
			nombreArchivo = armarNombre("cambio", divterritorial, periodo1,
					periodo2, "gruesa");
			if (nombreArchivo != null) {
				arCobertura.nuevoAnalisisResultadosWPSCobertura(treporte,
						divterritorial, nombreArchivo, vperiodos);
				request.getSession().setAttribute(
						"mensaje",
						"SE HA GENERADO EL REPORTE DE CAMBIO DE COBERTURA BOSCOSA PARA EL PERIODO "
								+ periodo);
			} else {
				// LimpiarDatosSesion
				request.getSession()
						.setAttribute(
								"mensaje",
								"NO SE HA GENERADO EL GEOPROCESO DE CAMBIO DE COBERTURA BOSCOSA PARA EL PERIODO "
										+ periodo);
			}
		} else if (treporte == 5) {
			nombreArchivo = armarNombre("deforestacion", divterritorial,
					periodo1, periodo2, "fina");
			if (nombreArchivo != null) {
				arDeforestacion.nuevoAnalisisResultadosWPSDeforestacion(treporte,
						divterritorial, nombreArchivo, vperiodos);
				request.getSession().setAttribute(
						"mensaje",
						"SE HA GENERADO EL REPORTE DE DEFORESTACION PARA EL PERIODO "
								+ periodo);
			} else {
				// LimpiarDatosSesion
				request.getSession().setAttribute(
						"mensaje",
						"NO SE HA GENERADO EL GEOPROCESO DE DEFRORESTACION PARA EL PERIODO "
								+ periodo);
			}
		} else if (treporte == 6) {
			nombreArchivo = armarNombre("regeneracion", divterritorial,
					periodo1, periodo2, "gruesa");
			if (nombreArchivo != null) {
				arDeforestacion.nuevoAnalisisResultadosWPSDeforestacion(treporte,
						divterritorial, nombreArchivo, vperiodos);
				request.getSession().setAttribute(
						"mensaje",
						"SE HA GENERADO EL REPORTE DE DEFORESTACION PARA EL PERIODO "
								+ periodo);
			} else {
				// LimpiarDatosSesion
				request.getSession().setAttribute(
						"mensaje",
						"NO SE HA GENERADO EL GEOPROCESO DE REGENERACION PARA EL PERIODO "
								+ periodo);
			}
		} else if (treporte == 8) {
			nombreArchivo = armarNombre("Biomasa", divterritorial, periodo1,
					periodo2, "fina");
			if (nombreArchivo != null) {
				arBiomasa.nuevoAnalisisResultadosWPSBiomasa(treporte,
						divterritorial, nombreArchivo, vperiodos[0]);
				request.getSession().setAttribute(
						"mensaje",
						"SE HA GENERADO EL REPORTE DE BIOMASA PARA EL PERIODO "
								+ periodo);
			} else {
				// LimpiarDatosSesion
				request.getSession().setAttribute(
						"mensaje",
						"NO SE HA GENERADO EL GEOPROCESO DE BIOMASA PARA EL PERIODO "
								+ periodo);
			}
		}
		response.sendRedirect("limpiardatossesion");
	}

	private String armarNombre(String treporte, Integer divterritorial,
			String periodo1, String periodo2, String resolucion) {
		String division = "";
		String periodo = "";
		String nombre = "";
		if (divterritorial == 1) {
			division = "car";
		} else if (divterritorial == 2) {
			division = "depto";
		} else if (divterritorial == 3) {
			division = "area";
		} else {
			division = "consolidado";
		}
		if (!periodo2.equals("-1"))
			periodo = periodo1 + periodo2;
		else
			periodo = periodo1;
		try {
			nombre = Util.obtenerNombreArchivo(treporte, division, periodo,
					resolucion);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nombre;
	}

}
