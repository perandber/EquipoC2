package Comun;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Conexion a la base de datos externa
 * @author Perceval */
public class conexion {

    //Base de datos
    /**[Tipo de base de datos] :// [direccion ip del servidor] : [puerto] / [nombre de base de datos]*/
    final private static String dir = "jdbc:mysql://localhost:3306/gestiontaller";
    /**nombre de usario a usar*/
    final private static String user = "root";
    /**contraseña de usario a usar*/
    final private static String pwd = "";  
    
	public static Connection Conectar() {
		Connection conexion = null;
		
		//Iniciar la conexion
		try {
			//juntar datos de la conexion, usuario y contraseña
			conexion = (Connection) DriverManager.getConnection(dir, user, pwd);
		} catch (SQLException e) {
            System.out.println("Error al intentar iniciar conexion a la base de datos");			
		}
		return conexion;
	}
}
