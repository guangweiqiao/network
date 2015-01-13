package com.symantec.network.interfaceworker;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.symantec.network.message.NetworkTask;
import com.symantec.network.model.NetworkConfigConstants;
import com.symantec.network.model.PhysicalInterfaceInfo;
import com.symantec.network.utils.NetworkCommandFactory;
import com.symantec.network.utils.ProcessUtil;

@Service
public class InterfaceWorker {
	
	private static Logger logger = Logger.getLogger(InterfaceWorker.class);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	public void configureIPAddress(NetworkTask task){
		PhysicalInterfaceInfo ifInfo = (PhysicalInterfaceInfo)task.getIfInfo();
		
		LinkedHashMap<String, String> optionMap = new LinkedHashMap<String,String>();
		optionMap = ifInfo.generateCommandOptionMap(optionMap);
		String[] commands = NetworkCommandFactory.createCommand(NetworkConfigConstants.COMMAND_IFCONFIG, optionMap);
		
		logger.info("command array:" + Arrays.toString(commands));
		
		String output = null;
		try {
			output= ProcessUtil.launchProcess(commands);
		} catch (TimeoutException | RuntimeException | IOException
				| InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(output);
	}
}
