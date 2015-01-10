package com.symantec.dbshare.server;

import java.util.ArrayList;
import java.util.List;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Tested;
import mockit.Verifications;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.symantec.dbshare.model.DBShare;
import com.symantec.dbshare.server.util.DBShareBuilder;

@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@TestExecutionListeners(inheritListeners = false, listeners = {
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class })
public class DBShareControllerTest extends AbstractTestNGSpringContextTests {
	@Tested
	DBShareController controller;

	@Injectable
	DBShareService service;

	@BeforeTest
	protected void setUp() {
		// Do not use RabbitTemplate to send messages in tests
		new MockUp<RabbitTemplate>() {
			@Mock
			public void convertAndSend(String routingKey, final Object object) {
			}
		};
	}

	@AfterClass
	protected void destroy() {
		try {
			service.deleteAllDBShares();
		} catch (Exception ignore) {

		}
	}

	@Test
	public void testSearchDBShares() throws Exception {
		final List<DBShare> dbshares = new ArrayList<DBShare>();
		dbshares.add(DBShareBuilder.builder());

		new Expectations() {
			{
				service.searchDBShares("", 0, 20);
				returns(dbshares);
			}
		};

		List<DBShare> actual = controller.searchDBShares("", 0, 20);
		Assert.assertEquals(dbshares, actual);

		new Verifications() {
			{
				service.searchDBShares("", 0, 20);
				times = 1;
			}
		};
	}

	@Test
	public void testCreateDBShare() throws Exception {
		final DBShare dbshare = DBShareBuilder.builder();

		new Expectations() {
			{
				service.createDBShare(dbshare);
				returns(dbshare);
			}
		};

		DBShare actual = controller.createDBShare(dbshare);
		Assert.assertNull(actual.getStatus());

		new Verifications() {
			{
				service.createDBShare(dbshare);
				times = 1;
			}
		};

		service.deleteAllDBShares();
	}

	@Test
	public void testGetDBShare() throws Exception {
		final DBShare expected = DBShareBuilder.builder();
		final String id = expected.getId();

		new Expectations() {
			{
				service.getDBShare(id);
				returns(expected);
			}
		};

		DBShare actual = controller.getDBShare(id);
		Assert.assertEquals(actual.getName(), expected.getName());
		Assert.assertEquals(actual.getPath(), expected.getPath());
		Assert.assertEquals(actual.getType(), expected.getType());
		Assert.assertEquals(actual.getSize(), expected.getSize());
		Assert.assertEquals(actual.getClients(), expected.getClients());
		Assert.assertEquals(actual.getOptions(), expected.getOptions());

		new Verifications() {
			{
				service.getDBShare(id);
				times = 1;
			}
		};
	}

	@Test
	public void testGetDBShareStatus() throws Exception {
		DBShare dbshare = DBShareBuilder.builder();
		final String id = dbshare.getId();

		new Expectations() {
			{
				service.getDBShareStatus(id);
				returns(null);
			}
		};

		Assert.assertNull(controller.getDBShareStatus(id));

		new Verifications() {
			{
				service.getDBShareStatus(id);
				times = 1;
			}
		};
	}

	@Test(expectedExceptions = Exception.class)
	public void testValidationError_EmptyObject() throws Exception {
		final DBShare dbshare = new DBShare("", "");

		new Expectations() {
			{
				controller.createDBShare(dbshare);
				result = new Exception();
			}
		};

		controller.createDBShare(dbshare);
	}
}
