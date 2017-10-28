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


public class Producto {

	
	@JsonProperty(value="nombre")
	private String nombre;
	
	@JsonProperty(value="restaurante")
	private String restaurante;
	
	@JsonProperty(value="costo")
	private double costo;
	
	@JsonProperty(value="tipo")
	private int tipo;
	
	@JsonProperty(value="descripcionE")
	private String descripcionE;
	
	@JsonProperty(value="descripcionEn")
	private String descripcionEn;

	@JsonProperty(value="tiempoPreparacion")
	private int tiempoPreparacion;
	
	@JsonProperty(value="precio")
	private double precio;
	
	@JsonProperty(value="cantidadMaxima")
	private int cantidadMaxima;
	
	private ArrayList<String> tipoComida;
	
	private ArrayList<Producto> equivalencias;

	private ArrayList<Ingrediente> ingredientes;
	

	private String cambios;

	public Producto(@JsonProperty(value="nombre") String nombre, @JsonProperty(value="restaurante")String restaurante,@JsonProperty(value="costo") double costo,@JsonProperty(value="tipo") int tipo, @JsonProperty(value="descripcionE")String descripcionE, @JsonProperty(value="descripcionEn")String descripcionEn,
			@JsonProperty(value="tiempoPreparacion")int tiempoPreparacion, @JsonProperty(value="precio")double precio, @JsonProperty(value="cantidadMaxima") int cantidadMaxima) {
		this.nombre = nombre;
		this.restaurante = restaurante;
		this.costo = costo;
		this.tipo = tipo;
		this.descripcionE = descripcionE;
		this.descripcionEn = descripcionEn;
		this.tiempoPreparacion = tiempoPreparacion;
		this.precio = precio;
		this.cantidadMaxima = cantidadMaxima;
	}
	public Producto( String nombre, String restaurante, double costo, int tipo,String descripcionE,String descripcionEn,
	int tiempoPreparacion,double precio, ArrayList<String> tipoComida, ArrayList<Producto> equivalencias,
	ArrayList<Ingrediente> ingredientes, int cantidadMaxima, String cambios) {
		this.nombre = nombre;
		this.restaurante = restaurante;
		this.costo = costo;
		this.tipo = tipo;
		this.descripcionE = descripcionE;
		this.descripcionEn = descripcionEn;
		this.tiempoPreparacion = tiempoPreparacion;
		this.precio = precio;
		this.tipoComida = tipoComida;
		this.equivalencias = equivalencias;
		this.ingredientes = ingredientes;
		this.cantidadMaxima = cantidadMaxima;
		this.cambios = cambios;
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

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
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

	public int getTiempoPreparacion() {
		return tiempoPreparacion;
	}

	public void setTiempoPreparacion(int tiempoPreparacion) {
		this.tiempoPreparacion = tiempoPreparacion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public ArrayList<String> getTipoComida() {
		return tipoComida;
	}

	public void setTipoComida(ArrayList<String> tipoComida) {
		this.tipoComida = tipoComida;
	}

	public ArrayList<Producto> getEquivalencias() {
		return equivalencias;
	}

	public void setEquivalencias(ArrayList<Producto> equivalencias) {
		this.equivalencias = equivalencias;
	}

	public ArrayList<Ingrediente> getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(ArrayList<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}

	
	
}
