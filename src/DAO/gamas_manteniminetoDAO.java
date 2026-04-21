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
import Objetos.gamas_mantenimiento;

/*
 * Clase DAO (Data Access Object) de gamas de mantenimiento.
 * Se encarga de toda la comunicación entre la aplicación y la tabla "gamas_mantenimiento"
 * de la base de datos. Implementa las cuatro operaciones básicas: mostrar, crear,
 * borrar y modificar. También incluye un menú interactivo por consola para usarlas.
 *
 * Extiende la clase "interfaces", que define los métodos que todo DAO debe implementar.
 *
 * @author Alvaro
 */
public class gamas_manteniminetoDAO extends interfaces {

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
            System.out.println("\n--- GAMAS MANTENIMIENTO ---");
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
     * Llama a Recibir() para obtener todas las gamas de la base de datos
     * y las imprime por consola una a una usando el método toString() de cada objeto.
     */
    @Override
    public boolean Mostrar() {
        Recibir().forEach(System.out::println);
        return true;
    }

    /*
     * Consulta toda la tabla "gamas_mantenimiento" en la base de datos y construye
     * un objeto gamas_mantenimiento por cada fila. Devuelve todos esos objetos en una lista.
     * Si ocurre algún error con la base de datos, lo muestra por consola.
     */
    @Override
    public ArrayList<Object> Recibir() {

        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM gamas_mantenimiento";

        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                gamas_mantenimiento g = new gamas_mantenimiento(
                        rs.getInt("id_gama"),
                        rs.getString("nombre"),
                        rs.getString("tipo_mantenimiento"),
                        rs.getString("tipo_gama"),
                        rs.getString("descripcion")
                );
                lista.add(g);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /*
     * Pide al usuario el nombre, tipo de mantenimiento y tipo de gama,
     * y crea un nuevo registro en la base de datos.
     * La descripción se deja vacía porque no se solicita al usuario en este momento.
     * El ID lo genera automáticamente la BD.
     */
    @Override
    protected boolean Crear() {

        sc.nextLine(); // Necesario para limpiar el salto de línea que queda del nextInt() anterior

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Tipo mantenimiento: ");
        String tipo = sc.nextLine();

        System.out.print("Tipo gama: ");
        String gama = sc.nextLine();

        String sql = "INSERT INTO gamas_mantenimiento VALUES (NULL,?,?,?,?)";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, tipo);
            ps.setString(3, gama);
            ps.setString(4, ""); // La descripción se deja vacía por defecto

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Pide al usuario el ID de la gama que quiere eliminar
     * y borra ese registro de la base de datos.
     */
    @Override
    protected boolean Borrar() {

        System.out.print("ID: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM gamas_mantenimiento WHERE id_gama=?";

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
     * Pide al usuario el ID de una gama y su nuevo nombre,
     * y actualiza ese campo en la base de datos. Solo permite cambiar el nombre,
     * no el resto de datos de la gama.
     */
    @Override
    protected boolean Modificar() {

        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine(); // Limpia el buffer antes de leer el texto

        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();

        String sql = "UPDATE gamas_mantenimiento SET nombre=? WHERE id_gama=?";

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
