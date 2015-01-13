package com.symantec.network.interfaceworker;

import static com.symantec.network.model.NetworkConfigConstants.ACTION_CONFIGURE_IP;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.symantec.network.message.NetworkTask;

public class Receiver implements MessageListener {	//@Autowired
	private static Logger logger = Logger.getLogger(Receiver.class);

	@Autowired
	InterfaceWorker worker;
	
	@Autowired
	Jackson2JsonMessageConverter messageConverter;

	public void onMessage(Message message) {
		logger.info("Received task:" + message);
		if (message != null) {
			NetworkTask task = (NetworkTask) messageConverter.fromMessage(message);
			String action = task.getAction();
			if(null == action){
				return;
			}
			
			switch(action){
				case ACTION_CONFIGURE_IP:
					worker.configureIPAddress(task);
					break;
				default :
					break;
			}
			
		}
	}

}
