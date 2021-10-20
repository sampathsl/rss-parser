package com.gifted.rss;

import com.gifted.rss.controller.RSSFeedController;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RssParserApplication.class)
public class RssParserApplicationTest {

	@Autowired
	private RSSFeedController rssFeedController;

	@org.junit.Test
	public void contextLoads() {
		Assertions.assertThat(rssFeedController).isNotNull();
	}

	@Test
	public void main() {
		RssParserApplication.main(new String[] {});
		Assertions.assertThat(rssFeedController).isNotNull();
	}
}
