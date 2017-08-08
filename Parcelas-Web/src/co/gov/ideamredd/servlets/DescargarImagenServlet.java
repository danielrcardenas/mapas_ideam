package co.gov.ideamredd.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.entities.Parcela;

public class DescargarImagenServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		Parcela parcela = (Parcela)req.getSession().getAttribute("par");
		String path = "////172.16.1.141/Modelo_Raster/Varios/Monitoreo_Forestal/Inventario_Forestal/Fotografias_Inventarios/Parcelas/REDD/Panoramica_Parcela/";
		String archivo = path + parcela.getNombreImagen();

		File fileToDownload = new File(archivo);
		FileInputStream fileInputStream = new FileInputStream(fileToDownload);

		ServletOutputStream out = response.getOutputStream();
		String mimeType = new MimetypesFileTypeMap()
				.getContentType(parcela.getNombreImagen());

		response.setContentType(mimeType);
		response.setContentLength(fileInputStream.available());
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileToDownload.getName() + "\"");

		int c;
		while ((c = fileInputStream.read()) != -1) {
			out.write(c);
		}
		out.flush();
		out.close();
		fileInputStream.close();
	}

}
