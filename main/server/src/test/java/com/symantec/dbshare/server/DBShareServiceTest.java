package com.symantec.dbshare.server;

import com.symantec.dbshare.model.DBShare;
import com.symantec.dbshare.server.util.DBShareBuilder;
import com.symantec.network.message.DBShareTask;

import mockit.Mock;
import mockit.MockUp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@TestExecutionListeners(inheritListeners = false, listeners = {
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class })
public class DBShareServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	DBShareService service;

	private List<DBShare> dbshares = new ArrayList<DBShare>();
	private static String filePath = "./test-dbshare-file.txt";
	private static int count = 5;
	private static String message = "Message was sent.";

	@BeforeTest
	protected void setUp() {
		writeDBShareFile(filePath, count);

		// Do not use RabbitTemplate to send messages in tests
		new MockUp<RabbitTemplate>() {
			@Mock
			public void convertAndSend(String routingKey, final Object object) {
				System.out.print(message);
			}
		};
	}

	@AfterTest
	protected void tearDown() {
		deleteDBShareFile(filePath);
	}

	@AfterClass
	protected void destroy() {
		try {
			service.deleteAllDBShares();
		} catch (Exception ignore) {

		}
	}

	@Test
	public void testCreateDBShare_EnterNull() throws Exception {
		DBShare dbshare = service.createDBShare(null);
		Assert.assertNull(dbshare);
	}

	@Test(expectedExceptions = IOException.class)
	public void testLoadDBShare_NoFile() throws IOException {
		Random randomGenerator = new Random();
		service.loadDBShares("file_does_not_exists" + randomGenerator.nextInt() + ".txt");
	}

	@Test
	public void testLoadDBShare_WithEmptyFile() throws IOException {
		String filename = "file_is_empty_" + System.currentTimeMillis() + ".txt";
		File f = new File(filename);
		f.createNewFile();
		service.loadDBShares(filename);
		deleteDBShareFile(filename);
	}

	@Test
	public void testLoadDBShare_WithFile() throws IOException {

		long actual = service.loadDBShares(filePath);
		Assert.assertEquals(actual, count);

	}

	private void writeDBShareFile(String filePath, int count) {
		try {
			PrintWriter writer = new PrintWriter(filePath, "UTF-8");
			for (int i = 0; i < count; i++) {
				DBShare dbshare = DBShareBuilder.builder();
				writer.println(dbshare.getId() + "|" + dbshare.getName() + "|"
						+ dbshare.getPath() + "|" + dbshare.getType() + "|"
						+ dbshare.getSize() + "|" + dbshare.getClients() + "|"
						+ dbshare.getOptions());
				dbshares.add(dbshare);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void deleteDBShareFile(String filePath) {
		File deleteFile = new File(filePath);
		try {
			deleteFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test()
	public void testGetDBShare_Success() throws Exception {
		DBShare expected = dbshares.get(0);

		service.loadDBShares(filePath);
		DBShare actual = service.getDBShare(expected.getId());

		Assert.assertEquals(actual.toString(), expected.toString());
	}

	@Test
	public void testGetDBShare_NoException() throws Exception {
		service.getDBShare(UUID.randomUUID().toString());
	}

	@Test
	public void testGetDBShareStatus_ExpectedNull() throws Exception {
		service.loadDBShares(filePath);
		Assert.assertNull(service.getDBShareStatus(dbshares.get(0).getId()));
	}

	@Test
	public void testSaveDBShareStatus_getDBShareStatus_ExpectedSuccess()
			throws Exception {
		service.loadDBShares(filePath);
		final String[] status = { "[DONE]", "[ERROR]" };

		for (int i = 0; i < status.length; i++) {
			DBShare dbshare = dbshares.get(i);
			String id = dbshare.getId();
			String expected = status[i];

			service.saveDBShareStatus(id, expected);

			Assert.assertEquals(expected, service.getDBShareStatus(id));
		}
	}

	@Test
	public void testSearchDBShares_ExpectedSuccess() throws Exception {
		service.loadDBShares(filePath);
		List<DBShare> dbshares = service.searchDBShares("", 0, count);
		Assert.assertEquals(dbshares.size(), count);

		List<DBShare> dbshares1 = service.searchDBShares("", 0, 1);
		Assert.assertEquals(dbshares1.size(), 1);

		List<DBShare> dbshares2 = service.searchDBShares(null, 1, 1);
		Assert.assertEquals(dbshares1.size(), 1);

		List<DBShare> dbshare3 = service.searchDBShares("Test", 0, count);
		Assert.assertEquals(dbshare3.size(), count);

		Assert.assertNotEquals(dbshares1.get(0).toString(), dbshares2.get(0)
				.toString());
	}

	@Test
	public void testCreateDBShare_ExpectedSuccess() throws Exception {
		DBShare expected = DBShareBuilder.builder();
		String id = expected.getId();

		service.createDBShare(expected);

		DBShare actual = service.getDBShare(id);
		Assert.assertEquals(actual.toString(), expected.toString());

		service.deleteAllDBShares();

		List<DBShare> all = service.searchDBShares("", 0, count);
		Assert.assertEquals(all.size(), 0);
	}

	@Test
	public void testCreateDBShare_NullId() throws Exception {
		DBShare to_create = DBShareBuilder.builder();
		String original_id = to_create.getId();
		to_create.setId(null);

		DBShare actual = service.createDBShare(to_create);
		String actual_id = actual.getId();

		// make sure UUID is generated by server
		Assert.assertNotEquals(actual_id, original_id);
		Assert.assertEquals(actual_id.length(), 36);
	}

	@Test
	public void testCreateDBShare_EmptyId() throws Exception {
		DBShare to_create = DBShareBuilder.builder();
		String original_id = to_create.getId();
		to_create.setId("");

		DBShare actual = service.createDBShare(to_create);
		String actual_id = actual.getId();

		Assert.assertNotEquals(actual_id, original_id);
		Assert.assertEquals(actual_id.length(), 36);
	}

	@Test
	public void testRunStorageWorker() throws Exception {
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));

		DBShareTask task = new DBShareTask();
		String action = "create something";
		task.setDBShare(dbshares.get(0));
		task.setAction(action);

		Assert.assertEquals(task.getDBShare(), dbshares.get(0));
		Assert.assertEquals(task.getAction(), action);

		service.sendTask(task);
		Assert.assertEquals(outContent.toString(), message);

		System.setOut(null);
		System.setErr(null);
	}
}
