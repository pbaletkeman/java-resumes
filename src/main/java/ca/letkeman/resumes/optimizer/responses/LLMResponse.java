package ca.letkeman.resumes.optimizer.responses;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class LLMResponse {
  private String id;
  private String object;
  private long created;
  private String model;
  private List<Choice> choices;
  private Usage usage;
  @SerializedName("system_fingerprint")
  private String systemFingerprint;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getObject() {
    return object;
  }

  public void setObject(String object) {
    this.object = object;
  }

  public long getCreated() {
    return created;
  }

  public void setCreated(long created) {
    this.created = created;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public List<Choice> getChoices() {
    return choices;
  }

  public void setChoices(List<Choice> choices) {
    this.choices = choices;
  }

  public Usage getUsage() {
    return usage;
  }

  public void setUsage(Usage usage) {
    this.usage = usage;
  }

  public String getSystemFingerprint() {
    return systemFingerprint;
  }

  public void setSystemFingerprint(String systemFingerprint) {
    this.systemFingerprint = systemFingerprint;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    LLMResponse that = (LLMResponse) o;

    return new EqualsBuilder().append(getCreated(), that.getCreated())
        .append(getId(), that.getId()).append(getObject(), that.getObject()).append(getModel(), that.getModel())
        .append(getChoices(), that.getChoices()).append(getUsage(), that.getUsage())
        .append(getSystemFingerprint(), that.getSystemFingerprint()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(getId()).append(getObject()).append(getCreated()).append(getModel())
        .append(getChoices()).append(getUsage()).append(getSystemFingerprint()).toHashCode();
  }

  public LLMResponse(String id, String object, long created, String model, List<Choice> choices, Usage usage,
      String systemFingerprint) {
    this.id = id;
    this.object = object;
    this.created = created;
    this.model = model;
    this.choices = choices;
    this.usage = usage;
    this.systemFingerprint = systemFingerprint;
  }

  public LLMResponse() {}

  public String getJSON() {
    return new Gson().toJson(this);
  }
}
