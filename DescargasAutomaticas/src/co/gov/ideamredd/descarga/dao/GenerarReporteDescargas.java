// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.descarga.dao;

import co.gov.ideamredd.descarga.dao.GenerarReporte;

/**
 * Clase de ejecución principal (main) para generar el reporte de descargas
 * 
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class GenerarReporteDescargas {

	/**
	 * Método main para orquestar la generación
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String tipodato = "";
		String autor = "";

		System.out.println("Analizando argumentos: ");

		try {
			for (String s : args) {
				System.out.println(s);
				if (s.indexOf("--t=") >= 0) {
					tipodato = s.replace("--t=", "");
				}
				else if (s.indexOf("--a=") >= 0) {
					autor = s.replace("--a=", "");
				}
			}
		}
		catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}

		System.out.println("Iniciando Main... con tipo de dato: " + tipodato + " y autor: " + autor);

		System.out.println("Generando reporte...");
		GenerarReporte generarReporte = new GenerarReporte();

		System.out.println("Consultando datasets...");
		ConsultarDatasets consultarDatasets = new ConsultarDatasets();

		System.out.println("Consultando descargas de usuario...");
		// String dataSetsLista = "Usuario,Tipo1,Numero1,Imagen1,Lidar, 2015/10/10,Usuario 2,Tipo2,Numero2,Imagen2,Lidar2, 2015/12/12,";
		String dataSetsLista = consultarDatasets.consultarDescargasUsuarios(tipodato, autor);
		String dataSetsEncabez = "Nombre Usuario,Tipo ID,Número ID,Nombre imagen,Tipo imagen,Fecha descarga,";

		try {
			System.out.println("Generando reporte XLS...");
			generarReporte.generarReporteXls(dataSetsEncabez, dataSetsLista, "TmpConsultaDetalleDesc", 6);

			System.out.println("Generando reporte PDF...");
			generarReporte.generarReportePDF(dataSetsEncabez, dataSetsLista, "TmpConsultaImg", 6);

			System.out.println("Enviando correo...");
			EnvioDeCorreo envioDeCorreo = new EnvioDeCorreo();
			System.out.println("con adjuntos...");
			envioDeCorreo.enviarCorreoConAdjuntos(tipodato, autor);

		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		System.out.println("Reporte generado");
	}
}
