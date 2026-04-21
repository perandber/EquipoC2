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
import Objetos.usuario;

/**
 * @author Alexandru
 */
public class usuariosDAO extends interfaces {

	private static Scanner sc = new Scanner(System.in);

	// MÉTODOS DE LA INTERFAZ

	/** Menu para mostrar al usuario todas las opciones disponibles */
	@Override
	public void Menu() {
		boolean bucle = true;
		int opcion;

		while (bucle) {
			System.out.println("\n═══ MENÚ USUARIOS ═══");
			System.out.println("0. Volver");
			System.out.println("1. Mostrar todos");
			System.out.println("2. Crear");
			System.out.println("3. Modificar");
			System.out.println("4. Borrar");
			System.out.print("Opción: ");

			try { opcion = Integer.parseInt(sc.nextLine()); }
			catch (NumberFormatException e) { opcion = -1; }

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
			System.out.println("No hay usuarios registrados.");
		} else {
			System.out.println("\n── Usuarios (" + lista.size() + ") ──");
			for (Object obj : lista) System.out.println(obj);
		}
		return true;
	}


	/** No accedido por los usuarios.
	 * Usado para recibir todos los objetos del tipo.
	 * @return arraylist con todos los objetos recibidos
	 * NOTA: no selecciona password_hash por seguridad */
	@Override
	public ArrayList<Object> Recibir() {
		ArrayList<Object> lista = new ArrayList<>();
		Connection con = conexion.Conectar();
		if (con == null) return null;

		String sql = "SELECT id_usuario, nombre, apellidos, email, rol, activo, fecha_creacion, ultimo_acceso " +
		             "FROM usuarios ORDER BY id_usuario";
		try (Statement stmt = con.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) lista.add(filaAObjeto(rs));
		} catch (SQLException e) {
			System.out.println("Error al consultar usuarios: " + e.getMessage());
			return null;
		} finally {
			try { con.close(); 
			} catch (SQLException ignored) {
				
			}
		}
		return lista;
	}


	/** Crear un objeto
	 * @return false si fallo */
	@Override
	protected boolean Crear() {
		System.out.println("\n── Nuevo usuario ──");

		System.out.print("Nombre: ");
		String nombre = sc.nextLine().trim();
		if (nombre.isEmpty()) { System.out.println("El nombre es obligatorio."); 
		return false; 
		}

		System.out.print("Apellidos: ");
		String apellidos = sc.nextLine().trim();

		System.out.print("Email: ");
		String email = sc.nextLine().trim();
		if (email.isEmpty()) { System.out.println("El email es obligatorio."); 
		return false; 
		}

		System.out.print("Contraseña: ");
		String password = sc.nextLine();
		if (password.isEmpty()) { System.out.println("La contraseña es obligatoria."); 
		return false; 
		}

		// MODO DEMO: guarda la contraseña tal cual (sin hash).
		// Para compatibilidad con la web usar jBCrypt:
		// String hash = BCrypt.hashpw(password, BCrypt.gensalt());
		String hash = password;

		System.out.print("Rol (tecnico/responsable) [tecnico]: ");
		String rol = sc.nextLine().trim();
		if (rol.isEmpty()) rol = "tecnico";

		System.out.print("¿Activo? (s/n) [s]: ");
		String activoStr = sc.nextLine().trim();
		boolean activo = !activoStr.equalsIgnoreCase("n");

		usuario u = new usuario();
		u.setNombre(nombre);
		u.setApellidos(apellidos);
		u.setEmail(email);
		u.setPassword_hash(hash);
		u.setRol(rol);
		u.setActivo(activo);

		int resultado = insertar(u);
		switch (resultado) {
			case 1:
				System.out.println("Usuario creado correctamente.");
				return true;
			case -1:
				System.out.println("Ese email ya está registrado.");
				return false;
			default:
				System.out.println("No se pudo crear el usuario.");
				return false;
		}
	}


	/** Modificar un objeto existente
	 * @return false si fallo */
	@Override
	protected boolean Modificar() {
		System.out.print("\nID del usuario a modificar: ");
		int id;
		try { id = Integer.parseInt(sc.nextLine()); }
		catch (NumberFormatException e) { System.out.println("ID inválido."); 
		return false; 
		}

		usuario u = buscarPorId(id);
		if (u == null) { System.out.println("No existe un usuario con id " + id); 
		return false; 
		}

		System.out.println("Usuario actual: " + u);
		System.out.println("(Deja vacío un campo para mantener el valor actual)");

		System.out.print("Nombre [" + u.getNombre() + "]: ");
		String v = sc.nextLine().trim();
		if (!v.isEmpty()) u.setNombre(v);

		System.out.print("Apellidos [" + u.getApellidos() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) u.setApellidos(v);

		System.out.print("Email [" + u.getEmail() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) u.setEmail(v);

		System.out.print("Rol [" + u.getRol() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) u.setRol(v);

		System.out.print("¿Activo? (s/n) [" + (u.isActivo() ? "s" : "n") + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) u.setActivo(v.equalsIgnoreCase("s"));

		System.out.print("Nueva contraseña (vacío = no cambiar): ");
		String nuevaPass = sc.nextLine();

		boolean ok = actualizar(u, nuevaPass);
		System.out.println(ok ? "Usuario modificado correctamente." : "No se pudo modificar.");
		return ok;
	}


	/** Borrar un objeto
	 * @return false si fallo */
	@Override
	protected boolean Borrar() {
		System.out.print("\nID del usuario a borrar: ");
		int id;
		try { id = Integer.parseInt(sc.nextLine()); 
		} catch (NumberFormatException e) {
			System.out.println("ID inválido."); 
		return false; 
		}

		System.out.print("¿Confirmas borrar el usuario " + id + "? (s/n): ");
		if (!sc.nextLine().trim().equalsIgnoreCase("s")) {
			System.out.println("Cancelado.");
			return false;
		}

		int resultado = borrar(id);
		switch (resultado) {
			case 1:
				System.out.println("Usuario borrado correctamente.");
				return true;
			case 0:
				System.out.println("No se borró ninguna fila (¿existe ese id?).");
				return false;
			case -1:
				System.out.println("No se puede borrar: el usuario tiene actividad registrada en el sistema.");
				System.out.print("¿Quieres DESACTIVARLO en su lugar? (s/n): ");
				if (sc.nextLine().trim().equalsIgnoreCase("s")) {
					boolean ok = desactivar(id);
					System.out.println(ok ? "Usuario desactivado." : "Error al desactivar.");
					return ok;
				}
				return false;
			default:
				System.out.println("Error al borrar.");
				return false;
		}
	}

	// OPERACIONES DE BASE DE DATOS

	/** Devuelve un usuario por su id, o null si no existe
	 * NOTA: no devuelve password_hash por seguridad */
	public usuario buscarPorId(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return null;

		String sql = "SELECT id_usuario, nombre, apellidos, email, rol, activo, fecha_creacion, ultimo_acceso " +
		             "FROM usuarios WHERE id_usuario=?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
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


	/** Inserta un nuevo usuario en la BD.
	 * @return 1 si OK, -1 si email duplicado, 0 si otro error */
	public int insertar(usuario u) {
		Connection con = conexion.Conectar();
		if (con == null) return 0;

		String sql = "INSERT INTO usuarios (nombre, apellidos, email, password_hash, rol, activo) " +
		             "VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, u.getNombre());
			ps.setString(2, u.getApellidos());
			ps.setString(3, u.getEmail());
			ps.setString(4, u.getPassword_hash());
			ps.setString(5, u.getRol());
			ps.setBoolean(6, u.isActivo());

			return ps.executeUpdate() > 0 ? 1 : 0;
		} catch (SQLException e) {
			if (e.getErrorCode() == 1062) 
				return -1;  // email duplicado
			System.out.println("Error al crear usuario: " + e.getMessage());
			return 0;
		} finally {
			try { 
				con.close(); 
				} catch (SQLException ignored) {
					
				}
		}
	}

	/** Actualiza los campos de un usuario existente.
	 * Si nuevaPass está vacío, no cambia la contraseña. */
	public boolean actualizar(usuario u, String nuevaPass) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		String sql;
		if (nuevaPass != null && !nuevaPass.isEmpty()) {
			sql = "UPDATE usuarios SET nombre=?, apellidos=?, email=?, rol=?, activo=?, password_hash=? " +
			      "WHERE id_usuario=?";
		} else {
			sql = "UPDATE usuarios SET nombre=?, apellidos=?, email=?, rol=?, activo=? " +
			      "WHERE id_usuario=?";
		}

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, u.getNombre());
			ps.setString(2, u.getApellidos());
			ps.setString(3, u.getEmail());
			ps.setString(4, u.getRol());
			ps.setBoolean(5, u.isActivo());
			if (nuevaPass != null && !nuevaPass.isEmpty()) {
				ps.setString(6, nuevaPass);
				ps.setInt(7, u.getId_usuario());
			} else {
				ps.setInt(6, u.getId_usuario());
			}

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

	/** Borra un usuario de la BD.
	 * @return 1 si OK, 0 si no existía, -1 si hay foreign key */
	public int borrar(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return 0;

		try (PreparedStatement ps = con.prepareStatement("DELETE FROM usuarios WHERE id_usuario=?")) {
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

	/** Pone activo=0 sin borrar físicamente */
	public boolean desactivar(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		try (PreparedStatement ps = con.prepareStatement("UPDATE usuarios SET activo=0 WHERE id_usuario=?")) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al desactivar: " + e.getMessage());
			return false;
		} finally {
			try { 
				con.close(); 
				} catch (SQLException ignored) {
					
				}
		}
	}


	/** Convierte una fila del ResultSet en objeto usuario
	 * NOTA: nunca lee password_hash por seguridad */
	private usuario filaAObjeto(ResultSet rs) throws SQLException {
		usuario u = new usuario();
		u.setId_usuario(rs.getInt("id_usuario"));
		u.setNombre(rs.getString("nombre"));
		u.setApellidos(rs.getString("apellidos"));
		u.setEmail(rs.getString("email"));
		u.setRol(rs.getString("rol"));
		u.setActivo(rs.getBoolean("activo"));
		u.setFecha_creacion(rs.getDate("fecha_creacion"));
		u.setUltimo_acceso(rs.getDate("ultimo_acceso"));
		return u;
	}
}