package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;
import Objetos.incidencias;

/**
 * @author Sergio
 * Clase DAO (Data Access Object) para la tabla incidencias.
 * Su responsabilidad es comunicarse con la base de datos: leer registros, reportar nuevas averías y marcarlas como resueltas. */
public class incidenciasDAO extends interfaces {

    // Scanner para leer la entrada del usuario por teclado cuando se piden datos
    Scanner sc = new Scanner(System.in);

    // Lista tipada que almacenará los objetos incidencias recuperados de la BD
    // Se declara aquí como atributo para que esté disponible en toda la clase
    ArrayList<incidencias> listaIncidencias = new ArrayList<incidencias>();

    // Menu() muestra las opciones disponibles en un bucle hasta que el usuario pulse 0 para salir
    @Override
    public void Menu() {
        int op;
        do {
            System.out.println("\n--- 6. INCIDENCIAS ---");
            System.out.println("1. Ver lista de fallos");
            System.out.println("2. Reportar nueva averia");
            System.out.println("3. Resolver incidencia (Update)");
            System.out.println("0. Volver");

            // Usamos try-catch porque si el usuario escribe algo que no es un número, parseInt lanzaría una excepción
            // Si ocurre ese error, asignamos -1 para que el if no ejecute nada
            try { op = Integer.parseInt(sc.nextLine()); } catch (Exception e) { op = -1; }

            if (op == 1) Mostrar();
            else if (op == 2) Crear();
            else if (op == 3) Modificar();
        } while (op != 0); // El bucle continúa mientras no se pulse 0
    }

    // Recibir() se conecta a la BD, ejecuta la consulta SELECT y convierte cada fila en un objeto incidencias
    // Devuelve ArrayList<Object> porque así lo exige la clase abstracta; los objetos incidencias son válidos porque toda clase hereda de Object
    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<Object>(); // Lista vacía que iremos llenando

        // Consulta SQL que trae las columnas que necesitamos; incluimos solucion_aplicada y fecha_resolucion
        // porque pueden estar rellenas si la incidencia ya fue resuelta
        String sql = "SELECT id_incidencia, id_maquina, descripcion, prioridad, estado, solucion_aplicada, fecha_resolucion FROM incidencias";

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
                int       idIncidencia    = rs.getInt("id_incidencia");
                int       idMaquina       = rs.getInt("id_maquina");
                String    descripcion     = rs.getString("descripcion");
                String    prioridad       = rs.getString("prioridad");
                String    estado          = rs.getString("estado");
                String    solucionAplicada = rs.getString("solucion_aplicada"); // Puede ser null si la incidencia no está resuelta aún
                Timestamp fechaResolucion = rs.getTimestamp("fecha_resolucion"); // Timestamp porque la BD guarda fecha y hora; también puede ser null

                // Con los datos de la fila construimos un objeto incidencias y lo metemos en la lista
                lista.add(new incidencias(idIncidencia, idMaquina, descripcion,
                          prioridad, estado, solucionAplicada, fechaResolucion));
            }

        } catch (SQLException e) {
            // Si hay cualquier error de base de datos, lo mostramos por consola y hacemos visible el detalle
            System.out.println("Error al recibir datos de incidencias");
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

    // Crear() pide los datos mínimos al usuario y crea una nueva incidencia en la tabla
    // La prioridad se fija como 'media' y el estado como 'abierta' por defecto
    @Override
    protected boolean Crear() {
        System.out.print("ID de la maquina averiada: ");
        int idM = Integer.parseInt(sc.nextLine());
        System.out.print("Describe el problema: ");
        String desc = sc.nextLine();

        // 'media' y 'abierta' se insertan directamente como valores fijos sin pedírselos al usuario
        // Los '?' son parámetros que se rellenan después para evitar inyección SQL
        String sql = "INSERT INTO incidencias (id_maquina, descripcion, prioridad, estado) VALUES (?, ?, 'media', 'abierta')";

        // try-with-resources: cierra automáticamente la conexión y el PreparedStatement al terminar el bloque
        try (Connection c = conexion.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idM);
            ps.setString(2, desc);
            ps.executeUpdate(); // executeUpdate() se usa para INSERT, UPDATE y DELETE (no devuelve filas)
            System.out.println("Incidencia reportada correctamente.");
            return true;
        } catch (SQLException e) { return false; }
    }

    // Modificar() sirve para cerrar una incidencia: cambia su estado a 'resuelta' y graba la solución aplicada
    @Override
    protected boolean Modificar() {
        System.out.print("ID de la incidencia a cerrar: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Solucion aplicada: ");
        String sol = sc.nextLine();

        // UPDATE que escribe la solución, cambia el estado y pone la fecha actual de resolución con NOW()
        String sql = "UPDATE incidencias SET estado = 'resuelta', solucion_aplicada = ?, fecha_resolucion = NOW() WHERE id_incidencia = ?";

        // try-with-resources: cierra automáticamente la conexión y el PreparedStatement al terminar el bloque
        try (Connection c = conexion.Conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sol); // Primer '?': texto con la solución
            ps.setInt(2, id);     // Segundo '?': ID del registro a actualizar
            ps.executeUpdate();
            System.out.println("Incidencia marcada como RESUELTA.");
            return true;
        } catch (SQLException e) { return false; }
    }

    // Mostrar() llama a Recibir() para obtener todos los registros y los imprime por consola
    // Iteramos sobre Object; al hacer println se llama automáticamente al toString() definido en incidencias
    @Override
    public boolean Mostrar() {
        ArrayList<Object> datos = Recibir();
        if (datos.isEmpty()) {
            System.out.println("No hay incidencias registradas.");
        } else {
            for (Object inc : datos) {
                System.out.println(inc); // Llama internamente a toString() del objeto
            }
        }
        return true;
    }

    // Borrar() no está implementado en esta versión; devuelve false para cumplir con el contrato de la clase abstracta
    @Override protected boolean Borrar() { return false; }
}