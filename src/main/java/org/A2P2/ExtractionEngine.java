package org.A2P2;
import java.io.*;
import java.net.*;

public class ExtractionEngine {

    /**
     * Extract news from newsapi and pass this data to dataprocessing Engine
     */
    public void extractsNews() {
        try {

            // Keyword to search in newsAPI for fetching news
            String[] keywords = {"Canada", "University", "Dalhousie", "Halifax", "Canada Education", "Moncton", "hockey", "Fredericton", "celebration"};

            //join all keywords with "OR"
            String query = String.join(" OR ", keywords);
            String encodedQuery = URLEncoder.encode(query, "UTF-8");

            //URL to hit for fetching all results
            String urlStr = "https://newsapi.org/v2/everything?q=" + encodedQuery + "&apiKey=ab3a6355793f4756bcda3e9f15b8b792";
            URL url = new URL(urlStr);

            //Making connection with the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            //Storing all response from API in string
            StringBuilder response = new StringBuilder();

            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();

            //Calling process method of data-processing to store in files
            DataProcessingEngine dataProcessingEngine=new DataProcessingEngine();
            dataProcessingEngine.process(response);

        } catch (IOException e) {
                e.printStackTrace();
            }
        }
}