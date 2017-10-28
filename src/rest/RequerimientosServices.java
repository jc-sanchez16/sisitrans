package rest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.annotate.JsonProperty;

import tm.TMIngrediente;
import tm.TMRequerimientos;
import tm.TMUsuario;
import vos.Ingrediente;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/VideoAndes/rest/videos/...
 */
@Path("RF")

public class RequerimientosServices {

	/**
	 * Atributo que usa la anotacion @Context para tener el ServletContext de la conexion actual.
	 */
	@Context
	private ServletContext context;

	/**
	 * Metodo que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}


	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}

	/**
	 * Metodo que expone servicio REST usando GET que da todos los videos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos
	 * @return Json con todos los videos de la base de datos o json con 
	 * el error que se produjo
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_JSON)
	@Path( "11")
	public Response AddEquivalenciaIngrediente(@QueryParam("clave") int clave, ArrayList<String> ingredientes) {
		TMRequerimientos tm = new TMRequerimientos(getPath());
		String res = "No se realizo la accion";
		try {
			res = tm.addEquivalenciaIngrediente(clave, ingredientes);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_JSON)
	@Path( "12")
	public Response AddEquivalenciaProducto(@QueryParam("clave") int clave,@QueryParam("restaurante") String restaurante, ArrayList<String> productos) {
		TMRequerimientos tm = new TMRequerimientos(getPath());
		String res = "No se realizo la accion";
		try {
			res = tm.addEquivalenciaProducto(clave, restaurante, productos);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("13")
	public Response surtirRestaurante(@QueryParam("clave") int clave,@QueryParam("restaurante") String restaurante) {
		TMRequerimientos tm = new TMRequerimientos(getPath());
		String res = "No se realizo la accion";
		try {
			res = tm.surtirRestaurante(clave, restaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	private static class Respuesta{
		@JsonProperty(value="productos")
		public ArrayList<String> productos;
		@JsonProperty(value="usuarios")
		public ArrayList<Integer> usuarios;
		public Respuesta(@JsonProperty(value="productos")ArrayList<String> productos,@JsonProperty(value="usuarios") ArrayList<Integer> usuarios) {
			this.productos = productos;
			this.usuarios = usuarios;
		}
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("15")
	public Response registrarPedidoOrden(@QueryParam("mesa") int mesa, Respuesta res) {
		TMRequerimientos tm = new TMRequerimientos(getPath());
		try {
			tm.registrarPedidoOrden(mesa,new Date(),res.productos,res.usuarios);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("16")
	public Response registrarServicio(@QueryParam("clave") int clave,@QueryParam("restaurante") String restaurante, @QueryParam("fecha") Date fecha,@QueryParam("mesa") int mesa) {
		TMRequerimientos tm = new TMRequerimientos(getPath());
		String res = "No se realizo la accion";
		try {
			res = tm.registrarServicio(clave, restaurante, fecha, mesa);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("17")
	public Response cancelarServicio(@QueryParam("clave") int clave,@QueryParam("restaurante") String restaurante, @QueryParam("fecha") Date fecha,@QueryParam("mesa") int mesa) {
		TMRequerimientos tm = new TMRequerimientos(getPath());
		String res = "No se realizo la accion";
		try {
			res = tm.cancelarServicio(clave, restaurante, fecha, mesa);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("C7")
	public Response consultarConsumo(@QueryParam("clave") int clave,@QueryParam("peticion") int peticion, @QueryParam("usuario") int usuario) {
		TMRequerimientos tm = new TMRequerimientos(getPath());
		int res = null;
		try {
			res = tm.consultarConsumo(usuario,clave, peticion);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
}
