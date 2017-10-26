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


public class DAOIngrediente {


	private ArrayList<Object> recursos;


	private Connection conn;


	public DAOIngrediente() {
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


	public ArrayList<Ingrediente> getIngredientes() throws SQLException, Exception 
	{
		ArrayList<Ingrediente> lista = new ArrayList<Ingrediente>();
		String sql = "SELECT * FROM INGREDIENTE";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			String descripcionE = rs.getString("DESCRIPCION_E");
			String descripcionEn = rs.getString("DESCRIPCION_EN");
			ArrayList<String> equivalencias = getEquivalencias(nombre);
			lista.add(new Ingrediente(nombre, descripcionE, descripcionEn, equivalencias));
		}

		return lista;
	}


	public Ingrediente getIngredientePK(String PK) throws SQLException, Exception 
	{
		Ingrediente lista = null;
		String sql = "SELECT * FROM INGREDIENTE WHERE NOMBRE = '"+PK+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			String descripcionE = rs.getString("DESCRIPCION_E");
			String descripcionEn = rs.getString("DESCRIPCION_EN");
			ArrayList<String> equivalencias = getEquivalencias(nombre);
			lista=(new Ingrediente(nombre, descripcionE, descripcionEn, equivalencias));
		}

		return lista;
	}




	public void addIngrediente(Ingrediente ingrediente) throws SQLException, Exception {

		String sql = "INSERT INTO INGREDIENTE VALUES ('";
		sql += ingrediente.getNombre() + "','";
		sql += ingrediente.getDescripcionE() + "','";
		sql += ingrediente.getDescripcionEn() + "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	public void deleteIngrediente(String nombre) throws SQLException, Exception {

		String sql = "DELETE FROM INGREDIENTE";
		sql += " WHERE ID = " + nombre;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	public ArrayList<Ingrediente> getIngredientesProducto(String nombre, String restaurante) throws Exception{
		ArrayList<Ingrediente> lista = new ArrayList<Ingrediente>();
		String sql = "SELECT * FROM INGREDIENTE_PRODUCTO WHERE ID_PRODUCTO = '"+nombre+"' AND RESTAURANTE = '"+restaurante+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			lista.add(getIngredientePK(rs.getString("ID_INGREDIENTE")));
		}

		return lista;
	}


	private ArrayList<String> getEquivalencias(String nombre) throws Exception{
		ArrayList<String> lista = new ArrayList<String>();
		String sql = "SELECT * FROM EQUIVALENCIA_INGREDIENTES WHERE NOMBRE='"+ nombre+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			lista.add(rs.getString("NOMBRE_EQUIVALENCIA"));
		}

		return lista;
	}

}
