package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;
import Objetos.horarios_trabajo;

/**
 * Clase encargada de conectar con la base de datos para gestionar los horarios
 * de trabajo de los empleados (turnos de mañana, tarde, etc).
 * Permite listar, añadir, modificar y borrar estos horarios.
 * 
 * @author Sergio
 */
public class horarios_trabajoDAO extends interfaces {

    private Scanner sc = new Scanner(System.in);

    /**
     * Muestra un menu por pantalla con todas las opciones disponibles para 
     * gestionar los horarios y lee la seleccion del usuario.
     */
    @Override
    public void Menu() {
        int opcion;
        do {
            System.out.println("\n--- MENU HORARIOS TRABAJO ---");
            System.out.println("1. Ver todos los horarios");
            System.out.println("2. Añadir nuevo turno");
            System.out.println("3. Modificar un horario");
            System.out.println("4. Borrar un horario");
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
     * Se conecta a la base de datos y recupera todos los horarios 
     * guardados para convertirlos en una lista de objetos.
     * 
     * @return Una lista con todos los objetos de tipo horarios_trabajo encontrados.
     */
    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM horarios_trabajo";
        
        Connection con = conexion.Conectar();
        Statement st = null;
        ResultSet rs = null;

        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                horarios_trabajo h = new horarios_trabajo(
                    rs.getInt("id_horario"),
                    rs.getString("nombre"),
                    rs.getTime("hora_inicio"),
                    rs.getTime("hora_fin"),
                    rs.getBoolean("lunes"),
                    rs.getBoolean("martes"),
                    rs.getBoolean("miercoles"),
                    rs.getBoolean("jueves"),
                    rs.getBoolean("viernes")
                );
                lista.add(h);
            }
        } catch (SQLException e) {
            System.out.println("Error al recibir datos de horarios.");
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
     * Coge la lista de horarios de la base de datos y los imprime uno a uno 
     * por la consola para que el usuario pueda verlos.
     * 
     * @return true si el proceso de mostrar los datos termina correctamente.
     */
    @Override
    public boolean Mostrar() {
        ArrayList<Object> datos = Recibir();
        if (datos.isEmpty()) {
            System.out.println("No hay horarios registrados.");
        } else {
            for (Object h : datos) {
                System.out.println(h.toString());
            }
        }
        return true;
    }

    /**
     * Pide al usuario el nombre del nuevo turno y sus horas de inicio y fin 
     * para guardarlo como un nuevo registro en la base de datos.
     * 
     * @return true si el horario se guardo bien, false si hubo algun problema.
     */
    @Override
    protected boolean Crear() {
        System.out.println("\n-- Añadir Nuevo Horario --");
        System.out.print("Nombre del turno: ");
        String nom = sc.nextLine();
        System.out.print("Hora inicio (HH:MM:SS): ");
        String inicio = sc.nextLine();
        System.out.print("Hora fin (HH:MM:SS): ");
        String fin = sc.nextLine();

        String sql = "INSERT INTO horarios_trabajo (nombre, hora_inicio, hora_fin, lunes, martes, miercoles, jueves, viernes) VALUES (?, ?, ?, 1, 1, 1, 1, 1)";

        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nom);
            ps.setTime(2, Time.valueOf(inicio));
            ps.setTime(3, Time.valueOf(fin));

            ps.executeUpdate();
            System.out.println("Horario creado.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al crear horario.");
            return false;
        }
    }

    /**
     * Pide el ID de un horario existente, muestra sus datos actuales y permite 
     * cambiarlos. Si se deja un campo en blanco, se conserva el valor que ya tenia.
     * 
     * @return true si se actualizo correctamente, false si el ID no existe o fallo la conexion.
     */
    @Override
    protected boolean Modificar() {
        System.out.print("\nID del horario a modificar: ");
        int id = Integer.parseInt(sc.nextLine());

        horarios_trabajo actual = buscarPorId(id);
        if (actual == null) {
            System.out.println("No se encontro el horario.");
            return false;
        }

        System.out.println("Datos actuales: " + actual);
        System.out.println("Nuevos datos (vacio para no cambiar):");

        System.out.print("Nombre [" + actual.getNombre() + "]: ");
        String line = sc.nextLine();
        if (!line.isEmpty()) actual.setNombre(line);

        System.out.print("Hora inicio [" + actual.getHoraInicio() + "]: ");
        line = sc.nextLine();
        if (!line.isEmpty()) actual.setHoraInicio(Time.valueOf(line));

        System.out.print("Hora fin [" + actual.getHoraFin() + "]: ");
        line = sc.nextLine();
        if (!line.isEmpty()) actual.setHoraFin(Time.valueOf(line));

        String sql = "UPDATE horarios_trabajo SET nombre=?, hora_inicio=?, hora_fin=? WHERE id_horario=?";

        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, actual.getNombre());
            ps.setTime(2, actual.getHoraInicio());
            ps.setTime(3, actual.getHoraFin());
            ps.setInt(4, id);

            ps.executeUpdate();
            System.out.println("Horario actualizado.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar horario.");
            return false;
        }
    }

    /**
     * Pide al usuario el ID de un horario y lo borra definitivamente 
     * de la base de datos.
     * 
     * @return true si el borrado fue exitoso, false si el ID no se encontro o dio error.
     */
    @Override
    protected boolean Borrar() {
        System.out.print("\nID del horario a borrar: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM horarios_trabajo WHERE id_horario = ?";

        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                System.out.println("Horario borrado.");
                return true;
            } else {
                System.out.println("No se encontro el ID.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al borrar horario.");
            return false;
        }
    }

    /**
     * Busca en la base de datos un horario especifico usando su ID identificador.
     * 
     * @param id El numero de ID del horario que buscamos.
     * @return El objeto con los datos del horario si lo encuentra, o null si no existe.
     */
    private horarios_trabajo buscarPorId(int id) {
        String sql = "SELECT * FROM horarios_trabajo WHERE id_horario = ?";
        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new horarios_trabajo(
                    rs.getInt("id_horario"),
                    rs.getString("nombre"),
                    rs.getTime("hora_inicio"),
                    rs.getTime("hora_fin"),
                    rs.getBoolean("lunes"),
                    rs.getBoolean("martes"),
                    rs.getBoolean("miercoles"),
                    rs.getBoolean("jueves"),
                    rs.getBoolean("viernes")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar horario por ID.");
        }
        return null;
    }
}