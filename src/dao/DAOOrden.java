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
			long f = rs.getInt("FECHA");
			Date fecha = new Date(f);
			ArrayList<Integer> usuarios = getUsuariosOrden(mesa, f);
			ArrayList<Menu> menus= getMenusOrden(mesa,f);
			ArrayList<Producto> productos = getProductosOrden(mesa,f);
			boolean atendido = rs.getInt("ATENDIDO")==0? true: false;
			lista.add(new Orden(mesa, fecha, usuarios, menus, productos, atendido));
		}

		return lista;
	}


	public Orden getOrdenPK(int mesa, Date fecha) throws SQLException, Exception 
	{
		Orden lista = null;		
		long f = fecha.getTime();
		String sql = "SELECT * FROM ORDEN WHERE MESA ="+mesa+" AND FECHA = "+f;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			ArrayList<Integer> usuarios = getUsuariosOrden(mesa, f);
			ArrayList<Menu> menus= getMenusOrden(mesa,f);
			ArrayList<Producto> productos = getProductosOrden(mesa,f);
			boolean atendido = rs.getInt("ATENDIDO")==0? true: false;
			lista = new Orden(mesa, fecha, usuarios, menus, productos, atendido);
		}
		return lista;
	}




	public void addOrdenEnProceso(Orden orden) throws SQLException, Exception {

		int aten = orden.getAtendido()==true?0:1;
		String sql = "INSERT INTO ORDEN VALUES (";
		sql += orden.getFecha().getTime() + ",";
		sql += orden.getMesa() + ",";
		sql += aten+")";


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		addUsuarios(orden);

	}


	public ArrayList<Orden> getOrdenesUsuario(int id)  throws Exception{
		ArrayList<Orden> lista = new ArrayList<Orden>();
		String sql = "SELECT * FROM ORDEN_USUARIO WHERE ID_CLIENTE ="+id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int mesa = rs.getInt("MESA");
			long f = rs.getInt("FECHA");
			Date fecha = new Date(f);			
			lista.add(getOrdenPK(mesa,fecha));
		}

		return lista;
	}


	private ArrayList<Integer> getUsuariosOrden(int mesa, long f) throws Exception {
		ArrayList<Integer> lista = new ArrayList<Integer>();
		String sql = "SELECT * FROM ORDEN_USUARIO WHERE MESA = "+mesa+" AND FECHA = "+f;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {		
			lista.add(rs.getInt("ID_CLIENTE"));
		}

		return lista;
	}


	private ArrayList<Menu> getMenusOrden(int mesa, long f) throws Exception {		
		ArrayList<Menu> lista = new ArrayList<Menu>();
		DAOProducto daoProducto = new DAOProducto();
		try
		{
			daoProducto.setConn(conn);
			String sql = "SELECT * FROM ORDEN_PRODUCTO WHERE MESA = "+mesa+" AND FECHA = "+f;
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {		
				String nombre = rs.getString("NOMBRE");
				String restaurante = rs.getString("RESTAURANTE");
				Menu menu = daoProducto.getMenuPK(nombre, restaurante);
				String cambios = rs.getString("CAMBIOS");
				if(menu!= null)	
				{
					menu.setCambios(cambios);
					lista.add(menu);
				}
			}
		}
		finally
		{
			daoProducto.cerrarRecursos();
		}
		return lista;
	}


	public ArrayList<Producto> getProductosOrden(int mesa, long f)throws Exception {
		ArrayList<Producto> lista = new ArrayList<Producto>();
		DAOProducto daoProducto = new DAOProducto();
		try
		{
			daoProducto.setConn(conn);
			String sql = "SELECT * FROM ORDEN_PRODUCTO WHERE MESA = "+mesa+" AND FECHA = "+f;
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {		
				String nombre = rs.getString("NOMBRE");
				String restaurante = rs.getString("RESTAURANTE");
				Producto producto = daoProducto.getProductoPK(nombre, restaurante);
				String cambios = rs.getString("CAMBIOS");
				if(producto!= null)	
				{
					producto.setCambios(cambios);
					lista.add(producto);
				}
			}
		}
		finally
		{
			daoProducto.cerrarRecursos();
		}
		return lista;
	}
	public ArrayList<Articulo> getArticulosOrden(int mesa, long f)throws Exception {
		ArrayList<Articulo> lista = new ArrayList<Articulo>();
		DAOProducto daoProducto = new DAOProducto();
		try
		{
			daoProducto.setConn(conn);
			String sql = "SELECT * FROM ORDEN_PRODUCTO WHERE MESA = "+mesa+" AND FECHA = "+f;
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {		
				String nombre = rs.getString("NOMBRE");
				String restaurante = rs.getString("RESTAURANTE");
				Producto producto = daoProducto.getProductoPK(nombre, restaurante);
				String cambios = rs.getString("CAMBIOS");
				if(producto ==null)
				{
					Menu product = daoProducto.getMenuPK(nombre, restaurante);
					if(product!= null)
					{
						product.setCambios(cambios);
						lista.add(product);
					}
				}
				else 
				{
					producto.setCambios(cambios);
					lista.add(producto);
				}
			}
		}
		finally
		{
			daoProducto.cerrarRecursos();
		}
		return lista;
	}
	private void addUsuarios(Orden orden) throws Exception {
		ArrayList<Integer> usuarios = orden.getUsuarios(); 
		for (int j = 0; j < usuarios.size(); j++) 
		{
			String sql = "INSERT INTO ORDEN_USUARIO VALUES (";
			sql += orden.getFecha().getTime() + ",";
			sql += orden.getMesa() + ",";
			sql += usuarios.get(j) +")";
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
		}
	}


	private void addMenus(Orden orden)throws Exception {
		ArrayList<Menu> menus = orden.getMenus(); 
		for (int j = 0; j < menus.size(); j++) 
		{
			Menu menu = menus.get(j);
			String sql = "INSERT INTO ORDEN_PRODUCTO VALUES ('";
			sql += menu.getRestaurante()+"','";
			sql += menu.getNombre()+"',";
			sql += orden.getFecha().getTime() + ",";
			sql += orden.getMesa() + ")";
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
		}
	}


	private void addProductos(Orden orden) throws Exception 
	{
		ArrayList<Producto> productos = orden.getProductos(); 
		for (int j = 0; j < productos.size(); j++) 
		{
			Producto producto = productos.get(j);
			String sql = "INSERT INTO ORDEN_PRODUCTO VALUES ('";
			sql += producto.getRestaurante()+"','";
			sql += producto.getNombre()+"',";
			sql += orden.getFecha().getTime() + ",";
			sql += orden.getMesa() + ")";
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
		}
	}

	private void deleteUsuarios(int mesa, long f)throws Exception 
	{
		String sql = "DELETE FROM ORDEN_USUARIO";
		sql += " WHERE MESA = "+mesa+" AND FECHA = "+f;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	private void deleteMenusYProductos(int mesa, long f)throws Exception 
	{
		String sql = "DELETE FROM ORDEN_PRODUCTO";
		sql += " WHERE MESA = "+mesa+" AND FECHA = "+f;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	public void registrarPedidoProducto(String nombre, String restaurante, String camb,int usuario, int mesa, Date fecha) throws Exception
	{

		String[] cambios = camb.split(":");

		String sql = "INSERT INTO ORDEN_PRODUCTO VALUES ('";
		sql += restaurante+"','";
		sql += nombre+"',";
		sql += fecha.getTime() + ",";
		sql += mesa + ",'";
		sql += camb +"',";
		sql += usuario +")";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}


	public String registrarServicio(int clave, String restaurante, Date fecha, int mesa) throws Exception 
	{
		String res = "no se realizo la accion";
		String sql = "UPDATE ORDEN SET ";
		sql += "ATENDIDO ="+ 0+"";
		sql += " WHERE FECHA = " + fecha.getTime()+" AND MESA ="+mesa;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		res= "se realizo la accion";

		return res;
	}


	public String cancelarServicio(int clave, String restaurante, Date fecha, int mesa) throws SQLException, Exception {
		String res = "no se realizo la accion";
		if(getOrdenPK(mesa, fecha).getAtendido())
			throw new Exception("La orden ya estaba finalizada");
		String sql = "DELETE FROM ORDEN WHERE FECHA = " + fecha.getTime()+" AND MESA ="+mesa;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		res= "se realizo la accion";

		return res;
	}


	public ArrayList<String[]> consultarConsumo(int usuario, int clave, int peticion) throws Exception {
		ArrayList<String[]> res = new ArrayList<String[]>();
		String sql = "SELECT * FROM ORDEN_PRODUCTO WHERE USUARIO = "+peticion;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {		
			String nombre = rs.getString("NOMBRE");
			String restaurante = rs.getString("RESTAURANTE");
			String[] temp = new String[2];
			temp[0] =nombre;
			temp[1]= restaurante;
			res.add(temp);
		}
		return res;
	}


	public String consultarPedidos(String usuario, int clave) throws SQLException, Exception {
		String res = null;
		DAOUsuario daoUsuario= new DAOUsuario();
		DAOProducto daoProducto = new DAOProducto();		
		DAORestaurante daoRestaurante = new DAORestaurante();
		try
		{
			daoUsuario.setConn(conn);
			daoRestaurante.setConn(conn);
			daoProducto.setConn(conn);
			String sql = "SELECT * FROM ORDEN_PRODUCTO ";
			if(daoRestaurante.verificarRest(usuario,clave))
				sql += "WHERE RESTAURANTE = '"+usuario+"' ";
			else if(!daoUsuario.verificar(Integer.parseInt(usuario), clave, 1))
				throw new Exception("no es un usuario valido");
			sql += "ORDER BY RESTAURANTE";
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			res = "[";
			String ant = null;
			int clientesNR = 0;
			double ventas= 0;
			ArrayList<String> productos= new ArrayList<String>();
			String restaurante = null;
			//	RFC8. CONSULTAR PEDIDOS
			//	Muestra la información consolidada de los pedidos hechos en RotondAndes. Consolida, como mínimo, para cada uno los
			//	restaurantes y para cada uno de sus productos las ventas totales (en dinero y en cantidad), lo consumidos por clientes
			//	registrados y por clientes no registrados.
			//	Esta operación es realizada por un usuario restaurante y por el administrador de RotondAndes.
			//	NOTA: Respetando la privacidad de los clientes, cuando un restaurante hace esta consulta obtiene la información de sus
			//	propias actividades, mientras que el administrador obtiene toda la información. Ver RNF1.
			while (rs.next())
			{
				restaurante = rs.getString("RESTAURANTE");
				if(ant !=null && !ant.equals(restaurante))
				{
					res +="\"productos\": [";
					for(int i = 0 ; i<productos.size();i++)
					{
						String actual = productos.get(i); 
						if(!actual.equals(""))
						{
							int cantidad =  1;
							for(int j = i+1 ; j<productos.size();j++)
							{
								String nuevo =  productos.get(j);
								if(nuevo.equals(actual))
								{
									cantidad++;
									productos.add(j, "");
								}
							}
							res+= "{\"nombre\": \""+actual+"\",";
							res+="\"unidadesVendidas\": "+cantidad+",";
							Producto prod = daoProducto.getProductoPK(actual, ant);
							if(prod == null)
							{
								Menu men = daoProducto.getMenuPK(actual, ant);
								res+="\"ventas\": "+men.getPrecio()*cantidad+"},";
								ventas+=men.getPrecio()*cantidad;
							}
							else
							{
								res+="\"ventas\": "+prod.getPrecio()*cantidad+"},";
								ventas+=prod.getPrecio()*cantidad;
							}
						}
					}
					res = res.substring(0,res.lastIndexOf(","));
					res +="], \"clientesNoRegistrados\": "+clientesNR+","; 
					res +=" \"clientesRegistrados\": "+(productos.size()-clientesNR)+","; 
					res += "\"cantidadVentas\": "+productos.size()+",";
					res += "\"valorVentas\": "+ventas+"}, ";
					res+="{\"nombre\": \""+ restaurante+"\",";
					ant = restaurante;
					clientesNR = 0;
					ventas= 0;
					productos= new ArrayList<String>();

				}
				if(ant ==null)
				{
					res+="{\"nombre\": \""+ restaurante+"\",";
					ant = restaurante;
				}
				String nombre = rs.getString("NOMBRE");
				int usua = rs.getInt("USUARIO");
				if(daoUsuario.verificar(usua, 0, 0))
					clientesNR++;
				productos.add(nombre);				
			}		
			res +="\"productos\": [";
			for(int i = 0 ; i<productos.size();i++)
			{
				String actual = productos.get(i); 
				int cantidad =  1;
				for(int j = i+1 ; j<productos.size();j++)
				{
					String nuevo =  productos.get(j);
					if(nuevo.equals(actual))
					{
						cantidad++;
						productos.add(j, "");
					}
				}
				res+= "{\"nombre\": \""+actual+"\",";
				res+="\"unidadesVendidas\": "+cantidad+",";
				Producto prod = daoProducto.getProductoPK(actual, ant);
				if(prod == null)
				{
					Menu men = daoProducto.getMenuPK(actual, ant);
					res+="\"ventas\": "+men.getPrecio()*cantidad+"},";
					ventas+=men.getPrecio()*cantidad;
				}
				else
				{
					res+="\"ventas\": "+prod.getPrecio()*cantidad+"},";
					ventas+=prod.getPrecio()*cantidad;
				}

			}
			res = res.substring(0,res.lastIndexOf(","));
			res +="], \"clientesNoRegistrados\": "+clientesNR+","; 
			res +=" \"clientesRegistrados\": "+(productos.size()-clientesNR)+","; 
			res += "\"cantidadVentas\": "+productos.size()+",";
			res += "\"valorVentas\": "+ventas+"}]";			
		}
		catch(NumberFormatException e)
		{
			throw new Exception("no es un restaurante valido");
		}
		finally
		{
			daoUsuario.cerrarRecursos();
			daoProducto.cerrarRecursos();
		}
		return res;
	}


	public String consultarConsumo(String restaurante, Date fecI, Date fecF, String order, String group) throws SQLException {
		String res = "no se pudo hacer";
		String sql = null;
			if(!group.equals(""))
			{
				String[] gro = group.split("|");	
				sql ="SELECT "+gro[0]+" from (((SELECT * FROM ORDEN_PRODUCTO PR WHERE PR.RESTAURANTE = '"+restaurante+"' AND PR.FECHA BETWEEN "+fecI.getTime()+" AND "+fecF.getTime()+" ) PRR  JOIN (PRODUCTO U JOIN  TIPO_COMIDA TC ON U.NOMBRE = TC.NOMBRE AND U.RESTAURANTE = TC.RESTAURANTE)on U.RESTAURANTE = PRR.RESTAURANTE AND U.NOMBRE=PRR.NOMBRE) JOIN PERSONA PS ON PS.NUMERO_ID = PRR.USUARIO) GROUP BY "+gro[1];
			}
			else	
				sql ="SELECT PRR.*,U.*, PS.NOMBRE AS NOM, PS.EDAD, PS.TIPO, TC.TIPO AS TIP from (((SELECT * FROM ORDEN_PRODUCTO PR WHERE PR.RESTAURANTE = '"+restaurante+"' AND PR.FECHA BETWEEN "+fecI.getTime()+" AND "+fecF.getTime()+" ) PRR  JOIN (PRODUCTO U JOIN  TIPO_COMIDA TC ON U.NOMBRE = TC.NOMBRE AND U.RESTAURANTE = TC.RESTAURANTE)on U.RESTAURANTE = PRR.RESTAURANTE AND U.NOMBRE=PRR.NOMBRE) JOIN PERSONA PS ON PS.NUMERO_ID = PRR.USUARIO)";
			if(!order.equals(""))
				sql += " ORDER BY "+order;
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			res= "[";
			while (rs.next()) {		
				if(group.equals(""))
				{
				res+="{ \"id\": "+ rs.getString("USUARIO");
				res+= ", \"nombre\": \""+ rs.getString("NOM");
				res+= "\", \"edad\":"+ rs.getString("EDAD");
				res+= ", \"fecha\": \""+new Date(rs.getLong("FECHA"));
				res+= "\", \"mesa\": "+ rs.getString("MESA");
				res+= ", \"producto\": \""+ rs.getString("NOMBRE");
				res+= "\", \"restaurante\": \""+rs.getString("RESTAURANTE");
				res+= "\", \"costo\": "+ rs.getString("COSTO");
				res+= ", \"tipo\": \""+ rs.getString("TIPO");
				res+= "\", \"descripcion español\": \""+rs.getString("DESCRIPCION_E");
				res+= "\", \"descripcion ingles\": \""+rs.getString("DESCRIPCION_EN");
				res+= "\", \"tiempo preparacion\": "+ rs.getString("TIEMPO_PREP");
				res+= ", \"precio\": "+ rs.getString("PRECIO");
				res+= ", \"tipo comida\": \""+rs.getString("TIP");
				res+= "\"},";
				}
				else
				{
					res="{ ";
					String[] gro = group.split("|")[0].split(",");
					for (int i = 0; i < gro.length; i++) {
						res+= " \""+gro[i]+"\": "+ rs.getString(gro[i].trim());
					}
					
					res+= "},";
				}
			}
			res = res.substring(0, res.length()-1);
			res+="]";
		
		return res;
	}
	public String consultarConsumo2(String restaurante, Date fecI, Date fecF, String order, String group) throws SQLException {
		String res = "no se pudo hacer";
		String sql = null;
			if(!group.equals(""))
			{
				String[] gro = group.split("|");	
				sql ="SELECT "+gro[0]+" FROM  ((SELECT * FROM ORDEN_PRODUCTO OP WHERE USUARIO NOT IN (SELECT USUARIO FROM ORDEN_PRODUCTO PR WHERE PR.RESTAURANTE = '"+restaurante+"' AND PR.FECHA BETWEEN "+fecI.getTime()+" AND "+fecF.getTime()+" AND OP.FECHA BETWEEN "+fecI.getTime()+" AND "+fecF.getTime()+")PRR JOIN PERSONA PS ON PS.NUMERO_ID = PRR.USUARIO) GROUP BY "+gro[1];
			}
			else	
				sql ="SELECT PRR.*,PS.NOMBRE AS NOM, PS.EDAD, PS.TIPO  FROM  ((SELECT * FROM ORDEN_PRODUCTO OP WHERE USUARIO NOT IN (SELECT USUARIO FROM ORDEN_PRODUCTO PR WHERE PR.RESTAURANTE = '"+restaurante+"' AND PR.FECHA BETWEEN "+fecI.getTime()+" AND "+fecF.getTime()+") AND OP.FECHA BETWEEN "+fecI.getTime()+" AND "+fecF.getTime()+")PRR JOIN PERSONA PS ON PS.NUMERO_ID = PRR.USUARIO)";
			if(!order.equals(""))
				sql += " ORDER BY "+order;
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			res= "[";
			while (rs.next()) {		
				if(group.equals(""))
				{
				res+="{ \"id\": "+ rs.getString("USUARIO");
				res+= ", \"nombre\": \""+ rs.getString("NOM");
				res+= "\", \"edad\":"+ rs.getString("EDAD");
				res+= ", \"fecha\": \""+new Date(rs.getLong("FECHA"));
				res+= "\", \"mesa\": "+ rs.getString("MESA");
				res+= ", \"producto\": \""+ rs.getString("NOMBRE");
				res+= "\", \"restaurante\": \""+rs.getString("RESTAURANTE");
				res+= "\"},";
				}
				else
				{
					res="{ ";
					String[] gro = group.split("|")[0].split(",");
					for (int i = 0; i < gro.length; i++) {
						res+= " \""+gro[i]+"\": "+ rs.getString(gro[i].trim());
					}
					
					res+= "},";
				}
			}
			res = res.substring(0, res.length()-1);
			res+="]";
		
		return res;
	}
	public String consultarFuncionalidad(Date dia) throws SQLException {
		String res = "no se pudo hacer";
		Long fec = dia.getTime();
		String sql = "SELECT * FROM (SELECT  RES1.NOMBRE AS NOM1, RES1.ZONA AS ZON1, RES1.TIPO_COMIDA AS TIP1, RES1.WEB AS WEB1 FROM (SELECT COUNT(USUARIO) AS NUMERO, RESTAURANTE FROM ORDEN_PRODUCTO WHERE  rownum = 1 AND FECHA BETWEEN "+fec+" AND "+(fec+86340000)+" GROUP BY RESTAURANTE ORDER BY NUMERO DESC) TAB1 JOIN RESTAURANTE RES1 ON RES1.NOMBRE=TAB1.RESTAURANTE   ), (SELECT RES2.* FROM (SELECT COUNT(USUARIO) AS NUMERO, RESTAURANTE FROM ORDEN_PRODUCTO WHERE rownum = 1 AND FECHA BETWEEN "+fec+" AND "+(fec+86340000)+" GROUP BY RESTAURANTE ORDER BY NUMERO ASC )TAB2 JOIN RESTAURANTE RES2 ON RES2.NOMBRE=TAB2.RESTAURANTE  ), (SELECT RES3.* FROM (SELECT COUNT(USUARIO) AS NUMERO, NOMBRE, RESTAURANTE FROM ORDEN_PRODUCTO WHERE rownum = 1 AND FECHA BETWEEN "+fec+" AND "+(fec+86340000)+" GROUP BY NOMBRE, RESTAURANTE ORDER BY NUMERO DESC )TAB3 JOIN PRODUCTO RES3 ON RES3.NOMBRE= TAB3.NOMBRE AND RES3.RESTAURANTE = TAB3.RESTAURANTE ), (SELECT RES4.* FROM (SELECT COUNT(USUARIO) AS NUMERO, NOMBRE, RESTAURANTE FROM ORDEN_PRODUCTO  WHERE rownum = 1 AND FECHA BETWEEN "+fec+" AND "+(fec+86340000)+" GROUP BY NOMBRE, RESTAURANTE ORDER BY NUMERO ASC )TAB4 JOIN PRODUCTO RES4 ON RES4.NOMBRE= TAB4.NOMBRE AND RES4.RESTAURANTE = TAB4.RESTAURANTE  )";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			res= "[";
			while (rs.next()) {		
				if(group.equals(""))
				{
				res+="{ \"id\": "+ rs.getString("USUARIO");
				res+= ", \"nombre\": \""+ rs.getString("NOM");
				res+= "\", \"edad\":"+ rs.getString("EDAD");
				res+= ", \"fecha\": \""+new Date(rs.getLong("FECHA"));
				res+= "\", \"mesa\": "+ rs.getString("MESA");
				res+= ", \"producto\": \""+ rs.getString("NOMBRE");
				res+= "\", \"restaurante\": \""+rs.getString("RESTAURANTE");
				res+= "\"},";
				}
				else
				{
					res="{ ";
					String[] gro = group.split("|")[0].split(",");
					for (int i = 0; i < gro.length; i++) {
						res+= " \""+gro[i]+"\": "+ rs.getString(gro[i].trim());
					}
					
					res+= "},";
				}
			}
			res = res.substring(0, res.length()-1);
			res+="]";
		
		return res;
	}
}
