
package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import vos.*;


public class DAOReserva {


	private ArrayList<Object> recursos;


	private Connection conn;


	public DAOReserva() {
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


	public ArrayList<Reserva> getReservas() throws SQLException, Exception 
	{
		ArrayList<Reserva> lista = new ArrayList<Reserva>();
		DAOProducto daoProducto = new DAOProducto();
		try
		{
			daoProducto.setConn(conn);
			String sql = "SELECT * FROM RESERVA";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int invitados = rs.getInt("INVITADOS");
				Date fecha = new Date(rs.getLong("FECHA"));
				int zona = rs.getInt("ZONA");
				int usuario = rs.getInt("USUARIO");
				Menu menu = daoProducto.getMenuPK(rs.getString("MENU"),rs.getString("RESTAURANTE"));
				lista.add(new Reserva(invitados, fecha, zona, usuario, menu));
			}
		}
		finally
		{
			daoProducto.cerrarRecursos();
		}
		return lista;
	}


	public Reserva getReservaPK(int PK1, Date PK2) throws SQLException, Exception 
	{
		Reserva lista = null;
		DAOProducto daoProducto = new DAOProducto();
		try
		{
			daoProducto.setConn(conn);
			String sql = "SELECT * FROM RESERVA WHERE FECHA = "+PK2.getTime()+" AND CLIENTE = "+PK2;

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int invitados = rs.getInt("INVITADOS");
				Date fecha = new Date(rs.getLong("FECHA"));
				int zona = rs.getInt("ZONA");
				int usuario = rs.getInt("USUARIO");
				Menu menu = daoProducto.getMenuPK(rs.getString("MENU"),rs.getString("RESTAURANTE"));
				lista=(new Reserva(invitados, fecha, zona, usuario, menu));
				}
		}
		finally
		{
			daoProducto.cerrarRecursos();
		}
		return lista;
	}

	

	
	public void addReserva(Reserva reserva) throws SQLException, Exception {

		String sql = "INSERT INTO RESERVA VALUES (";
		sql += reserva.getFecha() + ",";
		sql += reserva.getUsuario() + ",'";
		sql += reserva.getMenu().getNombre() + "','";
		sql += reserva.getMenu().getRestaurante() + "',";
		sql += reserva.getInvitados() + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	public void updateReserva(Reserva reserva) throws SQLException, Exception {

		String sql = "UPDATE RESERVA SET ";
		sql += "FECHA="+reserva.getFecha() + ",";
		sql += "CLIENTE="+reserva.getUsuario() + ",";
		sql += "MENU='"+reserva.getMenu().getNombre() + "',";
		sql += "RESTAURANTE='"+reserva.getMenu().getRestaurante() + "',";
		sql += "INVITADOS="+reserva.getInvitados() + ")";
		sql += "WHERE FECHA = "+reserva.getFecha()+" AND CLIENTE = "+reserva.getUsuario();


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	public void deleteReserva(int id, Date fecha) throws SQLException, Exception {

		String sql = "DELETE FROM RESERVA";
		sql += "WHERE FECHA = "+fecha.getTime()+" AND CLIENTE = "+id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	public ArrayList<Reserva> getReservasUsuario(int id) throws SQLException, Exception {
		ArrayList<Reserva> lista = new ArrayList<Reserva>();
		DAOProducto daoProducto = new DAOProducto();
		try
		{
			daoProducto.setConn(conn);
			String sql = "SELECT * FROM RESERVA WHERE USUARIO ="+id;

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int invitados = rs.getInt("INVITADOS");
				Date fecha = new Date(rs.getLong("FECHA"));
				int zona = rs.getInt("ZONA");
				int usuario = rs.getInt("USUARIO");
				Menu menu = daoProducto.getMenuPK(rs.getString("MENU"),rs.getString("RESTAURANTE"));
				lista.add(new Reserva(invitados, fecha, zona, usuario, menu));
			}
		}
		finally
		{
			daoProducto.cerrarRecursos();
		}
		return lista;
	}


	public ArrayList<Reserva> getReservasZona(int id) throws Exception {
		ArrayList<Reserva> lista = new ArrayList<Reserva>();
		DAOProducto daoProducto = new DAOProducto();
		try
		{
			daoProducto.setConn(conn);
			String sql = "SELECT * FROM RESERVA WHERE ZONA ="+id;

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int invitados = rs.getInt("INVITADOS");
				Date fecha = new Date(rs.getLong("FECHA"));
				int zona = rs.getInt("ZONA");
				int usuario = rs.getInt("USUARIO");
				Menu menu = daoProducto.getMenuPK(rs.getString("MENU"),rs.getString("RESTAURANTE"));
				lista.add(new Reserva(invitados, fecha, zona, usuario, menu));
			}
		}
		finally
		{
			daoProducto.cerrarRecursos();
		}
		return lista;
	}

}
