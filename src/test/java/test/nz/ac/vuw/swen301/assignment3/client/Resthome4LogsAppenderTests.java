package test.nz.ac.vuw.swen301.assignment3.client;

import junit.framework.TestCase;
import nz.ac.vuw.swen301.assignment3.client.Resthome4LogsAppender;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class Resthome4LogsAppenderTests {
    private static final String TEST_HOST = "localhost";
    private static final int TEST_PORT = 8080;
    private static final String TEST_PATH = "/resthome4logs"; // as defined in pom.xml
    private static final String LOGS_PATH = TEST_PATH + "/logs";


    private HttpResponse get(URI uri) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);
        return httpClient.execute(request);
    }

    private boolean isServerReady() throws Exception {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(LOGS_PATH)
                .addParameter("level","WARN").addParameter("limit", "25");
        URI uri = builder.build();
        try {
            HttpResponse response = get(uri);
            boolean success = response.getStatusLine().getStatusCode() == 200;

            if (!success) {
                System.err.println("Check whether server is up and running, request to " + uri
                        + " returns " + response.getStatusLine());
            }

            return success;
        }
        catch (Exception x) {
            System.err.println("Encountered error connecting to " + uri
                    + " -- check whether server is running and application has been deployed");
            return false;
        }
    }


    // APPENDER TESTS ==================================================================================================

    //public LoggingEvent(String fqnOfCategoryClass, Category logger, Priority level, Object message, Throwable throwable)
    //public LoggingEvent(String fqnOfCategoryClass, Category logger, long timeStamp, Priority level, Object message, Throwable throwable)

    @Test
    public void testLogCache()throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger logger = Logger.getLogger("test3");
    }

    @Test
    public void testClose()throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger logger = Logger.getLogger("test4");
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        logger.addAppender(appender);
        logger.error("Message");
    }

    @Test
    public void testERROR()throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger logger = Logger.getLogger("test7");
        Resthome4LogsAppender appender = new Resthome4LogsAppender(1);
        logger.addAppender(appender);
        LoggingEvent log = new LoggingEvent("", logger, Level.ERROR, "error message", null);
        appender.doAppend(log);
        TimeUnit.SECONDS.sleep(1);

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(LOGS_PATH)
                .setParameter("limit", "5")
                .addParameter("level","DEBUG");
        URI uri = builder.build();
        HttpResponse response = get(uri);

        TimeUnit.SECONDS.sleep(1);
        System.out.println(EntityUtils.toString(response.getEntity()));

        //assertEquals(logevent, EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testDEBUG()throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger logger = Logger.getLogger("test8");
        Layout layout = new PatternLayout();
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        logger.addAppender(appender);
        logger.debug("message");
        //assertEquals(memAppender.getCurrentLogs().size(), 1);
    }

    @Test
    public void testINFO()throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger logger = Logger.getLogger("test9");
        Layout layout = new PatternLayout();
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        logger.addAppender(appender);
        logger.info("message");
        //assertEquals(memAppender.getCurrentLogs().size(), 1);
    }

    @Test
    public void testWARN()throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger logger = Logger.getLogger("test10");
        Layout layout = new PatternLayout();
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        logger.addAppender(appender);
        logger.warn("message");
        //assertEquals(memAppender.getCurrentLogs().size(), 1);
    }

    @Test
    public void testFATAL()throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger logger = Logger.getLogger("test11");
        Layout layout = new PatternLayout();
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        logger.addAppender(appender);
        logger.fatal("Message");
        //assertEquals(memAppender.getCurrentLogs().size(), 1);
    }

    @Test
    public void testTrace()throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger logger = Logger.getLogger("test12");
        logger.setLevel(Level.TRACE);
        Layout layout = new PatternLayout();
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        logger.addAppender(appender);
        logger.trace("Message");
        //assertEquals(memAppender.getCurrentLogs().size(), 1);
    }

}
