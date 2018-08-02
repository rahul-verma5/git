package jms;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSListener {

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
	private static QueueReceiver qrcvr = null;


	public JMSListener(){
		super();
	}

	public static void main(String[] args) {
		try{
			recieveMessage();
		}finally {
			q = null;
			try{qsess.close();}catch(Exception e){}
			qsess = null;
			try{qc.close();}catch(Exception e){}
			qc = null;
			qcf = null;
			ctx = null;
		}
	}

	public static void recieveMessage(){
		Hashtable<String , String> properties = new Hashtable<String, String>();
		properties.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		properties.put(Context.PROVIDER_URL, PROVIDER_URL);
		properties.put(Context.SECURITY_PRINCIPAL, USERNAME);
		properties.put(Context.SECURITY_CREDENTIALS, PASSWORD);

		try {
			ctx = new InitialContext(properties);
			System.out.println("Got initial context : " + ctx.toString());

			qcf = (QueueConnectionFactory) ctx.lookup(CONNECTION_FACTORY_NAME);
			System.out.println("Got qcf :  " + qcf.toString());

			qc = qcf.createQueueConnection();
			System.out.println("Got qc :  " + qc.toString());
			
			qsess = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("Got qsess :  " + qsess.toString());

			q = (Queue)ctx.lookup(QUEUE_NAME);
			System.out.println("Got q :  " + q.toString());

			qrcvr = qsess.createReceiver(q);
			System.out.println("Got qsndr :  " + qrcvr.toString());

			qrcvr.setMessageListener(new SimpleMessageListener());
			qc.start();
			Thread.sleep(100);
			
			System.out.println("message received");
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
