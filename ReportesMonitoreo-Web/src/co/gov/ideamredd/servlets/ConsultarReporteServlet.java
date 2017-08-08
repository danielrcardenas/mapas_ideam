package co.gov.ideamredd.servlets;

import java.io.IOException;


import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.reportes.ConsultarReporteBiomasa;
import co.gov.ideamredd.reportes.ConsultarReporteBosqueNoBosque;
import co.gov.ideamredd.reportes.ConsultarReporteCobertura;
import co.gov.ideamredd.reportes.CrearLibroReporteDeforestacion;
import co.gov.ideamredd.reportes.LecturaArchivo;

public class ConsultarReporteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@EJB
	private ConsultarReporteBosqueNoBosque crbnb;
	@EJB
	private ConsultarReporteBiomasa crb;
	@EJB
	private ConsultarReporteCobertura crc;
	@EJB
	private CrearLibroReporteDeforestacion clrd;

	private String tipoGrafica, tipoDivision;

	/*
	 * public ConsultarReporteServlet() { super(); }
	 */

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
 
		Integer treporte = Integer.valueOf(request.getParameter("treporte"));
		Integer divterritorial = Integer.valueOf(request
				.getParameter("divterritorio"));
		Integer PDFdivterritorial = Integer.valueOf(request
				.getParameter("divterritorio"));
		String periodo = request.getParameter("periodo1");
		String[] periodos =null;
		try{
		periodos = request.getParameter("periodoFin").substring(1).split(",");
		}catch(Exception e){
			periodos=null;
		}
		String index = request.getParameter("i");
		String reporte[] = new String[3];
		Integer[] idReportes = null;
		Integer variosReport = null;
		tipoGrafica = "";

		if (periodos != null) {
			variosReport = 1;

			String periodosAux = "";
			for (int c = 0; c < periodos.length; c++) {
				periodosAux = periodosAux + periodos[c] + ",";
			}

			request.getSession().setAttribute("PDFperiodos", periodosAux);

			idReportes = new Integer[periodos.length];

			int count = 0;
			for (String tmp : periodos) {
				idReportes[count] = Integer.valueOf(tmp.substring(0,
						tmp.indexOf("-")));
				periodos[count++] = tmp.substring(tmp.indexOf("-") + 1);
			}
		} else {
			variosReport = 0;
			request.getSession().setAttribute("PDFperiodo", periodo);
			periodos = new String[1];
			idReportes = new Integer[1];
			idReportes[0] = Integer.valueOf(periodo.substring(0,
					periodo.indexOf("-")));
			periodos[0] = periodo.substring(periodo.indexOf("-") + 1);
		}

		if (treporte < 3) {

			switch (divterritorial) {
			case 1:
				tipoDivision = "CARs";
				break;
			case 2:
				tipoDivision = "Departamentos";
				break;
			case 3:
				tipoDivision = "AreasHidrograficas";
				break;
			case 4:
				tipoDivision = "ConsolidadoNacional";
				break;
			}

			crbnb.consultarReporte(divterritorial, periodos, idReportes);
			// request.getSession().setAttribute("rutaArchivo",
			// c.getRutaArchivo());
			reporte[0] = crbnb.getRutaArchivo();
			reporte[1] = LecturaArchivo.getTablaReporteBNB(LecturaArchivo
					.lecturaArchivo(crbnb.getRutaArchivo()));

			tipoGrafica = "Bosque";
			request.getSession().setAttribute("listaDatosWeb",
					crbnb.getListaDatosWeb());
			request.getSession().setAttribute("listaDatosConsolidadoWeb",
					crbnb.getListaDatosWebConsolidado());
			request.getSession().setAttribute("nombresCARs",
					crbnb.getNombresCARs());
			request.getSession().setAttribute("nombresDepartamentos",
					crbnb.getNombresDepartamentos());
			request.getSession().setAttribute("nombresAreasHidro",
					crbnb.getNombresAreasHidrog());
			request.getSession().setAttribute("PeriodosBosque",
					crbnb.getPeriodos());

		} else if (treporte > 4 && treporte < 7) {
			if (treporte == 5) {
				if (divterritorial == 3) {
					tipoDivision = "AreasHidrograficas";
					divterritorial = 5;
				} else if (divterritorial == 4) {
					tipoDivision = "ConsolidadoNacional";
					divterritorial = 4;
				}
			} else {
				if (divterritorial == 3) {
					tipoDivision = "AreasHidrograficas";
					divterritorial = 7;
				} else if (divterritorial == 4) {
					tipoDivision = "ConsolidadoNacional";
					divterritorial = 6;
				}
			}
			try {
				clrd.construirLibroReporte(idReportes[0], divterritorial,
						periodos[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// request.getSession().setAttribute("rutaArchivo",
			// c3.getRutaArchivo());
			reporte[0] = clrd.getRutaArchivo();
			reporte[1] = LecturaArchivo
					.getTablaReporteDeforestacion(LecturaArchivo
							.lecturaArchivo(clrd.getRutaArchivo()));

			// Datos necesarios para las graficas en la pagina de resultados
			tipoGrafica = "Deforestacion";
			request.getSession().setAttribute("listaDatosWeb",
					clrd.getListaDatosWeb());
			request.getSession().setAttribute("listaDatosConsolidadoWeb",
					clrd.getListaDatosConsolidadoWeb());
			request.getSession().setAttribute("nombresAreasHidro",
					clrd.getNombresAreasHidrog());
			request.getSession().setAttribute("nombresTiposBosque",
					clrd.getNombresTiposBosque());
			request.getSession().setAttribute("PeriodoBiomasa",
					clrd.getPeriodo());

		} else if (treporte == 7) {
			if (divterritorial == 3) {
				divterritorial = 8;
				tipoDivision = "AreasHidrograficas";
			} else if (divterritorial == 4) {
				divterritorial = 7;
				tipoDivision = "ConsolidadoNacional";
			}
			crb.consultarReporte(divterritorial, periodos, idReportes);
			reporte[0] = crb.getRutaArchivo();
			reporte[1] = LecturaArchivo
					.getTablaReporteBiomasa(LecturaArchivo
							.lecturaArchivo(crb.getRutaArchivo()));

			// Datos necesarios para las graficas en la pagina de resultados
			tipoGrafica = "Biomasa";
			request.getSession().setAttribute("listaDatosWeb",
					crb.getListaDatosWeb());
			request.getSession().setAttribute("listaDatosConsolidadoWeb",
					crb.getListaDatosConsolidadoWeb());
			request.getSession().setAttribute("nombresAreasHidro",
					crb.getNombresAreasHidrog());
			request.getSession().setAttribute("nombresTiposBosque",
					crb.getNombresTiposBosque());
			request.getSession().setAttribute("PeriodoBiomasa",
					crb.getPeriodo());

		} else {
			switch (divterritorial) {
			case 1:
				tipoDivision = "CARs";
				break;
			case 2:
				tipoDivision = "Departamentos";
				break;
			case 3:
				tipoDivision = "AreasHidrograficas";
				break;
			case 4:
				tipoDivision = "ConsolidadoNacional";
				break;
			}
			crc.consultarReporte(divterritorial, periodos, idReportes);
			// request.getSession().setAttribute("rutaArchivo",
			// c1.getRutaArchivo());
			reporte[0] = crc.getRutaArchivo();
			reporte[1] = LecturaArchivo.getTablaReporteCobertura(LecturaArchivo
					.lecturaArchivo(crc.getRutaArchivo()));

			tipoGrafica = "Cobertura";
			request.getSession().setAttribute("listaDatosWeb",
					crc.getListaDatosWeb());
			request.getSession().setAttribute("listaDatosConsolidadoWeb",
					crc.getListaDatosWebConsolidado());
			request.getSession().setAttribute("nombresCARs",
					crc.getNombresCARs());
			request.getSession().setAttribute("nombresDepartamentos",
					crc.getNombresDepartamentos());
			request.getSession().setAttribute("nombresAreasHidro",
					crc.getNombresAreasHidrog());
			request.getSession().setAttribute("PeriodosBosque",
					crc.getPeriodos());
		}
		reporte[2] = index;
		if (!reporte.equals(""))
			request.getSession().setAttribute("reporte", reporte);
		else
			request.getSession().setAttribute("reporte",
					"<th>NO FUE POSIBLE GENERAR EL REPORTE</th>");

		request.getSession().setAttribute("tipoGrafica", tipoGrafica);
		request.getSession().setAttribute("tipoDivision", tipoDivision);
		request.getSession().setAttribute("PDFdivision", PDFdivterritorial);
		request.getSession().setAttribute("PDFtiporeporte", treporte);
		request.getSession().setAttribute("variosPeriodos", variosReport);

		if (request.getParameter("esPublico") != null)
			response.sendRedirect("pub/verReportes.jsp?esPublico=1");
		else
			response.sendRedirect("pub/verReportes.jsp");
		// response.sendRedirect("verConsultaReporte.jsp");
	}

}
