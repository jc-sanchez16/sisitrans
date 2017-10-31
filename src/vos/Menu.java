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


public class Menu implements Articulo{

	
	@JsonProperty(value="nombre")
	private String nombre;

	
	@JsonProperty(value="restaurante")
	private String restaurante;

	
	@JsonProperty(value="costo")
	private double costo;
	
	@JsonProperty(value="precio")
	private double precio;

	@JsonProperty(value="tiempoPreparacion")
	private int tiempoPreparacion;
	
	@JsonProperty(value="cantidadMaxima")
	private int cantidadMaxima;
	
	private ArrayList<String> tipoComida;
	
	private ArrayList<Producto> productos;
	
	private String cambios;

	public Menu(@JsonProperty(value="nombre")String nombre,@JsonProperty(value="restaurante") String restaurante, @JsonProperty(value="costo")double costo, @JsonProperty(value="precio")double precio, @JsonProperty(value="tiempoPreparacion")int tiempoPreparacion,@JsonProperty(value="cantidadMaxima")int cantidadMaxima) {
		this.nombre = nombre;
		this.restaurante = restaurante;
		this.costo = costo;
		this.precio = precio;
		this.tiempoPreparacion = tiempoPreparacion;
		this.cantidadMaxima = cantidadMaxima;
	}
	public Menu(String nombre,String restaurante, double costo, double precio, int tiempoPreparacion,
			ArrayList<String> tipoComida, ArrayList<Producto> productos,int cantidadMaxima) {
		this.nombre = nombre;
		this.restaurante = restaurante;
		this.costo = costo;
		this.precio = precio;
		this.tiempoPreparacion = tiempoPreparacion;
		this.tipoComida = tipoComida;
		this.productos = productos;
		this.cantidadMaxima=cantidadMaxima;
	}

	public String getCambios() {
		return cambios;
	}
	public void setCambios(String cambios) {
		this.cambios = cambios;
	}
	public int getCantidadMaxima() {
		return cantidadMaxima;
	}
	public void setCantidadMaxima(int cantidadMaxima) {
		this.cantidadMaxima = cantidadMaxima;
	}
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(String restaurante) {
		this.restaurante = restaurante;
	}

	public double getCosto() {
		return costo;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getTiempoPreparacion() {
		return tiempoPreparacion;
	}

	public void setTiempoPreparacion(int tiempoPreparacion) {
		this.tiempoPreparacion = tiempoPreparacion;
	}

	public ArrayList<String> getTipoComida() {
		return tipoComida;
	}

	public void setTipoComida(ArrayList<String> tipoComida) {
		this.tipoComida = tipoComida;
	}

	public ArrayList<Producto> getProductos() {
		return productos;
	}

	public void setProductos(ArrayList<Producto> productos) {
		this.productos = productos;
	}
}
