package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import Comun.conexion;
import Comun.interfaces;
import Objetos.archivos_adjuntos;

/*
 * Clase DAO (Data Access Object) de archivos adjuntos.
 * Se encarga de toda la comunicación entre la aplicación y la tabla "archivos_adjuntos"
 * de la base de datos. Implementa las cuatro operaciones básicas: mostrar, crear,
 * borrar y modificar. También incluye un menú interactivo por consola para usarlas.
 *
 * Extiende la clase "interfaces", que define los métodos que todo DAO debe implementar.
 *
 * @author Alvaro
 */
public class archivos_adjuntosDAO extends interfaces {

    Scanner sc = new Scanner(System.in); // Lee la entrada del usuario por consola
    conexion con = new conexion();       // Gestiona la conexión con la base de datos

    /*
     * Muestra un menú por consola con las opciones disponibles y ejecuta la acción
     * que el usuario elija. El bucle se repite hasta que el usuario introduce 0 para salir.
     */
    @Override
    public void Menu() {

        int op;

        do {
            System.out.println("\n--- ARCHIVOS ADJUNTOS ---");
            System.out.println("1. Mostrar");
            System.out.println("2. Crear");
            System.out.println("3. Borrar");
            System.out.println("4. Modificar");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");

            op = sc.nextInt();

            switch (op) {
                case 1 -> Mostrar();
                case 2 -> Crear();
                case 3 -> Borrar();
                case 4 -> Modificar();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opcion no valida");
            }

        } while (op != 0);
    }

    /*
     * Llama a Recibir() para obtener todos los archivos adjuntos de la base de datos
     * y los imprime por consola uno a uno usando el método toString() de cada objeto.
     */
    @Override
    public boolean Mostrar() {
        Recibir().forEach(System.out::println);
        return true;
    }

    /*
     * Consulta toda la tabla "archivos_adjuntos" en la base de datos y construye
     * un objeto archivos_adjuntos por cada fila. Devuelve todos esos objetos en una lista.
     * Si ocurre algún error con la base de datos, lo muestra por consola.
     */
    @Override
    public ArrayList<Object> Recibir() {

        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM archivos_adjuntos";

        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                archivos_adjuntos a = new archivos_adjuntos(
                        rs.getInt("id_archivo"),
                        rs.getString("nombre_archivo"),
                        rs.getString("tipo_referencia"),
                        rs.getString("ruta_archivo"),
                        rs.getInt("id_usuario_subida")
                );
                lista.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /*
     * Pide al usuario el nombre del archivo y la ruta donde está guardado,
     * y lo inserta en la base de datos.
     * El tipo de referencia se fija siempre como "tarea" porque en este formulario
     * solo se adjuntan archivos a tareas. El ID del usuario que sube el archivo
     * se pone a 1 por defecto. El ID del archivo lo genera automáticamente la BD.
     */
    @Override
    protected boolean Crear() {

        sc.nextLine(); // Necesario para limpiar el salto de línea que queda del nextInt() anterior

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Ruta: ");
        String ruta = sc.nextLine();

        String sql = "INSERT INTO archivos_adjuntos VALUES (NULL,?,?,?,1)";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, "tarea"); // tipo de referencia fijo para este formulario
            ps.setString(3, ruta);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Pide al usuario el ID del archivo que quiere eliminar
     * y borra ese registro de la base de datos.
     */
    @Override
    protected boolean Borrar() {

        System.out.print("ID: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM archivos_adjuntos WHERE id_archivo=?";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Pide al usuario el ID de un archivo y su nuevo nombre,
     * y actualiza ese campo en la base de datos. Solo permite cambiar el nombre,
     * no la ruta ni el tipo de referencia.
     */
    @Override
    protected boolean Modificar() {

        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine(); // Limpia el buffer antes de leer el texto

        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();

        String sql = "UPDATE archivos_adjuntos SET nombre_archivo=? WHERE id_archivo=?";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setInt(2, id);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
