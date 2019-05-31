package nz.ac.vuw.swen301.assignment3.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * creates random logs (using random messages and levels) in an infinite loop at a rate of ca 1 LogEvent per second.
 * Logging is set up to use the Resthome4LogsAppender appender
 */
public class CreateRandomLogs {

    /**
     *
     */
    public static void main(String[] args) {
        try {
            while(true) {
                URIBuilder builder = new URIBuilder();
                builder.setScheme("http").setHost("localhost:8080").setPath("/resthome4logs/logs")
                        .setParameter("LogEvent", "");
                URI uri = builder.build();

                // create and execute the request
                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpGet request = new HttpGet(uri);
                HttpResponse response = httpClient.execute(request);
                TimeUnit.SECONDS.sleep(1);
            }
        }catch (Exception e){

        }
    }
}
