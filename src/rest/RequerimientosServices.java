package rest;

import java.util.ArrayList;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.xml.internal.ws.api.message.Message;

import tm.TMRequerimientos;
import tm.TMUsuario;

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
	@Path( "11/{clave: \\d+}")
	public Response AddEquivalenciaIngrediente(@PathParam("clave") int clave, ArrayList<String> ingredientes) {
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
	@Path( "12/{restaurante:[a-zA-Z]+}/{clave: \\d+}")
	public Response AddEquivalenciaProducto(@PathParam("clave") int clave,@PathParam("restaurante") String restaurante, ArrayList<String> productos) {
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
	@Path("13/{restaurante:[a-zA-Z]+}/{clave: \\\\d+}")
	public Response surtirRestaurante(@PathParam("clave") int clave,@PathParam("restaurante") String restaurante) {
		TMRequerimientos tm = new TMRequerimientos(getPath());
		String res = "No se realizo la accion";
		try {
			res = tm.surtirRestaurante(clave, restaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	
	
}
