package Parsing;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;

/**
 * Created by Gibson on 9/18/2016.
 */
public class ImportDataService {

  public <T> T importFromXMLFile(String filepath, T object) throws JAXBException {
    // Make JAXB Object
    JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

    // Parse XML file
    T result = (T) jaxbUnmarshaller.unmarshal(new File(filepath));

    // return object
    return result;
  }

  /**
   *
   * @param sourcePath Source data dump
   * @param targetPath Target data dump
   * @param n Number of posts to extract (from back)
   * @throws IOException
   */
  public void saveLastNElementsFromXML(String sourcePath
    , String targetPath, int n) throws IOException {
    // open target
    PrintWriter pw = new PrintWriter(targetPath, "UTF-8");

    // open source
    BufferedReader br = new BufferedReader(new FileReader(sourcePath));
    String line = br.readLine();

    // write xml header
    pw.write(line + "\n");
    // write root element
    line = br.readLine();
    pw.write(line + "\n");

    // get number of lines from source
    int lines = 2;
    line = br.readLine();
    while (line != null) {
      lines++;
      line = br.readLine();
    }

    // restart reader
    br.close();
    br = new BufferedReader(new FileReader(sourcePath));

    // take n last elements (excluding closing tag)
    int index = 0;
    int target = lines - n - 1;
    while (index < target) {
      index++;
      br.readLine();
    }

    // write remaining into target
    line = br.readLine();
    while (line != null) {
      pw.write(line + "\n");
      line = br.readLine();
    }

    // close xml
    pw.close();
    br.close();

  }
}
