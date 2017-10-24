/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: VideoAndes
 * Autor: Juan Felipe García - jf.garcia268@uniandes.edu.co
 * -------------------------------------------------------------------
 */
package vos;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.*;


public class Zona {

	
	@JsonProperty(value="id")
	private int id;

	
	@JsonProperty(value="abierto")
	private boolean abierto;

	
	@JsonProperty(value="capacidad")
	private int capacidad;
	
	@JsonProperty(value="discapacitados")
	private boolean discapacitados;
	
	@JsonProperty(value="especialidad")
	private String especialidad;
	
	private ArrayList<Reserva> reservas;
	
	private ArrayList<String> condiciones;
	
	private ArrayList<Restaurante> restaurantes;
	

	

	

public Zona(@JsonProperty(value="id") int id,@JsonProperty(value="abierto") boolean abierto,@JsonProperty(value="capacidad") int capacidad, @JsonProperty(value="discapacitados")boolean discapacitados, @JsonProperty(value="especialidad")String especialidad) {
		
		this.id = id;
		this.abierto = abierto;
		this.capacidad = capacidad;
		this.discapacitados = discapacitados;
		this.especialidad = especialidad;
	}
	public Zona(int id, boolean abierto,int capacidad, boolean discapacitados,String especialidad, ArrayList<Reserva> reservas, ArrayList<String> condiciones, ArrayList<Restaurante> restaurantes) {
		
		this.id = id;
		this.abierto = abierto;
		this.capacidad = capacidad;
		this.discapacitados = discapacitados;
		this.especialidad = especialidad;
		this.reservas = reservas;
		this.condiciones = condiciones;
		this.restaurantes = restaurantes;
	}

	public int getId() {
		return id;
	}





	public void setId(int id) {
		this.id = id;
	}





	public boolean getAbierto() {
		return abierto;
	}





	public void setAbierto(boolean abierto) {
		this.abierto = abierto;
	}





	public int getCapacidad() {
		return capacidad;
	}





	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}





	public boolean getDiscapacitados() {
		return discapacitados;
	}





	public void setDiscapacitados(boolean discapacitados) {
		this.discapacitados = discapacitados;
	}





	public String getEspecialidad() {
		return especialidad;
	}





	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}





	public ArrayList<Reserva> getReservas() {
		return reservas;
	}





	public void setReservas(ArrayList<Reserva> reservas) {
		this.reservas = reservas;
	}





	public ArrayList<String> getCondiciones() {
		return condiciones;
	}





	public void setCondiciones(ArrayList<String> condiciones) {
		this.condiciones = condiciones;
	}





	public ArrayList<Restaurante> getRestaurantes() {
		return restaurantes;
	}





	public void setRestaurantes(ArrayList<Restaurante> restaurantes) {
		this.restaurantes = restaurantes;
	}

	
}
