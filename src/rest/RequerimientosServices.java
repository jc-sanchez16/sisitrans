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

import tm.TM;
import vos.Articulo;
import vos.Ingrediente;
import vos.Producto;

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
		TM tm = new TM(getPath());
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
		TM tm = new TM(getPath());
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
		TM tm = new TM(getPath());
		String res = "No se realizo la accion";
		try {
			res = tm.surtirRestaurante(clave, restaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	public static class Respuesta{
		@JsonProperty(value="productos")
		public ArrayList<String> productos;
		@JsonProperty(value="usuarios")
		public ArrayList<Integer> usuarios;
		public boolean isEmpty;
		public Respuesta() {
			productos = new ArrayList<String>();
			usuarios = new ArrayList<Integer>();
			isEmpty = true;
		}
		public Respuesta(@JsonProperty(value="productos")ArrayList<String> productos,@JsonProperty(value="usuarios") ArrayList<Integer> usuarios) {
			this.productos = productos;
			this.usuarios = usuarios;
			isEmpty = false;
		}
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("15")
	public Response registrarPedidoOrden(@QueryParam("mesa") int mesa, Respuesta res) {
		TM tm = new TM(getPath());
		try {
			Date f =new Date();
			f = new Date(f.getYear(),f.getMonth(),f.getDate(),f.getHours(), f.getMinutes());
			tm.registrarPedidoOrden(mesa,f,res.productos,res.usuarios);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}		return Response.status(200).entity(res).build();
	}
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("16")
	public Response registrarServicio(@QueryParam("clave") int clave,@QueryParam("restaurante") String restaurante, @QueryParam("fecha") String fecha,@QueryParam("mesa") int mesa) {
		TM tm = new TM(getPath());
		Date fec = new Date(fecha);
		String res = "No se realizo la accion";
		try {
			res = tm.registrarServicio(clave, restaurante, fec, mesa);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("17")
	public Response cancelarServicio(@QueryParam("clave") int clave,@QueryParam("restaurante") String restaurante, @QueryParam("fecha") String fecha,@QueryParam("mesa") int mesa) {
		TM tm = new TM(getPath());
		String res = "No se realizo la accion";
		Date fec = new Date(fecha);
		try {
			res = tm.cancelarServicio(clave, restaurante, fec, mesa);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("C7")
	public Response consultarConsumo(@QueryParam("clave") int clave,@QueryParam("peticion") int peticion, @QueryParam("usuario") int usuario) {
		TM tm = new TM(getPath());
		ArrayList<Articulo> res = null;
		try {
			res = tm.consultarConsumo(usuario,clave, peticion);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	
//	RFC8. CONSULTAR PEDIDOS
//	Muestra la información consolidada de los pedidos hechos en RotondAndes. Consolida, como mínimo, para cada uno los
//	restaurantes y para cada uno de sus productos las ventas totales (en dinero y en cantidad), lo consumidos por clientes
//	registrados y por clientes no registrados.
//	Esta operación es realizada por un usuario restaurante y por el administrador de RotondAndes.
//	NOTA: Respetando la privacidad de los clientes, cuando un restaurante hace esta consulta obtiene la información de sus
//	propias actividades, mientras que el administrador obtiene toda la información. Ver RNF1.
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("C8")
	public Response consultarPedidos(@QueryParam("clave") int clave,@QueryParam("usuario") String usuario ) {
		TM tm = new TM(getPath());
		String res = null;
		try {
			res = tm.consultarPedidos(usuario,clave);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("C9")
	public Response consultarConsumo(@QueryParam("clave") int clave,@QueryParam("usuario") int usuario, @QueryParam("restaurante") String restaurante,@QueryParam("fechaI") String fechaI,@QueryParam("fechaF") String fechaF,
									 @QueryParam("order") String order, @QueryParam("group") String group) {
		TM tm = new TM(getPath());
		String res = null;
		Date fecI = new Date(fechaI);
		Date fecF = new Date(fechaF);
		try {
			res = tm.consultarConsumo(clave, usuario, restaurante,fecI, fecF,
					 order, group);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("C10")
	public Response consultarConsumo2(@QueryParam("clave") int clave,@QueryParam("usuario") int usuario, @QueryParam("restaurante") String restaurante,@QueryParam("fechaI") String fechaI,@QueryParam("fechaF") String fechaF,
			 @QueryParam("order") String order, @QueryParam("group") String group) {
		TM tm = new TM(getPath());
		String res = null;
		Date fecI = new Date(fechaI);
		Date fecF = new Date(fechaF);
		try {
			res = tm.consultarConsumo2(clave, usuario, restaurante,fecI, fecF,
					 order, group);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("C11")
	public Response consultarFuncionamiento(@QueryParam("clave") int clave,@QueryParam("usuario") int usuario, @QueryParam("dia") String dia ) {
		TM tm = new TM(getPath());
		String res = null;
		Date fecI = new Date(dia);
		try {
			res = tm.consultarFuncionamiento(usuario,clave,fecI);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("C12")
	public Response consultarBuenosClientes(@QueryParam("mes") String dia,@QueryParam("clave") int clave,@QueryParam("usuario") int usuario) {
		TM tm = new TM(getPath());
		String res = null;
		Date fecI = new Date(dia);
		try {
			res = tm.consultarBuenosClientes(fecI,clave,usuario);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("18")
	public Response registrarPedidoOrdenD(@QueryParam("mesa") int mesa, Respuesta res) {
		TM tm = new TM(getPath());
		try {
			Date f =new Date();
			f = new Date(f.getYear(),f.getMonth(),f.getDate(),f.getHours(), f.getMinutes());
			tm.registrarPedidoOrdenD(mesa,f,res.productos,res.usuarios);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}		return Response.status(200).entity(res).build();
	}
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("19C")
	public Response retirarRestauranteDC(@QueryParam("clave") int clave,@QueryParam("usuario") int usuario, @QueryParam("restaurante") String restaurante)
	{
		TM tm = new TM(getPath());
		String res= null;
		try {
			res = tm.retirarRestauranteDC(clave, usuario, restaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("19T")
	public Response retirarRestauranteDT(@QueryParam("clave") int clave,@QueryParam("usuario") int usuario, @QueryParam("restaurante") String restaurante)
	{
		TM tm = new TM(getPath());
		String res= null;
		try {
			res = tm.retirarRestauranteDT(clave, usuario, restaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("C13")
	public Response consultarProductosD()
	{
		TM tm = new TM(getPath());
		ArrayList<Producto> res = null;
		try {
			res = tm.consultarProductosD();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("C14")
	public Response consultarRentabilidadD(@QueryParam("restaurante") String restaurante,@QueryParam("usuario") int usuario,@QueryParam("clave") int clave,@QueryParam("fechaI") String fechaI,@QueryParam("fechaF") String fechaF) {
		TM tm = new TM(getPath());
		String res = null;
		Date fecI = new Date(fechaI);
		Date fecF = new Date(fechaF);
		try {
			res = tm.consultarRentabilidadD(restaurante, usuario,clave,fecI,fecF);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(res).build();
	}
}
