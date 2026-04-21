package Objetos;

import java.util.Date;
import java.time.LocalDateTime;

public class ordenes_trabajo {
	private int idOrden;
    private int idMaquina;
    private Integer idPlan;
    private int idTipo;
    private int idEstado;
    private int idPrioridad;
    private Integer idRolAsignado;
    private Date fechaProgramada;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinalizacion;
    private int tiempoEstimado;
    private Integer tiempoReal;
    private String observaciones;
    
	public ordenes_trabajo(int idOrden, int idMaquina, Integer idPlan, int idTipo, int idEstado, int idPrioridad,
			Integer idRolAsignado, Date fechaProgramada, LocalDateTime fechaInicio, LocalDateTime fechaFinalizacion,
			int tiempoEstimado, Integer tiempoReal, String observaciones) {
		super();
		this.idOrden = idOrden;
		this.idMaquina = idMaquina;
		this.idPlan = idPlan;
		this.idTipo = idTipo;
		this.idEstado = idEstado;
		this.idPrioridad = idPrioridad;
		this.idRolAsignado = idRolAsignado;
		this.fechaProgramada = fechaProgramada;
		this.fechaInicio = fechaInicio;
		this.fechaFinalizacion = fechaFinalizacion;
		this.tiempoEstimado = tiempoEstimado;
		this.tiempoReal = tiempoReal;
		this.observaciones = observaciones;
	}

	public int getIdOrden() {
		return idOrden;
	}

	public void setIdOrden(int idOrden) {
		this.idOrden = idOrden;
	}

	public int getIdMaquina() {
		return idMaquina;
	}

	public void setIdMaquina(int idMaquina) {
		this.idMaquina = idMaquina;
	}

	public Integer getIdPlan() {
		return idPlan;
	}

	public void setIdPlan(Integer idPlan) {
		this.idPlan = idPlan;
	}

	public int getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(int idTipo) {
		this.idTipo = idTipo;
	}

	public int getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(int idEstado) {
		this.idEstado = idEstado;
	}

	public int getIdPrioridad() {
		return idPrioridad;
	}

	public void setIdPrioridad(int idPrioridad) {
		this.idPrioridad = idPrioridad;
	}

	public Integer getIdRolAsignado() {
		return idRolAsignado;
	}

	public void setIdRolAsignado(Integer idRolAsignado) {
		this.idRolAsignado = idRolAsignado;
	}

	public Date getFechaProgramada() {
		return fechaProgramada;
	}

	public void setFechaProgramada(Date fechaProgramada) {
		this.fechaProgramada = fechaProgramada;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFinalizacion() {
		return fechaFinalizacion;
	}

	public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}

	public int getTiempoEstimado() {
		return tiempoEstimado;
	}

	public void setTiempoEstimado(int tiempoEstimado) {
		this.tiempoEstimado = tiempoEstimado;
	}

	public Integer getTiempoReal() {
		return tiempoReal;
	}

	public void setTiempoReal(Integer tiempoReal) {
		this.tiempoReal = tiempoReal;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Override
	public String toString() {
		return "ordenes_trabajo [idOrden=" + idOrden + ", idMaquina=" + idMaquina + ", idPlan=" + idPlan + ", idTipo="
				+ idTipo + ", idEstado=" + idEstado + ", idPrioridad=" + idPrioridad + ", idRolAsignado="
				+ idRolAsignado + ", fechaProgramada=" + fechaProgramada + ", fechaInicio=" + fechaInicio
				+ ", fechaFinalizacion=" + fechaFinalizacion + ", tiempoEstimado=" + tiempoEstimado + ", tiempoReal="
				+ tiempoReal + ", observaciones=" + observaciones + "]";
	}
}
