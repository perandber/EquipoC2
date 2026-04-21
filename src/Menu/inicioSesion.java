package Menu;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import Comun.conexion;
import DAO.usuariosDAO;
import Objetos.usuario;

/**Iniciar sesion, si la sesion es correcta, enviar el usuario al menu principal
 * * @author Perceval*/
public class inicioSesion {
	/**Usuarios en la base de datos*/
	static ArrayList<usuario> usuarios = new ArrayList<usuario>();
	
	static usuariosDAO usuariosDao = new usuariosDAO();
	static principal menu = new principal();
	static Scanner sc = new Scanner (System.in); //Crear escaner
	
	/**Iniciar sesion
	 * El usuario a de tener el rol "tecnico
	 * @author Perceval"*/
	public static void main(String[] args) {
		//Añadir usurios de la base de datos externa a local
		ArrayList<Object> usuariosTemp = new ArrayList<Object>();
		for (Object usuario : usuariosTemp) {
			usuarios.add((Objetos.usuario) usuario);
		}
		
		boolean exito = false;
        Connection con = conexion.Conectar();
        Statement stat = null;
        ResultSet rs = null;
        
        String usuario;
        String contrasenya;

        do {
        	
        	System.out.print("Introduzca el nombre de usuario: ");
    		usuario = sc.nextLine();
    		System.out.print("Introduzca la contraseña: ");
    		contrasenya = sc.nextLine();
    		
            try {
                stat = con.createStatement();
    			
                //query a ejecutar
                //comprueba si hay lineas con el usuario y contraseña introducidos
                rs = stat.executeQuery("select * from usuarios where nombre = '"+usuario+"' and password_hash = '"+contrasenya+"' and rol = 'tecnico' ");
                //pasa a verdadero si hay por lo menos una linea
                exito = rs.next();
            } catch (SQLException e) {
            	e.printStackTrace();
    		}
            
            if (!exito) {
            	//Inicio de sesion no exitoso
            	System.out.println("No existe ningun usuario tecnico con estos datos");
            }
        } while (!exito);    
        //Inicio de sesion exitoso
       	System.out.println("Bienvenido");
       	menu.menu();
	}

}
