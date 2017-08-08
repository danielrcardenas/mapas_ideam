package co.gov.ideamredd.dao;

import java.sql.CallableStatement;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;


@Stateless
public class EliminarParcela {

	private Integer idParcela;

	@Resource(mappedName = "java:/SMBC_DS")
	private DataSource dataSource;
	
	private Connection conn;
	
	public static void main(String[] args){
//		EliminarParcela eliminarParcela = new EliminarParcela();
//		eliminarParcela.setIdParcela(52);
//		eliminarParcela.eliminarParcela();
	}

	public void eliminarParcela() {
		try {
		//	EliminarAsociadosParcela.eliminarAsociados(idParcela);
			eliminarGeoParcela();
			eliminarDatosParcela();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void eliminarGeoParcela() throws SQLException {
		conn = dataSource.getConnection();
		CallableStatement eliminaGeoParcela = conn
				.prepareCall("{call RED_PK_GEOMETRIA.Parcela_Elimina(?,?)}");
		eliminaGeoParcela.setInt("una_Llave", idParcela);
		eliminaGeoParcela.registerOutParameter("un_Resultado",
				OracleTypes.VARCHAR);
		eliminaGeoParcela.execute();
		System.out.println(eliminaGeoParcela.getObject("un_Resultado"));
		eliminaGeoParcela.close();
		conn.close();
	}
	
	private void eliminarDatosParcela() throws SQLException {
		conn = dataSource.getConnection();
		CallableStatement eliminaParcela = conn
				.prepareCall("{call RED_PK_PARCELAS.Elimina_Parcela(?,?)}");
		eliminaParcela.setInt("una_parcela", idParcela);
		eliminaParcela.registerOutParameter("un_Resultado",
				OracleTypes.VARCHAR);
		eliminaParcela.execute();
		System.out.println(eliminaParcela.getObject("un_Resultado"));
		eliminaParcela.close();
		conn.close();
	}

	public Integer getIdParcela() {
		return idParcela;
	}

	public void setIdParcela(Integer idParcela) {
		this.idParcela = idParcela;
	}

}
