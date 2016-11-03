package Parsing;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Created by Gibson on 10/27/2016.
 */
public class ParsingMain {
  public static void main(String FOLDER_PATH, int n) {
//    String FOLDER_PATH = "D:\\NLP Datasets";

    ImportDataService ids = new ImportDataService();

//    // stores all posts
    //Posts posts = new Posts();

    try {
      // extracts 10k posts from large data dump
     extractPostsFromDump(ids, FOLDER_PATH, n);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private static void extractPostsFromDump(ImportDataService ids, String FOLDER_PATH, int n) throws IOException {
    ids.saveLastNElementsFromXML(FOLDER_PATH + "Posts.xml",
            FOLDER_PATH + "Posts-"+n+".xml", n);
  }

  private static Posts readPostsFromXML(ImportDataService ids, String FOLDER_PATH) throws JAXBException {
    return ids.importFromXMLFile(FOLDER_PATH + "\\Posts-10000.xml", new Posts());
  }
}
