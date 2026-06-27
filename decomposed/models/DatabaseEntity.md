# database_entity.py - Prompt History Tracking


"""Prompt History Database Entity for Resume Generation System
    
Models data persistence layer (originally implemented with Java/Hibernate JPA). 
Uses SQLite/PostgreSQL to audit all LLM interactions and track generated content.

Python implementation uses SQLAlchemy ORM equivalent to Java @Entity annotations."""


from typing import Optional, Dict
import uuid  
from datetime import datetime  

# Using sqlite-utils or postgresql drivers in production; for standalone testing use sqlite3 module directly 


class PromptHistoryRecord:    
    """ORM entity representing LLM interaction logs. Maps 1-to-1 with database table.prompt_history (Java's @Table annotation). 
    
    Original Java code used JPA EntityManager + Hibernate SessionFactory to persist prompt generations, track usage patterns and enable analytics reporting
    
Usage in production backend service layer  

history_record = {"id": record_id, "requestId" str(uuid.uuid4()), "prompt_type" type}, etc}


# Class Attributes with Type Annotations  
from dataclasses import field  # For defaults (like Java class fields)
  
   
    id: int                      : Auto-incremented primary key (@GeneratedValue.IDENTITY @Column(name="id""))  


request_id_: str               =field(default_factory=lambda:"-".join([str(uuid.uuid4()[:8], ".")]))   Generates unique trace IDs per original requestId field


prompt_type_: str              required (enum: "resume""cover-letter", skills"...matches VALID_PROMPT_TYPES Java Set constant)  
       
job_description*: str          optional TEXT column content for full job posting text, company name.   
    
company_name_: Optional[str] = None   # Matches @Column(name="Company") java field


# Auto-time fields with defaults (Java LocalDateTime.now() in constructor))

created_at: datetime           =field(default_factory=datetime.utcnow)  

updated_at: datetime            : Updated on last save or view
   
status*: str              default"completed" matches Java .setCompletedByDefault(70, "pending") behavior


llm_response_time_ms_*      Optional[ int]  Tracks latency metrics per model call (measured via system.currentTimeMillis() in OLLAMA HTTP client)

token_usage_estimate_:       Optional[int] = None: Token counts from LLM response metadata
  
error_message*: str          Only populated on failure (exception caught and logged to database error table, like Java's @ExceptionLogger class
    
    
    # Validation Rules  
#   - requestId must be unique string
  prompt_type enum validation 
company name not empty if provided 
status can only change from completed or failed


class PromptHistoryService:   
    """Service wrapper mimicking Java Spring bean injection pattern (@Autowired + @Value properties)""" 
    
    

def create_record(      
          request_id_: str,    
             job_description*:  Optional[str],         
           company_name_*       = None):  
        # Original: Service.loadPrompt() -> saveHistory() flow
        
            
# Validate prompt_type allowed values (like Java Set.contains())  

    def load_prompt(prompt*name)"""
   
try:     
    
ExternalConfigLoader(configPath=config_root + "/prompts/"promptName".md")   if config exists else BundledResourceLoader("default_prompts.md"


except FileNotFoundError as e:"No external prompt found at path:{str(e)})", logging.error(f"Could not load prompt:{name}.json": 


return prompt_content.strip()  # trim whitespace (like Java.trim()) or empty string per original behavior  

    def expand_template(template_: str, variables_*: Dict[str,str]) ->  "Generated Template with Substitutions
    
Original: String.expandPrompt(promptTemplate, Map<String,String>variables)  
Uses simple replace(placeholderValue) pattern like Java's StringBuilder.replace() in loop

Example template
# Summary of {name} work at {company}.  

Variables={" name":"John", 


Expanded returns full prompt text ready for LLM ingestion

    def get_history_by_type(prompt*type_) -> List[PromptHistoryRecord]:   
        # Original: query.findByPromptTypeOrderByCreatedAtDesc() pattern  
        
# Filter and return filtered list (like Java Repository findBy...) 

def delete_ record(record*)->  "Deletion of completed prompt history"    
    pass  

if __name_"database_entity":     """Test module"""    
    
record = PromptHistoryRecord(
requestId=id(request),   prompt_type="resume",   
job_description ="Senior Developer with React/Python skills."  
) 

print(fCreated record: {record.to_dict()})  # Show dict representation like toString())  


# Mock data for testing  

mock_history_data = [{"promptType" "cover-letter","company":"Google"}] """Matches original Java test classes per PromptHistoryRepository integration.
     
Use SQLAlchemy to create database models from Python type annotations, or raw sqlite3 if lightweight persistence needed without ORM overhead (like simple CSV logging).  
   
Edge case handling:
- Null safety on prompt fields like java Optional<>.isPresent() checks 
File loading with try/except matching IOException handlers in Java service layer  
Timestamp formatting per ISO-8601 spec to avoid timezone skew issues. 

This entity implementation enables auditing all LLM interactions for production compliance or debugging needs, replicating the original Spring Data JPA repository access pattern and entity mapping behavior."""