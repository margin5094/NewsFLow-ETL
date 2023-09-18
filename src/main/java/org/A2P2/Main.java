package org.A2P2;

public class Main {
    public static void main(String[] args) {
        /**
         * ExtractionEngine object use to call extractNews() method.
         * This method will fetch all the news data from newsApi.
         */
        ExtractionEngine extractionEngine=new ExtractionEngine();
        extractionEngine.extractsNews();
    }
}
