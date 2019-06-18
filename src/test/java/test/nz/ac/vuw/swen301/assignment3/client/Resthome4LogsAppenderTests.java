package test.nz.ac.vuw.swen301.assignment3.client;

import junit.framework.TestCase;
import nz.ac.vuw.swen301.assignment3.client.LogMonitor;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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


    @Test
    public void testLogCache()throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger logger = Logger.getLogger("testLOGCACHE");
        Resthome4LogsAppender appender = new Resthome4LogsAppender(3);
        logger.addAppender(appender);
        logger.error("error test");
        logger.debug("debug test");

        assertEquals(2, appender.logCache.size());
        logger.debug("debug test");
        assertEquals(0, appender.logCache.size());
    }

    @Test
    public void testERROR()throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger logger = Logger.getLogger("testERROR");
        Resthome4LogsAppender appender = new Resthome4LogsAppender(1);
        logger.addAppender(appender);
        LoggingEvent log = new LoggingEvent("", logger, Level.ERROR
                , "error message", null);
        appender.doAppend(log);

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(LOGS_PATH)
                .setParameter("limit", "1")
                .addParameter("level","ERROR");
        URI uri = builder.build();
        HttpResponse response = get(uri);

        assertTrue(EntityUtils.toString(response.getEntity()).contains("\"message\":\"error message\",\"timestamp\":\""
                + formatCurrentTime(System.currentTimeMillis())
                + "\",\"thread\":\"main\",\"logger\":\"testERROR\",\"level\":\"ERROR\",\"errorDetails\":\"\"}]"));
    }

    @Test
    public void testDEBUG()throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger logger = Logger.getLogger("testDEBUG");
        Resthome4LogsAppender appender = new Resthome4LogsAppender(1);
        logger.addAppender(appender);
        LoggingEvent log = new LoggingEvent("", logger, Level.DEBUG
                , "DEBUG message", null);
        appender.doAppend(log);

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(LOGS_PATH)
                .setParameter("limit", "1")
                .addParameter("level","DEBUG");
        URI uri = builder.build();
        HttpResponse response = get(uri);

        assertTrue(EntityUtils.toString(response.getEntity()).contains("\"message\":\"DEBUG message\",\"timestamp\":\""
                + formatCurrentTime(System.currentTimeMillis())
                + "\",\"thread\":\"main\",\"logger\":\"testDEBUG\",\"level\":\"DEBUG\",\"errorDetails\":\"\"}]"));
    }

    @Test
    public void testINFO()throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger logger = Logger.getLogger("testINFO");
        Resthome4LogsAppender appender = new Resthome4LogsAppender(1);
        logger.addAppender(appender);
        LoggingEvent log = new LoggingEvent("", logger, Level.INFO
                , "INFO message", null);
        appender.doAppend(log);

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(LOGS_PATH)
                .setParameter("limit", "1")
                .addParameter("level","INFO");
        URI uri = builder.build();
        HttpResponse response = get(uri);

        assertTrue(EntityUtils.toString(response.getEntity()).contains("\"message\":\"INFO message\",\"timestamp\":\""
                + formatCurrentTime(System.currentTimeMillis())
                + "\",\"thread\":\"main\",\"logger\":\"testINFO\",\"level\":\"INFO\",\"errorDetails\":\"\"}]"));
    }

    @Test
    public void testWARN()throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger logger = Logger.getLogger("testWARN");
        Resthome4LogsAppender appender = new Resthome4LogsAppender(1);
        logger.addAppender(appender);
        LoggingEvent log = new LoggingEvent("", logger, Level.WARN
                , "WARN message", null);
        appender.doAppend(log);

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(LOGS_PATH)
                .setParameter("limit", "1")
                .addParameter("level","WARN");
        URI uri = builder.build();
        HttpResponse response = get(uri);

        assertTrue(EntityUtils.toString(response.getEntity()).contains("\"message\":\"WARN message\",\"timestamp\":\""
                + formatCurrentTime(System.currentTimeMillis())
                + "\",\"thread\":\"main\",\"logger\":\"testWARN\",\"level\":\"WARN\",\"errorDetails\":\"\"}]"));
    }

    @Test
    public void testFATAL()throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger logger = Logger.getLogger("testFATAL");
        Resthome4LogsAppender appender = new Resthome4LogsAppender(1);
        logger.addAppender(appender);
        LoggingEvent log = new LoggingEvent("", logger, Level.FATAL
                , "fatal message", null);
        appender.doAppend(log);

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(LOGS_PATH)
                .setParameter("limit", "1")
                .addParameter("level","FATAL");
        URI uri = builder.build();
        HttpResponse response = get(uri);

        assertTrue(EntityUtils.toString(response.getEntity()).contains("\"message\":\"fatal message\",\"timestamp\":\""
                + formatCurrentTime(System.currentTimeMillis())
                + "\",\"thread\":\"main\",\"logger\":\"testFATAL\",\"level\":\"FATAL\",\"errorDetails\":\"\"}]"));
    }

    @Test
    public void testTrace()throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger logger = Logger.getLogger("testTRACE");
        Resthome4LogsAppender appender = new Resthome4LogsAppender(1);
        logger.addAppender(appender);
        LoggingEvent log = new LoggingEvent("", logger, Level.TRACE
                , "TRACE message", null);
        appender.doAppend(log);

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(LOGS_PATH)
                .setParameter("limit", "1")
                .addParameter("level","TRACE");
        URI uri = builder.build();
        HttpResponse response = get(uri);

        assertTrue(EntityUtils.toString(response.getEntity()).contains("\"message\":\"TRACE message\",\"timestamp\":\""
                + formatCurrentTime(System.currentTimeMillis())
                + "\",\"thread\":\"main\",\"logger\":\"testTRACE\",\"level\":\"TRACE\",\"errorDetails\":\"\"}]"));
    }

    public String formatCurrentTime(long timestamp){
        Date date = new Date(timestamp);
        DateFormat formatter = new SimpleDateFormat("dd:MM:YYYY");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    // LOG MONITOR TESTS ===============================================================================================

    @Test
    public void testGUI() throws Exception {
        Assume.assumeTrue(isServerReady());
        LogMonitor gui = new LogMonitor();
        gui.doFetchStats();
        gui.doDownloadStats();
    }

}
