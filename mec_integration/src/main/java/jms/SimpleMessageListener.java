package jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.amdocs.mec.MECInteraction;

public class SimpleMessageListener implements MessageListener {
	public void onMessage(Message message) {
		try {
			System.out.println("recieved message : " + message.getBooleanProperty("BOOLEAN_PROPERTY"));
			MECInteraction.invokeProcess();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
