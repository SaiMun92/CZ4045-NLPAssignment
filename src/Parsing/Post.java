package Parsing;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Gibson on 9/19/2016.
 */
public class Post implements Serializable {
  @XmlAttribute(name = "Id")
  private long Id;
  @XmlAttribute(name = "PostIdType")
  private long PostIdType;
  @XmlAttribute(name = "ParentId")
  private long ParentId;
  @XmlAttribute(name = "CreationDate")
  @XmlJavaTypeAdapter(DateAdapter.class)
  Date CreationDate;
  @XmlAttribute(name = "Score")
  private long Score;
  @XmlAttribute(name = "Body")
  private String Body;
  @XmlAttribute(name = "OwnerUserId")
  private long OwnerUserId;
  @XmlAttribute(name = "LastActivityDate")
  @XmlJavaTypeAdapter(DateAdapter.class)
  Date LastActivityDate;
  @XmlAttribute(name = "CommentCount")
  private long CommentCount;

  public long getId() {
    return Id;
  }

  public void setId(long id) {
    Id = id;
  }

  public long getPostIdType() {
    return PostIdType;
  }

  public void setPostIdType(long postIdType) {
    PostIdType = postIdType;
  }

  public long getParentId() {
    return ParentId;
  }

  public void setParentId(long parentId) {
    ParentId = parentId;
  }

  public Date getCreationDate() {
    return CreationDate;
  }

  public void setCreationDate(Date creationDate) {
    CreationDate = creationDate;
  }

  public long getScore() {
    return Score;
  }

  public void setScore(long score) {
    Score = score;
  }

  public String getBody() {
    return Body;
  }

  public void setBody(String body) {
    Body = body;
  }

  public long getOwnerUserId() {
    return OwnerUserId;
  }

  public void setOwnerUserId(long ownerUserId) {
    OwnerUserId = ownerUserId;
  }

  public Date getLastActivityDate() {
    return LastActivityDate;
  }

  public void setLastActivityDate(Date lastActivityDate) {
    LastActivityDate = lastActivityDate;
  }

  public long getCommentCount() {
    return CommentCount;
  }

  public void setCommentCount(long commentCount) {
    CommentCount = commentCount;
  }

  // Custom variables
  public transient int wordCount = 0;
}
