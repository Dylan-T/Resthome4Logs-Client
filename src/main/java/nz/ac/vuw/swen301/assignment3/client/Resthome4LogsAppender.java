package nz.ac.vuw.swen301.assignment3.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.net.URI;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class Resthome4LogsAppender extends AppenderSkeleton {

    private final long maxSize = 10;
    private long discardedLogs = 0;
    private List<String> currentLogs;
    private Layout layout;



    public Resthome4LogsAppender(){

    }

    /**
     * Returns the logs currently stored
     * @return list containing the current logs, must not be modifiable
     */
    public List<String> getCurrentLogs() {
        return Collections.unmodifiableList(this.currentLogs);
    }

    /**
     * Returns the 10 most recent logs stored
     * @return
     */
    public String[] getTopLogs() {
        //Check if <10 logs stored
        int numLogs = 10;
        if(getCurrentLogs().size() < 10){
            numLogs = getCurrentLogs().size();
        }

        //Add 10 most recent to array
        String[] topLogs = new String[10];
        for(int i = 0; i < numLogs; i++){
            topLogs[i] = currentLogs.get(currentLogs.size()-1-i);
        }
        return topLogs;
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        //Note that arrays of log events (and not just single log events) are processed.
        try {
            URIBuilder builder = new URIBuilder();
            builder.setScheme("http").setHost("localhost:8080").setPath("/resthome4logs/logs")
                    .setParameter("LogEvent", "");
            URI uri = builder.build();

            // create and execute the request
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(uri);
            HttpResponse response = httpClient.execute(request);

            // this string is the unparsed web page (=html source code)
            String content = EntityUtils.toString(response.getEntity());
            System.out.println(content);
        }catch (Exception e){

        }
    }

    /**
     * resets the appender's memory and fields
     */
    public void close() {
        currentLogs.clear();
        discardedLogs = 0;
        this.closed = true;
    }

    /**
     * Returns whether this appender requires a layout
     * @return
     */
    public boolean requiresLayout() {
        return true;
    }

    @Override
    public void setLayout(Layout layout){
        if(layout == null){
            throw new IllegalArgumentException();
        }
        this.layout = layout;
    }
}
