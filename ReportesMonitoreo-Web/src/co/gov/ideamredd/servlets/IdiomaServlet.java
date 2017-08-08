package co.gov.ideamredd.servlets;


import java.io.IOException;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadBean;
import javazoom.upload.UploadFile;

import co.gov.ideamredd.lenguaje.LenguajeI18N;
 
public class IdiomaServlet extends HttpServlet {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MultipartFormDataRequest mrequest = null;
	UploadBean upBean = null;
	UploadFile file;
	String idioma;
	String url;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			if (request.getContentType().contains("multi")) {
				mrequest = new MultipartFormDataRequest(request);
				@SuppressWarnings("rawtypes")
				Hashtable files = mrequest.getFiles();
				idioma = mrequest.getParameter("lenguaje");
				url = mrequest.getParameter("pagina");
			} else {
				idioma = request.getParameter("lenguaje");
				url = request.getParameter("pagina");
			}
			request.getSession().setAttribute("i18n", null);
			LenguajeI18N i18n = new LenguajeI18N();
			if (idioma.equals("ES")) {
				i18n.setLenguaje("es");
				i18n.setPais("CO");
			} else {
				i18n.setLenguaje("en");
				i18n.setPais("US");
			}
			ResourceBundle msj = i18n.obtenerMensajeIdioma();
			request.getSession().setAttribute("i18nAuxLeng", i18n.getLenguaje());
			request.getSession().setAttribute("i18nAuxPais", i18n.getPais());
			request.getSession().setAttribute("i18n", msj);
			response.sendRedirect(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
