package com.volvo.jvs.mqtest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.volvo.jvs.mqtest.beans.Result;


@Component
public class JmsServiceImpl implements JmsService{

	  private static String host = "eowyn-a3.it.volvo.net";
	  private static int port = 1437;
	  private static String channel = "ADTJAVA.SRV01";
	  private static String user = "a250868";
	  private static String password = null;
	  private static String queueManagerName = "EOWYN_A3";
	  private static String destinationName = "ADTJAVA.POS.PARTS.OUT";
	  private static String queueName = "ADTJAVA.POS.PARTS.OUT";
	  private static boolean isTopic = false;
	  private static boolean clientTransport = true;
	  
	@Override
	public void sendMessage(String message) {
		// Variables
	    Connection connection = null;
	    Session session = null;
	    Destination destination = null;
	    MessageProducer producer = null;

	    try {
	      // Create a connection factory
	      JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
	      JmsConnectionFactory cf = ff.createConnectionFactory();

	      // Set the properties
	      cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, host);
	      cf.setIntProperty(WMQConstants.WMQ_PORT, port);
	      cf.setStringProperty(WMQConstants.WMQ_CHANNEL, channel);
	      if (clientTransport) {
	          cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
	      }
	      else {
	    	  cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_BINDINGS);
	      }
	      cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, queueManagerName);
	      if (user != null) {
	          cf.setStringProperty(WMQConstants.USERID, user);
	          cf.setStringProperty(WMQConstants.PASSWORD, password);
	          cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
	        }

	      // Create JMS objects
	      connection = cf.createConnection();
	      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	      if (isTopic) {
	        destination = session.createTopic(destinationName);
	      }
	      else {
	        destination = session.createQueue(destinationName);
	      }
	      producer = session.createProducer(destination);

	      // Start the connection
	      connection.start();
	      
	      // Create message
	      TextMessage messageToSend = session.createTextMessage(message);
	      
	      // And, send the message	      
	      producer.send(messageToSend);
	      System.out.println("Sent message:\n" + messageToSend);
	     
	    }
	    catch (JMSException jmsex) {
	      //recordFailure(jmsex);
	    }
	    finally {
	      if (producer != null) {
	        try {
	          producer.close();
	        }
	        catch (JMSException jmsex) {
	          System.out.println("Producer could not be closed.");
	          //recordFailure(jmsex);
	        }
	      }

	      if (session != null) {
	        try {
	          session.close();
	        }
	        catch (JMSException jmsex) {
	          System.out.println("Session could not be closed.");
	          //recordFailure(jmsex);
	        }
	      }

	      if (connection != null) {
	        try {
	          connection.close();
	        }
	        catch (JMSException jmsex) {
	          System.out.println("Connection could not be closed.");
	          //recordFailure(jmsex);
	        }
	      }
	    }
	    return;
	}
	
	@Override
	public List<Result> getMessage() {
		// Variables
	    Connection connection = null;
	    Session session = null;
	    Queue destination = null;
	    QueueBrowser browser = null;
	    List<Result> list = new ArrayList<Result>();

	    try {
	      // Create a connection factory
	      JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
	      JmsConnectionFactory cf = ff.createConnectionFactory();

	      // Set the properties
	      cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, host);
	      cf.setIntProperty(WMQConstants.WMQ_PORT, port);
	      cf.setStringProperty(WMQConstants.WMQ_CHANNEL, channel);
	      if (clientTransport) {
	          cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
	      }
	      else {
	          cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_BINDINGS);
	      }
	      cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, queueManagerName);

	      // Create JMS objects
	      connection = cf.createConnection();
	      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	      //destination = session.createQueue(queueName);
	      
	      Destination queue = session.createQueue(queueName);
	      
	      MessageConsumer consumer = session.createConsumer(queue); 
	      
	     // browser = session.createBrowser(destination);
	      
	      // Start the connection
	      connection.start();

	      Message message = null ; 

	      while ( (message = consumer.receive(10000)) != null ){ 
	          if(message instanceof TextMessage){ 
	            TextMessage tx = (TextMessage)message; 
	            Result result = new Result();
		        result.setTest(tx.getText());
		        list.add(result);	            
	            System.out.println(tx); 
	            // date and time from client machine
	            System.out.println(new Date(tx.getJMSTimestamp()));
	            // date and time from mq server
	            System.out.println(tx.getStringProperty("JMS_IBM_PutDate"));
	            System.out.println(tx.getStringProperty("JMS_IBM_PutTime"));
	         } 
	      }
	      
	      // And, browse the message
	      /*Enumeration<?> messages = browser.getEnumeration();
	      int count = 0;
	      Message current;
	      System.out.println("Browse starts");
	      
	      while (messages.hasMoreElements()) {
	        current = (Message) messages.nextElement();
	        Result result = new Result();
	        result.setTest(current.getBody(String.class));
	        list.add(result);
	        System.out.println("\nMessage " + ++count + ":\n");
	        System.out.println(current);
	      }
	
	      
	      System.out.println("\nNo more messages\n");	
	      */      
	    }
	    catch (JMSException jmsex) {
	      //recordFailure(jmsex);
	    }
	    finally {
	      if (browser != null) {
	        try {
	          browser.close();
	        }
	        catch (JMSException jmsex) {
	          System.out.println("Browser could not be closed.");
	          //recordFailure(jmsex);
	        }
	      }

	      if (session != null) {
	        try {
	        //session.commit();
	          session.close();
	        }
	        catch (JMSException jmsex) {
	          System.out.println("Session could not be closed.");
	          //recordFailure(jmsex);
	        }
	      }

	      if (connection != null) {
	        try {
	          connection.close();
	        }
	        catch (JMSException jmsex) {
	          System.out.println("Connection could not be closed.");
	          //recordFailure(jmsex);
	        }
	      }
	    }
	    return list;
	}
}
