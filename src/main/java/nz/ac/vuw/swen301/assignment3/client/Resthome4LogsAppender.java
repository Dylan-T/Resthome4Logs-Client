package nz.ac.vuw.swen301.assignment3.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.javafx.util.Logging;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

    public Resthome4LogsAppender(){ }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        //Note that arrays of log events (and not just single log events) are processed.
        try {

            URIBuilder builder = new URIBuilder();
            builder.setScheme("http").setHost("localhost:8080").setPath("/resthome4logs/logs");
//                    .setParameter("LogEvent", gson.toJson(loggingEvent));
            URI uri = builder.build();

            // create and execute the request
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(uri);

            //            StringEntity params = new StringEntity("details={\"id\":\"" + "INSERTIDHERE" + "\",\"message\":\""+ loggingEvent.getMessage() +"\",\"timestamp\":\"" + loggingEvent.getTimeStamp() + "\",\"thread\":\"" + loggingEvent.getThreadName() + "\",\"logger\":\"" + loggingEvent.getLoggerName() + "\",\"level\":\"" + loggingEvent.getLevel() + "\",\"errorDetails\":\"" + loggingEvent.getThrowableInformation() + "\",} ");
            StringEntity params = new StringEntity(formatEvent(loggingEvent).toString());
            params.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);

            //Not sure what the response is
            String content = EntityUtils.toString(response.getEntity());
            System.out.println(content);
        }catch (Exception e){

        }
    }

    public JsonObject formatEvent(LoggingEvent loggingEvent){
        JsonObject json = new JsonObject();
        json.addProperty("id", 1);
        json.addProperty("message", (String)loggingEvent.getMessage());
        json.addProperty("timestamp", loggingEvent.getTimeStamp());
        json.addProperty("thread", loggingEvent.getThreadName());
        json.addProperty("logger", loggingEvent.getLoggerName());
        json.addProperty("level", loggingEvent.getLevel().toString());
        json.addProperty("errorDetails", loggingEvent.getNDC());
        return json;


    }

    public List<String> getLogs(int limit, String level){
        try {
            URIBuilder builder = new URIBuilder();
            builder.setScheme("http").setHost("localhost:8080").setPath("/resthome4logs/logs")
                    .setParameter("limit", ""+limit)
                    .addParameter("level", level);

            URI uri = builder.build();

            // create and execute the request
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(uri);
            HttpResponse response = httpClient.execute(request);

            String logs = EntityUtils.toString(response.getEntity());
            Gson gson = new Gson();
            return gson.fromJson(logs, new ArrayList<LoggingEvent>().getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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
