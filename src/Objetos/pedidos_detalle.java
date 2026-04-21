package Objetos;

public class pedidos_detalle {
	private int idDetalle;
    private int idPedido;
    private int idPieza;
    private int cantidadSolicitada;
    private int cantidadRecibida;
    private double precioUnitario;

    public double getSubtotal() {
        return this.cantidadSolicitada * this.precioUnitario;
    }

	public pedidos_detalle(int idDetalle, int idPedido, int idPieza, int cantidadSolicitada, int cantidadRecibida,
			double precioUnitario) {
		super();
		this.idDetalle = idDetalle;
		this.idPedido = idPedido;
		this.idPieza = idPieza;
		this.cantidadSolicitada = cantidadSolicitada;
		this.cantidadRecibida = cantidadRecibida;
		this.precioUnitario = precioUnitario;
	}

	public int getIdDetalle() {
		return idDetalle;
	}

	public void setIdDetalle(int idDetalle) {
		this.idDetalle = idDetalle;
	}

	public int getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(int idPedido) {
		this.idPedido = idPedido;
	}

	public int getIdPieza() {
		return idPieza;
	}

	public void setIdPieza(int idPieza) {
		this.idPieza = idPieza;
	}

	public int getCantidadSolicitada() {
		return cantidadSolicitada;
	}

	public void setCantidadSolicitada(int cantidadSolicitada) {
		this.cantidadSolicitada = cantidadSolicitada;
	}

	public int getCantidadRecibida() {
		return cantidadRecibida;
	}

	public void setCantidadRecibida(int cantidadRecibida) {
		this.cantidadRecibida = cantidadRecibida;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	@Override
	public String toString() {
		return "pedidos_detalle [idDetalle=" + idDetalle + ", idPedido=" + idPedido + ", idPieza=" + idPieza
				+ ", cantidadSolicitada=" + cantidadSolicitada + ", cantidadRecibida=" + cantidadRecibida
				+ ", precioUnitario=" + precioUnitario + "]";
	}
}
