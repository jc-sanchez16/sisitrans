
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
	

	private Menu menu;

	public Reserva(@JsonProperty(value="invitados")int invitados, @JsonProperty(value="fecha")Date fecha) {
		super();
		this.invitados = invitados;
		this.fecha = fecha;
	}
	public Reserva(int invitados, Date fecha, int zona, int usuario, Menu menu) {
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

	public long getFecha() {
		return fecha.getTime();
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

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	


	
	
	
	
	
}
