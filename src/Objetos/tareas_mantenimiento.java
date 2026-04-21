package Objetos;

/**Objeto a construir 
 * Incluye el objeto "planes mantenimiento"
 * @author Perceval*/
public class tareas_mantenimiento {

	private int id;
	private planes_mantenimiento plan;
	/**Descripcion de la tarea, maximo de 500 caracteres*/
	private String descripcion;
	private int orden;
	/**Instrucciones detalladas, caracteres ilimitados*/
	private String instrucciones;
	private boolean requiereEpi;
	/**Epis necesarios para la tarea, 500 caracteres*/
	private String episNecesarios;
	/**Reglas de seguridad, caracteres ilimitados*/
	private String reglasSeguridad;
	/**Reglas de medio ambiente, caracteres ilimitados*/
	private String medioAmbiente;
	
	
	/**Constructor completo*/
	public tareas_mantenimiento(int id, planes_mantenimiento plan, String descripcion, int orden, String instrucciones,
			boolean requiereEpi, String episNecesarios, String reglasSeguridad, String medioAmbiente) {
		this.id = id;
		this.plan = plan;
		this.descripcion = descripcion;
		this.orden = orden;
		this.instrucciones = instrucciones;
		this.requiereEpi = requiereEpi;
		this.episNecesarios = episNecesarios;
		this.reglasSeguridad = reglasSeguridad;
		this.medioAmbiente = medioAmbiente;
	}


	@Override
	public String toString() {
		String resultado = "id:"+id+" "+descripcion+", orden:"+orden+"\n";
		if (instrucciones != null && !instrucciones.isEmpty()) {
			resultado += "instrucciones: " + instrucciones + "\n";
		}
		if (requiereEpi) {
			resultado += "Requerido: " + episNecesarios + "\n";
		}
		if (reglasSeguridad != null && !reglasSeguridad.isEmpty()) {
			resultado += "Reglas de seguridad: " + reglasSeguridad + "\n";
		}
		if (medioAmbiente != null && !medioAmbiente.isEmpty()) {
			resultado += "Requisitos de medio ambiente: " + medioAmbiente + "\n";
		}
		resultado += "plan: " + plan;
		return resultado;
	}
	

	//Getters y setters
	public planes_mantenimiento getPlan() {
		return plan;
	}


	public void setPlan(planes_mantenimiento plan) {
		this.plan = plan;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public int getOrden() {
		return orden;
	}


	public void setOrden(int orden) {
		this.orden = orden;
	}


	public String getInstrucciones() {
		return instrucciones;
	}


	public void setInstrucciones(String instrucciones) {
		this.instrucciones = instrucciones;
	}


	public boolean isRequiereEpi() {
		return requiereEpi;
	}


	public void setRequiereEpi(boolean requiereEpi) {
		this.requiereEpi = requiereEpi;
	}


	public String getEpisNecesarios() {
		return episNecesarios;
	}


	public void setEpisNecesarios(String episNecesarios) {
		this.episNecesarios = episNecesarios;
	}


	public String getReglasSeguridad() {
		return reglasSeguridad;
	}


	public void setReglasSeguridad(String reglasSeguridad) {
		this.reglasSeguridad = reglasSeguridad;
	}


	public String getMedioAmbiente() {
		return medioAmbiente;
	}


	public void setMedioAmbiente(String medioAmbiente) {
		this.medioAmbiente = medioAmbiente;
	}


	public int getId() {
		return id;
	}


	
	
	
	
}
