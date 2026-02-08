package ca.letkeman.resumes.optimizer.responses;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class Usage {

  @SerializedName("prompt_tokens")
  private int promptTokens;
  @SerializedName("completion_tokens")
  private int completionTokens;
  @SerializedName("total_tokens")
  private int totalTokens;

  public int getPromptTokens() {
    return promptTokens;
  }

  public void setPromptTokens(int promptTokens) {
    this.promptTokens = promptTokens;
  }

  public int getCompletionTokens() {
    return completionTokens;
  }

  public void setCompletionTokens(int completionTokens) {
    this.completionTokens = completionTokens;
  }

  public int getTotalTokens() {
    return totalTokens;
  }

  public void setTotalTokens(int totalTokens) {
    this.totalTokens = totalTokens;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Usage usage = (Usage) o;

    return new EqualsBuilder().append(getPromptTokens(), usage.getPromptTokens())
        .append(getCompletionTokens(), usage.getCompletionTokens())
        .append(getTotalTokens(), usage.getTotalTokens()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(getPromptTokens()).append(getCompletionTokens())
        .append(getTotalTokens()).toHashCode();
  }

  public Usage() {}

  public Usage(int promptTokens, int completionTokens, int totalTokens) {
    this.promptTokens = promptTokens;
    this.completionTokens = completionTokens;
    this.totalTokens = totalTokens;
  }

  // Copy constructor for defensive copying
  public Usage(Usage other) {
    if (other != null) {
      this.promptTokens = other.promptTokens;
      this.completionTokens = other.completionTokens;
      this.totalTokens = other.totalTokens;
    }
  }
}
