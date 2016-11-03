package Parsing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gibson on 9/19/2016.
 */
@XmlRootElement(name = "posts")
@XmlAccessorType(XmlAccessType.FIELD)
public class Posts implements Serializable {
  @XmlElement(name = "row")
  private List<Post> posts = new ArrayList<>();

  public List<Post> getPosts() {
    return posts;
  }

  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }
}
