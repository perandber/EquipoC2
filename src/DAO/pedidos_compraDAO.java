package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;

/**
 * @author Cristian*/
public class pedidos_compraDAO extends interfaces {
    private conexion con = new conexion();
    private Scanner sc = new Scanner(System.in);

	/**
    * Menú principal para la gestión de cabeceras de pedidos.
    */
    @Override
    public void Menu() {
        System.out.println("\n--- PEDIDOS DE COMPRA ---");
        System.out.println("1. Ver historial de pedidos");
        System.out.println("2. Registrar cabecera de pedido");
        System.out.println("0. Volver");
        
        int op = Integer.parseInt(sc.nextLine());
        try (Connection c = con.Conectar()) {
            if (op == 1) {
                ResultSet rs = c.createStatement().executeQuery("SELECT * FROM pedidos_compra");
                while(rs.next()) System.out.println("ID Pedido: " + rs.getInt(1) + " | Fecha: " + rs.getDate(2));
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }
    
    /**
     * Lista todas las cabeceras de pedidos registradas en el sistema.
     * @return true si la consulta se ejecutó correctamente.
     */
    @Override
    public boolean Mostrar() {
        String sql = "SELECT * FROM pedidos_compra";
        try (Connection c = con.Conectar(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt(1) + " | Fecha: " + rs.getDate(2));
            }
            return true;
        } catch (SQLException e) { return false; }
    }

    /**
     * Obtiene una lista de los identificadores de pedidos disponibles.
     * @return ArrayList con los IDs de los pedidos.
     */
    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<>();
        try (Connection c = con.Conectar(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM pedidos_compra")) {
            while (rs.next()) { lista.add(rs.getInt(1)); }
        } catch (SQLException e) { }
        return lista;
    }

    /**
     * Registra un nuevo pedido solicitando la fecha de emisión.
     * @return true si el registro fue exitoso.
     */
    @Override
    protected boolean Crear() {
        System.out.print("Fecha del pedido (YYYY-MM-DD): ");
        String fecha = sc.nextLine();
        String sql = "INSERT INTO pedidos_compra (fecha) VALUES (?)";
        try (Connection c = con.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(fecha));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    /**
     * Elimina la cabecera de un pedido de la base de datos.
     * @return true si se encontró y borró el ID.
     */
    @Override
    protected boolean Borrar() {
        System.out.print("ID de pedido a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());
        try (Connection c = con.Conectar(); PreparedStatement ps = c.prepareStatement("DELETE FROM pedidos_compra WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    /**
     * Modifica la fecha de un pedido existente.
     * @return true si se actualizó el registro.
     */
    @Override
    protected boolean Modificar() {
        System.out.print("ID de pedido a modificar: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Nueva fecha (YYYY-MM-DD): ");
        String fecha = sc.nextLine();
        try (Connection c = con.Conectar(); PreparedStatement ps = c.prepareStatement("UPDATE pedidos_compra SET fecha = ? WHERE id = ?")) {
            ps.setDate(1, java.sql.Date.valueOf(fecha));
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}
