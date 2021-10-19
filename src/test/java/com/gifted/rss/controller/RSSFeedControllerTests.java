package com.gifted.rss.controller;

import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RSSFeedControllerTests {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void testGetItems() throws Exception {
        mockMvc.perform(get("/items?page=1&size=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", IsCollectionWithSize.hasSize(0)));
    }

    @Test
    public void testGetEmptyItems() throws Exception {
        mockMvc.perform(get("/items?page=1&size=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.first").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.empty").value(true));
    }

}