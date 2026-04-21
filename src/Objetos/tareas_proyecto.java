package Objetos;

import java.time.LocalDate;

/**Objeto a construir 
 * Incluye los objetos "proyectos_gantt" y "usuarios"
 * @author Perceval*/
public class tareas_proyecto {

	/**Clave principal*/
	private int id;
	private proyectos_gantt proyecto;
	/**Maximo 300 caracteres*/
	private String nombre;
	private String descripcion;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	/**En dias, calculada*/
	private int duracion;
	/**Maximo 500 caracteres*/
	private String dependencias;
	private usuario responsable;
	private int progreso;
	/**Maximo 15 caracteres*/
	private String estado;
	
	/**Constructor completo*/
	public tareas_proyecto(int id, proyectos_gantt proyecto, String nombre, String descripcion, LocalDate fechaInicio,
			LocalDate fechaFin, int duracion, String dependencias, usuario responsable, int progreso, String estado) {
		super();
		this.id = id;
		this.proyecto = proyecto;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.duracion = duracion;
		this.dependencias = dependencias;
		this.responsable = responsable;
		this.progreso = progreso;
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "id=" + id + ", "
				+ nombre+ ", "+descripcion+", desde: "+fechaInicio+ "hasta: "+fechaFin+" ("+duracion+" dias), necesita: "+dependencias
				+ ", progreso:"+progreso+"%, "+estado
				+ "Proyecto: "+proyecto
				+ "Responsable: "+responsable;
	}

	//Getters y Setters
	public proyectos_gantt getProyecto() {
		return proyecto;
	}

	public void setProyecto(proyectos_gantt proyecto) {
		this.proyecto = proyecto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public String getDependencias() {
		return dependencias;
	}

	public void setDependencias(String dependencias) {
		this.dependencias = dependencias;
	}

	public usuario getResponsable() {
		return responsable;
	}

	public void setResponsable(usuario responsable) {
		this.responsable = responsable;
	}

	public int getProgreso() {
		return progreso;
	}

	public void setProgreso(int progreso) {
		this.progreso = progreso;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getId() {
		return id;
	}
	
	
	
}
