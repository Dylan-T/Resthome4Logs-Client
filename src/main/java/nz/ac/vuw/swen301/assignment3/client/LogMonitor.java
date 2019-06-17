package nz.ac.vuw.swen301.assignment3.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class LogMonitor {
    static JFrame frame;
    private JPanel wrapper;

    private JPanel actionbar;

    private JPanel level;
    private JLabel levelLabel;
    private JComboBox levelSelector;

    JPanel limit;
    JFormattedTextField limitField;

    JButton fetchButton;
    JButton dlButton;


    JScrollPane scrollPane;
    JTable content;



    public LogMonitor() {
        this.wrapper = new JPanel();
        wrapper.setLayout(new BorderLayout());
        actionbar = new JPanel();

        level = new JPanel();
        levelLabel = new JLabel("Level");
        level.add(levelLabel);
        levelSelector = new JComboBox<>();
        levelSelector.addItem("ALL");
        levelSelector.addItem("TRACE");
        levelSelector.addItem("DEBUG");
        levelSelector.addItem("INFO");
        levelSelector.addItem("WARN");
        levelSelector.addItem("ERROR");
        levelSelector.addItem("FATAL");
        levelSelector.addItem("OFF");
        level.add(levelSelector);
        actionbar.add(level);

        limit = new JPanel();
        limit.add(new JLabel("Limit"));
        limitField = new JFormattedTextField();
        limitField.setValue(100);
        limitField.setColumns(6);
        limit.add(limitField);
        actionbar.add(limit);


        fetchButton = new JButton("Fetch Stats");
        fetchButton.addActionListener(e -> doFetchStats());
        actionbar.add(fetchButton);

        dlButton = new JButton("Download Stats");
        dlButton.addActionListener(e -> doDownloadStats());
        actionbar.add(dlButton);

        wrapper.add(actionbar);

        //Create table and add it to the GUI
        doFetchStats();
    }

    private void doDownloadStats() {

    }

    public void doFetchStats(){
        String level = (String) levelSelector.getSelectedItem();
        int limit = (int)limitField.getValue();

        //build request and set parameters
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost:8080").setPath("/resthome4logs/logs")
                .setParameter("limit", ""+limit)
                .addParameter("level", level);
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);

        //execute request
        try {
            HttpResponse response = httpClient.execute(request);
            String logs = EntityUtils.toString(response.getEntity());


            JsonParser jsonParser = new JsonParser();
            // Convert JSON Array String into JSON Array
            JsonArray jsonLogArray = jsonParser.parse(logs).getAsJsonArray();
            //jsonLogArray.get(0).get
            //BUILD RESPONSE INTO TABLE
//            Object[] column = {"Time", "Level", "Logger", "Thread", "Message"};
//            Object[][] data = buildData(logList);
//            content = new JTable(data, column);
//            scrollPane = new JScrollPane(content);
//            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//            wrapper.add(scrollPane, BorderLayout.SOUTH);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    Object[][] buildData(ArrayList<JsonObject> logs){
        // {"id": "d290f1ee-6c54-4b01-90e6-d701748f0851","message": "application started","timestamp": {},"thread": "main","logger": "com.example.Foo","level": "DEBUG","errorDetails": "string"}
        Object[][] data = new Object[logs.size()][5];
        int row = 0;

        for(JsonObject log: logs){

            row++;
        }

        return data;
    }

    public static void main(String[] args) {
        frame = new JFrame("LogMonitor");
        frame.setContentPane(new LogMonitor().wrapper);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
