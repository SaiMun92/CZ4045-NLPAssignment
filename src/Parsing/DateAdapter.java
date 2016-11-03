package Parsing;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gibson on 9/19/2016.
 */
public class DateAdapter extends XmlAdapter<String, Date> {
  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

  @Override
  public String marshal(Date v) throws Exception {
    return dateFormat.format(v);
  }

  @Override
  public Date unmarshal(String v) throws Exception {
    return dateFormat.parse(v);
  }

}
