package dtm;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

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
import javax.ws.rs.core.Context;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;

import jms.NonReplyException;
import jms.RF18MQ;
import rest.RequerimientosServices.Respuesta;
import tm.TM;
public class RotonAndesDistributed 
{
	private final static String QUEUE_NAME = "java:global/18";
	private final static String MQ_CONNECTION_NAME = "java:global/IT5";
	
	private static RotonAndesDistributed instance;
	
	private TM tm;
	
	private QueueConnectionFactory queueFactory;
	
	private TopicConnectionFactory factory;
	
	private RF18MQ req18;
	
	private static String path;


	private RotonAndesDistributed() throws NamingException, JMSException
	{
		InitialContext ctx = new InitialContext();
		factory = (RMQConnectionFactory) ctx.lookup(MQ_CONNECTION_NAME);
		req18 = new RF18MQ(factory, ctx);
		req18.start();
		
	}
	
	public void stop() throws JMSException
	{
		req18.close();
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
	
	private static RotonAndesDistributed getInst()
	{
		return instance;
	}
	
	public static RotonAndesDistributed getInstance(TM tm)
	{
		if(instance == null)
		{
			try {
				instance = new RotonAndesDistributed();
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
	
	public static RotonAndesDistributed getInstance()
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

	public boolean registrarPedidoOrdenD(Respuesta res) throws JsonGenerationException, JsonMappingException, NoSuchAlgorithmException, JMSException, IOException, NonReplyException, InterruptedException {
		
		return req18.registrar(res);
	}

	public Respuesta registrarLocal(Respuesta res) throws Exception {
		return tm.registrarPedidoOrden(res.mesa, res.fecha, res.productos, res.usuarios);
	}

	public void marcarAprovada(String datos) throws Exception {
		tm.marcarAprovada(datos);
	}
}
