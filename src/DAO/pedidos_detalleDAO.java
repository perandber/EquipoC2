package DAO;

import java.sql.*;
import java.util.Scanner;
import Comun.conexion;
import java.util.ArrayList;
import Comun.interfaces;

/**
 * @author Cristian*/
public class pedidos_detalleDAO extends interfaces {
    private conexion con = new conexion();
    private Scanner sc = new Scanner(System.in);

	/**
    * Menú para consultar o añadir líneas de detalle a pedidos existentes.
    */
    @Override
    public void Menu() {
        System.out.println("\n--- DETALLES DE PEDIDO ---");
        System.out.println("1. Ver artículos de un pedido");
        System.out.println("2. Añadir línea de detalle");
        System.out.println("0. Volver");

        int op = Integer.parseInt(sc.nextLine());
        try (Connection c = con.Conectar()) {
            if (op == 1) {
                System.out.print("ID Pedido a consultar: ");
                int id = Integer.parseInt(sc.nextLine());
                PreparedStatement ps = c.prepareStatement("SELECT * FROM pedidos_detalle WHERE id_pedido = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) System.out.println("Producto: " + rs.getString("producto") + " | Cant: " + rs.getInt("cantidad"));
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }
    
    /**
     * Filtra y muestra los artículos pertenecientes a un pedido específico.
     * @return true si la consulta por ID fue exitosa.
     */
    @Override
    public boolean Mostrar() {
        System.out.print("Ingrese ID del pedido para ver detalles: ");
        int id = Integer.parseInt(sc.nextLine());
        try (Connection c = con.Conectar(); PreparedStatement ps = c.prepareStatement("SELECT * FROM pedidos_detalle WHERE id_pedido = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("ID Detalle: " + rs.getInt("id") + " | Producto: " + rs.getString("producto") + " | Cant: " + rs.getInt("cantidad"));
            }
            return true;
        } catch (SQLException e) { return false; }
    }

    /**
     * Lista todos los nombres de productos presentes en los detalles de pedidos.
     * @return ArrayList de strings con los nombres de productos.
     */
    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> detalles = new ArrayList<>();
        try (Connection c = con.Conectar(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM pedidos_detalle")) {
            while (rs.next()) { detalles.add(rs.getString("producto")); }
        } catch (SQLException e) { }
        return detalles;
    }

    /**
     * Añade una nueva línea de producto a un pedido existente.
     * @return true si la inserción se realizó correctamente.
     */
    @Override
    protected boolean Crear() {
        System.out.print("ID Pedido Relacionado: ");
        int idPed = Integer.parseInt(sc.nextLine());
        System.out.print("Nombre del Producto: ");
        String prod = sc.nextLine();
        System.out.print("Cantidad: ");
        int cant = Integer.parseInt(sc.nextLine());
        String sql = "INSERT INTO pedidos_detalle (id_pedido, producto, cantidad) VALUES (?, ?, ?)";
        try (Connection c = con.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idPed);
            ps.setString(2, prod);
            ps.setInt(3, cant);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    /**
     * Elimina una línea de detalle específica.
     * @return true si se borró la línea correctamente.
     */
    @Override
    protected boolean Borrar() {
        System.out.print("ID de línea de detalle a borrar: ");
        int id = Integer.parseInt(sc.nextLine());
        try (Connection c = con.Conectar(); PreparedStatement ps = c.prepareStatement("DELETE FROM pedidos_detalle WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    /**
     * Actualiza el producto o la cantidad de una línea de detalle.
     * @return true si la modificación fue exitosa.
     */
    @Override
    protected boolean Modificar() {
        System.out.print("ID de detalle a modificar: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Nuevo Producto: ");
        String prod = sc.nextLine();
        System.out.print("Nueva Cantidad: ");
        int cant = Integer.parseInt(sc.nextLine());
        String sql = "UPDATE pedidos_detalle SET producto = ?, cantidad = ? WHERE id = ?";
        try (Connection c = con.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, prod);
            ps.setInt(2, cant);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}
