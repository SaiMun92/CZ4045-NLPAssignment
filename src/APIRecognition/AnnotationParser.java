package APIRecognition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This is to get the SortedTest.ann annotated file and get rid of the tags
 * Afterwards, put all those annotations into an arraylist = annotationText
 */
public class AnnotationParser {
    private String annotationText;
    private String[] lines;
    public  List<Annotation> annotationList;

    // constructor
    public AnnotationParser() {
        Parser();
    }

    // For Testing purposes only
//    public static void ParsingMain (String[] args) {
//        AnnotationParser parser = new AnnotationParser();
//        parser.Parser();
//    }

    public void Parser() {
        annotationList = new ArrayList<>();

        try {
            annotationText = new String(Files.readAllBytes(Paths.get("resources/sortedTest.ann")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        lines = annotationText.split("\n");
        System.out.println(lines);

        for (int i = 0; i < lines.length; i++) {

            Annotation ann = relevantInfo(i);
            annotationList.add(ann);
        }
        //System.out.println("Annotation List: " +annotationList);
    }

    private Annotation relevantInfo(int index) {
        int startIndex, endIndex;
        String text;
        String[] extractedString;

        extractedString = lines[index].split("\\s+");

        startIndex = Integer.parseInt(extractedString[2]);
        endIndex = Integer.parseInt(extractedString[3]);
        text = extractedString[4];

        return new Annotation(startIndex, endIndex, text);
    }

}
