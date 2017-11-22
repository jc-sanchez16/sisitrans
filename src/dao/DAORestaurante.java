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

				int administrador = rs.getInt("REPRESENTANTE");
				ArrayList<Producto> productos = daoProducto.getProductosRestaurante(nombre);
				ArrayList<Menu> menus = daoProducto.getMenusRestaurante(nombre);
				ArrayList<Contrato> contratos= getContratos(nombre);
				lista.add(new Restaurante(nombre, tipoComida, web, zona, administrador, contratos, productos, menus));
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

				int administrador =  rs.getInt("REPRESENTANTE");
				ArrayList<Producto> productos = daoProducto.getProductosRestaurante(nombre);
				ArrayList<Menu> menus = daoProducto.getMenusRestaurante(nombre);
				ArrayList<Contrato> contratos= getContratos(nombre);
				lista=(new Restaurante(nombre, tipoComida, web, zona, administrador,contratos, productos, menus));
			}
		}
		finally
		{
			daoUsuario.cerrarRecursos();
			daoProducto.cerrarRecursos();
		}
		return lista;
	}




	public void addRestaurante(Restaurante restaurante,int clave, int usuario, int contraseñaAd) throws SQLException, Exception {

		DAOUsuario daoUsuario = new DAOUsuario();
		
		try
		{
			daoUsuario.setConn(conn);
			if(!daoUsuario.verificar(usuario, contraseñaAd, DAOUsuario.ADMINISTRADOR))
				throw new Exception("No es un usuario valido");
			String sql = "INSERT INTO RESTAURANTE VALUES ('";
			sql += restaurante.getNombre() + "',";
			sql += restaurante.getZona() + ",'";
			sql += restaurante.getTipoComida() + "','";
			sql += restaurante.getWeb() + "',";
			sql += clave + ",";
			sql += restaurante.getAdministrador() + ")";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();

		}
		finally
		{
			daoUsuario.cerrarRecursos();
		}

	}

	public void updateRestaurante(Restaurante restaurante, int usuario, int contraseñaAd) throws SQLException, Exception {

		DAOUsuario daoUsuario = new DAOUsuario();
		try
		{
			daoUsuario.setConn(conn);
			if(!daoUsuario.verificar(usuario, contraseñaAd, DAOUsuario.ADMINISTRADOR))
				throw new Exception("No es un usuario valido");
			
			String sql = "UPDATE RESTAURANTE SET ";
			sql += "NOMBRE='"+restaurante.getNombre() + "',";
			sql += "TIPO_COMIDA='"+restaurante.getTipoComida() + "',";
			sql += "WEB='"+restaurante.getWeb() + "',";
			sql +="ZONA=" +restaurante.getZona() + ",";
			sql += "REPRESENTANTE="+restaurante.getAdministrador();
			sql += "WHERE NOMBRE = '" + restaurante.getNombre()+"'";


			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
			
		}
		finally
		{
			daoUsuario.cerrarRecursos();
		}
		
	}


	public void deleteRestaurante(String nombre, int usuario, int contraseñaAd) throws SQLException, Exception {

		DAOUsuario daoUsuario = new DAOUsuario();
		try
		{
			daoUsuario.setConn(conn);
			if(!daoUsuario.verificar(usuario, contraseñaAd, DAOUsuario.ADMINISTRADOR))
				throw new Exception("No es un usuario valido");
			
			String sql = "DELETE FROM RESTAURANTE";
			sql += " WHERE NOMBRE = '" + nombre+"'";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
			
		}
		finally
		{
			daoUsuario.cerrarRecursos();
		}
		
		
		
	}


	public ArrayList<Restaurante> getRestaurantesZona(int id) throws Exception {
		ArrayList<Restaurante> lista = new ArrayList<Restaurante>();
		DAOProducto daoProducto = new DAOProducto();
		try
		{
			String sql = "SELECT * FROM RESTAURANTE WHERE ZONA ='"+id+"'";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				String nombre =rs.getString("NOMBRE") ;
				String tipoComida =rs.getString("TIPO_COMIDA") ;
				String web =rs.getString("WEB") ;
				int zona = rs.getInt("ZONA");

				int administrador = rs.getInt("REPRESENTANTE");
				daoProducto.setConn(conn);
				ArrayList<Producto> productos = daoProducto.getProductosRestaurante(nombre);
				ArrayList<Menu> menus = daoProducto.getMenusRestaurante(nombre);
				daoProducto.cerrarRecursos();
				ArrayList<Contrato> contratos= getContratos(nombre);
				lista.add(new Restaurante(nombre, tipoComida, web, zona, administrador, contratos, productos, menus));
			}
		}
		finally
		{
			daoProducto.cerrarRecursos();
		}
		return lista;
	}
	
	public boolean verificarRest(String restaurante, int clave) throws SQLException {

		String sql = "SELECT CLAVE FROM RESTAURANTE WHERE NOMBRE ='"+restaurante+"' AND CLAVE="+clave;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return rs.next();
	}
	

	public boolean verificar(int clave) throws SQLException {

		String sql = "SELECT CLAVE FROM RESTAURANTE";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {

			if(rs.getInt(1)==clave)
				return true;
		}

		return false;
	}

	private ArrayList<Contrato> getContratos(String nombre) throws SQLException {
		ArrayList<Contrato> lista = new ArrayList<Contrato>();
		String sql = "SELECT * FROM CONTRATOS";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id = rs.getInt("NUM_CONTRATO");
			String descripcion = rs.getString("DESCRIPCION");
			String restaurante =rs.getString("RESTAURANTE") ;
			lista.add(new Contrato(id, descripcion, restaurante));
		}
		return lista;
	}

	public boolean verificar(String nombre, int clave, int representante) throws SQLException {

		String sql = "SELECT CLAVE FROM RESTAURANTE WHERE NOMBRE ='"+nombre+"' AND CLAVE = "+clave+" AND REPRESENTANTE ="+representante;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return rs.next();
	}

}
