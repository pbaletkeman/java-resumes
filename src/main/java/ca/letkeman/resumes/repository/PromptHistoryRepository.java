package ca.letkeman.resumes.repository;

import ca.letkeman.resumes.entity.PromptHistory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for PromptHistory entity.
 * Provides CRUD operations and custom queries for prompt history records.
 */
@Repository
public interface PromptHistoryRepository extends JpaRepository<PromptHistory, Long> {

  /**
   * Find prompt history by request ID.
   *
   * @param requestId the unique request identifier
   * @return Optional containing the prompt history if found
   */
  Optional<PromptHistory> findByRequestId(String requestId);

  /**
   * Find all prompt history records by prompt type.
   *
   * @param promptType the type of prompt (e.g., "interview-hr-questions")
   * @return List of prompt history records matching the type
   */
  List<PromptHistory> findByPromptType(String promptType);

  /**
   * Find all prompt history records created between two dates.
   *
   * @param startDate the start date/time
   * @param endDate the end date/time
   * @return List of prompt history records in the date range
   */
  List<PromptHistory> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

  /**
   * Find all prompt history records by company name.
   *
   * @param company the company name
   * @return List of prompt history records for the company
   */
  List<PromptHistory> findByCompany(String company);

  /**
   * Find all prompt history records by status.
   *
   * @param status the status (e.g., "completed", "failed", "pending")
   * @return List of prompt history records with the given status
   */
  List<PromptHistory> findByStatus(String status);

  /**
   * Find all prompt history records ordered by creation date descending.
   *
   * @return List of all prompt history records, newest first
   */
  List<PromptHistory> findAllByOrderByCreatedAtDesc();

  /**
   * Find prompt history records by prompt type, ordered by creation date descending.
   *
   * @param promptType the type of prompt
   * @return List of prompt history records, newest first
   */
  List<PromptHistory> findByPromptTypeOrderByCreatedAtDesc(String promptType);
}
