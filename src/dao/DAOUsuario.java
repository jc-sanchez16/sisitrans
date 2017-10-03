
package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;


public class DAOUsuario {


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
				ArrayList<Orden> ordenes = daoOrden.getOrdenesUsuario(id);
				ArrayList<String> preferencias = getPreferencias(id);
				ArrayList<Reserva> reservas =  daoReserva.getReservasUsuario(id);
				lista.add(new Usuario(id, nombre, edad, ordenes, preferencias, reservas));
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
				ArrayList<Orden> ordenes = daoOrden.getOrdenesUsuario(id);
				ArrayList<String> preferencias = getPreferencias(id);
				ArrayList<Reserva> reservas =  daoReserva.getReservasUsuario(id);
				lista=(new Usuario(id, nombre, edad, ordenes, preferencias, reservas));
			}
		}
		finally
		{
			daoReserva.cerrarRecursos();
			daoOrden.cerrarRecursos();
		}
		return lista;
	}

	

	


	public void addUsuario(Usuario usuario) throws SQLException, Exception {

		String sql = "INSERT INTO PERSONA VALUES (";
		sql += usuario.getId() + ",'";
		sql += usuario.getNombre() + "',";
		sql += usuario.getEdad() + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	public void updateUsuario(Usuario usuario) throws SQLException, Exception {

		String sql = "UPDATE PERSONA SET ";
		sql += "NUMERO_ID="+usuario.getId() + ",";
		sql += "NOMBRE='"+usuario.getNombre() + "',";
		sql += "EDAD="+usuario.getEdad();

		sql += " WHERE NUMERO_ID = " + usuario.getId();


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public ArrayList<Administrador> getAdministradores() throws SQLException, Exception 
	{
		ArrayList<Administrador> lista = new ArrayList<Administrador>();
		DAOReserva daoReserva = new DAOReserva();
		DAOOrden daoOrden = new DAOOrden();
		try
		{
			daoReserva.setConn(conn);
			daoOrden.setConn(conn);
			String sql = "SELECT * FROM PERSONA WHERE CLAVE IS NOT NULL";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("NUMERO_ID");
				String nombre =rs.getString("NOMBRE") ;
				int edad = rs.getInt("EDAD");
				ArrayList<Orden> ordenes = daoOrden.getOrdenesUsuario(id);
				ArrayList<String> preferencias = getPreferencias(id);
				ArrayList<Reserva> reservas =  daoReserva.getReservasUsuario(id);
				String clave = rs.getString("CLAVE");
				lista.add(new Administrador(id, nombre, edad, ordenes, preferencias, reservas, clave));
			}
		}
		finally
		{
			daoReserva.cerrarRecursos();
			daoOrden.cerrarRecursos();
		}
		return lista;
	}


	public Administrador getAdministradorPK(int PK) throws SQLException, Exception 
	{
		Administrador lista = null;
		DAOReserva daoReserva = new DAOReserva();
		DAOOrden daoOrden = new DAOOrden();
		try
		{
			daoReserva.setConn(conn);
			daoOrden.setConn(conn);
			String sql = "SELECT * FROM PERSONA WHERE NUMERO_ID = "+PK+" AND CLAVE IS NOT NULL";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("NUMERO_ID");
				String nombre =rs.getString("NOMBRE") ;
				int edad = rs.getInt("EDAD");
				ArrayList<Orden> ordenes = daoOrden.getOrdenesUsuario(id);
				ArrayList<String> preferencias = getPreferencias(id);
				ArrayList<Reserva> reservas =  daoReserva.getReservasUsuario(id);
				String clave = rs.getString("CLAVE");
				lista=(new Administrador(id, nombre, edad, ordenes, preferencias, reservas, clave));
			}
		}
		finally
		{
			daoReserva.cerrarRecursos();
			daoOrden.cerrarRecursos();
		}
		return lista;
	}

	

	


	public void addAdministrador(Administrador administrador) throws SQLException, Exception {

		String sql = "INSERT INTO PERSONA VALUES (";
		sql += administrador.getId() + ",'";
		sql += administrador.getNombre() + "',";
		sql += administrador.getEdad() + ",'";
		sql += administrador.getClave() + "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	public void updateAdministrador(Administrador administrador) throws SQLException, Exception {

		String sql = "UPDATE PERSONA SET ";
		sql += "NUMERO_ID="+administrador.getId() + ",";
		sql += "NOMBRE='"+administrador.getNombre() + "',";
		sql += "EDAD="+administrador.getEdad()+ ",";
		sql += "CLAVE='"+administrador.getClave();

		sql += "' WHERE NUMERO_ID = " + administrador.getId() + "AND CLAVE IS NOT NULL";


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public void deleteUsuario(Usuario usuario) throws SQLException, Exception {

		String sql = "DELETE FROM PERSONA";
		sql += " WHERE NUMERO_ID = " + usuario.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
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
}
