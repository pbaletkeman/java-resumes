package ca.letkeman.resumes;


import java.util.List;

public class ChatBody {

  String model;
  List<Message> messages;
  double temperature;
  int maxTokens;
  boolean stream;



  public String getModel() {
    return this.model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public List<Message> getMessages() {
    return this.messages;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public double getTemperature() {
    return this.temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public int getMaxTokens() {
    return this.maxTokens;
  }

  public void setMaxTokens(int maxTokens) {
    this.maxTokens = maxTokens;
  }

  public boolean getStream() {
    return this.stream;
  }

  public void setStream(boolean stream) {
    this.stream = stream;
  }
}

