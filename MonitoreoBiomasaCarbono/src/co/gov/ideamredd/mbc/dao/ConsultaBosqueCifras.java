package co.gov.ideamredd.mbc.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.sql.DataSource;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;

import co.gov.ideamredd.mbc.conexionBD.ConexionBD;
import co.gov.ideamredd.mbc.entities.BosqueArea;
import co.gov.ideamredd.mbc.entities.InfoCifras;
import co.gov.ideamredd.mbc.entities.InformacionReporteBiomasa;
import co.gov.ideamredd.mbc.entities.InformacionReporteBiomasaAreaHidro;
import co.gov.ideamredd.mbc.entities.InformacionReporteCobertura;
import co.gov.ideamredd.util.SMBC_Log;

public class ConsultaBosqueCifras {

	private static Connection conn;
	private static DataSource dataSource = ConexionBD.getConnection();

	private static Logger log;

	public static ArrayList<InfoCifras> consultarBosqNoBosqAreasHidro() {
		ArrayList<InfoCifras> cifrasAreasHid = new ArrayList<InfoCifras>();
		try {
			conn = dataSource.getConnection();
			log = SMBC_Log.Log(ConsultaBosqueCifras.class);
			CallableStatement consultarCifras = conn
					.prepareCall("{call RED_PK_REPORTES.CONSULTARULTIMOREPOIBOSQUE(?,?)}");
			consultarCifras.setInt("UNA_DIVISION", 3);
			consultarCifras.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarCifras.execute();

			ResultSet resultSet = (ResultSet) consultarCifras
					.getObject("un_Resultado");

			InfoCifras amazonas = new InfoCifras();
			InfoCifras caribe = new InfoCifras();
			InfoCifras orinoco = new InfoCifras();
			InfoCifras magCauca = new InfoCifras();
			InfoCifras pacifico = new InfoCifras();

			while (resultSet.next()) {

				if (resultSet.getString("INBS_NOMBRE").equals("Amazonas")) {
					amazonas.setBosque(resultSet.getDouble(1));
					amazonas.setNoBosque(resultSet.getDouble(2));
					amazonas.setSinInfo(resultSet.getDouble(3));
					amazonas.setNombre(resultSet.getString(4));
				} else if (resultSet.getString("INBS_NOMBRE").equals("Caribe")) {
					caribe.setBosque(resultSet.getDouble(1));
					caribe.setNoBosque(resultSet.getDouble(2));
					caribe.setSinInfo(resultSet.getDouble(3));
					caribe.setNombre(resultSet.getString(4));
				} else if (resultSet.getString("INBS_NOMBRE").equals("Orinoco")) {
					orinoco.setBosque(resultSet.getDouble(1));
					orinoco.setNoBosque(resultSet.getDouble(2));
					orinoco.setSinInfo(resultSet.getDouble(3));
					orinoco.setNombre(resultSet.getString(4));
				} else if (resultSet.getString("INBS_NOMBRE").equals(
						"Magdalena Cauca")) {
					magCauca.setBosque(resultSet.getDouble(1));
					magCauca.setNoBosque(resultSet.getDouble(2));
					magCauca.setSinInfo(resultSet.getDouble(3));
					magCauca.setNombre(resultSet.getString(4));
				} else if (resultSet.getString("INBS_NOMBRE")
						.equals("Pacifico")) {
					pacifico.setBosque(resultSet.getDouble(1));
					pacifico.setNoBosque(resultSet.getDouble(2));
					pacifico.setSinInfo(resultSet.getDouble(3));
					pacifico.setNombre(resultSet.getString(4));
				}

			}

			cifrasAreasHid.add(amazonas);
			cifrasAreasHid.add(caribe);
			cifrasAreasHid.add(orinoco);
			cifrasAreasHid.add(magCauca);
			cifrasAreasHid.add(pacifico);

			resultSet.close();
			conn.close();
			log.info("[consultarCifrasDepartamentos] Termino");
		} catch (Exception e) {
			log.error("[consultarCifrasDepartamentos] Fallo");
			e.printStackTrace();
		}

		return cifrasAreasHid;

	}

	public static ArrayList<Double> consultarPorcentCifrasConsolidado() {
		ArrayList<Double> cifrasConsolidado = new ArrayList<Double>();
		try {
			conn = dataSource.getConnection();
			log = SMBC_Log.Log(ConsultaBosqueCifras.class);
			CallableStatement consultarCifras = conn
					.prepareCall("{call RED_PK_REPORTES.CONSULTARULTIMOREPOIBOSQUE(?,?)}");
			consultarCifras.setInt("UNA_DIVISION", 1);
			consultarCifras.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarCifras.execute();

			ResultSet resultSet = (ResultSet) consultarCifras
					.getObject("un_Resultado");

			Double Sb = 0.0, Sbnb = 0.0, Ssi = 0.0;
			while (resultSet.next()) {
				Sb = Sb + resultSet.getDouble(1);
				Sbnb = Sbnb + resultSet.getDouble(2);
				Ssi = Ssi + resultSet.getDouble(3);
			}
			cifrasConsolidado.add(definirDecimales(
					(Sb / (Sb + Sbnb + Ssi)) * 100, 3));
			cifrasConsolidado.add(definirDecimales(
					(Sbnb / (Sb + Sbnb + Ssi)) * 100, 3));
			cifrasConsolidado.add(definirDecimales(
					(Ssi / (Sb + Sbnb + Ssi)) * 100, 3));

			resultSet.close();
			conn.close();
			log.info("[consultarCifrasConsolidado] Termino");
		} catch (Exception e) {
			log.error("[consultarCifrasConsolidado] Fallo");
			e.printStackTrace();
		}

		return cifrasConsolidado;
	}

	public static ArrayList<Double> consultarCifrasConsolidado() {
		ArrayList<Double> cifrasConsolidado = new ArrayList<Double>();
		try {
			conn = dataSource.getConnection();
			log = SMBC_Log.Log(ConsultaBosqueCifras.class);
			CallableStatement consultarCifras = conn
					.prepareCall("{call RED_PK_REPORTES.CONSULTARULTIMOREPOIBOSQUE(?,?)}");
			consultarCifras.setInt("UNA_DIVISION", 1);
			consultarCifras.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarCifras.execute();

			ResultSet resultSet = (ResultSet) consultarCifras
					.getObject("un_Resultado");

			Double Sb = 0.0, Sbnb = 0.0, Ssi = 0.0;
			while (resultSet.next()) {
				Sb = Sb + resultSet.getDouble(1);
				Sbnb = Sbnb + resultSet.getDouble(2);
				Ssi = Ssi + resultSet.getDouble(3);
			}
			cifrasConsolidado.add(definirDecimales(Sb, 3));
			cifrasConsolidado.add(definirDecimales(Sbnb, 3));
			cifrasConsolidado.add(definirDecimales(Ssi, 3));

			resultSet.close();
			conn.close();
			log.info("[consultarCifrasConsolidado] Termino");
		} catch (Exception e) {
			log.error("[consultarCifrasConsolidado] Fallo");
			e.printStackTrace();
		}

		return cifrasConsolidado;
	}

	public static ArrayList<InformacionReporteBiomasa> consultarInfoConsolidadoBiomasa() {
		ArrayList<InformacionReporteBiomasa> infoBiomasa = null;
		try {
			BigDecimal C = new BigDecimal(0);
			BigDecimal Cj = new BigDecimal(0);
			BigDecimal CO2 = new BigDecimal(0);
			consultarAreaHidrografica();
			log = SMBC_Log.Log(ConsultaBosqueCifras.class);
			conn = dataSource.getConnection();
			infoBiomasa = new ArrayList<InformacionReporteBiomasa>();

			CallableStatement consultarInfoBosque = conn
					.prepareCall("{call RED_PK_REPORTES.CONSULTAR_ULTI_INFOBIOMASA(?)}");
			consultarInfoBosque.registerOutParameter("UN_RESULTADO",
					OracleTypes.CURSOR);
			consultarInfoBosque.execute();
			ResultSet resultSet = (ResultSet) consultarInfoBosque
					.getObject("UN_RESULTADO");
			while (resultSet.next()) {
				InformacionReporteBiomasa info = new InformacionReporteBiomasa();
				info.setNombreTipoBosque(resultSet.getString("NOMBRE_BOSQUE"));
				info.setArea(resultSet.getDouble("AREA"));
				C = C.add(resultSet.getBigDecimal("C"));
				Cj = Cj.add(resultSet.getBigDecimal("Cj"));
				CO2 = CO2.add(resultSet.getBigDecimal("CO2"));
				infoBiomasa.add(info);
			}

			infoBiomasa.get(0).setC(C);
			infoBiomasa.get(0).setCarbono(Cj);
			infoBiomasa.get(0).setCO2(CO2);

			log.info("[consultarInfoBiomasa] Termino");
			resultSet.close();
			consultarInfoBosque.close();
			conn.close();

		} catch (Exception e) {
			log.error("[consultarInfoBiomasa] Fallo");
			e.printStackTrace();
		}
		return infoBiomasa;
	}

	public static ArrayList<InformacionReporteBiomasaAreaHidro> consultarInfoBiomasa() {
		ArrayList<InformacionReporteBiomasaAreaHidro> infoBiomasa = null;
		try {
			log = SMBC_Log.Log(ConsultaBosqueCifras.class);
			conn = dataSource.getConnection();
			infoBiomasa = new ArrayList<InformacionReporteBiomasaAreaHidro>();

			CallableStatement consultarInfoBosque = conn
					.prepareCall("{call RED_PK_REPORTES.CONSULTI_INFOBIOMASA_AREAH(?)}");
			consultarInfoBosque.registerOutParameter("UN_RESULTADO",
					OracleTypes.CURSOR);
			consultarInfoBosque.execute();
			ResultSet resultSet = (ResultSet) consultarInfoBosque
					.getObject("UN_RESULTADO");

			InformacionReporteBiomasaAreaHidro amazonas = new InformacionReporteBiomasaAreaHidro();
			amazonas.bosques = new ArrayList<BosqueArea>();
			InformacionReporteBiomasaAreaHidro caribe = new InformacionReporteBiomasaAreaHidro();
			caribe.bosques = new ArrayList<BosqueArea>();
			InformacionReporteBiomasaAreaHidro orinoco = new InformacionReporteBiomasaAreaHidro();
			orinoco.bosques = new ArrayList<BosqueArea>();
			InformacionReporteBiomasaAreaHidro magCauca = new InformacionReporteBiomasaAreaHidro();
			magCauca.bosques = new ArrayList<BosqueArea>();
			InformacionReporteBiomasaAreaHidro pacifico = new InformacionReporteBiomasaAreaHidro();
			pacifico.bosques = new ArrayList<BosqueArea>();

			while (resultSet.next()) {

				if (resultSet.getString("Area_Hidrografica").equals("Amazonas")) {
					amazonas.bosques.add(new BosqueArea(resultSet
							.getString("Nombre_Bosque"), resultSet
							.getDouble("Area")));
					amazonas.C = amazonas.C + resultSet.getDouble("C");
					amazonas.Cj = amazonas.Cj + resultSet.getDouble("Cj");
					amazonas.CO2 = amazonas.CO2 + resultSet.getDouble("CO2");
				} else if (resultSet.getString("Area_Hidrografica").equals(
						"Caribe")) {
					caribe.bosques.add(new BosqueArea(resultSet
							.getString("Nombre_Bosque"), resultSet
							.getDouble("Area")));
					caribe.C = caribe.C + resultSet.getDouble("C");
					caribe.Cj = caribe.Cj + resultSet.getDouble("Cj");
					caribe.CO2 = caribe.CO2 + resultSet.getDouble("CO2");
				} else if (resultSet.getString("Area_Hidrografica").equals(
						"Orinoco")) {
					orinoco.bosques.add(new BosqueArea(resultSet
							.getString("Nombre_Bosque"), resultSet
							.getDouble("Area")));
					orinoco.C = orinoco.C + resultSet.getDouble("C");
					orinoco.Cj = orinoco.Cj + resultSet.getDouble("Cj");
					orinoco.CO2 = orinoco.CO2 + resultSet.getDouble("CO2");
				} else if (resultSet.getString("Area_Hidrografica").equals(
						"Magdalena Cauca")) {
					magCauca.bosques.add(new BosqueArea(resultSet
							.getString("Nombre_Bosque"), resultSet
							.getDouble("Area")));
					magCauca.C = magCauca.C + resultSet.getDouble("C");
					magCauca.Cj = magCauca.Cj + resultSet.getDouble("Cj");
					magCauca.CO2 = magCauca.CO2 + resultSet.getDouble("CO2");
				} else if (resultSet.getString("Area_Hidrografica").equals(
						"Pacifico")) {
					pacifico.bosques.add(new BosqueArea(resultSet
							.getString("Nombre_Bosque"), resultSet
							.getDouble("Area")));
					pacifico.C = pacifico.C + resultSet.getDouble("C");
					pacifico.Cj = pacifico.Cj + resultSet.getDouble("Cj");
					pacifico.CO2 = pacifico.CO2 + resultSet.getDouble("CO2");
				}
			}

			infoBiomasa.add(amazonas);
			infoBiomasa.add(caribe);
			infoBiomasa.add(orinoco);
			infoBiomasa.add(magCauca);
			infoBiomasa.add(pacifico);

			log.info("[consultarInfoBiomasa] Termino");
			resultSet.close();
			consultarInfoBosque.close();
			conn.close();

		} catch (Exception e) {
			log.error("[consultarInfoBiomasa] Fallo");
			e.printStackTrace();
		}
		return infoBiomasa;
	}

	public static ArrayList<InformacionReporteCobertura> consultarInfoCoberturaAH() {
		ArrayList<InformacionReporteCobertura> infoCoberturas = null;
		try {
			log = SMBC_Log.Log(ConsultaBosqueCifras.class);
			conn = dataSource.getConnection();
			infoCoberturas = new ArrayList<InformacionReporteCobertura>();

			CallableStatement consultarInfoBosque = conn
					.prepareCall("{call RED_PK_REPORTES.CONSULTI_INFOCOBERTURA_AH(?)}");
			consultarInfoBosque.registerOutParameter("UN_RESULTADO",
					OracleTypes.CURSOR);
			consultarInfoBosque.execute();
			ResultSet resultSet = (ResultSet) consultarInfoBosque
					.getObject("UN_RESULTADO");

			InformacionReporteCobertura amazonas = new InformacionReporteCobertura();
			InformacionReporteCobertura caribe = new InformacionReporteCobertura();
			InformacionReporteCobertura orinoco = new InformacionReporteCobertura();
			InformacionReporteCobertura magCauca = new InformacionReporteCobertura();
			InformacionReporteCobertura pacifico = new InformacionReporteCobertura();

			while (resultSet.next()) {

				if (resultSet.getString("NOMBRE").equals("Amazonas")) {
					amazonas.setDeforestacion(resultSet
							.getBigDecimal("DEFOREST"));
					amazonas.setReforestacion(resultSet
							.getBigDecimal("REGENER"));
				} else if (resultSet.getString("NOMBRE").equals("Caribe")) {
					caribe.setDeforestacion(resultSet.getBigDecimal("DEFOREST"));
					caribe.setReforestacion(resultSet.getBigDecimal("REGENER"));
				} else if (resultSet.getString("NOMBRE").equals("Orinoco")) {
					orinoco.setDeforestacion(resultSet
							.getBigDecimal("DEFOREST"));
					orinoco.setReforestacion(resultSet.getBigDecimal("REGENER"));
				} else if (resultSet.getString("NOMBRE").equals(
						"Magdalena Cauca")) {
					magCauca.setDeforestacion(resultSet
							.getBigDecimal("DEFOREST"));
					magCauca.setReforestacion(resultSet
							.getBigDecimal("REGENER"));
				} else if (resultSet.getString("NOMBRE").equals("Pacifico")) {
					pacifico.setDeforestacion(resultSet
							.getBigDecimal("DEFOREST"));
					pacifico.setReforestacion(resultSet
							.getBigDecimal("REGENER"));
				}

			}

			infoCoberturas.add(amazonas);
			infoCoberturas.add(caribe);
			infoCoberturas.add(orinoco);
			infoCoberturas.add(magCauca);
			infoCoberturas.add(pacifico);

			log.info("[consultarInfoBiomasa] Termino");
			resultSet.close();
			consultarInfoBosque.close();
			conn.close();

		} catch (Exception e) {
			log.error("[consultarInfoBiomasa] Fallo");
			e.printStackTrace();
		}
		return infoCoberturas;
	}

	public static ArrayList<InformacionReporteCobertura> consultarInfoCoberturaConsolidado() {
		ArrayList<InformacionReporteCobertura> infoBiomasa = null;
		try {
			consultarAreaHidrografica();
			log = SMBC_Log.Log(ConsultaBosqueCifras.class);
			conn = dataSource.getConnection();
			infoBiomasa = new ArrayList<InformacionReporteCobertura>();

			CallableStatement consultarInfoBosque = conn
					.prepareCall("{call RED_PK_REPORTES.CONSULTAR_ULTI_INFOCOBERTURA(?)}");
			consultarInfoBosque.registerOutParameter("UN_RESULTADO",
					OracleTypes.CURSOR);
			consultarInfoBosque.execute();
			ResultSet resultSet = (ResultSet) consultarInfoBosque
					.getObject("UN_RESULTADO");
			while (resultSet.next()) {
				InformacionReporteCobertura info = new InformacionReporteCobertura();
				info.setDeforestacion(resultSet.getBigDecimal("DEFOREST"));
				info.setReforestacion(resultSet.getBigDecimal("REGENER"));
				infoBiomasa.add(info);
			}

			log.info("[consultarInfoBiomasa] Termino");
			resultSet.close();
			consultarInfoBosque.close();
			conn.close();

		} catch (Exception e) {
			log.error("[consultarInfoBiomasa] Fallo");
			e.printStackTrace();
		}
		return infoBiomasa;
	}

	public static ArrayList<BosqueArea> listaBosqueArea(
			ArrayList<InformacionReporteBiomasa> datos) {

		ArrayList<BosqueArea> resultado = null;

		InformacionReporteBiomasa datos2 = datos.get(0);

		return resultado;
	}

	public static void consultarAreaHidrografica() {
		ArrayList<Integer> idsAreaHidrografica = new ArrayList<Integer>();
		ArrayList<String> nombresAreaHidrografica = new ArrayList<String>();
		try {
			log = SMBC_Log.Log(ConsultaBosqueCifras.class);
			conn = dataSource.getConnection();
			CallableStatement consultarAreaH = conn
					.prepareCall("{call RED_PK_REPORTES.CONSULTAR_AREAHIDROGRAFICA(?)}");
			consultarAreaH.registerOutParameter("UN_RESULTADO",
					OracleTypes.CURSOR);
			consultarAreaH.execute();
			ResultSet resultSet = (ResultSet) consultarAreaH
					.getObject("UN_RESULTADO");

			idsAreaHidrografica.clear();
			nombresAreaHidrografica.clear();
			while (resultSet.next()) {
				idsAreaHidrografica.add(resultSet.getInt(1));
				nombresAreaHidrografica.add(resultSet.getString(2));
			}

			log.info("[consultarAreaHidrografica] Termino");
			resultSet.close();
			consultarAreaH.close();
			conn.close();

		} catch (Exception e) {
			log.error("[consultarAreaHidrografica] Fallo");
			e.printStackTrace();
		}
	}

	public static double definirDecimales(double numero, int decimales) {
		return Math.round(numero * Math.pow(10, decimales))
				/ Math.pow(10, decimales);
	}

}
