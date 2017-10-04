package gagan.practice.jsm;


import java.io.Serializable;
import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.QueueSender;
import javax.jms.QueueSession;

public class JMSQueueSender extends JMSQueueBase {

	private Serializable message;
	private String messageID;

	/**
	 * MQQueueSender constructor
	 */
	public JMSQueueSender() {
		super();
	}

	/**
	 * Send message to JMS Send Queue.
	 * http://www-1.ibm.com/support/docview.wss?uid=swg21206150 Logic: queue =
	 * (Queue) getInitialContext().lookup(jndiQueue); queueConnectionFactory =
	 * (QueueConnectionFactory)
	 * getInitialContext().lookup(jndiConnectionFactory);
	 * 
	 * queueConnection = queueConnectionFactory.createQueueConnection()
	 * queueSession = queueConnection.createQueueSession() queueSender =
	 * queueSession.createSender(queue)
	 * 
	 * message.setIntProperty("JMS_IBM_Report_Expiration", MQFB_EXPIRATION)
	 * message.setIntProperty("JMS_IBM_Report_COA", MQFB_COA)
	 * message.setIntProperty("JMS_IBM_Report_COD", MQFB_COD)
	 * queueSender.send(message, DeliveryMode=PERSISTENT=2,
	 * MessagePriority=DEFAULT_PRIORITY=4, TimeToLive=0(never expire))
	 * 
	 * queueSender.close() queueSession.close() queueConnection.close()
	 * 
	 * queueSender = null queueSession = null queueConnection = null
	 * 
	 * @return String The messageID
	 */
	public String sendMessage() {
		QueueSender queueSender = null;
		messageID = "";

		try {
			queueSender = getQueueSession().createSender(getQueue());

			startQueueConnection();

			Message jmsMessage = null;
			if (message instanceof String) {
				jmsMessage = getQueueSession().createTextMessage(
						(String) message);
			} else {
				jmsMessage = getQueueSession().createObjectMessage(message);
			}

			queueSender.send(jmsMessage);

			messageID = jmsMessage.getJMSMessageID();

		} catch (JMSException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeQueueSender(queueSender);
			closeQueueSession();
			closeQueueConnection();
		}

		return messageID;
	}

	/**
	 * Close the supplied QueueSender
	 * 
	 * @param queueSender
	 */
	private void closeQueueSender(QueueSender queueSender) {
		if (queueSender != null) {
			try {
				queueSender.close();
				queueSender = null;
			} catch (javax.jms.JMSException jmse) {
				jmse.printStackTrace();
			}
		}
	}

	public int getQueueDepth() throws Exception {
		int depth = 0;
		try {
			QueueSession queueSession = getQueueSession();

			startQueueConnection();

			QueueBrowser queueBrowse = queueSession.createBrowser(getQueue());

			Enumeration e = queueBrowse.getEnumeration();
			while (e.hasMoreElements()) {
				e.nextElement();
				depth++;
			}
			return depth;
		} catch (Exception e) {
			throw e;
		} finally {
			closeQueueSession();
			closeQueueConnection();
		}
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Serializable message) {
		this.message = message;
	}

}
