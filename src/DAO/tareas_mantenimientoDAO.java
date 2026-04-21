package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import Comun.conexion;
import Comun.interfaces;
import Objetos.planes_mantenimiento;
import Objetos.tareas_mantenimiento;

/** Manejar, crear y borrar el objeto del mismo nombre
 * @author Perceval*/
public class tareas_mantenimientoDAO extends interfaces{
	ArrayList<tareas_mantenimiento> tareas = new ArrayList<tareas_mantenimiento>(); 
	ArrayList<planes_mantenimiento> planes = new ArrayList<planes_mantenimiento>();
	
	static Scanner sc = new Scanner (System.in); //Crear escaner
	planes_manteniminetoDAO planesDAO = new planes_manteniminetoDAO();
	
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
					+ "5. Mostrar planes");
			
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
				planesDAO.Mostrar();
			break;
			default:
				//Opcion fuera del rango
				System.out.println("Opcion incorrecta o no existente");
			break;
			}
		}
	}


	@Override
	public ArrayList Recibir() {
		ArrayList<tareas_mantenimiento> tareasM = new ArrayList<tareas_mantenimiento>();
		Connection con = conexion.Conectar();
		Statement stat = null;
		ResultSet rs = null;
		
		//Recibir objetos de la clase agregada
		ArrayList<Object> planesTemp = planesDAO.Recibir();
		for (Object plan : planesTemp) {
			planes.add((planes_mantenimiento) plan);
		}
		
		//Crear query
		try {
			stat = con.createStatement();
			
			//Query a ejecutar
			rs = stat.executeQuery ("select * from tareas_mantenimiento");
			
			//Bucle, una iteracion por cada fila recibida
			while (rs.next()) {
				int idTarea = rs.getInt(1);
				int idPlan = rs.getInt(2);
				String descripcion = rs.getString(3);
				int orden = rs.getInt(4);
				String instruciones = rs.getString(5);
				boolean requiereEpi = rs.getBoolean(6);
				String epis = rs.getString(7);
				String seguridad = rs.getString(8);
				String medioAmbiente = rs.getString(9);
				
				//Asignar plan basado en la clave principal recibida
				planes_mantenimiento plan = null;
				for (planes_mantenimiento planTest : planes) {
					if (planTest.getIdPlan() == idPlan) {
						plan = planTest;
					}
				}
				
				tareasM.add(new tareas_mantenimiento(idTarea, plan, descripcion, orden, instruciones, requiereEpi, epis, seguridad, medioAmbiente));
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
		return tareasM;
	}

	@Override
	protected boolean Crear() {
		//Recibir objetos de la clase agregada
		ArrayList<Object> planesTemp = planesDAO.Recibir();
		for (Object plan : planesTemp) {
			planes.add((planes_mantenimiento) plan);
		}
		
		//Recibir objetos de la clase
		tareas = Recibir();
		
		//Datos a pedir
		int id;
		planes_mantenimiento plan;
		String descripcion;
		int orden;
		String instruciones = null; //Opcional
		boolean requiereEpis;
		String epis = null; //Opcional, depende de requiereEpis
		String seguridad = null; //Opcional
		String medioAmbiente = null; //Opcional
			
		//Recibir id de la tarea 
		/*(No se si el profe queria que lo recibieros o lo hicieramos automatico
		 * Pero por el odio que parece tenerle a los autoincrementales, decidi recibirlo)*/
		id = IntroducirIdNueva();
		if (id == 0) {
			//Usuario a cancelado la operacion
			return false;
		}
		
		//Recibir idPlan
		plan = IntroducirIdPlan();
		if (plan == null) {
			//Usuario a cancelado la operacion;
			return false;
		}
		
		//Recibir descripcion
		descripcion = IntroducirDescripcion();
		
		//Recibir orden
		orden = IntroducirOrden();
		
		//Recibir instruciones
		instruciones = IntroducirInstruciones();
		
		//Recibir booleano requiere epis
		requiereEpis = IntroducirRequiereEpis();
		
		//Recibir epis (Si las requiere)
		if (requiereEpis) {
			epis = IntroducirEpisNecesarios();
		}
		
		//Recibir normas de seguridad
		seguridad = IntroducirReglasSeguridad();
		
		//Recibir normas del medio ambiente
		medioAmbiente = IntroducirReglasMedioAmbiente();
		
		//Añadir en local
		boolean exito = tareas.add(new tareas_mantenimiento(id, plan, descripcion, orden, instruciones, requiereEpis, epis, seguridad, medioAmbiente));
		
		//Intentar introducir en la base de datos
		if (exito) {
			Connection con = conexion.Conectar();
            Statement stat = null;
            
            try {
            	stat = con.createStatement();
            	stat.executeUpdate("insert into tareas_mantenimiento"
            			+ " (id_tarea, id_plan, descripcion, orden, instrucciones_detalladas, requiere_epi, epis_necesarios, reglas_seguirdad, reglas_medio_ambiente)"
            			+ " ("+id+", "+plan.getIdPlan()+", '"+descripcion+"', "+orden+", '"+instruciones+"', "+requiereEpis+", '"+epis+"', '"+seguridad+"', '"+medioAmbiente+"')");
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
		//Recibir objetos de la clase agregada
		ArrayList<Object> planesTemp = planesDAO.Recibir();
		for (Object plan : planesTemp) {
			planes.add((planes_mantenimiento) plan);
		}
		//Recibir objetos de la clase
		tareas = Recibir();
				
		//Datos a borrar
		tareas_mantenimiento tarea;
		
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
            	stat.executeUpdate("delete from tareas_mantenimiento where id_tarea= '"+tarea.getId()+"'");
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
		//Recibir objetos de la clase agregada
		ArrayList<Object> planesTemp = planesDAO.Recibir();
		for (Object plan : planesTemp) {
			planes.add((planes_mantenimiento) plan);
		}
		//Recibir objetos de la clase
		tareas = Recibir();

		
		//Datos a pedir
		tareas_mantenimiento tarea;
		planes_mantenimiento plan;
		String descripcion;
		int orden;
		String instruciones = null; //Opcional
		boolean requiereEpis;
		String epis = null; //Opcional, depende de requiereEpis
		String seguridad = null; //Opcional
		String medioAmbiente = null; //Opcional
		
		
		//Recibir tarea
		tarea = IntroducirIdExistente();
		if (tarea == null ) {
			//Usuairo a cancelado la operacion
			return false;
		}
		
		//Recibir idPlan
		plan = IntroducirIdPlan();
		if (plan == null) {
			//Usuario a cancelado la operacion;
			return false;
		}
		
		//Recibir descripcion
		descripcion = IntroducirDescripcion();
		
		//Recibir orden
		orden = IntroducirOrden();
		
		//Recibir instruciones
		instruciones = IntroducirInstruciones();
		
		//Recibir booleano requiere epis
		requiereEpis = IntroducirRequiereEpis();
		
		//Recibir epis (Si las requiere)
		if (requiereEpis) {
			epis = IntroducirEpisNecesarios();
		}
		
		//Recibir normas de seguridad
		seguridad = IntroducirReglasSeguridad();
		
		//Recibir normas del medio ambiente
		medioAmbiente = IntroducirReglasMedioAmbiente();
		
		//Cambiar en local
		tarea.setPlan(plan);
		tarea.setDescripcion(descripcion);
		tarea.setOrden(orden);
		tarea.setInstrucciones(instruciones);
		tarea.setRequiereEpi(requiereEpis);
		tarea.setEpisNecesarios(epis);
		tarea.setReglasSeguridad(seguridad);
		tarea.setMedioAmbiente(medioAmbiente);
		
		//Intentar cambios en base de datos externa
		Connection con = conexion.Conectar();
        Statement stat = null;
 
        
        try {
        	stat = con.createStatement();
        	stat.executeUpdate("update tareas_mantenimiento"
        			+ " set descripcion = '"+descripcion+", orden = "+orden+","
        			+ " instrucciones_detalladas = '"+instruciones+"', requiere_epi = "+requiereEpis+","
        			+ " epis_necesarios = '"+epis+"', reglas_seguirdad = '"+seguridad+"', reglas_medio_ambiente = '"+medioAmbiente+"'"
        			+ " where id_tarea = "+tarea.getId()+"");
        } catch(SQLException e) {
        	System.out.println("Error al modificar datos en la base de datos externa");
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
	

	@Override
	public boolean Mostrar() {
		tareas = Recibir();
		System.out.println("--Tareas de mantenimiento--");
		for (tareas_mantenimiento tarea : tareas) {
			System.out.println(tarea);
		}
		return true;
	}

	/**El usuario introduce una id de la tarea que no exista ya
	 * @return la id introducida*/
	private int IntroducirIdNueva() {
		//Variable a devolver
		int id = 0;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
				
		do {
			try {
				//Pedir la informacion
				System.out.print("ID de la tarea (0 para cancelar):");
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
			for (tareas_mantenimiento tareaTest : tareas) {
				if (tareaTest.getId() == id) {
					System.out.println("Tarea con esa id ya existente");
					repetir = true;
				}
			}
			
		} while (repetir);
		
		return id;
	}
	
	/**El usuario introduce una id de la tarea que ya exista
	 * @return la id introducida*/
	private tareas_mantenimiento IntroducirIdExistente() {
		//Variable a devolver
		tareas_mantenimiento tarea = null;
		int id = 0;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
				
		do {
			try {
				//Pedir la informacion
				System.out.print("ID de la tarea (0 para cancelar):");
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
			for (tareas_mantenimiento tareaTest : tareas) {
				if (tareaTest.getId() == id) {
					//Existe una tarea con esa id
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
	
	/**El usuario introduce la id de plan
	 * @return plan de la id introducida*/
	private planes_mantenimiento IntroducirIdPlan() {
	
		int idPlan = 0;
		//Variable a devolver
		planes_mantenimiento plan = null;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
				
		do {
			try {
				//Pedir la informacion
				System.out.print("ID del plan (0 para cancelar):");
				idPlan = Integer.parseInt(sc.nextLine());
			} catch (java.lang.NumberFormatException e) {
				//Error si no es un numero
				System.out.println("Introduzca un numero");
			}
			
			if (idPlan == 0) {
				//Usuario elejio cancelar la operacion
				System.out.println("Cancelando");
				return null;
			}
			
			//Comprobaciones
			//Comprobar si existe un plan con la clave principal
			for (planes_mantenimiento planTest : planes) {
				if (planTest.getIdPlan() == idPlan) {
					//Asignar plan que tenga la id introducida
					plan = planTest;
					repetir = false;
				}
			}
			if (repetir && idPlan != 0) {
				//No se encontro ningun plan con la id introducida y no cancelo la operacion
				System.out.println("No existe ningun plan con esa id");
			}
			
		} while (repetir);
		
		return plan;
	}

	/**El usuario introduce una descripcion
	 * @return String introducida*/
	private String IntroducirDescripcion() {
		//Variable a devolver
		String descripcion;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
		
		do {
			//Pedir la informacion
			System.out.println("Descripcion de la tarea:");
			descripcion = sc.nextLine();
			
			if (descripcion.isBlank()) {
				//Si es vacio
				System.out.println("Por favor, introduzca una descripcion");
			} else {
				//Si no es vacio, continuar
				repetir = false;
			}
			
		} while (repetir);
		
		return descripcion;
	}
	
	/**El usuario introduce la orden
	 * @return int introducida*/
	private int IntroducirOrden() {
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
	
	/**El usuario introduce instruciones detalladas
	 * puede estar en blanco
	 * @return String introducida*/
	private String IntroducirInstruciones() {
		//Variable a devolver
		String instruciones;

		//Pedir la informacion
		System.out.println("Instruciones de la tarea:");
		instruciones = sc.nextLine();
		
		return instruciones;
	}
	
	/**El usuario introduce si la tarea requiere epis
	 * @return valor booleano con la informacion recibida*/
	private boolean IntroducirRequiereEpis() {
		//Valor a devolver
		boolean requiere = false;
		int recibido = 0;
		
		//Recibir informacion
		try {
			//Pedir la informacion
			System.out.print("¿Requiere Epis? (1 = sí)");
			recibido = Integer.parseInt(sc.nextLine());
		} catch (java.lang.NumberFormatException e) {
			//Error si no se introdujo un numero, parece tambier llegar aqui si no se introduce nada
			System.out.println("Introduzca un numero");
		}
		
		//Pasar informacion redibida a boolean
		if (recibido == 1) {
			requiere = true;
		}
		
		return requiere;
	}
	
	/**El usuario introduce los epis necesarios
	 * @return String introducida*/
	private String IntroducirEpisNecesarios() {
		//Variable a devolver
		String Epis;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
		
		do {
			//Pedir la informacion
			System.out.println("Epis necesarios para la tareas:");
			Epis = sc.nextLine();
			
			if (Epis.isBlank()) {
				//Si es vacio
				System.out.println("Por favor, los epis necesarios");
			} else {
				//Si no es vacio, continuar
				repetir = false;
			}
			
		} while (repetir);
		
		return Epis;
	}

	/**El usuario introduce reglas de seguridad
	 * puede estar en blanco
	 * @return String introducida*/
	private String IntroducirReglasSeguridad() {
		//Variable a devolver
		String reglas;

		//Pedir la informacion
		System.out.println("Reglas de seguridad de la tarea:");
		reglas = sc.nextLine();
		
		return reglas;
	}
	
	/**El usuario introduce reglas de seguridad
	 * puede estar en blanco
	 * @return String introducida*/
	private String IntroducirReglasMedioAmbiente() {
		//Variable a devolver
		String reglas;

		//Pedir la informacion
		System.out.println("Reglas de la tarea referentes al medio ambiente:");
		reglas = sc.nextLine();
		
		return reglas;
	}
}
