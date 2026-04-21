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
import Objetos.herramientas;

/*
 * Clase DAO (Data Access Object) de herramientas.
 * Se encarga de toda la comunicación entre la aplicación y la tabla "herramientas"
 * de la base de datos. Implementa las cuatro operaciones básicas: mostrar, crear,
 * borrar y modificar. También incluye un menú interactivo por consola para usarlas.
 *
 * Extiende la clase "interfaces", que define los métodos que todo DAO debe implementar.
 *
 * @author Alvaro
 */
public class herramientasDAO extends interfaces {

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
            System.out.println("\n--- HERRAMIENTAS ---");
            System.out.println("1. Mostrar");
            System.out.println("2. Crear");
            System.out.println("3. Borrar");
            System.out.println("4. Modificar");
            System.out.println("0. Salir");

            op = sc.nextInt();

            switch (op) {
                case 1 -> Mostrar();
                case 2 -> Crear();
                case 3 -> Borrar();
                case 4 -> Modificar();
            }

        } while (op != 0);
    }

    /*
     * Llama a Recibir() para obtener todas las herramientas de la base de datos
     * y las imprime por consola una a una usando el método toString() de cada objeto.
     */
    @Override
    public boolean Mostrar() {
        ArrayList<Object> lista = Recibir();
        lista.forEach(System.out::println);
        return true;
    }

    /*
     * Consulta toda la tabla "herramientas" en la base de datos y construye un objeto
     * herramientas por cada fila. Devuelve todos esos objetos en una lista.
     * Si ocurre algún error con la base de datos, lo muestra por consola.
     */
    @Override
    public ArrayList<Object> Recibir() {

        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM herramientas";

        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                herramientas h = new herramientas(
                        rs.getInt("id_herramienta"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getString("codigo_interno"),
                        rs.getString("estado"),
                        rs.getString("ubicacion"),
                        rs.getBoolean("activa")
                );
                lista.add(h);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /*
     * Pide al usuario que introduzca los datos de una nueva herramienta
     * (nombre, tipo, código, estado, ubicación y si está activa)
     * y la inserta en la base de datos. El ID lo genera automáticamente la BD.
     */
    @Override
    protected boolean Crear() {

        sc.nextLine(); // Necesario para limpiar el salto de línea que queda del nextInt() anterior

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Tipo: ");
        String tipo = sc.nextLine();

        System.out.print("Codigo: ");
        String codigo = sc.nextLine();

        System.out.print("Estado: ");
        String estado = sc.nextLine();

        System.out.print("Ubicacion: ");
        String ubi = sc.nextLine();

        System.out.print("Activa: ");
        boolean activa = sc.nextBoolean();

        String sql = "INSERT INTO herramientas VALUES (NULL,?,?,?,?,?,?)";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, tipo);
            ps.setString(3, codigo);
            ps.setString(4, estado);
            ps.setString(5, ubi);
            ps.setBoolean(6, activa);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Pide al usuario el ID de la herramienta que quiere eliminar
     * y borra ese registro de la base de datos.
     */
    @Override
    protected boolean Borrar() {

        System.out.print("ID: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM herramientas WHERE id_herramienta=?";

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
     * Pide al usuario el ID de una herramienta y su nuevo estado,
     * y actualiza ese campo en la base de datos. Solo permite cambiar el estado,
     * no el resto de datos de la herramienta.
     */
    @Override
    protected boolean Modificar() {

        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine(); // Limpia el buffer antes de leer el texto

        System.out.print("Nuevo estado: ");
        String estado = sc.nextLine();

        String sql = "UPDATE herramientas SET estado=? WHERE id_herramienta=?";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, id);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
