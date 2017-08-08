package co.gov.ideamredd.reportes;

import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.media.jai.JAI;

import co.gov.ideamredd.conexion.Conexion;
import co.gov.ideamredd.entities.InformacionReporteBiomasa;
import co.gov.ideamredd.entities.InformacionReporteBosque;
import co.gov.ideamredd.entities.InformacionReporteCobertura;
import co.gov.ideamredd.entities.InformacionReporteDeforestacion;

public abstract class AnalisisResultadosWPS {
	

	private ArrayList<Object> info = new ArrayList<Object>();
	private static final String archivoInformacion = "co/gov/ideamredd/reportes/reportesWPS";
	private static final ResourceBundle informacion = ResourceBundle
			.getBundle(archivoInformacion);
	private static final String car = "nombreCar";
	private static final String bosque = "nobreBosque";
	private static final String hidrografia = "nombreHidro";
	private static final Integer idCar = 1;
	private static final Integer idBosque = 2;
	private static final Integer idAreaHidro = 3;
	private static final Integer idBiomasa = 4;
	private static final Integer idReporteBosque = 1;
	private static final Integer idIndicativoBosque = 2;
	private static final Integer idReporteCobertura = 3;
	private static final Integer idIndicativoCobertura = 4;
	private static final Integer idReporteDeforestacion = 5;
	private static final Integer idReporteRegeneracion = 6;
	private static final Integer idReporteBiomasaConsolidado = 7;
	private static final Integer idReporteBiomasaWPS = 8;

	private String archivoCarga;
	private String nombreArchivo;
	private Integer tipoReporte;
	private Integer ItemCruce;
	private Integer numeroItems;
	private Integer numeroPixeles;
	private Integer periodoUno = -1;
	private Integer periodoDos = -1;
	private ArrayList<String> valores;
	private Integer cantidadItems = 0;

	public ArrayList<String> obtenerPixelesGDAL(Integer tipoReporte,
			Integer itemCruce, String nombreArchivo) {
		this.tipoReporte = tipoReporte;
		this.ItemCruce = itemCruce;
		GDALInfo info = new GDALInfo();
		info.setArchivo(nombreArchivo);
		info.setNumeroItems(obtenerNumeroItems(tipoReporte, itemCruce));
		cargarInformacionImagen();
		crearObjetosContenedores();
		info.obtenerInformacionPixeles();
		numeroPixeles = info.getNumeroPixeles();
		valores = info.getValores();
		return valores;
	}

	private Integer obtenerNumeroItems(Integer tipoReporte, Integer ItemCruce) {
		Integer cantidad = 0;
		Integer item = 0;
		
		if (ItemCruce.equals(idCar))
			cantidad = Integer.valueOf(informacion.getString("cantidadCar"));
		else if (ItemCruce.equals(idBosque))
			cantidad = Integer.valueOf(informacion.getString("cantidadBosque"));
		else if (ItemCruce.equals(idAreaHidro))
			cantidad = Integer.valueOf(informacion.getString("cantidadHidro"));
		else if (ItemCruce.equals(idBiomasa))
			cantidad = Integer.valueOf(informacion.getString("cantidadBiomasa"));
		if (tipoReporte.equals(idReporteBosque)
				|| tipoReporte.equals(idIndicativoBosque))
			item = Integer.valueOf(informacion
					.getString("BosqueNoBosqueCategorias"));
		else if (tipoReporte.equals(idReporteCobertura)
				|| tipoReporte.equals(idIndicativoCobertura))
			item = Integer.valueOf(informacion
					.getString("CambioCoberturaBoscosaCategorias"));
		else if (tipoReporte.equals(idReporteDeforestacion)
				|| tipoReporte.equals(idReporteRegeneracion))
			item = Integer.valueOf(informacion
					.getString("DeforestacionTipoCoberturaCategorias"));
		else if (tipoReporte.equals(idReporteBiomasaConsolidado))
			item = Integer.valueOf(informacion
					.getString("BiomasaConsolidadoCategotias"));
		else if (tipoReporte.equals(idReporteBiomasaWPS))
			item = Integer.valueOf(informacion
					.getString("BiomasaWPSCategotias"));
		cantidadItems = cantidad * item;
		return cantidadItems;
	}

	public DataBuffer obtenerPixeles(Integer tipoReporte, Integer ItemCruce,
			String nombreArchivo) {
		DataBuffer e=null;
		try{
		this.tipoReporte = tipoReporte;
		this.ItemCruce = ItemCruce;
		this.nombreArchivo = nombreArchivo;
		cargarInformacionImagen();
		crearObjetosContenedores();
		archivoCarga = informacion.getString("rutaImagen") + this.nombreArchivo;
		// + Util.obtenerFechaActual();
		// archivoCarga = nombreArchivo;
		RenderedImage image = JAI.create("fileload", archivoCarga);
		Raster d = image.getData();
		image = null;
		e = d.getDataBuffer();
		d = null;
		System.gc();
		}catch(Exception exc){
			exc.printStackTrace();
		}
		return e;
	}

	private void cargarInformacionImagen() {
		try {
			if (tipoReporte.equals(idReporteBosque)
					|| tipoReporte.equals(idIndicativoBosque)) {
				numeroItems = Integer.valueOf(informacion
						.getString("BosqueNoBosqueCategorias"));
			} else if (tipoReporte.equals(idReporteCobertura)
					|| tipoReporte.equals(idIndicativoCobertura)) {
				numeroItems = Integer.valueOf(informacion
						.getString("CambioCoberturaBoscosaCategorias"));
			} else if (tipoReporte.equals(idReporteDeforestacion)
					|| tipoReporte.equals(idReporteRegeneracion)) {
				numeroItems = Integer.valueOf(informacion
						.getString("DeforestacionTipoCoberturaCategorias"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void crearObjetosContenedores() {
		Integer cantidad = 0;
		String key = "";
		if (ItemCruce.equals(idCar)) {
			cantidad = Integer.valueOf(informacion.getString("cantidadCar"));
			key = car;
			// nombreArchivo += ".tif";
		} else if (ItemCruce.equals(idBosque)) {
			cantidad = Integer.valueOf(informacion.getString("cantidadBosque"));
			key = bosque;
			// nombreArchivo += ".tif";
		} else if (ItemCruce.equals(idAreaHidro)) {
			cantidad = Integer.valueOf(informacion.getString("cantidadHidro"));
			key = hidrografia;
			// nombreArchivo += ".tif";
		} else if (ItemCruce.equals(idBiomasa)) {
			cantidad = Integer.valueOf(informacion.getString("cantidadBiomasa"));
			// key = hidrografia;
			// nombreArchivo += ".tif";
		}
		if (info.size() == 0) {
			if (tipoReporte.equals(idReporteBosque)
					|| tipoReporte.equals(idIndicativoBosque))
				for (int i = 0; i < cantidad; i++) {
					InformacionReporteBosque informacionReporte = new InformacionReporteBosque();
					informacionReporte
							.setNombre(informacion.getString(key + i));
					info.add(informacionReporte);
				}
			else if (tipoReporte.equals(idReporteCobertura)
					|| tipoReporte.equals(idIndicativoCobertura))
				for (int i = 0; i < cantidad; i++) {
					InformacionReporteCobertura informacionReporte = new InformacionReporteCobertura();
					informacionReporte
							.setNombre(informacion.getString(key + i));
					info.add(informacionReporte);
				}
			else if (tipoReporte.equals(idReporteDeforestacion)
					|| tipoReporte.equals(idReporteRegeneracion))
				for (int i = 0; i < cantidad; i++) {
					InformacionReporteDeforestacion informacionReporte = new InformacionReporteDeforestacion();
					informacionReporte
							.setNombre(informacion.getString(key + i));
					info.add(informacionReporte);
				}
			else if (tipoReporte.equals(idReporteBiomasaConsolidado))
				for (int i = 0; i < cantidad; i++) {
					InformacionReporteBiomasa informacionReporte = new InformacionReporteBiomasa();
					info.add(informacionReporte);
				}
			else if (tipoReporte.equals(idReporteBiomasaWPS))
				for (int i = 0; i < cantidad; i++) {
					InformacionReporteBiomasa informacionReporte = new InformacionReporteBiomasa();
					info.add(informacionReporte);
				}
		}
	}

	public Integer getNumeroItems() {
		return numeroItems;
	}

	public void setNumeroItems(Integer numeroItems) {
		this.numeroItems = numeroItems;
	}

	public ArrayList<Object> getInfo() {
		return info;
	}

	public void setInfo(ArrayList<Object> info) {
		this.info = info;
	}

	public Integer getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(Integer tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public Integer getItemCruce() {
		return ItemCruce;
	}

	public void setItemCruce(Integer itemCruce) {
		ItemCruce = itemCruce;
	}

	public Integer getPeriodoUno() {
		return periodoUno;
	}

	public void setPeriodoUno(Integer periodoUno) {
		this.periodoUno = periodoUno;
	}

	public Integer getPeriodoDos() {
		return periodoDos;
	}

	public void setPeriodoDos(Integer periodoDos) {
		this.periodoDos = periodoDos;
	}

	public Integer getNumeroPixeles() {
		return numeroPixeles;
	}

	public void setNumeroPixeles(Integer numeroPixeles) {
		this.numeroPixeles = numeroPixeles;
	}

}
