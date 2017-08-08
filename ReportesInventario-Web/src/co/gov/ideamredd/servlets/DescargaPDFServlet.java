package co.gov.ideamredd.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.conexionBD.Parametro;
import co.gov.ideamredd.reportes.GeneradorReportePDF;

/**
 * Clase servler que retorna y construye un archivo en formato PDF
 * con la respuesta a la solicitud del repote de inventarios.
 * Utiliza la plantilla tempale que se encuentra en la variable
 * resourcePath de la tabla parametros en la base de datos.
 * @author Daniel Rodríguez
 *
 */
public class DescargaPDFServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public DescargaPDFServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String resourcePath = Parametro.getParametro("resourcePath");
		//resourcePath = resourcePath.substring(0,resourcePath.lastIndexOf(File.separator));
		

		File reporte = new File(resourcePath + File.separator + "templates" + File.separator + GeneradorReportePDF.ARCHIVO_REPORTE);
		//File reporte = new File("/etc/SMBC/ReportesInventario" + File.separator + "templates" + File.separator + GeneradorReportePDF.ARCHIVO_REPORTE);

		response.setContentType("application/pdf");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader("Content-disposition", "attachment; filename=Reporte.pdf");

		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(reporte);
		byte[] buffer = new byte[4096];
		int length;
		while ((length = in.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}
		in.close();
		out.flush();
	}
}
