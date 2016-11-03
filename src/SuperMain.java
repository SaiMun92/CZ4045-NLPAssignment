import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by SaiMunLee on 3/11/16.
 */

import Parsing.*;
import APIRecognition.*;
import Evaluator.*;
import Stemming.*;
import posTagging.*;

public class SuperMain {

    public static int n;
    static final String ROOT_DIR = "resources/";

    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);  // Reading from System.in

        while (n <6) {
            System.out.println("\nWelcome and this is the annotation tool...\n" +
                    "Please select the type of code you wish to run\n" +
                    "1. Parsing of Data from the DataDump\n" +
                    "2. Performing Stemming and generate the results\n" +
                    "3. Perform POS Tagging on 10 sentences\n" +
                    "4. Perform API Recoginition Training\n" +
                    "5. Perform k-Fold Cross Validation\n" +
                    "6. Exit" );

            System.out.println("Enter a number here: ");
            n = reader.nextInt();

            switch (n) {
                case 1:
                    ParsingMain ParsingMain = new ParsingMain();
                    ParsingMain.main(ROOT_DIR,10000);
                    break;

                case 2:
                    StemmingMain StemmingMain = new StemmingMain();
                    StemmingMain.main(null);
                    break;

                case 3:
                    TaggerMain TaggerMain = new TaggerMain();
                    TaggerMain.main(null);
                    break;

                case 4:
                    mainChunk mainChunk = new mainChunk();
                    try {
                        mainChunk.main(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException ce) {
                        ce.printStackTrace();
                    }
                    break;

                case 5:
                    mainEvaluator mainEvaluator = new mainEvaluator();
                    try {
                        mainEvaluator.main(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
