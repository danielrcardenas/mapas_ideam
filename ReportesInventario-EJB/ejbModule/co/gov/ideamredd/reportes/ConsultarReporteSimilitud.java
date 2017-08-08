package co.gov.ideamredd.reportes;

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
public class ConsultarReporteSimilitud {

	IndividuoDAO individuoDAO = new IndividuoDAO();

	@EJB
	private GeneradorReporteEstCompPDF generadorPDF;
	@EJB
	private GeneradorReporteEstCompExcel generadorXLS;

	DecimalFormat format = new DecimalFormat("0.0000");

	public String generarTablasHTML(InformacionReporteInventarios filtro1,
			InformacionReporteInventarios filtro2) throws Exception {

		RserverConexion r = RserverConexion.getConnector();
		String token = r.connect();
		System.out.println("Inicia proceso R: "+ token );
		try {
			//Esperar mientrar se desocupa servidor R
			int count = 0;
			while(token != r.currentToken()){
				try {
				    Thread.sleep(10000);
				    count++;
					System.out.println("Reintentando proceso R: "+ token );
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
				if (count >=10){
					r.closeConnection(token);
					return "<h3> El servidor R no está disponible";
				}
			}
			// Comprobar conexi�n a R
			if (token == r.currentToken()) {
				System.out.println("ejecuta proceso R: "+ token );

				r.setWorkspace(token, filtro1.getrScriptPath());

				filtro1.setResultName(token + "_1.csv");
				filtro2.setResultName(token + "_2.csv");

				List<InformacionReporteInventarios> result1 = this.getResults(
						filtro1, true);
				List<InformacionReporteInventarios> result2 = this.getResults(
						filtro2, true);
				System.out.println("Filtro 1: " +result1.size());
				System.out.println("Filtro 2: " +result2.size());
				if (result1.isEmpty()) {
					r.closeConnection(token);
					return "<h3>No hay datos asociados al filtro 1</h3>";
				}
				if (result2.isEmpty()) {
					r.closeConnection(token);
					return "<h3>No hay datos asociados al filtro 2</h3>";
				}

				// Cargar las funciones definidas en R
				r.ejecutarScript("script.r", token);
				//r.ejecutarScript("/etc/SMBC/ReportesInventario/scripts/script.r", token);
				// //////// Inicio Filtro 1 ///////////////////////////

				// Cargar datos desde CSV
				r.ejecutar(
						token,
						"data1<-read.csv2(\""
								+ filtro1.getResultName()
								+ "\",enc=\"latin1\", header=TRUE, sep=\";\", dec=\".\")");
				// Indicar la columna con tipos de especie
				r.ejecutar(token, "esp1<-data1[,2]");
				// ///////////// FIN Filtro 1//////////////////
				// //////// Inicio Filtro 2 ///////////////////////////

				// Cargar datos desde CSV
				r.ejecutar(
						token,
						"data2<-read.csv2(\""
								+ filtro2.getResultName()
								+ "\",enc=\"latin1\", header=TRUE, sep=\";\", dec=\".\")");
				// Indicar la columna con tipos de especie
				r.ejecutar(token, "esp2<-data2[,2]");
				// ///////////// FIN Filtro 2//////////////////

				REXP exp = r.ejecutar(token, "div_beta(esp1, esp2)");
				double diversidadBetta = exp.asDouble();
				exp = r.ejecutar(token, "ind_sorensen(esp1, esp2)");
				double indiceSorensen = exp.asDouble();

				r.closeConnection(token);
				String html = generarHTML(indiceSorensen, diversidadBetta);
				/*
				 * List<InformacionReporteInventarios> resultados =
				 * crearResultados( especies, frecuenciaRelativa,
				 * abundanciaRelativa, dominancia, ivi); String html =
				 * generarHTML(resultados);
				 * generadorPDF.generarReporte(resultados,
				 * filtro.getTemplatePath() + File.separator);
				 * generadorXLS.generarReporte(resultados,
				 * filtro.getTemplatePath() + File.separator);
				 */
				System.out.println("FInaliza proceso R: "+ token );

				return html;

			}
		} catch (Exception e) {
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
		System.out.println("Sale proceso R: "+ token );
		r.closeConnection(token);
		return "<h3>El servidor estadisitio R esta ocupado, intente mas tarde</h3>";
	}

	protected List<InformacionReporteInventarios> getResults(
			InformacionReporteInventarios filtro, boolean save) {

		if (!filtro.getEspecie().isEmpty()) {
			return individuoDAO.consultarEspecieReporte(filtro, save);
		}
		if (!filtro.getParcela().isEmpty()) {
			return individuoDAO.consultarEspeciePorParcela(filtro, save);
		}
		return individuoDAO.consultarReporteEstructuraComposicion(filtro, save);
	}

	protected List<InformacionReporteInventarios> builtResults(
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

	private String generarHTML(double indSorensen, double divBeta) {
		String html = "<div class='reporte_tabla'>";
		html += "<h3> Indicadores de similitud floristica</h3>";
		html += "<table><tr>";
		html += "<th>Diversidad Beta</th>";
		html += "<th>Indice de Sorensen</th>";
		html += "</tr>";

		html += "<tr class='negrilla'>";

		html += "<td>" + format.format(divBeta) + "</td>";
		html += "<td>" + format.format(indSorensen) + "</td>";
		html += "</tr>";

		html += "</table>";
		html += "</div><style>.reporte_tabla table, th, td {border: 1px solid black;border-collapse: collapse; padding: 5px; text-align: center;} .negrilla{font-weight: bold; background-color: #EEE;}</style>";
		return html;
	}

}
