package co.gov.ideamredd.proyecto.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;

import co.gov.ideamredd.util.Util;
import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.entities.Proyecto;

@Stateless
public class ConsultarProyecto {

	@EJB
	ConexionBD conexion;

	private Integer un_Consecutivo = -1;
	private String un_Nombre;
	private Date una_FechaInicio;
	private Date una_FechaFin;
	private Integer un_Pais = -1;
	private Integer un_Estado = -1;
	private String unDepartamento;
	private String unMunicipio;
	private String unaCar;
	private String unBosque;
	private String unaActividad;
	private Integer usuario;
	private Integer idUsuario;
	private ArrayList<Integer> un_Departamento;
	private ArrayList<Integer> un_Municipio;
	private ArrayList<Integer> una_CAR;
	private ArrayList<Integer> un_Bosque;
	private ArrayList<Integer> una_Actividad;
	private ArrayList<Proyecto> proyectos = null;
	private Connection conn;
	private ResultSet resultSet;
	private Logger log;

	public ArrayList<Proyecto> consultarProyecto() {
		try {
			conn = conexion.establecerConexion();
			proyectos = new ArrayList<Proyecto>();
			unDepartamento = un_Departamento == null ? "" : Util
					.construirStringConsulta(un_Departamento);
			unMunicipio = un_Municipio == null ? "" : Util
					.construirStringConsulta(un_Municipio);
			unaCar = una_CAR == null ? "" : Util
					.construirStringConsulta(una_CAR);
			unBosque = un_Bosque == null ? "" : Util
					.construirStringConsulta(un_Bosque);
			unaActividad = una_Actividad == null ? "" : Util
					.construirStringConsulta(una_Actividad);
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
			CallableStatement consultarProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.Consulta(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			consultarProyecto.setInt("un_Consecutivo", un_Consecutivo);
			consultarProyecto.setString("un_Nombre", un_Nombre);
			consultarProyecto
					.setString(
							"una_FechaInicio",
							una_FechaInicio == null ? null : sd
									.format(una_FechaInicio));
			consultarProyecto.setString("una_FechaFin",
					una_FechaFin == null ? null : sd.format(una_FechaFin));
			consultarProyecto.setInt("un_Pais", un_Pais);
			consultarProyecto.setInt("un_Estado", un_Estado);
			consultarProyecto.setString("un_Departamento",
					unDepartamento == null ? null : unDepartamento);
			consultarProyecto.setString("un_Municipio",
					unMunicipio == null ? null : unMunicipio);
			consultarProyecto.setString("una_CAR", unaCar == null ? null
					: unaCar);
			consultarProyecto.setString("un_Bosque", unBosque == null ? null
					: unBosque);
			consultarProyecto.setString("una_Actividad",
					unaActividad == null ? null : unaActividad);
			consultarProyecto.setInt("un_usuario", usuario != null ? usuario
					: 0);
			consultarProyecto.setInt("un_idUsuario",
					idUsuario != null ? idUsuario : -1);
			consultarProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarProyecto.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultarProyecto.execute();
			System.out.println(consultarProyecto.getObject("sentencia"));
			resultSet = (ResultSet) consultarProyecto.getObject("un_Resultado");

			while (resultSet.next()) {
				Proyecto proyecto = new Proyecto();
				proyecto.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				proyecto.setNombre((String) resultSet.getObject(2));
				proyecto.setDescripcionArea((String) resultSet.getObject(3));
				proyecto.setTipoBosques((String) resultSet.getObject(4));
				proyecto.setFechaInicio((Timestamp) resultSet.getObject(5));
				proyecto.setFechaFin((Timestamp) resultSet.getObject(6));
				proyecto.setPais(Integer.valueOf(resultSet.getObject(7)
						.toString()));
				proyecto.setEstado(Integer.valueOf(resultSet.getObject(8)
						.toString()));
				proyecto.setArea((BigDecimal) resultSet.getObject(9));
				proyecto.setCo2Reducir((BigDecimal) resultSet.getObject(10));
				proyecto.setTasaDeforestar((BigDecimal) resultSet.getObject(11));
				proyecto.setDuracionProyecto(Integer.valueOf(resultSet
						.getObject(12).toString()));
				proyecto.setPropietario(Integer.valueOf(resultSet.getObject(13)
						.toString()));
				proyecto.setPublico(Integer.valueOf(resultSet.getObject(14)
						.toString()));
				proyectos.add(proyecto);

			}
			if (proyectos.size() == 0) {
				System.out
						.println("No se encontraron resultados para la busqueda");
			}

			log.info("[consultarProyecto] Termino");
			resultSet.close();
			consultarProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[consultarProyecto] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return proyectos;
	}

	public Integer getUn_Consecutivo() {
		return un_Consecutivo;
	}

	public void setUn_Consecutivo(Integer unConsecutivo) {
		un_Consecutivo = unConsecutivo;
	}

	public String getUn_Nombre() {
		return un_Nombre;
	}

	public void setUn_Nombre(String unNombre) {
		un_Nombre = unNombre;
	}

	public Date getUna_FechaInicio() {
		return una_FechaInicio;
	}

	public void setUna_FechaInicio(Date unaFechaInicio) {
		una_FechaInicio = unaFechaInicio;
	}

	public Date getUna_FechaFin() {
		return una_FechaFin;
	}

	public void setUna_FechaFin(Date unaFechaFin) {
		una_FechaFin = unaFechaFin;
	}

	public Integer getUn_Pais() {
		return un_Pais;
	}

	public void setUn_Pais(Integer unPais) {
		un_Pais = unPais;
	}

	public Integer getUn_Estado() {
		return un_Estado;
	}

	public void setUn_Estado(Integer unEstado) {
		un_Estado = unEstado;
	}

	public ArrayList<Integer> getUn_Departamento() {
		return un_Departamento;
	}

	public void setUn_Departamento(ArrayList<Integer> unDepartamento) {
		un_Departamento = unDepartamento;
	}

	public ArrayList<Integer> getUn_Municipio() {
		return un_Municipio;
	}

	public void setUn_Municipio(ArrayList<Integer> unMunicipio) {
		un_Municipio = unMunicipio;
	}

	public ArrayList<Integer> getUna_CAR() {
		return una_CAR;
	}

	public void setUna_CAR(ArrayList<Integer> unaCAR) {
		una_CAR = unaCAR;
	}

	public ArrayList<Integer> getUn_Bosque() {
		return un_Bosque;
	}

	public void setUn_Bosque(ArrayList<Integer> unBosque) {
		un_Bosque = unBosque;
	}

	public ArrayList<Integer> getUna_Actividad() {
		return una_Actividad;
	}

	public void setUna_Actividad(ArrayList<Integer> unaActividad) {
		una_Actividad = unaActividad;
	}

	public Integer getUsuario() {
		return usuario;
	}

	public void setUsuario(Integer usuario) {
		this.usuario = usuario;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

}
