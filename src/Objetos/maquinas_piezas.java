package Objetos;

import Comun.interfaces;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

import DAO.maquinas_piezasDAO;

/**
 * @author Cristian
 */
public class maquinas_piezas {
	private int idMaquina;
    private int idPieza;
    
	public maquinas_piezas(int idMaquina, int idPieza) {
		super();
		this.idMaquina = idMaquina;
		this.idPieza = idPieza;
	}

	public int getIdMaquina() {
		return idMaquina;
	}

	public void setIdMaquina(int idMaquina) {
		this.idMaquina = idMaquina;
	}

	public int getIdPieza() {
		return idPieza;
	}

	public void setIdPieza(int idPieza) {
		this.idPieza = idPieza;
	}

	@Override
	public String toString() {
		return "maquinas_piezas [idMaquina=" + idMaquina + ", idPieza=" + idPieza + "]";
	}
}