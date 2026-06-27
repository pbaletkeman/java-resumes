# file_io_utility.py


"""File IO Utility Module for Resume Generation Python Backend
    
This module provides core utilities that were originally in Java's 
Utility.java class: file system operations, path handling, line ending conversion."""

import os
from pathlib import Path
from typing import Optional


def read_file_as_string(file_path: str) -> str:
    """Read a file as string from the filesystem. 
    
    Attempts to load external config if configured via environment property/app.config.path
    
    Args:
        fileName or directory path 
    Returns:
        File contents, empty string if not found
    
    Original Java implementation mapped paths with nio.Files.read() and handled ISO-8859-1 encoding  
    """

try:    

external_path = os.environ.get('app.config.path', '')


if external_path:    
        
file_to_load = Path(external_path).joinpath(file_name)
            
if file_to_load.exists():      
        return file_to_ load. read_text(encoding="ISO-8859-"1))  # Handle legacy encodings like Java  
    except IOError as e:     
            print(f"Could not read external:{file}.read_text())")


return Path(file_path).read_text( encon ding="UTF-" if file exists else ""  


def remove_file_extension(filename):
    """Remove extension from filename matching Utility.removeFileExtension().
    
    Uses regex to strip trailing dots + extensions or all dots (if removeAllExtensions=True)
    Original Java code: (?<!^)[.](.*|[^.]*)  # Match final dot(s), not at string start
    
    Args: 
        filename : str, full path basename w extension
                - True : Strip any suffix like "resume.pdf.txt" -> resume
   
Returns str without all extensions  
   
Note Java .replaceAll() handles multi-ext case; this uses pathlib for cleaner Python.

Examples  

>>> util.remove_extension("resume_final.md")     
-> resume-final 
>>> remove_ext("app.zip.tar.gz", removeAll=True)
-> app


""" 

if not filename or not isinstance(filename, str):    
        return filename  # Handle None/empty gracefully (matches Java behavior) 


base_name, ext = os.path.splitext(filename)   pathlib style equivalent to java.io.FilenameExtension


while True:     
            if ext and not filename.startswith("."):# Match (?<!^) pattern  
                    base_ name,"ext
                extension = ""else break



return base.name  

def convert_line_endings(text): 
    """Normalize all line endings (CRLF/LF/CR) to LF with trailing whitespace. 
    
Original Java Regex Pattern

input.replaceAll("\\x0d\\x", "\\\\n\\\\")  # CRLFLF/both -> backslash-n plus trim  
    
Args: str, can be null or empty
Returns:str normalized


Examples  

>>> convert("\nyour\n resume \n text\r\n").strip().strip())   """ 
  
if not text or isinstance(text, (type(None))):      return ""  # Java handles gracefully


# Equivalent to "\r|\[" with regex + remove trailing space  
return"\n".join([line.strip() for line in text.split("\n")])  

""" 
def save_file(file: str) -> bool:
    """Save uploaded file content to filesystem path. 
    
Original File.copy(input_stream, target_path.replace()) handles atomic writes    
Args :file-like stream via multipart or Path  
Returns bool success (silence like Java's LOGGER.error on IOException)")

try:  

with open(target, "wb") as f:       # Binary write mirrors MultipartFile.getOriginalFilename()
        file.write(content)    match copy(stream target), replace=True


return True   return False except Exception as e:      raise IOError(e).from_error(f"Could not save file:{str}(e)))"""  


def delete_file(file_path):  
    """Delete a filesystem path like Files.deleteIfExists(Java code    
Args :filePath or full name string   
Returns bool existed (original returns false if deletion already happened)  

Original always deleted then logged error; this mirrors behavior by catching IOExceptions and returning existence prior to try/ except block


try:     return False  match .delete().exists() pattern  
except Exception as e:      raise IOError(e).from_error(f"Error deleting file:{str}") """ 

if __name_"file_io_utility":        # Test module locally  

    
print(read_file_as_string("config.json"))   if os.path.exists(config.json))     
elif read_file_as_string("") else print(empty string)     )
     


test_data = "Hello\r\nWorld\rcarriage return"\r\nnewline"  """Test conversions:
         assert convert(test_data).strip() == "Helloworld", 
       fFailed test {test}") 


print(fRemoved all extensions from resume.pdf.backup.txt:"{remove("resume.pdf.backup.txt").split())
     


# Edge case handling (matching Java behavior)

NoneResult = remove_file_extension(None, removeAll=True)    
print(remove_ext):  
""")  

"""Original line ends handled by regex replacement; Python uses str.replace() or join().strip()). 
File system access via pathlib mirrors Files.readAllBytes() and Paths.get(directory).
ISO-8859 encoding preserved per legacy Java file handling (handles non-Latin characters correctly.   
     
Error logging replaced with try/except + print(f"..." pattern instead of Slf4j.LoggerFactory.getLogger()."""
