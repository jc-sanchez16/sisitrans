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


public class RFC14MQ implements MessageListener, ExceptionListener 
{
	public final static int TIME_OUT = 5;
	private final static String APP = "app1";
	
	private final static String GLOBAL_TOPIC_NAME1 = "java:global/RFC14.1";
	private final static String GLOBAL_TOPIC_NAME2 = "java:global/RFC14.2";
	private final static String GLOBAL_TOPIC_NAME3 = "java:global/RFC14.3";
	
	private final static String REQUEST = "REQUEST";
	private final static String REQUEST_ANSWER = "REQUEST_ANSWER";
	
	private TopicConnection topicConnection;
	private TopicSession topicSession;
	private Topic globalTopic1;
	private Topic globalTopic2;
	private Topic globalTopic3;
	
	private String answer1 = null;
	private String answer2 = null;
	
	public RFC14MQ(TopicConnectionFactory factory, InitialContext ctx) throws JMSException, NamingException 
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
	
	public String consultarRentabilidad(String restaurante, Date fecI, Date fecF) throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException, NoSuchAlgorithmException
	{
		answer1= null;
		answer2= null;
		String id = APP+""+System.currentTimeMillis();
		MessageDigest md = MessageDigest.getInstance("MD5");
		id = DatatypeConverter.printHexBinary(md.digest(id.getBytes())).substring(0, 8);
//		id = new String(md.digest(id.getBytes()));
		sendMessage(restaurante+";"+fecI.getTime()+";"+fecF.getTime(), REQUEST, globalTopic2, id);
		sendMessage(restaurante+";"+fecI.getTime()+";"+fecF.getTime(), REQUEST, globalTopic3, id);
		boolean waiting = true;
		int count = 0;
		while(TIME_OUT != count){
			TimeUnit.SECONDS.sleep(1);
			count++;
		}
		if(count == TIME_OUT){
			if(this.answer1== null || this.answer2== null){
				waiting = false;
				throw new NonReplyException("Time Out - No Reply");
			}
		}
		waiting = false;
		
		if(this.answer1== null || this.answer2== null)
			throw new NonReplyException("Non Response");
		String res= "\"App2\"= "+ answer1;
		res += ", \"App3\"= "+ answer2;
		return res;
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
					String[] pet = ex.getPayload().split(";");
					String resp = dtm.consultarRentabilidadL(pet[0], new Date(Long.parseLong(pet[1])), new Date(Long.parseLong(pet[2])));
					Topic t = ex.getSender().equals("app2") ? globalTopic2:globalTopic3;
					sendMessage(resp, REQUEST_ANSWER,t , id);
					
				}
				else if(ex.getStatus().equals(REQUEST_ANSWER))
				{
					if(ex.getSender().equals("app2"))
					{
						answer1 =ex.getPayload();
					}
					else
					{
						answer2 =ex.getPayload();
					}
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
