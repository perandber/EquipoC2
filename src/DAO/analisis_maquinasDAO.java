package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Comun.conexion;
import Comun.interfaces;
import Objetos.analisis_maquinas;

/**
 * @author Sergio
 * Clase DAO (Data Access Object) para la tabla analisis_maquinas.
 * Su responsabilidad es comunicarse con la base de datos: leer, insertar y eliminar registros. */
public class analisis_maquinasDAO extends interfaces {

    // Scanner para leer la entrada del usuario por teclado cuando se piden datos
    Scanner sc = new Scanner(System.in);

    // Lista tipada que almacenará los objetos analisis_maquinas recuperados de la BD
    // Se declara aquí como atributo para que esté disponible en toda la clase
    ArrayList<analisis_maquinas> analisis = new ArrayList<analisis_maquinas>();

    // Menu() muestra las opciones disponibles en un bucle hasta que el usuario pulse 0 para salir
    @Override
    public void Menu() {
        int opcion;
        do {
            System.out.println("\n--- 16. ANALISIS DE MAQUINAS ---");
            System.out.println("1. Mostrar todos los analisis");
            System.out.println("2. Registrar nuevo analisis (Insert)");
            System.out.println("3. Eliminar un analisis (Delete)");
            System.out.println("0. Volver al menu principal");
            System.out.print("Opcion: ");

            // Usamos try-catch porque si el usuario escribe algo que no es un número, parseInt lanzaría una excepción
            // Si ocurre ese error, asignamos -1 para que el switch no ejecute nada
            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (Exception e) { opcion = -1; }

            // Según la opción elegida, llamamos al método correspondiente
            switch (opcion) {
                case 1 -> Mostrar();
                case 2 -> Crear();
                case 3 -> Borrar();
            }
        } while (opcion != 0); // El bucle continúa mientras no se pulse 0
    }

    // Recibir() se conecta a la BD, ejecuta la consulta SELECT y convierte cada fila en un objeto analisis_maquinas
    // Devuelve ArrayList<Object> porque así lo exige la clase abstracta; los objetos analisis_maquinas son válidos porque toda clase hereda de Object
    @Override
    public ArrayList<Object> Recibir() {
        ArrayList<Object> lista = new ArrayList<Object>(); // Lista vacía que iremos llenando

        // Consulta SQL que trae todas las filas de la tabla
        String sql = "SELECT * FROM analisis_maquinas";

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
                int       idAnalisis     = rs.getInt("id_analisis");
                int       idMaquina      = rs.getInt("id_maquina");
                String    nombreVariable = rs.getString("nombre_variable");
                double    valor          = rs.getDouble("valor");
                String    unidadMedida   = rs.getString("unidad_medida");
                Timestamp fechaRegistro  = rs.getTimestamp("fecha_registro"); // Timestamp porque la BD guarda fecha y hora

                // Con los datos de la fila construimos un objeto analisis_maquinas y lo metemos en la lista
                lista.add(new analisis_maquinas(idAnalisis, idMaquina,
                          nombreVariable, valor, unidadMedida, fechaRegistro));
            }

        } catch (SQLException e) {
            // Si hay cualquier error de base de datos, lo mostramos por consola y hacemos visible el detalle
            System.out.println("Error al recibir datos de analisis");
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

    // Mostrar() llama a Recibir() para obtener todos los registros y los imprime por consola
    // Iteramos sobre Object; al hacer println se llama automáticamente al toString() definido en analisis_maquinas
    @Override
    public boolean Mostrar() {
        ArrayList<Object> datos = Recibir();
        if (datos.isEmpty()) {
            System.out.println("No hay registros en la tabla.");
        } else {
            for (Object a : datos) {
                System.out.println(a); // Llama internamente a toString() del objeto
            }
        }
        return true;
    }

    // Crear() pide los datos al usuario y los inserta como una nueva fila en la tabla
    @Override
    protected boolean Crear() {
        System.out.print("ID de la maquina: ");
        int idM = Integer.parseInt(sc.nextLine());
        System.out.print("Nombre de la variable (ej. Presion): ");
        String var = sc.nextLine();
        System.out.print("Valor medido: ");
        double val = Double.parseDouble(sc.nextLine());
        System.out.print("Unidad de medida (ej. bar): ");
        String uni = sc.nextLine();

        // Los '?' son parámetros que se rellenan después con setInt, setString, etc.
        // Esto evita inyección SQL y hace el código más seguro que concatenar Strings directamente
        // NOW() es una función de MySQL que inserta automáticamente la fecha y hora actuales
        String sql = "INSERT INTO analisis_maquinas (id_maquina, nombre_variable, valor, unidad_medida, fecha_registro) VALUES (?, ?, ?, ?, NOW())";

        // try-with-resources: cierra automáticamente la conexión y el PreparedStatement al terminar el bloque
        try (Connection c = conexion.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // Asignamos cada '?' en el mismo orden en que aparecen en el SQL
            ps.setInt(1, idM);
            ps.setString(2, var);
            ps.setDouble(3, val);
            ps.setString(4, uni);

            ps.executeUpdate(); // executeUpdate() se usa para INSERT, UPDATE y DELETE (no devuelve filas)
            System.out.println("Analisis guardado correctamente.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Borrar() pide el ID del registro que se quiere eliminar y lo borra de la tabla
    @Override
    protected boolean Borrar() {
        System.out.print("Introduce el ID del analisis a borrar: ");
        int id = Integer.parseInt(sc.nextLine());

        // DELETE con WHERE para borrar solo el registro con ese ID concreto
        String sql = "DELETE FROM analisis_maquinas WHERE id_analisis = ?";

        try (Connection c = conexion.Conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id); // Sustituimos el '?' por el ID introducido
            ps.executeUpdate();
            System.out.println("Registro eliminado.");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // Modificar() no está implementado en esta versión; devuelve false para cumplir con el contrato de la clase abstracta
    @Override
    protected boolean Modificar() {
        return false;
    }
}