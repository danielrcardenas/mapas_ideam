// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.descarga.servlets;

import co.gov.ideamredd.mbc.auxiliares.*;

/**
 * @author Santiago Hernández Plazas
 * @since 2016-07-15
 * 
 * Clase para ejecutar el comando de envío de reportes de descargas
 *
 */
public class EnviarReporteDescarga {

	/**
	 * Metodo para enviar el reporte
	 */
	public static String enviar() {
		String r = "";
		
		try {
			String commando = "java -jar /opt/SMBC/DescargasAutomaticas/DescargasAutomaticas.jar";
			String resultado_generacion = Auxiliar.commander(commando, commando);
			String [] a_resultado_generacion = resultado_generacion.split("-=-");		
			r = "<h4>Resultado/Result:</h4>";

			if (a_resultado_generacion[1].indexOf("Exception") >= 0) {
				r += Auxiliar.mensajeImpersonal("error", "error:");
			}
			else {
				r += Auxiliar.mensajeImpersonal("confirmacion", "éxito/success:");				
			}
			r += "<div class='div_resultado_commando'>" + a_resultado_generacion[1] + "</div>";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return r;
	}

}