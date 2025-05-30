## Problem Statement
Every new job posting requires a tailored resume and a tailored cover letter.

## Pain
Can take an hour to create a bespoke resume and cover letter
- I may miss something in the job description
- I may accidentally keep incorrect/unneeded experience in the resumne/cover letter which may turn off the potential employer
- I may accidentally remove a vital skill from the resume/cover letter and the potential employer will skip over me
- I want to know certifications or technologies I can learn to make myself better suited for the role

## Tools
- Java SpringBoot 3.5
- Java 17
- OpenAI compatible service
  - Ollama and LM Studio are two free services that you can run locally
    - Use an LLM that is at least 2 GB, you can get good results from:
      - gemma-3-4b-it-Q4_K_M
      - Hermes-3-Llama-3.1-8B.Q4_K_M
- ReactJS:
  - TypeScript
  - PrimeReact

## Solution
A LLM will take a job description and a resume and provide a tailored resume with some suggestions extra things to learn/earn and/or a tailored cover letter in both Mardown and PDF formats.
The resume and/or cover letter may need some manual fine tunning and should also help you get past the Applicant Tracking Systems (ATS).
Additionly, there is an option to convert a Markdown file to a PDF file.


### API Endpoints

#### http://localhost:8080/swagger-ui/index.html

#### http://localhost:8080/spotlight/index.html
