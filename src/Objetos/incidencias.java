package Objetos;

import java.sql.Timestamp;

/**
 * @author Sergio
 * Objeto que representa una fila de la tabla incidencias.
 * Cada instancia de esta clase equivale a un registro de la base de datos. */
public class incidencias {

    // Cada campo privado corresponde a una columna de la tabla incidencias
    private int idIncidencia;
    /** ID de la máquina que ha generado la incidencia */
    private int idMaquina;
    /** Descripción del problema reportado */
    private String descripcion;
    /** Prioridad de la incidencia: "baja", "media" o "alta" */
    private String prioridad;
    /** Estado actual del ticket: "abierta", "en_proceso" o "resuelta" */
    private String estado;
    /** Texto con la solución aplicada al resolver la incidencia. Puede ser null si aún no está resuelta */
    private String solucionAplicada;
    /** Fecha y hora en que se resolvió. Usamos Timestamp porque la BD guarda fecha + hora */
    private Timestamp fechaResolucion;


    // Constructor que recibe todos los campos: se usa al crear el objeto desde la BD
    public incidencias(int idIncidencia, int idMaquina, String descripcion,
                       String prioridad, String estado,
                       String solucionAplicada, Timestamp fechaResolucion) {
        this.idIncidencia     = idIncidencia;
        this.idMaquina        = idMaquina;
        this.descripcion      = descripcion;
        this.prioridad        = prioridad;
        this.estado           = estado;
        this.solucionAplicada = solucionAplicada;
        this.fechaResolucion  = fechaResolucion;
    }


    // Getters: permiten leer los campos desde fuera de la clase (los campos son privados)
    // Setters: permiten modificar los campos desde fuera de la clase
    public int getIdIncidencia() { return idIncidencia; }
    public void setIdIncidencia(int idIncidencia) { this.idIncidencia = idIncidencia; }

    public int getIdMaquina() { return idMaquina; }
    public void setIdMaquina(int idMaquina) { this.idMaquina = idMaquina; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getSolucionAplicada() { return solucionAplicada; }
    public void setSolucionAplicada(String solucionAplicada) { this.solucionAplicada = solucionAplicada; }

    public Timestamp getFechaResolucion() { return fechaResolucion; }
    public void setFechaResolucion(Timestamp fechaResolucion) { this.fechaResolucion = fechaResolucion; }


    // toString: se llama automáticamente al hacer System.out.println(objeto)
    // Devuelve una cadena legible con los datos de la incidencia
    @Override
    public String toString() {
        return "Ticket #" + idIncidencia +
               " | Máquina: " + idMaquina +
               " | " + descripcion +
               " | Prioridad: " + prioridad +
               " | Estado: " + estado;
    }
}
