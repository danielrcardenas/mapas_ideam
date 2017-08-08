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
import co.gov.ideamredd.reportes.GeneradorReporteExcel;
/**
 * Clase servle que retorna la respuesta en un archivo XLS de Excel.
 * Utiliza la variable resoursePath de la base de datos en la tabla parametros
 * y a plantilla Excel con la imágen del encabezado
 * @author Daniel Rodríguez
 *
 */
public class DescargaXLSServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public DescargaXLSServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/xls");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader("Content-disposition",
				"attachment; filename=ReporteInventario.xls");

		String resourcePath = Parametro.getParametro("resourcePath");
		
		File reporte = new File(resourcePath + File.separator + "templates" + File.separator + GeneradorReporteExcel.ARCHIVO_REPORTE);
		//File reporte = new File("/etc/SMBC/ReportesInventario" + File.separator + "templates" + File.separator + GeneradorReporteExcel.ARCHIVO_REPORTE);

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
