package ca.letkeman.resumes.optimizer.responses;

import java.util.StringJoiner;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class Message {

  private String role;
  private String content;

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Message message = (Message) o;

    return new EqualsBuilder().append(getRole(), message.getRole())
        .append(getContent(), message.getContent()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(getRole()).append(getContent()).toHashCode();
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Message.class.getSimpleName() + "[", "]")
        .add("role='" + role + "'")
        .add("content='" + content + "'")
        .toString();
  }
  public Message(){}

  public Message(String role, String content) {
    this.role = role;
    this.content = content;
  }
}