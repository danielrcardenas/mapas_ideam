package co.gov.ideamredd.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import co.gov.ideamredd.util.Buffer;

public class CapturaLocalizacionParcelaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private String area;
	private String largo;
	private String ancho;
	private String puntos;
	private String valor;
	private String geometria;
	private Integer mostrar;
	private String dir;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		area = req.getParameter("area");// req.getSession().getAttribute("area")!=null?(String)req.getSession().getAttribute("area"):
		valor = req.getParameter("valor") != null ? req.getParameter("valor")
				: req.getSession().getAttribute("valor") != null ? (String) req
						.getSession().getAttribute("valor") : "0";
		geometria = req.getParameter("geometria") != null ? req
				.getParameter("geometria") : req.getSession().getAttribute(
				"geometria") != null ? (String) req.getSession().getAttribute(
				"geometria") : "1";
		mostrar = Integer.valueOf(req.getParameter("mostrar"));
		largo = req.getParameter("largo");
		ancho = req.getParameter("ancho");
		dir = req.getParameter("volver");
		String[] valores = valor.split(",");

		if (area != null && geometria.equals("1")) {
			Buffer buffer = new Buffer();
			puntos = buffer.generaBufferPunto(Double.valueOf(valores[0]),
					Double.valueOf(valores[1]), (Double.valueOf(area) * 10000));
			req.getSession().setAttribute("puntos", puntos);
			req.getSession().setAttribute("area",
					(Double.valueOf(area) * 10000) + "");
		} else {
			req.getSession().removeAttribute("area");
			req.getSession().removeAttribute("puntos");
			req.getSession().removeAttribute("valor");
			req.getSession().removeAttribute("geometria");
		}

		if (largo != null && largo != "") {
			req.getSession().setAttribute("largo", largo);
			req.getSession().setAttribute("ancho", ancho);
		}

		req.getSession().setAttribute("valor", valor);
		req.getSession().setAttribute("geometria", geometria);

		if (mostrar == 0) {
			if (req.getSession().getAttribute("dir") != null)
				req.getSession().removeAttribute(dir);
			if (req.getParameter("update") == null)
				resp.sendRedirect("pages/registrarParcela3.jsp");
			else
				resp.sendRedirect("pages/modificarParcela3.jsp");
		} else {
			req.getSession().setAttribute("volver", dir);
			resp.sendRedirect("visor.jsp");
		}

	}

}
