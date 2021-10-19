package com.gifted.rss;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RssParserApplication.class)
public class RssParserApplicationTest {

	@org.junit.Test
	public void contextLoads() {}

	@Test
	public void main() {
		RssParserApplication.main(new String[] {});
	}
}
