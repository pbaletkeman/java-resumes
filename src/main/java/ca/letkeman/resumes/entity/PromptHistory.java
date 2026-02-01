package ca.letkeman.resumes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Entity representing prompt history records in the database.
 * Tracks all prompt generations including input parameters, LLM responses,
 * and metadata for audit trail and analytics.
 */
@Entity
@Table(name = "prompt_history")
public class PromptHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "request_id", unique = true, nullable = false)
  private String requestId;

  @Column(name = "prompt_type", nullable = false)
  private String promptType;

  @Column(name = "job_description", columnDefinition = "TEXT")
  private String jobDescription;

  @Column(name = "company")
  private String company;

  @Column(name = "job_title")
  private String jobTitle;

  @Column(name = "interviewer_name")
  private String interviewerName;

  @Column(name = "temperature")
  private Double temperature;

  @Column(name = "model")
  private String model;

  @Column(name = "expanded_prompt_json", columnDefinition = "TEXT")
  private String expandedPromptJson;

  @Column(name = "generated_content", columnDefinition = "TEXT")
  private String generatedContent;

  @Column(name = "generated_file_path")
  private String generatedFilePath;

  @Column(name = "output_format")
  private String outputFormat;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "file_size_bytes")
  private Long fileSizeBytes;

  @Column(name = "llm_response_time_ms")
  private Long llmResponseTimeMs;

  @Column(name = "token_usage_estimate")
  private Integer tokenUsageEstimate;

  @Column(name = "status")
  private String status;

  @Column(name = "error_message", columnDefinition = "TEXT")
  private String errorMessage;

  /**
   * Default constructor.
   */
  public PromptHistory() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    this.status = "completed";
    this.outputFormat = "markdown";
    this.temperature = 0.7;
  }

  // Getters and Setters

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getPromptType() {
    return promptType;
  }

  public void setPromptType(String promptType) {
    this.promptType = promptType;
  }

  public String getJobDescription() {
    return jobDescription;
  }

  public void setJobDescription(String jobDescription) {
    this.jobDescription = jobDescription;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getInterviewerName() {
    return interviewerName;
  }

  public void setInterviewerName(String interviewerName) {
    this.interviewerName = interviewerName;
  }

  public Double getTemperature() {
    return temperature;
  }

  public void setTemperature(Double temperature) {
    this.temperature = temperature;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getExpandedPromptJson() {
    return expandedPromptJson;
  }

  public void setExpandedPromptJson(String expandedPromptJson) {
    this.expandedPromptJson = expandedPromptJson;
  }

  public String getGeneratedContent() {
    return generatedContent;
  }

  public void setGeneratedContent(String generatedContent) {
    this.generatedContent = generatedContent;
  }

  public String getGeneratedFilePath() {
    return generatedFilePath;
  }

  public void setGeneratedFilePath(String generatedFilePath) {
    this.generatedFilePath = generatedFilePath;
  }

  public String getOutputFormat() {
    return outputFormat;
  }

  public void setOutputFormat(String outputFormat) {
    this.outputFormat = outputFormat;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Long getFileSizeBytes() {
    return fileSizeBytes;
  }

  public void setFileSizeBytes(Long fileSizeBytes) {
    this.fileSizeBytes = fileSizeBytes;
  }

  public Long getLlmResponseTimeMs() {
    return llmResponseTimeMs;
  }

  public void setLlmResponseTimeMs(Long llmResponseTimeMs) {
    this.llmResponseTimeMs = llmResponseTimeMs;
  }

  public Integer getTokenUsageEstimate() {
    return tokenUsageEstimate;
  }

  public void setTokenUsageEstimate(Integer tokenUsageEstimate) {
    this.tokenUsageEstimate = tokenUsageEstimate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @Override
  public String toString() {
    return "PromptHistory{"
        + "id=" + id
        + ", requestId='" + requestId + '\''
        + ", promptType='" + promptType + '\''
        + ", company='" + company + '\''
        + ", jobTitle='" + jobTitle + '\''
        + ", status='" + status + '\''
        + ", createdAt=" + createdAt
        + '}';
  }
}
