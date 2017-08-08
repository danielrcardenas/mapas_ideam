package co.gov.ideamredd.servlets;

import java.io.IOException;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.dao.GenerarReporteParcela;
import co.gov.ideamredd.util.Util;

public class ExportarReporteParcelaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private GenerarReporteParcela generarReporteParcela;

    private String idParcelas;
    private String param;
    // private ArrayList<Parcela> parcelas;
    // private ArrayList<Parcela> parcelasReporte = new ArrayList<Parcela>();
    private HashMap<String, String> parametros = new HashMap<String, String>();

    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {

//	idParcelas = req.getParameter("p");
//	param = req.getParameter("param");
	idParcelas = req.getParameter("idsParcela");
	param = req.getParameter("paramReporte");
	String[] params = param.split(";");
	// parcelas = (ArrayList<Parcela>) req.getSession().getAttribute(
	// "parcelas");
	// String index = req.getParameter("i");
	// parcelasReporte.clear();
	// for (int i = 0; i < parcelas.size(); i++) {
	// if (i == Integer.valueOf(index))
	// parcelasReporte.add(parcelas.get(i));
	// }

	agregarParametros("codigo", params[0], req);
	agregarParametros("nombre", params[1], req);
	agregarParametros("codigoCampo", params[2], req);
	agregarParametros("tipoParcela", params[3], req);
	agregarParametros("fecha", params[4], req);
	agregarParametros("aprov", params[5], req);
	agregarParametros("tipoInventario", params[6], req);
	agregarParametros("descripcion", params[7], req);
	agregarParametros("proposito", params[8], req);
	agregarParametros("observaciones", params[9], req);
	agregarParametros("archivo", params[10], req);
	agregarParametros("geometria", params[11], req);
	agregarParametros("puntos", params[12], req);
	agregarParametros("pais", params[13], req);
	agregarParametros("departamento", params[14], req);
	agregarParametros("municipio", params[15], req);
	agregarParametros("fgda", params[16], req);
	agregarParametros("propietario", params[17], req);
	agregarParametros("custodio", params[18], req);
	agregarParametros("otrosConts", params[19], req);
	agregarParametros("biomasa", params[20], req);
	agregarParametros("individuos", params[21], req);

	generarReporteParcela.setIdParcelas(idParcelas);
	generarReporteParcela.setParametros(parametros);
	String reporte[] = generarReporteParcela.generarReporte(Util.obtenerReportesWPS("rutaReportes"));
	// reporte[2]=index;
	resp.sendRedirect("pub/descarga.jsp?a="+Util.encriptar(reporte[0]+"/"+reporte[1]+";"+reporte[1]));
	
	//	if (!reporte.equals(""))
//	    req.getSession().setAttribute("reporte", reporte);
//	else
//	    req.getSession().setAttribute("reporte",
//		    "<th>NO FUE POSIBLE GENERAR EL REPORTE</th>");
//
//	if (req.getParameter("esPublico") != null)
//	    resp.sendRedirect("verReporteParcela.jsp?esPublico=1");
//	else
//	    resp.sendRedirect("verReporteParcela.jsp");
    }

    private void agregarParametros(String parametro, String valor,
	    HttpServletRequest req) {
	parametros.put(parametro, valor.equals("undefined") ? "off" : valor);
    }
}
