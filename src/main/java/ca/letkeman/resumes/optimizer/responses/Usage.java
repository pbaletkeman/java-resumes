package ca.letkeman.resumes.optimizer.responses;

import com.google.gson.annotations.SerializedName;

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

  public Usage() {}

  public Usage(int promptTokens, int completionTokens, int totalTokens) {
    this.promptTokens = promptTokens;
    this.completionTokens = completionTokens;
    this.totalTokens = totalTokens;
  }
}
