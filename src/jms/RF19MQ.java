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


public class RF19MQ implements MessageListener, ExceptionListener 
{
	public final static int TIME_OUT = 5;
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
	
	public RF19MQ(TopicConnectionFactory factory, InitialContext ctx) throws JMSException, NamingException 
	{	
		topicConnection = factory.createTopicConnection();
		topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		globalTopic1 = (RMQDestination) ctx.lookup(GLOBAL_TOPIC_NAME1);
		globalTopic2 = (RMQDestination) ctx.lookup(GLOBAL_TOPIC_NAME2);
		globalTopic3 = (RMQDestination) ctx.lookup(GLOBAL_TOPIC_NAME3);
		TopicSubscriber topicSubscriber =  topicSession.createSubscriber(globalTopic1);
    	topicSubscriber.setMessageListener(this);
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
	
	public void retirar(String restaurante) throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException, NoSuchAlgorithmException
	{
		String id = APP+""+System.currentTimeMillis();
		MessageDigest md = MessageDigest.getInstance("MD5");
		id = DatatypeConverter.printHexBinary(md.digest(id.getBytes())).substring(0, 8);
//		id = new String(md.digest(id.getBytes()));
		sendMessage(restaurante, REQUEST, globalTopic2, id);
		sendMessage(restaurante, REQUEST, globalTopic3, id);
	}
	
	
	private void sendMessage(String payload, String status, Topic dest, String id) throws JMSException, JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(id);
		ExchangeMsg msg = new ExchangeMsg("orden", APP, payload, status, id);
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
					dtm.retirarRestauranteL(ex.getPayload());
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
