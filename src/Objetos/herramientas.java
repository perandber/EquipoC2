package Objetos;

/*
 * Clase que representa una herramienta del sistema de mantenimiento.
 * Cada herramienta tiene un identificador único, un nombre, un tipo
 * (eléctrica, manual, etc.), un código interno de inventario, un estado
 * que indica su situación actual (disponible, en reparación...), la ubicación
 * física donde está guardada, y un flag que indica si está activa o dada de baja.
 */
public class herramientas {

    private int id_herramienta;   // Identificador único en la base de datos
    private String nombre;         // Nombre descriptivo de la herramienta
    private String tipo;           // Categoría: eléctrica, manual, neumática, etc.
    private String codigo_interno; // Código con el que se registra en el inventario
    private String estado;         // Situación actual: disponible, en reparación, prestada...
    private String ubicacion;      // Lugar físico donde está almacenada
    private boolean activa;        // true = disponible para usar, false = dada de baja

    // Constructor vacío, necesario para crear objetos sin datos iniciales
    public herramientas() {}

    // Constructor completo: crea una herramienta con todos sus datos de una vez
    public herramientas(int id_herramienta, String nombre, String tipo,
                        String codigo_interno, String estado,
                        String ubicacion, boolean activa) {
        this.id_herramienta = id_herramienta;
        this.nombre = nombre;
        this.tipo = tipo;
        this.codigo_interno = codigo_interno;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.activa = activa;
    }

    public int getId_herramienta() { return id_herramienta; }
    public void setId_herramienta(int id_herramienta) { this.id_herramienta = id_herramienta; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getCodigo_interno() { return codigo_interno; }
    public void setCodigo_interno(String codigo_interno) { this.codigo_interno = codigo_interno; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    // Representación en texto de la herramienta para mostrar por consola
    @Override
    public String toString() {
        return id_herramienta + " - " + nombre + " - " + estado;
    }
}