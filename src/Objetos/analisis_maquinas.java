package Objetos;

import java.sql.Timestamp;

/**
 * @author Sergio
 * Objeto que representa una fila de la tabla analisis_maquinas */
public class analisis_maquinas {

    private int idAnalisis;
    private int idMaquina;
    /** Nombre de la variable medida, ej: "Presion", "Temperatura" */
    private String nombreVariable;
    /** Valor numérico medido */
    private double valor;
    /** Unidad de medida, ej: "bar", "°C" */
    private String unidadMedida;
    /** Fecha y hora en que se registró el análisis */
    private Timestamp fechaRegistro;


    // ── Constructor completo
    public analisis_maquinas(int idAnalisis, int idMaquina, String nombreVariable,
                             double valor, String unidadMedida, Timestamp fechaRegistro) {
        this.idAnalisis    = idAnalisis;
        this.idMaquina     = idMaquina;
        this.nombreVariable = nombreVariable;
        this.valor         = valor;
        this.unidadMedida  = unidadMedida;
        this.fechaRegistro = fechaRegistro;
    }


    // ── Getters y setters
    public int getIdAnalisis() {
        return idAnalisis;
    }

    public void setIdAnalisis(int idAnalisis) {
        this.idAnalisis = idAnalisis;
    }

    public int getIdMaquina() {
        return idMaquina;
    }

    public void setIdMaquina(int idMaquina) {
        this.idMaquina = idMaquina;
    }

    public String getNombreVariable() {
        return nombreVariable;
    }

    public void setNombreVariable(String nombreVariable) {
        this.nombreVariable = nombreVariable;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }


    // ── toString 
    @Override
    public String toString() {
        return "ID: " + idAnalisis +
               " | Máquina: " + idMaquina +
               " | Variable: " + nombreVariable +
               " | Valor: " + valor + " " + unidadMedida +
               " | Fecha: " + fechaRegistro;
    }
}
