package co.gov.ideamredd.reportes;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.rosuda.REngine.REXP;

import co.gov.ideamredd.dao.IndividuoDAO;
import co.gov.ideamredd.entities.InformacionReporteInventarios;
import co.gov.ideamredd.rserver.conexion.RserverConexion;

@Stateless
public class ConsultarReporteInventarioEstComp {

	IndividuoDAO individuoDAO = new IndividuoDAO();

	@EJB
	private GeneradorReporteEstCompPDF generadorPDF;
	@EJB
	private GeneradorReporteEstCompExcel generadorXLS;

	DecimalFormat format = new DecimalFormat("0.0000");

	private double diversidadGamma;
	private double indiceShannon;
	private double indiceSimpson;

	public String generarTablasHTML(InformacionReporteInventarios filtro)
			throws Exception {

		// List<InformacionReporteInventarios> resultado =
		// individuoDAO.consultarReporteEstructuraComposicion(filtro);

		RserverConexion r = RserverConexion.getConnector();
		String token = r.connect();
		try {
			//Esperar mientrar se desocupa servidor R
			int count = 0;
			while(token != r.currentToken()){
				try {
				    Thread.sleep(10000);
				    count++;
				    System.out.println("Esperando proceso R: " + token);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
				if (count >=10){
					r.closeConnection(token);
					return "<h3> El servidor R no está disponible";
				}
			}
			if (token == r.currentToken()) {
			    System.out.println("Inicia proceso R: " + token);

				r.setWorkspace(token, filtro.getrScriptPath());
				
				filtro.setResultName(token + ".csv");
				List<InformacionReporteInventarios> individuos = individuoDAO
						.consultarReporteEstructuraComposicion(filtro, true);
				if (individuos.isEmpty()) {
					r.closeConnection(token);
					return "<h3>No hay datos asociados a este filtro</h3>";
				}
				// Cargar las funciones definidas en R
				//r.ejecutarScript("script.r", token);
				r.ejecutarScript("script.r", token);
				// Cargar datos desde CSV
				r.ejecutar(
						token,
						"data<-read.csv2(\""
								+ token
								+ ".csv\",enc=\"latin1\", header=TRUE, sep=\";\", dec=\".\")");
				// Indicar la columna con tipos de especie
				r.ejecutar(token, "esp<-data[,2]");
				REXP exp = r.ejecutar(token, "frec_relativa(esp)");
				double frecuenciaRelativa[] = exp.asDoubles();
				exp = r.ejecutar(token, "data.frame(table(esp))$esp");
				String[] especies = exp.asStrings();
				exp = r.ejecutar(token, "abun_relativa(esp)");
				double abundanciaRelativa[] = exp.asDoubles();
				r.ejecutar(token, "DAP<-data[,7]");
				exp = r.ejecutar(token, "domi_relativa(DAP)");
				double dominancia[] = exp.asDoubles();

				exp = r.ejecutar(token, "ivi(esp,DAP)");
				double ivi[] = exp.asDoubles();
				exp = r.ejecutar(token, "div_gamma(esp)");
				diversidadGamma = exp.asDouble();
				exp = r.ejecutar(token, "indice_shannon_weaver(esp)");
				indiceShannon = exp.asDouble();
				exp = r.ejecutar(token, "indice_simpson(esp)");
				indiceSimpson = exp.asDouble();
				r.closeConnection(token);
				List<InformacionReporteInventarios> resultados = crearResultados(
						especies, frecuenciaRelativa, abundanciaRelativa,
						dominancia, ivi);
				String html = generarHTML(resultados);
				generadorPDF.generarReporte(resultados,
						filtro.getTemplatePath() + File.separator, indiceShannon,indiceSimpson,diversidadGamma);
				generadorXLS.generarReporte(resultados,
						filtro.getTemplatePath() + File.separator, indiceShannon,indiceSimpson,diversidadGamma);
			    System.out.println("Termina proceso R: " + token);


				return html;
			}
		} catch (Exception e) {
		    System.out.println("No se puedo ejecutar proceso R: " + token);
			r.closeConnection(token);
		} finally {
			r.closeConnection(token);
			
		}

		/*
		 * Map<String, List<InformacionReporteInventarios>> mapaResultados =
		 * extraerResultados(resultado);
		 * 
		 * generadorXLS.generarReporte(mapaResultados); String html =
		 * generarHTML(mapaResultados);
		 */
		r.closeConnection(token);
	    System.out.println("Esperando proceso R: " + token);
		return "<h3>El servidor estadisitio R esta ocupado, intente mas tarde</h3>";
	}

	protected List<InformacionReporteInventarios> crearResultados(
			String[] especies, double frecuenciaRelativa[],
			double abundanciaRelativa[], double dominancia[], double ivi[]) {
		List<InformacionReporteInventarios> resultados = new ArrayList<InformacionReporteInventarios>();
		for (int i = 0; i < especies.length; i++) {
			InformacionReporteInventarios registro = new InformacionReporteInventarios();
			registro.setEspecie(especies[i]);
			registro.setFrecuenciaRelativa(frecuenciaRelativa[i]);
			registro.setAbundanciaRelativa(abundanciaRelativa[i]);
			registro.setDominancia(dominancia[i]);
			registro.setIvi(ivi[i]);
			resultados.add(registro);

		}
		return resultados;
	}

	private String generarHTML(List<InformacionReporteInventarios> resultados) {
		String html = "<div class='reporte_tabla'>";
		html += "<h3> Reporte de Estructura y Composici&oacute;n</h3>";
		html += "<table><tr>";
		html += "<th>Especie</th>";
		html += "<th>Frecuencia Relativa</th>";
		html += "<th>Abundancia Relativa</th>";
		html += "<th>Dominancia</th>";
		html += "<th>IVI</th>";
		html += "</tr>";
		for (InformacionReporteInventarios resultado : resultados) {
			html += generarFilaHTML(resultado);

		}

		html += "<tr class='negrilla'>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td> Diversidad Gamma:</td>";
		html += "<td>" + format.format(diversidadGamma) + "</td>";
		html += "</tr>";

		html += "<tr class='negrilla'>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td> Indice Shannon Weaver:</td>";
		html += "<td>" + format.format(indiceShannon) + "</td>";
		html += "</tr>";

		html += "<tr class='negrilla'>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td> Indice Simpson:</td>";
		html += "<td>" + format.format(indiceSimpson) + "</td>";
		html += "</tr>";

		html += "</table>";
		html += "</div><style>.reporte_tabla table, th, td {border: 1px solid black;border-collapse: collapse; padding: 5px; text-align: center;} .negrilla{font-weight: bold; background-color: #EEE;}</style>";
		return html;
	}

	private String generarFilaHTML(InformacionReporteInventarios resultado) {
		String html = "<tr>";
		html += "<td>" + resultado.getEspecie() + "</td>";
		html += "<td>" + format.format(resultado.getFrecuenciaRelativa())
				+ "</td>";
		html += "<td>" + format.format(resultado.getAbundanciaRelativa())
				+ "</td>";
		html += "<td>" + format.format(resultado.getDominancia()) + "</td>";
		html += "<td>" + format.format(resultado.getIvi()) + "</td>";

		html += "</tr>";
		return html;
	}

}
