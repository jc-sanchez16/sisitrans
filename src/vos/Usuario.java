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


public class Usuario {

	
	@JsonProperty(value="id")
	private int id;

	
	@JsonProperty(value="nombre")
	private String nombre;

	
	@JsonProperty(value="edad")
	private int edad;
	
	
	private ArrayList<Orden> ordenes;
	
	private ArrayList<String> preferencias;
	
	private ArrayList<Reserva> reservas;

	public Usuario(@JsonProperty(value="id")int id, @JsonProperty(value="nombre")String nombre, @JsonProperty(value="edad")int edad) {
	
		this.id = id;
		this.nombre = nombre;
		this.edad = edad;
	}

	public Usuario(int id, String nombre,int edad, ArrayList<Orden> ordenes, ArrayList<String> preferencias,
			ArrayList<Reserva> reservas) {
	
		this.id = id;
		this.nombre = nombre;
		this.edad = edad;
		this.ordenes = ordenes;
		this.preferencias = preferencias;
		this.reservas = reservas;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public ArrayList<Orden> getOrdenes() {
		return ordenes;
	}

	public void setOrdenes(ArrayList<Orden> ordenes) {
		this.ordenes = ordenes;
	}

	public ArrayList<String> getPreferencias() {
		return preferencias;
	}

	public void setPreferencias(ArrayList<String> preferencias) {
		this.preferencias = preferencias;
	}

	public ArrayList<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(ArrayList<Reserva> reservas) {
		this.reservas = reservas;
	}
	

	
	
	
	
}
