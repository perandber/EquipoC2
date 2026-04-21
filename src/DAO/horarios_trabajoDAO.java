package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;
import Objetos.horarios_trabajo;

/**
 * @author Sergio
 * Clase DAO (Data Access Object) para la tabla horarios_trabajo.
 * Su responsabilidad es comunicarse con la base de datos: leer e insertar registros de horarios. */
public class horarios_trabajoDAO extends interfaces {

    // Scanner para leer la entrada del usuario por teclado cuando se piden datos
    Scanner sc = new Scanner(System.in);

    // Lista tipada que almacenará los objetos horarios_trabajo recuperados de la BD
    // Se declara aquí como atributo para que esté disponible en toda la clase
    ArrayList<horarios_trabajo> horarios = new ArrayList<horarios_trabajo>();

    // Menu() muestra las opciones disponibles en un bucle hasta que el usuario pulse 0 para salir
    @Override
    public void Menu() {
        int opcion;
        do {
            System.out.println("\n--- 5. HORARIOS DE TRABAJO ---");
            System.out.println("1. Ver todos los horarios");
            System.out.println("2. Añadir nuevo turno");
            System.out.println("0. Volver");
            System.out.print("Opcion: ");

            // Usamos try-catch porque si el usuario escribe algo que no es un número, parseInt lanzaría una excepción
            // Si ocurre ese error, asignamos -1 para que el if no ejecute nada
            try { opcion = Integer.parseInt(sc.nextLine()); } catch (Exception e) { opcion = -1; }

            if (opcion == 1) Mostrar();
            else if (opcion == 2) Crear();
        } while (opcion != 0); // El bucle continúa mientras no se pulse 0
    }

    // Recibir() se conecta a la BD, ejecuta la consulta SELECT y convierte cada fila en un objeto horarios_trabajo
    // Devuelve ArrayList<Object> porque así lo exige la clase abstracta; los objetos horarios_trabajo son válidos porque toda clase hereda de Object
    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<Object>(); // Lista vacía que iremos llenando

        // Consulta SQL que trae las columnas que necesitamos de la tabla
        String sql = "SELECT id_horario, nombre, hora_inicio, hora_fin, lunes, martes, miercoles, jueves, viernes FROM horarios_trabajo";

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
                int     idHorario  = rs.getInt("id_horario");
                String  nombre     = rs.getString("nombre");
                Time    horaInicio = rs.getTime("hora_inicio"); // Time porque la BD guarda solo hora, sin fecha
                Time    horaFin    = rs.getTime("hora_fin");
                boolean lunes      = rs.getBoolean("lunes");    // Los días se guardan como 0/1 en la BD; getBoolean los convierte a true/false
                boolean martes     = rs.getBoolean("martes");
                boolean miercoles  = rs.getBoolean("miercoles");
                boolean jueves     = rs.getBoolean("jueves");
                boolean viernes    = rs.getBoolean("viernes");

                // Con los datos de la fila construimos un objeto horarios_trabajo y lo metemos en la lista
                lista.add(new horarios_trabajo(idHorario, nombre, horaInicio, horaFin,
                          lunes, martes, miercoles, jueves, viernes));
            }

        } catch (SQLException e) {
            // Si hay cualquier error de base de datos, lo mostramos por consola y hacemos visible el detalle
            System.out.println("Error al recibir datos de horarios");
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
    @Override
    protected boolean Crear() {
        System.out.print("Nombre del horario (ej. Turno Mañana): ");
        String nom = sc.nextLine();
        System.out.print("Hora inicio (HH:MM:SS): ");
        String inicio = sc.nextLine();
        System.out.print("Hora fin (HH:MM:SS): ");
        String fin = sc.nextLine();

        // Los días lunes a viernes se insertan directamente como 1 (true) sin pedírselo al usuario
        // Los '?' son parámetros que se rellenan después para evitar inyección SQL
        String sql = "INSERT INTO horarios_trabajo (nombre, hora_inicio, hora_fin, lunes, martes, miercoles, jueves, viernes) VALUES (?, ?, ?, 1, 1, 1, 1, 1)";

        // try-with-resources: cierra automáticamente la conexión y el PreparedStatement al terminar el bloque
        try (Connection c = conexion.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setTime(2, Time.valueOf(inicio)); // Time.valueOf convierte el texto "HH:MM:SS" al tipo Time que necesita SQL
            ps.setTime(3, Time.valueOf(fin));
            ps.executeUpdate(); // executeUpdate() se usa para INSERT, UPDATE y DELETE (no devuelve filas)
            System.out.println("Horario creado.");
            return true;
        } catch (SQLException e) { return false; }
    }

    // Mostrar() llama a Recibir() para obtener todos los registros y los imprime por consola
    // Iteramos sobre Object; al hacer println se llama automáticamente al toString() definido en horarios_trabajo
    @Override
    public boolean Mostrar() {
        ArrayList<Object> datos = Recibir();
        if (datos.isEmpty()) {
            System.out.println("No hay horarios registrados.");
        } else {
            for (Object h : datos) {
                System.out.println(h); // Llama internamente a toString() del objeto
            }
        }
        return true;
    }

    // Borrar() y Modificar() no están implementados en esta versión; devuelven false para cumplir con el contrato de la clase abstracta
    @Override protected boolean Borrar() { return false; }
    @Override protected boolean Modificar() { return false; }
}