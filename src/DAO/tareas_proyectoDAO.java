package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import Comun.conexion;
import Comun.interfaces;
import Objetos.proyectos_gantt;
import Objetos.tareas_proyecto;
import Objetos.usuario;

/** Manejar, crear y borrar el objeto del mismo nombre
 * @author Perceval*/
public class tareas_proyectoDAO extends interfaces{
	ArrayList<tareas_proyecto> tareas = new ArrayList<tareas_proyecto>(); 
	ArrayList<proyectos_gantt> proyectos = new ArrayList<proyectos_gantt>();
	ArrayList<usuario> usuariosList = new ArrayList<usuario>();
	
	static Scanner sc = new Scanner (System.in); //Crear escaner
	proyectos_gnattDAO proyectosDAO = new proyectos_gnattDAO();
	usuariosDAO usuariosDAO = new usuariosDAO();
	
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
					+ "5. Mostrar proyectos\n"
					+ "6. Mostrar usuarios");
			
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
				proyectosDAO.Mostrar();
			break;
			case 6:
				usuariosDAO.Mostrar();
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
		ArrayList<tareas_proyecto> tareasP = new ArrayList<tareas_proyecto>();
		Connection con = conexion.Conectar();
		Statement stat = null;
		ResultSet rs = null;
		
		//Recibir objetos de las clases agregadas
		ArrayList<Object> proyectosTemp = proyectosDAO.Recibir();
		for (Object proyecto : proyectosTemp) {
			proyectos.add((proyectos_gantt) proyecto);
		}
		
		ArrayList<Object> usuariosTemp = usuariosDAO.Recibir();
		for (Object usuario : usuariosTemp) {
			usuariosList.add((usuario) usuario);
		}
		
		//Crear query
		try {
			stat = con.createStatement();
			
			//Query a ejecutar
			rs = stat.executeQuery ("select * from tareas_proyecto");
			
			//Bucle, una iteracion por cada fila recibida
			while (rs.next()) {
				int id = rs.getInt(1);
				int idProyecto = rs.getInt(2);
				String nombre = rs.getString(3);
				String descripcion = rs.getString(4);
				LocalDate fechaInicio = rs.getDate(5).toLocalDate();
				LocalDate fechaFin = rs.getDate(6).toLocalDate();
				int duracion = rs.getInt(7);
				String dependencias = rs.getString(8);
				int idResponsable = rs.getInt(9);
				int progreso = rs.getInt(10);
				String estado = rs.getString(11);
				
				//Asignar proyecto basado en la clave principal recibida
				proyectos_gantt proyecto = null;
				for (proyectos_gantt proyectoTest : proyectos) {
					if (proyectoTest.getIdProyecto() == idProyecto) {
						proyecto = proyectoTest;
					}
				}
				
				//Asignar responsable basado en la clave principal recibida
				usuario responsable = null;
				for (usuario usuarioTest : usuariosList) {
					if (usuarioTest.getId_usuario() == idResponsable) {
						responsable = usuarioTest;
					}
				}
				
				tareasP.add(new tareas_proyecto(id, proyecto, nombre, descripcion, fechaInicio, fechaFin, duracion, dependencias, responsable, progreso, estado));
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
		return tareasP;
	}

	@Override
	protected boolean Crear() {
		//Recibir objetos de las clases agregadas
		ArrayList<Object> proyectosTemp = proyectosDAO.Recibir();
		for (Object proyecto : proyectosTemp) {
			proyectos.add((proyectos_gantt) proyecto);
		}
		
		ArrayList<Object> usuariosTemp = usuariosDAO.Recibir();
		for (Object usuario : usuariosTemp) {
			usuariosList.add((usuario) usuario);
		}
		
		//Recibir objetos de la clase
		tareas = Recibir();
		
		//Datos a pedir
		int id;
		proyectos_gantt proyecto;
		String nombre;
		String descripcion;
		LocalDate fechaInicio;
		LocalDate fechaFin;
		int duracion;
		String dependencias;
		usuario responsable;
		int progreso;
		String estado;
			
		//Recibir id de la tarea 
		id = IntroducirIdNueva();
		if (id == 0) {
			//Usuario a cancelado la operacion
			return false;
		}
		
		//Recibir idProyecto
		proyecto = IntroducirIdProyecto();
		if (proyecto == null) {
			//Usuario a cancelado la operacion;
			return false;
		}
		
		//Recibir nombre
		nombre = IntroducirNombre();
		
		//Recibir descripcion
		descripcion = IntroducirDescripcion();
		
		//Recibir fecha inicio
		fechaInicio = IntroducirFechaInicio();
		
		//Recibir fecha fin
		fechaFin = IntroducirFechaFin();
		
		//Calcular duracion
		duracion = (int) java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaFin);
		
		//Recibir dependencias
		dependencias = IntroducirDependencias();
		
		//Recibir responsable
		responsable = IntroducirResponsable();
		if (responsable == null) {
			//Usuario a cancelado la operacion;
			return false;
		}
		
		//Recibir progreso
		progreso = IntroducirProgreso();
		
		//Recibir estado
		estado = IntroducirEstado();
		
		//Añadir en local
		boolean exito = tareas.add(new tareas_proyecto(id, proyecto, nombre, descripcion, fechaInicio, fechaFin, duracion, dependencias, responsable, progreso, estado));
		
		//Intentar introducir en la base de datos
		if (exito) {
			Connection con = conexion.Conectar();
            Statement stat = null;
            
            try {
            	stat = con.createStatement();
            	stat.executeUpdate("insert into tareas_proyecto"
            			+ " (id_tarea, id_proyecto, nombre, descripcion, fecha_inicio, fecha_fin, duracion, dependencias, id_responsable, progreso, estado)"
            			+ " values ("+id+", "+proyecto.getIdProyecto()+", '"+nombre+"', '"+descripcion+"', '"+fechaInicio+"', '"+fechaFin+"', "+duracion+", '"+dependencias+"', "+responsable.getId_usuario()+", "+progreso+", '"+estado+"')");
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
		//Recibir objetos de las clases agregadas
		ArrayList<Object> proyectosTemp = proyectosDAO.Recibir();
		for (Object proyecto : proyectosTemp) {
			proyectos.add((proyectos_gantt) proyecto);
		}
		
		ArrayList<Object> usuariosTemp = usuariosDAO.Recibir();
		for (Object usuario : usuariosTemp) {
			usuariosList.add((usuario) usuario);
		}
		
		//Recibir objetos de la clase
		tareas = Recibir();
				
		//Datos a borrar
		tareas_proyecto tarea;
		
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
            	stat.executeUpdate("delete from tareas_proyecto where id_tarea= '"+tarea.getId()+"'");
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
		//Recibir objetos de las clases agregadas
		ArrayList<Object> proyectosTemp = proyectosDAO.Recibir();
		for (Object proyecto : proyectosTemp) {
			proyectos.add((proyectos_gantt) proyecto);
		}
		
		ArrayList<Object> usuariosTemp = usuariosDAO.Recibir();
		for (Object usuario : usuariosTemp) {
			usuariosList.add((usuario) usuario);
		}
		
		//Recibir objetos de la clase
		tareas = Recibir();

		
		//Datos a pedir
		tareas_proyecto tarea;
		proyectos_gantt proyecto;
		String nombre;
		String descripcion;
		LocalDate fechaInicio;
		LocalDate fechaFin;
		int duracion;
		String dependencias;
		usuario responsable;
		int progreso;
		String estado;
		
		
		//Recibir tarea
		tarea = IntroducirIdExistente();
		if (tarea == null ) {
			//Usuairo a cancelado la operacion
			return false;
		}
		
		//Recibir idProyecto
		proyecto = IntroducirIdProyecto();
		if (proyecto == null) {
			//Usuario a cancelado la operacion;
			return false;
		}
		
		//Recibir nombre
		nombre = IntroducirNombre();
		
		//Recibir descripcion
		descripcion = IntroducirDescripcion();
		
		//Recibir fecha inicio
		fechaInicio = IntroducirFechaInicio();
		
		//Recibir fecha fin
		fechaFin = IntroducirFechaFin();
		
		//Calcular duracion
		duracion = (int) java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaFin);
		
		//Recibir dependencias
		dependencias = IntroducirDependencias();
		
		//Recibir responsable
		responsable = IntroducirResponsable();
		if (responsable == null) {
			//Usuario a cancelado la operacion;
			return false;
		}
		
		//Recibir progreso
		progreso = IntroducirProgreso();
		
		//Recibir estado
		estado = IntroducirEstado();
		
		//Cambiar en local
		tarea.setProyecto(proyecto);
		tarea.setNombre(nombre);
		tarea.setDescripcion(descripcion);
		tarea.setFechaInicio(fechaInicio);
		tarea.setFechaFin(fechaFin);
		tarea.setDuracion(duracion);
		tarea.setDependencias(dependencias);
		tarea.setResponsable(responsable);
		tarea.setProgreso(progreso);
		tarea.setEstado(estado);
		
		//Intentar cambios en base de datos externa
		Connection con = conexion.Conectar();
        Statement stat = null;
 
        
        try {
        	stat = con.createStatement();
        	stat.executeUpdate("update tareas_proyecto"
        			+ " set id_proyecto = "+proyecto.getIdProyecto()+", nombre = '"+nombre+"', descripcion = '"+descripcion+"',"
        			+ " fecha_inicio = '"+fechaInicio+"', fecha_fin = '"+fechaFin+"', duracion = "+duracion+","
        			+ " dependencias = '"+dependencias+"', id_responsable = "+responsable.getId_usuario()+", progreso = "+progreso+", estado = '"+estado+"'"
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
		System.out.println("--Tareas de proyecto--");
		for (tareas_proyecto tarea : tareas) {
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
			for (tareas_proyecto tareaTest : tareas) {
				if (tareaTest.getId() == id) {
					System.out.println("Tarea con esa id ya existente");
					repetir = true;
				}
			}
			
		} while (repetir);
		
		return id;
	}
	
	/**El usuario introduce una id de la tarea que ya exista
	 * @return la tarea introducida*/
	private tareas_proyecto IntroducirIdExistente() {
		//Variable a devolver
		tareas_proyecto tarea = null;
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
			//Comprobar si existe una tarea con la clave principal
			for (tareas_proyecto tareaTest : tareas) {
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
	
	/**El usuario introduce la id de proyecto
	 * @return proyecto de la id introducida*/
	private proyectos_gantt IntroducirIdProyecto() {
	
		int idProyecto = 0;
		//Variable a devolver
		proyectos_gantt proyecto = null;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
				
		do {
			try {
				//Pedir la informacion
				System.out.print("ID del proyecto (0 para cancelar):");
				idProyecto = Integer.parseInt(sc.nextLine());
			} catch (java.lang.NumberFormatException e) {
				//Error si no es un numero
				System.out.println("Introduzca un numero");
			}
			
			if (idProyecto == 0) {
				//Usuario elejio cancelar la operacion
				System.out.println("Cancelando");
				return null;
			}
			
			//Comprobaciones
			//Comprobar si existe un proyecto con la clave principal
			for (proyectos_gantt proyectoTest : proyectos) {
				if (proyectoTest.getIdProyecto() == idProyecto) {
					//Asignar proyecto que tenga la id introducida
					proyecto = proyectoTest;
					repetir = false;
				}
			}
			if (repetir && idProyecto != 0) {
				//No se encontro ningun proyecto con la id introducida y no cancelo la operacion
				System.out.println("No existe ningun proyecto con esa id");
			}
			
		} while (repetir);
		
		return proyecto;
	}

	/**El usuario introduce el nombre
	 * @return String introducida*/
	private String IntroducirNombre() {
		//Variable a devolver
		String nombre;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
		
		do {
			//Pedir la informacion
			System.out.println("Nombre de la tarea:");
			nombre = sc.nextLine();
			
			if (nombre.isBlank()) {
				//Si es vacio
				System.out.println("Por favor, introduzca un nombre");
			} else {
				//Si no es vacio, continuar
				repetir = false;
			}
			
		} while (repetir);
		
		return nombre;
	}

	/**El usuario introduce una descripcion
	 * @return String introducida*/
	private String IntroducirDescripcion() {
		//Variable a devolver
		String descripcion;

		//Pedir la informacion
		System.out.println("Descripcion de la tarea:");
		descripcion = sc.nextLine();
		
		return descripcion;
	}
	
	/**El usuario introduce la fecha de inicio
	 * @return LocalDate introducida*/
	private LocalDate IntroducirFechaInicio() {
		//Variable a devolver
		LocalDate fecha = null;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
		
		do {
			try {
				//Pedir la informacion
				System.out.print("Fecha de inicio (yyyy-MM-dd):");
				fecha = LocalDate.parse(sc.nextLine());
				repetir = false;
			} catch (java.time.format.DateTimeParseException e) {
				//Error si no se introdujo una fecha valida
				System.out.println("Introduzca una fecha valida en formato yyyy-MM-dd");
				repetir = true;
			}
			
		} while (repetir);
		
		return fecha;
	}
	
	/**El usuario introduce la fecha de fin
	 * @return LocalDate introducida*/
	private LocalDate IntroducirFechaFin() {
		//Variable a devolver
		LocalDate fecha = null;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
		
		do {
			try {
				//Pedir la informacion
				System.out.print("Fecha de fin (yyyy-MM-dd):");
				fecha = LocalDate.parse(sc.nextLine());
				repetir = false;
			} catch (java.time.format.DateTimeParseException e) {
				//Error si no se introdujo una fecha valida
				System.out.println("Introduzca una fecha valida en formato yyyy-MM-dd");
				repetir = true;
			}
			
		} while (repetir);
		
		return fecha;
	}

	/**El usuario introduce las dependencias
	 * puede estar en blanco
	 * @return String introducida*/
	private String IntroducirDependencias() {
		//Variable a devolver
		String dependencias;

		//Pedir la informacion
		System.out.println("Dependencias de la tarea:");
		dependencias = sc.nextLine();
		
		return dependencias;
	}
	
	/**El usuario introduce el responsable
	 * @return usuario responsable*/
	private usuario IntroducirResponsable() {
	
		int idUsuario = 0;
		//Variable a devolver
		usuario responsable = null;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
				
		do {
			try {
				//Pedir la informacion
				System.out.print("ID del responsable (0 para cancelar):");
				idUsuario = Integer.parseInt(sc.nextLine());
			} catch (java.lang.NumberFormatException e) {
				//Error si no es un numero
				System.out.println("Introduzca un numero");
			}
			
			if (idUsuario == 0) {
				//Usuario elejio cancelar la operacion
				System.out.println("Cancelando");
				return null;
			}
			
			//Comprobaciones
			//Comprobar si existe un usuario con la clave principal
			for (usuario usuarioTest : usuariosList) {
				if (usuarioTest.getId_usuario() == idUsuario) {
					//Asignar usuario que tenga la id introducida
					responsable = usuarioTest;
					repetir = false;
				}
			}
			if (repetir && idUsuario != 0) {
				//No se encontro ningun usuario con la id introducida y no cancelo la operacion
				System.out.println("No existe ningun usuario con esa id");
			}
			
		} while (repetir);
		
		return responsable;
	}
	
	/**El usuario introduce el progreso
	 * @return int introducida*/
	private int IntroducirProgreso() {
		//Variable a devolver
		int progreso = 0;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
		
		do {
			
			try {
				//Pedir la informacion
				System.out.print("Progreso (0-100):");
				progreso = Integer.parseInt(sc.nextLine());
				
				if (progreso < 0 || progreso > 100) {
					System.out.println("El progreso debe estar entre 0 y 100");
					repetir = true;
				} else {
					repetir = false;
				}
			} catch (java.lang.NumberFormatException e) {
				//Error si no se introdujo un numero
				System.out.println("Introduzca un numero");
				repetir = true;
			}
			
		} while (repetir);
		
		return progreso;
	}
	
	/**El usuario introduce el estado
	 * @return String introducida*/
	private String IntroducirEstado() {
		//Variable a devolver
		String estado;
		
		//Repetir el bucle pidiendo al usuario informacion
		boolean repetir = true;
		
		do {
			//Pedir la informacion
			System.out.println("Estado de la tarea:");
			estado = sc.nextLine();
			
			if (estado.isBlank()) {
				//Si es vacio
				System.out.println("Por favor, introduzca un estado");
			} else if (estado.length() > 15) {
				//Si excede 15 caracteres
				System.out.println("El estado no puede exceder 15 caracteres");
			} else {
				//Si es valido, continuar
				repetir = false;
			}
			
		} while (repetir);
		
		return estado;
	}
}
