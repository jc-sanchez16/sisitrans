package tm;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import vos.Articulo;
import vos.Menu;
import vos.Orden;
import vos.Producto;
import vos.Zona;
import dao.DAOIngrediente;
import dao.DAOOrden;
import dao.DAOProducto;
import dao.DAOOrden;
import dao.DAOZona;

public class TMRequerimientos {

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
	public TMRequerimientos(String contextPathP) {
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
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoIngrediente.setConn(conn);
			res = daoIngrediente.AddEquivalenciaIngrediente(clave, ingredientes);
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
		return res;
	}
	public String addEquivalenciaProducto(int clave, String restaurante, ArrayList<String> productos)throws Exception {
		DAOProducto daoProducto = new DAOProducto();
		String res = "no se realizo la accion";		
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoProducto.setConn(conn);
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
		String res = "no se realizo la accion";		
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoProducto.setConn(conn);
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
		Savepoint save = null;
		try 
		{
			this.conn = darConexion();	
			conn.setAutoCommit(false);		
			save =conn.setSavepoint();
			daoOrden.setConn(conn);
			daoOrden.addOrdenEnProceso(new Orden(mesa, fecha, usuarios, null, null, false));
			for(int i = 0 ; i<productos.size(); i++)
			{
				String[] producto = productos.get(i).split(";");
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
		Savepoint save = null;
		try 
		{
			this.conn = darConexion();	
			conn.setAutoCommit(false);		
			save =conn.setSavepoint();
			daoOrden.setConn(conn);
			res = daoOrden.registrarServicio(clave,restaurante,fecha,mesa);
			conn.commit();
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();			
			conn.rollback(save);
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

	public String cancelarServicio(int clave, String restaurante, Date fecha, int mesa) throws Exception 
	{
		String res = "no se realizo la accion";
		DAOOrden daoOrden = new DAOOrden();	
		Savepoint save = null;
		try 
		{
			this.conn = darConexion();	
			conn.setAutoCommit(false);		
			save =conn.setSavepoint();
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
		try 
		{
			this.conn = darConexion();		
			conn.setAutoCommit(false);	
			daoOrden.setConn(conn);
			res = daoOrden.consultarConsumo(usuario, clave, peticion);
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

	public String consultarPedidos(String usuario, int clave) throws Exception {
		String res = null;
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
}
