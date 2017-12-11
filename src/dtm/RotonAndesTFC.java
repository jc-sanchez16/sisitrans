package dtm;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.naming.Context;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;

import jms.NonReplyException;
import jms.RF18MQ;
import jms.RF19MQ;
import rest.RequerimientosServices.Respuesta;
import tm.TM;
public class RotonAndesTFC 
{
	private final static String QUEUE_NAME = "java:global/18";
	private final static String MQ_CONNECTION_NAME = "java:global/IT5";
	
	private static RotonAndesTFC instance;
	
	private TM tm;
	
	private static String path;
	private DataSource ds1;
	private Connection conn1;
	private InitialContext context;


	private RotonAndesTFC() throws NamingException, JMSException
	{
		Hashtable<String, String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
		env.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
		context = new InitialContext(env);
		ds1= (DataSource) context.lookup("java:XAApp2");
		InitialContext ctx = new InitialContext();
		
	}
	
	public void stop() throws  SQLException
	{
		conn1.close();
	}
	
	/**
	 * Método que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	public static void setPath(String p) {
		path = p;
	}
	
	public void setUpTransactionManager(TM tm)
	{
	   this.tm = tm;
	}
	
	private static RotonAndesTFC getInst()
	{
		return instance;
	}
	
	public static RotonAndesTFC getInstance(TM tm)
	{
		if(instance == null)
		{
			try {
				instance = new RotonAndesTFC();
			} catch (NamingException e) {
				e.printStackTrace();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		instance.setUpTransactionManager(tm);
		return instance;
	}
	
	public static RotonAndesTFC getInstance()
	{
		if(instance == null)
		{
			TM tm = new TM(path);
			return getInstance(tm);
		}
		if(instance.tm != null)
		{
			return instance;
		}
		TM tm = new TM(path);
		return getInstance(tm);
	}

	public void retirarRestauranteD(String restaurante) throws SQLException, NamingException, IllegalStateException, SystemException, SecurityException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		try
		{
			conn1 = ds1.getConnection();
			UserTransaction utx = (UserTransaction) context.lookup("/UserTransaction");
			try
			{
				Statement st = conn1.createStatement();
				String sql = "UPDATE PRODUCTO SET NO_VALIDO="+ 0 +" WHERE RESTAURANTE ='"+restaurante+"'";
				st.executeUpdate(sql);
				st.close();
			}
			catch (SQLException e) {
				utx.setRollbackOnly();
			}
			utx.commit();
			stop();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally 
		{
			try {
				if(this.conn1!=null)
					this.conn1.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}			
		}
	}
}
