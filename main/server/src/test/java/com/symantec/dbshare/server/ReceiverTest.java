package com.symantec.dbshare.server;

import java.util.UUID;

import mockit.Expectations;
import mockit.Injectable;
import mockit.NonStrictExpectations;
import mockit.Tested;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;

import com.symantec.dbshare.model.DBShare;
import com.symantec.dbshare.server.util.DBShareBuilder;
import com.symantec.network.message.DBShareEvent;

@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@TestExecutionListeners(inheritListeners = false, listeners = {
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class })
public class ReceiverTest extends AbstractTestNGSpringContextTests {
	@Tested
	Receiver receiver;

	@Injectable
	DBShareService service;

	@Test
	public void testOnMessage_ValidMessage() throws Exception {
		final String id = UUID.randomUUID().toString();
		final String status = "[DONE]";

		new NonStrictExpectations() {
			{
				service.getDBShare(id);
				DBShare dbshare = DBShareBuilder.createDBShare(id);
				dbshare.setStatus(status);
				result = dbshare;
			}
		};

		new Expectations() {
			{
				service.saveDBShareStatus(id, status);
				DBShare dbshare = DBShareBuilder.createDBShare(id);
				dbshare.setStatus(status);
				result = dbshare;
			}
		};

		DBShareEvent event = new DBShareEvent();
		event.setStatus(status);
		event.setId(id);

		Jackson2JsonMessageConverter jmc = new Jackson2JsonMessageConverter();
		MessageProperties messageProperties = new MessageProperties();
		Message message = jmc.toMessage(event, messageProperties);

		receiver.onMessage(message);
	}

	@Test
	public void testOnMessage_NullDBShare() throws Exception {
		final String id = UUID.randomUUID().toString();
		final String status = "[DONE]";

		new NonStrictExpectations() {
			{
				service.getDBShare(id);
				result = null;
			}
		};

		DBShareEvent event = new DBShareEvent(id, status);

		Jackson2JsonMessageConverter jmc = new Jackson2JsonMessageConverter();
		MessageProperties messageProperties = new MessageProperties();
		Message message = jmc.toMessage(event, messageProperties);

		receiver.onMessage(message);
	}

	@Test(expectedExceptions = Exception.class)
	public void testOnMessage_throwException() throws Exception {
		DBShareEvent event = null;
		Jackson2JsonMessageConverter jmc = new Jackson2JsonMessageConverter();
		MessageProperties messageProperties = new MessageProperties();
		Message message = jmc.toMessage(event, messageProperties);

		receiver.onMessage(message);
	}

	@Test
	public void testOnMessage_NullMessage() throws Exception {
		receiver.onMessage(null);
	}
}
