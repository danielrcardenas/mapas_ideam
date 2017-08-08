package co.gov.ideamredd.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.entities.Parcela;
import co.gov.ideamredd.util.Util;

@Stateless
public class ConsultarParcela {
	
	@Resource(mappedName = "java:/SMBC_DS")
	private DataSource dataSource;

	private Integer consecutivo = -1;
	private String nombre;
	private Date fechaInicio;
	private Date fechaFin;
	private Integer pais = -1;
	private String departamento;
	private String municipio;
	private String car;
	private String bosque = "";
	private String fgda;
	private Integer tipoInventario = -1;
	private String codigoCampo;
	private Integer usuario;
	private Integer idUsuario;

	private ArrayList<Integer> un_Departamento = new ArrayList<Integer>();
	private ArrayList<Integer> un_Municipio = new ArrayList<Integer>();
	private ArrayList<String> un_TipoBosque = new ArrayList<String>();
	private ArrayList<Integer> una_Car = new ArrayList<Integer>();
	private ArrayList<Parcela> parcelas = null;

	private Connection conn;

	public ArrayList<Parcela> consultarParcela() {
//		try {
//			conn = dataSource.getConnection();
//			parcelas = new ArrayList<Parcela>();
//			departamento = un_Departamento == null ? "" : Util
//					.construirStringConsulta(un_Departamento);
//			municipio = un_Municipio == null ? "" : Util
//					.construirStringConsulta(un_Municipio);
//			car = una_Car == null ? "" : Util
//					.construirStringConsulta(una_Car);
//			if (un_TipoBosque != null)
//				for (int i = 0; i < un_TipoBosque.size(); i++) {
//					if (un_TipoBosque.get(i) != null
//							&& !un_TipoBosque.equals(""))
//						bosque += un_TipoBosque.get(i);
//				}
//			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
//			CallableStatement consultarProyecto = conn
//					.prepareCall("{call RED_PK_PARCELAS.Consulta_Parcela(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
//			consultarProyecto.setInt("un_Consecutivo", consecutivo);
//			consultarProyecto.setString("un_Nombre", nombre);
//			consultarProyecto.setString("un_codigo", codigoCampo);
//			consultarProyecto.setString("una_FechaInicio", sd
//					.format(fechaInicio));
//			consultarProyecto.setString("una_FechaFin", sd.format(fechaFin));
//			consultarProyecto.setInt("un_Pais", pais);
//			consultarProyecto.setString("un_Departamento", departamento);
//			consultarProyecto.setString("un_Municipio", municipio);
//			consultarProyecto.setString("una_car", car);
//			consultarProyecto.setString("un_Bosque", bosque);
//			consultarProyecto.setString("una_FGDA", fgda);
//			consultarProyecto.setInt("un_TipoInventario", tipoInventario);
//			consultarProyecto.setInt("un_usuario",usuario);
//			consultarProyecto.setInt("un_idusuario",idUsuario==null?-1:idUsuario);
//			consultarProyecto.registerOutParameter("un_Resultado",
//					OracleTypes.CURSOR);
//			consultarProyecto.registerOutParameter("sentencia",
//					OracleTypes.VARCHAR);
//			consultarProyecto.execute();
//			System.out.println(consultarProyecto.getObject("sentencia"));
//			OracleResultSet resultSet = (OracleResultSet) consultarProyecto
//					.getObject("un_Resultado");
//			while (resultSet.next()) {
//				Parcela parcela = new Parcela();
//				parcela.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
//						.toString()));
//				parcela.setNombre((String) resultSet.getObject(2));
//				parcela.setPerteneceProyecto((String) resultSet.getObject(3));
//				parcela.setFechaEstablecimiento((Timestamp) resultSet
//						.getObject(4));
//				parcela.setAprovechamiento((String) resultSet.getObject(5));
//				parcela.setColecciones((String) resultSet.getObject(6));
//				parcela.setInvestigador(resultSet.getObject(7)==null?"":resultSet.getObject(7).toString());
//				parcela.setArea(new BigDecimal(resultSet.getObject(8)==null?"1":resultSet.getObject(8).toString()));
//				parcela.setVerificado(resultSet.getObject(9)!=null?resultSet.getObject(9).toString():"NO");
//				parcela
//						.setInventarioPublico(resultSet.getObject(10)
//								.toString());
//				parcela.setEstado(Integer.valueOf(resultSet.getObject(11).toString()));
//				parcela.setTemporalidad(Integer.valueOf(resultSet.getObject(12)
//						.toString()));
//				parcela.setInventario(Integer.valueOf(resultSet.getObject(13)
//						.toString()));
//				parcela.setFgda(Integer.valueOf(resultSet.getObject(14)
//						.toString()));
//				parcela.setTipoAutor(Integer.valueOf(resultSet.getObject(15)
//						.toString()));
//				parcela.setTipoCustodio(Integer.valueOf(resultSet.getObject(16)
//						.toString()));
//				parcela.setMetadato(Integer.valueOf(resultSet.getObject(17)!=null?resultSet.getObject(17).toString():"0"));
//				parcela.setIdFgda(Integer.valueOf(resultSet.getObject(18)
//						.toString()));
//				parcela.setIdAutor(Integer.valueOf(resultSet.getObject(19)
//						.toString()));
//				parcela.setIdCustodio(Integer.valueOf(resultSet.getObject(20)
//						.toString()));
//				parcela.setIdInvetigador(Integer.valueOf(resultSet
//						.getObject(21)==null?"-1":resultSet
//								.getObject(21).toString()));
//				parcela.setIdEncargado(Integer.valueOf(resultSet.getObject(22)
//						.toString()));
//				parcela.setIdColeccion(Integer.valueOf(resultSet.getObject(23)==null?"-1":resultSet.getObject(23)
//						.toString()));
//				parcela.setLargoParcela((BigDecimal)resultSet.getObject(24));
//				parcela.setAnchoParcela((BigDecimal)resultSet.getObject(25));
//				parcela.setRadioParcela((BigDecimal)resultSet.getObject(26));
//				parcela.setObservaciones(resultSet.getObject(27)==null?"":resultSet.getObject(27).toString());
//				parcela.setRutaImagen(resultSet.getObject(28)!=null?resultSet.getObject(28).toString():"");
//				parcela.setNombreImagen(resultSet.getObject(29)!=null?resultSet.getObject(29).toString():"");
//				parcela.setPais(Integer.valueOf(resultSet.getObject(30)
//						.toString()));
//				parcela.setProposito(Integer.valueOf(resultSet.getObject(31)
//						.toString()));
//				parcela.setCodigoCampo(resultSet.getObject(32)!=null?resultSet.getObject(32).toString():"0");
//				parcela.setForma(Integer.valueOf(resultSet.getObject(33)==null?"-1":resultSet.getObject(33)
//						.toString()));
//				parcela.setDescripcion(resultSet.getObject(34)==null?"":resultSet.getObject(34).toString());
//				parcelas.add(parcela);
//			}
//			if (parcelas.size() == 0) {
//				System.out
//						.println("No se encontraron resultados para la busqueda");
//			}
//			resultSet.close();
//			consultarProyecto.close();
//			conn.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return parcelas;
	}
	
	public static void main(String[] args) {
//		ConsultarParcela parcela = new ConsultarParcela();
//		parcela.setConsecutivo(95);
		// parcela.setNombre("p");
		// parcela.setFechaInicio(new Date(2011-1900,06-1,15));
//		parcela.consultarParcela();
	}

	public Integer getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Integer getPais() {
		return pais;
	}

	public void setPais(Integer pais) {
		this.pais = pais;
	}

	public Integer getTipoInventario() {
		return tipoInventario;
	}

	public void setTipoInventario(Integer tipoInventario) {
		this.tipoInventario = tipoInventario;
	}

	public String getFgda() {
		return fgda;
	}

	public void setFgda(String fgda) {
		this.fgda = fgda;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getBosque() {
		return bosque;
	}

	public void setBosque(String bosque) {
		this.bosque = bosque;
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

	public String getCodigoCampo() {
		return codigoCampo;
	}

	public void setCodigoCampo(String codigoCampo) {
		this.codigoCampo = codigoCampo;
	}

	public ArrayList<String> getUn_TipoBosque() {
		return un_TipoBosque;
	}

	public void setUn_TipoBosque(ArrayList<String> unTipoBosque) {
		un_TipoBosque = unTipoBosque;
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

	public ArrayList<Integer> getUna_Car() {
		return una_Car;
	}

	public void setUna_Car(ArrayList<Integer> unaCar) {
		una_Car = unaCar;
	}

}
