package gagan.practice.jsm;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/*
 *  Basic steps:
 *  
 *  Context  jndiContext = new InitialContext(parms);
 *  queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup(jndiConnectionFactory);
 *  replyQueue = (Queue) jndiContext.lookup(jndiQueue);
 *  queueConnection = queueConnectionFactory.createQueueConnection();
 *  queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
 * 
 *  queueBrowser = queueSession.createBrowser(replyQueue);
 *  queueSender = queueSession.createSender(sendQueue);
 *  queueReceiver = queueSession.createReceiver(replyQueue, aMessageSelector);
 *  queueReceiver = queueSession.createReceiver(replyQueue);
 * 
 *  queueConnection.start();
 */

public abstract class JMSQueueBase {

	// JMS Properties managed connection properties
	private String initial_context_factory;
	private String url_pkg_prefixes;
	private String provider_Url;
	
	private String jndiConnectionFactory;
	private String jndiQueue;

	private QueueConnectionFactory queueConnectionFactory = null;
	private Queue queue = null;
	private QueueConnection queueConnection = null;
	private QueueSession queueSession = null;
	
	private Context initialContext = null;

	/**
	 * MQQueueSender constructor
	 */
	public JMSQueueBase() {
		super();
	}

	/**
	 * toString()
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("\nProvider URL        = " + provider_Url);
		buf.append("\nInitial Context Factory        = "
				+ initial_context_factory);
		buf.append("\nJNDI Connection Factory  = " + jndiConnectionFactory);
		buf.append("\nJNDI Queue Name          = " + jndiQueue);

		return buf.toString();
	}

	/**
	 * Lazy initialize InitialContext
	 * 
	 * @throws NamingException
	 */
	@SuppressWarnings("unchecked")
	public Context getInitialContext() throws NamingException {
		if (initialContext == null) {
			if (initial_context_factory != null) {
				
				// Initialise manually
				Hashtable<String, String> environment = new Hashtable<String, String>();
				environment.put(Context.INITIAL_CONTEXT_FACTORY, initial_context_factory);
				if(url_pkg_prefixes != null){
					environment.put(Context.URL_PKG_PREFIXES, url_pkg_prefixes);
				}
				if(provider_Url != null){
					environment.put(Context.PROVIDER_URL, provider_Url);
				}
				initialContext = new InitialContext(environment);
			} else {
				
				// Initialise through property file (Default name: "jndi.properties")
				initialContext = new InitialContext();
			}
		}
		return initialContext;
	}

	/**
	 * Lazy initialize QueueConnectionFactory
	 */
	public QueueConnectionFactory getQueueConnectionFactory() throws Exception {
		if (queueConnectionFactory == null) {
			queueConnectionFactory = (QueueConnectionFactory) getInitialContext()
					.lookup(jndiConnectionFactory);
		}
		return queueConnectionFactory;
	}

	/**
	 * Lazy initialize Queue
	 */
	public Queue getQueue() throws Exception {
		if (queue == null) {
			queue = (Queue) getInitialContext().lookup(jndiQueue);
		}
		return queue;
	}

	/**
	 * Lazy initialize QueueConnection
	 * 
	 * @throws Exception
	 * @throws JMSException
	 */
	public QueueConnection getQueueConnection() throws JMSException, Exception {
		if (queueConnection == null) {
			queueConnection = getQueueConnectionFactory()
					.createQueueConnection();
		}
		return queueConnection;
	}

	/**
	 * Lazy initialize QueueSession
	 * 
	 * @throws Exception
	 * @throws JMSException
	 */
	public QueueSession getQueueSession() throws JMSException, Exception {
		if (queueSession == null) {
			queueSession = getQueueConnection().createQueueSession(false,
					Session.AUTO_ACKNOWLEDGE);
		}
		return queueSession;
	}

	/**
	 * Start the QueueConnection
	 * 
	 * @throws Exception
	 * @throws JMSException
	 */
	public void startQueueConnection() throws JMSException, Exception {
		getQueueConnection().start();
	}

	/**
	 * Close QueueConnection
	 */
	public void closeQueueConnection() {
		if (queueConnection != null) {
			try {
				queueConnection.close();
				queueConnection = null;
			} catch (javax.jms.JMSException jmse) {
				jmse.printStackTrace();//
			}
		}
	}

	/**
	 * Close QueueSession
	 */
	public void closeQueueSession() {
		if (queueSession != null) {
			try {
				queueSession.close();
				queueSession = null;
			} catch (javax.jms.JMSException jmse) {
				jmse.printStackTrace();
			}
		}
	}

	public String getInitial_context_factory() {
		return initial_context_factory;
	}

	public void setInitial_context_factory(String initial_context_factory) {
		if (!initial_context_factory.equals(this.initial_context_factory)) {
			this.initial_context_factory = initial_context_factory;
			this.initialContext = null;
			setQueueConnectionFactory(null);
			setQueue(null);
		}
	}

	/**
	 * @return the url_pkg_prefixes
	 */
	public String getUrl_pkg_prefixes() {
		return url_pkg_prefixes;
	}

	/**
	 * @param url_pkg_prefixes the url_pkg_prefixes to set
	 */
	public void setUrl_pkg_prefixes(String url_pkg_prefixes) {
		if (!url_pkg_prefixes.equals(this.url_pkg_prefixes)) {
			this.url_pkg_prefixes = provider_Url;
			this.initialContext = null;
			setQueueConnectionFactory(null);
			setQueue(null);
		}
	}
	
	public String getProvider_Url() {
		return provider_Url;
	}

	public void setProvider_Url(String provider_Url) {
		if (!provider_Url.equals(this.provider_Url)) {
			this.provider_Url = provider_Url;
			this.initialContext = null;
			setQueueConnectionFactory(null);
			setQueue(null);
		}
	}

	public String getJndiConnectionFactory() {
		return jndiConnectionFactory;
	}

	public void setJndiConnectionFactory(String jndiConnectionFactory) {
		if (!jndiConnectionFactory.equals(this.jndiConnectionFactory)) {
			this.jndiConnectionFactory = jndiConnectionFactory;
			setQueueConnectionFactory(null);
		}
	}

	public String getJndiQueue() {
		return jndiQueue;
	}

	public void setJndiQueue(String jndiQueue) {
		if (!jndiQueue.equals(this.jndiQueue)) {
			this.jndiQueue = jndiQueue;
			setQueue(null);
		}
	}

	private void setQueueConnectionFactory(
			QueueConnectionFactory queueConnectionFactory) {
			this.queueConnectionFactory = queueConnectionFactory;
			setQueueConnection(null);
	}

	private void setQueueConnection(QueueConnection queueConnection) {
		this.queueConnection = queueConnection;
		setQueueSession(null);
	}

	private void setQueueSession(QueueSession queueSession) {
		this.queueSession = queueSession;
	}

	private void setQueue(Queue queue) {
		this.queue = queue;
	}

}
