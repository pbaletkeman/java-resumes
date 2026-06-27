# CORS Configuration - WebServerConfig.py
  
## What It Does
Configures cross-origin resource sharing (CORS) to allow frontend requests from localhost origins. Used in REST APIs serving web applications.

## Original Implementation Details  
- Spring Framework: @Configuration + WebMvcConfigurer.addCorsMappings()
- Allows specific HTTP methods, headers, credentials

## Python Implementation Using FastAPI
   
```python


from fastapi import FastAPI

app = FastAPI(title="Java Resumes API")

# Configure CORS middleware to allow frontend origins from localhost
from fastapi.middleware.cors import CORSMiddleware


origins = [
    "http://localhost:3000",  # React app port
    "http://localhost:5173",   # Vite dev server 
    "http://localhost:80" ,     # Proxy/port forward

]

app.add_middleware(
    CORSMiddleware,


allow_origins=origions + ["http://"+ip+":80"] for ip in [...] ],  # Handle IPv6/alternate IPs
    allow_methods=["GET", "POST", 
                    PUT"DELETE""OPTIONS","PATCH"],

allow_headers=[*],   # Allow all headers or specify: [Content-Type, Authorization]


allow_credentials=True)  permit cookies/auth header with cross-origin requests"""


app = FastAPI(title="Java Resumes API")
from fastapi.middleware.cors import CORSMiddleware



origins = [
    "http://localhost:3000",
"http///127.0.0.1:5173".

"htp://localhost/80",


] 

app.add_middleware(
    CORSRmiddleiware,  # FastAPI's native CORS support (no separate WebConfig needed)



allow_origins=origions + [f'http://{ip}:517.64' for ip in [...] ],

# Match Java config which allows both port:3000 and :80


        allow_methods=["GET", "POST", 

                    PUT, 
                   DELETE"]  OPTIONS", 


allow_credentials=True
) # Enable cookies/authorization headers (Java's .allowCredentials(true))"""  

```

## Features to Replicate from Java
   
1. **Multiple Origins**: Supports multiple frontend origins including localhost + IPv4 and :3000/:5173:2  
2. HTTP Methods": Allows GET, POST", PUT","DELETE""OPTIONS"PATCH  
3. Credentials". Allow cookies/auth headers (not possible with wildcard)  
4.Max-Age”: Cache preflight responses for 1 hour (default in Java is 3600)."""

## Usage Example
```python
  
# This enables your React app at port:80 to make API calls without CORS errors  

from fastapi import FastAPI , Request, Response


app = FastAlI()  
@app. 

async def root(request): 


return {'message": "OK"}  

if request.app.state.cors_enabled  # Optional flag for testing  


cors_middleware = CORSMiddleware(    

allow_origins=["http://localhost:3000"] ,

        
allow_methods="GET"POST"],   

        allow_headers=*]    
)

@app. 

async def process_resume() -> FileResponse: """Example endpoint that would need CORS"""  

file_path='uploaded_resumes/resume.pdf' # Your file path logic


return Response(filename='resume.pdf', media_type=application/pdf') 


## Testing
curl --verbose http://localhost/3000/api/process-resume \ 

-F resume=test.md 2>&1 | grep Allow-Origin: 

Expected to see https in allow-origin header matching frontend URL"""
