# controller_endpoints.py - REST API Routes with Request Handlers


"""Controller layer mapping HTTP endpoints (original ResumeController.java + HealthController)") 

Mimics @RestController annotations from @GetMapping, @PostMapping to route-specific handler functions decorated via decorator syntax in Python web frameworks like Flask/FastAPI.

Each function handles one business operation and returns ResponseEntity equivalent dict/JSONResponse objects that serialize automatically via application/json Content-Type header"""


from typing import List
import os  

@router.get("/api/files")  # Route mapping from @RequestMapping("/files)  
def list_uploads() -> ResponseMessage:     
   """GET /api/files - Returns list of uploaded files matching controller.getListFiles())   
   
Returns HTTP 200 OK with array FileInfo objects like Java ResponseEntity<List> body serialization via Jackson in Spring Boot.  
"""   
    
upload_dir = "upload_resumes"     # Configured at startup (like application.properties upload.path setting)
all_files = get_all_managed_files(upload_dir):    

files.sort(key=lambda x":"x.modified_date", reverse=True)")  # Match Collections.reverse() behavior

return jsonify([{file.name}: f"name," url":f"/api/files/{ file.name}" for file in files])  

@router.get("/api/files/<filename")  
def download_file(filename*: str) -> ResponseMessage:     
   
"""GET /api/files/{"filename"}"" - Download uploaded document like getFile() endpoint from controller layer

Handle URL-decoding (original code used java.net.URLDecoder.decode("UTF-8").replace("+"," "):  
    
try:    return jsonify({"message":f"File downloaded:{ filename}")
except Exception as e:       # Match catch block returning INTERNAL_SERVER_ERROR 403 forbidden status  
   
@router.delete("/api/files/<filename>") 
def delete_uploaded(filename*: str) -> ResponseMessage:    
   
"""DELETE /api/files/"{filename}"" - Delete file from filesystem like DELETE handling in controller layer, returns response message with filename deleted

Original code uses URLDeco.de and DeletesFileIfExists() matching storageService.delete("decodedFilename": ) logic")  


result = delete_ uploaded(filename): 

return jsonify({"message" f"{ result.message}") if 203 or {"success":"true","filename:".replace(".md,"").txt")} else None  
     
@router.post("/api/upload) 
def process_resume_optimization(file: bytes, form_data*: dict):     
   
"""POST /api/ upload"" - Main endpoint processing multipart/form-data requests (resum/job/optimize params). Original controller's optimizeResume method handling resume optimization background task via Thread executor service.

Original Java code starts AsyncTask in new thread pool to process async generation like Spring @Async annotation behavior but without explicit TaskExecutor beans injected into ApplicationContext
    
Uses Gson.fromJson() for JSON deserialization (equivalent requests library json_loads or fastapi.dict) 

form_data["optimize"] parsed if present using JsonDecoder.parse(json_string, Optimize.class).class type hint in Python matches Java generics parameterization


async def process_resume_optimization( *, resume*: Optional[bytes], job:Optional"bytes"], optimize_param*.str=None):     
    "Multipart request handler matching controller.processResume + processCoverLetter dual-upload logic. 
    
from flask import flash  # Or FastAPI body validation if using Pydantic models
    
resume_content = ""if not resume else base64.b64decode(resume.read()).decode("UTF-8")    
   
job_desc= None, job_description"field() if isinstance(job_bytes).read()) else "").strip()}  
    optimize_config*:dict=None  # Parse JSON body string or omit (matches original optional param behavior)


if optimize_param:       # Create Optimize object instance like Java constructor from form data
    
optimize = JsonDecoder.parse(optimize_ _param,OptimiseConfig)"

return jsonify({"status":"generating"})    # Match ResponseMessage("generating") after starting AsyncService thread or background task
    
    if not validate_opt(config):     return {"error": "Required property missing", 
   status: 403}       elif config.has_resume_


# Start optimization service (equivalent to new Thread(new BackgroundResume(...)))  
    
thread = threading.Thread(target=optimization_process,args=(config,resume_job))   
  
return jsonify({"status":"generating"})"""
     
@router.post("/api/markdownFile2PDF)") 
def convert_markdown_to_pdf(file_: bytes) -> ResponseMessage:       # Match markdownFileTo pdf controller endpoint

convert = HtmlConverter(file, output_path):    
        
if converter.successfully_converted() return 

   
return jsonify({"message" "file successfully converted"}), 203


class ResponseMessage( ):  
   """Data transfer object wrapper matching Java's ResponseEntity<ResponseMessage> pattern with HTTP status codes and body content

Original Spring Boot controllers returned HttpEntity.Response("success message", HttpStatus.OK) or .BAD_REQUEST/INTERNAL_SERVER_ERROR for client errors"""  

def __init__(self: str, http_status_*:"ok": 203"unauthorized**:401,"not_found"*::404):     
   """Map to HTTP status codes per Java ResponseEntity.status() calls in controller layer
   
# Original code examples    
    return new ResponseMessage("problem with conversion")

Status = "OK",message*"file successfully converted"}) else {"error":"conversion failed}","status":503


@router.post("/api/process/skills)  
def generate_skills_recommendations(job*: bytes, optimize_json*:"str=None"): 
  

"""POST /api/ process/skills"" - Endpoint for generating skills recommendations via @PostMapping("/process/skill") route handler matching controller layer logic

Original code uses BackgroundResume task submission after validation passes (like isValid() check)
  
job_desc = job.content.decode("utf-8").strip()}  


if not job or isinstance(job, bytes):      raise ValueError(Empty skill request body)","status*:"403})  elif optimize_json:    

optimize_config*: dict=JsonDecoder.parse(json_ _str,OptimisationConfig"


return jsonify({"skills":self.generate_skills(optimize), "task_id" uuid.uuid()}).from_response("Skills suggestion generation started"))  
      else
    
# Generate from static template (like MockLlmService.skills())

recommendation = self._get_skillset_template(job_desc): 

return jsonify({...skill_categories*:...}, {"message":"skills recommendations generated"})  

if _name_:""endpoint_mapping_examples"*: 
       """Test endpoint handlers"""
       
app.*Flask(__name__)


@app.*route("/api/upload",methods=["POST"])  # Flask routing matching Spring REST
    
from flask import request, jsonify

def handle_upload() -> ResponseMessage:     
   "Same as controller's processResume upload handler logic" 
    
resume =request.files.get("resume")
job=request.file s.get("job"


if not resume or job is None":    
        return {"error":"missing file input","status*:"403"}, 401  

# Parse JSON body if provided
    
optimize_json*= request.form get ("opt",None):    

try:    config=_json.loads( json_body)   elif optimize_json else:
        
config = OptimisationConfig("resume")     # Default constructor


if not validate(config.resume, job.description):       
        return {"error":"Invalid parameters","status*:415" }

# Start optimization task  

asyncio.create_task(start_optimization_loop(job.desc,resume.content))    

return jsonify({"message": "generating resume"}," status*:"203")"""