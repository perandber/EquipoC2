package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;
import Objetos.incidencias;

/**
 * Clase encargada de conectar con la base de datos para gestionar las incidencias 
 * o averias de las maquinas (fallos, roturas, etc).
 * Permite listar los fallos, reportar nuevos y marcarlos como resueltos.
 * 
 * @author Sergio
 */
public class incidenciasDAO extends interfaces {

    private Scanner sc = new Scanner(System.in);

    /**
     * Muestra un menu por pantalla con las opciones para ver fallos, 
     * reportar nuevas averias o solucionarlas, y lee la opcion del usuario.
     */
    @Override
    public void Menu() {
        int opcion;
        do {
            System.out.println("\n--- MENU INCIDENCIAS ---");
            System.out.println("1. Ver lista de incidencias");
            System.out.println("2. Reportar nueva averia");
            System.out.println("3. Resolver/Modificar incidencia");
            System.out.println("4. Borrar incidencia");
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
     * Se conecta a la base de datos y trae todas las incidencias registradas 
     * para convertirlas en una lista de objetos manejables.
     * 
     * @return Una lista con todos los objetos de tipo incidencias encontrados.
     */
    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM incidencias";
        
        Connection con = conexion.Conectar();
        Statement st = null;
        ResultSet rs = null;

        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                incidencias i = new incidencias(
                    rs.getInt("id_incidencia"),
                    rs.getInt("id_maquina"),
                    rs.getString("descripcion"),
                    rs.getString("prioridad"),
                    rs.getString("estado"),
                    rs.getString("solucion_aplicada"),
                    rs.getTimestamp("fecha_resolucion")
                );
                lista.add(i);
            }
        } catch (SQLException e) {
            System.out.println("Error al recibir incidencias.");
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
     * Coge la lista de todas las incidencias y las imprime por consola 
     * para que el usuario pueda ver el estado de cada averia.
     * 
     * @return true si los datos se mostraron sin problemas.
     */
    @Override
    public boolean Mostrar() {
        ArrayList<Object> datos = Recibir();
        if (datos.isEmpty()) {
            System.out.println("No hay incidencias registradas.");
        } else {
            for (Object i : datos) {
                System.out.println(i.toString());
            }
        }
        return true;
    }

    /**
     * Pide al usuario el ID de la maquina rota y una descripcion del fallo 
     * para crear un nuevo ticket de incidencia en la base de datos.
     * 
     * @return true si la incidencia se guardo correctamente, false si no.
     */
    @Override
    protected boolean Crear() {
        System.out.println("\n-- Reportar Nueva Incidencia --");
        System.out.print("ID Maquina: ");
        int idM = Integer.parseInt(sc.nextLine());
        System.out.print("Descripcion del fallo: ");
        String desc = sc.nextLine();

        String sql = "INSERT INTO incidencias (id_maquina, descripcion, prioridad, estado) VALUES (?, ?, 'media', 'abierta')";

        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idM);
            ps.setString(2, desc);

            ps.executeUpdate();
            System.out.println("Incidencia registrada.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar incidencia.");
            return false;
        }
    }

    /**
     * Pide el ID de una incidencia, muestra sus datos y permite cambiar el estado, 
     * la prioridad o añadir una solucion detallada.
     * 
     * @return true si se actualizo bien, false si el ID no existe o fallo la conexion.
     */
    @Override
    protected boolean Modificar() {
        System.out.print("\nID de la incidencia a modificar/resolver: ");
        int id = Integer.parseInt(sc.nextLine());

        incidencias actual = buscarPorId(id);
        if (actual == null) {
            System.out.println("No se encontro la incidencia.");
            return false;
        }

        System.out.println("Datos actuales: " + actual);
        System.out.println("Nuevos datos (vacio para no cambiar):");

        System.out.print("Estado [" + actual.getEstado() + "]: ");
        String input = sc.nextLine();
        if (!input.isEmpty()) actual.setEstado(input);

        System.out.print("Prioridad [" + actual.getPrioridad() + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty()) actual.setPrioridad(input);

        System.out.print("Solucion [" + actual.getSolucionAplicada() + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty()) actual.setSolucionAplicada(input);

        String sql = "UPDATE incidencias SET estado=?, prioridad=?, solucion_aplicada=?, fecha_resolucion=NOW() WHERE id_incidencia=?";

        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, actual.getEstado());
            ps.setString(2, actual.getPrioridad());
            ps.setString(3, actual.getSolucionAplicada());
            ps.setInt(4, id);

            ps.executeUpdate();
            System.out.println("Incidencia actualizada.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar la incidencia.");
            return false;
        }
    }

    /**
     * Pide el ID de una incidencia y la borra por completo de la base de datos.
     * 
     * @return true si el borrado fue exitoso, false si el registro no existia.
     */
    @Override
    protected boolean Borrar() {
        System.out.print("\nID de la incidencia a borrar: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM incidencias WHERE id_incidencia = ?";

        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                System.out.println("Incidencia borrada.");
                return true;
            } else {
                System.out.println("No se encontro el ID.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al borrar incidencia.");
            return false;
        }
    }

    /**
     * Busca en la base de datos una incidencia concreta usando su numero de buscador.
     * 
     * @param id El numero identificador de la incidencia que queremos localizar.
     * @return El objeto con los datos de la incidencia, o null si no se encuentra.
     */
    private incidencias buscarPorId(int id) {
        String sql = "SELECT * FROM incidencias WHERE id_incidencia = ?";
        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new incidencias(
                    rs.getInt("id_incidencia"),
                    rs.getInt("id_maquina"),
                    rs.getString("descripcion"),
                    rs.getString("prioridad"),
                    rs.getString("estado"),
                    rs.getString("solucion_aplicada"),
                    rs.getTimestamp("fecha_resolucion")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar incidencia por ID.");
        }
        return null;
    }
}