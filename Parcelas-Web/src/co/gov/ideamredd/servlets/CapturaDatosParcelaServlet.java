package co.gov.ideamredd.servlets;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.entities.Parcela;
import co.gov.ideamredd.util.Util;

public class CapturaDatosParcelaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;


	public CapturaDatosParcelaServlet() {
		super();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String nombreParcela = request.getParameter("nomparcela");
		String tieneProyecto = request.getParameter("tieneproyecto")==null?"0":request.getParameter("tieneproyecto");
		Integer idProyectoParcela = request.getParameter("proparcela")==null?0:Integer.parseInt(request.getParameter("proparcela"));
		Integer temporalidad = Integer.parseInt(request.getParameter("temporalidad"));
		String fechaEstablecimiento = request.getParameter("festablecimiento");
		String aprovechamiento = request.getParameter("aprovechamiento");
		Integer tipoInventario = Integer.parseInt(request.getParameter("tipoinv"));
		String descripcionParcela = request.getParameter("descparcela");
		String proposito = request.getParameter("proposito");
		String observaciones = request.getParameter("observaciones");
		String tieneImagen = request.getParameter("tieneImagen");
		String estado = "1";				
		
		String fe[] = fechaEstablecimiento.split("/");
		Date fep = new Date(Integer.valueOf(fe[0].trim()) - 1900, Integer.valueOf(fe[1].trim()) - 1, Integer.valueOf(fe[2].trim()));
		
//		if(tieneProyecto.equalsIgnoreCase("1")){
//			Integer idUsuario = ConsultarAsociadosParcela.ConsultarPropietarioProyecto(idProyectoParcela);			
//			Contacto c1 = ConsultarAsociadosParcela.ConsultarContactoParcela(-1, idUsuario);
//			if(c1.getCorreo()==null){
//				try {
//					idUsuario = InsertarAsociadosParcela.insertaContactoParcela(idUsuario, "FGDA");
//					c1 = ConsultarAsociadosParcela.ConsultarContactoParcela(-1, idUsuario);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			request.getSession().setAttribute("contacto1", c1);
//			request.getSession().setAttribute("idproyecto", idProyectoParcela);
//		}
		Parcela parcela = null;		
		if(request.getParameter("update")==null){
			parcela = new Parcela();
			parcela.setNombre(nombreParcela);
			parcela.setTemporalidad(temporalidad);
//			parcela.setFechaEstablecimiento(Util.convertToDate(fep));
			parcela.setInventario(tipoInventario);
			parcela.setDescripcion(descripcionParcela);
			parcela.setProposito(Integer.valueOf(proposito));
			parcela.setObservaciones(observaciones);
			parcela.setEstado(Integer.valueOf(estado));
			parcela.setAprovechamiento(aprovechamiento);
			request.getSession().setAttribute("parcela", parcela);		
			if(Integer.valueOf(tieneImagen)>0)
				response.sendRedirect("pages/registrarParcela1.jsp");
			else
				response.sendRedirect("pages/registrarParcela2.jsp");
		}else{
			ArrayList p = (ArrayList)request.getSession().getAttribute("parcelas");
			String index = request.getParameter("update");	
			request.getSession().setAttribute("update", index);
			parcela = (Parcela)p.get(Integer.valueOf(index));
//			String geometria[] = ConsultarAsociadosParcela.ConsultarGeoParcela(parcela.getConsecutivo());
			parcela.setNombre(nombreParcela);
			parcela.setTemporalidad(temporalidad);
//			parcela.setFechaEstablecimiento(Util.convertToTimestamp(fep));
			parcela.setInventario(tipoInventario);
			parcela.setDescripcion(descripcionParcela);
			parcela.setProposito(Integer.valueOf(proposito));
			parcela.setObservaciones(observaciones);
			parcela.setEstado(Integer.valueOf(estado));
			parcela.setAprovechamiento(aprovechamiento);
//			request.getSession().setAttribute("parcela", parcela);
//			request.getSession().setAttribute("geometria", geometria[0]);
//			request.getSession().setAttribute("valor", geometria[1]);
//			request.getSession().setAttribute("area", parcela.getArea().toString());
//			request.getSession().setAttribute("largo", parcela.getLargoParcela().toString());
//			request.getSession().setAttribute("ancho", parcela.getAnchoParcela().toString());
			if(Integer.valueOf(tieneImagen)>0){
				File archivo_server = null;
				if(!parcela.getNombreImagen().equals("")){
					archivo_server = new File(Util.getProperty(this.getClass(),"parcela.properties", "rutaImagen") + parcela.getNombreImagen());
					request.getSession().setAttribute("imgCargada", archivo_server);
				}
				response.sendRedirect("pages/modificarParcela1.jsp");
				
			}else{
				File archivo_server = new File(Util.getProperty(this.getClass(),"parcela.properties", "rutaImagen") + parcela.getNombreImagen());
				request.getSession().setAttribute("imgCargada", archivo_server);
				response.sendRedirect("pages/modificarParcela2.jsp");
			}
		}
		
	}

}
