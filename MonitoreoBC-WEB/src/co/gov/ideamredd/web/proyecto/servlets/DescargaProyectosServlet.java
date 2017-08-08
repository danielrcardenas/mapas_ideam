// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.proyecto.servlets;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import co.gov.ideamredd.mbc.conexion.Parametro;
import co.gov.ideamredd.proyecto.dao.ConsultarDatosReporte;
import co.gov.ideamredd.proyecto.entities.ReporteProyecto;

/**
 * Servlet usado para descargar datos de proyectos
 */
public class DescargaProyectosServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private String tipoDescarga;
	private String fechaIni;
	private String fechaFin;
	private String dirDescargas;
	private XSSFCellStyle estiloCabeceras;
	private XSSFCellStyle estiloDatos;
	private ArrayList<ReporteProyecto> proyectos;
	private String basePath2;

	@EJB
	public Parametro parametro;
	@EJB
	public ConsultarDatosReporte cdr;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		proyectos = new ArrayList<ReporteProyecto>();

		tipoDescarga = req.getParameter("hidTipoDescarga");
		String[] columnas = req.getParameterValues("selColumnas");
		dirDescargas = parametro.getParametro("ruta_reportes") + "/";

		proyectos = cdr.obtenerProyectos();
		
		basePath2 = req.getScheme() + "://"
				+ req.getServerName() + ":" + req.getServerPort() + "/";

		if (tipoDescarga.equals("XLSX")) {
			generarReporteExcel(resp, columnas);
		}

		if (tipoDescarga.equals("PDF")) {
			generarReportePDF(resp, columnas);
		}

	}

	public void generarReporteExcel(HttpServletResponse response,
			String[] columnas) {
		
		Random random = new Random();
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-k");
			String fechaConFormato = sdf.format(new Date());
			String fileNombre = "Proyectos";
			//if (fechaIni == null) {
				fileNombre = fileNombre + fechaConFormato;
			/*} else {
				fileNombre = fileNombre + fechaIni.replace("/", "-");
				if (fechaFin != null) {
					fileNombre = fileNombre + "--";
					fileNombre = fileNombre + fechaFin.replace("/", "-");
				}
			}*/

			String rutaArchivo = dirDescargas + fileNombre.replace("-", "").replace("0", "") + random.nextInt(1000) + ".xlsx";

			File archivo = new File(rutaArchivo);

			if (!archivo.exists()) {
				FileOutputStream fileOut = new FileOutputStream(rutaArchivo);
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet worksheet = workbook.createSheet("Proyectos");
				//XSSFRow rowCabecera;
				//XSSFCell cellCabecera, cellFechas1, cellFechas2, cellFechas3;
				// cellFechas4;

				// Estilos
				estiloCabeceras = estiloTituloReporte(workbook);
				estiloDatos = estiloDivisionReporte(workbook);

				// Datos
				XSSFRow rowCabecera = worksheet.createRow(1);
				for (int x = 0; x < columnas.length; x++) {
					XSSFCell cellCabecera = rowCabecera.createCell(1+x);
					cellCabecera.setCellValue(columnas[x].replace("_", " "));
					cellCabecera.setCellStyle(estiloCabeceras);
				}
				
				int contCeldas=1;
				for (int x = 0; x < proyectos.size(); x++) {
					contCeldas=1;
					XSSFRow rowRegistro = worksheet.createRow(x+2);
					
					if(listaContiene(columnas,"Codigo_Proyecto")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getCodigo_Proyecto());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Nombre_Proyecto")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getNombre_Proyecto());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Area_Proyecto")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getArea_Proyecto());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Descripcion_Area")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getDescripcion_Area());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Nombre_Pais")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getNombre_Pais());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Nombre_Departamento")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getNombre_Departamento());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Nombre_Municipio")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getNombre_Municipio());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Tipo_De_Bosque")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getTipo_De_Bosque());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"CAR")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getCAR());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Tipo_De_Proyecto")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getTipo_De_Proyecto());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Fecha_De_Inicio")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						if(proyectos.get(x).getFecha_De_Inicio()!=null){
							cellRegistro.setCellValue(proyectos.get(x).getFecha_De_Inicio().toString());
						}else{cellRegistro.setCellValue(" ");}
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Fecha_De_Finalizacion")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						if(proyectos.get(x).getFecha_De_Finalizacion()!=null){
							cellRegistro.setCellValue(proyectos.get(x).getFecha_De_Finalizacion().toString());
						}else{cellRegistro.setCellValue(" ");}
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Tenencia_De_La_Tierra")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getTenencia_De_La_Tierra());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Actividad_REDD")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getActividad_REDD());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Cantidad_De_CO2_A_Reducir")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getCantidad_De_CO2_A_Reducir());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Tasa_Deforestacion_A_Reducir")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getTasa_Deforestacion_A_Reducir());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Nombre_Metodologia")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getNombre_Metodologia());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Descripcion_Metodologia")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getDescripcion_Metodologia());
						cellRegistro.setCellStyle(estiloDatos);
						contCeldas++;
					}
					if(listaContiene(columnas,"Ecuacion_Metodologia")){
						XSSFCell cellRegistro = rowRegistro.createCell(contCeldas);
						cellRegistro.setCellValue(proyectos.get(x).getEcuacion_Metodologia());
						cellRegistro.setCellStyle(estiloDatos);
					}
				}

				workbook.write(fileOut);
				fileOut.flush();
				fileOut.close();
			}

			response.setContentType("application/xlsx");
			response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
			response.setHeader("Cache-Control", "max-age=0");
			response.setHeader("Content-disposition",
					"attachment; filename=Proyectos.xlsx");

			ServletOutputStream stream = response.getOutputStream();
			FileInputStream input = new FileInputStream(rutaArchivo);
			BufferedInputStream buf = new BufferedInputStream(input);
			int readBytes = 0;

			while ((readBytes = buf.read()) != -1) {
				stream.write(readBytes);
			}
			stream.flush();
			buf.close();
			stream.close();
			
			archivo.delete();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generarReportePDF(HttpServletResponse response,
			String[] columnas) {
		
		Random random = new Random();
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-k-m-s");
			String fechaConFormato = sdf.format(new Date());
			String fileNombre = "Proyectos";
			//if (fechaIni == null) {
				fileNombre = fileNombre + fechaConFormato;
			/*} else {
				fileNombre = fileNombre + fechaIni.replace("/", "-");
				if (fechaFin != null) {
					fileNombre = fileNombre + "--";
					fileNombre = fileNombre + fechaFin.replace("/", "-");
				}
			}*/

			String rutaArchivo = dirDescargas + fileNombre.replace("-", "").replace("0", "") + random.nextInt(1000) + ".pdf";

			File archivo = new File(rutaArchivo);

			if (!archivo.exists()) {
				FileOutputStream fileOut = new FileOutputStream(rutaArchivo);
				
				Document document = new Document(PageSize.LETTER.rotate());
				PdfWriter.getInstance(document, fileOut);
				
				document.open();
				
				PdfPTable cabecera = new PdfPTable(3);
				cabecera.setWidthPercentage(100);
		        
				Image img = Image.getInstance(parametro.getParametro("birt.Plantillas") + "/IDEAMRepo.png");
				img.scaleToFit(70, 70);
				PdfPCell ideamImg=new PdfPCell(img);
				ideamImg.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ideamImg.setHorizontalAlignment(Element.ALIGN_CENTER);
				ideamImg.setBorder(0);
				cabecera.addCell(ideamImg);
				
				PdfPCell tituloRepo=new PdfPCell(new Phrase("PROYECTOS REDD"));
				tituloRepo.setVerticalAlignment(Element.ALIGN_MIDDLE);
				tituloRepo.setHorizontalAlignment(Element.ALIGN_CENTER);
				tituloRepo.setBorder(0);
				cabecera.addCell(tituloRepo);
				
				Image imgMin = Image.getInstance(parametro.getParametro("birt.Plantillas") +"/MINRepo.png");
				imgMin.scaleToFit(160, 70);
				PdfPCell minImg=new PdfPCell(imgMin);
				minImg.setVerticalAlignment(Element.ALIGN_MIDDLE);
				minImg.setHorizontalAlignment(Element.ALIGN_CENTER);
				minImg.setBorder(0);
				cabecera.addCell(minImg);
				
				document.add(cabecera);
				
				PdfPTable table = new PdfPTable(columnas.length);
		        table.setWidthPercentage(100);
		        table.setSpacingAfter(15);
		        table.setSpacingBefore(15);
		        
		        //table.setWidths(new int[]{2, 1, 1});
		        
		        
//		        cell = new PdfPCell(new Phrase("Table 1"));
//		        cell.setColspan(3);
//		        table.addCell(cell);
//		        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
//		        cell.setRowspan(2);
//		        table.addCell(cell);
//		        table.addCell("row 1; cell 1");
//		        table.addCell("row 1; cell 2");
//		        table.addCell("row 2; cell 1");
//		        table.addCell("row 2; cell 2");
		        
		        
				
//				XSSFWorkbook workbook = new XSSFWorkbook();
//				XSSFSheet worksheet = workbook.createSheet("Proyectos");
//
//				estiloCabeceras = estiloTituloReporte(workbook);
//				estiloDatos = estiloDivisionReporte(workbook);

				// Datos
				for (int x = 0; x < columnas.length; x++) {
					PdfPCell celda = new PdfPCell(new Phrase(columnas[x].replace("_", " ")));
					celda.setBackgroundColor(new BaseColor(180,255,180));
					celda.setBorderColor(new BaseColor(Color.GREEN));
			        table.addCell(celda);
				}
				
				for (int x = 0; x < proyectos.size(); x++) {
					
					if(listaContiene(columnas,"Codigo_Proyecto")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getCodigo_Proyecto().toString()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Nombre_Proyecto")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getNombre_Proyecto()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Area_Proyecto")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getArea_Proyecto().toString()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Descripcion_Area")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getDescripcion_Area()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Nombre_Pais")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getNombre_Pais()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Nombre_Departamento")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getNombre_Departamento()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Nombre_Municipio")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getNombre_Municipio()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Tipo_De_Bosque")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getTipo_De_Bosque()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"CAR")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getCAR()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Tipo_De_Proyecto")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getTipo_De_Proyecto()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Fecha_De_Inicio")){
						PdfPCell celda;
						if(proyectos.get(x).getFecha_De_Inicio()!=null){
							celda = new PdfPCell(new Phrase(proyectos.get(x).getFecha_De_Inicio().toString()));
						}else
						{
							celda = new PdfPCell(new Phrase(" "));
						}
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Fecha_De_Finalizacion")){
						PdfPCell celda;
						if(proyectos.get(x).getFecha_De_Finalizacion()!=null){
							celda = new PdfPCell(new Phrase(proyectos.get(x).getFecha_De_Finalizacion().toString()));
						}else
						{
							celda = new PdfPCell(new Phrase(" "));
						}
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Tenencia_De_La_Tierra")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getTenencia_De_La_Tierra()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Actividad_REDD")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getActividad_REDD()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Cantidad_De_CO2_A_Reducir")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getCantidad_De_CO2_A_Reducir().toString()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Tasa_Deforestacion_A_Reducir")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getTasa_Deforestacion_A_Reducir().toString()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Nombre_Metodologia")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getNombre_Metodologia()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Descripcion_Metodologia")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getDescripcion_Metodologia()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
					if(listaContiene(columnas,"Ecuacion_Metodologia")){
						PdfPCell celda = new PdfPCell(new Phrase(proyectos.get(x).getEcuacion_Metodologia()));
						celda.setBackgroundColor(new BaseColor(Color.WHITE));
						celda.setBorderColor(new BaseColor(Color.GREEN));
				        table.addCell(celda);
					}
				}
		        
				document.add(table);
				
				
				Image imgLogos = Image.getInstance(parametro.getParametro("birt.Plantillas") +"/LogosRepo.jpg");
				imgLogos.scaleToFit(600, 80);
				document.add(imgLogos);
				
		        document.close();
				fileOut.flush();
				fileOut.close();
			}

			response.setContentType("application/pdf");
			response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
			response.setHeader("Cache-Control", "max-age=0");
			response.setHeader("Content-disposition",
					"attachment; filename=Proyectos.pdf");

			ServletOutputStream stream = response.getOutputStream();
			FileInputStream input = new FileInputStream(rutaArchivo);
			BufferedInputStream buf = new BufferedInputStream(input);
			int readBytes = 0;

			while ((readBytes = buf.read()) != -1) {
				stream.write(readBytes);
			}
			
			stream.flush();
			buf.close();
			stream.close();
			
			archivo.delete();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public XSSFCellStyle estiloTituloReporte(XSSFWorkbook libro) {
		XSSFCellStyle estiloCelda = libro.createCellStyle();
		estiloCelda.setWrapText(true);
		estiloCelda.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		estiloCelda.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		estiloCelda.setFillForegroundColor((short) 44);
		estiloCelda.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeightInPoints((short) 10);
		fuente.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		fuente.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		fuente.setColor((short) 9);
		estiloCelda.setFont(fuente);
		estiloCelda.setBorderBottom((short) 1);
		estiloCelda.setBottomBorderColor((short) 30);
		estiloCelda.setBorderLeft((short) 1);
		estiloCelda.setLeftBorderColor((short) 30);
		estiloCelda.setBorderRight((short) 1);
		estiloCelda.setRightBorderColor((short) 30);
		estiloCelda.setBorderTop((short) 1);
		estiloCelda.setTopBorderColor((short) 30);
		return estiloCelda;
	}
	
	public static XSSFCellStyle estiloDivisionReporte(XSSFWorkbook libro) {
		XSSFCellStyle estiloCelda = libro.createCellStyle();
		estiloCelda.setWrapText(true);
		estiloCelda.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		estiloCelda.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// estiloCelda.setFillForegroundColor((short) 31);
		// estiloCelda.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeightInPoints((short) 8);
		fuente.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		fuente.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		fuente.setColor((short) 8);
		estiloCelda.setFont(fuente);
		estiloCelda.setBorderBottom((short) 1);
		estiloCelda.setBottomBorderColor((short) 30);
		estiloCelda.setBorderLeft((short) 1);
		estiloCelda.setLeftBorderColor((short) 30);
		estiloCelda.setBorderRight((short) 1);
		estiloCelda.setRightBorderColor((short) 30);
		estiloCelda.setBorderTop((short) 1);
		estiloCelda.setTopBorderColor((short) 30);
		return estiloCelda;
	}
	
	public boolean listaContiene(String[] lista, String dato){
		boolean resultado=false;
		
		for(int i=0;i<lista.length;i++){
			if(lista[i].equals(dato)){
				resultado=true;
			}
		}
			
		return resultado;
				
	}

}
