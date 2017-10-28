package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Contrato {
	
	@JsonProperty(value="id")
	private int id;
	
	@JsonProperty(value="descripcion")
	private String descripcion;
	
	@JsonProperty(value="restaurante")
	private String restaurante;
	
	public Contrato(@JsonProperty(value="id") int id, @JsonProperty(value="descripcion") String descripcion, @JsonProperty(value="restaurante")String restaurante)
	{
		this.id = id;
		this.descripcion = descripcion;
		this.restaurante = restaurante;
	}

	public String getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(String restaurante) {
		this.restaurante = restaurante;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
