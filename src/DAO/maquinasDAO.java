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
import Objetos.maquina;

/**
 * @author Alexandru
 */
public class maquinasDAO extends interfaces {

	private static Scanner sc = new Scanner(System.in);


	// ════════════════════════════════════════════════════════════
	// MÉTODOS DE LA INTERFAZ
	// ════════════════════════════════════════════════════════════

	/** Menu para mostrar al usuario todas las opciones disponibles */
	@Override
	public void Menu() {
		boolean bucle = true;
		int opcion;

		while (bucle) {
			System.out.println("\n═══ MENÚ MÁQUINAS ═══");
			System.out.println("0. Volver");
			System.out.println("1. Mostrar todas");
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
			System.out.println("No hay máquinas registradas.");
		} else {
			System.out.println("\n── Máquinas registradas (" + lista.size() + ") ──");
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

		String sql = "SELECT * FROM maquinas ORDER BY id_maquina";
		try (Statement stmt = con.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) lista.add(filaAObjeto(rs));
		} catch (SQLException e) {
			System.out.println("Error al consultar máquinas: " + e.getMessage());
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
		System.out.println("\n── Nueva máquina ──");

		System.out.print("Nombre: ");
		String nombre = sc.nextLine().trim();
		if (nombre.isEmpty()) { System.out.println("El nombre es obligatorio."); return false; }

		System.out.print("Tipo: ");
		String tipo = sc.nextLine().trim();

		System.out.print("Número de serie (vacío = ninguno): ");
		String numeroSerie = sc.nextLine().trim();
		if (numeroSerie.isEmpty()) numeroSerie = null;

		System.out.print("Ubicación: ");
		String ubicacion = sc.nextLine().trim();

		System.out.print("Fabricante: ");
		String fabricante = sc.nextLine().trim();

		System.out.print("Fecha compra (YYYY-MM-DD, vacío = hoy): ");
		String fechaCompra = sc.nextLine().trim();
		if (fechaCompra.isEmpty()) fechaCompra = null;

		System.out.print("Estado [disponible]: ");
		String estado = sc.nextLine().trim();
		if (estado.isEmpty()) estado = "disponible";

		System.out.print("QR code (vacío = ninguno): ");
		String qr = sc.nextLine().trim();
		if (qr.isEmpty()) qr = null;

		System.out.print("ID proveedor (vacío = sin proveedor): ");
		String provStr = sc.nextLine().trim();
		Integer proveedorId = null;
		if (!provStr.isEmpty()) {
			try {
				proveedorId = Integer.parseInt(provStr); 
				}
			catch (NumberFormatException e) {
				System.out.println("ID inválido, se deja vacío."); 
				}
		}

		System.out.print("¿Activa? (s/n) [s]: ");
		String activaStr = sc.nextLine().trim();
		boolean activa = !activaStr.equalsIgnoreCase("n");

		maquina m = new maquina();
		m.setNombre(nombre);
		m.setTipo(tipo);
		m.setNumero_serie(numeroSerie);
		m.setUbicacion(ubicacion);
		m.setFabricante(fabricante);
		m.setEstado(estado);
		m.setQr_code(qr);
		m.setProveedor_id(proveedorId);
		m.setActiva(activa);

		boolean ok = insertar(m, fechaCompra);
		System.out.println(ok ? "Máquina creada correctamente." : "No se pudo crear la máquina.");
		return ok;
	}


	/** Modificar un objeto existente
	 * @return false si fallo */
	@Override
	protected boolean Modificar() {
		System.out.print("\nID de la máquina a modificar: ");
		int id;
		try {
			id = Integer.parseInt(sc.nextLine()); }
		catch (NumberFormatException e) { System.out.println("ID inválido."); 
		return false; 
		}

		maquina m = buscarPorId(id);
		if (m == null) { System.out.println("No existe una máquina con id " + id); 
		return false; 
		}

		System.out.println("Máquina actual: " + m);
		System.out.println("(Deja vacío un campo para mantener el valor actual)");

		System.out.print("Nombre [" + m.getNombre() + "]: ");
		String v = sc.nextLine().trim();
		if (!v.isEmpty()) m.setNombre(v);

		System.out.print("Tipo [" + m.getTipo() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) m.setTipo(v);

		System.out.print("Número serie [" + m.getNumero_serie() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) m.setNumero_serie(v);

		System.out.print("Ubicación [" + m.getUbicacion() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) m.setUbicacion(v);

		System.out.print("Fabricante [" + m.getFabricante() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) m.setFabricante(v);

		System.out.print("Estado [" + m.getEstado() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) m.setEstado(v);

		System.out.print("¿Activa? (s/n) [" + (m.isActiva() ? "s" : "n") + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) m.setActiva(v.equalsIgnoreCase("s"));

		boolean ok = actualizar(m);
		System.out.println(ok ? "Máquina modificada correctamente." : "No se pudo modificar.");
		return ok;
	}


	/** Borrar un objeto
	 * @return false si fallo */
	@Override
	protected boolean Borrar() {
		System.out.print("\nID de la máquina a borrar: ");
		int id;
		try {
			id = Integer.parseInt(sc.nextLine()); 
			} catch (NumberFormatException e) {
			System.out.println("ID inválido."); 
			return false; 
			}

		System.out.print("¿Confirmas borrar la máquina " + id + "? (s/n): ");
		if (!sc.nextLine().trim().equalsIgnoreCase("s")) {
			System.out.println("Cancelado.");
			return false;
		}

		int resultado = borrar(id);
		switch (resultado) {
			case 1:
				System.out.println("Máquina borrada correctamente.");
				return true;
			case 0:
				System.out.println("No se borró ninguna fila (¿existe ese id?).");
				return false;
			case -1:
				System.out.println("No se puede borrar: hay análisis, órdenes o piezas asociadas a esta máquina.");
				System.out.print("¿Quieres DESACTIVARLA en su lugar? (s/n): ");
				if (sc.nextLine().trim().equalsIgnoreCase("s")) {
					boolean ok = desactivar(id);
					System.out.println(ok ? "Máquina desactivada." : "Error al desactivar.");
					return ok;
				}
				return false;
			default:
				System.out.println("Error al borrar.");
				return false;
		}
	}

	// OPERACIONES DE BASE DE DATOS

	/** Devuelve una máquina por su id, o null si no existe */
	public maquina buscarPorId(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return null;

		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM maquinas WHERE id_maquina=?")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return filaAObjeto(rs);
			}
		} catch (SQLException e) {
			System.out.println("Error buscarPorId: " + e.getMessage());
		} finally {
			try { con.close(); 
			} catch (SQLException ignored) {
				
			}
		}
		return null;
	}

	/** Inserta una nueva máquina en la BD */
	public boolean insertar(maquina m, String fechaCompra) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		String sql = "INSERT INTO maquinas (nombre, tipo, numero_serie, ubicacion, fabricante, " +
		             "fecha_compra, estado, qr_code, proveedor_id, activa) " +
		             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, m.getNombre());
			ps.setString(2, m.getTipo());
			ps.setString(3, m.getNumero_serie());
			ps.setString(4, m.getUbicacion());
			ps.setString(5, m.getFabricante());
			ps.setString(6, fechaCompra);
			ps.setString(7, m.getEstado());
			ps.setString(8, m.getQr_code());
			if (m.getProveedor_id() != null) ps.setInt(9, m.getProveedor_id());
			else                             ps.setNull(9, java.sql.Types.INTEGER);
			ps.setBoolean(10, m.isActiva());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al insertar máquina: " + e.getMessage());
			return false;
		} finally {
			try { 
				con.close(); 
				} catch (SQLException ignored) {
					
				}
		}
	}

	/** Actualiza los campos de una máquina existente */
	public boolean actualizar(maquina m) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		String sql = "UPDATE maquinas SET nombre=?, tipo=?, numero_serie=?, ubicacion=?, " +
		             "fabricante=?, estado=?, activa=? WHERE id_maquina=?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, m.getNombre());
			ps.setString(2, m.getTipo());
			ps.setString(3, m.getNumero_serie());
			ps.setString(4, m.getUbicacion());
			ps.setString(5, m.getFabricante());
			ps.setString(6, m.getEstado());
			ps.setBoolean(7, m.isActiva());
			ps.setInt(8, m.getId_maquina());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al modificar: " + e.getMessage());
			return false;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
	}


	/** Borra una máquina de la BD.
	 * @return 1 si OK, 0 si no existía, -1 si hay foreign key */
	public int borrar(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return 0;

		try (PreparedStatement ps = con.prepareStatement("DELETE FROM maquinas WHERE id_maquina=?")) {
			ps.setInt(1, id);
			int filas = ps.executeUpdate();
			return filas > 0 ? 1 : 0;
		} catch (SQLException e) {
			if (e.getErrorCode() == 1451) return -1;
			System.out.println("Error al borrar: " + e.getMessage());
			return 0;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
	}


	/** Pone activa=0 sin borrar físicamente */
	public boolean desactivar(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return false;

		try (PreparedStatement ps = con.prepareStatement("UPDATE maquinas SET activa=0 WHERE id_maquina=?")) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al desactivar: " + e.getMessage());
			return false;
		} finally {
			try { con.close(); } catch (SQLException ignored) {}
		}
	}


	/** Convierte una fila del ResultSet en objeto maquina */
	private maquina filaAObjeto(ResultSet rs) throws SQLException {
		maquina m = new maquina();
		m.setId_maquina(rs.getInt("id_maquina"));
		m.setNombre(rs.getString("nombre"));
		m.setTipo(rs.getString("tipo"));
		m.setNumero_serie(rs.getString("numero_serie"));
		m.setUbicacion(rs.getString("ubicacion"));
		m.setFabricante(rs.getString("fabricante"));
		m.setFecha_compra(rs.getDate("fecha_compra"));
		m.setEstado(rs.getString("estado"));
		m.setQr_code(rs.getString("qr_code"));
		int prov = rs.getInt("proveedor_id");
		m.setProveedor_id(rs.wasNull() ? null : prov);
		m.setActiva(rs.getBoolean("activa"));
		m.setFecha_registro(rs.getDate("fecha_registro"));
		return m;
	}
}