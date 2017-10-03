
package vos;

import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.annotate.*;


public class Reserva {

	
	@JsonProperty(value="invitados")
	private int invitados;

	
	@JsonProperty(value="fecha")
	private Date fecha;

	
	private int zona;
	
	private int usuario;
	
	private String menu;

	public Reserva(int invitados, Date fecha, int zona, int usuario, String menu) {
		super();
		this.invitados = invitados;
		this.fecha = fecha;
		this.zona = zona;
		this.usuario = usuario;
		this.menu = menu;
	}

	public int getInvitados() {
		return invitados;
	}

	public void setInvitados(int invitados) {
		this.invitados = invitados;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getZona() {
		return zona;
	}

	public void setZona(int zona) {
		this.zona = zona;
	}

	public int getUsuario() {
		return usuario;
	}

	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}
	


	
	
	
	
	
}
