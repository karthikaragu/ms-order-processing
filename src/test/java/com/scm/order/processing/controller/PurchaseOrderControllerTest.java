package com.scm.order.processing.controller;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class PurchaseOrderControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    MockWebServer userWebServer;
    MockWebServer productWebServer;

    @Before
    public void init() throws IOException {
        userWebServer = new MockWebServer();
        userWebServer.start(8011);
        productWebServer = new MockWebServer();
        productWebServer.start(8021);
        mockMvc = MockMvcBuilders.webAppContextSetup(this.ctx).build();
    }

    @After
    public void tearDown() throws IOException{
        userWebServer.shutdown();
        productWebServer.shutdown();
    }

    @Test
    public void createOrder_happyflow() throws IOException, Exception{
        userWebServer.enqueue(new MockResponse().setResponseCode(200).addHeader("Content-Type","application/json").setBody("true"));
        productWebServer.enqueue(new MockResponse().setResponseCode(200).addHeader("Content-Type","application/hal+json")
                .setBody(fetchProductResponse("src/test/resources/products.json")));
        MvcResult result = this.mockMvc.perform(post("/customer/createorder")
                            .contentType("application/json")
                            .header("Authorization","Basic Z291dGhhbTpKdW5AMjA=")
                            .content("{\n" +
                                    "\t\"orderedBy\" : 7,\n" +
                                    "\t\"orderDetails\" : [{\n" +
                                    "\t\t\"productId\" : 2,\n" +
                                    "\t\t\"quantity\" : 1\n" +
                                    "\t},{\n" +
                                    "\t\t\"productId\" : 4,\n" +
                                    "\t\t\"quantity\" : 1\n" +
                                    "\t}]\n" +
                                    "\t\n" +
                                    "}"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());
        Assert.assertFalse(result.getResponse().getContentAsString().contains("\"order\":null"));
        Assert.assertTrue(result.getResponse().getContentAsString().contains("\"errors\":[]"));
    }

    @Test
    public void createOrder_badrequest() throws IOException, Exception{
        MvcResult result = this.mockMvc.perform(post("/customer/createorder")
                .contentType("application/json")
                .header("Authorization","Basic Z291dGhhbTpKdW5AMjA=")
                .content("{\n" +
                        "\t\"orderedBy\" : null,\n" +
                        "\t\"orderDetails\" : [{\n" +
                        "\t\t\"productId\" : null,\n" +
                        "\t\t\"quantity\" : null\n" +
                        "\t},{\n" +
                        "\t\t\"productId\" : null,\n" +
                        "\t\t\"quantity\" : null\n" +
                        "\t}]\n" +
                        "\t\n" +
                        "}"))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());
        Assert.assertTrue(result.getResponse().getContentAsString().contains("\"order\":null"));
        Assert.assertTrue(result.getResponse().getContentAsString().contains("\"OP-FV01\""));
    }

    @Test
    public void dipatchOrder() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/dealer/dispatchorder")
                .param("productId","4")
                .param("orderId","1")
                .contentType("application/json")
                .header("Authorization","Basic Z291dGhhbTpKdW5AMjA="))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        Assert.assertEquals("Order Detail Dispatched Successfully" ,result.getResponse().getContentAsString());
    }

    @Test
    public void dipatchOrder_errorflow() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/dealer/dispatchorder")
                .param("productId","5")
                .param("orderId","1")
                .contentType("application/json")
                .header("Authorization","Basic Z291dGhhbTpKdW5AMjA="))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        Assert.assertEquals("Order Detail Not Found!!" ,result.getResponse().getContentAsString());
    }

    private String fetchProductResponse(String filePath) throws IOException {
        return Files.readString(Paths.get(filePath));
    }
}


