package rest;

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

import tm.TM;
import vos.Usuario;
import vos.Zona;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/VideoAndes/rest/videos/...
 */
@Path("usuarios")
public class UsuarioServices {

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



	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Metodo que expone servicio REST usando GET que da todos los videos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos
	 * @return Json con todos los videos de la base de datos o json con 
	 * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getUsuarios( @QueryParam("usuario") int usuario,@QueryParam("clave") int clave) {
		TM tm = new TM(getPath());
		List<Usuario> usuarios;
		try {
			usuarios = tm.getUsuarios(usuario,clave);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuarios).build();
	}

	/**
	 * Metodo que expone servicio REST usando GET que busca el video con el id que entra como parametro
	 * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/<<id>>" para la busqueda"
	 * @param name - Nombre del video a buscar que entra en la URL como parametro 
	 * @return Json con el/los videos encontrados con el nombre que entra como parametro o json con 
	 * el error que se produjo
	 */
	@GET
	@Path("/PK")
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getUsuarioPK(@QueryParam("clave") int clave)
	{
		TM tm = new TM( getPath( ) );
		try
		{
			Usuario user = tm.getUsuarioPK(clave);
			return Response.status( 200 ).entity( user ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}


	/**
	 * Metodo que expone servicio REST usando POST que agrega el video que recibe en Json
	 * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/video
	 * @param video - video a agregar
	 * @return Json con el video que agrego o Json con el error que se produjo
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUsuario(Usuario user,@QueryParam("usuario") int usuario,@QueryParam("claveNueva") int claveNueva,@QueryParam("clave") int clave) {
		TM tm = new TM(getPath());
		try {
			tm.addUsuario(user, claveNueva, usuario, clave);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(user).build();
	}


	/**
	 * Metodo que expone servicio REST usando PUT que actualiza el video que recibe en Json
	 * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos
	 * @param user - video a actualizar. 
	 * @return Json con el video que actualizo o Json con el error que se produjo
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUsuario(Usuario user, @QueryParam("usuario") int usuario ,@QueryParam("clave") int clave) {
		TM tm = new TM(getPath());
		try {
			tm.updateUsuario(user,usuario,clave);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(user).build();
	}

	/**
	 * Metodo que expone servicio REST usando DELETE que elimina el video que recibe en Json
	 * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos
	 * @param user - video a aliminar. 
	 * @return Json con el video que elimino o Json con el error que se produjo
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUsuario( @QueryParam( "id" ) int id,@QueryParam("usuario") int usuario ,@QueryParam("clave") int clave ) {
		TM tm = new TM(getPath());
		try {
			tm.deleteUsuario(id, usuario, clave);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(id).build();
	}
	/**
	 * Metodo que expone servicio REST usando GET que da todos los videos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos
	 * @return Json con todos los videos de la base de datos o json con 
	 * el error que se produjo
	 */
	@GET
	@Path("admin")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAdministradores( @QueryParam("usuario") int usuario,@QueryParam("clave") int clave) {
		TM tm = new TM(getPath());
		List<Usuario> administradores;
		try {
			administradores = tm.getAdministradores(usuario,clave);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(administradores).build();
	}

	/**
	 * Metodo que expone servicio REST usando GET que busca el video con el id que entra como parametro
	 * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/<<id>>" para la busqueda"
	 * @param name - Nombre del video a buscar que entra en la URL como parametro 
	 * @return Json con el/los videos encontrados con el nombre que entra como parametro o json con 
	 * el error que se produjo
	 */
	@GET
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getAdministradorPK( @QueryParam( "id" ) int id, @QueryParam("usuario") int usuario,@QueryParam("clave") int clave )
	{
		TM tm = new TM( getPath( ) );
		try
		{
			Usuario admin = tm.getAdministradorPK(id,usuario,clave);
			return Response.status( 200 ).entity( admin ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	/**
	 * Metodo que expone servicio REST usando GET que da todos los videos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos
	 * @return Json con todos los videos de la base de datos o json con 
	 * el error que se produjo
	 */
	@GET
	@Path("representantes")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRepresentantes(@QueryParam("usuario") int admin,@QueryParam("usuarioRep") int userRepresentante,
			@QueryParam("claveUser") int claveUsuario, @QueryParam("claveRep") int claveRep) {
		TM tm = new TM(getPath());
		List<Usuario> administradores;
		try {
			administradores = tm.getRepresentantes(admin,userRepresentante,claveUsuario,claveRep);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(administradores).build();
	}

	/**
	 * Metodo que expone servicio REST usando GET que busca el video con el id que entra como parametro
	 * <b>URL: </b> http://"ip o nombre de host":8080/VideoAndes/rest/videos/<<id>>" para la busqueda"
	 * @param name - Nombre del video a buscar que entra en la URL como parametro 
	 * @return Json con el/los videos encontrados con el nombre que entra como parametro o json con 
	 * el error que se produjo
	 */
	@GET
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getRepresentantePK( @QueryParam( "id" ) int id,@QueryParam("usuario") int admin2,@QueryParam("usuarioRep") int userRepresentante,@QueryParam("restaurante") String restaurante,
			@QueryParam("claveUser") int claveUsuario, @QueryParam("claveRep") int claveRep,@QueryParam("claveRestaurante") int claveRestaurante )
	{
		TM tm = new TM( getPath( ) );
		try
		{
			Usuario admin = tm.getRepresentantesPK(id,admin2,userRepresentante,restaurante,claveUsuario,claveRep,claveRestaurante);
			return Response.status( 200 ).entity( admin ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}



}
