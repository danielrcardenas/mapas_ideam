// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.proyecto.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import co.gov.ideamredd.mbc.conexion.ConexionBD;

import co.gov.ideamredd.proyecto.entities.ActividadRedd;
import co.gov.ideamredd.proyecto.entities.Metodologia;
import co.gov.ideamredd.proyecto.entities.ReporteProyecto;
import co.gov.ideamredd.proyecto.entities.Tenencia;
import co.gov.ideamredd.util.SMBC_Log;

@Stateless 
public class ConsultarDatosReporte {
	
	private Logger log;
	private Connection conn;
	
	@EJB
	private ConexionBD conexionBD;
	
    /**
     * Método usado para obtener los proyectos registrados en el sistema.
     */
	public ArrayList<ReporteProyecto> obtenerProyectos(){
		ArrayList<ReporteProyecto> proyectos = new ArrayList<ReporteProyecto>();
		ReporteProyecto proyecto;
		
		try {
			log = SMBC_Log.Log(ConsultarDatosReporte.class);
			conn = conexionBD.establecerConexion();
			CallableStatement consultarProyectos = conn
					.prepareCall("{call RED_PK_PROYECTOS.consultarTodosProyectosR(?)}");
			consultarProyectos.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarProyectos.execute();
			
			ResultSet resultSet = (ResultSet) consultarProyectos.getObject("un_Resultado");
			
			while (resultSet.next()) {
				
				proyecto = new ReporteProyecto();
			
				proyecto.setCodigo_Proyecto(resultSet.getInt("PRYC_CONSECUTIVO"));
				proyecto.setNombre_Proyecto(resultSet.getString("PRYC_NOMBRE"));
				proyecto.setArea_Proyecto(Double.parseDouble(resultSet.getString("PRYC_AREA")));
				proyecto.setNombre_Pais(resultSet.getString("NOMBRE_PAIS"));
				proyecto.setNombre_Departamento(resultSet.getString("NOMBRE_DEPTO"));
				proyecto.setNombre_Municipio(resultSet.getString("NOMBRE_MUNI"));
				proyecto.setTipo_De_Bosque(resultSet.getString("TPBS_DESCRIPCION"));
				proyecto.setCAR(resultSet.getString("CAR"));
				proyecto.setTipo_De_Proyecto(resultSet.getString("ESPR_NOMBRE"));
				proyecto.setFecha_De_Inicio(resultSet.getDate("PRYC_FECHA_INICIO"));
				proyecto.setFecha_De_Finalizacion(resultSet.getDate("PRYC_FECHA_FIN"));
				proyecto.setTenencia_De_La_Tierra(consultarTenencia(resultSet.getInt("PRYC_CONSECUTIVO")).getDescripcion());
				proyecto.setActividad_REDD(consultarActividadProyecto(resultSet.getInt("PRYC_CONSECUTIVO")).get(0).getNombre());
				proyecto.setCantidad_De_CO2_A_Reducir(resultSet.getDouble("PRYC_CO2_REDUCIR"));
				proyecto.setTasa_Deforestacion_A_Reducir(resultSet.getDouble("PRYC_TASA_DEFORESTAR"));
				proyecto.setNombre_Metodologia(consultarMetodologia(resultSet.getInt("PRYC_CONSECUTIVO")).getMetodologiaNombre());
				proyecto.setDescripcion_Metodologia(consultarMetodologia(resultSet.getInt("PRYC_CONSECUTIVO")).getDescripcion());
				proyecto.setEcuacion_Metodologia(consultarMetodologia(resultSet.getInt("PRYC_CONSECUTIVO")).getMetodologiaEcuacion());
				
				proyectos.add(proyecto);	
			}
			resultSet.close();
			consultarProyectos.close();
			conn.close();
			log.info("[obtenerProyectos] Termino");
		} catch (Exception e) {
			log.info("[obtenerProyectos] Fallo :: "+e.getMessage());
			e.printStackTrace();
		}
		
		return proyectos;
	}
	
	
    /**
     * Método usado para consultar la tenencia de un proyecto.
     */
	public Tenencia consultarTenencia(Integer idProyecto) {
		Tenencia tenencia = new Tenencia();
		try {
			log = SMBC_Log.Log(ConsultarDatosReporte.class);
			CallableStatement consultarTenenciaProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.TenenciaProyecto_Consulta(?,?)}");
			consultarTenenciaProyecto.setInt("un_Proyecto", idProyecto);
			consultarTenenciaProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarTenenciaProyecto.execute();
			ResultSet resultSet = (ResultSet) consultarTenenciaProyecto
					.getObject("un_Resultado");
			while (resultSet.next()) {
				tenencia.setConsecutivo(resultSet.getInt(1));
				tenencia.setDescripcion(resultSet.getString(2));
			}
			resultSet.close();
			consultarTenenciaProyecto.close();
			log.info("[consultarTenencia] Termino");
		} catch (Exception e) {
			log.info("[consultarTenencia] Fallo");
			e.printStackTrace();
		}
		return tenencia;
	}
	
    /**
     * Método usado para consultar la actividad de un proyecto.
     */
	public ArrayList<ActividadRedd> consultarActividadProyecto(
			Integer idProyecto) {
		ArrayList<ActividadRedd> actividades = new ArrayList<ActividadRedd>();
		try {
			log = SMBC_Log.Log(ConsultarDatosReporte.class);
			CallableStatement consultarActividadProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.ActividadProyecto_Consulta(?,?)}");
			consultarActividadProyecto.setInt("un_Proyecto", idProyecto);
			consultarActividadProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarActividadProyecto.execute();
			ResultSet resultSet = (ResultSet) consultarActividadProyecto
					.getObject("un_Resultado");
			while (resultSet.next()) {
				ActividadRedd actividadRedd = new ActividadRedd();
				actividadRedd.setConsecutivo(Integer.valueOf(resultSet
						.getObject(1).toString()));
				actividadRedd.setNombre((String) resultSet.getObject(2));
				actividades.add(actividadRedd);
			}
			resultSet.close();
			consultarActividadProyecto.close();
			log.info("[consultarActividadProyecto] Termino");
		} catch (Exception e) {
			log.info("[consultarActividadProyecto] Fallo");
			e.printStackTrace();
		}
		return actividades;
	}
	
    /**
     * Método usado para consultar la metodologia de un proyecto.
     */
	public Metodologia consultarMetodologia(Integer idProyecto) {
		Metodologia metodologia = new Metodologia();
		try {
			log = SMBC_Log.Log(ConsultarDatosReporte.class);
			CallableStatement consultaMetologia = conn
					.prepareCall("{call RED_PK_PROYECTOS.MetodologiaProyecto_Consulta(?,?)}");
			consultaMetologia.setInt("un_Proyecto", idProyecto);
			consultaMetologia.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaMetologia.execute();
			ResultSet r = (ResultSet) consultaMetologia
					.getObject("un_Resultado");
			while (r.next()) {
				metodologia.setMetodologiaId(r.getInt(1));
				metodologia.setMetodologiaNombre(r.getString(2));
				metodologia.setMetodologiaEcuacion(r.getString(3) == null ? ""
						: r.getString(3));
				metodologia
						.setMetodologiaDirArchivo(r.getString(4) == null ? ""
								: r.getString(4));
				metodologia.setDescripcion(r.getString(5));
			}
			r.close();
			consultaMetologia.close();
			log.info("[consultarMetodologia] Termino");
		} catch (Exception e) {
			log.info("[consultarMetodologia] Fallo");
			e.printStackTrace();
		}
		return metodologia;
	}

}
