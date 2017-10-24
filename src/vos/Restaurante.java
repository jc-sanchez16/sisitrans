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


public class Restaurante {

	
	@JsonProperty(value="nombre")
	private String nombre;

	
	@JsonProperty(value="tipoComida")
	private String tipoComida;

	
	@JsonProperty(value="web")
	private String web;
	
	
	private int zona;
	
	private Administrador administrador;
	
	private ArrayList<Producto> productos;
	
	private ArrayList<Menu> menus;

	public Restaurante(String nombre, String tipoComida,String web, int zona, Administrador administrador,ArrayList<Producto> productos, ArrayList<Menu> menus) {
	
		this.nombre = nombre;
		this.tipoComida = tipoComida;
		this.web = web;
		this.zona = zona;
		this.administrador = administrador;
		this.productos = productos;
		this.menus = menus;
	}
	public Restaurante(@JsonProperty(value="nombre")String nombre, @JsonProperty(value="tipoComida")String tipoComida, @JsonProperty(value="web")String web) {
		
		this.nombre = nombre;
		this.tipoComida = tipoComida;
		this.web = web;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipoComida() {
		return tipoComida;
	}

	public void setTipoComida(String tipoComida) {
		this.tipoComida = tipoComida;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public int getZona() {
		return zona;
	}

	public void setZona(int zona) {
		this.zona = zona;
	}

	public Administrador getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Administrador administrador) {
		this.administrador = administrador;
	}

	public ArrayList<Producto> getProductos() {
		return productos;
	}

	public void setProductos(ArrayList<Producto> productos) {
		this.productos = productos;
	}

	public ArrayList<Menu> getMenus() {
		return menus;
	}

	public void setMenus(ArrayList<Menu> menus) {
		this.menus = menus;
	}
	

	
	
	
	

	
}
