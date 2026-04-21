package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;
import Objetos.analisis_maquinas;

/**
 * Clase encargada de conectar con la base de datos para gestionar los analisis 
 * que se les hacen a las maquinas (presion, temperatura, etc).
 * Permite listar, crear, modificar y borrar estos registros.
 * 
 * @author Sergio
 */
public class analisis_maquinasDAO extends interfaces {

    private Scanner sc = new Scanner(System.in);

    /**
     * Muestra un menu por pantalla con todas las opciones disponibles para 
     * gestionar los analisis y lee la opcion que elija el usuario.
     */
    @Override
    public void Menu() {
        int opcion;
        do {
            System.out.println("\n--- MENU ANALISIS MAQUINAS ---");
            System.out.println("1. Mostrar todos los registros");
            System.out.println("2. Crear nuevo analisis");
            System.out.println("3. Modificar un analisis");
            System.out.println("4. Borrar un analisis");
            System.out.println("0. Salir");
            System.out.print("Elige una opcion: ");

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
     * Se conecta a la base de datos y recupera todos los registros de analisis 
     * guardados para convertirlos en una lista de objetos.
     * 
     * @return Una lista con todos los objetos de tipo analisis_maquinas encontrados.
     */
    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT * FROM analisis_maquinas";
        
        Connection con = conexion.Conectar();
        Statement st = null;
        ResultSet rs = null;

        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                analisis_maquinas obj = new analisis_maquinas(
                    rs.getInt("id_analisis"),
                    rs.getInt("id_maquina"),
                    rs.getString("nombre_variable"),
                    rs.getDouble("valor"),
                    rs.getString("unidad_medida"),
                    rs.getTimestamp("fecha_registro")
                );
                lista.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Error al leer la base de datos.");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexion.");
            }
        }
        return lista;
    }

    /**
     * Coge la lista de registros de la base de datos y los imprime uno a uno 
     * por la consola para que el usuario pueda verlos.
     * 
     * @return true si el proceso de mostrar los datos termina correctamente.
     */
    @Override
    public boolean Mostrar() {
        ArrayList<Object> datos = Recibir();
        if (datos.isEmpty()) {
            System.out.println("No hay datos que mostrar.");
        } else {
            for (Object obj : datos) {
                System.out.println(obj.toString());
            }
        }
        return true;
    }

    /**
     * Pide al usuario por teclado los datos de un nuevo analisis (id maquina, variable, 
     * valor y unidad) y los guarda como una nueva fila en la base de datos.
     * 
     * @return true si el registro se guardo correctamente, false si hubo algun error.
     */
    @Override
    protected boolean Crear() {
        System.out.println("\n-- Registrar Nuevo Analisis --");
        System.out.print("ID Maquina: ");
        int idM = Integer.parseInt(sc.nextLine());
        System.out.print("Variable (ej: Temperatura): ");
        String var = sc.nextLine();
        System.out.print("Valor: ");
        double val = Double.parseDouble(sc.nextLine());
        System.out.print("Unidad (ej: Celsius): ");
        String uni = sc.nextLine();

        String sql = "INSERT INTO analisis_maquinas (id_maquina, nombre_variable, valor, unidad_medida, fecha_registro) VALUES (?, ?, ?, ?, NOW())";

        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idM);
            ps.setString(2, var);
            ps.setDouble(3, val);
            ps.setString(4, uni);

            ps.executeUpdate();
            System.out.println("Registro guardado con exito.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al guardar el registro.");
            return false;
        }
    }

    /**
     * Pide el ID de un analisis existente, muestra sus datos actuales y permite 
     * cambiarlos uno por uno. Si se deja un campo vacio, se mantiene el valor original.
     * 
     * @return true si la actualizacion fue bien, false si no se encontro el ID o fallo el proceso.
     */
    @Override
    protected boolean Modificar() {
        System.out.print("\nID del analisis a modificar: ");
        int id = Integer.parseInt(sc.nextLine());

        analisis_maquinas actual = buscarPorId(id);
        if (actual == null) {
            System.out.println("No se encontro el registro.");
            return false;
        }

        System.out.println("Datos actuales: " + actual);
        System.out.println("Introduce nuevos datos (deja vacio para no cambiar):");

        System.out.print("ID Maquina [" + actual.getIdMaquina() + "]: ");
        String input = sc.nextLine();
        if (!input.isEmpty()) actual.setIdMaquina(Integer.parseInt(input));

        System.out.print("Variable [" + actual.getNombreVariable() + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty()) actual.setNombreVariable(input);

        System.out.print("Valor [" + actual.getValor() + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty()) actual.setValor(Double.parseDouble(input));

        System.out.print("Unidad [" + actual.getUnidadMedida() + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty()) actual.setUnidadMedida(input);

        String sql = "UPDATE analisis_maquinas SET id_maquina=?, nombre_variable=?, valor=?, unidad_medida=? WHERE id_analisis=?";

        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, actual.getIdMaquina());
            ps.setString(2, actual.getNombreVariable());
            ps.setDouble(3, actual.getValor());
            ps.setString(4, actual.getUnidadMedida());
            ps.setInt(5, id);

            ps.executeUpdate();
            System.out.println("Registro actualizado.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar.");
            return false;
        }
    }

    /**
     * Pide al usuario el ID de un analisis y lo intenta eliminar permanentemente 
     * de la tabla en la base de datos.
     * 
     * @return true si el registro fue borrado, false si el ID no existia o dio error.
     */
    @Override
    protected boolean Borrar() {
        System.out.print("\nID del analisis a borrar: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM analisis_maquinas WHERE id_analisis = ?";

        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                System.out.println("Registro borrado.");
                return true;
            } else {
                System.out.println("No se encontro el ID.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al borrar.");
            return false;
        }
    }

    /**
     * Busca en la base de datos un analisis concreto usando su numero de ID.
     * 
     * @param id El numero identificador del analisis que queremos buscar.
     * @return El objeto con los datos del analisis si lo encuentra, o null si no existe.
     */
    private analisis_maquinas buscarPorId(int id) {
        String sql = "SELECT * FROM analisis_maquinas WHERE id_analisis = ?";
        try (Connection con = conexion.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new analisis_maquinas(
                    rs.getInt("id_analisis"),
                    rs.getInt("id_maquina"),
                    rs.getString("nombre_variable"),
                    rs.getDouble("valor"),
                    rs.getString("unidad_medida"),
                    rs.getTimestamp("fecha_registro")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por ID.");
        }
        return null;
    }
}