package co.gov.ideamredd.reportes;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.gov.ideamredd.dao.IndividuoDAO;
import co.gov.ideamredd.entities.InformacionReporteInventarios;
/**
 * Clase principal para generar los reportes de inventario
 * 
 * 
 * @author Daniel Rodríguez Cárdenas
 *
 */
@Stateless
public class ConsultarReporteInventario {

	IndividuoDAO individuoDAO = new IndividuoDAO();

	@EJB
	private GeneradorReportePDF generadorPDF;
	@EJB
	private GeneradorReporteExcel generadorXLS;

	DecimalFormat format = new DecimalFormat("0.0000");


/**
 * Genera una tabla en formato HTML con el resultado del filtro de individuos 
 * @param filtro Filtro que comprende los parámetros para la busqueda de los individuos
 * @return Texto con el HTML a inyectar
 * @throws Exception
 */
	public String generarTablasHTML(InformacionReporteInventarios filtro)
			throws Exception {

		List<InformacionReporteInventarios> resultado = individuoDAO
				.consultarEspecieReporte(filtro, false);
		Map<String, List<InformacionReporteInventarios>> mapaResultados = extraerResultados(resultado);
		generadorPDF.generarReporte(mapaResultados, filtro.getTemplatePath()
				+ File.separator);
		generadorXLS.generarReporte(mapaResultados, filtro.getTemplatePath()
				+ File.separator);
		if(mapaResultados.isEmpty()){
			return "<h3>No hay datos asociados a este filtro</h3> " ;
		}else{
			return generarHTML(mapaResultados);
		}
		

	}

	private Map<String, List<InformacionReporteInventarios>> extraerResultados(
			List<InformacionReporteInventarios> resultado) {
		Map<String, List<InformacionReporteInventarios>> mapa = new HashMap<String, List<InformacionReporteInventarios>>();

		for (InformacionReporteInventarios informacionReporteInventarios : resultado) {
			String especie = informacionReporteInventarios.getEspecie();
			if (mapa.containsKey(especie)) {
				mapa.get(especie).add(informacionReporteInventarios);
			} else {
				List<InformacionReporteInventarios> listaPorEspecie = new ArrayList<InformacionReporteInventarios>();
				listaPorEspecie.add(informacionReporteInventarios);
				mapa.put(especie, listaPorEspecie);
			}
		}
		return mapa;
	}

	private String generarHTML(
			Map<String, List<InformacionReporteInventarios>> mapaResultados) {
		System.out.println("Generando resultados HTML");
		String html = "<div class='reporte_tabla'>";
		for (String especie : mapaResultados.keySet()) {
			html += generarTabla(especie, mapaResultados.get(especie));
			html += "<br></br>";
		}
		html += "</div><style>.reporte_tabla table, th, td {border: 1px solid black;border-collapse: collapse; padding: 5px; text-align: center;} .negrilla{font-weight: bold; background-color: #EEE;}</style>";
		return html ;
	}

	private String generarTabla(String especie,
			List<InformacionReporteInventarios> list) {

		BigDecimal biomasaTotal = BigDecimal.ZERO;
		BigDecimal carbonoTotal = BigDecimal.ZERO;
		BigDecimal areaTotal = BigDecimal.ZERO;

		BigDecimal cantidadRegistros = new BigDecimal(list.size());

		String html = "<h3>" + especie + "</h3>";
		html += "<table><tr>";
		html += "<th>N\u00FAmero de \u00E1rbol</th>";
		html += "<th>G\u00E9nero</th>";
		html += "<th>Familia</th>";
		html += "<th>Biomasa a\u00E9rea</th>";
		html += "<th>Carbono</th>";
		html += "<th>\u00C1rea basal</th>";
		html += "</tr>";

		for (InformacionReporteInventarios registro : list) {
			html += "<tr>";
			html += "<td>" + registro.getNumeroArbol() + "</td>";
			html += "<td>" + registro.getGenero() + "</td>";
			html += "<td>" + registro.getFamilia() + "</td>";
			html += "<td>" + format.format(registro.getBiomasa()) + "</td>";
			html += "<td>" + format.format(registro.getCarbono()) + "</td>";
			html += "<td>" + format.format(registro.getAreaBasal()) + "</td>";
			html += "</tr>";

			biomasaTotal = biomasaTotal.add(registro.getBiomasa());
			carbonoTotal = carbonoTotal.add(registro.getCarbono());
			areaTotal = areaTotal.add(registro.getAreaBasal());
		}

		html += "<tr class='negrilla'>";
		html += "<td>Total</td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td>" + format.format(biomasaTotal) + "</td>";
		html += "<td>" + format.format(carbonoTotal) + "</td>";
		html += "<td>--</td>";
		html += "</tr>";

		html += "<tr class='negrilla'>";
		html += "<td>Promedio</td>";
		html += "<td></td>";
		html += "<td></td>";
		html += "<td>"
				+ format.format(biomasaTotal.divide(cantidadRegistros,
						RoundingMode.HALF_EVEN)) + "</td>";
		html += "<td>"
				+ format.format(carbonoTotal.divide(cantidadRegistros,
						RoundingMode.HALF_EVEN)) + "</td>";
		html += "<td>"
				+ format.format(areaTotal.divide(cantidadRegistros,
						RoundingMode.HALF_EVEN)) + "</td>";
		html += "</tr>";

		html += "</table>";
		return html;
	}

}
