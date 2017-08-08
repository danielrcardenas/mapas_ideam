package co.gov.ideamredd.mbc.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LimpiarSesionServlet  extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String basePath = req.getScheme() + "://"
				+ req.getServerName() + ":" + req.getServerPort() + "/";
		
		req.getSession().invalidate();

		resp.sendRedirect(basePath+"MonitoreoBiomasaCarbono/home.jsp");
	}

}
