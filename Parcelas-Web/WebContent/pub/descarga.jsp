<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.dao.ObtenerParcelaConsulta"%>
<%@page import="java.io.FileInputStream"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Descarga</title>
<%try{
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
	String imagen = Util.desencriptar(request.getParameter("a"));
	String[] file = imagen.split(";");
	FileInputStream archivo = new FileInputStream(file[0]);
	int longitud = archivo.available();
	byte[] datos = new byte[longitud];
	archivo.read(datos);
	archivo.close();
	response.setContentType("application/octet-stream");
	response.setHeader("Content-Disposition","attachment;filename="+file[1]);
	ServletOutputStream ouputStream = response.getOutputStream();
	ouputStream.write(datos);
	ouputStream.flush();
	ouputStream.close();
// 	response.sendRedirect(basePath+"pub/consultarParcela.jsp");
	}catch(Exception e){ 
		e.printStackTrace(); 
	}
	%>
	
</head>
<body onload="window.close();">

</body>
</html>