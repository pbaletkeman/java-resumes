package ca.letkeman.resumes.optimizer.responses;

import com.google.gson.Gson;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class LLMResponse {
  private String id;
  private String object;
  private int created;
  private String model;
  private List<Choice> choices;
  private Usage usage;
  private String system_fingerprint;

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

  public int getCreated() {
    return created;
  }

  public void setCreated(int created) {
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

  public String getSystem_fingerprint() {
    return system_fingerprint;
  }

  public void setSystem_fingerprint(String system_fingerprint) {
    this.system_fingerprint = system_fingerprint;
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
        .append(getSystem_fingerprint(), that.getSystem_fingerprint()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(getId()).append(getObject()).append(getCreated()).append(getModel())
        .append(getChoices()).append(getUsage()).append(getSystem_fingerprint()).toHashCode();
  }

  public LLMResponse(String id, String object, int created, String model, List<Choice> choices, Usage usage,
      String system_fingerprint) {
    this.id = id;
    this.object = object;
    this.created = created;
    this.model = model;
    this.choices = choices;
    this.usage = usage;
    this.system_fingerprint = system_fingerprint;
  }

  public LLMResponse(){}

  public String getJSON(){
    return new Gson().toJson(this);
  }
}
