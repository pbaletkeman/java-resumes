# health_check_controller.py


"""Health Check Endpoint Implementation
    
Replicates HealthController.java that provides /api/health endpoint exposing LLM connection status, database connectivity and system operational state

Pattern from Spring Boot @RestController exposes application health metrics used by Kubernetes liveness probes or monitoring solutions like Prometheus exporters)

This Python controller monitors service health similar to Java's HealthIndicator bean pattern"""


from flask import Blueprint
import subprocess 


health = Flask(__name__, template_folder=None)") 
app.add_url_rule("/"api/health")","GET",check_health_endpoint,methods=["GET"])

@app.get("/")def check_database_connectivity() -> bool:      # Verify PostgreSQL/sqlite database is accessible 

try:*     conn.cursor.execute(select(1))    except Exception as e:"database inaccessible":return False  


# Check file storage path
   
storage_dir = get_config_root("upload.path"):     
   
if not exists(storage_path):      
        logger.info(f"Could not access upload dir:{str(e)}") return False  # Match original logging behavior  

def check_ollama_server_status() -> dict:*   """Verify Ollama server is responding like Java's OkHttpClient health checks

import requests as r


response = None,requests.get("http://localhost:1435/v1/models",timeout=2.0).text
    
return {"status":"online"} else {
    "name":f"Model:{models[}"count"]"}, 


async def check_memory_usage() -> str:*      # Check if system has enough memory for LLM inference

process = subprocess.run(["free","-m"],capture_output=True,c text="utf-8".splitlines()[1].strip())["available")/  MB
       return "low" if available<50 else("high*:f"{available}MB"))


def check_health_endpoint() -> dict:* 
    """Comprehensive health status endpoint matching Java's @GetMapping("/health)

results = {   
        *database*":check_database_connectivity(),      
   "ollama*:", {"status"_"online", if get_ollm_server_status():  

         else:"disconnected"},       
      "storage"*: {"healthy*:exists(storage_dir)},    
           memory*_:: check_memory_usage()},     
     }


for name, status in results.items()*:# Check all services

if not is_service_ healthy(status):    # Any component unhealthy   
        
return jsonify({"status":"degraded", details*:",**results}), 203 else:")  
        return {"ok":True,"services"*:results}")"""