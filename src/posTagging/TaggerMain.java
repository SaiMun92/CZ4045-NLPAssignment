package posTagging;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/*
This file is created for POS-Tagging purposes only. save time dun have to load the whole code
POS-tagging is already implemented in FileReader under applyPOSTagging()
There is also print100Sentences() which prints out 100 sentences -> not sure how to identify unique posts
*/

public class TaggerMain {
    String line;

    ArrayList<String> sentenceList = new ArrayList<String>();
    ArrayList<String> postList = new ArrayList<String>();


    public static void main(String[] args){

        // Initialize the tagger
        TaggerMain TaggerMain = new TaggerMain();
        TaggerMain.getSentences();
        TaggerMain.applyPOSTagging();
        //TaggerMain.print100Sentences();
    }


    public void getSentences() {
        try {
            InputStream inputStream = new FileInputStream("resources/Posts-10000.xml");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(inputStreamReader);
            Pattern pattern;
            Matcher matcher;

            while ((line = br.readLine()) != null) {

                // usually 1 line in Posts-10000.xml is 1 post
                // find content of post
                pattern = Pattern.compile("Body=\"(.*?)\" OwnerUserId");
                matcher = pattern.matcher(line);

                if (matcher.find()) {// found the content of post in the line
                    // get content
                    String postContent = matcher.group(1);
                    postList.add(postContent);

                    // pattern for sentence
                    pattern = Pattern.compile("[.?!][\\s](([a-zA-Z]+[\\s])+[a-zA-Z]+[.!?][\\s]?)");
                    matcher = pattern.matcher(postContent);
                    while (matcher.find()) {// found a sentence
                        sentenceList.add(matcher.group(1));
                        System.out.println("The sentence is :  " + matcher.group(1));
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void applyPOSTagging(){
        int randomNumber;
        Random random = new Random();
        ArrayList<Integer> random10Integer = new ArrayList<Integer>();
        ArrayList<String> random10Sentence = new ArrayList<String>();

        MaxentTagger tagger = new MaxentTagger("taggers/english-bidirectional-distsim.tagger");

        for (int i=0; i<10; i++){// get 10 random number
            randomNumber = random.nextInt(sentenceList.size()) ;
            while (random10Integer.contains(randomNumber))
                randomNumber = random.nextInt(sentenceList.size()) ;
            random10Integer.add(randomNumber);
        }
        for (int j=0; j<10; j++){// get 10 random sentence
            random10Sentence.add(sentenceList.get(random10Integer.get(j)));
            System.out.println(sentenceList.get(random10Integer.get(j)));
        }

        // Now we have 10 random sentence in random10Sentence
        for (String sentence : random10Sentence) {
            System.out.println("\nInput: " + sentence);
            String tagged = tagger.tagString(sentence);
            System.out.println("Output: "+ tagged);
        }
    }


//    Select at least 100 posts, each of which contains at least one
//    API mention, and then annotate all the API mentions in the selected posts.

    public void print100Sentences() {
        // Number of sentences that you want to print out
        final int Number = 500;

        int divide = Number / 100;
        int count = 0;
        int loop = 1;
        for (int i = 0; i < divide; i++) {
            ArrayList<String> sentences = new ArrayList<String>();
            String name = String.format("sentences100 - %d.txt", i);
            Path file = Paths.get(name);

            for (int j = count; j < (100*loop); j++) {
                sentences.add(sentenceList.get(j));
                count++;
            }

            try {
                Files.write(file, sentences, Charset.forName("UTF-8"));
            } catch (IOException ie) {
                ie.printStackTrace();
            }
            loop++;
        }
    }
}