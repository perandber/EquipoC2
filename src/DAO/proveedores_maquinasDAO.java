package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import Comun.conexion;
import Comun.interfaces;
import Objetos.proveedores;

/**
 * @author Alexandru
 */
public class proveedores_maquinasDAO extends interfaces {

	private static Scanner sc = new Scanner(System.in);

	// MÉTODOS DE LA INTERFAZ

	/** Menu para mostrar al usuario todas las opciones disponibles */
	@Override
	public void Menu() {
		boolean bucle = true;
		int opcion;

		while (bucle) {
			System.out.println("\n═══ MENÚ PROVEEDORES ═══");
			System.out.println("0. Volver");
			System.out.println("1. Mostrar todos");
			System.out.println("2. Crear");
			System.out.println("3. Modificar");
			System.out.println("4. Borrar");
			System.out.print("Opción: ");

			try {
				opcion = Integer.parseInt(sc.nextLine()); 
				} catch (NumberFormatException e) { 
					opcion = -1; 
					}

			switch (opcion) {
				case 0: bucle = false;
				break;
				case 1: Mostrar();
				break;
				case 2: Crear();
				break;
				case 3: Modificar();
				break;
				case 4: Borrar();
				break;
				default: System.out.println("Opción incorrecta");
			}
		}
	}


	/** Select de todos los objetos
	 * @return false si fallo */
	@Override
	public boolean Mostrar() {
		ArrayList<Object> lista = Recibir();
		if (lista == null) {
			System.out.println("Error al consultar la base de datos.");
			return false;
		}

		if (lista.isEmpty()) {
			System.out.println("No hay proveedores registrados.");
		} else {
			System.out.println("\n── Proveedores (" + lista.size() + ") ──");
			for (Object obj : lista) System.out.println(obj);
		}
		return true;
	}


	/** No accedido por los usuarios.
	 * Usado para recibir todos los objetos del tipo.
	 * @return arraylist con todos los objetos recibidos */
	@Override
	public ArrayList<Object> Recibir() {
		ArrayList<Object> lista = new ArrayList<>();
		Connection con = conexion.Conectar();
		if (con == null) return null;

		String sql = "SELECT * FROM proveedores_maquinas ORDER BY id_proveedor";
		try (Statement stmt = con.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) lista.add(filaAObjeto(rs));
		} catch (SQLException e) {
			System.out.println("Error al consultar proveedores: " + e.getMessage());
			return null;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
		return lista;
	}


	/** Crear un objeto
	 * @return false si fallo */
	@Override
	protected boolean Crear() {
		System.out.println("\n── Nuevo proveedor ──");

		System.out.print("Nombre: ");
		String nombre = sc.nextLine().trim();
		if (nombre.isEmpty()) { System.out.println("El nombre es obligatorio."); return false; }

		System.out.print("Contacto: ");
		String contacto = sc.nextLine().trim();

		System.out.print("Teléfono: ");
		String telefono = sc.nextLine().trim();

		System.out.print("Email: ");
		String email = sc.nextLine().trim();

		System.out.print("Dirección: ");
		String direccion = sc.nextLine().trim();

		proveedores p = new proveedores();
		p.setNombre(nombre);
		p.setContacto(contacto);
		p.setTelefono(telefono);
		p.setEmail(email);
		p.setDireccion(direccion);

		boolean ok = insertar(p);
		System.out.println(ok ? "Proveedor creado correctamente." : "No se pudo crear el proveedor.");
		return ok;
	}


	/** Modificar un objeto existente
	 * @return false si fallo */
	@Override
	protected boolean Modificar() {
		System.out.print("\nID del proveedor a modificar: ");
		int id;
		try {
			id = Integer.parseInt(sc.nextLine()); 
			} catch (NumberFormatException e) {
				System.out.println("ID inválido."); 
				return false; }

		proveedores p = buscarPorId(id);
		if (p == null) {
			System.out.println("No existe un proveedor con id " + id); 
		return false; 
		}

		System.out.println("Proveedor actual: " + p);
		System.out.println("(Deja vacío un campo para mantener el valor actual)");

		System.out.print("Nombre [" + p.getNombre() + "]: ");
		String v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setNombre(v);

		System.out.print("Contacto [" + p.getContacto() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setContacto(v);

		System.out.print("Teléfono [" + p.getTelefono() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setTelefono(v);

		System.out.print("Email [" + p.getEmail() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setEmail(v);

		System.out.print("Dirección [" + p.getDireccion() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setDireccion(v);

		boolean ok = actualizar(p);
		System.out.println(ok ? "Proveedor modificado correctamente." : "No se pudo modificar.");
		return ok;
	}


	/** Borrar un objeto
	 * @return false si fallo */
	@Override
	protected boolean Borrar() {
		System.out.print("\nID del proveedor a borrar: ");
		int id;
		try { id = Integer.parseInt(sc.nextLine()); }
		catch (NumberFormatException e) { System.out.println("ID inválido."); return false; }

		System.out.print("¿Confirmas borrar el proveedor " + id + "? (s/n): ");
		if (!sc.nextLine().trim().equalsIgnoreCase("s")) {
			System.out.println("Cancelado.");
			return false;
		}

		int resultado = borrar(id);
		switch (resultado) {
			case 1:
				System.out.println("Proveedor borrado correctamente.");
				return true;
			case 0:
				System.out.println("No se borró ninguna fila (¿existe ese id?).");
				return false;
			case -1:
				System.out.println("No se puede borrar: hay máquinas o piezas asociadas a este proveedor.");
				return false;
			default:
				System.out.println("Error al borrar.");
				return false;
		}
	}

	// OPERACIONES DE BASE DE DATOS

	/** Devuelve un proveedor por su id, o null si no existe */
	public proveedores buscarPorId(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return null;

		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM proveedores_maquinas WHERE id_proveedor=?")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return filaAObjeto(rs);
			}
		} catch (SQLException e) {
			System.out.println("Error buscarPorId: " + e.getMessage());
		} finally {
			try { 
				con.close(); 
				} catch (SQLException ignored) {
					
				}
		}
		return null;
	}


	/** Inserta un nuevo proveedor en la BD */
	public boolean insertar(proveedores p) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		String sql = "INSERT INTO proveedores_maquinas (nombre, contacto, telefono, email, direccion) " +
		             "VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, p.getNombre());
			ps.setString(2, p.getContacto());
			ps.setString(3, p.getTelefono());
			ps.setString(4, p.getEmail());
			ps.setString(5, p.getDireccion());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al insertar proveedor: " + e.getMessage());
			return false;
		} finally {
			try { con.close(); 
			} catch (SQLException ignored) {
				
			}
		}
	}


	/** Actualiza los campos de un proveedor existente */
	public boolean actualizar(proveedores p) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		String sql = "UPDATE proveedores_maquinas SET nombre=?, contacto=?, telefono=?, email=?, direccion=? " +
		             "WHERE id_proveedor=?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, p.getNombre());
			ps.setString(2, p.getContacto());
			ps.setString(3, p.getTelefono());
			ps.setString(4, p.getEmail());
			ps.setString(5, p.getDireccion());
			ps.setInt(6, p.getId_proveedor());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al modificar: " + e.getMessage());
			return false;
		} finally {
			try { 
				con.close(); 
				} catch (SQLException ignored) {
					
				}
		}
	}


	/** Borra un proveedor de la BD.
	 * @return 1 si OK, 0 si no existía, -1 si hay foreign key */
	public int borrar(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return 0;

		try (PreparedStatement ps = con.prepareStatement("DELETE FROM proveedores_maquinas WHERE id_proveedor=?")) {
			ps.setInt(1, id);
			int filas = ps.executeUpdate();
			return filas > 0 ? 1 : 0;
		} catch (SQLException e) {
			if (e.getErrorCode() == 1451) return -1;
			System.out.println("Error al borrar: " + e.getMessage());
			return 0;
		} finally {
			try { 
				con.close(); 
				} catch (SQLException ignored) {
					
				}
		}
	}


	/** Convierte una fila del ResultSet en objeto proveedores */
	private proveedores filaAObjeto(ResultSet rs) throws SQLException {
		proveedores p = new proveedores();
		p.setId_proveedor(rs.getInt("id_proveedor"));
		p.setNombre(rs.getString("nombre"));
		p.setContacto(rs.getString("contacto"));
		p.setTelefono(rs.getString("telefono"));
		p.setEmail(rs.getString("email"));
		p.setDireccion(rs.getString("direccion"));
		return p;
	}
}