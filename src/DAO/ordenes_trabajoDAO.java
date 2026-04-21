package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;

/**
 * @author Cristian*/
public class ordenes_trabajoDAO extends interfaces {
    private conexion con = new conexion();
    private Scanner sc = new Scanner(System.in);

	/**
    * Interfaz de usuario por consola para gestionar las órdenes de trabajo.
    */

    @Override
    public void Menu() {
        System.out.println("\n--- ÓRDENES DE TRABAJO ---");
        System.out.println("1. Listar Órdenes");
        System.out.println("2. Nueva Orden");
        System.out.println("0. Volver");
        
        int op = Integer.parseInt(sc.nextLine());
        try (Connection c = con.Conectar()) {
            if (op == 1) listar(c);
            else if (op == 2) crear(c);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Implementación del método de la interfaz para mostrar datos.
     */
    @Override
    public boolean Mostrar() {
        try (Connection c = con.Conectar()) {
            if (c != null) {
                this.listar(c);
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
            return false;
        }
    }

    /**
     * MÉTODO QUE FALTA: Este es el que el IDE te pide crear.
     * @param c Conexión activa.
     * @throws SQLException Si falla la consulta.
     */
    private void listar(Connection c) throws SQLException {
        String sql = "SELECT * FROM ordenes_trabajo";
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Desc: " + rs.getString("descripcion"));
            }
        }
    }

    /**
     * Recupera todos los registros de la tabla ordenes_trabajo.
     * @return ArrayList de objetos con la descripción e ID de las órdenes.
     */
    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM ordenes_trabajo";
        try (Connection c = con.Conectar(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add("ID: " + rs.getInt("id") + " - " + rs.getString("descripcion"));
            }
        } catch (SQLException e) {
            System.out.println("Error al recibir datos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Ejecuta el proceso de creación de una nueva orden.
     * @return true si se insertó correctamente.
     */
    @Override
    public boolean Crear() {
        try (Connection c = con.Conectar()) {
            if (c != null) {
                this.crear(c);
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error de SQL: " + e.getMessage());
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Método auxiliar privado que contiene la lógica SQL de inserción.
     * @param c Conexión activa.
     * @throws SQLException Si hay un error en la base de datos.
     */
    private void crear(Connection c) throws SQLException {
        System.out.print("Descripción de la orden: ");
        String desc = sc.nextLine();
        String sql = "INSERT INTO ordenes_trabajo (descripcion) VALUES (?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, desc);
            ps.executeUpdate();
            System.out.println("Orden creada con éxito.");
        }
    }

    /**
     * Elimina una orden de trabajo específica mediante su ID.
     * @return true si se eliminó al menos un registro.
     */
    @Override
    protected boolean Borrar() {
        System.out.print("ID de la orden a borrar: ");
        int id = Integer.parseInt(sc.nextLine());
        String sql = "DELETE FROM ordenes_trabajo WHERE id = ?";
        try (Connection c = con.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Actualiza la descripción de una orden existente.
     * @return true si la actualización fue exitosa.
     */
    @Override
    protected boolean Modificar() {
        System.out.print("ID de la orden a modificar: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Nueva descripción: ");
        String desc = sc.nextLine();
        String sql = "UPDATE ordenes_trabajo SET descripcion = ? WHERE id = ?";
        try (Connection c = con.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, desc);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
