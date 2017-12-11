
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
				int tiempoPreparacion = rs.getInt("TIEMPO_PREP");
				ArrayList<Ingrediente> ingredientes =  daoIngrediente.getIngredientesProducto(nombre,restaurante);
				ArrayList<String> tipoComida = getTipoComida(nombre, restaurante);
				ArrayList<Producto> equivalencias = getEquivalencias(nombre, restaurante);
				int cantidadMaxima = rs.getInt("CANT_MAX");
				lista.add(new Producto(nombre, restaurante, costo, tipo, descripcionE, descripcionEn, tiempoPreparacion, precio, tipoComida, equivalencias, ingredientes, cantidadMaxima, null));
			}
		}
		finally
		{
			daoIngrediente.cerrarRecursos();
		}
		return lista;
	}






	public Producto getProductoPK(String nombr, String restaurant) throws SQLException, Exception 
	{
		Producto lista = null;
		DAOIngrediente daoIngrediente = new DAOIngrediente();
		try
		{
			daoIngrediente.setConn(conn);
			String sql = "SELECT * FROM PRODUCTO WHERE MENU = 1 AND NOMBRE ='"+nombr+"' AND RESTAURANTE ='"+restaurant+"'";

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
				ArrayList<Ingrediente> ingredientes =  daoIngrediente.getIngredientesProducto(nombre, restaurante);
				ArrayList<String> tipoComida = getTipoComida(nombre, restaurante);
				ArrayList<Producto> equivalencias = getEquivalencias(nombre, restaurante);
				int cantidadMaxima = rs.getInt("CANT_MAX");
				lista = (new Producto(nombre, restaurante, costo, tipo, descripcionE, descripcionEn, tiempoPreparacion, precio, tipoComida, equivalencias, ingredientes,cantidadMaxima, null));
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
				ArrayList<Ingrediente> ingredientes =  daoIngrediente.getIngredientesProducto(nombre, restaurante);
				ArrayList<String> tipoComida = getTipoComida(nombre, restaurante);
				ArrayList<Producto> equivalencias =new ArrayList<Producto>();
				int cantidadMaxima = rs.getInt("CANT_MAX");
				lista = (new Producto(nombre, restaurante, costo, tipo, descripcionE, descripcionEn, tiempoPreparacion, precio, tipoComida, equivalencias, ingredientes,cantidadMaxima, null));
			}
		}
		finally
		{
			daoIngrediente.cerrarRecursos();
		}
		return lista;
	}




	public void addProducto(Producto producto, String restaurante, int claveRestaurante) throws SQLException, Exception {

		DAORestaurante daoRestaurante = new DAORestaurante();
		try
		{
			daoRestaurante.setConn(conn);
			if(restaurante.equals(producto.getRestaurante()))
			{
				if(!daoRestaurante.verificarRest(restaurante, claveRestaurante+""))
					throw new Exception("No es un usuario valido");
			}
			else
			{
				throw new Exception("No es un usuario valido");
			}

			String sql = "INSERT INTO PRODUCTO VALUES ('";
			sql += producto.getNombre() + "','";
			sql += producto.getRestaurante() + "',";
			sql += producto.getCosto() + ",";
			sql += producto.getTipo() + ",'";
			sql += producto.getDescripcionE() + "','";
			sql += producto.getDescripcionEn() + "',";
			sql += producto.getTiempoPreparacion() + ",";
			sql += producto.getPrecio() + ",";
			sql +="1,";
			sql +="0,";
			sql += producto.getCantidadMaxima()+")";


			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();

		}
		finally
		{
			daoRestaurante.cerrarRecursos();
		}

	}

	public void updateProducto(Producto producto, String restaurante, int claveRestaurante) throws SQLException, Exception {

		DAORestaurante daoRestaurante = new DAORestaurante();
		try
		{
			daoRestaurante.setConn(conn);
			if(restaurante.equals(producto.getRestaurante()))
			{
				if(!daoRestaurante.verificarRest(restaurante, claveRestaurante+""))
					throw new Exception("No es un usuario valido");
			}
			else
			{
				throw new Exception("No es un usuario valido");
			}

			String sql = "UPDATE PRODUCTO SET ";
			sql += "NOMBRE='"+producto.getNombre() + "',";
			sql += "RESTAURANTE='"+producto.getRestaurante() + "',";
			sql += "COSTO="+producto.getCosto() + ",";
			sql += "TIPO="+producto.getTipo() + ",";
			sql += "DESCRIPCION_E='"+producto.getDescripcionE() + "',";
			sql += "DESCRIPCION_EN='"+producto.getDescripcionEn() + "',";
			sql += "TIEMPO_PREP="+producto.getTiempoPreparacion() + ",";
			sql += "PRECIO="+producto.getPrecio() + ",";
			sql += "CANT_MAX="+producto.getCantidadMaxima();
			sql += "WHERE NOMBRE ='"+producto.getNombre()+"' AND RESTAURANTE = '"+ producto.getRestaurante()+"'";


			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();

		}
		finally
		{
			daoRestaurante.cerrarRecursos();
		}

	}


	public void deleteProducto(String nombr, String restaurant, String restaurante2, int claveRestaurante) throws SQLException, Exception {

		DAORestaurante daoRestaurante = new DAORestaurante();
		try
		{
			daoRestaurante.setConn(conn);
			if(restaurant.equals(restaurante2))
			{
				if(!daoRestaurante.verificarRest(restaurante2, claveRestaurante+""))
					throw new Exception("No es un usuario valido");
			}
			else
			{
				throw new Exception("No es un usuario valido");
			}


			String sql = "DELETE FROM PRODUCTO";
			sql += " WHERE NOMBRE ='" + nombr+"' AND RESTAURANTE = '"+ restaurant+"'";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
		}
		finally
		{
			daoRestaurante.cerrarRecursos();
		}


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
			int tiempoPreparacion = rs.getInt("TIEMPO_PREP");
			int cantidadMaxima = rs.getInt("CANT_MAX");
			ArrayList<String> tipoComida = getTipoComida(nombre, restaurante);
			ArrayList<Producto> productos = getPlatos(nombre, restaurante);
			lista.add(new Menu(nombre, restaurante, costo, precio, tiempoPreparacion, tipoComida, productos, cantidadMaxima));
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
			int tiempoPreparacion = rs.getInt("TIEMPO_PREP");
			int cantidadMaxima = rs.getInt("CANT_MAX");
			ArrayList<String> tipoComida = getTipoComida(nombre, restaurante);
			ArrayList<Producto> productos = getPlatos(nombre, restaurante);
			lista=(new Menu(nombre, restaurante, costo, precio, tiempoPreparacion, tipoComida, productos, cantidadMaxima));
		}

		return lista;
	}





	public void addMenu(Menu menu, String restaurante, int claveRestaurante)  throws SQLException, Exception {

		DAORestaurante daoRestaurante = new DAORestaurante();
		try
		{
			daoRestaurante.setConn(conn);
			if(restaurante.equals(menu.getRestaurante()))
			{
				if(!daoRestaurante.verificarRest(restaurante, claveRestaurante+""))
					throw new Exception("No es un usuario valido");
			}
			else
			{
				throw new Exception("No es un usuario valido");
			}

			String sql = "INSERT INTO PRODUCTO VALUES ('";
			sql += menu.getNombre() + "','";
			sql += menu.getRestaurante() + "',";
			sql += menu.getCosto() + ",";
			sql += "NULL,";
			sql += "NULL,";
			sql += "NULL,";
			sql += menu.getTiempoPreparacion() + ",";
			sql += menu.getPrecio() + ",";
			sql +="0,";
			sql +="0,";
			sql += menu.getCantidadMaxima()+")";


			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
			addPlatos(menu);
		}
		finally
		{
			daoRestaurante.cerrarRecursos();
		}




	}

	private void addPlatos(Menu menu) throws Exception 
	{
		String sql = "INSERT INTO MENU VALUES ('";
		sql += menu.getNombre() + "',";
		if(menu.getProductos()!=null)
		{

			sql += "'"+menu.getProductos().get(0) + "',";
			sql += "'"+menu.getProductos().get(1) + "',";
			sql += "'"+menu.getProductos().get(2) + "',";
			sql += "'"+menu.getProductos().get(3) + "',";
			sql += "'"+menu.getProductos().get(4) + "','";
		}
		else
		{
			sql += "NULL,";
			sql += "NULL,";
			sql += "NULL,";
			sql += "NULL,";
			sql += "NULL,'";
		}
		sql += menu.getRestaurante() + "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}


	public void updateMenu(Menu menu, String restaurante, int claveRestaurante) throws SQLException, Exception {

		DAORestaurante daoRestaurante = new DAORestaurante();
		try
		{
			daoRestaurante.setConn(conn);
			if(restaurante.equals(menu.getRestaurante()))
			{
				if(!daoRestaurante.verificarRest(restaurante, claveRestaurante+""))
					throw new Exception("No es un usuario valido");
			}
			else
			{
				throw new Exception("No es un usuario valido");
			}

			String sql = "UPDATE PRODUCTO SET ";
			sql += "NOMBRE='"+menu.getNombre() + "',";
			sql += "RESTAURANTE='"+menu.getRestaurante() + "',";
			sql += "COSTO="+menu.getCosto() + ",";
			sql += "TIPO= NULL,";
			sql += "DESCRIPCION_E= NULL,";
			sql += "DESCRIPCION_EN= NULL,";
			sql += "TIEMPO_PREP="+menu.getTiempoPreparacion() + ",";
			sql += "PRECIO="+menu.getPrecio() + ",";
			sql += "CANT_MAX="+menu.getCantidadMaxima();
			sql += " WHERE NOMBRE ='"+menu.getNombre()+"' AND RESTAURANTE = '"+ menu.getRestaurante()+"'";
			updatePlatos(menu);


			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			System.out.println(sql);
			prepStmt.executeQuery();

		}
		finally
		{
			daoRestaurante.cerrarRecursos();
		}

	}


	private void updatePlatos(Menu menu) throws Exception {
		String sql = "UPDATE MENU SET ";
		sql += "NOMBRE='"+menu.getNombre() + "',";
		if(menu.getProductos()!=null)
		{
			sql += "ENTRADA='"+menu.getProductos().get(0) + "',";
			sql += "PLATO_FUERTE='"+menu.getProductos().get(1) + "',";
			sql += "POSTRE='"+menu.getProductos().get(2) + "',";
			sql += "BEBIDA='"+menu.getProductos().get(3) + "',";
			sql += "ACOMPAÑAMIENTO='"+menu.getProductos().get(4) + "',";	

		}
		else
		{
			sql += "ENTRADA="+"NULL,";
			sql += "PLATO_FUERTE="+"NULL,";
			sql += "POSTRE="+"NULL,";
			sql += "BEBIDA="+"NULL,";
			sql += "ACOMPAÑAMIENTO="+"NULL,";
		}

		sql += "RESTAURANTE='"+menu.getRestaurante() + "'";
		sql += "WHERE NOMBRE ='"+menu.getNombre()+"' AND RESTAURANTE = '"+ menu.getRestaurante()+"'";


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		System.out.println(sql);
		prepStmt.executeQuery();

	}


	public void deleteMenu(String nombre, String restaurante, String restaurante2, int claveRestaurante) throws SQLException, Exception {

		DAORestaurante daoRestaurante = new DAORestaurante();
		try
		{
			daoRestaurante.setConn(conn);
			if(restaurante.equals(restaurante2))
			{
				if(!daoRestaurante.verificarRest(restaurante2, claveRestaurante+""))
					throw new Exception("No es un usuario valido");
			}
			else
			{
				throw new Exception("No es un usuario valido");
			}

			String sql = "DELETE FROM PRODUCTO";
			sql += " WHERE NOMBRE ='" + nombre+"' AND RESTAURANTE = '"+ restaurante +"'";
			deletePlatos(nombre, restaurante);

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
		}
		finally
		{
			daoRestaurante.cerrarRecursos();
		}

	}


	private void deletePlatos(String nombre, String restaurante) throws SQLException {
		String sql = "DELETE FROM MENU";
		sql += " WHERE NOMBRE ='" + nombre+"' AND RESTAURANTE = '"+ restaurante+"'";

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
				int tiempoPreparacion = rs.getInt("TIEMPO_PREP");
				ArrayList<Ingrediente> ingredientes =  daoIngrediente.getIngredientesProducto(nombre, restaurante);
				ArrayList<String> tipoComida = getTipoComida(nombre, restaurante);
				ArrayList<Producto> equivalencias = getEquivalencias(nombre, restaurante);
				int cantidadMaxima = rs.getInt("CANT_MAX");
				lista.add(new Producto(nombre, restaurante, costo, tipo, descripcionE, descripcionEn, tiempoPreparacion, precio, tipoComida, equivalencias, ingredientes,cantidadMaxima, null));
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
			int tiempoPreparacion = rs.getInt("TIEMPO_PREP");
			int cantidadMaxima = rs.getInt("CANT_MAX");
			ArrayList<String> tipoComida = getTipoComida(nombre, restaurante);
			ArrayList<Producto> productos = getPlatos(nombre, restaurante);
			lista.add(new Menu(nombre, restaurante, costo, precio, tiempoPreparacion, tipoComida, productos,cantidadMaxima));
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
		String sql = "SELECT * FROM MENU WHERE NOMBRE ='" + nombre+"' AND RESTAURANTE = '"+ restaurante+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			for (int i =1; i <6; i++)
			{
				String nom = rs.getString(i+1);
				lista.add(getProductoPK(nom, restaurante));				
			}
		}

		return lista;
	}

	private int darTipo(String restaurante, String nombre) throws SQLException {
		int lista = 0;
		String sql = "SELECT TIPO FROM PRODUCTO WHERE NOMBRE ='" + nombre+"' AND RESTAURANTE = '"+ restaurante+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			lista = rs.getInt(1);
		}

		return lista;
	}

	public String addEquivalenciaProducto(int clave, String restaurante, ArrayList<String> productos) throws Exception {

		String res = "no se realizo la accion";
		int tipo = darTipo(restaurante, productos.get(0));

		String sql = "INSERT ALL ";
		for (int i = 0; i < productos.size(); i++) 
		{
			for (int j = 0; j <productos.size(); j++) 
			{
				if(i!=j)
				{
					if(tipo !=darTipo(restaurante, productos.get(j)))
						throw new Exception("Los productos no son del mismo tipo");
					sql += "INTO EQUIVALENCIA_PRODUCTO VALUES ('";
					sql += productos.get(i)+"','";
					sql += productos.get(j)+"','";
					sql += restaurante+"') ";


				}
			}
		}
		sql += " SELECT * FROM DUAL";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		res="se realizo la accion";

		return res;
	}


	public String surtirRestaurante(int clave, String restaurante)throws Exception {
		String res = "No se realizo la accion";

		String sql ="UPDATE PRODUCTO SET CANT_ACTUAL =CANT_MAX";
		sql += " WHERE RESTAURANTE = '"+ restaurante+"'";		

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		res="Se realizo la accion";

		return res;


	}


	public boolean verificarDisponibilidad(String nombre, String restaurante, String[] cambios) throws Exception {

		String sql = "SELECT * FROM PRODUCTO WHERE NOMBRE ='"+nombre+"' AND RESTAURANTE ='"+restaurante+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		if(rs.next()) {
			if(rs.getInt("CANT_ACTUAL")<=0)
				return false;
			if (rs.getInt("MENU")==0)
			{
				return verificarDisponibilidadPlatos(nombre,restaurante,cambios);
			}
		}
		else
			return false;
		return true;
	}


	private boolean verificarDisponibilidadPlatos(String nombre, String restaurante, String[] cambios) throws Exception {

		String sql = "SELECT * FROM MENU WHERE NOMBRE ='" + nombre+"' AND RESTAURANTE = '"+ restaurante+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		try {
			while (rs.next()) {
				for (int i =1; i <6; i++)
				{
					String nom = rs.getString(i+1);
					if(!cambios[i].equals("")) {
						verificarEquivalencia(nom,cambios[i],restaurante);
						nom = cambios[i];					
					}
					if(!verificarDisponibilidad(nom, restaurante, null))
						return false;
				}
				return true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return false;
	}


	private void verificarEquivalencia(String nom,String equi, String restaurante)throws Exception 
	{
		String sql = "SELECT * FROM EQUIVALENCIA_PRODUCTO WHERE NOMBRE ='" + nom+"' AND RESTAURANTE = '"+ restaurante+"'  AND NOMBRE_EQUIVALENCIA ='" +equi+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(!rs.next()) {
			throw new Exception("no es una equivalencia");
		}
	}


	public void restarUnidad(String nombre, String restaurante, String[] cambios) throws Exception 
	{
		String sql = "UPDATE PRODUCTO SET ";
		sql += "CANT_ACTUAL="+ "CANT_ACTUAL -1" + " ";
		sql += "WHERE NOMBRE ='"+nombre+"' AND RESTAURANTE ='"+restaurante+"'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		ArrayList<Producto> platos = getPlatos(nombre, restaurante);
		for (int i = 0; i < platos.size(); i++) 
		{
			Producto plato = platos.get(i);
			if(plato!= null)
			{
				String nom = plato.getNombre();
				if(!cambios[i+1].equals("") && cambios.length>1)
					nom =cambios[i+1];
				restarUnidad(nom, plato.getRestaurante(), null);				
			}
		}		
	}


	public void retirarRestaurante(String restaurante) throws SQLException {
		String sql = "UPDATE PRODUCTO SET ";
		sql += "NO_VALIDO="+ 0;
		sql += " WHERE RESTAURANTE ='"+restaurante+"'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();		
	}





}
