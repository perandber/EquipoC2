package Objetos;

/** Objeto a construir
 * Incluye los objetos "ejecuciones_mantenimiento" y "tareas_mantenimineto"
 * @author Perceval*/
public class tareas_ejecutadas {

	/**Clave principal*/
	private int id;
	private ejecuciones_mantenimiento ejecucion;
	private tareas_mantenimiento tarea;
	private boolean completada;
	private String observaciones;
	/**Tiempo en minutos*/
	private int tiempoEmpleado;
	
	/**Constructor completo*/
	public tareas_ejecutadas(int id, ejecuciones_mantenimiento ejecucion, tareas_mantenimiento tarea,
			boolean completada, String observaciones, int tiempoEmpleado) {
		super();
		this.id = id;
		this.ejecucion = ejecucion;
		this.tarea = tarea;
		this.completada = completada;
		this.observaciones = observaciones;
		this.tiempoEmpleado = tiempoEmpleado;
	}

	
	
	@Override
	public String toString() {
		return "id:"+id+", "
				+ "ejecucion: " + ejecucion + ", "
				+ "tarea: " + tarea + ", "
				+ "completada: "+completada + ", observaciones: " + observaciones + ", tiempoEmpleado: " + tiempoEmpleado + "Minutos";
	}



	//Getters y Setters
	public ejecuciones_mantenimiento getEjecucion() {
		return ejecucion;
	}

	public void setEjecucion(ejecuciones_mantenimiento ejecucion) {
		this.ejecucion = ejecucion;
	}

	public tareas_mantenimiento getTarea() {
		return tarea;
	}

	public void setTarea(tareas_mantenimiento tarea) {
		this.tarea = tarea;
	}

	public boolean isCompletada() {
		return completada;
	}

	public void setCompletada(boolean completada) {
		this.completada = completada;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getTiempoEmpleado() {
		return tiempoEmpleado;
	}

	public void setTiempoEmpleado(int tiempoEmpleado) {
		this.tiempoEmpleado = tiempoEmpleado;
	}

	public int getId() {
		return id;
	}
	
	
	
}
