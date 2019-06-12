package test.nz.ac.vuw.swen301.assignment3.client;

import com.google.gson.Gson;
import nz.ac.vuw.swen301.assignment3.client.Resthome4LogsAppender;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;

public class Resthome4LogsAppenderTests {
    private static final String TEST_HOST = "localhost";
    private static final int TEST_PORT = 8080;
    private static final String TEST_PATH = "/resthome4logs"; // as defined in pom.xml
    private static final String LOGS_PATH = TEST_PATH + "/logs";

    //HELPER METHODS
//    @BeforeClass
//    public static void startServer() throws Exception {
//        Runtime.getRuntime().exec("mvn jetty:run");
//        Thread.sleep(3000);
//    }
//
//    @AfterClass
//    public static void stopServer() throws Exception {
//        Runtime.getRuntime().exec("mvn jetty:stop");
//        Thread.sleep(3000);
//    }

    private HttpResponse get(URI uri) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);
        return httpClient.execute(request);
    }

    private boolean isServerReady() throws Exception {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(TEST_PATH);
        URI uri = builder.build();
        try {
            HttpResponse response = get(uri);
            boolean success = response.getStatusLine().getStatusCode() == 200;

            if (!success) {
                System.err.println("Check whether server is up and running, request to " + uri + " returns " + response.getStatusLine());
            }

            return success;
        }
        catch (Exception x) {
            System.err.println("Encountered error connecting to " + uri + " -- check whether server is running and application has been deployed");
            return false;
        }
    }

    @Test
    public void testGet() throws Exception{
        Assume.assumeTrue(isServerReady());
        Resthome4LogsAppender appender = new Resthome4LogsAppender();// TODO: Check assignment 2 for test idea's

    }


    @Test
    public void test(){
        /**
         * [
         *   {
         *     "id": "d290f1ee-6c54-4b01-90e6-d701748f0851",
         *     "message": "application started",
         *     "timestamp": {},
         *     "thread": "main",
         *     "logger": "com.example.Foo",
         *     "level": "DEBUG",
         *     "errorDetails": "string"
         *   }
         * ]
         */
        Logger logger = Logger.getLogger("test1");
        LoggingEvent logEvent = new LoggingEvent("", logger, Level.INFO, "Test", null);
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        String json = appender.formatEvent(logEvent).toString();
        System.out.println(json);
        System.out.println(logEvent.toString());
        System.out.println(logEvent.getMessage());
    }
}
