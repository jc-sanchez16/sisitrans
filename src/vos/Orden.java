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
import java.util.Date;

import org.codehaus.jackson.annotate.*;


public class Orden {

	
	@JsonProperty(value="mesa")
	private int mesa;

	
	@JsonProperty(value="fecha")
	private Date fecha;
	
	private ArrayList<Integer> usuarios;
	
	private ArrayList<Menu> menus;
	
	private ArrayList<Producto> productos;
	
	@JsonProperty(value="atendido")
	private Boolean atendido;

	public Orden(@JsonProperty(value="mesa")int mesa, @JsonProperty(value="fecha")Date fecha,@JsonProperty(value="atendido") Boolean atendido) {
		this.mesa = mesa;
		this.fecha = fecha;
		this.atendido = atendido;
	}
	public Orden(int mesa, Date fecha, ArrayList<Integer> usuarios, ArrayList<Menu> menus,
			ArrayList<Producto> productos,Boolean atendido) {
		this.mesa = mesa;
		this.fecha = fecha;
		this.usuarios = usuarios;
		this.menus = menus;
		this.productos = productos;
		this.atendido = atendido;
	}

	public Boolean getAtendido() {
		return atendido;
	}
	public void setAtendido(Boolean atendido) {
		this.atendido = atendido;
	}
	public int getMesa() {
		return mesa;
	}

	public void setMesa(int mesa) {
		this.mesa = mesa;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public ArrayList<Integer> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(ArrayList<Integer> usuarios) {
		this.usuarios = usuarios;
	}

	public ArrayList<Menu> getMenus() {
		return menus;
	}

	public void setMenus(ArrayList<Menu> menus) {
		this.menus = menus;
	}

	public ArrayList<Producto> getProductos() {
		return productos;
	}

	public void setProductos(ArrayList<Producto> productos) {
		this.productos = productos;
	}
	

	
}
