package demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.symantec.network.routeworker.RouteworkerApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RouteworkerApplication.class)
@WebAppConfiguration
public class RouteworkerApplicationTests {

	@Test
	public void contextLoads() {
	}

}
