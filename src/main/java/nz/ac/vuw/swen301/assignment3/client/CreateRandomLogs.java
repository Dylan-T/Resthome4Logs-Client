package nz.ac.vuw.swen301.assignment3.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * creates random logs (using random messages and levels) in an infinite loop at a rate of  1 LogEvent per second.
 * Logging is set up to use the Resthome4LogsAppender appender
 */
public class CreateRandomLogs {

    /**
     *
     */
    public static void main(String[] args) {
        try {
            Logger logger = Logger.getLogger("test1");
            logger.addAppender(new Resthome4LogsAppender());
            while(true) {
                //LoggingEvent logEvent = new LoggingEvent("", logger, Level.INFO, "Test", null);
                logger.error("error");
                TimeUnit.SECONDS.sleep(1);
            }
        }catch (Exception e){

        }
    }
}
