package rest;

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

import tm.TMOrden;
import tm.TMReserva;
import vos.Menu;
import vos.Orden;
import vos.Reserva;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o mesa de host":8080/VideoAndes/rest/videos/...
 */
@Path("ordenes")
public class OrdenServices {
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
	 * <b>URL: </b> http://"ip o mesa de host":8080/VideoAndes/rest/videos
	 * @return Json con todos los videos de la base de datos o json con 
	 * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getOrdenes() {
		TMOrden tm = new TMOrden(getPath());
		List<Orden> ordenes;
		try {
			ordenes = tm.getOrdenes();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(ordenes).build();
	}

	/**
	 * Metodo que expone servicio REST usando GET que busca el video con el id que entra como parametro
	 * <b>URL: </b> http://"ip o mesa de host":8080/VideoAndes/rest/videos/<<id>>" para la busqueda"
	 * @param name - Nombre del video a buscar que entra en la URL como parametro 
	 * @return Json con el/los videos encontrados con el mesa que entra como parametro o json con 
	 * el error que se produjo
	 */
	@GET
	@Path("PK")
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getOrdenPK(  @QueryParam("mesa") int mesa, @QueryParam("fecha") String fecha)
	{
		TMOrden tm = new TMOrden( getPath( ) );
		try
		{			
			Orden orden = tm.getOrdenPK(mesa, new Date(fecha));
			return Response.status( 200 ).entity( orden ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

}
