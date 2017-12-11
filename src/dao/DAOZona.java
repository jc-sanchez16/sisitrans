
package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;


public class DAOZona {


	private ArrayList<Object> recursos;


	private Connection conn;


	public DAOZona() {
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


	public ArrayList<Zona> getZonas() throws SQLException, Exception 
	{
		ArrayList<Zona> lista = new ArrayList<Zona>();
		DAOReserva daoReserva = new DAOReserva();
		DAORestaurante daoRestaurante = new DAORestaurante();
		try
		{
			String sql = "SELECT * FROM ZONA";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID");
				boolean abierto =((rs.getInt("ABIERTO") == 0) ? true:false);
				int capacidad = rs.getInt("CAPACIDAD");
				boolean discapacitados =((rs.getInt("DISCAPACITADOS") == 0) ? true:false);
				String especialidad =rs.getString("ESPECIALIDAD") ;
				daoReserva.setConn(conn);
				ArrayList<Reserva> reservas =  daoReserva.getReservasZona(id);
				daoReserva.cerrarRecursos();
				ArrayList<String> condiciones = getCondiciones(id);
				daoRestaurante.setConn(conn);
				ArrayList<Restaurante> restaurantes = daoRestaurante.getRestaurantesZona(id);
				daoRestaurante.cerrarRecursos();
				lista.add(new Zona(id, abierto, capacidad, discapacitados, especialidad, reservas, condiciones, restaurantes));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			daoReserva.cerrarRecursos();
			daoRestaurante.cerrarRecursos();
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

	

	
	public void addZona(Zona zona, int usuario, int clave, int tipo) throws SQLException, Exception {
		
		DAOUsuario daoUsuario = new DAOUsuario();
		try
		{
			daoUsuario.setConn(conn);
			if(!daoUsuario.verificar(usuario,clave+"",tipo))
			throw new Exception("usuario no valido");
			String sql = "INSERT INTO ZONA VALUES (";
			sql += zona.getId() + ",";
			int abierto = zona.getAbierto()== true ? 0:1;
			sql += abierto + ",";
			sql += zona.getCapacidad() + ",";
			int discapacitados = zona.getDiscapacitados()== true ? 0:1;
			sql += discapacitados + ",'";
			sql += zona.getEspecialidad() + "')";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
			
		}
		finally
		{
			daoUsuario.cerrarRecursos();
		}
	
		
		

	}

	public void updateZona(Zona zona, int usuario, int clave,int tipo) throws SQLException, Exception {

		DAOUsuario daoUsuario = new DAOUsuario();
		try
		{
			daoUsuario.setConn(conn);
			if(!daoUsuario.verificar(usuario,clave+"",tipo))
			throw new Exception("usuario no valido");
			String sql = "UPDATE ZONA SET ";
			sql += "ID="+zona.getId() + ",";
			int abierto = zona.getAbierto()== true ? 0:1;
			sql += "ABIERTO="+abierto + ",";
			sql += "CAPACIDAD="+zona.getCapacidad() + ",";
			int discapacitados = zona.getDiscapacitados()== true ? 0:1;
			sql += "DISCAPACITADOS="+discapacitados + ",";
			sql += "ESPECIALIDAD='"+zona.getEspecialidad() ;
			sql += "' WHERE ID = " + zona.getId();


			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
			
		}
		finally
		{
			daoUsuario.cerrarRecursos();
		}
		
	}


	public void deleteZona(int PK) throws SQLException, Exception {

		String sql = "DELETE FROM ZONA";
		sql += " WHERE ID = " + PK;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	
	private ArrayList<String> getCondiciones(int id) throws Exception 
	{
		ArrayList<String> lista = new ArrayList<String>();
			String sql = "SELECT * FROM CONDICIONES_TECNICAS WHERE ID_ZONA = "+id;

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				lista.add(rs.getString("NOMBRE"));
			}
		
		return lista;
	}
	
}
