package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import Comun.conexion;
import Comun.interfaces;
import Objetos.herramientas;
import Objetos.tareas_herramientas;
import Objetos.tareas_mantenimiento;

/** Manejar, crear y borrar el objeto del mismo nombre
 * @author Perceval*/
public class tareas_herramientasDAO extends interfaces{

	ArrayList<tareas_herramientas> tareas = new ArrayList<tareas_herramientas>();
	ArrayList<tareas_mantenimiento> tareasMantenimiento = new ArrayList<tareas_mantenimiento>();
	ArrayList<herramientas> herramientasList = new ArrayList<herramientas>();
	
	static Scanner sc = new Scanner(System.in); //Crear escaner
	
	tareas_mantenimientoDAO tareasMDAO = new tareas_mantenimientoDAO();
	herramientasDAO herramientasDAO = new herramientasDAO();
	
	@Override
	public void Menu() {
		boolean bucle = true;
		int decision = 1000; //Opcion elejida por el usuario
		
		//Bucle, solo para si el usuario intenta cerrar la aplicacion
		while (bucle) {
			//Imprimir
			System.out.println("0. Salir\n"
					+ "1. Mostrar\n"
					+ "2. Nueva\n"
					+ "3. Actualizar\n"
					+ "4. Borrar\n"
					+ "5. Mostrar tareas de mantenimineto\n"
					+ "6. Mostrar herramientas");
			
			//recibir decision del usuario
			try {
				/*Recogido a traves de "Integer.parseInt(sc.nextLine())"
				 *dado que, recogerlo a traves de nextInt trae un problema de repeticion infinita con try/catch.*/
				decision = Integer.parseInt(sc.nextLine());
			} catch (java.lang.NumberFormatException e) {
				//Dejar variable a un valor que lo marcara como incorrecto.
				decision = 1000;
			}
			
			
			//LLevar usuario a su decision
			switch (decision) {
			case 0:
				//Salir
				bucle = false;
				System.out.println("Saliendo del programa");
				break;
			case 1:
				Mostrar();
				break;
			case 2:
				Crear();
				break;
			case 3:
				Modificar();
				break;
			case 4:
				Borrar();
				break;
			case 5:
				tareasMDAO.Mostrar();
				break;
			case 6:
				herramientasDAO.Mostrar();
				break;
			default:
				//Opcion fuera del rango
				System.out.println("Opcion incorrecta o no existente");
				break;
			}
		}
	}

	@Override
	public boolean Mostrar() {
		//Recibir objetos de la clase
		ArrayList<Object> tareasTemp = Recibir();
		tareas.clear();
		for (Object tareaTemp : tareasTemp) {
			tareas.add((tareas_herramientas) tareaTemp);
		}
		
		//Imprimir
		System.out.println("--Tareas herramientas--");
		for (tareas_herramientas tarea : tareas) {
			System.out.println(tarea);
		}
		return true;
	}

	@Override
	public ArrayList Recibir() {
		ArrayList<tareas_herramientas> tareasHerr = new ArrayList<tareas_herramientas>();
		Connection con = conexion.Conectar();
		Statement stat = null;
		ResultSet rs = null;

		
		//Recibir objetos de las clases agregada
		ArrayList<Object> tareasTemp = tareasMDAO.Recibir();
		tareasMantenimiento.clear();
		for (Object tarea : tareasTemp) {
			tareasMantenimiento.add((tareas_mantenimiento) tarea);
		}
		ArrayList<Object> herrTemp = herramientasDAO.Recibir();
		herramientasList.clear();
		for (Object herr : herrTemp) {
			herramientasList.add((herramientas) herr);
		}
		
		//Crear query
		try {
			stat = con.createStatement();
			
			//Query a ejecutar
			rs = stat.executeQuery("select * from tareas_herramientas");
			
			//Bucle, una iteracion por cada fila recibida
			while (rs.next()) {
				int idTarea = rs.getInt(1);
				int idHerramienta = rs.getInt(2);
				int cantidad = rs.getInt(3);
				
				//Asignar tarea basado en la clave principal recibida
				tareas_mantenimiento tarea = null;
				for (tareas_mantenimiento tareaTest : tareasMantenimiento) {
					if (tareaTest.getId() == idTarea) {
						tarea = tareaTest;
						break;
					}
				}
				
				//Asignar herramienta basado en la clave principal recibida
				herramientas herramienta = null;
				for (herramientas herrTest : herramientasList) {
					if (herrTest.getId_herramienta() == idHerramienta) {
						herramienta = herrTest;
						break;
					}
				}
				
				tareasHerr.add(new tareas_herramientas(tarea, herramienta, cantidad));
			}
		} catch (SQLException e) {
			System.out.println("Error al intentar recibir informacion de la base de datos");
		} finally {
			
			try {
				if (rs != null) {
					rs.close();
				}
				if (stat != null) {
					stat.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				System.out.println("Error al cerrar conexion a la base de datos");
			}
		}
		return tareasHerr;
	}

	@Override
	protected boolean Crear() {
		//Recibir objetos de las clases agregada
		ArrayList<Object> tareasTemp = tareasMDAO.Recibir();
		tareasMantenimiento.clear();
		for (Object tarea : tareasTemp) {
			tareasMantenimiento.add((tareas_mantenimiento) tarea);
		}
		ArrayList<Object> herrTemp = herramientasDAO.Recibir();
		herramientasList.clear();
		for (Object herr : herrTemp) {
			herramientasList.add((herramientas) herr);
		}
		
		//Recibir objetos de la clase
		tareas = (ArrayList<tareas_herramientas>) Recibir();
		
		//Datos a pedir
		tareas_mantenimiento tarea;
		herramientas herramienta;
		int cantidad;
		
		//Recibir datos
		tarea = IntroducirIdTarea();
		if (tarea == null) {
			//Usuario cancelo la operacion
			return false;
		}
		
		herramienta = IntroducirIdHerramienta();
		if (herramienta == null) {
			//Usuario cancelo la operacion
			return false;
		}
		
		cantidad = IntroducirCantidad();
		
		//Añadir en local
		boolean exito = tareas.add(new tareas_herramientas(tarea, herramienta, cantidad));
		
		//Intentar introducir en la base de datos
		if (exito) {
			Connection con = conexion.Conectar();
			Statement stat = null;
    
			try {
				stat = con.createStatement();
				stat.executeUpdate("insert into tareas_herramientas"
						+ " (id_tarea, id_herramienta, cantidad)"
						+ " values ("+tarea.getId()+", "+herramienta.getId_herramienta()+", "+cantidad+")");
				System.out.println("Tarea herramienta creada exitosamente");
			} catch(SQLException e) {
				System.out.println("Error al insertar datos en la base de datos externa");
				e.printStackTrace();
			} finally {
    	
				try {
					if (stat != null){
						stat.close();
					} 
					con.close();
				} catch (SQLException e) {
					System.out.println("Error al cerrar conexion");
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("Error respecto a los datos introducidos");
		}
		
		return true;
	}

	@Override
	protected boolean Borrar() {
		//Recibir objetos de las clases agregada
		ArrayList<Object> tareasTemp = tareasMDAO.Recibir();
		tareasMantenimiento.clear();
		for (Object tarea : tareasTemp) {
			tareasMantenimiento.add((tareas_mantenimiento) tarea);
		}
		ArrayList<Object> herrTemp = herramientasDAO.Recibir();
		herramientasList.clear();
		for (Object herr : herrTemp) {
			herramientasList.add((herramientas) herr);
		}
		//Recibir objetos de la clase
		tareas = (ArrayList<tareas_herramientas>) Recibir();
				
		//Datos a borrar
		tareas_herramientas tareaHerr;
		
		tareaHerr = IntroducirIdExistente();
		
		//¿Usuario a cancelado la operacion?
		if (tareaHerr == null) {
			return false;
		}
		
		//Borrar en local
		boolean exito = tareas.remove(tareaHerr);
		
		//Intentar borrar en base de datos externa
		Connection con = conexion.Conectar();
        Statement stat = null;
        
        if (exito) {
        	try {
            	
            	stat = con.createStatement();
            	stat.executeUpdate("delete from tareas_herramientas where id_tarea = "+tareaHerr.getTarea().getId()+" and id_herramienta = "+tareaHerr.getHerramienta().getId_herramienta()+"");
            	System.out.println("Borrado exitoso");
            } catch (SQLException e) {
            	System.out.println("Error al borrar datos en la base de datos");
            	e.printStackTrace();
            } finally {
            	try {
                    if (stat != null){
                        stat.close();
                    }            
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar conexion");
                    e.printStackTrace();
                }
            }
        } else {
        	System.out.println("No encontrado");
        }
		return true;
	}

	@Override
	protected boolean Modificar() {
		//Recibir objetos de las clases agregada
		ArrayList<Object> tareasTemp = tareasMDAO.Recibir();
		tareasMantenimiento.clear();
		for (Object tarea : tareasTemp) {
			tareasMantenimiento.add((tareas_mantenimiento) tarea);
		}
		ArrayList<Object> herrTemp = herramientasDAO.Recibir();
		herramientasList.clear();
		for (Object herr : herrTemp) {
			herramientasList.add((herramientas) herr);
		}
		//Recibir objetos de la clase
		tareas = (ArrayList<tareas_herramientas>) Recibir();

		
		//Datos a pedir
		tareas_herramientas tareaHerr;
		tareas_mantenimiento tarea;
		herramientas herramienta;
		int cantidad;
		
		
		//Recibir tarea herramienta existente
		tareaHerr = IntroducirIdExistente();
		if (tareaHerr == null) {
			//Usuario a cancelado la operacion
			return false;
		}
		
		//Recibir nueva tarea
		tarea = IntroducirIdTarea();
		if (tarea == null) {
			//Usuario a cancelado la operacion
			return false;
		}
		
		//Recibir nueva herramienta
		herramienta = IntroducirIdHerramienta();
		if (herramienta == null) {
			//Usuario a cancelado la operacion
			return false;
		}
		
		//Recibir nueva cantidad
		cantidad = IntroducirCantidad();
		
		//Cambiar en local
		tareaHerr.setTarea(tarea);
		tareaHerr.setHerramienta(herramienta);
		tareaHerr.setCantidad(cantidad);
		
		//Intentar cambios en base de datos externa
		Connection con = conexion.Conectar();
        Statement stat = null;
 
        
        try {
        	stat = con.createStatement();
        	stat.executeUpdate("update tareas_herramientas"
        			+ " set id_tarea = "+tarea.getId()+", id_herramienta = "+herramienta.getId_herramienta()+","
        			+ " cantidad = "+cantidad+""
        			+ " where id_tarea = "+tareaHerr.getTarea().getId()+" and id_herramienta = "+tareaHerr.getHerramienta().getId_herramienta()+"");
        	System.out.println("Modificacion exitosa");
        } catch(SQLException e) {
        	System.out.println("Error al modificar datos en la base de datos externa");
        	e.printStackTrace();
        } finally {
        	
        	try {
        		if (stat != null){
        			stat.close();
                } 
                con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion");
                e.printStackTrace();
            }
        }
		
		return true;
	}

	/**El usuario introduce una tarea existente
	 * @return la tarea introducida*/
	private tareas_mantenimiento IntroducirIdTarea() {
		int idTarea = 0;
		//Variable a devolver
		tareas_mantenimiento tarea = null;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
				
		do {
			try {
				//Pedir la informacion
				System.out.print("ID de la tarea (0 para cancelar):");
				idTarea = Integer.parseInt(sc.nextLine());
			} catch (java.lang.NumberFormatException e) {
				//Error si no es un numero
				System.out.println("Introduzca un numero");
			}
			
			if (idTarea == 0) {
				//Usuario elejio cancelar la operacion
				System.out.println("Cancelando");
				return null;
			}
			
			//Comprobaciones
			//Comprobar si existe una tarea con la clave principal
			for (tareas_mantenimiento tareaTest : tareasMantenimiento) {
				if (tareaTest.getId() == idTarea) {
					//Asignar tarea que tenga la id introducida
					tarea = tareaTest;
					repetir = false;
					break;
				}
			}
			if (repetir && idTarea != 0) {
				//No se encontro ninguna tarea con la id introducida y no cancelo la operacion
				System.out.println("No existe ninguna tarea con esa id");
			}
			
		} while (repetir);
		
		return tarea;		
	}

	/**El usuario introduce una herramienta existente
	 * @return la herramienta introducida*/
	private herramientas IntroducirIdHerramienta() {
		int idHerramienta = 0;
		//Variable a devolver
		herramientas herramienta = null;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
				
		do {
			try {
				//Pedir la informacion
				System.out.print("ID de la herramienta (0 para cancelar):");
				idHerramienta = Integer.parseInt(sc.nextLine());
			} catch (java.lang.NumberFormatException e) {
				//Error si no es un numero
				System.out.println("Introduzca un numero");
			}
			
			if (idHerramienta == 0) {
				//Usuario elejio cancelar la operacion
				System.out.println("Cancelando");
				return null;
			}
			
			//Comprobaciones
			//Comprobar si existe una herramienta con la clave principal
			for (herramientas herrTest : herramientasList) {
				if (herrTest.getId_herramienta() == idHerramienta) {
					//Asignar herramienta que tenga la id introducida
					herramienta = herrTest;
					repetir = false;
					break;
				}
			}
			if (repetir && idHerramienta != 0) {
				//No se encontro ninguna herramienta con la id introducida y no cancelo la operacion
				System.out.println("No existe ninguna herramienta con esa id");
			}
			
		} while (repetir);
		
		return herramienta;		
	}

	/**El usuario introduce una relacion existente (tarea-herramienta)
	 * @return la relacion introducida*/
	private tareas_herramientas IntroducirIdExistente() {
		//Variables a devolver
		tareas_herramientas tareaHerr = null;
		int idTarea = 0;
		int idHerramienta = 0;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
				
		do {
			try {
				//Pedir la informacion
				System.out.print("ID de la tarea (0 para cancelar):");
				idTarea = Integer.parseInt(sc.nextLine());
			} catch (java.lang.NumberFormatException e) {
				//Error si no es un numero
				System.out.println("Introduzca un numero");
			}
			
			if (idTarea == 0) {
				//Usuario elejio cancelar la operacion
				System.out.println("Cancelando");
				return null;
			}
			
			try {
				//Pedir la informacion
				System.out.print("ID de la herramienta (0 para cancelar):");
				idHerramienta = Integer.parseInt(sc.nextLine());
			} catch (java.lang.NumberFormatException e) {
				//Error si no es un numero
				System.out.println("Introduzca un numero");
			}
			
			if (idHerramienta == 0) {
				//Usuario elejio cancelar la operacion
				System.out.println("Cancelando");
				return null;
			}
			
			//Comprobaciones
			//Comprobar si existe una relacion con estas claves principales
			for (tareas_herramientas tareaTest : tareas) {
				if (tareaTest.getTarea().getId() == idTarea && tareaTest.getHerramienta().getId_herramienta() == idHerramienta) {
					//Existe una relacion con esa id
					tareaHerr = tareaTest;
					repetir = false;
					break;
				}
			}
			if (repetir && idTarea != 0 && idHerramienta != 0) {
				//No se encontro ninguna relacion con los ids introducidos y no cancelo la operacion
				System.out.println("No existe ninguna relacion con esos ids");
			}
			
		} while (repetir);
		
		return tareaHerr;	
	}

	/**El usuario introduce la cantidad
	 * @return int introducida*/
	private int IntroducirCantidad() {
		//Variable a devolver
		int cantidad = 0;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
		
		do {
			
			try {
				//Pedir la informacion
				System.out.print("Cantidad:");
				cantidad = Integer.parseInt(sc.nextLine());
				repetir = false;
			} catch (java.lang.NumberFormatException e) {
				//Error si no se introdujo un numero
				System.out.println("Introduzca un numero");
				repetir = true;
			}
			
		} while (repetir);
		
		return cantidad;
	}	
}