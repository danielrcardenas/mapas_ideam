package co.gov.ideamredd.reportes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.ejb.Stateless;

import co.gov.ideamredd.entities.InformacionReporteInventarios;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Stateless
public class GeneradorReporteEstCompPDF {

	public static final String ARCHIVO_REPORTE = "reporte_inventarios.pdf";
	private static final String[] ENCABEZADOS = { "Especie",
			"Frecuencia Relativa", "Abundancia Relativa", "Dominancia", "IVI" };

	DecimalFormat format = new DecimalFormat("0.0000");

	public void generarReporte(List<InformacionReporteInventarios> resultados,
			String path, double indiceShannon, double indiceSimpson, double diversidadGamma) throws IOException, DocumentException {

		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(path
				+ ARCHIVO_REPORTE));
		document.open();
		Image image1 = Image.getInstance(path + "EncabezadoRptEstComp.png");
		image1.scaleAbsolute(500, 50);
		document.add(image1);
		PdfPTable table = createFirstTable(resultados);


		PdfPCell celdaEmpty = new PdfPCell(new Phrase(" ", FontFactory.getFont(
				"Serif", 10, new BaseColor(102, 102, 102))));
		celdaEmpty.setBorderColor(new BaseColor(204, 204, 204));
		celdaEmpty.setBorderWidthLeft(2f);
		celdaEmpty.setBorderWidthRight(1f);
		celdaEmpty.setBackgroundColor(new BaseColor(160, 160, 160));



		PdfPCell celdaDiversidad = new PdfPCell(new Phrase("Diversidad Gamma",
				FontFactory.getFont("Serif", 10, new BaseColor(102, 102, 102))));
		celdaDiversidad.setBorderColor(new BaseColor(204, 204, 204));
		celdaDiversidad.setBorderWidthLeft(2f);
		celdaDiversidad.setBorderWidthRight(1f);
		celdaDiversidad.setBackgroundColor(new BaseColor(160, 160, 160));
		PdfPCell celdaDiversidadGammma = new PdfPCell(new Phrase(
				format.format(diversidadGamma), FontFactory.getFont("Serif", 10,
						new BaseColor(102, 102, 102))));
		celdaDiversidad.setBorderColor(new BaseColor(204, 204, 204));
		celdaDiversidad.setBorderWidthLeft(2f);
		celdaDiversidad.setBorderWidthRight(1f);
		celdaDiversidad.setBackgroundColor(new BaseColor(160, 160, 160));
		table.addCell(celdaEmpty);
		table.addCell(celdaEmpty);
		table.addCell(celdaEmpty);
		table.addCell(celdaDiversidad);
		table.addCell(celdaDiversidadGammma);


		PdfPCell celdaShannon = new PdfPCell(new Phrase("Indice Shannon Weaver",
				FontFactory.getFont("Serif", 10, new BaseColor(102, 102, 102))));
		celdaShannon.setBorderColor(new BaseColor(204, 204, 204));
		celdaShannon.setBorderWidthLeft(2f);
		celdaShannon.setBorderWidthRight(1f);
		celdaShannon.setBackgroundColor(new BaseColor(160, 160, 160));
		PdfPCell celdaShannonWeaver = new PdfPCell(new Phrase(
				format.format(indiceShannon), FontFactory.getFont("Serif", 10,
						new BaseColor(102, 102, 102))));
		celdaShannon.setBorderColor(new BaseColor(204, 204, 204));
		celdaShannon.setBorderWidthLeft(2f);
		celdaShannon.setBorderWidthRight(1f);
		celdaShannon.setBackgroundColor(new BaseColor(160, 160, 160));
		table.addCell(celdaEmpty);
		table.addCell(celdaEmpty);
		table.addCell(celdaEmpty);
		table.addCell(celdaShannon);
		table.addCell(celdaShannonWeaver);

		PdfPCell celdaSimpson = new PdfPCell(new Phrase("Indice Simpson",
				FontFactory.getFont("Serif", 10, new BaseColor(102, 102, 102))));
		celdaSimpson.setBorderColor(new BaseColor(204, 204, 204));
		celdaSimpson.setBorderWidthLeft(2f);
		celdaSimpson.setBorderWidthRight(1f);
		celdaSimpson.setBackgroundColor(new BaseColor(160, 160, 160));
		PdfPCell celdaIndiceSimpson = new PdfPCell(new Phrase(
				format.format(indiceSimpson), FontFactory.getFont("Serif", 10,
						new BaseColor(102, 102, 102))));
		celdaIndiceSimpson.setBorderColor(new BaseColor(204, 204, 204));
		celdaIndiceSimpson.setBorderWidthLeft(2f);
		celdaIndiceSimpson.setBorderWidthRight(1f);
		celdaIndiceSimpson.setBackgroundColor(new BaseColor(160, 160, 160));
		table.addCell(celdaEmpty);
		table.addCell(celdaEmpty);
		table.addCell(celdaEmpty);
		table.addCell(celdaSimpson);
		table.addCell(celdaIndiceSimpson);



		document.add(table);

		document.close();

	}

	private PdfPTable createFirstTable(
			List<InformacionReporteInventarios> resultados) {

		PdfPTable table = new PdfPTable(ENCABEZADOS.length);

		PdfPCell cell;
		cell = new PdfPCell(new Phrase(
				"Indicadores de estructura y composición"));
		cell.setColspan(6);
		cell.setBackgroundColor(new BaseColor(245, 245, 245));
		cell.setBorderColor(new BaseColor(204, 204, 204));
		table.addCell(cell);

		PdfPCell cellEncabezado, cellContenido;
		for (int i = 0; i < ENCABEZADOS.length; i++) {
			cellEncabezado = new PdfPCell(new Phrase(ENCABEZADOS[i],
					FontFactory.getFont("Serif", 11, new BaseColor(102, 102,
							102))));
			cellEncabezado.setBorderColor(new BaseColor(204, 204, 204));
			cellEncabezado.setBorderWidthLeft(2f);
			cellEncabezado.setBorderWidthRight(1f);
			cellEncabezado.setBackgroundColor(new BaseColor(160, 160, 160));
			table.addCell(cellEncabezado);
		}

		int contColor = 0;

		for (InformacionReporteInventarios informacionReporteInventarios : resultados) {
			contColor++;
			String[] informacion = extraerInformacion(informacionReporteInventarios);
			for (int i = 0; i < ENCABEZADOS.length; i++) {

				cellContenido = new PdfPCell(new Phrase(informacion[i],
						FontFactory.getFont("Serif", 9, new BaseColor(102, 102,
								102))));

				if (contColor % 2 == 0) {
					cellContenido.setBackgroundColor(new BaseColor(245, 245,
							245));
				} else {
					cellContenido.setBackgroundColor(new BaseColor(221, 221,
							221));
				}
				cellContenido.setBorderColor(new BaseColor(204, 204, 204));

				cellContenido.setBorderWidthLeft(2f);
				cellContenido.setBorderWidthRight(1f);
				table.addCell(cellContenido);
			}

		}

		return table;
	}

	private String[] extraerInformacion(
			InformacionReporteInventarios informacionReporteInventarios) {
		String[] informacion = new String[ENCABEZADOS.length];

		informacion[0] = informacionReporteInventarios.getEspecie();

		informacion[1] = format.format(informacionReporteInventarios
				.getFrecuenciaRelativa());
		informacion[2] = format.format(informacionReporteInventarios
				.getAbundanciaRelativa());
		informacion[3] = format.format(informacionReporteInventarios
				.getDominancia());
		informacion[4] = format.format(informacionReporteInventarios.getIvi());

		return informacion;
	}

}
