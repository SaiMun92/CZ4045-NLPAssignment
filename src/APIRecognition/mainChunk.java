package APIRecognition;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunking;
import com.aliasi.crf.ChainCrfChunker;
import com.aliasi.util.AbstractExternalizable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;

/**
 * what is a crf?
 * CRF is used for named entity recognition - which is in this case to identify all the APIs.
 */
public class mainChunk {
    public static void main (String[] args) throws IOException, ClassNotFoundException{
        AnnotationTrainer trainer = new AnnotationTrainer();

        // Train the model using the training dataset
        trainer.crfTrainer();

        // read in the model file created in the AnnotationTrainer and the training dataset
        ChainCrfChunker crfChunker = (ChainCrfChunker) AbstractExternalizable.readObject(new File("resources/API_Training_Model.ser"));
        String raw_text = new String(Files.readAllBytes(Paths.get("resources/test.txt")));

        // This is testing the model against its own text that we previously trained
        Chunking chunking = crfChunker.chunk(raw_text);

        // Return all the API chunks for this chunking
        Set<Chunk> chunkSet = chunking.chunkSet();

        Iterator<Chunk> it = chunkSet.iterator();

        System.out.println("Chunkset size: " + chunkSet.size());

        // print out all the API chunks in the chunkset
        while (it.hasNext()) {
            Chunk chunk = it.next();
            int start = chunk.start();
            int end = chunk.end();
            String ann = raw_text.substring(start,end);
            System.out.println("Chunk Index: " + chunk + "Predicted API: " + ann);

            // Chunking Score
            //System.out.println("Rank log p(tags|tokens)  Tagging");

        }


        // Experimenting with nBestChunks and scoredChunking
        // args is the text file
//        for (int i = 1; i < args.length; ++i) {
//            String arg = args[i];
//            char[] cs = arg.toCharArray();
//
//            System.out.println("\nFIRST BEST");
//            Chunking chunking = crfChunker.chunk(arg);
//            System.out.println(chunking);
//
//            int maxNBest = 10;
//            System.out.println("\n" + maxNBest + " BEST CONDITIONAL");
//            System.out.println("Rank log p(tags|tokens)  Tagging");
//            Iterator<ScoredObject<Chunking>> it
//                    = crfChunker.nBestConditional(cs,0,cs.length,maxNBest);
//            for (int rank = 0; rank < maxNBest && it.hasNext(); ++rank) {
//                ScoredObject<Chunking> scoredChunking = it.next();
//                System.out.println(rank + "    " + scoredChunking.score() + " " + scoredChunking.getObject());
//            }
//
//            System.out.println("\nMARGINAL CHUNK PROBABILITIES");
//            System.out.println("Rank Chunk Phrase");
//            int maxNBestChunks = 10;
//            Iterator<Chunk> nBestChunkIt = crfChunker.nBestChunks(cs,0,cs.length,maxNBestChunks);
//            for (int n = 0; n < maxNBestChunks && nBestChunkIt.hasNext(); ++n) {
//                Chunk chunk = nBestChunkIt.next();
//                System.out.println(n + " " + chunk + " " + arg.substring(chunk.start(),chunk.end()));
//            }
//        }
    }
}




