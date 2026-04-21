package Comun;

import java.util.ArrayList;

/**
 * @author Perceval
*/
public abstract class interfaces {

	/**Menu para mostrar al usuario todas las opciones disponibles*/
	public abstract void Menu();
	
	/**Select de todos los objetos
	 * @return false si fallo
	 */
	public abstract boolean Mostrar();
	
	/**No accedido por los usuarios
	 * usado para recebir todos los objetos del tipo
	 * @return arraylist con todos los objetos recibidos
	 */
	public abstract ArrayList<Object> Recibir();
		
	/**Crear un objeto
	 * @return false si fallo*/
	protected abstract boolean Crear();
	
	/** Borrar un objeto
	 * @return false si fallo */
	protected abstract boolean Borrar();
	
	/** Modificar un objeto existente
	 * @return false si fallo*/
	protected abstract boolean Modificar();

}
