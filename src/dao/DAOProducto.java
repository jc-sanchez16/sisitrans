
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
				int tiempoPreparacion = rs.getInt("TIEMPO_PREPARACION");
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

	

	
	public void addZona(Producto producto) throws SQLException, Exception {

		String sql = "INSERT INTO PRODUCTO VALUES ('";
		sql += producto.getNombre() + "','";
		sql += producto.getRestaurante() + "',";
		sql += producto.getCosto() + ",";
		sql += producto.getTipo() + ",'";
		sql += producto.getCosto() + ",";
		sql += producto.getCosto() + ",";
		sql += producto.getCosto() + ",";
		sql += producto.getDiscapacitados() + ",'";
		sql += producto.getEspecialidad() + "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	public void updateZona(Zona zona) throws SQLException, Exception {

		String sql = "UPDATE ZONA SET ";
		sql += "ID="+zona.getId() + ",";
		sql += "ABIERTO="+zona.getAbierto() + ",";
		sql += "CAPACIDAD="+zona.getCapacidad() + ",";
		sql += "DISCAPACITADOS="+zona.getDiscapacitados() + ",";
		sql += "ESPECIALIDAD='"+zona.getEspecialidad() ;
		sql += "' WHERE ID = " + zona.getId();


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	public void deleteZona(Zona zona) throws SQLException, Exception {

		String sql = "DELETE FROM ZONA";
		sql += " WHERE ID = " + zona.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	public ArrayList<Producto> getProductosRestaurante(String nombre) {
		// TODO Auto-generated method stub
		return nl;
	}


	public ArrayList<Menu> getMenusRestaurante(String nombre) {
		// TODO Auto-generated method stub
		return nl;
	}

	private ArrayList<String> getTipoComida(String nombre, String restaurante) {
		// TODO Auto-generated method stub
		return nll;
	}
	private ArrayList<Producto> getEquivalencias(String nombre, String restaurante) {
		// TODO Auto-generated method stub
		return nll;
	}
}
