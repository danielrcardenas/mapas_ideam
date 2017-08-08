// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import co.gov.ideamredd.mbc.conexion.ParametroNoBean;
import co.gov.ideamredd.mbc.entities.DatosAlertas;
import co.gov.ideamredd.mbc.entities.DatosExcelAlertas;
import co.gov.ideamredd.mbc.entities.EntidadAlertas;
import co.gov.ideamredd.mbc.entities.ReporteAlertas;
import co.gov.ideamredd.mbc.entities.Nucleo;

/**
 * Métodos para cargar datos de alertas tempranas
 * 
 * @author Julio Sanchez y Santiago Hernández Plazas
 * 
 */
public class CargarDatosAlertasTempranas {

	ParametroNoBean	parametro;

	/**
	 * Metodo para obtener reportes de alertas.
	 */
	public ArrayList<ReporteAlertas> obtenerReportesHistorico() {
		ArrayList<ReporteAlertas> reportes = null;

		parametro = new ParametroNoBean();
		String ruta = parametro.getParametro("ruta_reportes") + "/AlertasTempranas/";

		File carpeta = new File(ruta);

		if (carpeta.exists()) {
			File[] ficheros = carpeta.listFiles();
			Arrays.sort(ficheros);
			reportes = new ArrayList<ReporteAlertas>();
			for (int x = 0; x < ficheros.length; x++) {
				if (!ficheros[x].getName().contains("Temp")) {
					ReporteAlertas reporte = new ReporteAlertas();
					reporte.setNombre(ficheros[x].getName());
					reporte.setPeriodo(Integer.parseInt(ficheros[x].getName().toLowerCase().replaceAll("boletin", "").replace(".pdf", "")));
					reportes.add(reporte);
				}
			}
		}
		else {
			reportes = null;
		}

		return reportes;
	}

	/**
	 * Metodo para consultar los datos de alertas temp.
	 */
	public DatosAlertas obtenerDatosAlertas() {
		DatosAlertas datos = new DatosAlertas();
		parametro = new ParametroNoBean();

		String archTexto = parametro.getParametro("ruta_reportes") + "/AlertasTempranas/Temp/datos_reporte.txt";

		File arch = new File(archTexto);
		if (!arch.exists()) {
			return datos;
		}

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archTexto), "UTF8"));
			String curline = "";
			String todo = "";
			while ((curline = br.readLine()) != null) {
				todo = todo + curline;
			}

			String[] cadenasDatos = todo.split("#");

			for (int i = 0; i < cadenasDatos.length; i++) {
				if (cadenasDatos[i].equals("NOMBRE")) {
					datos.setNombre(cadenasDatos[i + 1]);
				}
				if (cadenasDatos[i].equals("PERIODO")) {
					datos.setPeriodo(cadenasDatos[i + 1]);
				}
				if (cadenasDatos[i].equals("NUMERO")) {
					datos.setNumero(Integer.parseInt(cadenasDatos[i + 1]));
				}
				if (cadenasDatos[i].equals("FECHA")) {
					datos.setFecha(cadenasDatos[i + 1]);
				}
				if (cadenasDatos[i].equals("DESCRIPCION")) {
					datos.setDescripcion(cadenasDatos[i + 1]);
				}
				if (cadenasDatos[i].equals("DIRECCION_CONTACTO")) {
					datos.setDireccionContacto(cadenasDatos[i + 1]);
				}
				if (cadenasDatos[i].equals("INFORMACION_COMPLEMENTARIA")) {
					datos.setInformacionComplementaria(cadenasDatos[i + 1]);
				}
				if (cadenasDatos[i].equals("CREDITOS")) {
					datos.setCreditos(cadenasDatos[i + 1]);
				}
				if (cadenasDatos[i].equals("PROXIMAS_PUBLICACIONES")) {
					datos.setProximasPublicaciones(cadenasDatos[i + 1]);
				}
				if (cadenasDatos[i].equals("DESCRIPCION_DEPARTAMENTOS")) {
					datos.setDesccripcionDepartamentos(cadenasDatos[i + 1]);
				}
				if (cadenasDatos[i].equals("DESCRIPCION_REGIONES_NATURALES")) {
					datos.setDescripcionRegiones(cadenasDatos[i + 1]);
				}
				if (cadenasDatos[i].equals("DESCRIPCION_AUTORIDADES")) {
					datos.setDescripcionAutoridades(cadenasDatos[i + 1]);
				}
				if (cadenasDatos[i].equals("DESCRIPCION_PERSISTENCIA")) {
					datos.setDescripcionPersistencia(cadenasDatos[i + 1]);
				}
				if (cadenasDatos[i].equals("DESCRIPCION_CONCENTRACION")) {
					datos.setDescripcionConcentracion(cadenasDatos[i + 1]);
				}
				if (cadenasDatos[i].equals("DESCRIPCION_NUCLEOS")) {
					datos.setDescripcionNucleos(cadenasDatos[i + 1]);
				}

			}

			br.close();
			return datos;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return datos;
	}

	/**
	 * Metodo para consultar datos de archivo excel.
	 */
	public DatosExcelAlertas obtenerDatosExcel() {
		DatosExcelAlertas datos = new DatosExcelAlertas();
		ArrayList<EntidadAlertas> entidades = new ArrayList<EntidadAlertas>();
		ArrayList<Nucleo> nucleos = new ArrayList<Nucleo>();
		parametro = new ParametroNoBean();
		String archTexto = parametro.getParametro("ruta_reportes") + "/AlertasTempranas/Temp/tabla_datos.xlsx";

		File arch = new File(archTexto);
		if (!arch.exists()) {
			return datos;
		}

		try {
			File file = new File(archTexto);
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook wb = new XSSFWorkbook(fis);

			XSSFSheet sh = wb.getSheetAt(0);

			Row row = null;
			entidades = new ArrayList<EntidadAlertas>();
			for (int i = 2; i < 34; i++) {
				EntidadAlertas entidad = new EntidadAlertas();
				row = sh.getRow(i);
				entidad.setNombre(row.getCell(27).getStringCellValue());
				entidad.setPorcentaje(row.getCell(30).getNumericCellValue());
				entidad.setPorcentajeAcumulado(row.getCell(31).getNumericCellValue());
				entidades.add(entidad);
			}
			datos.setDepartamentos(entidades);

			row = null;
			entidades = new ArrayList<EntidadAlertas>();
			for (int i = 2; i < 7; i++) {
				EntidadAlertas entidad = new EntidadAlertas();
				row = sh.getRow(i);
				entidad.setNombre(row.getCell(9).getStringCellValue());
				entidad.setPorcentaje(row.getCell(12).getNumericCellValue());
				entidad.setPorcentajeAcumulado(row.getCell(13).getNumericCellValue());
				entidades.add(entidad);
			}
			datos.setRegiones(entidades);

			row = null;
			entidades = new ArrayList<EntidadAlertas>();
			for (int i = 2; i < 36; i++) {
				EntidadAlertas entidad = new EntidadAlertas();
				row = sh.getRow(i);
				entidad.setNombre(row.getCell(16).getStringCellValue());
				entidad.setPorcentaje(row.getCell(19).getNumericCellValue());
				entidad.setPorcentajeAcumulado(row.getCell(20).getNumericCellValue());
				entidades.add(entidad);
			}
			datos.setAutoridades(entidades);

			XSSFSheet sh2 = wb.getSheetAt(1);

			String numero_nucleo = "";
			String nombre_nucleo = "";
			String descripcion_nucleo = "";

			row = null;
			nucleos = new ArrayList<Nucleo>();
			for (int i = 1; i <= 7; i++) {
				Nucleo nucleo = new Nucleo();
				row = sh2.getRow(i);
				numero_nucleo = row.getCell(0).getStringCellValue();
				nucleo.setNumero(numero_nucleo);
				nombre_nucleo = row.getCell(1).getStringCellValue();
				nucleo.setNombre(nombre_nucleo);
				descripcion_nucleo = row.getCell(2).getStringCellValue();
				nucleo.setDescripcion(descripcion_nucleo);
				nucleos.add(nucleo);
			}
			datos.setNucleos(nucleos);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return datos;
	}

}
