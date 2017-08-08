package co.gov.ideamredd.dao;

import java.io.FileWriter;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.ejb.EJB;

import co.gov.ideamredd.entities.Individuo;
import co.gov.ideamredd.entities.TipoBosque;
import co.gov.ideamredd.util.Util;

public class GenerarArchivosCSV {
	@EJB
	ConsultarAsociadosParcela consultarAsociadosParcela;

	private static final String archivoBiomasa = "co/gov/ideamredd/biomasa/biomasa";
	private static final ResourceBundle biomasa = ResourceBundle
			.getBundle(archivoBiomasa);

	private static String bio1[] = { "bs-T", "bms-T" };
	private static String bio2[] = { "bh-T", "bmh-T" };
	private static String bio3[] = { "bp-T" };
	private static String bio4[] = { "bh-PM", "bs-PM", "bmh-PM", "bp-PM" };
	private static String bio5[] = { "bh-MB", "bs-MB", "bmh-MB", "bp-MB" };
	private static String bio6[] = { "bh-M", "bmh-M", "bp-M" };

	private ArrayList<TipoBosque> tipoBosque;
	private ArrayList<String> info = new ArrayList<String>();
	private ArrayList<String> infoindividuos = new ArrayList<String>();
	private Integer indexFID = 1;
	private String ecuacion = ".";
	private ArrayList<Individuo> individuos;
	private String archivoParcela;
	private String archivoIndividuos;

	public void generarArchivos(ArrayList<String> codigoCampo,
			ArrayList<Integer> idParcela) {
		construirArchivoParcela(idParcela);
		construirArchivoIndividuos(codigoCampo);
	}

	private void construirArchivoParcela(ArrayList<Integer> idParcela) {
		try {
			recopilarInfoParcela(idParcela);
			archivoParcela = Util.obtenerClave("ruta", biomasa)
					+ Util.obtenerClave("nombreParcela", biomasa)
					+ Util.obtenerFechaActual().replace("/", "") + ".csv";
			FileWriter fichero = new FileWriter(archivoParcela);
			for (int i = 0; i < info.size(); i++) {
				fichero.write(info.get(i)+"\n");
			}
			fichero.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void construirArchivoIndividuos(ArrayList<String> codigoCampo) {
		try {
			recopilarInfoIndividuos(codigoCampo);
			archivoIndividuos = Util.obtenerClave("ruta", biomasa)
					+ Util.obtenerClave("nombreIndividuos", biomasa)
					+ Util.obtenerFechaActual().replace("/", "") + ".csv";
			FileWriter fichero = new FileWriter(archivoIndividuos);
			for (int i = 0; i < infoindividuos.size(); i++) {
				fichero.write(infoindividuos.get(i)+"\n");
			}
			fichero.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void recopilarInfoParcela(ArrayList<Integer> idParcela) {
		info.add("Plot,�rea,Bosque, Ecuacion");
		for (int i = 0; i < idParcela.size(); i++) {
			String infoI;
			tipoBosque = consultarAsociadosParcela
					.consultaTipoBosqueParcela(idParcela.get(i));
			Double area = consultarAsociadosParcela
					.ConsultarAreaParcela(idParcela.get(i));
			for (int j = 0; j < tipoBosque.size(); j++) {
				TipoBosque tb = tipoBosque.get(j);
				infoI = (i + 1) + "," + area + "," + tb.getTipoBosque() + ","
						+ seleccionarEcuacion(tb.getTipoBosque());
				info.add(infoI);
			}
		}
	}

	private void recopilarInfoIndividuos(ArrayList<String> codigoCampo) {
		infoindividuos.add("FID,Plot,DAP,Familia");
		for (int i = 0; i < codigoCampo.size(); i++) {
			String infoParcelas;
			individuos = consultarAsociadosParcela
					.ConsultarIndividuoParcela(codigoCampo.get(i));
			for (int j = 0; j < individuos.size(); j++) {
				Individuo ind = individuos.get(j);
				infoParcelas = indexFID + "," + (i + 1) + "," + ind.getDap1()
						+ "," + ind.getEspecie();
				indexFID++;
				infoindividuos.add(infoParcelas);
			}
		}
	}

	private String seleccionarEcuacion(String tipoBosque) {
		for (int i = 0; i < bio1.length; i++) {
			if (tipoBosque.equals(bio1[i])) {
				ecuacion = "1";
			}
		}
		for (int i = 0; i < bio2.length; i++) {
			if (tipoBosque.equals(bio2[i])) {
				ecuacion = "2";
			}
		}
		for (int i = 0; i < bio3.length; i++) {
			if (tipoBosque.equals(bio3[i])) {
				ecuacion = "3";
			}
		}
		for (int i = 0; i < bio4.length; i++) {
			if (tipoBosque.equals(bio4[i])) {
				ecuacion = "4";
			}
		}
		for (int i = 0; i < bio5.length; i++) {
			if (tipoBosque.equals(bio5[i])) {
				ecuacion = "5";
			}
		}
		for (int i = 0; i < bio6.length; i++) {
			if (tipoBosque.equals(bio6[i])) {
				ecuacion = "6";
			}
		}
		return ecuacion;
	}

	public static void main(String[] args) {
		GenerarArchivosCSV g = new GenerarArchivosCSV();
		ArrayList<Integer> p = new ArrayList<Integer>();
		p.add(246);
		ArrayList<String> cc = new ArrayList<String>();
		cc.add("P07");
		g.generarArchivos(cc, p);
	}

	public String getArchivoParcela() {
		return archivoParcela;
	}

	public void setArchivoParcela(String archivoParcela) {
		this.archivoParcela = archivoParcela;
	}

	public String getArchivoIndividuos() {
		return archivoIndividuos;
	}

	public void setArchivoIndividuos(String archivoIndividuos) {
		this.archivoIndividuos = archivoIndividuos;
	}
}
