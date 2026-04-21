package Objetos;

/*
 * Clase que representa un archivo adjunto dentro del sistema.
 * Permite asociar ficheros (imágenes, PDFs, documentos, etc.) a otros registros
 * del sistema, como tareas u órdenes de mantenimiento.
 *
 * Cada archivo tiene un nombre, una ruta donde está guardado físicamente,
 * el tipo de entidad al que está vinculado (tipo_referencia) y el ID del usuario
 * que lo subió al sistema.
 */
public class archivos_adjuntos {

    private int id_archivo;          // Identificador único del archivo en la base de datos
    private String nombre_archivo;   // Nombre del archivo con su extensión (ej: "informe.pdf")
    private String tipo_referencia;  // Entidad a la que está vinculado: "tarea", "orden", etc.
    private String ruta_archivo;     // Ruta del sistema de archivos donde está guardado el fichero
    private int id_usuario_subida;   // ID del usuario que subió este archivo

    // Constructor vacío, necesario para crear objetos sin datos iniciales
    public archivos_adjuntos() {}

    // Constructor completo: crea un archivo adjunto con todos sus datos de una vez
    public archivos_adjuntos(int id_archivo, String nombre_archivo,
                             String tipo_referencia, String ruta_archivo,
                             int id_usuario_subida) {
        this.id_archivo = id_archivo;
        this.nombre_archivo = nombre_archivo;
        this.tipo_referencia = tipo_referencia;
        this.ruta_archivo = ruta_archivo;
        this.id_usuario_subida = id_usuario_subida;
    }

    public int getId_archivo() { return id_archivo; }
    public void setId_archivo(int id_archivo) { this.id_archivo = id_archivo; }

    public String getNombre_archivo() { return nombre_archivo; }
    public void setNombre_archivo(String nombre_archivo) { this.nombre_archivo = nombre_archivo; }

    public String getTipo_referencia() { return tipo_referencia; }
    public void setTipo_referencia(String tipo_referencia) { this.tipo_referencia = tipo_referencia; }

    public String getRuta_archivo() { return ruta_archivo; }
    public void setRuta_archivo(String ruta_archivo) { this.ruta_archivo = ruta_archivo; }

    public int getId_usuario_subida() { return id_usuario_subida; }
    public void setId_usuario_subida(int id_usuario_subida) { this.id_usuario_subida = id_usuario_subida; }

    // Representación en texto del archivo para mostrar por consola
    @Override
    public String toString() {
        return id_archivo + " - " + nombre_archivo + " - " + tipo_referencia;
    }
}