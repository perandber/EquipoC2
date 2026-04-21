package Objetos;

import java.sql.Date;

/**
 * @author Sergio
 * Objeto que representa una fila de la tabla proyectos_gantt.
 * Cada instancia de esta clase equivale a un registro de la base de datos. */
public class proyectos_gantt {

    // Cada campo privado corresponde a una columna de la tabla proyectos_gantt
    private int idProyecto;
    /** Nombre descriptivo del proyecto */
    private String nombre;
    /** Fecha de inicio del proyecto. Usamos Date porque la BD guarda solo fecha, sin hora */
    private Date fechaInicio;
    /** Estado de avance del proyecto: "planificado", "en_curso" o "finalizado" */
    private String estado;


    // Constructor que recibe todos los campos: se usa al crear el objeto desde la BD
    public proyectos_gantt(int idProyecto, String nombre, Date fechaInicio, String estado) {
        this.idProyecto  = idProyecto;
        this.nombre      = nombre;
        this.fechaInicio = fechaInicio;
        this.estado      = estado;
    }


    // Getters: permiten leer los campos desde fuera de la clase (los campos son privados)
    // Setters: permiten modificar los campos desde fuera de la clase
    public int getIdProyecto() { return idProyecto; }
    public void setIdProyecto(int idProyecto) { this.idProyecto = idProyecto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }


    // toString: se llama automáticamente al hacer System.out.println(objeto)
    // Devuelve una cadena legible con los datos del proyecto
    @Override
    public String toString() {
        return idProyecto + " - Project: " + nombre +
               " | Inicio: " + fechaInicio +
               " [" + estado + "]";
    }
}
