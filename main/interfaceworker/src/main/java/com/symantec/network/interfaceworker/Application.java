package com.symantec.network.interfaceworker;

import java.util.Arrays;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.symantec.network.message.NetworkTask;
import com.symantec.network.model.NetworkConfigConstants;
import com.symantec.network.model.PhysicalInterfaceInfo;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application implements CommandLineRunner{

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
    public static void main(String[] args) {
    	ApplicationContext ctx = SpringApplication.run(Application.class, args);
        
        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

	@Override
	public void run(String... arg0) throws Exception {
		System.out.println("###########################");
		
		PhysicalInterfaceInfo ifInfo = new PhysicalInterfaceInfo("eth1", "10.200.100.10");
		rabbitTemplate.convertAndSend(NetworkTask.QUEUE, new NetworkTask(NetworkConfigConstants.ACTION_CONFIGURE_IP, ifInfo));
	}
}
