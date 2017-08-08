package co.gov.ideamredd.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.entities.InventarioSuelo;

public class DescargaMetodologiaServlet extends HttpServlet {

    private ArrayList<Integer> indicesInvSuelos;
    private ArrayList<String> direcciones;
    private static final String configLog = "co/gov/ideamredd/recursos/newMetodologia";
    private static final ResourceBundle prop = ResourceBundle
	    .getBundle(configLog);
    private static final int BUFFER_SIZE = 1024;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	@SuppressWarnings("unchecked")
	ArrayList<InventarioSuelo> inventariosDeSuelo = (ArrayList<InventarioSuelo>) request
		.getSession().getAttribute("invSuelos");

	indicesInvSuelos = new ArrayList<Integer>();
	indicesInvSuelos.clear();
	direcciones = new ArrayList<String>();
	direcciones.clear();

	String selected = request.getParameter("ISseleccionados");

	for (int i = 0; i < selected.length(); i++) {
	    if (selected.charAt(i) != ',') {
		indicesInvSuelos.add(Integer.parseInt(String.valueOf(selected
			.charAt(i))));
	    }
	}

	for (int c = 0; c < indicesInvSuelos.size(); c++) {
	    direcciones.add(inventariosDeSuelo.get(indicesInvSuelos.get(c))
		    .getMetodologiaArchivo());
	}

	for (int c = 0; c < indicesInvSuelos.size(); c++) {
	    if (indicesInvSuelos.size() == 1) {

		response.setContentType("application/pdf");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader(
			"Content-disposition",
			"attachment; filename="
				+ direcciones.get(0).replaceAll(
					prop.getString("ruta.newMetodologia"),
					""));
		ServletOutputStream stream = response.getOutputStream();
		FileInputStream input = new FileInputStream(direcciones.get(0));
		BufferedInputStream buf = new BufferedInputStream(input);
		int readBytes = 0;

		while ((readBytes = buf.read()) != -1) {
		    stream.write(readBytes);
		}

		stream.flush();
		buf.close();
		stream.close();

	    } else {
		String nombre = "Metodologias.zip";
		try {
		    BufferedInputStream origin = null;
		    FileOutputStream dest = new FileOutputStream(
			    prop.getString("ruta.newMetodologia") + nombre);
		    ZipOutputStream out = new ZipOutputStream(

		    new BufferedOutputStream(dest));

		    byte[] data = new byte[BUFFER_SIZE];
		    for (int i = 0; i < direcciones.size(); i++) {

			String filename = direcciones.get(i);
			FileInputStream fi = new FileInputStream(filename);
			origin = new BufferedInputStream(fi, BUFFER_SIZE);

			ZipEntry entry = new ZipEntry(filename.replaceAll(
				prop.getString("ruta.newMetodologia"), ""));
			out.putNextEntry(entry);

			int count;
			while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
			    out.write(data, 0, count);
			}
			origin.close();
		    }
		    out.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}

		response.setContentType("application/zip");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader("Content-disposition",
			"attachment; filename=" + nombre);
		ServletOutputStream stream = response.getOutputStream();
		FileInputStream input = new FileInputStream(
			prop.getString("ruta.newMetodologia") + nombre);
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
}
