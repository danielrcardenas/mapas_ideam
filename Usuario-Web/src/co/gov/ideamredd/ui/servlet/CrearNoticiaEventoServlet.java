package co.gov.ideamredd.ui.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadBean;
import javazoom.upload.UploadException;
import javazoom.upload.UploadFile;
import co.gov.ideamredd.ui.dao.RegistraNoticiaEvento;
import co.gov.ideamredd.util.SMBC_Log;
import co.gov.ideamredd.util.Util;

public class CrearNoticiaEventoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String archivoParcela = "co/gov/ideamredd/servlet/home";
	private static final ResourceBundle par = ResourceBundle
			.getBundle(archivoParcela);
	private static final String noticia = "1";
	private static final String evento = "2";
	private RegistraNoticiaEvento registrar = new RegistraNoticiaEvento();
	MultipartFormDataRequest mrequest = null;
	UploadBean upBean = null;
	UploadFile file;
	String ubicacionArchivo = Util.obtenerClave("ruta.imagen.noticia", par);
	String nombre;
	String tipo;
	String descripcion;
	String imagen;
	String fecha;
	String hora;
	String lugar;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// SMBC_Log.Log(this.getClass());
			mrequest = new MultipartFormDataRequest(request);
			@SuppressWarnings("rawtypes")
			Hashtable files = mrequest.getFiles();
			file = (UploadFile) files.get("imagen");
			upBean = new UploadBean();
			try {
				upBean.setFolderstore(ubicacionArchivo);
				upBean.store(mrequest, "imagen");
			} catch (UploadException e) {
				e.printStackTrace();
			}
			registrarNoticia();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void registrarNoticia() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		tipo = mrequest.getParameter("noticiaEvento");
		nombre = mrequest.getParameter("nombre");
		descripcion = mrequest.getParameter("descripcion");
		fecha = mrequest.getParameter("fecha");
		hora = mrequest.getParameter("hora");
		lugar = mrequest.getParameter("lugar");
		imagen = file.getFileName();
		registrar.setTipo(Integer.valueOf(tipo));
		if (tipo.equals(noticia)) {
			registrar.setNombre(nombre);
			registrar.setDescripcion(descripcion);
			registrar.setFecha(dateFormat.format(new Date()));
			registrar.setPathImagen(ubicacionArchivo + imagen);
			registrar.crearNoticia();
		} else if (tipo.equals(evento)) {
			registrar.setNombre(nombre);
			registrar.setDescripcion(descripcion);
			registrar.setFecha(fecha);
			registrar.setLugar(lugar);
			registrar.setHora(hora);
			registrar.crearEvento();
		}
		
	}
}
