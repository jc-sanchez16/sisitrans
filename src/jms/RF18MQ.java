package jms;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.jms.DeliveryMode;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.DatatypeConverter;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;

import dtm.RotonAndesDistributed;
import rest.RequerimientosServices.Respuesta;
import vos.ExchangeMsg;


public class RF18MQ implements MessageListener, ExceptionListener 
{
	public final static int TIME_OUT = 1000000;
	private final static String APP = "app1";
	
	private final static String GLOBAL_TOPIC_NAME1 = "java:global/RF19.1";
	private final static String GLOBAL_TOPIC_NAME2 = "java:global/RF19.2";
	private final static String GLOBAL_TOPIC_NAME3 = "java:global/RF19.3";
	
	private final static String REQUEST = "REQUEST";
	private final static String REQUEST_ANSWER = "REQUEST_ANSWER";
	private final static String REQUEST_ACTION = "REQUEST_ACTION";
	
	private TopicConnection topicConnection;
	private TopicSession topicSession;
	private Topic globalTopic1;
	private Topic globalTopic2;
	private Topic globalTopic3;
	
	private Respuesta answer = new Respuesta();
	
	private int appA =0;
	
	public RF18MQ(TopicConnectionFactory factory, InitialContext ctx) throws JMSException, NamingException 
	{	
		topicConnection = factory.createTopicConnection();
		topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		globalTopic1 = (RMQDestination) ctx.lookup(GLOBAL_TOPIC_NAME1);
		globalTopic2 = (RMQDestination) ctx.lookup(GLOBAL_TOPIC_NAME2);
		globalTopic3 = (RMQDestination) ctx.lookup(GLOBAL_TOPIC_NAME3);
		TopicSubscriber topicSubscriber =  topicSession.createSubscriber(globalTopic1);
    	topicSubscriber.setMessageListener(this);
		topicSubscriber =  topicSession.createSubscriber(globalTopic2);
		topicSubscriber =  topicSession.createSubscriber(globalTopic3);
		topicConnection.setExceptionListener(this);
	}
	
	public void start() throws JMSException
	{
		topicConnection.start();
	}
	
	public void close() throws JMSException
	{
		topicSession.close();
		topicConnection.close();
	}
	
	public boolean registrar(Respuesta rest) throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException, NoSuchAlgorithmException
	{
		answer= new Respuesta();
		appA=0;
		String id = APP+""+System.currentTimeMillis();
		MessageDigest md = MessageDigest.getInstance("MD5");
		id = DatatypeConverter.printHexBinary(md.digest(id.getBytes())).substring(0, 8);
//		id = new String(md.digest(id.getBytes()));
		ObjectMapper map = new ObjectMapper();
		sendMessage(map.writeValueAsString(rest), REQUEST, globalTopic1, id);
		boolean waiting = true;
		int count = 0;
		while(TIME_OUT != count){
			TimeUnit.SECONDS.sleep(1);
			count++;
		}
		if(count == TIME_OUT){
			if(this.answer.isEmpty){
				waiting = false;
				throw new NonReplyException("Time Out - No Reply");
			}
		}
		waiting = false;
		
		if(answer.isEmpty || appA!=2)
			throw new NonReplyException("Non Response");
		sendMessage(map.writeValueAsString(answer), REQUEST, globalTopic2, id);
		waiting = true;
		count = 0;
		while(TIME_OUT != count){
			TimeUnit.SECONDS.sleep(1);
			count++;
		}
		if(count == TIME_OUT){
			if(this.answer.isEmpty){
				waiting = false;
				throw new NonReplyException("Time Out - No Reply");
			}
		}
		waiting = false;
		
		if(answer.isEmpty)
			throw new NonReplyException("Non Response");
		if(answer.productos.isEmpty())
		{
			sendMessage(rest.mesa+";"+rest.fecha, REQUEST_ACTION, globalTopic1, id);
			sendMessage(rest.mesa+";"+rest.fecha, REQUEST_ACTION, globalTopic2, id);
			return true;
		}	
		return false;
	}
	
	
	private void sendMessage(String payload, String status, Topic dest, String id) throws JMSException, JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(id);
		ExchangeMsg msg = new ExchangeMsg("videos.general", APP, payload, status, id);
		TopicPublisher topicPublisher = topicSession.createPublisher(dest);
		topicPublisher.setDeliveryMode(DeliveryMode.PERSISTENT);
		TextMessage txtMsg = topicSession.createTextMessage();
		txtMsg.setJMSType("TextMessage");
		String envelope = mapper.writeValueAsString(msg);
		System.out.println(envelope);
		txtMsg.setText(envelope);
		topicPublisher.publish(txtMsg);
	}
	
	@Override
	public void onMessage(Message message) 
	{
		TextMessage txt = (TextMessage) message;
		try 
		{
			String body = txt.getText();
			System.out.println(body);
			ObjectMapper mapper = new ObjectMapper();
			ExchangeMsg ex = mapper.readValue(body, ExchangeMsg.class);
			String id = ex.getMsgId();
			System.out.println(ex.getSender());
			System.out.println(ex.getStatus());
			if(!ex.getSender().equals(APP))
			{
				if(ex.getStatus().equals(REQUEST))
				{
					
					RotonAndesDistributed dtm = RotonAndesDistributed.getInstance();
					Respuesta resp = dtm.registrarLocal(new Respuesta());
					String payload = mapper.writeValueAsString(mapper.readValue(ex.getPayload(), Respuesta.class));
					Topic t = new RMQDestination(payload, "1", ex.getRoutingKey(), "", false);
					sendMessage(payload, REQUEST_ANSWER, t, id);
				}
				else if(ex.getStatus().equals(REQUEST_ANSWER))
				{
					appA=Integer.parseInt(ex.getSender().charAt(3)+"");
					answer = mapper.readValue(ex.getPayload(), Respuesta.class);
				}
				else if (ex.getStatus().equals(REQUEST_ACTION))
				{
					
					RotonAndesDistributed dtm = RotonAndesDistributed.getInstance();
					dtm.marcarAprovada(ex.getPayload());
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onException(JMSException exception) 
	{
		System.out.println(exception);
	}

}
