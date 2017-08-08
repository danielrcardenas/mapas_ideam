// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.descarga.servlets;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import co.gov.ideamredd.descarga.dao.GenerarReporte;

/**
 * Servlet implementation class GenerarReporteDescargasServlet, genera el reporte de las descargas de im�genes realizadas por los usuarios
 */
@WebServlet("/generarReporteDescargasServlet")
public class GenerarReporteDescargasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	GenerarReporte generarReporte;

	private static Logger logg=Logger.getLogger("SMBCLog");
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logg.info( GenerarReporteDescargasServlet.class+ "Inicio Generar Reporte Descargas");
		String dataSetsLista = req.getParameter("dataSetsLista");
		String dataSetsEncabez = req.getParameter("dataSetsListaEncab");
		
		if (req.getParameter("btnReporte") != null) {
		generarReporte.generarReporteXls(dataSetsEncabez, dataSetsLista, "TmpConsultaDetalleDesc",6);
		resp.setContentType("application/xls");
		resp.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		resp.setHeader("Cache-Control", "max-age=0");
		resp.setHeader("Content-disposition",
				"attachment; filename=TmpConsultaDetalleDesc.xls");

		ServletOutputStream stream = resp.getOutputStream();
		FileInputStream input = new FileInputStream(generarReporte.getRutaReporte()+generarReporte.getNombreReporte()+".xlsx");
		BufferedInputStream buf = new BufferedInputStream(input);

		int readBytes = 0;

		while ((readBytes = buf.read()) != -1) {
			stream.write(readBytes);
		}
		stream.flush();
		buf.close();
		stream.close();
	 } else if (req.getParameter("btnReportepdf") != null) {
		 generarReporte.generarReportePDF(dataSetsEncabez, dataSetsLista, "TmpConsultaImg",6);
     	resp.setContentType("application/pdf");
			resp.setHeader("Cache-Control", "no-cache"); 
			resp.setHeader("Cache-Control", "max-age=0");
			resp.setHeader("Content-disposition",
					"attachment; filename=TmpConsultaDetalleDesc.pdf");

			ServletOutputStream stream = resp.getOutputStream();
			FileInputStream input = new FileInputStream(generarReporte.getRutaReporte()+generarReporte.getNombreReporte()+".pdf");
			BufferedInputStream buf = new BufferedInputStream(input);

			int readBytes = 0;

			while ((readBytes = buf.read()) != -1) {
				stream.write(readBytes);
			}
			stream.flush();
			buf.close();
			stream.close();
     } 

}


}
