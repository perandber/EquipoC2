package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Scanner;

import Comun.conexion;
import Comun.interfaces;
import Objetos.piezas_repuesto;

/**
 * @author Alexandru
 */
public class piezas_repuestoDAO extends interfaces {

	private static Scanner sc = new Scanner(System.in);

	/** Menu para mostrar al usuario todas las opciones disponibles */
	@Override
	public  void Menu() {
		boolean bucle = true;
		int opcion;

		while (bucle) {
			System.out.println("\n═══ MENÚ PIEZAS DE REPUESTO ═══");
			System.out.println("0. Volver");
			System.out.println("1. Mostrar todas");
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
			System.out.println("No hay piezas registradas.");
		} else {
			System.out.println("\n── Piezas de repuesto (" + lista.size() + ") ──");
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

		String sql = "SELECT * FROM piezas_repuesto ORDER BY id_pieza";
		try (Statement stmt = con.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) lista.add(filaAObjeto(rs));
		} catch (SQLException e) {
			System.out.println("Error al consultar piezas: " + e.getMessage());
			return null;
		} finally {
			try { 
				con.close(); } catch (SQLException ignored) {
					
				}
		}
		return lista;
	}


	/** Crear un objeto
	 * @return false si fallo */
	@Override
	protected boolean Crear() {
		System.out.println("\n── Nueva pieza de repuesto ──");

		System.out.print("Nombre: ");
		String nombre = sc.nextLine().trim();
		if (nombre.isEmpty()) { 
			System.out.println("El nombre es obligatorio."); return false; }

		System.out.print("Código proveedor (vacío = ninguno): ");
		String codProv = sc.nextLine().trim();
		if (codProv.isEmpty()) codProv = null;

		System.out.print("Código interno (vacío = ninguno): ");
		String codInt = sc.nextLine().trim();
		if (codInt.isEmpty()) codInt = null;

		System.out.print("Descripción: ");
		String descripcion = sc.nextLine().trim();

		System.out.print("Stock actual [0]: ");
		String sActualStr = sc.nextLine().trim();
		int stockActual = 0;
		if (!sActualStr.isEmpty()) {
			try { stockActual = Integer.parseInt(sActualStr);
			} catch (NumberFormatException e) { 
				System.out.println("Valor inválido, uso 0."); 
			}
		}

		System.out.print("Stock mínimo [0]: ");
		String sMinimoStr = sc.nextLine().trim();
		int stockMinimo = 0;
		if (!sMinimoStr.isEmpty()) {
			try { stockMinimo = Integer.parseInt(sMinimoStr); 
			} catch (NumberFormatException e) { 
				System.out.println("Valor inválido, uso 0."); 
				}
		}

		System.out.print("Stock máximo [0]: ");
		String sMaximoStr = sc.nextLine().trim();
		int stockMaximo = 0;
		if (!sMaximoStr.isEmpty()) {
			try { stockMaximo = Integer.parseInt(sMaximoStr); 
			} catch (NumberFormatException e) { 
				System.out.println("Valor inválido, uso 0."); 
				}
		}

		System.out.print("Precio unitario [0.0]: ");
		String precioStr = sc.nextLine().trim();
		float precio = 0.0f;
		if (!precioStr.isEmpty()) {
			try { precio = Float.parseFloat(precioStr); 
			} catch (NumberFormatException e) { 
				System.out.println("Precio inválido, uso 0."); 
				}
		}

		System.out.print("Ubicación almacén (vacío = ninguna): ");
		String ubicacion = sc.nextLine().trim();
		if (ubicacion.isEmpty()) ubicacion = null;

		System.out.print("ID proveedor (vacío = sin proveedor): ");
		String provStr = sc.nextLine().trim();
		Integer idProveedor = null;
		if (!provStr.isEmpty()) {
			try { idProveedor = Integer.parseInt(provStr); 
			} catch (NumberFormatException e) { 
				System.out.println("ID inválido, se deja vacío."); 
				}
		}

		piezas_repuesto p = new piezas_repuesto();
		p.setNombre(nombre);
		p.setCodigo_proveedor(codProv);
		p.setCodigo_interno(codInt);
		p.setDescripcion(descripcion);
		p.setStock_actual(stockActual);
		p.setStock_minimo(stockMinimo);
		p.setStock_maximo(stockMaximo);
		p.setPrecio_unitario(precio);
		p.setUbicacion_almacen(ubicacion);
		p.setId_proveedor(idProveedor);

		boolean ok = insertar(p);
		System.out.println(ok ? "Pieza creada correctamente." : "No se pudo crear la pieza.");
		return ok;
	}


	/** Modificar un objeto existente
	 * @return false si fallo */
	@Override
	protected boolean Modificar() {
		System.out.print("\nID de la pieza a modificar: ");
		int id;
		try { id = Integer.parseInt(sc.nextLine()); }
		catch (NumberFormatException e) { System.out.println("ID inválido."); 
		return false; 
		}

		piezas_repuesto p = buscarPorId(id);
		if (p == null) { System.out.println("No existe una pieza con id " + id); 
		return false; 
		}

		System.out.println("Pieza actual: " + p);
		System.out.println("(Deja vacío un campo para mantener el valor actual)");

		System.out.print("Nombre [" + p.getNombre() + "]: ");
		String v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setNombre(v);

		System.out.print("Descripción [" + p.getDescripcion() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setDescripcion(v);

		System.out.print("Stock actual [" + p.getStock_actual() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) {
			try { p.setStock_actual(Integer.parseInt(v)); }
			catch (NumberFormatException e) { 
				System.out.println("Valor ignorado."); 
				}
		}

		System.out.print("Stock mínimo [" + p.getStock_minimo() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) {
			try { p.setStock_minimo(Integer.parseInt(v)); }
			catch (NumberFormatException e) { 
				System.out.println("Valor ignorado."); 
				}
		}

		System.out.print("Stock máximo [" + p.getStock_maximo() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) {
			try { p.setStock_maximo(Integer.parseInt(v)); }
			catch (NumberFormatException e) { 
				System.out.println("Valor ignorado."); 
				}
		}

		System.out.print("Precio [" + p.getPrecio_unitario() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) {
			try { p.setPrecio_unitario(Float.parseFloat(v)); }
			catch (NumberFormatException e) { 
				System.out.println("Valor ignorado."); 
				}
		}

		System.out.print("Ubicación almacén [" + p.getUbicacion_almacen() + "]: ");
		v = sc.nextLine().trim();
		if (!v.isEmpty()) p.setUbicacion_almacen(v);

		boolean ok = actualizar(p);
		System.out.println(ok ? "Pieza modificada correctamente." : "No se pudo modificar.");
		return ok;
	}


	/** Borrar un objeto
	 * @return false si fallo */
	@Override
	protected boolean Borrar() {
		System.out.print("\nID de la pieza a borrar: ");
		int id;
		try { id = Integer.parseInt(sc.nextLine()); }
		catch (NumberFormatException e) { 
			System.out.println("ID inválido."); 
			return false; 
		}

		System.out.print("¿Confirmas borrar la pieza " + id + "? (s/n): ");
		if (!sc.nextLine().trim().equalsIgnoreCase("s")) {
			System.out.println("Cancelado.");
			return false;
		}

		int resultado = borrar(id);
		switch (resultado) {
			case 1:
				System.out.println("Pieza borrada correctamente.");
				return true;
			case 0:
				System.out.println("No se borró ninguna fila (¿existe ese id?).");
				return false;
			case -1:
				System.out.println("No se puede borrar: la pieza está usada en órdenes o pedidos.");
				return false;
			default:
				System.out.println("Error al borrar.");
				return false;
		}
	}

	// OPERACIONES DE BASE DE DATOS

	/** Devuelve una pieza por su id, o null si no existe */
	public piezas_repuesto buscarPorId(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return null;

		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM piezas_repuesto WHERE id_pieza=?")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return filaAObjeto(rs);
			}
		} catch (SQLException e) {
			System.out.println("Error buscarPorId: " + e.getMessage());
		} finally {
			try { con.close(); } catch (SQLException ignored) {
				
			}
		}
		return null;
	}

	/** Inserta una nueva pieza en la BD */
	public boolean insertar(piezas_repuesto p) {
		Connection con = conexion.Conectar();
		if (con == null) 
			return false;

		String sql = "INSERT INTO piezas_repuesto (codigo_proveedor, codigo_interno, nombre, descripcion, " +
		             "stock_actual, stock_minimo, stock_maximo, precio_unitario, ubicacion_almacen, id_proveedor) " +
		             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, p.getCodigo_proveedor());
			ps.setString(2, p.getCodigo_interno());
			ps.setString(3, p.getNombre());
			ps.setString(4, p.getDescripcion());
			ps.setInt(5, p.getStock_actual());
			ps.setInt(6, p.getStock_minimo());
			ps.setInt(7, p.getStock_maximo());
			ps.setFloat(8, p.getPrecio_unitario());
			ps.setString(9, p.getUbicacion_almacen());
			if (p.getId_proveedor() != null)
				ps.setInt(10, p.getId_proveedor());
			else                             
				ps.setNull(10, Types.INTEGER);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al insertar pieza: " + e.getMessage());
			return false;
		} finally {
			try { 
				con.close(); 
				} catch (SQLException ignored) {
					
				}
		}
	}

	/** Actualiza los campos de una pieza existente */
	public boolean actualizar(piezas_repuesto p) {
		Connection con = conexion.Conectar();
		if (con == null) 
			return false;

		String sql = "UPDATE piezas_repuesto SET nombre=?, descripcion=?, stock_actual=?, " +
		             "stock_minimo=?, stock_maximo=?, precio_unitario=?, ubicacion_almacen=? " +
		             "WHERE id_pieza=?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, p.getNombre());
			ps.setString(2, p.getDescripcion());
			ps.setInt(3, p.getStock_actual());
			ps.setInt(4, p.getStock_minimo());
			ps.setInt(5, p.getStock_maximo());
			ps.setFloat(6, p.getPrecio_unitario());
			ps.setString(7, p.getUbicacion_almacen());
			ps.setInt(8, p.getId_pieza());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al modificar: " + e.getMessage());
			return false;
		} finally {
			try { con.close(); 
			} catch (SQLException ignored) {
				
			}
		}
	}


	/** Borra una pieza de la BD.
	 * @return 1 si OK, 0 si no existía, -1 si hay foreign key */
	public int borrar(int id) {
		Connection con = conexion.Conectar();
		if (con == null) return 0;

		try (PreparedStatement ps = con.prepareStatement("DELETE FROM piezas_repuesto WHERE id_pieza=?")) {
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


	/** Convierte una fila del ResultSet en objeto piezas_repuesto */
	private piezas_repuesto filaAObjeto(ResultSet rs) throws SQLException {
		piezas_repuesto p = new piezas_repuesto();
		p.setId_pieza(rs.getInt("id_pieza"));
		p.setCodigo_proveedor(rs.getString("codigo_proveedor"));
		p.setCodigo_interno(rs.getString("codigo_interno"));
		p.setNombre(rs.getString("nombre"));
		p.setDescripcion(rs.getString("descripcion"));
		p.setStock_actual(rs.getInt("stock_actual"));
		p.setStock_minimo(rs.getInt("stock_minimo"));
		p.setStock_maximo(rs.getInt("stock_maximo"));
		p.setPrecio_unitario(rs.getFloat("precio_unitario"));
		p.setUbicacion_almacen(rs.getString("ubicacion_almacen"));
		int prov = rs.getInt("id_proveedor");
		p.setId_proveedor(rs.wasNull() ? null : prov);
		return p;
	}
}