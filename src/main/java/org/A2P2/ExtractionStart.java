package org.A2P2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataProcessingEngine {

    /**
     *
     * @param response response passed from ExtractionEngine
     * @throws IOException
     */
    void process(StringBuilder response) throws IOException {

                //Regex to match the title from json
                Pattern patternTitle = Pattern.compile("\"title\":\"(.*?)\"");
                //Regex to match the content from json
                Pattern patternContent = Pattern.compile("\"content\":\"(.*?)\"");
                Matcher titles = patternTitle.matcher(response.toString());
                Matcher contents = patternContent.matcher(response.toString());

                // initial filename
                int fileName = 1;

                // counter to track total news article in one file
                int fiveNewsCounter=0;

                //Directory path to store all new article
                String path="NewsData";

                FileWriter writer = new FileWriter(new File(path, String.valueOf(fileName)));

                // loop to store all titles and contents
                while (titles.find() && contents.find()) {

                    //if there are five articles then create new file and increment fileName with one
                    if(fiveNewsCounter%5==0){
                        writer.close();
                        writer = new FileWriter(new File(path, String.valueOf(fileName)));
                        fileName++;
                    }

                    String title=titles.group(1);
                    String content=contents.group(1);

                    //write title and content in file
                    writer.write(title + "\n");
                    writer.write(content + "\n");
                    fiveNewsCounter++;
                }
                writer.close();

                // Call transform method to store in MongoDB
                TransformationEngine transformationEngine=new TransformationEngine();
                transformationEngine.transforms();

    }

}
