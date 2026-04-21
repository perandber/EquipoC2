package Menu;

import java.util.Scanner;

import DAO.analisis_maquinasDAO;
import DAO.archivos_adjuntosDAO;
import DAO.ejecuciones_mantenimientoDAO;
import DAO.gamas_manteniminetoDAO;
import DAO.herramientasDAO;
import DAO.horarios_trabajoDAO;
import DAO.incidenciasDAO;
import DAO.maquinasDAO;
import DAO.maquinas_piezasDAO;
import DAO.ordenes_trabajoDAO;
import DAO.pedidos_compraDAO;
import DAO.pedidos_detalleDAO;
import DAO.piezas_repuestoDAO;
import DAO.proveedores_maquinasDAO;
import DAO.proyectos_gnattDAO;
import DAO.tareas_ejecutadasDAO;
import DAO.tareas_herramientasDAO;
import DAO.tareas_mantenimientoDAO;
import DAO.tareas_proyectoDAO;
import DAO.usuariosDAO;


/**
 * Menu para elegir a que tabla acceder
 * @author Perceval
 */
public class principal {

	private static boolean bucle = true ;//Si pasa a falso, se cierra la aplicacion
	static Scanner sc = new Scanner (System.in); //Crear escaner
	
	
	public void menu() {
		maquinasDAO maquina = new maquinasDAO();
		piezas_repuestoDAO repuesto = new piezas_repuestoDAO();
		proveedores_maquinasDAO proveedores = new proveedores_maquinasDAO();
		usuariosDAO usuario = new usuariosDAO();
		archivos_adjuntosDAO archivos = new archivos_adjuntosDAO();
		ejecuciones_mantenimientoDAO ejecuciones = new ejecuciones_mantenimientoDAO();
		gamas_manteniminetoDAO gamas = new gamas_manteniminetoDAO();
		herramientasDAO herramienta = new herramientasDAO();
		maquinas_piezasDAO piezas = new maquinas_piezasDAO();
		ordenes_trabajoDAO ordenes = new ordenes_trabajoDAO();
		pedidos_compraDAO compra = new pedidos_compraDAO();
		pedidos_detalleDAO detalles = new pedidos_detalleDAO();
		tareas_ejecutadasDAO ejecutadas = new tareas_ejecutadasDAO();
		tareas_herramientasDAO tareasHerramientas = new tareas_herramientasDAO();
		tareas_mantenimientoDAO mantenimiento = new tareas_mantenimientoDAO();
		tareas_proyectoDAO proyecto = new tareas_proyectoDAO();
		analisis_maquinasDAO analisis = new analisis_maquinasDAO();
		horarios_trabajoDAO horarios = new horarios_trabajoDAO();
		incidenciasDAO incidencia = new incidenciasDAO();
		proyectos_gnattDAO gnatt = new proyectos_gnattDAO();
		
		int decision = 1000; //Opcion elejida por el usuario
		
		//Bucle, solo para si el usuario intenta cerrar la aplicacion
		while (bucle) {
			//Imprimir
			System.out.println("0. Salir\n"
					+ "1. Archivos adjuntos\n"
					+ "2. Ejecuciones mantenimiento\n"
					+ "3. Gamas mantenimiento\n"
					+ "4. Herramientas\n"
					+ "5. Horarios trabajo\n"
					+ "6. Incidencias\n"
					+ "7. Maquinas\n"
					+ "8. Maquinas piezas\n"
					+ "9. Ordenes trabajo\n"
					+ "10. Pedidos compra\n"
					+ "11. Pedido detalle\n"
					+ "12. Piezas repuesto\n"
					+ "13. Planes mantenimiento\n"
					+ "14. Proveedores maquinas\n"
					+ "15. Proyectos gantt\n"
					+ "16. Analisis maquinas\n"
					+ "17. Tareas ejecutadas\n"
					+ "18. Tareas herramientas\n"
					+ "19. Taeras mantenimiento\n"
					+ "20. Tareas proyecto\n"
					+ "21. Usuarios\n");
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
				archivos.Menu();
			break;
			case 2:
				ejecuciones.Menu();
			break;
			case 3:
				gamas.Menu();
			break;
			case 4:
				herramienta.Menu();
			break;
			case 5:
				horarios.Menu();
			break;
			case 6:
				incidencia.Menu();
			break;
			case 7:
				maquina.Menu();
			break;
			case 8:
				piezas.Menu();
			break;
			case 9:
				ordenes.Menu();
			break;
			case 10:
				compra.Menu();
			break;
			case 11:
				detalles.Menu();
			break;
			case 12:
				repuesto.Menu();
			break;
			case 13:
				System.out.println("No disponible");
			break;
			case 14:
				proveedores.Menu();
			break;
			case 15:
				gnatt.Menu();
			break;
			case 16:
				analisis.Menu();
			break;
			case 17:
				ejecutadas.Menu();
			break;
			case 18:
				tareasHerramientas.Menu();
			break;
			case 19:
				mantenimiento.Menu();
			break;
			case 20:
				proyecto.Menu();
			break;
			case 21:
				usuario.Menu();
			break;
			default:
				//Opcion fuera del rango
				System.out.println("Opcion incorrecta o no existente");
			break;
			}
		}
	}

}
