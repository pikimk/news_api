package com.example.newsapi.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class FetchThread  {
    private String result;
    private final String URL = "https://feeds.nos.nl/nosnieuwsalgemeen";

    @Autowired
    private NewsService service;

    // change timing here - fixedDelay (in miliseconds)
    @Scheduled(fixedDelay = 10000, initialDelay = 0)
    @Async
    //Retrieves data from URL in XML format every 300000 milisec (5min)
    public void getData() {
            try {
                URL url = new URL(URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                InputStream is = connection.getInputStream();
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
                StringBuilder respponse = new StringBuilder();
                String line;
                while ((line = bf.readLine()) != null)  respponse.append(line);    //while buffer is not empty - append
                bf.close();
                result = respponse.toString();                                     //XML response
                connection.disconnect();
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            List<NewsObject> parsedList = parseObjects();               //List of Parsed JSON obj.
            if (!parsedList.isEmpty()) {                                //Check if the list is empty
                service.saveAdjacent(parsedList);                       //Save just adjacent, and keep ID on old records
            }
    }

    //Returns List of News Objects parsed from JSON (MAX 10)
    private List<NewsObject> parseObjects() {
        List<NewsObject> news = new ArrayList<>();
        if (result != null) {
            JSONObject jsonObject = XML.toJSONObject(result);                      //XML parse from string to JSON obj
            JSONObject rss = (JSONObject) jsonObject.get("rss");                   //Static keys
            JSONObject channel = (JSONObject) rss.get("channel");
            JSONArray items = (JSONArray) channel.get("item");

            int size = items.length();
            if (size > 10) size = 10;                                                           // Set max results 10
            for (int i = 0; i < size; i++) {                                                    // Loop all items
                JSONObject item = items.getJSONObject(i);                                       // Parse Keys
                JSONObject enclosure = (JSONObject) item.opt("enclosure");
                String url = enclosure.getString("url");
                String description = item.getString("description");
                String title = item.getString("title");
                String date = item.getString("pubDate");
                NewsObject newsObject = new NewsObject(title,clean(description),url, date);       // Construct News obj.
                news.add(newsObject);                                                             // Add to array
            }
        }
        return news;
    }

    // Function to clean the Description String from HTML tags inside the string, with regex
    private String clean(String dirty){
        String regex = "\\<.*?\\>";                                                                //regex
        String clean  = dirty.replaceAll(regex, "");
        return clean;
    }

}
