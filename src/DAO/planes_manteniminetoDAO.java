package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;

/**
 * Clase DAO para la gestión de Planes de Mantenimiento.
 * Permite programar y organizar las revisiones de maquinaria.
 * @author Cristian
 */
public class planes_manteniminetoDAO extends interfaces {

    private conexion con = new conexion();
    private Scanner sc = new Scanner(System.in);

    /**
     * Menú principal para la gestión de planes de mantenimiento.
     */
    @Override
    public void Menu() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- PLANES DE MANTENIMIENTO ---");
            System.out.println("1. Mostrar todos los planes");
            System.out.println("2. Crear nuevo plan");
            System.out.println("3. Modificar plan existente");
            System.out.println("4. Borrar plan");
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
                System.out.println("Error: Ingrese un número válido.");
            }
        }
    }

    /**
     * Recupera y muestra por consola todos los registros de la tabla.
     * @return true si se mostraron datos, false en caso contrario.
     */
    @Override
    public boolean Mostrar() {
        String sql = "SELECT * FROM planes_mantenimiento";
        try (Connection c = con.Conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            System.out.println("\nID | Nombre | Descripción | Frecuencia");
            System.out.println("---------------------------------------");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | %s\n", 
                    rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al mostrar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Crea un nuevo registro de plan de mantenimiento.
     * @return true si la inserción fue exitosa.
     */
    @Override
    protected boolean Crear() {
        System.out.print("Nombre del plan: ");
        String nombre = sc.nextLine();
        System.out.print("Descripción: ");
        String desc = sc.nextLine();
        System.out.print("Frecuencia (ej. Mensual): ");
        String frec = sc.nextLine();

        String sql = "INSERT INTO planes_mantenimiento (nombre, descripcion, frecuencia) VALUES (?, ?, ?)";
        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, desc);
            ps.setString(3, frec);
            ps.executeUpdate();
            System.out.println("Plan creado con éxito.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al crear: " + e.getMessage());
            return false;
        }
    }

    /**
     * Modifica un plan existente solicitando su ID.
     * @return true si se actualizó correctamente.
     */
    @Override
    protected boolean Modificar() {
        System.out.print("ID del plan a modificar: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();

        String sql = "UPDATE planes_mantenimiento SET nombre = ? WHERE id = ?";
        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, id);
            int filas = ps.executeUpdate();
            if (filas > 0) System.out.println("Plan actualizado.");
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un plan de mantenimiento de la base de datos.
     * @return true si se borró el registro.
     */
    @Override
    protected boolean Borrar() {
        System.out.print("ID del plan a borrar: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM planes_mantenimiento WHERE id = ?";
        try (Connection c = con.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) System.out.println("Plan eliminado.");
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al borrar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<Object> Recibir() {
        // Implementación opcional para devolver listas de objetos si es necesario
        return null;
    }
}
