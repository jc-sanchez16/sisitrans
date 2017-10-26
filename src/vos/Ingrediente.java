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


public class Ingrediente {

	
	@JsonProperty(value="nombre")
	private String nombre;
	
	@JsonProperty(value="descripcionE")
	private String descripcionE;
	
	@JsonProperty(value="descripcionEn")
	private String descripcionEn;
	
	private ArrayList<String> equivalencias;

	public Ingrediente(@JsonProperty(value="nombre")String nombre, @JsonProperty(value="descripcionE")String descripcionE, @JsonProperty(value="descripcionEn")String descripcionEn) {
		this.nombre = nombre;
		this.descripcionE = descripcionE;
		this.descripcionEn = descripcionEn;
	}
	public Ingrediente(String nombre,String descripcionE, String descripcionEn, ArrayList<String> equivalencias) {
		this.nombre = nombre;
		this.descripcionE = descripcionE;
		this.descripcionEn = descripcionEn;
		this.equivalencias = equivalencias;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcionE() {
		return descripcionE;
	}

	public void setDescripcionE(String descripcionE) {
		this.descripcionE = descripcionE;
	}

	public String getDescripcionEn() {
		return descripcionEn;
	}

	public void setDescripcionEn(String descripcionEn) {
		this.descripcionEn = descripcionEn;
	}

	public ArrayList<String> getEquivalencias() {
		return equivalencias;
	}

	public void setEquivalencias(ArrayList<String> equivalencias) {
		this.equivalencias = equivalencias;
	}	
}
