package ca.letkeman.resumes.optimizer.responses;

import com.google.gson.annotations.SerializedName;
import java.util.StringJoiner;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class Choice {

  private int index;
  private String logprobs;
  @SerializedName("finish_reason")
  private String finishReason;
  private Message message;

  public Choice() {}

  public Choice(int index, String logprobs, String finishReason, Message message) {
    this.index = index;
    this.logprobs = logprobs;
    this.finishReason = finishReason;
    // Defensive copy to prevent external mutation
    this.message = message != null ? new Message(message) : null;
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

  public String getFinishReason() {
    return finishReason;
  }

  public void setFinishReason(String finishReason) {
    this.finishReason = finishReason;
  }

  public Message getMessage() {
    // Return defensive copy to prevent external mutation
    return message != null ? new Message(message) : null;
  }

  public void setMessage(Message message) {
    // Store defensive copy to prevent external mutation
    this.message = message != null ? new Message(message) : null;
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
        .append(getLogprobs(), choice.getLogprobs()).append(getFinishReason(), choice.getFinishReason())
        .append(getMessage(), choice.getMessage()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(getIndex()).append(getLogprobs()).append(getFinishReason())
        .append(getMessage()).toHashCode();
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Choice.class.getSimpleName() + "[", "]")
        .add("index=" + index)
        .add("logprobs='" + logprobs + "'")
        .add("finishReason='" + finishReason + "'")
        .add("message=" + message)
        .toString();
  }
}
