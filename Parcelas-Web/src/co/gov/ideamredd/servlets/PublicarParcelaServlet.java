package co.gov.ideamredd.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PublicarParcelaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//		ArrayList<Proyecto> p = (ArrayList) req.getSession().getAttribute("proyectos");
//		Integer index = Integer.valueOf(req.getParameter("proyecto"));
//		Proyecto proyecto = (Proyecto) p.get(index);		
//		try {
//			InsertarAsociadosProyecto.publicarProyecto(proyecto.getConsecutivo());
//			proyecto.setPublico(1);
//			p.set(index, proyecto);
//			req.getSession().setAttribute("proyectos", p);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		resp.sendRedirect("pages/verConsultaProyecto.jsp");
	}

}
