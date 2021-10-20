package com.gifted.rss;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RssParserApplication.class)
public class RssParserApplicationTest {

	protected MockMvc mvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Test
	public void contextLoads() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		Assertions.assertThat(mvc).isNotNull();
	}

	@Test
	public void main() {
		RssParserApplication.main(new String[] {});
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		Assertions.assertThat(mvc).isNotNull();
	}
}
