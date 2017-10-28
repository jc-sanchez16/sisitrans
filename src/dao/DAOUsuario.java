
package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;


public class DAOUsuario {

	public static final int USUARIO = 0;
	public static final int ADMINISTRADOR = 1;
	public static final int REPRESENTANTE = 2;

	private ArrayList<Object> recursos;


	private Connection conn;


	public DAOUsuario() {
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


	public ArrayList<Usuario> getUsuarios() throws SQLException, Exception 
	{
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		DAOReserva daoReserva = new DAOReserva();
		DAOOrden daoOrden = new DAOOrden();
		try
		{
			daoReserva.setConn(conn);
			daoOrden.setConn(conn);
			String sql = "SELECT * FROM PERSONA";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("NUMERO_ID");
				String nombre =rs.getString("NOMBRE") ;
				int edad = rs.getInt("EDAD");
				int tipo = rs.getInt("TIPO");
				ArrayList<Orden> ordenes = daoOrden.getOrdenesUsuario(id);
				ArrayList<String> preferencias = getPreferencias(id);
				ArrayList<Reserva> reservas =  daoReserva.getReservasUsuario(id);
				lista.add(new Usuario(id, nombre, edad, ordenes, preferencias, reservas, tipo));
			}
		}
		finally
		{
			daoReserva.cerrarRecursos();
			daoOrden.cerrarRecursos();
		}
		return lista;
	}


	public Usuario getUsuarioPK(int PK) throws SQLException, Exception 
	{
		Usuario lista = null;
		DAOReserva daoReserva = new DAOReserva();
		DAOOrden daoOrden = new DAOOrden();
		try
		{
			daoReserva.setConn(conn);
			daoOrden.setConn(conn);
			String sql = "SELECT * FROM PERSONA WHERE NUMERO_ID = "+PK;

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("NUMERO_ID");
				String nombre =rs.getString("NOMBRE") ;
				int edad = rs.getInt("EDAD");
				int tipo = rs.getInt("TIPO");
				ArrayList<Orden> ordenes = daoOrden.getOrdenesUsuario(id);
				ArrayList<String> preferencias = getPreferencias(id);
				ArrayList<Reserva> reservas =  daoReserva.getReservasUsuario(id);
				lista=(new Usuario(id, nombre, edad, ordenes, preferencias, reservas,tipo));
			}
		}
		finally
		{
			daoReserva.cerrarRecursos();
			daoOrden.cerrarRecursos();
		}
		return lista;
	}

	public void addUsuario(Usuario usuario, int clave) throws SQLException, Exception {

		String sql = "INSERT INTO PERSONA VALUES (";
		sql += usuario.getId() + ",'";
		sql += usuario.getNombre() + "',";
		sql += usuario.getEdad() + ",";
		sql += clave +",";
		sql += usuario.getTipo()+")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	public void updateUsuario(Usuario usuario) throws SQLException, Exception {

		String sql = "UPDATE PERSONA SET ";
		sql += "NUMERO_ID="+usuario.getId() + ",";
		sql += "NOMBRE='"+usuario.getNombre() + "',";
		sql += "EDAD="+usuario.getEdad();
		sql += "TIPO ="+ usuario.getTipo()+",";
		sql += " WHERE NUMERO_ID = " + usuario.getId();


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public void deleteUsuario(int id) throws SQLException, Exception {
	
		String sql = "DELETE FROM PERSONA";
		sql += " WHERE NUMERO_ID = " + id;
	
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	public ArrayList<Usuario> getAdministradores() throws SQLException, Exception 
	{
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		DAOReserva daoReserva = new DAOReserva();
		DAOOrden daoOrden = new DAOOrden();
		try
		{
			daoReserva.setConn(conn);
			daoOrden.setConn(conn);
			String sql = "SELECT * FROM PERSONA WHERE TIPO = 1";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("NUMERO_ID");
				String nombre =rs.getString("NOMBRE") ;
				int edad = rs.getInt("EDAD");
				int tipo = rs.getInt("TIPO");
				ArrayList<Orden> ordenes = daoOrden.getOrdenesUsuario(id);
				ArrayList<String> preferencias = getPreferencias(id);
				ArrayList<Reserva> reservas =  daoReserva.getReservasUsuario(id);
				lista.add(new Usuario(id, nombre, edad, ordenes, preferencias, reservas, tipo));
			}
		}
		finally
		{
			daoReserva.cerrarRecursos();
			daoOrden.cerrarRecursos();
		}
		return lista;
	}


	public Usuario getAdministradorPK(int PK) throws SQLException, Exception 
	{
		Usuario lista = null;
		DAOReserva daoReserva = new DAOReserva();
		DAOOrden daoOrden = new DAOOrden();
		try
		{
			daoReserva.setConn(conn);
			daoOrden.setConn(conn);
			String sql = "SELECT * FROM PERSONA WHERE NUMERO_ID = "+PK+" AND TIPO = 1";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("NUMERO_ID");
				String nombre =rs.getString("NOMBRE") ;
				int edad = rs.getInt("EDAD");
				int tipo = rs.getInt("TIPO");
				ArrayList<Orden> ordenes = daoOrden.getOrdenesUsuario(id);
				ArrayList<String> preferencias = getPreferencias(id);
				ArrayList<Reserva> reservas =  daoReserva.getReservasUsuario(id);
				lista=(new Usuario(id, nombre, edad, ordenes, preferencias, reservas, tipo));
			}
		}
		finally
		{
			daoReserva.cerrarRecursos();
			daoOrden.cerrarRecursos();
		}
		return lista;
	}

	private ArrayList<String> getPreferencias(int id) throws Exception {
		ArrayList<String> lista = new ArrayList<String>();
		String sql = "SELECT * FROM PREFERENCIAS WHERE NUMERO_ID = "+id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			lista.add(rs.getString("NOMBRE"));
		}
	
	return lista;
	}
	
	public ArrayList<Usuario> getRepresentantes() throws SQLException, Exception 
	{
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		DAOReserva daoReserva = new DAOReserva();
		DAOOrden daoOrden = new DAOOrden();
		try
		{
			daoReserva.setConn(conn);
			daoOrden.setConn(conn);
			String sql = "SELECT * FROM PERSONA WHERE TIPO = 2";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("NUMERO_ID");
				String nombre =rs.getString("NOMBRE") ;
				int edad = rs.getInt("EDAD");
				int tipo = rs.getInt("TIPO");
				ArrayList<Orden> ordenes = daoOrden.getOrdenesUsuario(id);
				ArrayList<String> preferencias = getPreferencias(id);
				ArrayList<Reserva> reservas =  daoReserva.getReservasUsuario(id);
				lista.add(new Usuario(id, nombre, edad, ordenes, preferencias, reservas, tipo));
			}
		}
		finally
		{
			daoReserva.cerrarRecursos();
			daoOrden.cerrarRecursos();
		}
		return lista;
	}


	public Usuario getRepresentantesPK(int PK) throws SQLException, Exception 
	{
		Usuario lista = null;
		DAOReserva daoReserva = new DAOReserva();
		DAOOrden daoOrden = new DAOOrden();
		try
		{
			daoReserva.setConn(conn);
			daoOrden.setConn(conn);
			String sql = "SELECT * FROM PERSONA WHERE NUMERO_ID = "+PK+" AND TIPO = 2";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("NUMERO_ID");
				String nombre =rs.getString("NOMBRE") ;
				int edad = rs.getInt("EDAD");
				int tipo = rs.getInt("TIPO");
				ArrayList<Orden> ordenes = daoOrden.getOrdenesUsuario(id);
				ArrayList<String> preferencias = getPreferencias(id);
				ArrayList<Reserva> reservas =  daoReserva.getReservasUsuario(id);
				lista=(new Usuario(id, nombre, edad, ordenes, preferencias, reservas, tipo));
			}
		}
		finally
		{
			daoReserva.cerrarRecursos();
			daoOrden.cerrarRecursos();
		}
		return lista;
	}


	public boolean verificar(int usuario, int clave, int tipo) throws SQLException {
		String sql = "SELECT CLAVE FROM PERSONA WHERE NUMERO_ID ="+usuario+" AND CLAVE = "+clave+" AND TIPO ="+tipo;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return rs.next();
	}

	
}
