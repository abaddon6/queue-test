package com.volvo.jvs.mqtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MqTestApplicationTests {

	// code identifier
	  static final String sccsid = "@(#) MQMBID sn=p800-002-150217.2 su=_7QzOILa2EeS2tKt4Znd7Ug pn=MQJavaSamples/wmqjava/MQSample.java";

	  // define the name of the QueueManager
	  private static final String qManager = "my_queue_manager";
	  // and define the name of the Queue
	  private static final String qName = "SYSTEM.DEFAULT.LOCAL.QUEUE";
	  
	@Test
	public void contextLoads() throws Exception{
		
		// Create a connection to the QueueManager
	      System.out.println("Connecting to queue manager: " + qManager);
	      MQQueueManager qMgr = new MQQueueManager(qManager);

	      // Set up the options on the queue we wish to open
	      int openOptions = MQConstants.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_OUTPUT;

	      // Now specify the queue that we wish to open and the open options
	      System.out.println("Accessing queue: " + qName);
	      MQQueue queue = qMgr.accessQueue(qName, openOptions);

	      // Define a simple WebSphere MQ Message ...
	      MQMessage msg = new MQMessage();
	      // ... and write some text in UTF8 format
	      msg.writeUTF("Hello, World!");

	      // Specify the default put message options
	      MQPutMessageOptions pmo = new MQPutMessageOptions();

	      // Put the message to the queue
	      System.out.println("Sending a message...");
	      queue.put(msg, pmo);
	}

}
