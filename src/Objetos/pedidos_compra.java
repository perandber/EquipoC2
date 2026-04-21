package Objetos;

import java.util.Date;

public class pedidos_compra {
	private int idPedido;
    private Date fechaPedido;
    private int idProveedor;
    private int idEstado;
    private Date fechaEntregaEsperada;
    private Date fechaEntregaReal;
    private String observaciones;
    
	public pedidos_compra(int idPedido, Date fechaPedido, int idProveedor, int idEstado, Date fechaEntregaEsperada,
			Date fechaEntregaReal, String observaciones) {
		super();
		this.idPedido = idPedido;
		this.fechaPedido = fechaPedido;
		this.idProveedor = idProveedor;
		this.idEstado = idEstado;
		this.fechaEntregaEsperada = fechaEntregaEsperada;
		this.fechaEntregaReal = fechaEntregaReal;
		this.observaciones = observaciones;
	}

	public int getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(int idPedido) {
		this.idPedido = idPedido;
	}

	public Date getFechaPedido() {
		return fechaPedido;
	}

	public void setFechaPedido(Date fechaPedido) {
		this.fechaPedido = fechaPedido;
	}

	public int getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(int idProveedor) {
		this.idProveedor = idProveedor;
	}

	public int getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(int idEstado) {
		this.idEstado = idEstado;
	}

	public Date getFechaEntregaEsperada() {
		return fechaEntregaEsperada;
	}

	public void setFechaEntregaEsperada(Date fechaEntregaEsperada) {
		this.fechaEntregaEsperada = fechaEntregaEsperada;
	}

	public Date getFechaEntregaReal() {
		return fechaEntregaReal;
	}

	public void setFechaEntregaReal(Date fechaEntregaReal) {
		this.fechaEntregaReal = fechaEntregaReal;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Override
	public String toString() {
		return "pedidos_compra [idPedido=" + idPedido + ", fechaPedido=" + fechaPedido + ", idProveedor=" + idProveedor
				+ ", idEstado=" + idEstado + ", fechaEntregaEsperada=" + fechaEntregaEsperada + ", fechaEntregaReal="
				+ fechaEntregaReal + ", observaciones=" + observaciones + "]";
	}
}
