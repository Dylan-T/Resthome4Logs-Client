package nz.ac.vuw.swen301.assignment3.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.javafx.util.Logging;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *{
 *     "id": "d290f1ee-6c54-4b01-90e6-d701748f0851",
 *     "message": "application started",
 *     "timestamp": {},
 *     "thread": "main",
 *     "logger": "com.example.Foo",
 *     "level": "DEBUG",
 *     "errorDetails": "string"
 *   }
 */
public class Resthome4LogsAppender extends AppenderSkeleton {
    private List<String> logCache = new ArrayList<>();
    private int cacheLimit;

    public Resthome4LogsAppender(){
        cacheLimit = 10;
    }

    public Resthome4LogsAppender(int cacheLimit){
        if(cacheLimit < 1){
            this.cacheLimit = 1;
        }else{
            this.cacheLimit = cacheLimit;
        }
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        logCache.add(formatEvent(loggingEvent).toString());
        if(logCache.size() >= cacheLimit){
            postLogs(logCache);
            logCache.clear();
        }
    }

    private void postLogs(List<String> loggingEvents) {
        try {
            URIBuilder builder = new URIBuilder();
            builder.setScheme("http").setHost("localhost:8080").setPath("/resthome4logs/logs");
            URI uri = builder.build();

            // create and execute the request
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(uri);

            //Create entity
            System.out.println(new Gson().toJson(loggingEvents));
            StringEntity params = new StringEntity(new Gson().toJson(loggingEvents));
            params.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            request.setEntity(params);

            //Execute request
            HttpResponse response = httpClient.execute(request);

            String content = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {

        }
    }

    public JsonObject formatEvent(LoggingEvent loggingEvent){
        JsonObject json = new JsonObject();
        json.addProperty("id", UUID.randomUUID().toString());
        json.addProperty("message", (String)loggingEvent.getMessage());

        Date date = new Date(loggingEvent.getTimeStamp());
        DateFormat formatter = new SimpleDateFormat("dd:MM:YYYY");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);

        json.addProperty("timestamp", dateFormatted);
        json.addProperty("thread", loggingEvent.getThreadName());
        json.addProperty("logger", loggingEvent.getLoggerName());
        json.addProperty("level", loggingEvent.getLevel().toString());
        String errorDetails;
        if(loggingEvent.getThrowableInformation() == null){
            errorDetails = "";
        }else{
            errorDetails = loggingEvent.getThrowableInformation().toString();
        }
        json.addProperty("errorDetails", errorDetails);
        return json;
    }


    @Override
    public void close() {

    }

    /**
     * Returns whether this appender requires a layout
     * @return
     */
    public boolean requiresLayout() {
        return false;
    }

}
