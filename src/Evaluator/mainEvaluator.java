package Evaluator;

import com.aliasi.chunk.*;
import com.aliasi.crf.ChainCrfChunker;
import com.aliasi.util.AbstractExternalizable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by HP on 22/10/2016.
 */
public class mainEvaluator {
    private static final int[] kList = {2,3,4,5,6,7}; //#!

    public static void main(String[] args) throws Exception {
        String txt = "";
        String ann = "";
        try {
            txt = new String(Files.readAllBytes(Paths.get("resources/test.txt")));
            ann = new String(Files.readAllBytes(Paths.get("resources/sortedTest.ann")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("File input success");

        System.out.println("Setting up Evaluator");
        ChunkingEvaluation evaluation = new ChunkingEvaluation();

        for (int k : kList) {

            int[] partitionLines = partitionAnnLines(ann.split("\n"), k); // 73, 146, 219, 292
            int[] partitionIndexes = partitionAnnIndexes(ann.split("\n"), partitionLines); // 87185
            String[] partitionedAnn = partitionAnn(ann.split("\n"), partitionLines);
            String[] partitionedStrings = partitionTxt(txt, partitionIndexes);
            String[] whitespaceFiller = new String[k];
            for (int i=0; i<k; i++) {
                whitespaceFiller[i] = "";
                for (int j=0; j<partitionedStrings[i].length(); j++)
                    whitespaceFiller[i] += " ";
            }

            System.out.println("Partitioned Data");

            System.out.println("Starting K-fold cross validation for k=" + k);



            for (int i = 0; i < k; i++) { // 0123
                System.out.println("Training round " + (i + 1));
                System.out.println("Partitioning Corpus and Annotations");
                String frontString = "";
                String backString = "";
                String frontAnnotation = "";
                String backAnnotation = "";
                String trainingString = "";
                String trainingAnnotation = "";
                String frontReferenceString = "";
                String backReferenceString = "";
                String trainingReferenceString = "";

                for (int j = 0; j < k; j++) {
                    if (j < k - 1) { // 012 // 012 123 230 301  // 01 12 20
                        if (i + j >= k) { // %4
                            frontString += partitionedStrings[(i + j) % k];
                            frontAnnotation += partitionedAnn[(i + j) % k];
                        } else {
                            backString += partitionedStrings[(i + j) % k];
                            backAnnotation += partitionedAnn[(i + j) % k];
                        }
                    } else {
                        if (i + j >= k) { // %4
                            frontString += whitespaceFiller[(i + j) % k];
                        } else {
                            backString += whitespaceFiller[(i + j) % k];
                        }
                    }
                }
                trainingString = frontString + backString;
                trainingAnnotation = frontAnnotation + backAnnotation;

                for (int j = 0; j < k; j++) {
                    if (j < k - 1) { // 012 // 012 123 230 301  // 01 12 20
                        if (i + j >= k) // %4
                            frontReferenceString += whitespaceFiller[(i + j) % k];
                        else
                            backReferenceString += whitespaceFiller[(i + j) % k];
                    } else {
                        if (i + j >= k) // %4
                            frontReferenceString += partitionedStrings[(i + j) % k];
                        else
                            backReferenceString += partitionedStrings[(i + j) % k];
                    }
                }
                trainingReferenceString = frontReferenceString + backReferenceString;
                //System.out.println(trainingReferenceString); // debugging

                System.out.println("Training Corpus");
                CorpusTrainer trainer = new CorpusTrainer(trainingString, trainingAnnotation); //(trainingString, trainingAnnotation)

                File chunkerFile = new File("resources/evaluationModel.ser");
                System.out.println("Reading compiled model from file=" + chunkerFile);
                ChainCrfChunker chunker = (ChainCrfChunker) AbstractExternalizable.readObject(chunkerFile);

                System.out.println("Generating Response Chunk");
                Chunking response = chunker.chunk(trainingReferenceString); //partitionedStrings[(i+(k-1)) % k]

                System.out.println("Generating Reference Chunk");
                Chunking reference = referenceChunking(trainingReferenceString, partitionedAnn[(i + (k - 1)) % k]); //(partitionedStrings[(i+(k-1)) % k], partitionedAnn[(i + (k - 1)) % k])

                evaluation.addCase(reference, response);

                System.out.println("Results");
                System.out.println("True Positive Set: " + evaluation.truePositiveSet());
                System.out.println("False Positive Set: " + evaluation.falsePositiveSet());
                System.out.println("False Negative Set: " + evaluation.falseNegativeSet());
                System.out.println("Evaluation Results: " + evaluation.toString());
            }

            System.out.println("Intemediate Results");
            System.out.println("True Positive Set: " + evaluation.truePositiveSet());
            System.out.println("False Positive Set: " + evaluation.falsePositiveSet());
            System.out.println("False Negative Set: " + evaluation.falseNegativeSet());
            System.out.println("Evaluation Results: " + evaluation.toString());

        }

        System.out.println("Final Results");
        System.out.println("True Positive Set: " + evaluation.truePositiveSet());
        System.out.println("False Positive Set: " + evaluation.falsePositiveSet());
        System.out.println("False Negative Set: " + evaluation.falseNegativeSet());
        System.out.println("Evaluation Results: " + evaluation.toString());
    }

    private static Chunking referenceChunking(String text, String ann) {
        ChunkingImpl chunkings = new ChunkingImpl(text);
        String[] lines = ann.split("\n");
        for(int i=0; i<lines.length; i++) {
            int start, end;
            String[] extractedString;
            extractedString = lines[i].split("\\s+");
            start = Integer.parseInt(extractedString[2]);
            end = Integer.parseInt(extractedString[3]);
            chunkings.add(chunk(start, end, "API"));
        }
        return chunkings;
    }

    private static Chunk chunk(int start, int end, String type) {
        return ChunkFactory.createChunk(start,end,type);
    }

    private static String[] partitionTxt(String text, int[] indexes) {
        String[] substrings = new String[indexes.length];
        for(int i=0; i<indexes.length; i++) {
            if (i==0)
                substrings[i] = text.substring(i,indexes[i]); //first index = 0
            else
                substrings[i] = text.substring(indexes[i-1],indexes[i]);
        }
        return substrings;
    }

    private static String[] partitionAnn(String[] ann, int[] lines) {
        String[] substrings = new String[lines.length];
        for(int i=0; i<lines.length; i++) {
            substrings[i] = "";
            for(int j=(i==0?0:lines[i-1]); j<lines[i]; j++) {
                substrings[i] += ann[j] + "\n";
            }
        }
        return substrings;
    }

    private static int[] partitionAnnIndexes(String[] lines, int[] splitLines) {
        int[] splitIndexes = new int[splitLines.length];
        String[] extractedString;
        for(int i=0; i<splitLines.length; i++) {
            extractedString = lines[splitLines[i]-1].split("\\s+");
            splitIndexes[i] = Integer.parseInt(extractedString[3]);
        }
        return splitIndexes;
    }

    private static int[] partitionAnnLines(String[] lines, int k) {
        int splitSize = Math.floorDiv(lines.length, k);
        int[] splitLines = new int[k];
        for (int i = 0; i < k; i++) {
            splitLines[i] = splitSize * (i + 1);
            if (i == k - 1)
                splitLines[k - 1] = lines.length; //last index
        }
        return splitLines;
    }
}
