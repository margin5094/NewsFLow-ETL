package org.A2P2;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class TransformationEngine {
    public void transforms() throws IOException {

        String directoryPath = "NewsData";
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        //sort files on the basis of number names as listFiles() method fetch all files randomly.
        Arrays.sort(files, (file1, file2) -> {
            int file1Number = Integer.parseInt(file1.getName().split("\\.")[0]);
            int file2Number = Integer.parseInt(file2.getName().split("\\.")[0]);
            return Integer.compare(file1Number, file2Number);
        });

        /**
         * Connect with MongoDB
         */
        MongoCollection collection=getDatabaseConnection();


        for (File file : files) {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            int count_Each_Record=0;
            ArrayList<String> listOf_Title_And_Content=new ArrayList<>();

            while ((line = bufferedReader.readLine()) != null) {

                //clean and transform title and content
                String cleanedLine=cleanAndTransform(line);
                listOf_Title_And_Content.add(cleanedLine);
                count_Each_Record++;

                //If title and content are read then insert in database
                if(count_Each_Record % 2 == 0){
                    Document document = new Document();
                    document.append("title",listOf_Title_And_Content.get(0));
                    document.append("content",listOf_Title_And_Content.get(1));
                    collection.insertOne(document);
                    listOf_Title_And_Content.clear();
                }
            }
            bufferedReader.close();
            fileReader.close();
        }
    }

    /**
     *
     * @param lineToClean line which is cleaned and transform
     * @return cleaned string is return
     */
    String cleanAndTransform(String lineToClean){

        //Regex to remove url from content & title
        String URL_REGEX = "((http|https)://)?(www\\.)?[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,}(\\S*)?";
        String urlRemoved = lineToClean.replaceAll(URL_REGEX, "");

        //Regex to clean remoticon from content & title
        String EMOTICON_REGEX = "[:;=]-?[)DdpP\\[\\]/\\{\\}]";
        String emoticonRemoved = urlRemoved.replaceAll(EMOTICON_REGEX, "");

        //Regex to clean special character from content & title
        String SPECIAL_CHARACTER_REGEX = "[^a-zA-Z0-9\\s]";
        String cleanedLine = emoticonRemoved.replaceAll(SPECIAL_CHARACTER_REGEX, "").replaceAll("\\<.*?\\>", "");

        return cleanedLine;
    }

    /**
     *
     * @return MongoDB Collection newsData to store contents and titles
     * @throws UnsupportedEncodingException
     */
    MongoCollection getDatabaseConnection() throws UnsupportedEncodingException {

        MongoClient mongoClient;
        MongoDatabase database;
        MongoCollection collection;

        //username of mongoDB cluster
        String username = "margin0607";
        //password of mongoDB cluster
        String password = "Margin@123";

        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
        String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8.toString());

        ConnectionString connectionString = new ConnectionString("mongodb+srv://"+encodedUsername+":"+encodedPassword+"@cluster0.k2maqtk.mongodb.net/?retryWrites=true&w=majority");

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        mongoClient = MongoClients.create(settings);

        database = mongoClient.getDatabase("myMongoNews");
        collection=database.getCollection("newsData");

        return collection;
    }
}
