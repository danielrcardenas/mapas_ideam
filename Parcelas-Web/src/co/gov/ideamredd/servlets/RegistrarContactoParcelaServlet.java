package co.gov.ideamredd.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.dao.InsertarAsociadosParcela;
import co.gov.ideamredd.entities.Contacto;

public class RegistrarContactoParcelaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public RegistrarContactoParcelaServlet() {
		super();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String nombre = request.getParameter("nombre");
		String org = request.getParameter("org");
		Integer pais = Integer.parseInt(request.getParameter("pais"));
		String dir = request.getParameter("dir");
		String tel = request.getParameter("tel");
		String cel = request.getParameter("cel");
		String email = request.getParameter("email");
		String cargo = request.getParameter("cargo");
		String tipo = request.getParameter("tipocontacto"); 
		String sector = request.getParameter("sector"); 
		
		String tipoFGDA = request.getSession().getAttribute("tipoFGDA")!=null?request.getSession().getAttribute("tipoFGDA").toString():request.getParameter("tipoFGDA");//contacto 1
		String tipoAutor = request.getSession().getAttribute("tipoAutor")!=null?request.getSession().getAttribute("tipoAutor").toString():request.getParameter("tipoAutor");
		String tipoCustodio = request.getSession().getAttribute("tipoCustodio")!=null?request.getSession().getAttribute("tipoCustodio").toString():request.getParameter("tipoCustodio");
		String tipoInvestigador = request.getSession().getAttribute("tipoInvestigador")!=null?request.getSession().getAttribute("tipoInvestigador").toString():request.getParameter("tipoInvestigador");
		String tipoColeccion = request.getSession().getAttribute("tipoColeccion")!=null?request.getSession().getAttribute("tipoColeccion").toString():request.getParameter("tipoColeccion");
		String tipoEncargado = request.getSession().getAttribute("tipoEncargado")!=null?request.getSession().getAttribute("tipoEncargado").toString():request.getParameter("tipoEncargado");//contacto 6
		
//		Contacto contacto = new Contacto();
//		contacto.setNombre(nombre);
//		contacto.setOrganizacion(org);
//		contacto.setPais(pais);
//		contacto.setDireccion(dir);
//		contacto.setTelefono(tel);
//		contacto.setMovil(cel);
//		contacto.setCorreo(email);
//		contacto.setCargo(cargo);
//		contacto.setTipo(tipo);	
//		contacto.setSector(sector);
//		
//		try {
//			int consecutivo=(int)InsertarAsociadosParcela.insertaContactoParcela(contacto);
//			if(consecutivo>0){	
//				contacto.setConsecutivo(consecutivo);
//				if(contacto.getTipo().equalsIgnoreCase("FGDA")){					
//					request.getSession().setAttribute("contacto1", contacto);					
//				}else if(contacto.getTipo().equalsIgnoreCase("Autor")){
//					request.getSession().setAttribute("contacto2", contacto);
//				}else if(contacto.getTipo().equalsIgnoreCase("Custodio")){
//					request.getSession().setAttribute("contacto3", contacto);
//				}else if(contacto.getTipo().equalsIgnoreCase("Investigador")){
//					request.getSession().setAttribute("contacto4", contacto);
//				}else if(contacto.getTipo().equalsIgnoreCase("Coleccion")){
//					request.getSession().setAttribute("contacto5", contacto);
//					tipoColeccion="3";
//				}else if(contacto.getTipo().equalsIgnoreCase("Encargado")){
//					request.getSession().setAttribute("contacto6", contacto);
//					tipoEncargado="3";
//				}
//			}else{//reportar error en registro
//				if(contacto.getTipo().equalsIgnoreCase("FGDA")){
//					request.getSession().setAttribute("freg1", "ERROR EN EL REGISTRO.POR FAVOR VERIFIQUE QUE EL CONTACTO NO EXISTA");
//				}else if(contacto.getTipo().equalsIgnoreCase("Autor")){
//					request.getSession().setAttribute("freg2", "ERROR EN EL REGISTRO.POR FAVOR VERIFIQUE QUE EL CONTACTO NO EXISTA");
//				}else if(contacto.getTipo().equalsIgnoreCase("Custodio")){
//					request.getSession().setAttribute("freg3", "ERROR EN EL REGISTRO.POR FAVOR VERIFIQUE QUE EL CONTACTO NO EXISTA");
//				}else if(contacto.getTipo().equalsIgnoreCase("Investigador")){
//					request.getSession().setAttribute("freg4", "ERROR EN EL REGISTRO.POR FAVOR VERIFIQUE QUE EL CONTACTO NO EXISTA");
//				}else if(contacto.getTipo().equalsIgnoreCase("Coleccion")){
//					request.getSession().setAttribute("freg5", "ERROR EN EL REGISTRO.POR FAVOR VERIFIQUE QUE EL CONTACTO NO EXISTA");
//				}else if(contacto.getTipo().equalsIgnoreCase("Encargado")){
//					request.getSession().setAttribute("freg6", "ERROR EN EL REGISTRO.POR FAVOR VERIFIQUE QUE EL CONTACTO NO EXISTA");
//				}
//			}				
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
		
		request.getSession().setAttribute("tipoFGDA", tipoFGDA);
		request.getSession().setAttribute("tipoAutor", tipoAutor);
		request.getSession().setAttribute("tipoCustodio", tipoCustodio);
		request.getSession().setAttribute("tipoInvestigador", tipoInvestigador);
		request.getSession().setAttribute("tipoColeccion", tipoColeccion);
		request.getSession().setAttribute("tipoEncargado", tipoEncargado);
		
		if(request.getParameter("mod")!=null)
			response.sendRedirect("pages/modificarParcela3.jsp");
		else
			response.sendRedirect("pages/registrarParcela3.jsp");
	}

}
