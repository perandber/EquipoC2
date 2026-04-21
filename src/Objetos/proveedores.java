package Objetos;

/**
 * @author Alexandru
 */
public class proveedores {

	// Propiedades (columnas de la tabla)

	private int    id_proveedor;
	private String nombre;
	private String contacto;
	private String telefono;
	private String email;
	private String direccion;


	// Constructores

	public proveedores() {

	}


	public proveedores(int id_proveedor, String nombre, String contacto, String telefono, String email,
			String direccion) {
		super();
		this.id_proveedor = id_proveedor;
		this.nombre = nombre;
		this.contacto = contacto;
		this.telefono = telefono;
		this.email = email;
		this.direccion = direccion;
	}

	// Getters y Setters

	public int getId_proveedor() {
		return id_proveedor;
	}


	public void setId_proveedor(int id_proveedor) {
		this.id_proveedor = id_proveedor;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getContacto() {
		return contacto;
	}


	public void setContacto(String contacto) {
		this.contacto = contacto;
	}


	public String getTelefono() {
		return telefono;
	}


	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	@Override
	public String toString() {
		return "proveedores [id_proveedor=" + id_proveedor + ", nombre=" + nombre + ", contacto=" + contacto
				+ ", telefono=" + telefono + ", email=" + email + ", direccion=" + direccion + "]";
	}

}