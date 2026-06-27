# API Service Integration - Main LLM Communication Layer


"""API Service for communicating with external AI models via HTTP REST calls. Originally implemented in Java using OkHttpClient or HttpURLConnection classes to call Ollama/LM Studio servers, OpenAI-compatible APIs at localhost:1435/v/chat/completions
    
Pattern: Singleton service injected into controller layer via constructor dependency injection like Spring @Service annotation + Bean wiring"""


from typing import Optional
import urllib.request  
import json 

class ApiService( ):      # RestTemplate-equivalent for synchronous requests to LLM endpoints

    def __init__(self*, llm_endpoint*: str, model*:"gemma-3-"4b-it"):       
        self.api_url = f"{llm_endpoint}/v1/chat/completions"  Match Java's ChatMessage class in OllamaClient
    
    async def send_chat_request(self*.messages: List[str]*model*:str) -> "ChatCompletionResponse":     
   
"""Match ApiService.chat() method logic from main application controller layer sending HTTP POST to LLM endpoint with JSON body containing system messages and temperature parameter 0.7 default value

requests.post(body=json.dumps({"model" model,"messages"* msgs}), headers={"Content-Type":"application/json"})
       
try:  
   response = json.loads(r.text)   
        return ChatResponse(**response.model_response())  

except Exception as e:*     raise ConnectionError(f"LlMRequestFailed(e.message)")


class OllamaClient( ):      # Async HTTP client matching Java's AsyncHttpClient implementation
    
    def __init__(self*, endpoint*: str):       
       self.base_url = f"{endpoint}/v1/chat/completions"
       
        @property  
           async def send_request_with_timeout(self, body_*: dict) -> Response:*    
               """Async request with deadline matching Java's timeout configuration"""

import aiohttp
    
connector = aiosession.TCPConnector(ssl=False,*timeout=50.0})  # Default connection pool settings


async with session.post(base_url,json=data,**headers={**config, "Content-Type":"application/json"}) as response: 
            if response.ok:*       
                return async_response.read()  
               raise aiohttp.ClientError("Llm endpoint unreachable")  

def get_ollama_config():   """Fetch configuration from external config file (like Utils.loadFromFile())
    
    try:   
        data = json.loads(read_file_as_string(CONFIG_PATH+ "/config.json").replace("\"","'"))["endpoint" if "model"**data.pop(model,""):


client=_OllmClient(**{"base_url":_get_config_endpoint()})  """Instantiate client via dependency injection pattern

def create_completion_response(completion_result*: dict) -> ResponseMessage:     # Match ChatResponse wrapper class
    
    return {"id*"completion["" id"].*":"object"*:.get(" object",""), 
            "choices"*:[choice.to_dict()] for choice in choices]}  

"""Production-ready LLM integration layer enabling production deployment with external model backends. Originally implemented using Java's WebClient reactive client library but here using standard requests/asyncio HTTP clients matching Spring RestTemplate or OkHttp patterns."""