package com.symantec.dbshare.server;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import mockit.Mock;
import mockit.MockUp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.symantec.dbshare.model.DBShare;
import com.symantec.dbshare.model.DBShareURIConstants;
import com.symantec.dbshare.server.util.DBShareBuilder;
import com.symantec.dbshare.server.util.MockMvcTestUtil;

@WebAppConfiguration
@SpringApplicationConfiguration(classes = { Application.class })
@TestExecutionListeners(inheritListeners = false, listeners = {
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class })
public class MockMvcTest extends AbstractTestNGSpringContextTests {
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@BeforeClass
	public void setUp() throws Exception {
		this.mockMvc = webAppContextSetup(context).build();
	}

	@Test
	public void testDBSharesHome() throws Exception {
		this.mockMvc.perform(get(DBShareURIConstants.LIST_DBSHARE))
				.andExpect(status().isOk()).andExpect(content().string("[]"));
	}

	@Test
	public void testCreateEmptyDBShare() throws Exception {
		DBShare dbshare = DBShareBuilder.createDBShare(null, "", "", "", "",
				"", "");
		mockMvc.perform(
				post(DBShareURIConstants.CREATE_DBSHARE).contentType(
						MockMvcTestUtil.UTF8).content(
						MockMvcTestUtil.convertObjectToJsonBytes(dbshare)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testUnexpcetedException() throws Exception {
		new MockUp<DBShareService>() {
			@Mock
			public DBShare getDBShare(String id) throws Exception {
				throw new Exception("Unexpected Exception");
			}
		};
		String id = "c6ebfa60-7055-11e4-8cdb-3c15c2c9e11a";
		mockMvc.perform(get(DBShareURIConstants.GET_DBSHARE, id)).andExpect(
				status().is5xxServerError());
	}
}
