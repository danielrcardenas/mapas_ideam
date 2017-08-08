package co.gov.ideamredd.entities;

import java.util.ArrayList;

public class ConsultaProyectos {

	private ArrayList<String> tipoReporte;
	private ArrayList<Integer> cantidadDivision;

	private ArrayList<String> tipoEstado;
	private ArrayList<String> estado;
	private ArrayList<Integer> cantidadEstado;

	private ArrayList<String> tipoActividad;
	private ArrayList<String> actividad;
	private ArrayList<Integer> cantidadActividad;

	private ArrayList<String> tipoTenencia;
	private ArrayList<String> tenencia;
	private ArrayList<Integer> cantidadTenencia;

	public ArrayList<String> getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(ArrayList<String> tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public ArrayList<Integer> getCantidadDivision() {
		return cantidadDivision;
	}

	public void setCantidadDivision(ArrayList<Integer> cantidadDivision) {
		this.cantidadDivision = cantidadDivision;
	}

	public ArrayList<String> getTipoEstado() {
		return tipoEstado;
	}

	public void setTipoEstado(ArrayList<String> tipoEstado) {
		this.tipoEstado = tipoEstado;
	}

	public ArrayList<String> getEstado() {
		return estado;
	}

	public void setEstado(ArrayList<String> estado) {
		this.estado = estado;
	}

	public ArrayList<Integer> getCantidadEstado() {
		return cantidadEstado;
	}

	public void setCantidadEstado(ArrayList<Integer> cantidadEstado) {
		this.cantidadEstado = cantidadEstado;
	}

	public ArrayList<String> getTipoActividad() {
		return tipoActividad;
	}

	public void setTipoActividad(ArrayList<String> tipoActividad) {
		this.tipoActividad = tipoActividad;
	}

	public ArrayList<String> getActividad() {
		return actividad;
	}

	public void setActividad(ArrayList<String> actividad) {
		this.actividad = actividad;
	}

	public ArrayList<Integer> getCantidadActividad() {
		return cantidadActividad;
	}

	public void setCantidadActividad(ArrayList<Integer> cantidadActividad) {
		this.cantidadActividad = cantidadActividad;
	}

	public ArrayList<String> getTipoTenencia() {
		return tipoTenencia;
	}

	public void setTipoTenencia(ArrayList<String> tipoTenencia) {
		this.tipoTenencia = tipoTenencia;
	}

	public ArrayList<String> getTenencia() {
		return tenencia;
	}

	public void setTenencia(ArrayList<String> tenencia) {
		this.tenencia = tenencia;
	}

	public ArrayList<Integer> getCantidadTenencia() {
		return cantidadTenencia;
	}

	public void setCantidadTenencia(ArrayList<Integer> cantidadTenencia) {
		this.cantidadTenencia = cantidadTenencia;
	}

}
