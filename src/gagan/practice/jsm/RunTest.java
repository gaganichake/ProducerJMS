package gagan.practice.jsm;


import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

public class RunTest {

	private static String jndiConnectionFactoryName = "jndiActiveMQConnctionFactory";
	private static String jndiQueueName = "jndiActiveMQ";

	private static String user = ActiveMQConnection.DEFAULT_USER;
	private static String pwd = ActiveMQConnection.DEFAULT_PASSWORD;
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

	// private static String url = "tcp://Gagan-PC:61616";

	/**
	 * @param args
	 * @throws NamingException
	 */
	public static void main(String[] args) throws NamingException {

		JMSQueueSender jmsQueueSender = new JMSQueueSender();

		// JNDI object binding : START
		/*
		 * Note: Following stuff can/should be done in J2EE server
		 */
		QueueConnectionFactory queueConnectionFactory = new ActiveMQConnectionFactory(
				user, pwd, url);
		Queue queue = new ActiveMQQueue("FOO.BAR");

		jmsQueueSender.getInitialContext().rebind(jndiConnectionFactoryName,
				queueConnectionFactory);
		jmsQueueSender.getInitialContext().rebind(jndiQueueName, queue);
		// JNDI object binding : END
		
		jmsQueueSender.setJndiConnectionFactory(jndiConnectionFactoryName);
		jmsQueueSender.setJndiQueue(jndiQueueName);
		jmsQueueSender.setMessage("Hello Gagan");
		jmsQueueSender.sendMessage();
	}

}
