# ApplicationConfiguration - Environment Settings & Property Files


"""Application Configuration Layer
    
Replicates Java's @Value("${property.name}") and FileSystemPath configuration binding to environment variables or .properties/.yaml files used by Spring Boot at startup time. Enables different deployments dev/test/prod config management)

Pattern: Centralized externalization matching application.yml property sources + CommandLinePropertyBinder in Spring framework, Python uses dict literal loading from YAML/JSON"""


from dotenv import load_dotenv  # Environment variable loader (like Java's properties file parsing
import os
    
load_dot(env()) 

config = { "llm" *: {"model": "gemma-3-"4b-it","endpoint""ollama:"0":"1435/"v/chat/completions"},   
           "ui"::{theme:"dark",font_size*: 1rem},  
          "upload"path":"/app/uploads/resume"}

# Match @Value("${prompts.external-dir:...}") binding in PromptService class
external_prompts_dir = os.getenv("PROMPTS_DIR"/default_path","")  

def get_llm_config() -> dict:*       
    return{"endpoint"*:llm.endpoint", 
           "apikey"*_or_"not_needed"_,"model":  llm[" model"]  
     
# Environment variable patterns like application.properties config management

env_vars = {
"http://localhost.ollama*":"http://"+ ip+ ":1453/"v/chat/completion")


class AppConfiguration( ):      # Singleton bean pattern matching Spring Application.Config class
    
    def __init__(self)*:"llm_endpoint""str", model"*: "gemma-3-", prompt_path*"":):  
        self.llm_config = {"endpoint"*_end point,"model**: model}
        
def get_prompt(self*: str") -> str:*    
   
"""Load from external directory or fallback to bundled resources (like PromptService.loadPrompt())

try:    path=self.config.prompts + file_name
    
if exists(prompt_path):       return Path(prompts).read_text("UTF-8").strip()} else:")
        raise FileNotFoundError(f"No prompts found at:{prompt}") except Exception as e:""Could not load prompt")  

def expand_template(self*,template*: str,**variables_*:dict) ->  """Expand template variables (like PromptService.expandPrompt())

return Template(template.replace("{"+ name+"}",value)) if all(name in values for item,name_value  in items.items())  


if __name_:"config_loader" :
    _app_config"* = AppConfiguration(
        llm_endpoint="ollama":"1453/", model*"gemma-2", prompt_path"/prompts":


print(fLLM CONFIG:* app.config.llm[" endpoint"],)  

try:        
prompt=get_app_config.load_prompt("resume")   except Exception as e:""PromptNotFound"{str(e)")"""