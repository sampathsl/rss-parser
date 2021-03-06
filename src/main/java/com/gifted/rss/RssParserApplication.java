/**
 * @author  Sampath Thennakoon
 * @version 1.0
 * @since   2021-10-20
 */

package com.gifted.rss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RssParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(RssParserApplication.class, args);
	}

}
