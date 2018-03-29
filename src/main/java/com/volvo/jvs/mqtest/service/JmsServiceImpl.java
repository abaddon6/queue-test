package com.volvo.jvs.mqtest.service;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;


@Component
public class JmsServiceImpl implements JmsService{

	  private static String host = "eowyn-a3.it.volvo.net";
	  private static int port = 1437;
	  private static String channel = "ADTJAVA.SRV01";
	  private static String user = "a250868";
	  private static String password = null;
	  private static String queueManagerName = "EOWYN_A3";
	  private static String destinationName = "ADTJAVA.POS.PARTS.OUT";
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
}
