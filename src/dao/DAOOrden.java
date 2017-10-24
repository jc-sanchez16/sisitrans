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
			int f = rs.getInt("FECHA");
			Date fecha = new Date(f);
			ArrayList<Integer> usuarios = getUsuariosOrden(mesa, f);
			ArrayList<Menu> menus= getMenusOrden(mesa,f);
			ArrayList<Producto> productos = getProductosOrden(mesa,f);
			lista.add(new Orden(mesa, fecha, usuarios, menus, productos));
		}

		return lista;
	}


	public Zona getZonaPK(int PK) throws SQLException, Exception 
	{
		Zona lista = null;
		DAOReserva daoReserva = new DAOReserva();
		DAORestaurante daoRestaurante = new DAORestaurante();
		try
		{
			daoReserva.setConn(conn);
			daoRestaurante.setConn(conn);
			String sql = "SELECT * FROM ZONA WHERE ID = "+PK;

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID");
				boolean abierto =((rs.getInt("ABIERTO") == 0) ? true:false);
				int capacidad = rs.getInt("CAPACIDAD");
				boolean discapacitados =((rs.getInt("DISCAPACITADOS") == 0) ? true:false);
				String especialidad =rs.getString("ESPECIALIDAD") ;
				ArrayList<Reserva> reservas =  daoReserva.getReservasZona(id);
				ArrayList<String> condiciones = getCondiciones(id);
				ArrayList<Restaurante> restaurantes = daoRestaurante.getRestaurantesZona(id);
				String name  rs.getString("NAME");
				lista=(new Zona(id, abierto, capacidad, discapacitados, especialidad, reservas, condiciones, restaurantes));
			}
		}
		finally
		{
			daoReserva.cerrarRecursos();
			daoRestaurante.cerrarRecursos();
		}
		return lista;
	}




	public void addZona(Zona zona) throws SQLException, Exception {

		String sql = "INSERT INTO ZONA VALUES (";
		sql += zona.getId() + ",";
		sql += zona.getAbierto() + ",";
		sql += zona.getCapacidad() + ",";
		sql += zona.getDiscapacitados() + ",'";
		sql += zona.getEspecialidad() + "')";

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


	public ArrayList<Orden> getOrdenesUsuario(int id) {
		// TODO Auto-generated method stub
		return null;
	}


	private ArrayList<Integer> getUsuariosOrden(int mesa, int f) {
		// TODO Auto-generated method stub
		return nul;
	}


	private ArrayList<Menu> getMenusOrden(int mesa, int f) {
		// TODO Auto-generated method stub
		return nul;
	}


	private ArrayList<Producto> getProductosOrden(int mesa, int f) {
		// TODO Auto-generated method stub
		return nul;
	}

}
