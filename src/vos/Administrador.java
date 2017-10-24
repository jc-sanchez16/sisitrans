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


public class Administrador extends Usuario 
{	
	@JsonProperty(value="clave")
	private String clave;

	public Administrador(@JsonProperty(value="id")int id, @JsonProperty(value="nombre")String nombre, @JsonProperty(value="edad")int edad, @JsonProperty(value="clave")String clave) {
		super(id, nombre, edad);
		this.clave = clave;
	}
	public Administrador(int id, String nombre, int edad, ArrayList<Orden> ordenes, ArrayList<String> preferencias,
			ArrayList<Reserva> reservas,String clave) {
		super(id, nombre, edad, ordenes, preferencias, reservas);
		this.clave = clave;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}	
}
