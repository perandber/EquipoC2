package Objetos;

import java.time.LocalDateTime;

public class planes_mantenimiento {
	private int idPlan;
    private int idMaquina;
    private int idGama;
    private int idFrecuencia;
    private Integer horasIntervalo;
    private int idDificultad;
    private int duracionEstimada;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    
	public planes_mantenimiento(int idPlan, int idMaquina, int idGama, int idFrecuencia, Integer horasIntervalo,
			int idDificultad, int duracionEstimada, boolean activo, LocalDateTime fechaCreacion) {
		super();
		this.idPlan = idPlan;
		this.idMaquina = idMaquina;
		this.idGama = idGama;
		this.idFrecuencia = idFrecuencia;
		this.horasIntervalo = horasIntervalo;
		this.idDificultad = idDificultad;
		this.duracionEstimada = duracionEstimada;
		this.activo = activo;
		this.fechaCreacion = fechaCreacion;
	}

	public int getIdPlan() {
		return idPlan;
	}

	public void setIdPlan(int idPlan) {
		this.idPlan = idPlan;
	}

	public int getIdMaquina() {
		return idMaquina;
	}

	public void setIdMaquina(int idMaquina) {
		this.idMaquina = idMaquina;
	}

	public int getIdGama() {
		return idGama;
	}

	public void setIdGama(int idGama) {
		this.idGama = idGama;
	}

	public int getIdFrecuencia() {
		return idFrecuencia;
	}

	public void setIdFrecuencia(int idFrecuencia) {
		this.idFrecuencia = idFrecuencia;
	}

	public Integer getHorasIntervalo() {
		return horasIntervalo;
	}

	public void setHorasIntervalo(Integer horasIntervalo) {
		this.horasIntervalo = horasIntervalo;
	}

	public int getIdDificultad() {
		return idDificultad;
	}

	public void setIdDificultad(int idDificultad) {
		this.idDificultad = idDificultad;
	}

	public int getDuracionEstimada() {
		return duracionEstimada;
	}

	public void setDuracionEstimada(int duracionEstimada) {
		this.duracionEstimada = duracionEstimada;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Override
	public String toString() {
		return "planes_mantenimiento [idPlan=" + idPlan + ", idMaquina=" + idMaquina + ", idGama=" + idGama
				+ ", idFrecuencia=" + idFrecuencia + ", horasIntervalo=" + horasIntervalo + ", idDificultad="
				+ idDificultad + ", duracionEstimada=" + duracionEstimada + ", activo=" + activo + ", fechaCreacion="
				+ fechaCreacion + "]";
	}
}
