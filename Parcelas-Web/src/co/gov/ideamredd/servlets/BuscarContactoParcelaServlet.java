package co.gov.ideamredd.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.dao.ConsultarAsociadosParcela;
import co.gov.ideamredd.entities.Contacto;

public class BuscarContactoParcelaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public BuscarContactoParcelaServlet() {
		super();
	}

	/**
	 * Servlet que responde a las paginas
	 * -registrarParcela3.jsp
	 * -modificarParcela3.jsp
	 * Permite realizar la busqueda de un contacto registrado
	 */
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		
		ArrayList<Contacto> contactos = new ArrayList<Contacto>();
		String nombre = request.getParameter("nombre");
		String email = request.getParameter("email");
		String tipoContacto = request.getParameter("tipoContacto");
		String idContacto = request.getParameter("idcontacto");
		Contacto contacto = new Contacto();
		
		String tipoFGDA = request.getSession().getAttribute("tipoFGDA")!=null?request.getSession().getAttribute("tipoFGDA").toString():request.getParameter("tipoFGDA");//contacto 1
		String tipoAutor = request.getSession().getAttribute("tipoAutor")!=null?request.getSession().getAttribute("tipoAutor").toString():request.getParameter("tipoAutor");
		String tipoCustodio = request.getSession().getAttribute("tipoCustodio")!=null?request.getSession().getAttribute("tipoCustodio").toString():request.getParameter("tipoCustodio");
		String tipoInvestigador = request.getSession().getAttribute("tipoInvestigador")!=null?request.getSession().getAttribute("tipoInvestigador").toString():request.getParameter("tipoInvestigador");
		String tipoColeccion = request.getSession().getAttribute("tipoColeccion")!=null?request.getSession().getAttribute("tipoColeccion").toString():request.getParameter("tipoColeccion");
		String tipoEncargado = request.getSession().getAttribute("tipoEncargado")!=null?request.getSession().getAttribute("tipoEncargado").toString():request.getParameter("tipoEncargado");//contacto 6
			
		request.getSession().setAttribute("tipoFGDA", tipoFGDA);
		request.getSession().setAttribute("tipoAutor", tipoAutor);
		request.getSession().setAttribute("tipoCustodio", tipoCustodio);
		request.getSession().setAttribute("tipoInvestigador", tipoInvestigador);
		request.getSession().setAttribute("tipoColeccion", tipoColeccion);
		request.getSession().setAttribute("tipoEncargado", tipoEncargado);
		
		try {
//			if(idContacto==null){
//				contactos = ConsultarAsociadosParcela.ConsultarContactoParcela(nombre.toUpperCase(), email.toUpperCase());	
//				request.getSession().setAttribute("contactos", contactos);
//				request.getSession().setAttribute("origen", tipoContacto);
//				if(request.getParameter("mod")!=null)
//					response.sendRedirect("pages/verConsultaContactos.jsp?mod='mod'");
//				else
//					response.sendRedirect("pages/verConsultaContactos.jsp");
//			}else{
//				contactos = (ArrayList)request.getSession().getAttribute("contactos");
//				contacto = contactos.get(Integer.valueOf(idContacto));
//				tipoContacto = (String)request.getSession().getAttribute("origen");
//				request.getSession().setAttribute("contacto"+tipoContacto, contacto);
//				if(request.getParameter("mod")!=null)					
//					response.sendRedirect("pages/modificarParcela3.jsp");
//				else
//					response.sendRedirect("pages/registrarParcela3.jsp");
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
