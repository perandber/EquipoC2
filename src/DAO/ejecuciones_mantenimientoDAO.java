package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Scanner;

import Comun.conexion;
import Comun.interfaces;
import Objetos.ejecuciones_mantenimiento;

/*
 * Clase DAO (Data Access Object) de ejecuciones de mantenimiento.
 * Se encarga de toda la comunicación entre la aplicación y la tabla "ejecuciones_mantenimiento"
 * de la base de datos. Implementa las cuatro operaciones básicas: mostrar, crear,
 * borrar y modificar. También incluye un menú interactivo por consola para usarlas.
 *
 * Extiende la clase "interfaces", que define los métodos que todo DAO debe implementar.
 *
 * @author Alvaro
 */
public class ejecuciones_mantenimientoDAO extends interfaces {

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
            System.out.println("\n--- EJECUCIONES MANTENIMIENTO ---");
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
     * Llama a Recibir() para obtener todas las ejecuciones de la base de datos
     * y las imprime por consola una a una usando el método toString() de cada objeto.
     */
    @Override
    public boolean Mostrar() {
        Recibir().forEach(System.out::println);
        return true;
    }

    /*
     * Consulta toda la tabla "ejecuciones_mantenimiento" en la base de datos y construye
     * un objeto ejecuciones_mantenimiento por cada fila. Devuelve todos esos objetos en una lista.
     * Si ocurre algún error con la base de datos, lo muestra por consola.
     */
    @Override
    public ArrayList<Object> Recibir() {

        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM ejecuciones_mantenimiento";

        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                ejecuciones_mantenimiento e = new ejecuciones_mantenimiento(
                        rs.getInt("id_ejecucion"),
                        rs.getInt("id_orden"),
                        rs.getDate("fecha_ejecucion"),
                        rs.getTime("hora_inicio"),
                        rs.getTime("hora_fin"),
                        rs.getInt("id_tecnico"),
                        rs.getString("observaciones"),
                        rs.getString("resultado")
                );
                lista.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /*
     * Pide al usuario el ID de la orden de mantenimiento y registra una nueva ejecución.
     * La fecha se pone automáticamente con NOW() (la fecha actual del servidor).
     * Las horas de inicio y fin se dejan en NULL porque aún no se conocen.
     * El técnico asignado por defecto es el de ID 1, y las observaciones y resultado
     * se dejan vacíos para rellenar más adelante con Modificar().
     */
    @Override
    protected boolean Crear() {

        System.out.print("ID orden: ");
        int id = sc.nextInt();

        String sql = "INSERT INTO ejecuciones_mantenimiento VALUES (NULL,?,NOW(),NULL,NULL,1,'','')";

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
     * Pide al usuario el ID de la ejecución que quiere eliminar
     * y borra ese registro de la base de datos.
     */
    @Override
    protected boolean Borrar() {

        System.out.print("ID: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM ejecuciones_mantenimiento WHERE id_ejecucion=?";

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
     * Pide al usuario el ID de una ejecución y el resultado final,
     * y actualiza ese campo en la base de datos. Solo permite cambiar el resultado,
     * no el resto de datos de la ejecución.
     */
    @Override
    protected boolean Modificar() {

        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine(); // Limpia el buffer antes de leer el texto

        System.out.print("Resultado: ");
        String res = sc.nextLine();

        String sql = "UPDATE ejecuciones_mantenimiento SET resultado=? WHERE id_ejecucion=?";

        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, res);
            ps.setInt(2, id);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
