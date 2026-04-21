package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;

/**
 * Clase DAO para la gestión de la relación entre máquinas y sus piezas de repuesto.
 * Esta clase permite administrar qué piezas son compatibles o están instaladas en cada máquina.
 * * @author Cristian
 */
public class maquinas_piezasDAO extends interfaces {

    private conexion con = new conexion();
    private Scanner sc = new Scanner(System.in);

    /**
     * Menú interactivo para gestionar las vinculaciones entre máquinas y piezas.
     */
    @Override
    public void Menu() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- VINCULACIÓN MÁQUINAS Y PIEZAS ---");
            System.out.println("1. Mostrar todas las vinculaciones");
            System.out.println("2. Vincular pieza a máquina");
            System.out.println("3. Modificar vinculación");
            System.out.println("4. Eliminar vinculación");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            try {
                int opcion = Integer.parseInt(sc.nextLine());
                switch (opcion) {
                    case 1 -> Mostrar();
                    case 2 -> Crear();
                    case 3 -> Modificar();
                    case 4 -> Borrar();
                    case 0 -> salir = true;
                    default -> System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce un número válido.");
            }
        }
    }

    /**
     * Muestra por consola todas las relaciones existentes en la tabla maquinas_piezas.
     * @return true si la consulta se ejecutó correctamente.
     */
    @Override
    public boolean Mostrar() {
        String sql = "SELECT * FROM maquinas_piezas";
        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            System.out.println("\nID Máquina | ID Pieza");
            System.out.println("---------------------");
            while (rs.next()) {
                System.out.println(rs.getInt("id_maquina") + "          | " + rs.getInt("id_pieza"));
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al mostrar datos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Crea una nueva vinculación entre una máquina y una pieza.
     * @return true si la inserción fue exitosa.
     */
    @Override
    public boolean Crear() {
        System.out.print("Introduce el ID de la Máquina: ");
        int idM = Integer.parseInt(sc.nextLine());
        System.out.print("Introduce el ID de la Pieza: ");
        int idP = Integer.parseInt(sc.nextLine());

        String sql = "INSERT INTO maquinas_piezas (id_maquina, id_pieza) VALUES (?, ?)";
        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idM);
            ps.setInt(2, idP);
            int filas = ps.executeUpdate();
            if (filas > 0) System.out.println("Vinculación creada con éxito.");
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al crear vinculación: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza una vinculación existente. Dado que es una tabla de relación,
     * se requiere identificar el par actual para cambiarlo por uno nuevo.
     * @return true si se modificó el registro.
     */
    @Override
    protected boolean Modificar() {
        System.out.print("ID Máquina actual: ");
        int idM_old = Integer.parseInt(sc.nextLine());
        System.out.print("ID Pieza actual: ");
        int idP_old = Integer.parseInt(sc.nextLine());
        System.out.print("Nuevo ID de Pieza: ");
        int idP_new = Integer.parseInt(sc.nextLine());

        String sql = "UPDATE maquinas_piezas SET id_pieza = ? WHERE id_maquina = ? AND id_pieza = ?";
        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idP_new);
            ps.setInt(2, idM_old);
            ps.setInt(3, idP_old);
            int filas = ps.executeUpdate();
            if (filas > 0) System.out.println("Vinculación actualizada.");
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una vinculación específica entre una máquina y una pieza.
     * @return true si se eliminó la relación.
     */
    @Override
    protected boolean Borrar() {
        System.out.print("ID Máquina: ");
        int idM = Integer.parseInt(sc.nextLine());
        System.out.print("ID Pieza a desvincular: ");
        int idP = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM maquinas_piezas WHERE id_maquina = ? AND id_pieza = ?";
        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idM);
            ps.setInt(2, idP);
            int filas = ps.executeUpdate();
            if (filas > 0) System.out.println("Vinculación eliminada.");
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al borrar: " + e.getMessage());
            return false;
        }
    }

    /**
     * @return null ya que esta clase maneja relaciones ID-ID y no una lista de objetos simple.
     */
    @Override
    public ArrayList<Object> Recibir() {
        return null;
    }
}