package co.gov.ideamredd.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.dao.ConsultarAsociadosParcela;
import co.gov.ideamredd.dao.InsertarAsociadosParcela;
import co.gov.ideamredd.entities.Contacto;
import co.gov.ideamredd.entities.Parcela;

public class CapturaContactosParcelaServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	String tipoFGDA;
	String tipoAutor;
	String tipoCustodio;
	String poseeInvestigador;
	String poseeColeccion;
	boolean isUsuarioContacto = false;
	
	Integer idUsuario;
	Integer idContactoUsuario;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
//		tipoFGDA = request.getParameter("tipoFGDA")!=""?request.getParameter("tipoFGDA"):request.getSession().getAttribute("tipoFGDA")!=null?request.getSession().getAttribute("tipoFGDA").toString():"3";//contacto 1
//		tipoAutor = request.getParameter("tipoAutor")!=""?request.getParameter("tipoAutor"):request.getSession().getAttribute("tipoAutor").toString();
//		tipoCustodio = request.getParameter("tipoCustodio")!=""?request.getParameter("tipoCustodio"):request.getSession().getAttribute("tipoCustodio").toString();
//		poseeInvestigador = request.getParameter("poseeInvestigador");
//		poseeColeccion = request.getParameter("poseeColeccion");
//		boolean ok = true;
//		idUsuario = Integer.valueOf(request.getSession().getAttribute("usr_seq").toString());
//		
//		Contacto cUsuario = new Contacto();		
//		
//		Contacto cIdeam = new Contacto();
//		cIdeam = ConsultarAsociadosParcela.ConsultarContactoParcela(1,-1);
//		if(request.getSession().getAttribute("update")==null){
//			if(tipoFGDA.equals("1")){			
//				try {
//					idContactoUsuario = InsertarAsociadosParcela.insertaContactoParcela(idUsuario, "FGDA");
//					if(idContactoUsuario>0){
//						cUsuario.setConsecutivo(idContactoUsuario);
//						request.getSession().setAttribute("contacto1", cUsuario);
//						isUsuarioContacto = true;
//					}else{
//						cUsuario = ConsultarAsociadosParcela.ConsultarContactoParcela(-1,idUsuario);
//						request.getSession().setAttribute("contacto1", cUsuario);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}			
//			}else if(tipoFGDA.equals("2"))
//				request.getSession().setAttribute("contacto2", cIdeam);
//			
//			if(tipoAutor.equals("1")){
//				if(isUsuarioContacto)
//					request.getSession().setAttribute("contacto2", cUsuario);
//				else{
//					try {
//						idContactoUsuario = InsertarAsociadosParcela.insertaContactoParcela(idUsuario, "Autor");
//						if(idContactoUsuario>0){
//							cUsuario.setConsecutivo(idContactoUsuario);
//							request.getSession().setAttribute("contacto2", cUsuario);
//							isUsuarioContacto = true;
//						}else{
//							cUsuario = ConsultarAsociadosParcela.ConsultarContactoParcela(-1,idUsuario);
//							request.getSession().setAttribute("contacto2", cUsuario);
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}else if(tipoAutor.equals("2"))
//				request.getSession().setAttribute("contacto2", cIdeam);
//		
//			if(tipoCustodio.equals("1")){
//				if(isUsuarioContacto)
//					request.getSession().setAttribute("contacto3", cUsuario);
//				else{
//					try {
//						idContactoUsuario = InsertarAsociadosParcela.insertaContactoParcela(idUsuario, "Custodio");
//						if(idContactoUsuario>0){
//							cUsuario.setConsecutivo(idContactoUsuario);
//							request.getSession().setAttribute("contacto3", cUsuario);
//							isUsuarioContacto = true;
//						}else{
//							cUsuario = ConsultarAsociadosParcela.ConsultarContactoParcela(-1,idUsuario);
//							request.getSession().setAttribute("contacto3", cUsuario);
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}else if(tipoCustodio.equals("2"))
//				request.getSession().setAttribute("contacto3", cIdeam);
//					
//			request.getSession().setAttribute("tipoFGDA", tipoFGDA);
//			request.getSession().setAttribute("tipoAutor", tipoAutor);
//			request.getSession().setAttribute("tipoCustodio", tipoCustodio);
//		}else{
//			Parcela p = (Parcela)request.getSession().getAttribute("parcela"); 
//			request.getSession().setAttribute("tipoFGDA",p.getFgda()+"");
//			request.getSession().setAttribute("tipoAutor", p.getTipoAutor()+"");
//			request.getSession().setAttribute("tipoCustodio", p.getTipoCustodio()+"");
//			if(poseeInvestigador.equalsIgnoreCase("0"))
//				request.getSession().setAttribute("tieneInves",0);
//			if(poseeColeccion.equalsIgnoreCase("0"))
//				request.getSession().setAttribute("tieneColeccion",0);
//		}
//		if(ok){
//			if(request.getParameter("update")==null)
//				response.sendRedirect("pages/registrarParcela4.jsp");
//			else
//				response.sendRedirect("modificarparcela");
//		}else
//			response.sendRedirect("pages/registrarParcela3.jsp");
	}

}
