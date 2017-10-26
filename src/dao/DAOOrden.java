/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: VideoAndes
 * Autor: Juan Felipe García - jf.garcia268@uniandes.edu.co
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
			lista.add(new Orden(mesa, fecha, usuarios, menus, productos));
		}

		return lista;
	}


	public Orden getOrdenPK(int mesa, Date fecha) throws SQLException, Exception 
	{
		Orden lista = null;		
		long f = fecha.getTime();
		ArrayList<Integer> usuarios = getUsuariosOrden(mesa, f);
		ArrayList<Menu> menus= getMenusOrden(mesa,f);
		ArrayList<Producto> productos = getProductosOrden(mesa,f);
		if(usuarios!= null && menus!= null && productos!= null)
			lista = new Orden(mesa, fecha, usuarios, menus, productos);
		return lista;
	}




	public void addOrden(Orden orden) throws SQLException, Exception {

		String sql = "INSERT INTO ORDEN VALUES (";
		sql += orden.getFecha().getTime() + ",";
		sql += orden.getMesa() + ")";
		

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		addUsuarios(orden);
		addMenus(orden);
		addProductos(orden);

	}

	public void updateOrden(Orden orden) throws SQLException, Exception {

		deleteUsuarios(orden.getMesa(), orden.getFecha().getTime());
		deleteMenusYProductos(orden.getMesa(), orden.getFecha().getTime());
		addUsuarios(orden);
		addMenus(orden);
		addProductos(orden);
		
	}


	public void deleteOrden(int mesa, Date fecha) throws SQLException, Exception {

		String sql = "DELETE FROM ORDEN";
		sql += " WHERE MESA= " + mesa+ " AND FECHA = " + fecha.getTime();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
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
				if(menu!= null)	
					lista.add(menu);
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
				if(producto!= null)	
					lista.add(producto);
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
			String sql = "INSERT INTO ORDEN_USUARIOS VALUES (";
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
			String sql = "INSERT INTO ORDEN_PRODUCTOS VALUES ('";
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
			String sql = "INSERT INTO ORDEN_PRODUCTOS VALUES ('";
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
		String sql = "DELETE FROM ORDEN_USUARIOS";
		sql += " WHERE MESA = "+mesa+" AND FECHA = "+f;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	private void deleteMenusYProductos(int mesa, long f)throws Exception 
	{
		String sql = "DELETE FROM ORDEN_PRODUCTOS";
		sql += " WHERE MESA = "+mesa+" AND FECHA = "+f;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
