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

import vos.*;


public class DAORestaurante {


	private ArrayList<Object> recursos;


	private Connection conn;


	public DAORestaurante() {
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


	public ArrayList<Restaurante> getRestaurantes() throws SQLException, Exception 
	{
		ArrayList<Restaurante> lista = new ArrayList<Restaurante>();
		DAOUsuario daoUsuario = new DAOUsuario();
		DAOProducto daoProducto = new DAOProducto();
		try
		{
			daoUsuario.setConn(conn);
			daoProducto.setConn(conn);
			String sql = "SELECT * FROM RESTAURANTE";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				String nombre =rs.getString("NOMBRE") ;
				String tipoComida =rs.getString("TIPO_COMIDA") ;
				String web =rs.getString("WEB") ;
				int zona = rs.getInt("ZONA");
				
				Administrador administrador =  daoUsuario.getAdministradorPK(rs.getInt("ID_ADMIN"));
				ArrayList<Producto> productos = daoProducto.getProductosRestaurante(nombre);
				ArrayList<Menu> menus = daoProducto.getMenusRestaurante(nombre);
				lista.add(new Restaurante(nombre, tipoComida, web, zona, administrador, productos, menus));
			}
		}
		finally
		{
			daoUsuario.cerrarRecursos();
			daoProducto.cerrarRecursos();
		}
		return lista;
	}


	public Restaurante getRestaurantePK(String PK) throws SQLException, Exception 
	{
		Restaurante lista = null;
		DAOUsuario daoUsuario = new DAOUsuario();
		DAOProducto daoProducto = new DAOProducto();
		try
		{
			daoUsuario.setConn(conn);
			daoProducto.setConn(conn);
			String sql = "SELECT * FROM RESTAURANTE WHERE NOMBRE ='"+PK+"'";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				String nombre =rs.getString("NOMBRE") ;
				String tipoComida =rs.getString("TIPO_COMIDA") ;
				String web =rs.getString("WEB") ;
				int zona = rs.getInt("ZONA");
				
				Administrador administrador =  daoUsuario.getAdministradorPK(rs.getInt("ID_ADMIN"));
				ArrayList<Producto> productos = daoProducto.getProductosRestaurante(nombre);
				ArrayList<Menu> menus = daoProducto.getMenusRestaurante(nombre);
				lista=(new Restaurante(nombre, tipoComida, web, zona, administrador, productos, menus));
			}
		}
		finally
		{
			daoUsuario.cerrarRecursos();
			daoProducto.cerrarRecursos();
		}
		return lista;
	}

	

	
	public void addRestaurante(Restaurante restaurante) throws SQLException, Exception {

		String sql = "INSERT INTO RESTAURANTE VALUES ('";
		sql += restaurante.getNombre() + "','";
		sql += restaurante.getTipoComida() + "','";
		sql += restaurante.getWeb() + "',";
		sql += restaurante.getZona() + ",";
		sql += restaurante.getAdministrador() + "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	public void updateRestaurante(Restaurante restaurante) throws SQLException, Exception {

		String sql = "UPDATE RESTAURANTE SET ";
		sql += "NOMBRE='"+restaurante.getNombre() + "',";
		sql += "TIPO_COMIDA='"+restaurante.getTipoComida() + "',";
		sql += "WEB='"+restaurante.getWeb() + "',";
		sql +="ZONA=" +restaurante.getZona() + ",";
		sql += "ID_ADMIN="+restaurante.getAdministrador();
		sql += "WHERE NOMBRE = " + restaurante.getNombre();


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	public void deleteRestaurante(Restaurante Restaurantena) throws SQLException, Exception {

		String sql = "DELETE FROM RESTAURANTE";
		sql += " WHERE NOMBRE = '" + Restaurantena.getNombre()+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	public ArrayList<Restaurante> getRestaurantesZona(int id) throws Exception {
		ArrayList<Restaurante> lista = new ArrayList<Restaurante>();
		DAOUsuario daoUsuario = new DAOUsuario();
		DAOProducto daoProducto = new DAOProducto();
		try
		{
			daoUsuario.setConn(conn);
			daoProducto.setConn(conn);
			String sql = "SELECT * FROM RESTAURANTE WHERE ZONA ='"+id+"'";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				String nombre =rs.getString("NOMBRE") ;
				String tipoComida =rs.getString("TIPO_COMIDA") ;
				String web =rs.getString("WEB") ;
				int zona = rs.getInt("ZONA");
				
				Administrador administrador =  daoUsuario.getAdministradorPK(rs.getInt("ID_ADMIN"));
				ArrayList<Producto> productos = daoProducto.getProductosRestaurante(nombre);
				ArrayList<Menu> menus = daoProducto.getMenusRestaurante(nombre);
				lista.add(new Restaurante(nombre, tipoComida, web, zona, administrador, productos, menus));
			}
		}
		finally
		{
			daoUsuario.cerrarRecursos();
			daoProducto.cerrarRecursos();
		}
		return lista;
	}

}
