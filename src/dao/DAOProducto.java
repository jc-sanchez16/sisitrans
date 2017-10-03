
package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;


public class DAOProducto {


	private ArrayList<Object> recursos;


	private Connection conn;


	public DAOProducto() {
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


	public ArrayList<Producto> getProductos() throws SQLException, Exception 
	{
		ArrayList<Producto> lista = new ArrayList<Producto>();
		DAOIngrediente daoIngrediente = new DAOIngrediente();
		try
		{
			daoIngrediente.setConn(conn);
			String sql = "SELECT * FROM PRODUCTO WHERE MENU = 1";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				String nombre = rs.getString("NOMBRE");
				String restaurante = rs.getString("RESTAURANTE");
				double costo = rs.getDouble("COSTO");
				double precio = rs.getDouble("PRECIO");
				int tipo = rs.getInt("TIPO");
				String descripcionE = rs.getString("DESCRIPCION_E");
				String descripcionEn = rs.getString("DESCRIPCION_EN");
				int tiempoPreparacion = rs.getInt("TIEMPO_PREPARACION");
				ArrayList<Ingrediente> ingredientes =  daoIngrediente.getIngredientesProducto();
				ArrayList<String> tipoComida = getTipoComida(nombre, restaurante);
				ArrayList<Producto> equivalencias = getEquivalencias(nombre, restaurante);
				lista.add(new Producto(nombre, restaurante, costo, tipo, descripcionE, descripcionEn, tiempoPreparacion, precio, tipoComida, equivalencias, ingredientes));
			}
		}
		finally
		{
			daoIngrediente.cerrarRecursos();
		}
		return lista;
	}






	public Producto getProductoPK(String PK1, String PK2) throws SQLException, Exception 
	{
		Producto lista = null;
		DAOIngrediente daoIngrediente = new DAOIngrediente();
		try
		{
			daoIngrediente.setConn(conn);
			String sql = "SELECT * FROM PRODUCTO WHERE MENU = 1 AND NOMBRE ='"+PK1+"' AND RESTAURANTE = '"+ PK2+"'";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				String nombre = rs.getString("NOMBRE");
				String restaurante = rs.getString("RESTAURANTE");
				double costo = rs.getDouble("COSTO");
				double precio = rs.getDouble("PRECIO");
				int tipo = rs.getInt("TIPO");
				String descripcionE = rs.getString("DESCRIPCION_E");
				String descripcionEn = rs.getString("DESCRIPCION_EN");
				int tiempoPreparacion = rs.getInt("TIEMPO_PREP");
				ArrayList<Ingrediente> ingredientes =  daoIngrediente.getIngredientesProducto();
				ArrayList<String> tipoComida = getTipoComida(nombre, restaurante);
				ArrayList<Producto> equivalencias = getEquivalencias(nombre, restaurante);
				lista = (new Producto(nombre, restaurante, costo, tipo, descripcionE, descripcionEn, tiempoPreparacion, precio, tipoComida, equivalencias, ingredientes));
			}
		}
		finally
		{
			daoIngrediente.cerrarRecursos();
		}
		return lista;
	}
	public Producto getProductoPKSE(String PK1, String PK2) throws SQLException, Exception 
	{
		Producto lista = null;
		DAOIngrediente daoIngrediente = new DAOIngrediente();
		try
		{
			daoIngrediente.setConn(conn);
			String sql = "SELECT * FROM PRODUCTO WHERE MENU = 1 AND NOMBRE ='"+PK1+"' AND RESTAURANTE = '"+ PK2+"'";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				String nombre = rs.getString("NOMBRE");
				String restaurante = rs.getString("RESTAURANTE");
				double costo = rs.getDouble("COSTO");
				double precio = rs.getDouble("PRECIO");
				int tipo = rs.getInt("TIPO");
				String descripcionE = rs.getString("DESCRIPCION_E");
				String descripcionEn = rs.getString("DESCRIPCION_EN");
				int tiempoPreparacion = rs.getInt("TIEMPO_PREP");
				ArrayList<Ingrediente> ingredientes =  daoIngrediente.getIngredientesProducto();
				ArrayList<String> tipoComida = getTipoComida(nombre, restaurante);
				ArrayList<Producto> equivalencias =new ArrayList<Producto>();
				lista = (new Producto(nombre, restaurante, costo, tipo, descripcionE, descripcionEn, tiempoPreparacion, precio, tipoComida, equivalencias, ingredientes));
			}
		}
		finally
		{
			daoIngrediente.cerrarRecursos();
		}
		return lista;
	}

	

	
	public void addProducto(Producto producto) throws SQLException, Exception {

		String sql = "INSERT INTO PRODUCTO VALUES ('";
		sql += producto.getNombre() + "','";
		sql += producto.getRestaurante() + "',";
		sql += producto.getCosto() + ",";
		sql += producto.getTipo() + ",'";
		sql += producto.getDescripcionE() + "','";
		sql += producto.getDescripcionEn() + "',";
		sql += producto.getTiempoPreparacion() + ",";
		sql += producto.getPrecio() + ",";
		sql +="1)";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	public void updateProducto(Producto producto) throws SQLException, Exception {

		String sql = "UPDATE ZONA SET ";
		sql += "NOMBRE='"+producto.getNombre() + "',";
		sql += "RESTAURANTE'"+producto.getRestaurante() + "',";
		sql += "COSTO="+producto.getCosto() + ",";
		sql += "TIPO="+producto.getTipo() + ",";
		sql += "DESCRIPCION_E='"+producto.getDescripcionE() + "',";
		sql += "DESCRIPCIPN_EN='"+producto.getDescripcionEn() + "',";
		sql += "TIEMPO_PREP="+producto.getTiempoPreparacion() + ",";
		sql += "PRECIO="+producto.getPrecio() + ",";
		sql += "WHERE NOMBRE ='"+producto.getNombre()+"' AND RESTAURANTE = '"+ producto.getRestaurante()+"'";


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	public void deleteProducto(Producto producto) throws SQLException, Exception {

		String sql = "DELETE FROM ZONA";
		sql += " WHERE NOMBRE ='" + producto.getNombre()+"' AND RESTAURANTE = '"+ producto.getRestaurante()+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	public ArrayList<Menu> getMenus() throws SQLException, Exception 
	{
		ArrayList<Menu> lista = new ArrayList<Menu>();
			String sql = "SELECT * FROM PRODUCTO WHERE MENU = 0";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				String nombre = rs.getString("NOMBRE");
				String restaurante = rs.getString("RESTAURANTE");
				double costo = rs.getDouble("COSTO");
				double precio = rs.getDouble("PRECIO");
				int tiempoPreparacion = rs.getInt("TIEMPO_PREPARACION");
				ArrayList<String> tipoComida = getTipoComida(nombre, restaurante);
				ArrayList<Producto> productos = getPlatos(nombre, restaurante);
				lista.add(new Menu(nombre, restaurante, costo, precio, tiempoPreparacion, tipoComida, productos));
			}
	
		return lista;
	}
	public Menu getMenuPK(String PK1, String PK2) throws SQLException, Exception 
	{
		Menu lista = null;
			String sql = "SELECT * FROM PRODUCTO WHERE MENU = 0 AND NOMBRE ='"+PK1+"' AND RESTAURANTE = '"+ PK2+"'";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				String nombre = rs.getString("NOMBRE");
				String restaurante = rs.getString("RESTAURANTE");
				double costo = rs.getDouble("COSTO");
				double precio = rs.getDouble("PRECIO");
				int tiempoPreparacion = rs.getInt("TIEMPO_PREPARACION");
				ArrayList<String> tipoComida = getTipoComida(nombre, restaurante);
				ArrayList<Producto> productos = getPlatos(nombre, restaurante);
				lista=(new Menu(nombre, restaurante, costo, precio, tiempoPreparacion, tipoComida, productos));
			}
	
		return lista;
	}

	

	
	public void addMenu(Menu menu)  throws SQLException, Exception {

		String sql = "INSERT INTO PRODUCTO VALUES ('";
		sql += menu.getNombre() + "','";
		sql += menu.getRestaurante() + "',";
		sql += menu.getCosto() + ",";
		sql += "NULL,";
		sql += "NULL,'";
		sql += "NULL,";
		sql += menu.getTiempoPreparacion() + ",";
		sql += menu.getPrecio() + ",";
		sql +="0)";
		addPlatos(menu);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	private void addPlatos(Menu menu) throws Exception 
	{
		String sql = "INSERT INTO MENU VALUES ('";
		sql += menu.getNombre() + "',";
		sql += menu.getProductos().get(0) + ",";
		sql += menu.getProductos().get(1) + ",";
		sql += menu.getProductos().get(2) + ",";
		sql += menu.getProductos().get(3) + ",";
		sql += menu.getProductos().get(4) + ",'";
		sql += menu.getRestaurante() + "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
	}


	public void updateMenu(Menu menu) throws SQLException, Exception {

		String sql = "UPDATE PRODUCTO SET ";
		sql += "NOMBRE="+menu.getNombre() + "',";
		sql += "RESTAURANTE"+menu.getRestaurante() + "',";
		sql += "COSTO="+menu.getCosto() + ",";
		sql += "TIPO= NULL,";
		sql += "DESCRIPCION_E= NULL,";
		sql += "DESCRIPCIPN_EN= NULL,";
		sql += "TIEMPO_PREP="+menu.getTiempoPreparacion() + ",";
		sql += "PRECIO="+menu.getPrecio() + ",";
		sql += "WHERE NOMBRE ='"+menu.getNombre()+"' AND RESTAURANTE = '"+ menu.getRestaurante()+"'";
		updatePlatos(menu);


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	private void updatePlatos(Menu menu) throws Exception {
		String sql = "UPDATE MENU SET ";
		sql += "NOMBRE="+menu.getNombre() + "',";
		sql += "COSTO="+menu.getProductos().get(0) + ",";
		sql += "COSTO="+menu.getProductos().get(1) + ",";
		sql += "COSTO="+menu.getProductos().get(2) + ",";
		sql += "COSTO="+menu.getProductos().get(3) + ",";
		sql += "COSTO="+menu.getProductos().get(4) + ",";		
		sql += "RESTAURANTE'"+menu.getRestaurante() + "',";
		sql += "WHERE NOMBRE ='"+menu.getNombre()+"' AND RESTAURANTE = '"+ menu.getRestaurante()+"'";


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
	}


	public void deleteMenu(Menu menu) throws SQLException, Exception {

		String sql = "DELETE FROM PRODUCTO";
		sql += " WHERE NOMBRE ='" + menu.getNombre()+"' AND RESTAURANTE = '"+ menu.getRestaurante()+"'";
		deletePlatos(menu);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	private void deletePlatos(Menu menu) throws SQLException {
		String sql = "DELETE FROM MENU";
		sql += " WHERE NOMBRE ='" + menu.getNombre()+"' AND RESTAURANTE = '"+ menu.getRestaurante()+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
	}


	public ArrayList<Producto> getProductosRestaurante(String nombrer) throws Exception {
		ArrayList<Producto> lista = new ArrayList<Producto>();
		DAOIngrediente daoIngrediente = new DAOIngrediente();
		try
		{
			daoIngrediente.setConn(conn);
			String sql = "SELECT * FROM PRODUCTO WHERE MENU = 1 AND RESTAURANTE='" + nombrer+"'";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				String nombre = rs.getString("NOMBRE");
				String restaurante = rs.getString("RESTAURANTE");
				double costo = rs.getDouble("COSTO");
				double precio = rs.getDouble("PRECIO");
				int tipo = rs.getInt("TIPO");
				String descripcionE = rs.getString("DESCRIPCION_E");
				String descripcionEn = rs.getString("DESCRIPCION_EN");
				int tiempoPreparacion = rs.getInt("TIEMPO_PREPARACION");
				ArrayList<Ingrediente> ingredientes =  daoIngrediente.getIngredientesProducto();
				ArrayList<String> tipoComida = getTipoComida(nombre, restaurante);
				ArrayList<Producto> equivalencias = getEquivalencias(nombre, restaurante);
				lista.add(new Producto(nombre, restaurante, costo, tipo, descripcionE, descripcionEn, tiempoPreparacion, precio, tipoComida, equivalencias, ingredientes));
			}
		}
		finally
		{
			daoIngrediente.cerrarRecursos();
		}
		return lista;
	}


	public ArrayList<Menu> getMenusRestaurante(String nombrer) throws Exception {
		ArrayList<Menu> lista = new ArrayList<Menu>();
		String sql = "SELECT * FROM PRODUCTO WHERE MENU = 0 AND RESTAURANTE='" + nombrer+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			String restaurante = rs.getString("RESTAURANTE");
			double costo = rs.getDouble("COSTO");
			double precio = rs.getDouble("PRECIO");
			int tiempoPreparacion = rs.getInt("TIEMPO_PREPARACION");
			ArrayList<String> tipoComida = getTipoComida(nombre, restaurante);
			ArrayList<Producto> productos = getPlatos(nombre, restaurante);
			lista.add(new Menu(nombre, restaurante, costo, precio, tiempoPreparacion, tipoComida, productos));
		}

	return lista;
	}

	private ArrayList<String> getTipoComida(String nombre, String restaurante) throws SQLException {
		ArrayList<String> lista = new ArrayList<String>();
		String sql = "SELECT * FROM TIPO_COMIDA WHERE NOMBRE ='" + nombre+"' AND RESTAURANTE = '"+ restaurante+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			lista.add(rs.getString("TIPO"));
		}

	return lista;
	}
	private ArrayList<Producto> getEquivalencias(String nombre, String restaurante) throws Exception {
		ArrayList<Producto> lista = new ArrayList<Producto>();
			String sql = "SELECT * FROM EQUIVALENCIA_PRODUCTO WHERE NOMBRE ='" + nombre+"' AND RESTAURANTE = '"+ restaurante+"'";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				lista.add(getProductoPKSE(rs.getString("NOMBRE_EQUIVALENCIA"), restaurante));
			}
		
		return lista;
	}
	private ArrayList<Producto> getPlatos(String nombre, String restaurante) throws Exception 
	{
		ArrayList<Producto> lista = new ArrayList<Producto>();
		String sql = "SELECT * FROM EQUIVALENCIA_PRODUCTO WHERE NOMBRE ='" + nombre+"' AND RESTAURANTE = '"+ restaurante+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			for (int i = 2; i < 7; i++)
			{
				String nom = rs.getString(i);
				if(nom!= "null")
					lista.add(getProductoPK(nom, restaurante));	
				else
					lista.add(null);
			}
		}
	
	return lista;
	}
}
