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
	
	@JsonProperty(value="zona")
	private int zona;
	
	@JsonProperty(value="administrador")
	private int administrador;
	
	private ArrayList<Contrato> contratos;
	
	private ArrayList<Producto> productos;
	
	private ArrayList<Menu> menus;

	public Restaurante(String nombre, String tipoComida,String web, int zona, int administrador,ArrayList<Contrato> contratos, ArrayList<Producto> productos, ArrayList<Menu> menus) {
	
		this.nombre = nombre;
		this.tipoComida = tipoComida;
		this.web = web;
		this.zona = zona;
		this.administrador = administrador;
		this.productos = productos;
		this.menus = menus;
	}
	public ArrayList<Contrato> getContratos() {
		return contratos;
	}
	public void setContratos(ArrayList<Contrato> contratos) {
		this.contratos = contratos;
	}
	public Restaurante(@JsonProperty(value="nombre")String nombre, @JsonProperty(value="tipoComida")String tipoComida, @JsonProperty(value="web")String web,@JsonProperty(value="zona") int zona,@JsonProperty(value="administrador") int administrador) {
		
		this.nombre = nombre;
		this.tipoComida = tipoComida;
		this.web = web;
		this.zona=zona;
		this.administrador=administrador;
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

	public int getAdministrador() {
		return administrador;
	}

	public void setAdministrador(int administrador) {
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
