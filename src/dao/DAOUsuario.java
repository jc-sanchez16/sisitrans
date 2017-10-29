
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


	public ArrayList<Usuario> getUsuarios(int usuario2, int clave, int administrador2) throws SQLException, Exception 
	{
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		DAOReserva daoReserva = new DAOReserva();
		DAOUsuario daoUsuario = new DAOUsuario();
		DAOOrden daoOrden = new DAOOrden();
		try
		{
			daoReserva.setConn(conn);
			daoOrden.setConn(conn);
			daoUsuario.setConn(conn);
			if(!daoUsuario.verificar(usuario2, clave, administrador2))
				throw new Exception("No es un usuario valido");	
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
			daoUsuario.cerrarRecursos();
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
			String sql = "SELECT * FROM PERSONA WHERE CLAVE = "+PK;

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

	public void addUsuario(Usuario usuario, int clave, int usuario2, int clave2) throws SQLException, Exception {


		DAOUsuario daoUsuario = new DAOUsuario();

		try
		{
			daoUsuario.setConn(conn);
			if(!daoUsuario.verificar(usuario2, clave2, DAOUsuario.ADMINISTRADOR))
				throw new Exception("No es un usuario valido");	
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
		finally
		{
			daoUsuario.cerrarRecursos();
		}

	}

	public void updateUsuario(Usuario usuario, int usuario2, int clave) throws SQLException, Exception {


		DAOUsuario daoUsuario = new DAOUsuario();

		try
		{
			daoUsuario.setConn(conn);
			if(usuario.getId()==usuario2)
			{
				if(!daoUsuario.verificar(usuario2, clave, DAOUsuario.USUARIO))
					throw new Exception("No es un usuario valido");	

				String sql = "UPDATE PERSONA SET ";
				sql += "NOMBRE='"+usuario.getNombre() + "',";
				sql += "EDAD="+usuario.getEdad()+",";
				sql += "TIPO ="+ usuario.getTipo();
				sql += " WHERE NUMERO_ID = " + usuario.getId();


				PreparedStatement prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				prepStmt.executeQuery();

			}
			else
			{
				if(!daoUsuario.verificar(usuario2, clave, DAOUsuario.ADMINISTRADOR))
					throw new Exception("No es un usuario valido");	
				String sql = "UPDATE PERSONA SET ";
				sql += "NOMBRE='"+usuario.getNombre() + "',";
				sql += "EDAD="+usuario.getEdad()+",";
				sql += "TIPO ="+ usuario.getTipo();
				sql += " WHERE NUMERO_ID = " + usuario.getId();


				PreparedStatement prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				prepStmt.executeQuery();
			}

		}
		finally
		{
			daoUsuario.cerrarRecursos();
		}

	}

	public void deleteUsuario(int id, int usuario2, int clave) throws SQLException, Exception {


		DAOUsuario daoUsuario = new DAOUsuario();

		try
		{
			daoUsuario.setConn(conn);
			if(!daoUsuario.verificar(usuario2, clave, DAOUsuario.ADMINISTRADOR))
				throw new Exception("No es un usuario valido");	
			String sql = "DELETE FROM PERSONA";
			sql += " WHERE NUMERO_ID = " + id;

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();

		}
		finally
		{
			daoUsuario.cerrarRecursos();
		}

	}


	public ArrayList<Usuario> getAdministradores(int usuario2, int clave) throws SQLException, Exception 
	{
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		DAOReserva daoReserva = new DAOReserva();
		DAOOrden daoOrden = new DAOOrden();
		DAOUsuario daoUsuario = new DAOUsuario();
		try
		{
			daoReserva.setConn(conn);
			daoOrden.setConn(conn);
			daoUsuario.setConn(conn);
			if(!daoUsuario.verificar(usuario2, clave, DAOUsuario.ADMINISTRADOR))
				throw new Exception("No es un usuario valido");
			String sql = "SELECT * FROM PERSONA WHERE TIPO = 1";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("NUMERO_ID");
				String nombre =rs.getString("NOMBRE") ;
				int edad = rs.getInt("EDAD");
				int tipo = rs.getInt("TIPO");
//				ArrayList<Orden> ordenes = daoOrden.getOrdenesUsuario(id); falta mirar.
				ArrayList<String> preferencias = getPreferencias(id);
				ArrayList<Reserva> reservas =  daoReserva.getReservasUsuario(id);
				lista.add(new Usuario(id, nombre, edad, new ArrayList<Orden>(), preferencias, reservas, tipo));
			}
		}
		finally
		{
			daoReserva.cerrarRecursos();
			daoOrden.cerrarRecursos();
			daoUsuario.cerrarRecursos();
		}
		return lista;
	}


	public Usuario getAdministradorPK(int PK, int usuario2, int clave) throws SQLException, Exception 
	{
		Usuario lista = null;
		DAOReserva daoReserva = new DAOReserva();
		DAOOrden daoOrden = new DAOOrden();
		DAOUsuario daoUsuario = new DAOUsuario();
		try
		{
			daoReserva.setConn(conn);
			daoOrden.setConn(conn);
			daoUsuario.setConn(conn);
			if(!daoUsuario.verificar(usuario2, clave, DAOUsuario.ADMINISTRADOR))
				throw new Exception("No es un usuario valido");
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
			daoUsuario.cerrarRecursos();
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

	public ArrayList<Usuario> getRepresentantes(int admin, int userRepresentante, int claveUsuario, int claveRep) throws SQLException, Exception 
	{
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		DAOReserva daoReserva = new DAOReserva();
		DAOOrden daoOrden = new DAOOrden();
		DAOUsuario daoUsuario = new DAOUsuario();
		DAORestaurante daoRestaurante = new DAORestaurante();
		try
		{
			daoReserva.setConn(conn);
			daoOrden.setConn(conn);
			daoUsuario.setConn(conn);
			daoRestaurante.setConn(conn);
			if(admin>0)
			{
				if(!daoUsuario.verificar(admin, claveUsuario, DAOUsuario.ADMINISTRADOR))
					throw new Exception("No es un usuario valido");
			}
			if(userRepresentante>0)
			{
				if(!daoUsuario.verificar(userRepresentante, claveRep, DAOUsuario.REPRESENTANTE))
					throw new Exception("No es un usuario valido");
			}
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
			daoUsuario.cerrarRecursos();
			daoRestaurante.cerrarRecursos();
		}
		return lista;
	}


	public Usuario getRepresentantesPK(int PK, int admin2, int userRepresentante, String restaurante, int claveUsuario, int claveRep, int claveRestaurante) throws SQLException, Exception 
	{
		Usuario lista = null;
		DAOReserva daoReserva = new DAOReserva();
		DAOOrden daoOrden = new DAOOrden();
		DAOUsuario daoUsuario = new DAOUsuario();
		DAORestaurante daoRestaurante = new DAORestaurante();
		try
		{
			daoReserva.setConn(conn);
			daoOrden.setConn(conn);
			daoRestaurante.setConn(conn);
			daoUsuario.setConn(conn);
			if(admin2>0)
			{
				if(!daoUsuario.verificar(admin2, claveUsuario, DAOUsuario.ADMINISTRADOR))
					throw new Exception("No es un usuario valido");
			}
			if(userRepresentante>0)
			{
				if(userRepresentante==PK)
				{
					if(!daoUsuario.verificar(userRepresentante, claveRep, DAOUsuario.REPRESENTANTE))
						throw new Exception("No es un usuario valido");
				}
				else
				{
					throw new Exception("No es un usuario valido");
				}
			}
			if(!restaurante.equals("NA"))
			{
				if(!daoRestaurante.verificar(restaurante, claveRestaurante, PK));
					throw new Exception("No es un usuario valido");
			}
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
			daoRestaurante.cerrarRecursos();
			daoUsuario.cerrarRecursos();
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
