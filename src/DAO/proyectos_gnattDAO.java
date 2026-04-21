package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;
import Objetos.proyectos_gantt;

/**
 * @author Sergio
 * Clase DAO (Data Access Object) para la tabla proyectos_gantt.
 * Su responsabilidad es comunicarse con la base de datos: leer e insertar proyectos del diagrama de Gantt. */
public class proyectos_gnattDAO extends interfaces {

    // Scanner para leer la entrada del usuario por teclado cuando se piden datos
    Scanner sc = new Scanner(System.in);

    // Lista tipada que almacenará los objetos proyectos_gantt recuperados de la BD
    // Se declara aquí como atributo para que esté disponible en toda la clase
    ArrayList<proyectos_gantt> proyectos = new ArrayList<proyectos_gantt>();

    // Menu() muestra las opciones disponibles en un bucle hasta que el usuario pulse 0 para salir
    @Override
    public void Menu() {
        int op;
        do {
            System.out.println("\n--- 15. PROYECTOS GANTT ---");
            System.out.println("1. Listar proyectos activos");
            System.out.println("2. Crear nuevo proyecto");
            System.out.println("0. Volver");

            // Usamos try-catch porque si el usuario escribe algo que no es un número, parseInt lanzaría una excepción
            // Si ocurre ese error, asignamos -1 para que el if no ejecute nada
            try { op = Integer.parseInt(sc.nextLine()); } catch (Exception e) { op = -1; }

            if (op == 1) Mostrar();
            else if (op == 2) Crear();
        } while (op != 0); // El bucle continúa mientras no se pulse 0
    }

    // Recibir() se conecta a la BD, ejecuta la consulta SELECT y convierte cada fila en un objeto proyectos_gantt
    // Devuelve ArrayList<Object> porque así lo exige la clase abstracta; los objetos proyectos_gantt son válidos porque toda clase hereda de Object
    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<Object>(); // Lista vacía que iremos llenando

        // Consulta SQL que trae las columnas que necesitamos de la tabla
        String sql = "SELECT id_proyecto, nombre, fecha_inicio, estado FROM proyectos_gantt";

        // Declaramos la conexión y los objetos SQL fuera del try para poder cerrarlos en el finally
        Connection con = conexion.Conectar(); // Abre la conexión con la base de datos
        Statement st = null;  // Statement sirve para enviar la consulta SQL a la BD
        ResultSet rs = null;  // ResultSet es el "cursor" que recorre las filas devueltas por la consulta

        try {
            st = con.createStatement();  // Creamos el Statement a partir de la conexión abierta
            rs = st.executeQuery(sql);   // Enviamos el SELECT y el resultado queda en rs

            // rs.next() avanza una fila cada vez; devuelve false cuando no hay más filas
            while (rs.next()) {
                // Leemos cada columna de la fila actual y la guardamos en una variable local
                int    idProyecto  = rs.getInt("id_proyecto");
                String nombre      = rs.getString("nombre");
                Date   fechaInicio = rs.getDate("fecha_inicio"); // Date porque la BD guarda solo fecha, sin hora
                String estado      = rs.getString("estado");

                // Con los datos de la fila construimos un objeto proyectos_gantt y lo metemos en la lista
                lista.add(new proyectos_gantt(idProyecto, nombre, fechaInicio, estado));
            }

        } catch (SQLException e) {
            // Si hay cualquier error de base de datos, lo mostramos por consola y hacemos visible el detalle
            System.out.println("Error al recibir datos de proyectos");
            e.printStackTrace();
        } finally {
            // El bloque finally se ejecuta SIEMPRE, aunque haya habido un error
            // Es importante cerrar los recursos para no dejar conexiones abiertas en la BD
            try {
                if (rs  != null) rs.close();  // Cerramos el cursor de resultados
                if (st  != null) st.close();  // Cerramos el statement
                if (con != null) con.close(); // Cerramos la conexión con la BD
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion a la base de datos");
            }
        }

        return lista; // Devolvemos la lista con todos los objetos, o vacía si hubo error
    }

    // Crear() pide los datos al usuario y los inserta como una nueva fila en la tabla
    // El estado se fija como 'planificado' por defecto al crear un proyecto nuevo
    @Override
    protected boolean Crear() {
        System.out.print("Nombre del nuevo proyecto: ");
        String nom = sc.nextLine();
        System.out.print("Fecha de inicio (YYYY-MM-DD): ");
        String inicio = sc.nextLine();

        // 'planificado' se inserta como valor fijo; es el estado inicial de todo proyecto
        // Los '?' son parámetros que se rellenan después para evitar inyección SQL
        String sql = "INSERT INTO proyectos_gantt (nombre, fecha_inicio, estado) VALUES (?, ?, 'planificado')";

        // try-with-resources: cierra automáticamente la conexión y el PreparedStatement al terminar el bloque
        try (Connection c = conexion.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setDate(2, Date.valueOf(inicio)); // Date.valueOf convierte el texto "YYYY-MM-DD" al tipo Date que necesita SQL
            ps.executeUpdate(); // executeUpdate() se usa para INSERT, UPDATE y DELETE (no devuelve filas)
            System.out.println("Proyecto añadido al diagrama.");
            return true;
        } catch (SQLException e) { return false; }
    }

    // Mostrar() llama a Recibir() para obtener todos los registros y los imprime por consola
    // Iteramos sobre Object; al hacer println se llama automáticamente al toString() definido en proyectos_gantt
    @Override
    public boolean Mostrar() {
        ArrayList<Object> datos = Recibir();
        if (datos.isEmpty()) {
            System.out.println("No hay proyectos registrados.");
        } else {
            for (Object p : datos) {
                System.out.println(p); // Llama internamente a toString() del objeto
            }
        }
        return true;
    }

    // Borrar() y Modificar() no están implementados en esta versión; devuelven false para cumplir con el contrato de la clase abstracta
    @Override protected boolean Borrar() { return false; }
    @Override protected boolean Modificar() { return false; }
}