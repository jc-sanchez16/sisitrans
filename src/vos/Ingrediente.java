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
	private int nombre;
	
	@JsonProperty(value="descripcionE")
	private String descripcionE;
	
	@JsonProperty(value="descripcionEn")
	private String descripcionEn;
	
	private ArrayList<Producto> equivalencias;

	public Ingrediente(@JsonProperty(value="nombre")int nombre, @JsonProperty(value="descripcionE")String descripcionE, @JsonProperty(value="descripcionEn")String descripcionEn, ArrayList<Producto> equivalencias) {
		this.nombre = nombre;
		this.descripcionE = descripcionE;
		this.descripcionEn = descripcionEn;
		this.equivalencias = equivalencias;
	}

	public int getNombre() {
		return nombre;
	}

	public void setNombre(int nombre) {
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

	public ArrayList<Producto> getEquivalencias() {
		return equivalencias;
	}

	public void setEquivalencias(ArrayList<Producto> equivalencias) {
		this.equivalencias = equivalencias;
	}	
}
