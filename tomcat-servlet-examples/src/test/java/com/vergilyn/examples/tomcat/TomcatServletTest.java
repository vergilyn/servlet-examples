package com.vergilyn.examples.tomcat;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author VergiLyn
 * @date 2019-05-21
 */
public class TomcatServletTest {
    private HttpClient httpClient;

    @BeforeTest
    private void before(){
        httpClient = HttpClients.custom()
                .setMaxConnPerRoute(10)
                .setMaxConnTotal(20)
                .build();
    }

    @Test(threadPoolSize = 7, invocationCount = 7)
    public void test() throws IOException {
        HttpGet httpGet = new HttpGet("http://127.0.0.1:8080/index");

        long begin = System.currentTimeMillis();
        HttpResponse response = httpClient.execute(httpGet);

        long time = System.currentTimeMillis() - begin;

        String rs = EntityUtils.toString(response.getEntity());

        System.out.println("http index >>>> " + time + ", " + rs);


        Assert.assertEquals("index >>> ", "index", rs);
    }
}
