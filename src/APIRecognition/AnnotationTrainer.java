package APIRecognition;

import com.aliasi.chunk.BioTagChunkCodec;
import com.aliasi.chunk.Chunking;
import com.aliasi.chunk.TagChunkCodec;
import com.aliasi.corpus.Corpus;
import com.aliasi.corpus.ObjectHandler;
import com.aliasi.crf.ChainCrfChunker;
import com.aliasi.crf.ChainCrfFeatureExtractor;
import com.aliasi.io.LogLevel;
import com.aliasi.io.Reporter;
import com.aliasi.io.Reporters;
import com.aliasi.stats.AnnealingSchedule;
import com.aliasi.stats.RegressionPrior;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.AbstractExternalizable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class AnnotationTrainer {

    public static List<Annotation>parsedList;

    public AnnotationTrainer() {
        super();
    }

    public void crfTrainer() throws IOException {
        String raw_text = new String(Files.readAllBytes(Paths.get("resources/test.txt")));

        // Call in the Parser
        AnnotationParser parser = new AnnotationParser();
        parsedList = parser.annotationList;
        System.out.println("parsedList: " + parsedList);

        // Code from simpleEntityTrain.java -  THIS IS THE EXACT SAME CODE PROVIDED IN THE LINGPIPE TUTORIAL
        Corpus<ObjectHandler<Chunking>> corpus = new AnnotationCorpus(raw_text, parsedList);

        // Initialise the tokenizerFactory
        TokenizerFactory tokenizerFactory = IndoEuropeanTokenizerFactory.INSTANCE;
        boolean enforceConsistency = true;
        TagChunkCodec tagChunkCodec = new BioTagChunkCodec(tokenizerFactory, enforceConsistency);

        ChainCrfFeatureExtractor<String> featureExtractor
                = new SimpleChainCrfFeatureExtractor();

        int minFeatureCount = 1;

        boolean cacheFeatures = true;

        boolean addIntercept = true;

        double priorVariance = 4.0;
        boolean uninformativeIntercept = true;
        RegressionPrior prior
                = RegressionPrior.gaussian(priorVariance,
                uninformativeIntercept);
        int priorBlockSize = 3;


        double initialLearningRate = 0.05;
        double learningRateDecay = 0.995;
        AnnealingSchedule annealingSchedule
                = AnnealingSchedule.exponential(initialLearningRate,
                learningRateDecay);

        double minImprovement = 0.00001;
        int minEpochs = 10;
        int maxEpochs = 5000;

        Reporter reporter
                        = Reporters.stdOut().setLevel(LogLevel.DEBUG);

        System.out.println("\nEstimating");
        ChainCrfChunker crfChunker
                = ChainCrfChunker.estimate(corpus,
                tagChunkCodec,
                tokenizerFactory,
                featureExtractor,
                addIntercept,
                minFeatureCount,
                cacheFeatures,
                prior,
                priorBlockSize,
                annealingSchedule,
                minImprovement,
                minEpochs,
                maxEpochs,
                reporter);

        // Output the file to a ".ser" file
        File modelFile = new File("resources/API_Training_Model.ser");
        System.out.println("\nCompiling to file=" + modelFile);
        AbstractExternalizable.serializeTo(crfChunker, modelFile);
    }
}
