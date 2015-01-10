package com.symantec.network.interfaceworker;

import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.symantec.network.message.AbstractMsgConfig;
import com.symantec.network.message.NetworkTask;

@Configuration
public class MsgConfig extends AbstractMsgConfig {

    private final static String taskQueue = NetworkTask.QUEUE;

    MsgConfig() {
        super(taskQueue);
    }

    @Bean
    Receiver receiver() {
        return new Receiver();
    }

    @Bean
    MessageListenerAdapter listernerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver);
    }
}
