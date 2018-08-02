package jms;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SimpleSender {

	private static String CONNECTION_FACTORY_NAME = "jms/RemoteConnectionFactory";
	private static String QUEUE_NAME = "jms/queue/JMSQ";
	private static String USERNAME = "appuser";
	private static String PASSWORD = "appuser123";
	private static String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	private static String PROVIDER_URL = "http-remoting://127.0.0.1:8080";
	
	private static InitialContext ctx = null;
	private static QueueConnectionFactory qcf = null;
	private static QueueConnection qc = null;
	private static QueueSession qsess = null;
	private static Queue q = null;
	private static QueueSender qsndr = null;
	
	public SimpleSender(){}

	public static void sendMessage(String messageText){
		Hashtable<String , String> properties = new Hashtable<String, String>();
		properties.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		properties.put(Context.PROVIDER_URL, PROVIDER_URL);
		properties.put(Context.SECURITY_PRINCIPAL, USERNAME);
		properties.put(Context.SECURITY_CREDENTIALS, PASSWORD);
		
		try {
			ctx = new InitialContext(properties);
		} catch (NamingException e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Got initial context : " + ctx.toString());
		
		try {
			qcf = (QueueConnectionFactory) ctx.lookup(CONNECTION_FACTORY_NAME);
		} catch (NamingException e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Got qcf :  " + qcf.toString());
		
		try {
			qc = qcf.createQueueConnection();
		} catch (JMSException e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Got qc :  " + qc.toString());
		
		try {
			qsess = qc.createQueueSession(false, 0);
		} catch (JMSException e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Got qsess :  " + qsess.toString());
		
		try {
			q = (Queue)ctx.lookup(QUEUE_NAME);
		} catch (NamingException e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Got q :  " + q.toString());
		
		try {
			qsndr = qsess.createSender(q);
		} catch (JMSException e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Got qsndr :  " + qsndr.toString());
		TextMessage message = null;
		try {
			message = qsess.createTextMessage();
			message.setBooleanProperty("BOOLEAN_PROPERTY", Boolean.TRUE);
			message.setText(messageText);
			qsndr.send(message);
		} catch (JMSException e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("message sent");
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
		sendMessage("test jms message");
		}finally{
			try {qsndr.close();} catch (JMSException e) {}
			qsndr = null;
			q = null;
			try {qsess.close();} catch (JMSException e) {}
			qsess = null;
			try {qc.close();} catch (JMSException e) {}
			qc = null;
			qcf = null;
			ctx = null;
		}

	}

}
