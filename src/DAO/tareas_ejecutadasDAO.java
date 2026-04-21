package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import Comun.conexion;
import Comun.interfaces;
import Objetos.ejecuciones_mantenimiento;
import Objetos.planes_mantenimiento;
import Objetos.tareas_ejecutadas;
import Objetos.tareas_mantenimiento;

/** Manejar, crear y borrar el objeto del mismo nombre
 * @author Perceval*/
public class tareas_ejecutadasDAO extends interfaces{

	ArrayList<tareas_ejecutadas> tareas = new ArrayList<tareas_ejecutadas>();
	ArrayList<ejecuciones_mantenimiento> ejecuciones = new ArrayList<ejecuciones_mantenimiento>();
	ArrayList<tareas_mantenimiento> manteniminetos = new ArrayList<tareas_mantenimiento>();
	
	static Scanner sc = new Scanner (System.in); //Crear escaner
	
	tareas_mantenimientoDAO tareasMDAO =  new tareas_mantenimientoDAO();
	ejecuciones_mantenimientoDAO ejecucionesDAO = new ejecuciones_mantenimientoDAO();
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
					+ "5. Mostrar ejecuciones de mantenimineto\n"
					+ "6. Mostrar tareas de mantenimineto");
			
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
				ejecucionesDAO.Mostrar();
			break;
			case 6:
				tareasMDAO.Mostrar();
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
		for (Object tareaTemp : tareasTemp) {
			tareas.add((tareas_ejecutadas) tareaTemp);
		}
		
		//Imprimir
		System.out.println("--Tareas ejecutadas--");
		for (tareas_ejecutadas tarea : tareas) {
			System.out.println(tarea);
		}
		return true;
	}

	@Override
	public ArrayList Recibir() {
		ArrayList<tareas_ejecutadas> tareasEjec = new ArrayList<tareas_ejecutadas>();
		Connection con = conexion.Conectar();
		Statement stat = null;
		ResultSet rs = null;

		
		//Recibir objetos de las clases agregada
		ArrayList<Object> manTemp = tareasMDAO.Recibir();
		for (Object tareaM : manTemp) {
			manteniminetos.add((tareas_mantenimiento) tareaM);
		}
		ArrayList<Object> ejeTemp = ejecucionesDAO.Recibir();
		for (Object ejec : ejeTemp) {
			ejecuciones.add((ejecuciones_mantenimiento) ejec);
		}
		
		//Crear query
		try {
			stat = con.createStatement();
			
			//Query a ejecutar
			rs = stat.executeQuery ("select * from tareas_ejecutadas");
			
			//Bucle, una iteracion por cada fila recibida
			while (rs.next()) {
				int id = rs.getInt(1);
				int idEjecucion = rs.getInt(2);
				int idTarea = rs.getInt(3);
				boolean completada = rs.getBoolean(4);
				String observaciones = rs.getString(5);
				int tiempo = rs.getInt(6);
				
				//Asignar ejecucion basado en la clave principal recibida
				ejecuciones_mantenimiento ejecucion = null;
				for (ejecuciones_mantenimiento ejecuionTest : ejecuciones) {
					if (ejecuionTest.getId_ejecucion() == idEjecucion) {
						ejecucion = ejecuionTest;
					}
				}
				
				//Asignar tarea de mantenimineto basado en la clave principal recibida
				tareas_mantenimiento tareaM = null;
				for (tareas_mantenimiento manTest : manteniminetos) {
					if (manTest.getId() == id) {
						tareaM = manTest;
					}
				}
				
				tareasEjec.add(new tareas_ejecutadas(id, ejecucion, tareaM, completada, observaciones, tiempo));
			}
		} catch (SQLException e) {
			System.out.println("Error al intentar recibir informacion de la base de datos");
		} finally {
			
			try {
				if (rs != null) {
					rs.close();
				}
				if (stat != null ) {
					stat.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				System.out.println("Error al cerrar conexion a la base de datos");
			}
		}
		return tareasEjec;
	}

	@Override
	protected boolean Crear() {
		//Recibir objetos de las clases agregada
		ArrayList<Object> manTemp = tareasMDAO.Recibir();
		for (Object tareaM : manTemp) {
			manteniminetos.add((tareas_mantenimiento) tareaM);
		}
		ArrayList<Object> ejeTemp = ejecucionesDAO.Recibir();
		for (Object ejec : ejeTemp) {
			ejecuciones.add((ejecuciones_mantenimiento) ejec);
		}	
		
		//Recibir objetos de la clase
		tareas = Recibir();
		
		//Datos a pedir
		int id;
		ejecuciones_mantenimiento ejecucion;
		tareas_mantenimiento tarea;
		boolean completada;
		String observaciones;
		int tiempo;
		
		//Recibir datos
		id = IntroducirIdNueva();
		if (id == 0) {
			//Usuario cancelo la operacion
			return false;
		}
		
		ejecucion = IntroducirIdEjecucion();
		if (ejecucion == null) {
			//Usuario cancelo la operacion
			return false;
		}
		
		tarea = IntroducirIdTarea();
		if (tarea == null) {
			//Usuario cancelo la operacion
			return false;
		}
		
		completada = IntroducirCompletada();
		observaciones = IntroducirObservaciones();
		tiempo = IntroducirTiempo();
		
		//Añadir en local
		boolean exito = tareas.add(new tareas_ejecutadas(id, ejecucion, tarea, completada, observaciones, tiempo));
		
		//Intentar introducir en la base de datos
		if (exito) {
			Connection con = conexion.Conectar();
			Statement stat = null;
    
			try {
				stat = con.createStatement();
				stat.executeUpdate("insert into tareas_ejecutadas"
						+ " (id_tarea_ejecutada, id_ejecucion, id_tarea, completada, observaciones, tiempo_empleado)"
						+ " ("+id+", "+ejecucion.getId_ejecucion()+", "+tarea.getId()+", "+completada+", '"+observaciones+"', "+tiempo+")");
			} catch(SQLException e) {
				System.out.println("Error al insertar datos en la base de datos externa");
			} finally {
    	
				try {
					if (stat!=null){
						stat.close();
					} 
					con.close();
				} catch (SQLException e) {
					System.out.println("Error al cerrar conexion");
					e.printStackTrace();
				}
    }
    
} else {
	System.out.print("Error respecto a los datos introducidos");
}
        return true;
	}

	@Override
	protected boolean Borrar() {
		//Recibir objetos de las clases agregada
		ArrayList<Object> manTemp = tareasMDAO.Recibir();
		manteniminetos.clear();
		for (Object tareaM : manTemp) {
			manteniminetos.add((tareas_mantenimiento) tareaM);
		}
		ArrayList<Object> ejeTemp = ejecucionesDAO.Recibir();
		ejecuciones.clear();
		for (Object ejec : ejeTemp) {
			ejecuciones.add((ejecuciones_mantenimiento) ejec);
		}
		
		//Recibir objetos de la clase
		tareas = (ArrayList<tareas_ejecutadas>) Recibir();
						
		//Datos a borrar
		tareas_ejecutadas tarea;
				
		tarea = IntroducirIdExistente();
				
		//¿Usuario a cancelado la operacion?
		if (tarea == null) {
			return false;
		}
				
		//Borrar en local
		boolean exito = tareas.remove(tarea);
				
		//Intentar borrar en base de datos externa
		Connection con = conexion.Conectar();
		Statement stat = null;
		        
		if (exito) {
			try {
		        stat = con.createStatement();
		        stat.executeUpdate("delete from tareas_ejecutadas where id_tarea_ejecutada= "+tarea.getId()+"");
		        System.out.println("Borrado exitoso");
		   } catch (SQLException e) {
			   System.out.println("Error al borrar datos en la base de datos");
		       e.printStackTrace();
		   } finally {
			   try {
				   if (stat!=null){
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
		ArrayList<Object> manTemp = tareasMDAO.Recibir();
		manteniminetos.clear();
		for (Object tareaM : manTemp) {
			manteniminetos.add((tareas_mantenimiento) tareaM);
		}
		ArrayList<Object> ejeTemp = ejecucionesDAO.Recibir();
		ejecuciones.clear();
		for (Object ejec : ejeTemp) {
			ejecuciones.add((ejecuciones_mantenimiento) ejec);
		}
		//Recibir objetos de la clase
		tareas = (ArrayList<tareas_ejecutadas>) Recibir();

		
		//Datos a pedir
		tareas_ejecutadas tarea;
		ejecuciones_mantenimiento ejecucion;
		tareas_mantenimiento tareaM;
		boolean completada;
		String observaciones;
		int tiempo;
		
		
		//Recibir tarea
		tarea = IntroducirIdExistente();
		if (tarea == null ) {
			//Usuairo a cancelado la operacion
			return false;
		}
		
		//Recibir idEjecucion
		ejecucion = IntroducirIdEjecucion();
		if (ejecucion == null) {
			//Usuario a cancelado la operacion;
			return false;
		}
		
		//Recibir idTarea
		tareaM = IntroducirIdTarea();
		if (tareaM == null) {
			//Usuario a cancelado la operacion;
			return false;
		}
		
		//Recibir completada
		completada = IntroducirCompletada();
		
		//Recibir observaciones
		observaciones = IntroducirObservaciones();
		
		//Recibir tiempo
		tiempo = IntroducirTiempo();
		
		//Cambiar en local
		tarea.setEjecucion(ejecucion);
		tarea.setTarea(tareaM);
		tarea.setCompletada(completada);
		tarea.setObservaciones(observaciones);
		tarea.setTiempoEmpleado(tiempo);
		
		//Intentar cambios en base de datos externa
		Connection con = conexion.Conectar();
        Statement stat = null;
 
        
        try {
        	stat = con.createStatement();
        	stat.executeUpdate("update tareas_ejecutadas"
        			+ " set id_ejecucion = "+ejecucion.getId_ejecucion()+", id_tarea = "+tareaM.getId()+","
        			+ " completada = "+completada+", observaciones = '"+observaciones+"', tiempo_empleado = "+tiempo+""
        			+ " where id_tarea_ejecutada = "+tarea.getId()+"");
        	System.out.println("Modificacion exitosa");
        } catch(SQLException e) {
        	System.out.println("Error al modificar datos en la base de datos externa");
        	e.printStackTrace();
        } finally {
        	
        	try {
        		if (stat!=null){
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

	/**El usuario introduce una id de la tarea ejecutada que no exista ya
	 * @return la id introducida*/
	private int IntroducirIdNueva() {
		//Variable a devolver
		int id = 0;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
				
		do {
			try {
				//Pedir la informacion
				System.out.print("ID de la tarea ejecutada (0 para cancelar):");
				id = Integer.parseInt(sc.nextLine());
			} catch (java.lang.NumberFormatException e) {
				//Error si no es un numero
				System.out.println("Introduzca un numero");
			}
			
			if (id == 0) {
				//Usuario elejio cancelar la operacion
				System.out.println("Cancelando");
				return 0;
			}
			
			//Comprobaciones
			repetir = false;
			//Comprobar si existe una tarea con la misma clave principal
			for (tareas_ejecutadas tareaTest : tareas) {
				if (tareaTest.getId() == id) {
					System.out.println("Tarea ejecutada con esa id ya existente");
					repetir = true;
				}
			}
			
		} while (repetir);
		
		return id;		
	}
	
	/**El usuario introduce una id de la tarea ejecutada que ya exista
	 * @return la id introducida*/
	private tareas_ejecutadas IntroducirIdExistente() {
		//Variable a devolver
		tareas_ejecutadas tarea = null;
		int id = 0;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
				
		do {
			try {
				//Pedir la informacion
				System.out.print("ID de la tarea ejecutada (0 para cancelar):");
				id = Integer.parseInt(sc.nextLine());
			} catch (java.lang.NumberFormatException e) {
				//Error si no es un numero
				System.out.println("Introduzca un numero");
			}
			
			if (id == 0) {
				//Usuario elejio cancelar la operacion
				System.out.println("Cancelando");
				return null;
			}
			
			//Comprobaciones
			//Comprobar si existe un plan con la clave principal
			for (tareas_ejecutadas tareaTest : tareas) {
				if (tareaTest.getId() == id) {
					//Existe una tarea ejecuta con esa id
					tarea = tareaTest;
					repetir = false;
				}
			}
			if (repetir && id != 0) {
				//No se encontro ninguna tarea con la id introducida y no cancelo la operacion
				System.out.println("No existe ninguna tarea con esa id");
			}
			
		} while (repetir);
		
		return tarea;	
	}

	/**El usuario introduce una id de la ejecucion
	 * @return plan de la id introducida*/
	private ejecuciones_mantenimiento IntroducirIdEjecucion() {
		int idEjecucion = 0;
		//Variable a devolver
		ejecuciones_mantenimiento ejecucion = null;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
				
		do {
			try {
				//Pedir la informacion
				System.out.print("ID de la ejecucion (0 para cancelar):");
				idEjecucion = Integer.parseInt(sc.nextLine());
			} catch (java.lang.NumberFormatException e) {
				//Error si no es un numero
				System.out.println("Introduzca un numero");
			}
			
			if (idEjecucion == 0) {
				//Usuario elejio cancelar la operacion
				System.out.println("Cancelando");
				return null;
			}
			
			//Comprobaciones
			//Comprobar si existe una ejecucion con la clave principal
			for (ejecuciones_mantenimiento ejecucionTest : ejecuciones) {
				if (ejecucionTest.getId_ejecucion() == idEjecucion) {
					//Asignar ejecucion que tenga la id introducida
					ejecucion = ejecucionTest;
					repetir = false;
				}
			}
			if (repetir && idEjecucion != 0) {
				//No se encontro ninguna ejecucion con la id introducida y no cancelo la operacion
				System.out.println("No existe ninguna ejecucion con esa id");
			}
			
		} while (repetir);
		
		return ejecucion;		
	}

	/**El usuario introduce una id de la tarea de mantenimineto
	 * @return plan de la id introducida*/
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
			//Comprobar si existe una ejecucion con la clave principal
			for (tareas_mantenimiento tareaTest : manteniminetos) {
				if (tareaTest.getId() == idTarea) {
					//Asignar tarea que tenga la id introducida
					tarea = tareaTest;
					repetir = false;
				}
			}
			if (repetir && idTarea != 0) {
				//No se encontro ninguna ejecucion con la id introducida y no cancelo la operacion
				System.out.println("No existe ninguna ejecucion con esa id");
			}
			
		} while (repetir);
		
		return tarea;		
	}
	
	/**El usuario introduce si la tarea esta completada
	 * @return valor booleano con la informacion recibida*/
	private boolean IntroducirCompletada() {
		//Valor a devolver
		boolean completada = false;
		int recibido = 0;
		
		//Recibir informacion
		try {
			//Pedir la informacion
			System.out.print("¿Tarea completada? (1 = sí)");
			recibido = Integer.parseInt(sc.nextLine());
		} catch (java.lang.NumberFormatException e) {
			//Error si no se introdujo un numero, parece tambier llegar aqui si no se introduce nada
			System.out.println("Introduzca un numero");
		}
		
		//Pasar informacion redibida a boolean
		if (recibido == 1) {
			completada = true;
		}
		
		return completada;
	}
	
	/**El usuario introduce observaciones
	 * puede estar en blanco
	 * @return String introducida*/
	private String IntroducirObservaciones() {
		//Variable a devolver
		String observaciones;

		//Pedir la informacion
		System.out.println("Observaciones de la tarea:");
		observaciones = sc.nextLine();
		
		return observaciones;
	}	
	
	/**El usuario introduce el tiempo empleado
	 * @return int introducida*/
	private int IntroducirTiempo() {
		//Variable a devolver
		int orden = 0;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
		
		do {
			
			try {
				//Pedir la informacion
				System.out.print("Numero de orden:");
				orden = Integer.parseInt(sc.nextLine());
				repetir = false;
			} catch (java.lang.NumberFormatException e) {
				//Error si no se introdujo un numero, parece tambier llegar aqui si no se introduce nada
				System.out.println("Introduzca un numero");
				repetir = true;
			}
			
		} while (repetir);
		
		return orden;
	}	
}
