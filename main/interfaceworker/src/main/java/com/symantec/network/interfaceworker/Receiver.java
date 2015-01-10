package com.symantec.network.interfaceworker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import com.symantec.network.message.NetworkTask;
import static com.symantec.network.model.NetworkConfigConstants.CONFIGURE_IP;

public class Receiver implements MessageListener {	//@Autowired
	Logger logger = LoggerFactory.getLogger(Receiver.class);

	@Autowired
	InterfaceWorker worker;

	public void onMessage(Message message) {
		logger.info("Received task: {}", message);
		if (message != null) {
			Jackson2JsonMessageConverter jmc = new Jackson2JsonMessageConverter();
			NetworkTask task = (NetworkTask) jmc.fromMessage(message);
			String action = task.getAction();
			if(null == action){
				return;
			}
			
			switch(action){
				case CONFIGURE_IP:
					worker.configureIPAddress(task);
					break;
				default :
					break;
			}
			
		}
	}

}
