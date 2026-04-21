package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;
import Objetos.proyectos_gantt;

/**
 * Clase encargada de conectar con la base de datos para gestionar los 
 * proyectos del diagrama de Gantt (nombres, fechas de inicio y estados).
 * Permite listar, registrar, modificar y borrar proyectos.
 * 
 * @author Sergio
 */
public class proyectos_gnattDAO extends interfaces {

    private Scanner sc = new Scanner(System.in);

    /**
     * Muestra un menu por pantalla con todas las opciones disponibles para 
     * gestionar los proyectos de Gantt y recoge la seleccion del usuario.
     */
    @Override
    public void Menu() {
        int opcion;
        do {
            System.out.println("\n--- MENU PROYECTOS GANTT ---");
            System.out.println("1. Listar proyectos");
            System.out.println("2. Registrar nuevo proyecto");
            System.out.println("3. Modificar un proyecto");
            System.out.println("4. Borrar un proyecto");
            System.out.println("0. Volver");
            System.out.print("Opcion: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1: Mostrar(); break;
                case 2: Crear(); break;
                case 3: Modificar(); break;
                case 4: Borrar(); break;
                case 0: System.out.println("Volviendo..."); break;
                default: System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    /**
     * Se conecta a la base de datos y recupera todos los proyectos 
     * guardados para convertirlos en una lista de objetos de Java.
     * 
     * @return Una lista con todos los objetos de tipo proyectos_gantt encontrados.
     */
    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM proyectos_gantt";
        
        Connection con = conexion.Conectar();
        Statement st = null;
        ResultSet rs = null;

        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                proyectos_gantt p = new proyectos_gantt(
                    rs.getInt("id_proyecto"),
                    rs.getString("nombre"),
                    rs.getDate("fecha_inicio"),
                    rs.getString("estado")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al recibir proyectos.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion.");
            }
        }
        return lista;
    }

    /**
     * Coge la lista de proyectos de la base de datos y los imprime por 
     * consola para que el usuario pueda ver su informacion general.
     * 
     * @return true si los proyectos se mostraron correctamente.
     */
    @Override
    public boolean Mostrar() {
        ArrayList<Object> datos = Recibir();
        if (datos.isEmpty()) {
            System.out.println("No hay proyectos registrados.");
        } else {
            for (Object p : datos) {
                System.out.println(p.toString());
            }
        }
        return true;
    }

    /**
     * Pide al usuario el nombre del proyecto y su fecha de inicio para 
     * guardarlo como un nuevo registro planificado en la base de datos.
     * 
     * @return true si el proyecto se guardo con exito, false si hubo algun error.
     */
    @Override
    protected boolean Crear() {
        System.out.println("\n-- Nuevo Proyecto --");
        System.out.print("Nombre del proyecto: ");
        String nom = sc.nextLine();
        System.out.print("Fecha inicio (YYYY-MM-DD): ");
        String inicio = sc.nextLine();

        String sql = "INSERT INTO proyectos_gantt (nombre, fecha_inicio, estado) VALUES (?, ?, 'planificado')";

        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nom);
            ps.setDate(2, Date.valueOf(inicio));

            ps.executeUpdate();
            System.out.println("Proyecto guardado.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al guardar proyecto.");
            return false;
        }
    }

    /**
     * Pide el ID de un proyecto existente, muestra su estado actual y permite 
     * cambiar sus datos básicos. Si no se escribe nada, se mantiene el dato antiguo.
     * 
     * @return true si la actualizacion fue bien, false si el ID no existe o fallo el sistema.
     */
    @Override
    protected boolean Modificar() {
        System.out.print("\nID del proyecto a modificar: ");
        int id = Integer.parseInt(sc.nextLine());

        proyectos_gantt actual = buscarPorId(id);
        if (actual == null) {
            System.out.println("No se encontro el proyecto.");
            return false;
        }

        System.out.println("Datos actuales: " + actual);
        System.out.println("Nuevos datos (vacio para no cambiar):");

        System.out.print("Nombre [" + actual.getNombre() + "]: ");
        String line = sc.nextLine();
        if (!line.isEmpty()) actual.setNombre(line);

        System.out.print("Fecha inicio [" + actual.getFechaInicio() + "]: ");
        line = sc.nextLine();
        if (!line.isEmpty()) actual.setFechaInicio(Date.valueOf(line));

        System.out.print("Estado [" + actual.getEstado() + "]: ");
        line = sc.nextLine();
        if (!line.isEmpty()) actual.setEstado(line);

        String sql = "UPDATE proyectos_gantt SET nombre=?, fecha_inicio=?, estado=? WHERE id_proyecto=?";

        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, actual.getNombre());
            ps.setDate(2, actual.getFechaInicio());
            ps.setString(3, actual.getEstado());
            ps.setInt(4, id);

            ps.executeUpdate();
            System.out.println("Proyecto actualizado.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar proyecto.");
            return false;
        }
    }

    /**
     * Pide al usuario el ID de un proyecto y lo elimina de forma definitiva 
     * de la tabla de la base de datos.
     * 
     * @return true si el borrado se realizo, false si el ID no existia o dio error.
     */
    @Override
    protected boolean Borrar() {
        System.out.print("\nID del proyecto a borrar: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM proyectos_gantt WHERE id_proyecto = ?";

        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                System.out.println("Proyecto eliminado.");
                return true;
            } else {
                System.out.println("No se encontro el ID.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al borrar el proyecto.");
            return false;
        }
    }

    /**
     * Busca en la base de datos un proyecto concreto usando su numero identificador.
     * 
     * @param id El numero de ID del proyecto que deseamos encontrar.
     * @return El objeto con los datos del proyecto si existe, o null si no se encuentra.
     */
    private proyectos_gantt buscarPorId(int id) {
        String sql = "SELECT * FROM proyectos_gantt WHERE id_proyecto = ?";
        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new proyectos_gantt(
                    rs.getInt("id_proyecto"),
                    rs.getString("nombre"),
                    rs.getDate("fecha_inicio"),
                    rs.getString("estado")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar proyecto por ID.");
        }
        return null;
    }
}