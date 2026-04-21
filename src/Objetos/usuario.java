package Objetos;

import java.util.Date;

/**
 * @author Alexandru
 */
public class usuario {

	// Propiedades (columnas de la tabla)

	private int       id_usuario;
	private String    nombre;
	private String    apellidos;
	private String    email;
	private String    password_hash;   // NUNCA se muestra
	private String    rol;             // 'tecnico' o 'responsable'
	private boolean   activo;
	private Date fecha_creacion;
	private Date ultimo_acceso;


	// Constructores

	public usuario() {

	}

	public usuario(int id_usuario, String nombre, String apellidos, String email, String password_hash, String rol,
			boolean activo, Date fecha_creacion, Date ultimo_acceso) {
		super();
		this.id_usuario = id_usuario;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.password_hash = password_hash;
		this.rol = rol;
		this.activo = activo;
		this.fecha_creacion = fecha_creacion;
		this.ultimo_acceso = ultimo_acceso;
	}


	// Getters y Setters

	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword_hash() {
		return password_hash;
	}

	public void setPassword_hash(String password_hash) {
		this.password_hash = password_hash;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Date getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(Date fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	public Date getUltimo_acceso() {
		return ultimo_acceso;
	}

	public void setUltimo_acceso(Date ultimo_acceso) {
		this.ultimo_acceso = ultimo_acceso;
	}

	@Override
	public String toString() {
		return "usuario [id_usuario=" + id_usuario + ", nombre=" + nombre + ", apellidos=" + apellidos + ", email="
				+ email + ", password_hash=" + password_hash + ", rol=" + rol + ", activo=" + activo
				+ ", fecha_creacion=" + fecha_creacion + ", ultimo_acceso=" + ultimo_acceso + "]";
	}


}