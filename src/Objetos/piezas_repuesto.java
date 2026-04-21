package Objetos;

/**
 * @author Alexandru
 */
public class piezas_repuesto {

	// Propiedades (columnas de la tabla)

	private int        id_pieza;
	private String     codigo_proveedor;
	private String     codigo_interno;
	private String     nombre;
	private String     descripcion;
	private int        stock_actual;
	private int        stock_minimo;
	private int        stock_maximo;
	private float precio_unitario;
	private String     ubicacion_almacen;
	private Integer    id_proveedor;     // Integer para aceptar NULL


	// Constructores

	public piezas_repuesto() {

	}

	public piezas_repuesto(int id_pieza, String codigo_proveedor, String codigo_interno, String nombre,
			String descripcion, int stock_actual, int stock_minimo, int stock_maximo, float precio_unitario,
			String ubicacion_almacen, Integer id_proveedor) {
		super();
		this.id_pieza = id_pieza;
		this.codigo_proveedor = codigo_proveedor;
		this.codigo_interno = codigo_interno;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.stock_actual = stock_actual;
		this.stock_minimo = stock_minimo;
		this.stock_maximo = stock_maximo;
		this.precio_unitario = precio_unitario;
		this.ubicacion_almacen = ubicacion_almacen;
		this.id_proveedor = id_proveedor;
	}

	// Getters y Setters

	public int getId_pieza() {
		return id_pieza;
	}

	public void setId_pieza(int id_pieza) {
		this.id_pieza = id_pieza;
	}

	public String getCodigo_proveedor() {
		return codigo_proveedor;
	}

	public void setCodigo_proveedor(String codigo_proveedor) {
		this.codigo_proveedor = codigo_proveedor;
	}

	public String getCodigo_interno() {
		return codigo_interno;
	}

	public void setCodigo_interno(String codigo_interno) {
		this.codigo_interno = codigo_interno;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getStock_actual() {
		return stock_actual;
	}

	public void setStock_actual(int stock_actual) {
		this.stock_actual = stock_actual;
	}

	public int getStock_minimo() {
		return stock_minimo;
	}

	public void setStock_minimo(int stock_minimo) {
		this.stock_minimo = stock_minimo;
	}

	public int getStock_maximo() {
		return stock_maximo;
	}

	public void setStock_maximo(int stock_maximo) {
		this.stock_maximo = stock_maximo;
	}

	public float getPrecio_unitario() {
		return precio_unitario;
	}

	public void setPrecio_unitario(float precio_unitario) {
		this.precio_unitario = precio_unitario;
	}

	public String getUbicacion_almacen() {
		return ubicacion_almacen;
	}

	public void setUbicacion_almacen(String ubicacion_almacen) {
		this.ubicacion_almacen = ubicacion_almacen;
	}

	public Integer getId_proveedor() {
		return id_proveedor;
	}

	public void setId_proveedor(Integer id_proveedor) {
		this.id_proveedor = id_proveedor;
	}

	@Override
	public String toString() {
		return "piezas_repuesto [id_pieza=" + id_pieza + ", codigo_proveedor=" + codigo_proveedor + ", codigo_interno="
				+ codigo_interno + ", nombre=" + nombre + ", descripcion=" + descripcion + ", stock_actual="
				+ stock_actual + ", stock_minimo=" + stock_minimo + ", stock_maximo=" + stock_maximo
				+ ", precio_unitario=" + precio_unitario + ", ubicacion_almacen=" + ubicacion_almacen
				+ ", id_proveedor=" + id_proveedor + "]";
	}

}
