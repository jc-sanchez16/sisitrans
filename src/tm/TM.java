package tm;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import vos.Articulo;
import vos.Ingrediente;
import vos.Menu;
import vos.Orden;
import vos.Producto;
import vos.Reserva;
import vos.Restaurante;
import vos.Usuario;
import vos.Zona;
import dao.DAOIngrediente;
import dao.DAOOrden;
import dao.DAOProducto;
import dao.DAOReserva;
import dao.DAORestaurante;
import dao.DAOUsuario;
import dao.DAOOrden;
import dao.DAOZona;

public class TM {

	/**
	 * Atributo estatico que contiene el path relativo del archivo que tiene los datos de la conexion
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estatico que contiene el path absoluto del archivo que tiene los datos de la conexion
	 */
	private  String connectionDataPath;

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base de datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base de datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base de datos.
	 */
	private String driver;

	/**
	 * conexion a la base de datos
	 */
	private Connection conn;


	/**
	 * Metodo constructor de la clase Master, esta clase modela y contiene cada una de las 
	 * transacciones y la logica de negocios que estas conllevan.
	 * <b>post: </b> Se crea el objeto TM, se inicializa el path absoluto del archivo de conexion y se
	 * inicializa los atributos que se usan par la conexion a la base de datos.
	 * @param contextPathP - path absoluto en el servidor del contexto del deploy actual
	 */
	public TM(String contextPathP) {
		connectionDataPath = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
		initConnectionData();
	}

	/**
	 * Metodo que  inicializa los atributos que se usan para la conexion a la base de datos.
	 * <b>post: </b> Se han inicializado los atributos que se usan par la conexion a la base de datos.
	 */
	private void initConnectionData() {
		try {
			File arch = new File(this.connectionDataPath);
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(arch);
			prop.load(in);
			in.close();
			this.url = prop.getProperty("url");
			this.user = prop.getProperty("usuario");
			this.password = prop.getProperty("clave");
			this.driver = prop.getProperty("driver");
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que  retorna la conexion a la base de datos
	 * @return Connection - la conexion a la base de datos
	 * @throws SQLException - Cualquier error que se genere durante la conexion a la base de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("Connecting to: " + url + " With user: " + user);
		return DriverManager.getConnection(url, user, password);
	}

	public String addEquivalenciaIngrediente(int clave, ArrayList<String> ingredientes) throws Exception {
		DAOIngrediente daoIngrediente = new DAOIngrediente();
		String res = "no se realizo la accion";
		DAORestaurante daoRestaurante = new DAORestaurante();
		try
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoIngrediente.setConn(conn);
			daoRestaurante.setConn(conn);
			if(!daoRestaurante.verificar( clave))
				throw new Exception("No es un usuario valido");
			daoRestaurante.cerrarRecursos();
			res = daoIngrediente.AddEquivalenciaIngrediente(ingredientes);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoIngrediente.cerrarRecursos();
				daoRestaurante.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}			
		}
		return res;
	}
	public String addEquivalenciaProducto(int clave, String restaurante, ArrayList<String> productos)throws Exception {
		DAOProducto daoProducto = new DAOProducto();
		DAORestaurante daoRestaurante = new DAORestaurante();
		String res = "no se realizo la accion";		
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoProducto.setConn(conn);
			daoRestaurante.setConn(conn);
			if(!daoRestaurante.verificarRest(restaurante, clave))
				throw new Exception("No es un usuario valido");
			daoRestaurante.cerrarRecursos();
			res = daoProducto.addEquivalenciaProducto(clave,restaurante,productos);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				daoRestaurante.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return res;
	}

	public String surtirRestaurante(int clave, String restaurante) throws Exception {
		DAOProducto daoProducto = new DAOProducto();
		DAORestaurante daoRestaurante = new DAORestaurante();
		String res = "no se realizo la accion";		
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoProducto.setConn(conn);
			daoRestaurante.setConn(conn);
			if(!daoRestaurante.verificarRest(restaurante, clave))
				throw new Exception("No es un usuario valido");
			daoRestaurante.cerrarRecursos();
			res = daoProducto.surtirRestaurante(clave,restaurante);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				daoRestaurante.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return res;
	}
	public void registrarPedidoOrden(int mesa, Date fecha, ArrayList<String> productos, ArrayList<Integer> usuarios) throws Exception {
		DAOOrden daoOrden = new DAOOrden();	
		DAOProducto daoProducto = new DAOProducto();
		Savepoint save = null;
		try 
		{
			this.conn = darConexion();	
			conn.setAutoCommit(false);		
			save =conn.setSavepoint();
			daoOrden.setConn(conn);
			daoOrden.addOrdenEnProceso(new Orden(mesa, fecha, usuarios, null, null, false));
			daoProducto.setConn(conn);
			for(int i = 0 ; i<productos.size(); i++)
			{
				String[] producto = productos.get(i).split(";");
				String[] cambios = producto[2].split(":");
				daoProducto.setConn(conn);
				if(daoProducto.verificarDisponibilidad(producto[0],producto[1],cambios))
					throw new Exception("no hay unidades disponible");
				daoProducto.cerrarRecursos();
				daoOrden.registrarPedidoProducto(producto[0],producto[1],producto[2],Integer.parseInt(producto[3]), mesa, fecha);
			}
			conn.commit();


		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback(save);
			throw e;
		} finally {
			try {
				daoOrden.cerrarRecursos();
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public String registrarServicio(int clave, String restaurante, Date fecha, int mesa) throws Exception {
		String res = "no se realizo la accion";
		DAOOrden daoOrden = new DAOOrden();	
		DAORestaurante daoRestaurante = new DAORestaurante();
		DAOProducto daoProducto = new DAOProducto();
		Savepoint save = null;
		try 
		{
			this.conn = darConexion();	
			conn.setAutoCommit(false);		
			save =conn.setSavepoint();
			daoOrden.setConn(conn);
			daoRestaurante.setConn(conn);
			if(!daoRestaurante.verificarRest(restaurante, clave))
				throw new Exception("No es un usuario valido");
			daoRestaurante.cerrarRecursos();
			res = daoOrden.registrarServicio(clave,restaurante,fecha,mesa);
			ArrayList<Articulo> productos = daoOrden.getArticulosOrden(mesa, fecha.getTime());
			daoOrden.cerrarRecursos();
			daoProducto.setConn(conn);
			for (int i = 0; i < productos.size(); i++) 
			{
				Articulo producto = productos.get(i);
				daoProducto.restarUnidad(producto.getNombre(), producto.getRestaurante(), producto.getCambios().split(":"));
			}
			conn.commit();
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();			
			conn.rollback(save);
			throw e;
		} finally {
			try {
				daoOrden.cerrarRecursos();
				daoRestaurante.cerrarRecursos();
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return res;
	}

	public String cancelarServicio(int clave, String restaurante, Date fecha, int mesa) throws Exception 
	{
		String res = "no se realizo la accion";
		DAOOrden daoOrden = new DAOOrden();	
		DAORestaurante daoRestaurante = new DAORestaurante();
		Savepoint save = null;
		try 
		{
			this.conn = darConexion();	
			conn.setAutoCommit(false);		
			save =conn.setSavepoint();
			daoRestaurante.setConn(conn);
			if(!daoRestaurante.verificarRest(restaurante, clave))
				throw new Exception("No es un usuario valido");
			daoRestaurante.cerrarRecursos();
			daoOrden.setConn(conn);
			res = daoOrden.cancelarServicio(clave,restaurante,fecha,mesa);
			conn.commit();
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();			
			conn.rollback(save);
			throw e;
		} finally {
			try {
				daoOrden.cerrarRecursos();
				daoRestaurante.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return res;
	}

	public ArrayList<Articulo> consultarConsumo(int usuario, int clave, int peticion) throws Exception {
		ArrayList<Articulo> res = null;
		DAOOrden daoOrden = new DAOOrden();	
		DAOUsuario daoUsuario = new DAOUsuario();
		DAOProducto daoProducto = new DAOProducto();
		try 
		{
			this.conn = darConexion();		
			conn.setAutoCommit(false);	
			daoUsuario.setConn(conn);
			if(!(daoUsuario.verificar(usuario, clave,1)||(usuario==peticion && daoUsuario.verificar(usuario, clave, 0))))
				throw new Exception("no es un usuario valido");
			daoUsuario.cerrarRecursos();
			daoOrden.setConn(conn);
			ArrayList<String[]> ans  = daoOrden.consultarConsumo(usuario, clave, peticion);
			daoOrden.cerrarRecursos();
			daoProducto.setConn(conn);
			for (int i = 0; i < ans.size(); i++) 
			{
				String [] an = ans.get(i);
				Articulo art = daoProducto.getProductoPK(an[0], an[1]);
				if(art!= null)
				{
					res.add(art);
				}
				res.add(daoProducto.getMenuPK(an[0], an[1]));
			}
			conn.commit();
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOrden.cerrarRecursos();
				daoUsuario.cerrarRecursos();
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return res;
	}

	public String consultarPedidos(String usuario, int clave) throws Exception {
		String res = null;
		//TODO: FALTA Y EN ADELANTE
		DAOOrden daoOrden = new DAOOrden();	
		try 
		{
			this.conn = darConexion();	
			conn.setAutoCommit(false);		
			daoOrden.setConn(conn);
			res = daoOrden.consultarPedidos(usuario, clave);
			conn.commit();
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOrden.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return res;
	}

	public String consultarConsumo(int clave, int usuario, String restaurante, Date fecI, Date fecF, String order,
			String group) throws Exception {
		String res = null;
		DAOOrden daoOrden = new DAOOrden();	
		DAOUsuario daoUsuario = new DAOUsuario();
		DAORestaurante daoRestaurante = new DAORestaurante();
		try 
		{
			this.conn = darConexion();		
			daoUsuario.setConn(conn);
			if(usuario != 0 && !(daoUsuario.verificar(usuario, clave,1)))
				throw new Exception("no es un usuario valido");
			daoUsuario.cerrarRecursos();
			daoRestaurante.setConn(conn);
			if(usuario == 0 && !daoRestaurante.verificarRest(restaurante, clave))
				throw new Exception("no es un usuario valido");
			daoRestaurante.cerrarRecursos();
			daoOrden.setConn(conn);
			res = daoOrden.consultarConsumo(restaurante, fecI, fecF,order, group);
			daoOrden.cerrarRecursos();			
			conn.commit();
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOrden.cerrarRecursos();
				daoUsuario.cerrarRecursos();
				daoRestaurante.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return res;
	}

	public String consultarConsumo2(int clave, int usuario, String restaurante, Date fecI, Date fecF, String order,
			String group) throws Exception {
		String res = null;
		DAOOrden daoOrden = new DAOOrden();	
		DAOUsuario daoUsuario = new DAOUsuario();
		DAORestaurante daoRestaurante = new DAORestaurante();
		try 
		{
			this.conn = darConexion();		
			daoUsuario.setConn(conn);
			if(usuario != 0 && !(daoUsuario.verificar(usuario, clave,1)))
				throw new Exception("no es un usuario valido");
			daoUsuario.cerrarRecursos();
			daoRestaurante.setConn(conn);
			if(usuario == 0 && !daoRestaurante.verificarRest(restaurante, clave))
				throw new Exception("no es un usuario valido");
			daoRestaurante.cerrarRecursos();
			daoOrden.setConn(conn);
			res = daoOrden.consultarConsumo2(restaurante, fecI, fecF,order, group);
			daoOrden.cerrarRecursos();			
			conn.commit();
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOrden.cerrarRecursos();
				daoUsuario.cerrarRecursos();
				daoRestaurante.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return res;
	}
	
	public String consultarFuncionamiento(int usuario, int clave, String dia) throws Exception {
		String res = null;
		DAOOrden daoOrden = new DAOOrden();	
		DAOUsuario daoUsuario = new DAOUsuario();
		try 
		{
			this.conn = darConexion();		
			daoUsuario.setConn(conn);
			if(!(daoUsuario.verificar(usuario, clave,1)))
				throw new Exception("no es un usuario valido");
			daoUsuario.cerrarRecursos();
			daoOrden.setConn(conn);
			res = daoOrden.consultarFuncionamiento(dia);
			daoOrden.cerrarRecursos();			
			conn.commit();
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOrden.cerrarRecursos();
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return res;
	}
	//Ingredientes

	public List<Ingrediente> getIngredientes() throws Exception {
		List<Ingrediente> ingredientes;
		DAOIngrediente daoIngrediente = new DAOIngrediente();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoIngrediente.setConn(conn);
			ingredientes = daoIngrediente.getIngredientes();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoIngrediente.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ingredientes;
	}

	public Ingrediente getIngredientePK(String nombre) throws Exception {
		Ingrediente ingrediente;
		DAOIngrediente daoIngrediente = new DAOIngrediente();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoIngrediente.setConn(conn);
			ingrediente = daoIngrediente.getIngredientePK(nombre);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoIngrediente.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ingrediente;
	}

	public void addIngrediente(Ingrediente ingrediente, String restaurante, int claveRestaurante) throws Exception {
		DAOIngrediente daoIngrediente = new DAOIngrediente();
		DAORestaurante daoRestaurante = new DAORestaurante();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoIngrediente.setConn(conn);
			daoRestaurante.setConn(conn);
			if(!daoRestaurante.verificarRest(restaurante, claveRestaurante))
				throw new Exception("No es un usuario valido");
			daoIngrediente.addIngrediente(ingrediente);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoIngrediente.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void deleteIngrediente(String nombre, String restaurante, int claveRestaurante) throws Exception {
		DAOIngrediente daoIngrediente = new DAOIngrediente();
		DAORestaurante daoRestaurante = new DAORestaurante();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoIngrediente.setConn(conn);
			daoRestaurante.setConn(conn);
			if(!daoRestaurante.verificarRest(restaurante, claveRestaurante))
				throw new Exception("No es un usuario valido");
			daoIngrediente.deleteIngrediente(nombre);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoIngrediente.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	//Orden
	public List<Orden> getOrdenes() throws Exception {
		List<Orden> ordenes;
		DAOOrden daoOrden = new DAOOrden();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoOrden.setConn(conn);
			ordenes = daoOrden.getOrdenes();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOrden.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ordenes;
	}

	public Orden getOrdenPK(int mesa,Date fecha) throws Exception {
		Orden orden;
		DAOOrden daoOrden = new DAOOrden();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoOrden.setConn(conn);
			orden = daoOrden.getOrdenPK(mesa, fecha);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOrden.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return orden;
	}

	//Producto
	
	public List<Producto> getProductos() throws Exception {
		List<Producto> productos;
		DAOProducto daoProducto = new DAOProducto();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoProducto.setConn(conn);
			productos = daoProducto.getProductos();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}

	public Producto getProductoPK(String nombre,String restaurante) throws Exception {
		Producto producto;
		DAOProducto daoProducto = new DAOProducto();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoProducto.setConn(conn);
			producto = daoProducto.getProductoPK(nombre, restaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return producto;
	}

	public void addProducto(Producto producto, String restaurante, int claveRestaurante) throws Exception {
		DAOProducto daoProducto = new DAOProducto();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoProducto.setConn(conn);
			daoProducto.addProducto(producto,restaurante,claveRestaurante);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updateProducto(Producto producto, String restaurante, int claveRestaurante) throws Exception {
		DAOProducto daoProducto = new DAOProducto();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoProducto.setConn(conn);
			daoProducto.updateProducto(producto,restaurante,claveRestaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void deleteProducto(String nombre,String restaurante, String restaurante2, int claveRestaurante) throws Exception {
		DAOProducto daoProducto = new DAOProducto();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoProducto.setConn(conn);
			daoProducto.deleteProducto(nombre, restaurante,restaurante2,claveRestaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public List<Menu> getMenus() throws Exception {
		List<Menu> menus;
		DAOProducto daoProducto = new DAOProducto();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoProducto.setConn(conn);
			menus = daoProducto.getMenus();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return menus;
	}

	public Menu getMenuPK(String nombre,String restaurante) throws Exception {
		Menu menu;
		DAOProducto daoProducto = new DAOProducto();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoProducto.setConn(conn);
			menu = daoProducto.getMenuPK(nombre, restaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return menu;
	}

	public void addMenu(Menu menu, String restaurante, int claveRestaurante) throws Exception {
		DAOProducto daoProducto = new DAOProducto();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoProducto.setConn(conn);
			daoProducto.addMenu(menu,restaurante,claveRestaurante);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updateMenu(Menu menu, String restaurante, int claveRestaurante) throws Exception {
		DAOProducto daoProducto = new DAOProducto();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoProducto.setConn(conn);
			daoProducto.updateMenu(menu,restaurante,claveRestaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void deleteMenu(String nombre, String restaurante, String restaurante2, int claveRestaurante) throws Exception {
		DAOProducto daoProducto = new DAOProducto();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoProducto.setConn(conn);
			daoProducto.deleteMenu(nombre, restaurante,restaurante2,claveRestaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	//Reserva
	
	public List<Reserva> getReservas() throws Exception {
		List<Reserva> reservas;
		DAOReserva daoReserva = new DAOReserva();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoReserva.setConn(conn);
			reservas = daoReserva.getReservas();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoReserva.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return reservas;
	}
	
	public Reserva getReservaPK(int id, Date fecha) throws Exception {
		Reserva reserva;
		DAOReserva daoReserva = new DAOReserva();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoReserva.setConn(conn);
			reserva = daoReserva.getReservaPK(id, fecha);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoReserva.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return reserva;
	}

	public void addReserva(Reserva reserva) throws Exception {
		DAOReserva daoReserva = new DAOReserva();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoReserva.setConn(conn);
			daoReserva.addReserva(reserva);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoReserva.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updateReserva(Reserva reserva) throws Exception {
		DAOReserva daoReserva = new DAOReserva();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoReserva.setConn(conn);
			daoReserva.updateReserva(reserva);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoReserva.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void deleteReserva(int id, Date fecha) throws Exception {
		DAOReserva daoReserva = new DAOReserva();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoReserva.setConn(conn);
			daoReserva.deleteReserva(id, fecha);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoReserva.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	//Restaurante
	public List<Restaurante> getRestaurantes() throws Exception {

		List<Restaurante> restaurantes;
		DAORestaurante daoRestaurante = new DAORestaurante();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoRestaurante.setConn(conn);

			restaurantes = daoRestaurante.getRestaurantes();


		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRestaurante.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restaurantes;
	}

	public Restaurante getRestaurantePK(String nombre) throws Exception {

		Restaurante restaurante;
		DAORestaurante daoRestaurante = new DAORestaurante();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoRestaurante.setConn(conn);
			restaurante = daoRestaurante.getRestaurantePK(nombre);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRestaurante.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restaurante;
	}

	public void addRestaurante(Restaurante restaurante, int clave, int usuario, int contraseñaAd) throws Exception {

		DAORestaurante daoRestaurante = new DAORestaurante();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoRestaurante.setConn(conn);
			daoRestaurante.addRestaurante(restaurante, clave, usuario,contraseñaAd);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRestaurante.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updateRestaurante(Restaurante restaurante, int usuario, int contraseñaAd) throws Exception {
		DAORestaurante daoRestaurante = new DAORestaurante();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoRestaurante.setConn(conn);
			daoRestaurante.updateRestaurante(restaurante,usuario,contraseñaAd);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRestaurante.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void deleteRestaurante(String nombre, int usuario, int contraseñaAd) throws Exception {
		DAORestaurante daoRestaurante = new DAORestaurante();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoRestaurante.setConn(conn);
			daoRestaurante.deleteRestaurante(nombre,usuario,contraseñaAd);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRestaurante.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	//Usuario
	public List<Usuario> getUsuarios(int usuario, int clave) throws Exception {
		List<Usuario> usuarios;
		DAOUsuario daoUsuario = new DAOUsuario();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			usuarios = daoUsuario.getUsuarios(usuario,clave,DAOUsuario.ADMINISTRADOR);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usuarios;
	}

	public Usuario getUsuarioPK(int id) throws Exception {
		Usuario usuario;
		DAOUsuario daoUsuario = new DAOUsuario();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			usuario = daoUsuario.getUsuarioPK(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usuario;
	}

	public void addUsuario(Usuario usuario, int clave, int usuario2, int clave2) throws Exception {
		DAOUsuario daoUsuario = new DAOUsuario();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			daoUsuario.addUsuario(usuario, clave, usuario2, clave2);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updateUsuario(Usuario usuario, int usuario2, int clave) throws Exception {
		DAOUsuario daoUsuario = new DAOUsuario();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			daoUsuario.updateUsuario(usuario, usuario2, clave);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void deleteUsuario(int id, int usuario, int clave) throws Exception {
		DAOUsuario daoUsuario = new DAOUsuario();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			daoUsuario.deleteUsuario(id, usuario, clave);
	
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public List<Usuario> getAdministradores(int usuario, int clave) throws Exception {
		List<Usuario> administradores;
		DAOUsuario daoUsuario = new DAOUsuario();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			administradores = daoUsuario.getAdministradores(usuario,clave);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return administradores;
	}

	public Usuario getAdministradorPK(int id, int usuario, int clave) throws Exception {
		Usuario administrador;
		DAOUsuario daoUsuario = new DAOUsuario();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			administrador = daoUsuario.getAdministradorPK(id,usuario,clave);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return administrador;
	}
	public List<Usuario> getRepresentantes(int admin, int userRepresentante, int claveUsuario, int claveRep) throws Exception {
		List<Usuario> administradores;
		DAOUsuario daoUsuario = new DAOUsuario();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			administradores = daoUsuario.getRepresentantes(admin,userRepresentante,claveUsuario,claveRep);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return administradores;
	}

	public Usuario getRepresentantesPK(int id, int admin2, int userRepresentante, String restaurante, int claveUsuario, int claveRep, int claveRestaurante) throws Exception {
		Usuario administrador;
		DAOUsuario daoUsuario = new DAOUsuario();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			administrador = daoUsuario.getRepresentantesPK(id,admin2,userRepresentante,restaurante,claveUsuario,claveRep,claveRestaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return administrador;
	}

	//Zona

	public List<Zona> getZonas() throws Exception {
		List<Zona> zonas;
		DAOZona daoZona = new DAOZona();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoZona.setConn(conn);
			zonas = daoZona.getZonas();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoZona.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return zonas;
	}

	public Zona getZonaPK(int id) throws Exception {
		Zona zona;
		DAOZona daoZona = new DAOZona();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoZona.setConn(conn);
			zona = daoZona.getZonaPK(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoZona.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return zona;
	}

	public void addZona(Zona zona, int usuario, int clave) throws Exception {
		DAOZona daoZona = new DAOZona();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoZona.setConn(conn);
			daoZona.addZona(zona,usuario, clave, DAOUsuario.ADMINISTRADOR);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoZona.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updateZona(Zona zona, int usuario, int clave) throws Exception {
		DAOZona daoZona = new DAOZona();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoZona.setConn(conn);
			daoZona.updateZona(zona,usuario,clave,DAOUsuario.ADMINISTRADOR);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoZona.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void deleteZona(int id) throws Exception {
		DAOZona daoZona = new DAOZona();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoZona.setConn(conn);
			daoZona.deleteZona(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoZona.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

}
