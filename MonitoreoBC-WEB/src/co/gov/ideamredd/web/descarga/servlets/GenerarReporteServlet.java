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

import co.gov.ideamredd.descarga.dao.GenerarReporte;

import com.itextpdf.text.*;
import org.apache.log4j.Logger;

/**
 * Servlet implementation class GenerarReporteServlet, genera el reporte de las imagenes (Data sets) Registradas en apollo
 */
@WebServlet("/GenerarReporteServlet") public class GenerarReporteServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;
	private static Logger		log					= Logger.getLogger("SMBCLog");
	@EJB
	GenerarReporte				generarReporte;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info(GenerarReporteServlet.class + "Inicio Generar Reporte Datasets");
		req.setCharacterEncoding("UTF-8");
		String dataSetsLista = req.getParameter("dataSetsLista");
		String dataSetsEncabez = req.getParameter("dataSetsListaEncab");

		if (req.getParameter("btnReporte") != null) {
			generarReporte.generarReporteXls(dataSetsEncabez, dataSetsLista, "TmpConsultaImg", 13);
			resp.setContentType("application/xls");
			resp.setHeader("Cache-Control", "no-cache");
			resp.setHeader("Cache-Control", "max-age=0");
			resp.setHeader("Content-disposition", "attachment; filename=TmpConsultaImg.xlsx");

			ServletOutputStream stream = resp.getOutputStream();
			String ruta = generarReporte.getRutaReporte() + "/" + generarReporte.getNombreReporte() + ".xlsx";
			FileInputStream input = new FileInputStream(ruta);
			BufferedInputStream buf = new BufferedInputStream(input);

			int readBytes = 0;

			while ((readBytes = buf.read()) != -1) {
				stream.write(readBytes);
			}
			stream.flush();
			buf.close();
			stream.close();
		}
		else if (req.getParameter("btnReportepdf") != null) {
			generarReporte.generarReportePDF(dataSetsEncabez, dataSetsLista, "TmpConsultaImg", 13);
			resp.setContentType("application/pdf");
			resp.setHeader("Cache-Control", "no-cache");
			resp.setHeader("Cache-Control", "max-age=0");
			resp.setHeader("Content-disposition", "attachment; filename=TmpConsultaImg.pdf");

			ServletOutputStream stream = resp.getOutputStream();
			String ruta = generarReporte.getRutaReporte() + "/" + generarReporte.getNombreReporte() + ".pdf";
			FileInputStream input = new FileInputStream(ruta);
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
