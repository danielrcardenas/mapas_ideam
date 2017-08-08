package co.gov.ideamredd.servlets;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.entities.Parcela;

public class RemoverImagenParcelaServlet extends HttpServlet {

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			req.getSession().removeAttribute("imagen");
			req.getSession().removeAttribute("imgCargada");
			Parcela p = (Parcela) req.getSession().getAttribute("parcela");
			File f = new File(p.getRutaImagen());
			f.delete();
			p.setNombreImagen("");
			p.setRutaImagen("");
			req.getSession().setAttribute("parcela", p);

			resp.sendRedirect("pages/decisionImagen.jsp");
	}

}
