package co.gov.ideamredd.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadBean;
import javazoom.upload.UploadException;
import javazoom.upload.UploadFile;
import co.gov.ideamredd.inventarioSuelo.dao.RegistrarInventarioSuelo;

/**
 * RegistrarInventarioSueloServlet.java Servlet que obtiene los datos para
 * registrar un inventario de suelo.
 * 
 * SMBC Proyecto: Sistema de Monitoreo de Bosques y Carbono
 * 
 * @author Julio Cesar Sanchez Torres Oct 25 de 2013
 */

public class RegistrarInventarioSueloServlet extends HttpServlet {

    @EJB
    RegistrarInventarioSuelo ris;

    private static final String configNewMetodologia = "co/gov/ideamredd/recursos/newMetodologia";
    private static final ResourceBundle prop = ResourceBundle
	    .getBundle(configNewMetodologia);
    private Integer parcela;
    private Integer metodologia;
    private Integer profundidad;
    private String textura;
    private Integer densidad;
    private Integer flujo;
    private String fecha;
    private Boolean nuevaMetod;
    private String nombreNewMetodologia;
    private String ecuacionNewMetodologia;
    private String archivoNewMetodologia;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	MultipartFormDataRequest mrequest = null;
	UploadBean upBean = null;
	String ubicacionArchivo = null;

	try {
	    mrequest = new MultipartFormDataRequest(request);

	    ubicacionArchivo = prop.getString("ruta.newMetodologia")
		    + mrequest.getParameter("parcelaID")
		    + mrequest.getParameter("parcelaNombre") + "/";
	    parcela = Integer.parseInt(mrequest.getParameter("parcelaID"));
	    profundidad = Integer
		    .parseInt(mrequest.getParameter("newProfToma"));
	    textura = mrequest.getParameter("newTextura");
	    densidad = Integer.parseInt(mrequest
		    .getParameter("newDensAparente"));
	    flujo = Integer.parseInt(mrequest.getParameter("newFlujoCO2"));
	    fecha = mrequest.getParameter("newFechaTomaDatos");
	    nuevaMetod = Boolean.valueOf(mrequest.getParameter("hayMetod"));
	} catch (UploadException e) {
	    e.printStackTrace();
	}

	if (!nuevaMetod) {
	    metodologia = Integer.parseInt(mrequest
		    .getParameter("newMetodologia"));
	    ris.registraInventarioSuelo(parcela, metodologia, profundidad,
		    textura, densidad, flujo, fecha);
	} else {
	    nombreNewMetodologia = mrequest.getParameter("metodNombre");
	    ecuacionNewMetodologia = mrequest.getParameter("metodEcuacion");

	    @SuppressWarnings("rawtypes")
	    Hashtable files = mrequest.getFiles();
	    UploadFile file = (UploadFile) files.get("fileMetodologia");

	    String nombre = (new Date()).toString().replaceAll(" ", "_")
		    + file.getFileName().replace(' ', '_');
	    file.setFileName(nombre);

	    upBean = new UploadBean();
	    try {
		upBean.setFolderstore(ubicacionArchivo);
		upBean.store(mrequest, "fileMetodologia");
	    } catch (UploadException e) {
		e.printStackTrace();
	    }

	    if (file != null) {
		archivoNewMetodologia = ubicacionArchivo + file.getFileName();
	    }

	    ris.registrarInventarioSueloConMetodologia(parcela,
		    nombreNewMetodologia, ecuacionNewMetodologia,
		    archivoNewMetodologia, profundidad, textura, densidad,
		    flujo, fecha);
	}

	response.sendRedirect("reg/consultaInventarioSuelos.jsp");
    }
}
