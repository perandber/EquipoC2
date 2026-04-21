package Objetos;

/*
 * Clase que representa una gama de mantenimiento.
 * Una gama es un conjunto de tareas o procedimientos de mantenimiento agrupados
 * bajo un mismo criterio. Por ejemplo, puede ser una gama de mantenimiento preventivo
 * semanal o una gama correctiva para averías eléctricas.
 *
 * Cada gama tiene un tipo de mantenimiento (preventivo, correctivo, predictivo...)
 * y un tipo de gama que la clasifica según su alcance o frecuencia (diaria, mensual, etc.).
 */
public class gamas_mantenimiento {

    private int id_gama;                // Identificador único de la gama en la base de datos
    private String nombre;              // Nombre de la gama (ej: "Mantenimiento preventivo mensual")
    private String tipo_mantenimiento;  // Tipo: preventivo, correctivo, predictivo...
    private String tipo_gama;           // Clasificación por alcance o frecuencia: diaria, anual, etc.
    private String descripcion;         // Descripción detallada de las tareas que incluye esta gama

    // Constructor vacío, necesario para crear objetos sin datos iniciales
    public gamas_mantenimiento() {}

    // Constructor completo: crea una gama con todos sus datos de una vez
    public gamas_mantenimiento(int id_gama, String nombre,
                               String tipo_mantenimiento,
                               String tipo_gama,
                               String descripcion) {
        this.id_gama = id_gama;
        this.nombre = nombre;
        this.tipo_mantenimiento = tipo_mantenimiento;
        this.tipo_gama = tipo_gama;
        this.descripcion = descripcion;
    }

    public int getId_gama() { return id_gama; }
    public void setId_gama(int id_gama) { this.id_gama = id_gama; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo_mantenimiento() { return tipo_mantenimiento; }
    public void setTipo_mantenimiento(String tipo_mantenimiento) { this.tipo_mantenimiento = tipo_mantenimiento; }

    public String getTipo_gama() { return tipo_gama; }
    public void setTipo_gama(String tipo_gama) { this.tipo_gama = tipo_gama; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    // Representación en texto de la gama para mostrar por consola
    @Override
    public String toString() {
        return id_gama + " - " + nombre + " - " + tipo_mantenimiento;
    }
}