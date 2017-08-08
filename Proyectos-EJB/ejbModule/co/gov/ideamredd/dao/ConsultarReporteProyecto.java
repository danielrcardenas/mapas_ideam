package co.gov.ideamredd.dao;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import oracle.jdbc.OracleTypes;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.entities.ConsultaProyectos;

@Stateless
public class ConsultarReporteProyecto {

	@EJB
	private CrearReporteProyecto crearReporteProyecto;

	@EJB
	private ConexionBD conexionBD;

	private Connection conn;
	private XSSFWorkbook libro;
	private String[] a = { "2000" };
	private String tipoBosque;
	private String fechaInicial;
	private String fechaFinal;
	private ArrayList<String> nombreDivision;
	private ArrayList<Integer> cantidadDivision;
	private ArrayList<String> nombreTipoEstado;
	private ArrayList<String> nombreEstado;
	private ArrayList<Integer> cantidadEstado;
	private ArrayList<String> nombreTipoActividad;
	private ArrayList<String> nombreActividad;
	private ArrayList<Integer> cantidadActividad;
	private ArrayList<String> nombreTipoTenencia;
	private ArrayList<String> nombreTenencia;
	private ArrayList<Integer> cantidadTenencia;

	public void consultarReporte() {
		cargaDatos();
		crearReporteProyecto.setNombreDivision(nombreDivision);
		crearReporteProyecto.setCantidadDivision(cantidadDivision);
		crearReporteProyecto.setNombreTipoEstado(nombreTipoEstado);
		crearReporteProyecto.setNombreEstado(nombreEstado);
		crearReporteProyecto.setCantidadEstado(cantidadEstado);
		crearReporteProyecto.setNombreTipoActividad(nombreTipoActividad);
		crearReporteProyecto.setNombreActividad(nombreActividad);
		crearReporteProyecto.setCantidadActividad(cantidadActividad);
		crearReporteProyecto.setNombreTipoTenencia(nombreTipoTenencia);
		crearReporteProyecto.setNombreTenencia(nombreTenencia);
		crearReporteProyecto.setCantidadTenencia(cantidadTenencia);
		libro = crearReporteProyecto.construirLibroReporte(
				Integer.valueOf(tipoBosque), a);
		escribirReportes("/home/harry.sanchez/ima/descargas/", "test", libro);
	}

	private void cargaDatos() {
		conn = conexionBD.establecerConexion();
		ConsultaProyectos proyectos = new ConsultaProyectos();
		nombreDivision = new ArrayList<String>();
		cantidadDivision = new ArrayList<Integer>();
		nombreTipoEstado = new ArrayList<String>();
		nombreEstado = new ArrayList<String>();
		cantidadEstado = new ArrayList<Integer>();
		nombreTipoActividad = new ArrayList<String>();
		nombreActividad = new ArrayList<String>();
		cantidadActividad = new ArrayList<Integer>();
		nombreTipoTenencia = new ArrayList<String>();
		nombreTenencia = new ArrayList<String>();
		cantidadTenencia = new ArrayList<Integer>();
		switch (Integer.valueOf(tipoBosque)) {
		case 1:
			try {
				CallableStatement consolidado = conn
						.prepareCall("{call RED_PK_PROYECTOS.consultarConsolidado(?,?,?,?,?,?)}");
				consolidado.setString("una_fechaInicial", fechaInicial);
				consolidado.setString("una_fechaFinal", fechaFinal);
				consolidado.registerOutParameter("un_resultado",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado1",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado2",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado3",
						OracleTypes.CURSOR);
				consolidado.execute();
				ResultSet r = (ResultSet) consolidado
						.getObject("un_resultado");
				ResultSet r1 = (ResultSet) consolidado
						.getObject("un_resultado1");
				ResultSet r2 = (ResultSet) consolidado
						.getObject("un_resultado2");
				ResultSet r3 = (ResultSet) consolidado
						.getObject("un_resultado3");
				while (r.next()) {
					cantidadDivision.add(r.getInt(1));
					nombreDivision.add("Colombia");
				}
				while (r1.next()) {
					nombreActividad.add(r1.getString(1));
					cantidadActividad.add(r1.getInt(2));
				}
				while (r2.next()) {
					nombreEstado.add(r2.getString(1));
					cantidadEstado.add(r2.getInt(2));
				}
				while (r3.next()) {
					nombreTenencia.add(r3.getString(1));
					cantidadTenencia.add(r3.getInt(2));
				}
				consolidado.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				CallableStatement consolidado = conn
						.prepareCall("{call RED_PK_PROYECTOS.consultarDepartamentos(?,?,?,?,?,?)}");
				consolidado.setString("una_fechaInicial", fechaInicial);
				consolidado.setString("una_fechaFinal", fechaFinal);
				consolidado.registerOutParameter("un_resultado",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado1",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado2",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado3",
						OracleTypes.CURSOR);
				consolidado.execute();
				ResultSet r = (ResultSet) consolidado
						.getObject("un_resultado");
				ResultSet r1 = (ResultSet) consolidado
						.getObject("un_resultado1");
				ResultSet r2 = (ResultSet) consolidado
						.getObject("un_resultado2");
				ResultSet r3 = (ResultSet) consolidado
						.getObject("un_resultado3");
				while (r.next()) {
					nombreDivision.add(r.getString(1));
					proyectos.setTipoReporte(nombreDivision);
					cantidadDivision.add(r.getInt(2));
					proyectos.setCantidadDivision(cantidadDivision);
				}
				while (r1.next()) {
					nombreTipoActividad.add(r1.getString(1));
					proyectos.setTipoActividad(nombreTipoActividad);
					nombreActividad.add(r1.getString(2));
					proyectos.setActividad(nombreActividad);
					cantidadActividad.add(r1.getInt(3));
					proyectos.setCantidadActividad(cantidadActividad);
				}
				while (r2.next()) {
					nombreTipoEstado.add(r2.getString(1));
					proyectos.setTipoEstado(nombreTipoEstado);
					nombreEstado.add(r2.getString(2));
					proyectos.setEstado(nombreEstado);
					cantidadEstado.add(r2.getInt(3));
					proyectos.setCantidadEstado(cantidadEstado);
				}
				while (r3.next()) {
					nombreTipoTenencia.add(r3.getString(1));
					proyectos.setTipoTenencia(nombreTipoTenencia);
					nombreTenencia.add(r3.getString(2));
					proyectos.setTenencia(nombreTenencia);
					cantidadTenencia.add(r3.getInt(3));
					proyectos.setCantidadTenencia(cantidadTenencia);
				}
				consolidado.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 3:
			try {
				CallableStatement consolidado = conn
						.prepareCall("{call RED_PK_PROYECTOS.consultarMunicipios(?,?,?,?,?,?)}");
				consolidado.setString("una_fechaInicial", fechaInicial);
				consolidado.setString("una_fechaFinal", fechaFinal);
				consolidado.registerOutParameter("un_resultado",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado1",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado2",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado3",
						OracleTypes.CURSOR);
				consolidado.execute();
				ResultSet r = (ResultSet) consolidado
						.getObject("un_resultado");
				ResultSet r1 = (ResultSet) consolidado
						.getObject("un_resultado1");
				ResultSet r2 = (ResultSet) consolidado
						.getObject("un_resultado2");
				ResultSet r3 = (ResultSet) consolidado
						.getObject("un_resultado3");
				while (r.next()) {
					nombreDivision.add(r.getString(1));
					proyectos.setTipoReporte(nombreDivision);
					cantidadDivision.add(r.getInt(2));
					proyectos.setCantidadDivision(cantidadDivision);
				}
				while (r1.next()) {
					nombreTipoActividad.add(r1.getString(1));
					proyectos.setTipoActividad(nombreTipoActividad);
					nombreActividad.add(r1.getString(2));
					proyectos.setActividad(nombreActividad);
					cantidadActividad.add(r1.getInt(3));
					proyectos.setCantidadActividad(cantidadActividad);
				}
				while (r2.next()) {
					nombreTipoEstado.add(r2.getString(1));
					proyectos.setTipoEstado(nombreTipoEstado);
					nombreEstado.add(r2.getString(2));
					proyectos.setEstado(nombreEstado);
					cantidadEstado.add(r2.getInt(3));
					proyectos.setCantidadEstado(cantidadEstado);
				}
				while (r3.next()) {
					nombreTipoTenencia.add(r3.getString(1));
					proyectos.setTipoTenencia(nombreTipoTenencia);
					nombreTenencia.add(r3.getString(2));
					proyectos.setTenencia(nombreTenencia);
					cantidadTenencia.add(r3.getInt(3));
					proyectos.setCantidadTenencia(cantidadTenencia);
				}
				consolidado.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 4:
			try {
				CallableStatement consolidado = conn
						.prepareCall("{call RED_PK_PROYECTOS.consultarCorporaciones(?,?,?,?,?,?)}");
				consolidado.setString("una_fechaInicial", fechaInicial);
				consolidado.setString("una_fechaFinal", fechaFinal);
				consolidado.registerOutParameter("un_resultado",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado1",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado2",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado3",
						OracleTypes.CURSOR);
				consolidado.execute();
				ResultSet r = (ResultSet) consolidado
						.getObject("un_resultado");
				ResultSet r1 = (ResultSet) consolidado
						.getObject("un_resultado1");
				ResultSet r2 = (ResultSet) consolidado
						.getObject("un_resultado2");
				ResultSet r3 = (ResultSet) consolidado
						.getObject("un_resultado3");
				while (r.next()) {
					nombreDivision.add(r.getString(1));
					proyectos.setTipoReporte(nombreDivision);
					cantidadDivision.add(r.getInt(2));
					proyectos.setCantidadDivision(cantidadDivision);
				}
				while (r1.next()) {
					nombreTipoActividad.add(r1.getString(1));
					proyectos.setTipoActividad(nombreTipoActividad);
					nombreActividad.add(r1.getString(2));
					proyectos.setActividad(nombreActividad);
					cantidadActividad.add(r1.getInt(3));
					proyectos.setCantidadActividad(cantidadActividad);
				}
				while (r2.next()) {
					nombreTipoEstado.add(r2.getString(1));
					proyectos.setTipoEstado(nombreTipoEstado);
					nombreEstado.add(r2.getString(2));
					proyectos.setEstado(nombreEstado);
					cantidadEstado.add(r2.getInt(3));
					proyectos.setCantidadEstado(cantidadEstado);
				}
				while (r3.next()) {
					nombreTipoTenencia.add(r3.getString(1));
					proyectos.setTipoTenencia(nombreTipoTenencia);
					nombreTenencia.add(r3.getString(2));
					proyectos.setTenencia(nombreTenencia);
					cantidadTenencia.add(r3.getInt(3));
					proyectos.setCantidadTenencia(cantidadTenencia);
				}
				consolidado.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 5:
			try {
				CallableStatement consolidado = conn
						.prepareCall("{call RED_PK_PROYECTOS.consultarTipoBosque(?,?,?,?,?,?)}");
				consolidado.registerOutParameter("un_resultado",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado1",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado2",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado3",
						OracleTypes.CURSOR);
				consolidado.execute();
				ResultSet r = (ResultSet) consolidado
						.getObject("un_resultado");
				ResultSet r1 = (ResultSet) consolidado
						.getObject("un_resultado1");
				ResultSet r2 = (ResultSet) consolidado
						.getObject("un_resultado2");
				ResultSet r3 = (ResultSet) consolidado
						.getObject("un_resultado3");
				while (r.next()) {
					nombreDivision.add(r.getString(1));
					proyectos.setTipoReporte(nombreDivision);
					cantidadDivision.add(r.getInt(2));
					proyectos.setCantidadDivision(cantidadDivision);
				}
				while (r1.next()) {
					nombreTipoActividad.add(r1.getString(1));
					proyectos.setTipoActividad(nombreTipoActividad);
					nombreActividad.add(r1.getString(2));
					proyectos.setActividad(nombreActividad);
					cantidadActividad.add(r1.getInt(3));
					proyectos.setCantidadActividad(cantidadActividad);
				}
				while (r2.next()) {
					nombreTipoEstado.add(r2.getString(1));
					proyectos.setTipoEstado(nombreTipoEstado);
					nombreEstado.add(r2.getString(2));
					proyectos.setEstado(nombreEstado);
					cantidadEstado.add(r2.getInt(3));
					proyectos.setCantidadEstado(cantidadEstado);
				}
				while (r3.next()) {
					nombreTipoTenencia.add(r3.getString(1));
					proyectos.setTipoTenencia(nombreTipoTenencia);
					nombreTenencia.add(r3.getString(2));
					proyectos.setTenencia(nombreTenencia);
					cantidadTenencia.add(r3.getInt(3));
					proyectos.setCantidadTenencia(cantidadTenencia);
				}
				consolidado.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 6:
			try {
				CallableStatement consolidado = conn
						.prepareCall("{call RED_PK_PROYECTOS.consultarAreas(?,?,?,?,?,?)}");
				consolidado.registerOutParameter("un_resultado",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado1",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado2",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado3",
						OracleTypes.CURSOR);
				consolidado.execute();
				ResultSet r = (ResultSet) consolidado
						.getObject("un_resultado");
				ResultSet r1 = (ResultSet) consolidado
						.getObject("un_resultado1");
				ResultSet r2 = (ResultSet) consolidado
						.getObject("un_resultado2");
				ResultSet r3 = (ResultSet) consolidado
						.getObject("un_resultado3");
				while (r.next()) {
					nombreDivision.add(r.getString(1));
					proyectos.setTipoReporte(nombreDivision);
					cantidadDivision.add(r.getInt(2));
					proyectos.setCantidadDivision(cantidadDivision);
				}
				while (r1.next()) {
					nombreTipoActividad.add(r1.getString(1));
					proyectos.setTipoActividad(nombreTipoActividad);
					nombreActividad.add(r1.getString(2));
					proyectos.setActividad(nombreActividad);
					cantidadActividad.add(r1.getInt(3));
					proyectos.setCantidadActividad(cantidadActividad);
				}
				while (r2.next()) {
					nombreTipoEstado.add(r2.getString(1));
					proyectos.setTipoEstado(nombreTipoEstado);
					nombreEstado.add(r2.getString(2));
					proyectos.setEstado(nombreEstado);
					cantidadEstado.add(r2.getInt(3));
					proyectos.setCantidadEstado(cantidadEstado);
				}
				while (r3.next()) {
					nombreTipoTenencia.add(r3.getString(1));
					proyectos.setTipoTenencia(nombreTipoTenencia);
					nombreTenencia.add(r3.getString(2));
					proyectos.setTenencia(nombreTenencia);
					cantidadTenencia.add(r3.getInt(3));
					proyectos.setCantidadTenencia(cantidadTenencia);
				}
				consolidado.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 7:
			try {
				CallableStatement consolidado = conn
						.prepareCall("{{call RED_PK_PROYECTOS.consultarResguardos(?,?,?,?,?,?)}}");
				consolidado.registerOutParameter("un_resultado",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado1",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado2",
						OracleTypes.CURSOR);
				consolidado.registerOutParameter("un_resultado3",
						OracleTypes.CURSOR);
				consolidado.execute();
				ResultSet r = (ResultSet) consolidado
						.getObject("un_resultado");
				ResultSet r1 = (ResultSet) consolidado
						.getObject("un_resultado1");
				ResultSet r2 = (ResultSet) consolidado
						.getObject("un_resultado2");
				ResultSet r3 = (ResultSet) consolidado
						.getObject("un_resultado3");
				while (r.next()) {
					nombreDivision.add(r.getString(1));
					proyectos.setTipoReporte(nombreDivision);
					cantidadDivision.add(r.getInt(2));
					proyectos.setCantidadDivision(cantidadDivision);
				}
				while (r1.next()) {
					nombreTipoActividad.add(r1.getString(1));
					proyectos.setTipoActividad(nombreTipoActividad);
					nombreActividad.add(r1.getString(2));
					proyectos.setActividad(nombreActividad);
					cantidadActividad.add(r1.getInt(3));
					proyectos.setCantidadActividad(cantidadActividad);
				}
				while (r2.next()) {
					nombreTipoEstado.add(r2.getString(1));
					proyectos.setTipoEstado(nombreTipoEstado);
					nombreEstado.add(r2.getString(2));
					proyectos.setEstado(nombreEstado);
					cantidadEstado.add(r2.getInt(3));
					proyectos.setCantidadEstado(cantidadEstado);
				}
				while (r3.next()) {
					nombreTipoTenencia.add(r3.getString(1));
					proyectos.setTipoTenencia(nombreTipoTenencia);
					nombreTenencia.add(r3.getString(2));
					proyectos.setTenencia(nombreTenencia);
					cantidadTenencia.add(r3.getInt(3));
					proyectos.setCantidadTenencia(cantidadTenencia);
				}
				consolidado.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 8:

			break;
		}
	}

	public boolean escribirReportes(String ruta, String nombreArchivo,
			XSSFWorkbook libro) {
		boolean esEscrito = true;
		try {
			String path = ruta + nombreArchivo + ".xlsx";
			FileOutputStream elFichero = new FileOutputStream(path);
			libro.write(elFichero);
			elFichero.close();
		} catch (Exception e) {
			e.printStackTrace();
			esEscrito = false;
		}
		return esEscrito;
	}

	public String getTipoBosque() {
		return tipoBosque;
	}

	public void setTipoBosque(String tipoBosque) {
		this.tipoBosque = tipoBosque;
	}

	public String getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

}
