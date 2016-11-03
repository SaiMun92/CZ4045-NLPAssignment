package Evaluator.TrainerSubclass;

import java.util.ArrayList;
import java.util.List;

/**
 * This is to get the sortedTest.ann annotated file and get rid of the tags
 * Afterwards, put all those annotations into an arraylist = annotationText
 */
public class AnnotationParser {
    private String annotationText;
    private String[] lines;
    public List<Annotation> annotationList;

    public AnnotationParser(String annotationText) {
        annotationList = new ArrayList<>();

        lines = annotationText.split("\n");

        for (int i = 0; i < lines.length; i++) {

            Annotation ann = relevantInfo(i);
            annotationList.add(ann);
        }
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
