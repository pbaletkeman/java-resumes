package ca.letkeman.resumes.optimizer.responses;

import java.util.StringJoiner;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class Choice {

  private int index;
  private String logprobs;
  private String finish_reason;
  private Message message;

  public Choice(){}

  public Choice(int index, String logprobs, String finish_reason, Message message) {
    this.index = index;
    this.logprobs = logprobs;
    this.finish_reason = finish_reason;
    this.message = message;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getLogprobs() {
    return logprobs;
  }

  public void setLogprobs(String logprobs) {
    this.logprobs = logprobs;
  }

  public String getFinish_reason() {
    return finish_reason;
  }

  public void setFinish_reason(String finish_reason) {
    this.finish_reason = finish_reason;
  }

  public Message getMessage() {
    return message;
  }

  public void setMessage(Message message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Choice choice = (Choice) o;

    return new EqualsBuilder().append(getIndex(), choice.getIndex())
        .append(getLogprobs(), choice.getLogprobs()).append(getFinish_reason(), choice.getFinish_reason())
        .append(getMessage(), choice.getMessage()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(getIndex()).append(getLogprobs()).append(getFinish_reason())
        .append(getMessage()).toHashCode();
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Choice.class.getSimpleName() + "[", "]")
        .add("index=" + index)
        .add("logprobs='" + logprobs + "'")
        .add("finish_reason='" + finish_reason + "'")
        .add("message=" + message)
        .toString();
  }
}