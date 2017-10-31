/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (BogotÃ¡ - Colombia)
 * Departamento de IngenierÃ­a de Sistemas y ComputaciÃ³n
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: VideoAndes
 * Autor: Juan Felipe GarcÃ­a - jf.garcia268@uniandes.edu.co
 * -------------------------------------------------------------------
 */
package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import vos.*;


public class DAOOrden {


	private ArrayList<Object> recursos;


	private Connection conn;


	public DAOOrden() {
		recursos = new ArrayList<Object>();
	}


	public void cerrarRecursos() {
		for(Object ob : recursos){
			if(ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}


	public void setConn(Connection con){
		this.conn = con;
	}


	public ArrayList<Orden> getOrdenes() throws SQLException, Exception 
	{
		ArrayList<Orden> lista = new ArrayList<Orden>();
		String sql = "SELECT * FROM ORDEN";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int mesa = rs.getInt("MESA");
			long f = rs.getInt("FECHA");
			Date fecha = new Date(f);
			ArrayList<Integer> usuarios = getUsuariosOrden(mesa, f);
			ArrayList<Menu> menus= getMenusOrden(mesa,f);
			ArrayList<Producto> productos = getProductosOrden(mesa,f);
			boolean atendido = rs.getInt("ATENDIDO")==0? true: false;
			lista.add(new Orden(mesa, fecha, usuarios, menus, productos, atendido));
		}

		return lista;
	}


	public Orden getOrdenPK(int mesa, Date fecha) throws SQLException, Exception 
	{
		Orden lista = null;		
		long f = fecha.getTime();
		String sql = "SELECT * FROM ORDEN WHERE MESA ="+mesa+" AND FECHA = "+f;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			ArrayList<Integer> usuarios = getUsuariosOrden(mesa, f);
			ArrayList<Menu> menus= getMenusOrden(mesa,f);
			ArrayList<Producto> productos = getProductosOrden(mesa,f);
			boolean atendido = rs.getInt("ATENDIDO")==0? true: false;
			lista = new Orden(mesa, fecha, usuarios, menus, productos, atendido);
		}
		return lista;
	}




	public void addOrdenEnProceso(Orden orden) throws SQLException, Exception {

		int aten = orden.getAtendido()==true?0:1;
		String sql = "INSERT INTO ORDEN VALUES (";
		sql += orden.getFecha().getTime() + ",";
		sql += orden.getMesa() + ",";
		sql += aten+")";


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		addUsuarios(orden);

	}


	public ArrayList<Orden> getOrdenesUsuario(int id)  throws Exception{
		ArrayList<Orden> lista = new ArrayList<Orden>();
		String sql = "SELECT * FROM ORDEN_USUARIO WHERE ID_CLIENTE ="+id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int mesa = rs.getInt("MESA");
			long f = rs.getInt("FECHA");
			Date fecha = new Date(f);			
			lista.add(getOrdenPK(mesa,fecha));
		}

		return lista;
	}


	private ArrayList<Integer> getUsuariosOrden(int mesa, long f) throws Exception {
		ArrayList<Integer> lista = new ArrayList<Integer>();
		String sql = "SELECT * FROM ORDEN_USUARIO WHERE MESA = "+mesa+" AND FECHA = "+f;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {		
			lista.add(rs.getInt("ID_CLIENTE"));
		}

		return lista;
	}


	private ArrayList<Menu> getMenusOrden(int mesa, long f) throws Exception {		
		ArrayList<Menu> lista = new ArrayList<Menu>();
		DAOProducto daoProducto = new DAOProducto();
		try
		{
			daoProducto.setConn(conn);
			String sql = "SELECT * FROM ORDEN_PRODUCTO WHERE MESA = "+mesa+" AND FECHA = "+f;
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {		
				String nombre = rs.getString("NOMBRE");
				String restaurante = rs.getString("RESTAURANTE");
				Menu menu = daoProducto.getMenuPK(nombre, restaurante);
				String cambios = rs.getString("CAMBIOS");
				if(menu!= null)	
				{
					menu.setCambios(cambios);
					lista.add(menu);
				}
			}
		}
		finally
		{
			daoProducto.cerrarRecursos();
		}
		return lista;
	}


	private ArrayList<Producto> getProductosOrden(int mesa, long f)throws Exception {
		ArrayList<Producto> lista = new ArrayList<Producto>();
		DAOProducto daoProducto = new DAOProducto();
		try
		{
			daoProducto.setConn(conn);
			String sql = "SELECT * FROM ORDEN_PRODUCTO WHERE MESA = "+mesa+" AND FECHA = "+f;
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {		
				String nombre = rs.getString("NOMBRE");
				String restaurante = rs.getString("RESTAURANTE");
				Producto producto = daoProducto.getProductoPK(nombre, restaurante);
				String cambios = rs.getString("CAMBIOS");
				if(producto!= null)	
				{
					producto.setCambios(cambios);
					lista.add(producto);
				}
			}
		}
		finally
		{
			daoProducto.cerrarRecursos();
		}
		return lista;
	}
	private ArrayList<Articulo> getArticulosOrden(int mesa, long f)throws Exception {
		ArrayList<Articulo> lista = new ArrayList<Articulo>();
		DAOProducto daoProducto = new DAOProducto();
		try
		{
			daoProducto.setConn(conn);
			String sql = "SELECT * FROM ORDEN_PRODUCTO WHERE MESA = "+mesa+" AND FECHA = "+f;
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {		
				String nombre = rs.getString("NOMBRE");
				String restaurante = rs.getString("RESTAURANTE");
				Producto producto = daoProducto.getProductoPK(nombre, restaurante);
				String cambios = rs.getString("CAMBIOS");
				if(producto ==null)
				{
					Menu product = daoProducto.getMenuPK(nombre, restaurante);
					if(product!= null)
					{
						product.setCambios(cambios);
						lista.add(product);
					}
				}
				else 
				{
					producto.setCambios(cambios);
					lista.add(producto);
				}
			}
		}
		finally
		{
			daoProducto.cerrarRecursos();
		}
		return lista;
	}
	private void addUsuarios(Orden orden) throws Exception {
		ArrayList<Integer> usuarios = orden.getUsuarios(); 
		for (int j = 0; j < usuarios.size(); j++) 
		{
			String sql = "INSERT INTO ORDEN_USUARIO VALUES (";
			sql += orden.getFecha().getTime() + ",";
			sql += orden.getMesa() + ",";
			sql += usuarios.get(j) +")";
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
		}
	}


	private void addMenus(Orden orden)throws Exception {
		ArrayList<Menu> menus = orden.getMenus(); 
		for (int j = 0; j < menus.size(); j++) 
		{
			Menu menu = menus.get(j);
			String sql = "INSERT INTO ORDEN_PRODUCTO VALUES ('";
			sql += menu.getRestaurante()+"','";
			sql += menu.getNombre()+"',";
			sql += orden.getFecha().getTime() + ",";
			sql += orden.getMesa() + ")";
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
		}
	}


	private void addProductos(Orden orden) throws Exception 
	{
		ArrayList<Producto> productos = orden.getProductos(); 
		for (int j = 0; j < productos.size(); j++) 
		{
			Producto producto = productos.get(j);
			String sql = "INSERT INTO ORDEN_PRODUCTO VALUES ('";
			sql += producto.getRestaurante()+"','";
			sql += producto.getNombre()+"',";
			sql += orden.getFecha().getTime() + ",";
			sql += orden.getMesa() + ")";
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
		}
	}

	private void deleteUsuarios(int mesa, long f)throws Exception 
	{
		String sql = "DELETE FROM ORDEN_USUARIO";
		sql += " WHERE MESA = "+mesa+" AND FECHA = "+f;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	private void deleteMenusYProductos(int mesa, long f)throws Exception 
	{
		String sql = "DELETE FROM ORDEN_PRODUCTO";
		sql += " WHERE MESA = "+mesa+" AND FECHA = "+f;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	public void registrarPedidoProducto(String nombre, String restaurante, String camb,int usuario, int mesa, Date fecha) throws Exception
	{
		DAOProducto daoProducto = new DAOProducto();
		try
		{
			String[] cambios = camb.split(":");
			daoProducto.setConn(conn);
			if(daoProducto.verificarDisponibilidad(nombre,restaurante,cambios))
			{
				String sql = "INSERT INTO ORDEN_PRODUCTO VALUES ('";
				sql += restaurante+"','";
				sql += nombre+"',";
				sql += fecha.getTime() + ",";
				sql += mesa + ",'";
				sql += camb +"',";
				sql += usuario +")";
				PreparedStatement prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				prepStmt.executeQuery();
			}
			else 
			{
				throw new Exception("no hay unidades disponible");
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			daoProducto.cerrarRecursos();
		}
	}


	public String registrarServicio(int clave, String restaurante, Date fecha, int mesa) throws Exception 
	{
		String res = "no se realizo la accion";
		DAOProducto daoProducto = new DAOProducto();
		DAORestaurante daoRestaurante = new DAORestaurante();
		try
		{
			daoProducto.setConn(conn);
			daoRestaurante.setConn(conn);
			if(!daoRestaurante.verificarRest(restaurante, clave))
				throw new Exception("no es un usuario valido");
			String sql = "UPDATE ORDEN SET ";
			sql += "ATENDIDO ="+ 0+"";
			sql += " WHERE FECHA = " + fecha.getTime()+" AND MESA ="+mesa;
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
			ArrayList<Articulo> productos = getArticulosOrden(mesa, fecha.getTime());
			for (int i = 0; i < productos.size(); i++) 
			{
				Articulo producto = productos.get(i);
				daoProducto.restarUnidad(producto.getNombre(), producto.getRestaurante(), producto.getCambios().split(":"));
			}
			res= "se realizo la accion";
		}
		finally
		{
			daoProducto.cerrarRecursos();
			daoRestaurante.cerrarRecursos();
		}
		return res;
	}


	public String cancelarServicio(int clave, String restaurante, Date fecha, int mesa) throws SQLException, Exception {
		String res = "no se realizo la accion";
		DAORestaurante daoRestaurante = new DAORestaurante();
		try
		{
			daoRestaurante.setConn(conn);
			if(getOrdenPK(mesa, fecha).getAtendido())
				throw new Exception("La orden ya estaba finalizada");
			if(!daoRestaurante.verificarRest(restaurante, clave))
				throw new Exception("no es un usuario valido");
			String sql = "DELETE FROM ORDEN WHERE FECHA = " + fecha.getTime()+" AND MESA ="+mesa;
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
			res= "se realizo la accion";
		}
		finally
		{
			daoRestaurante.cerrarRecursos();
		}
		return res;
	}


	public ArrayList<Articulo> consultarConsumo(int usuario, int clave, int peticion) throws Exception {
		ArrayList<Articulo> res = new ArrayList<Articulo>();
		DAOUsuario daoUsuario= new DAOUsuario();
		DAOProducto daoProducto = new DAOProducto();		
		try
		{
			daoProducto.setConn(conn);
			daoUsuario.setConn(conn);
			if(!(daoUsuario.verificar(usuario, clave,1)||(usuario==peticion && daoUsuario.verificar(usuario, clave, 0))))
				throw new Exception("no es un usuario valido");
			String sql = "SELECT * FROM ORDEN_PRODUCTO WHERE USUARIO = "+peticion;
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {		
				String nombre = rs.getString("NOMBRE");
				String restaurante = rs.getString("RESTAURANTE");
				Articulo art = daoProducto.getProductoPK(nombre, restaurante);
				if(art!= null)
				{
					res.add(art);
				}
				res.add(daoProducto.getMenuPK(nombre, restaurante));
			}
		}
		finally
		{
			daoUsuario.cerrarRecursos();
			daoProducto.cerrarRecursos();
		}
		return res;
	}


	public String consultarPedidos(String usuario, int clave) throws SQLException, Exception {
		String res = null;
		DAOUsuario daoUsuario= new DAOUsuario();
		DAOProducto daoProducto = new DAOProducto();		
		DAORestaurante daoRestaurante = new DAORestaurante();
		try
		{
			daoUsuario.setConn(conn);
			daoRestaurante.setConn(conn);
			daoProducto.setConn(conn);
			String sql = "SELECT * FROM ORDEN_PRODUCTO ";
			if(daoRestaurante.verificarRest(usuario,clave))
				sql += "WHERE RESTAURANTE = '"+usuario+"' ";
			else if(!daoUsuario.verificar(Integer.parseInt(usuario), clave, 1))
				throw new Exception("no es un usuario valido");
			sql += "ORDER BY RESTAURANTE";
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			res = "[";
			String ant = null;
			int clientesNR = 0;
			double ventas= 0;
			ArrayList<String> productos= new ArrayList<String>();
			String restaurante = null;
			//	RFC8. CONSULTAR PEDIDOS
			//	Muestra la información consolidada de los pedidos hechos en RotondAndes. Consolida, como mínimo, para cada uno los
			//	restaurantes y para cada uno de sus productos las ventas totales (en dinero y en cantidad), lo consumidos por clientes
			//	registrados y por clientes no registrados.
			//	Esta operación es realizada por un usuario restaurante y por el administrador de RotondAndes.
			//	NOTA: Respetando la privacidad de los clientes, cuando un restaurante hace esta consulta obtiene la información de sus
			//	propias actividades, mientras que el administrador obtiene toda la información. Ver RNF1.
			while (rs.next())
			{
				restaurante = rs.getString("RESTAURANTE");
				if(ant !=null && !ant.equals(restaurante))
				{
					res +="\"productos\": [";
					for(int i = 0 ; i<productos.size();i++)
					{
						String actual = productos.get(i); 
						if(!actual.equals(""))
						{
							int cantidad =  1;
							for(int j = i+1 ; j<productos.size();j++)
							{
								String nuevo =  productos.get(j);
								if(nuevo.equals(actual))
								{
									cantidad++;
									productos.add(j, "");
								}
							}
							res+= "{\"nombre\": \""+actual+"\",";
							res+="\"unidadesVendidas\": "+cantidad+",";
							Producto prod = daoProducto.getProductoPK(actual, ant);
							if(prod == null)
							{
								Menu men = daoProducto.getMenuPK(actual, ant);
								res+="\"ventas\": "+men.getPrecio()*cantidad+"},";
								ventas+=men.getPrecio()*cantidad;
							}
							else
							{
								res+="\"ventas\": "+prod.getPrecio()*cantidad+"},";
								ventas+=prod.getPrecio()*cantidad;
							}
						}
					}
					res = res.substring(0,res.lastIndexOf(","));
					res +="], \"clientesNoRegistrados\": "+clientesNR+","; 
					res +=" \"clientesRegistrados\": "+(productos.size()-clientesNR)+","; 
					res += "\"cantidadVentas\": "+productos.size()+",";
					res += "\"valorVentas\": "+ventas+"}, ";
					res+="{\"nombre\": \""+ restaurante+"\",";
					ant = restaurante;
					clientesNR = 0;
					ventas= 0;
					productos= new ArrayList<String>();

				}
				if(ant ==null)
				{
					res+="{\"nombre\": \""+ restaurante+"\",";
					ant = restaurante;
				}
				String nombre = rs.getString("NOMBRE");
				int usua = rs.getInt("USUARIO");
				if(daoUsuario.verificar(usua, 0, 0))
					clientesNR++;
				productos.add(nombre);				
			}		
			res +="\"productos\": [";
			for(int i = 0 ; i<productos.size();i++)
			{
				String actual = productos.get(i); 
				int cantidad =  1;
				for(int j = i+1 ; j<productos.size();j++)
				{
					String nuevo =  productos.get(j);
					if(nuevo.equals(actual))
					{
						cantidad++;
						productos.add(j, "");
					}
				}
				res+= "{\"nombre\": \""+actual+"\",";
				res+="\"unidadesVendidas\": "+cantidad+",";
				Producto prod = daoProducto.getProductoPK(actual, ant);
				if(prod == null)
				{
					Menu men = daoProducto.getMenuPK(actual, ant);
					res+="\"ventas\": "+men.getPrecio()*cantidad+"},";
					ventas+=men.getPrecio()*cantidad;
				}
				else
				{
					res+="\"ventas\": "+prod.getPrecio()*cantidad+"},";
					ventas+=prod.getPrecio()*cantidad;
				}

			}
			res = res.substring(0,res.lastIndexOf(","));
			res +="], \"clientesNoRegistrados\": "+clientesNR+","; 
			res +=" \"clientesRegistrados\": "+(productos.size()-clientesNR)+","; 
			res += "\"cantidadVentas\": "+productos.size()+",";
			res += "\"valorVentas\": "+ventas+"}]";			
		}
		catch(NumberFormatException e)
		{
			throw new Exception("no es un restaurante valido");
		}
		finally
		{
			daoUsuario.cerrarRecursos();
			daoProducto.cerrarRecursos();
		}
		return res;
	}
}
