package Objetos;

/**Objeto a construir 
 * Incluye los objeto "tareas_mantenimineto" y "herraminetas"
 * @author Perceval*/
public class tareas_herramientas {

	private tareas_mantenimiento tarea;
	private herramientas herramienta;
	private int cantidad;
	
	/**Constructor entero*/
	public tareas_herramientas(tareas_mantenimiento tarea, herramientas herramineta, int cantidad) {
		super();
		this.tarea = tarea;
		this.herramienta = herramineta;
		this.cantidad = cantidad;
	}

	@Override
	public String toString() {
		return "tarea: "+tarea
				+" herramienta ("+cantidad+"):"+herramienta;
	}

	//Getters y setters
	public tareas_mantenimiento getTarea() {
		return tarea;
	}

	public void setTarea(tareas_mantenimiento tarea) {
		this.tarea = tarea;
	}

	public herramientas getHerramienta() {
		return herramienta;
	}

	public void setHerramienta(herramientas herramienta) {
		this.herramienta = herramienta;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	
}
