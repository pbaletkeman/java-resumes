package ca.letkeman.resumes.optimizer;


public final class Message {

  String role;
  String content;

  public Message() {
  }

  public Message(String role, String content) {
    this.role = role;
    this.content = content;
  }

  public String getRole() {
    return this.role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
