package com.symantec.network.interfaceworker;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.symantec.network.message.NetworkTask;
import com.symantec.network.model.PhysicalInterfaceInfo;

@Service
public class InterfaceWorker {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	public void configureIPAddress(NetworkTask task){
		PhysicalInterfaceInfo ifInfo = (PhysicalInterfaceInfo)task.getIfInfo();
		System.out.println(ifInfo);
	}
}
