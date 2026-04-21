package Objetos;

import java.util.Date;

/**
 * @author Alexandru
 */
public class maquina {

	// Propiedades (columnas de la tabla)
	private int       id_maquina;
	private String    nombre;
	private String    tipo;
	private String    numero_serie;
	private String    ubicacion;
	private String    fabricante;
	private Date      fecha_compra;
	private String    estado;        
	private String    qr_code;
	private Integer   proveedor_id;  
	private boolean   activa;
	private Date fecha_registro;


	// Constructores
	public maquina() {
		
	}

	public maquina(int id_maquina, String nombre, String tipo, String numero_serie, String ubicacion, String fabricante,
			Date fecha_compra, String estado, String qr_code, Integer proveedor_id, boolean activa,
			Date fecha_registro) {
		super();
		this.id_maquina = id_maquina;
		this.nombre = nombre;
		this.tipo = tipo;
		this.numero_serie = numero_serie;
		this.ubicacion = ubicacion;
		this.fabricante = fabricante;
		this.fecha_compra = fecha_compra;
		this.estado = estado;
		this.qr_code = qr_code;
		this.proveedor_id = proveedor_id;
		this.activa = activa;
		this.fecha_registro = fecha_registro;
	}

	// Getters y Setters
	
	public int getId_maquina() {
		return id_maquina;
	}

	public void setId_maquina(int id_maquina) {
		this.id_maquina = id_maquina;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNumero_serie() {
		return numero_serie;
	}

	public void setNumero_serie(String numero_serie) {
		this.numero_serie = numero_serie;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public Date getFecha_compra() {
		return fecha_compra;
	}

	public void setFecha_compra(Date fecha_compra) {
		this.fecha_compra = fecha_compra;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getQr_code() {
		return qr_code;
	}

	public void setQr_code(String qr_code) {
		this.qr_code = qr_code;
	}

	public Integer getProveedor_id() {
		return proveedor_id;
	}

	public void setProveedor_id(Integer proveedor_id) {
		this.proveedor_id = proveedor_id;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	public Date getFecha_registro() {
		return fecha_registro;
	}

	public void setFecha_registro(Date fecha_registro) {
		this.fecha_registro = fecha_registro;
	}
	
	@Override
	public String toString() {
		return "maquina [id_maquina=" + id_maquina + ", nombre=" + nombre + ", tipo=" + tipo + ", numero_serie="
				+ numero_serie + ", ubicacion=" + ubicacion + ", fabricante=" + fabricante + ", fecha_compra="
				+ fecha_compra + ", estado=" + estado + ", qr_code=" + qr_code + ", proveedor_id=" + proveedor_id
				+ ", activa=" + activa + ", fecha_registro=" + fecha_registro + "]";
	}
	
}