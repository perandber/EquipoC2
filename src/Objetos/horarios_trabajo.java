package Objetos;

import java.sql.Time;

/**
 * @author Sergio
 * Objeto que representa una fila de la tabla horarios_trabajo.
 * Cada instancia de esta clase equivale a un registro de la base de datos. */
public class horarios_trabajo {

    // Cada campo privado corresponde a una columna de la tabla horarios_trabajo
    private int idHorario;
    /** Nombre descriptivo del turno, ej: "Turno Mañana" */
    private String nombre;
    /** Hora a la que empieza el turno. Usamos Time porque la BD guarda solo hora, sin fecha */
    private Time horaInicio;
    /** Hora a la que termina el turno */
    private Time horaFin;
    /** Días de la semana en los que aplica este horario. true = activo ese día */
    private boolean lunes;
    private boolean martes;
    private boolean miercoles;
    private boolean jueves;
    private boolean viernes;


    // Constructor que recibe todos los campos: se usa al crear el objeto desde la BD
    public horarios_trabajo(int idHorario, String nombre, Time horaInicio, Time horaFin,
                            boolean lunes, boolean martes, boolean miercoles,
                            boolean jueves, boolean viernes) {
        this.idHorario  = idHorario;
        this.nombre     = nombre;
        this.horaInicio = horaInicio;
        this.horaFin    = horaFin;
        this.lunes      = lunes;
        this.martes     = martes;
        this.miercoles  = miercoles;
        this.jueves     = jueves;
        this.viernes    = viernes;
    }


    // Getters: permiten leer los campos desde fuera de la clase (los campos son privados)
    // Setters: permiten modificar los campos desde fuera de la clase
    public int getIdHorario() { return idHorario; }
    public void setIdHorario(int idHorario) { this.idHorario = idHorario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Time getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Time horaInicio) { this.horaInicio = horaInicio; }

    public Time getHoraFin() { return horaFin; }
    public void setHoraFin(Time horaFin) { this.horaFin = horaFin; }

    public boolean isLunes() { return lunes; }
    public void setLunes(boolean lunes) { this.lunes = lunes; }

    public boolean isMartes() { return martes; }
    public void setMartes(boolean martes) { this.martes = martes; }

    public boolean isMiercoles() { return miercoles; }
    public void setMiercoles(boolean miercoles) { this.miercoles = miercoles; }

    public boolean isJueves() { return jueves; }
    public void setJueves(boolean jueves) { this.jueves = jueves; }

    public boolean isViernes() { return viernes; }
    public void setViernes(boolean viernes) { this.viernes = viernes; }


    // toString: se llama automáticamente al hacer System.out.println(objeto)
    // Devuelve una cadena legible con los datos del horario
    @Override
    public String toString() {
        return idHorario + " - " + nombre + " (" + horaInicio + " a " + horaFin + ")" +
               " | L:" + lunes + " M:" + martes + " X:" + miercoles +
               " J:" + jueves + " V:" + viernes;
    }
}
