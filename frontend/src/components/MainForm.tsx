import { useState } from "react";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { InputTextarea } from "primereact/inputtextarea";
import {
  InputNumber,
  type InputNumberValueChangeEvent,
} from "primereact/inputnumber";
import { Checkbox, type CheckboxChangeEvent } from "primereact/checkbox";
import { Button } from "primereact/button";
import { Accordion, AccordionTab } from "primereact/accordion";

import FileUpload from "./FileUpload";

import MDEditor from "@uiw/react-md-editor";
import { Dialog } from "primereact/dialog";
import { useInterval } from "primereact/hooks";

interface OptimizeType {
  model: string;
  jobTitle: string;
  company: string;
  temperature: number;
  resume?: string;
  jobDescription?: string;
  promptType?: string[];
}

export const API_HOST = (
  window as Window & typeof globalThis & { Config: { host: string } }
).Config.host;

// export const API_HOST = window["Config"].host; // "http://localhost:8080";

export default function MainForm() {
  const [model, setModel] = useState<string>("");
  const [jobTitle, setJobTitle] = useState<string>("");
  const [company, setCompany] = useState<string>("");
  const [temperature, setTemperature] = useState<number>(0);
  const [resumeMD, setResumeMD] = useState<string>("");
  const [jobText, setJobText] = useState<string>("");
  const [resumeFile, setResumeFile] = useState<File | null>();
  const [jobFile, setJobFile] = useState<File | null>();
  const [prompt, setPrompt] = useState<string[]>([]);
  const [resetResume, setResetResume] = useState<boolean>(false);
  const [resetJob, setResetJob] = useState<boolean>(false);

  const [showDialog, setShowDialog] = useState<boolean>(false);
  const [dialogHeader, setDialogHeader] = useState<string>("");
  const [dialogBody, setDialogBody] = useState<string>("");
  const [countDown, setCountDown] = useState<number>(5);
  const [active, setActive] = useState<boolean>(false);

  useInterval(
    () => {
      setCountDown((countDown) => countDown - 1);
      if (countDown <= 0) {
        setActive(false);
        setShowDialog(false);
        setCountDown(5);
      }
    },
    1000,
    active
  );

  const onPromptChange = (e: CheckboxChangeEvent) => {
    const _prompt = [...prompt];

    if (e.checked) _prompt.push(e.value);
    else _prompt.splice(_prompt.indexOf(e.value), 1);

    setPrompt(_prompt);
  };

  const handleFilesSelected = (files: File[], source: string) => {
    if (source === "job") {
      if (files?.length > 0) {
        setJobFile(files[0]);
      } else {
        setJobFile(null);
      }
    } else {
      if (files?.length > 0) {
        setResumeFile(files[0]);
      } else {
        setResumeFile(null);
      }
    }
  };

  const clearForm = async () => {
    setResetResume(true);
    setResetJob(true);
    setCompany("");
    setJobTitle("");
    setModel("");
    setTemperature(0.01);
    setPrompt([]);
    setJobText("");
    setResumeMD("");
  };

  const isValidForm = () => {
    return !(
      !prompt ||
      prompt.length === 0 ||
      !temperature ||
      temperature < 0.01 ||
      temperature > 1.99 ||
      !company ||
      company.length === 0 ||
      !jobTitle ||
      jobTitle.length === 0 ||
      !model ||
      model.length === 0 ||
      (jobFile?.name == null && (jobText == null || jobText.length === 0)) ||
      (resumeFile?.name == null && (resumeMD == null || resumeMD.length === 0))
    );
  };

  const handleFormSubmit = async () => {
    if (!isValidForm()) {
      // console.log("bad form values");
      setActive(true);
      setShowDialog(true);
      setDialogHeader("Invalid Form Values");
      setDialogBody("One Or More Inputs Are Invalid");
      return;
    }

    const optimize: OptimizeType = {
      company: company,
      jobTitle: jobTitle,
      model: model,
      temperature: temperature,
      promptType: prompt,
    };
    const formData = new FormData();
    if (jobFile) {
      formData.append("job", jobFile, jobFile.name);
    } else {
      optimize["jobDescription"] = jobText;
    }
    if (resumeFile) {
      formData.append("resume", resumeFile, resumeFile.name);
    } else {
      optimize["resume"] = resumeMD;
    }

    formData.append("optimize", JSON.stringify(optimize));

    const response = await fetch(API_HOST + "/upload", {
      method: "POST",
      body: formData,
    });

    if (response.ok) {
      console.log("Form submitted successfully!");
      setShowDialog(true);
      setDialogHeader("Form submitted successfully!");
      setActive(true);
      if (countDown == 0) {
        setActive(false);
        clearForm();
      }
    } else {
      console.error(
        "Error submitting form:",
        response.status,
        response.statusText
      );
      setShowDialog(true);
      setDialogHeader("Form Submission Error");
      setDialogBody(response.status + "\n" + response.statusText);
      setActive(true);
      if (countDown == 0) {
        setActive(false);
      }
    }
  };

  return (
    <Card
      title="Resume Optimizer"
      className="border-round-3xl	"
    >
      <div className="card flex justify-content-center">
        <Dialog
          header={dialogHeader}
          visible={showDialog}
          style={{ width: "25vw" }}
          onHide={() => {
            if (!showDialog) return;
            setCountDown(5);
            setShowDialog(false);
            setDialogHeader("");
            setDialogBody("");
          }}
        >
          {dialogBody}
          <br />
          Closing in {countDown} seconds.
        </Dialog>
      </div>

      <div className="grid">
        <div className="col-12 grid">
          <div className="col-6 pt-3 mt-1 mb-1">
            <label htmlFor="company">Company</label>
          </div>
          <div className="col-6">
            <InputText
              id="company"
              value={company}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                setCompany(e.target.value)
              }
              className="p-inputtext-sm"
              size={30}
              variant="filled"
              tooltip="Company That Posted Job"
              placeholder="Microsoft Game Studios"
            />
          </div>
        </div>
        <div className="border-top-1 col-12 grid">
          <div className="col-6 pt-3 mt-1 mb-1">
            <label htmlFor="title">Job Title</label>
          </div>
          <div className="col-6">
            <InputText
              id="title"
              value={jobTitle}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                setJobTitle(e.target.value)
              }
              className="p-inputtext-sm"
              size={30}
              tooltip="Position Title"
              placeholder="Primary Game Developer"
            />
          </div>
        </div>
        <div className="border-top-1 col-12 grid">
          <div className="col-6 pt-3 mt-1 mb-1">
            <label htmlFor="model">Model</label>
          </div>
          <div className="col-6">
            <InputText
              id="model"
              value={model}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                setModel(e.target.value)
              }
              className="p-inputtext-sm"
              size={30}
              variant="filled"
              tooltip="LLM Identifer"
              placeholder="gemma-3-4b-it"
            />
          </div>
        </div>
        <div className="border-top-1 col-12 grid">
          <div className="col-6 pt-3 mt-1 mb-1">
            <label htmlFor="temperature">Temperature</label>
          </div>
          <div className="col-6">
            <InputNumber
              inputId="temperature"
              value={temperature}
              onValueChange={(e: InputNumberValueChangeEvent) =>
                setTemperature(e.value ?? 0.01)
              }
              minFractionDigits={2}
              max={1.99}
              min={0.01}
              showButtons
              size={24}
              step={0.01}
              className="p-inputtext-sm"
              tooltip="Higher Values Produce More Creative Responses"
              placeholder="0.01"
            />
          </div>
        </div>
        <div className="border-top-1 col-12 grid">
          <div className="col-6 mt-1 mb-1 pb-2">
            <label>Prompt Type</label>
          </div>
          <div className="col-6 mt-1">
            <div className="flex flex-wrap">
              <div className="ml-4 mr-6">
                <Checkbox
                  inputId="promptResume"
                  name="promptType"
                  value="Resume"
                  onChange={onPromptChange}
                  checked={prompt.includes("Resume")}
                  variant="filled"
                  tooltip="Create Resume Response"
                />
                <label
                  htmlFor="promptResume"
                  className="ml-2"
                >
                  Resume
                </label>
              </div>
              <div>
                <Checkbox
                  inputId="promptCover"
                  name="promptType"
                  value="Cover"
                  onChange={onPromptChange}
                  checked={prompt.includes("Cover")}
                  variant="filled"
                  tooltip="Create Cover Letter Response"
                />
                <label
                  htmlFor="promptCover"
                  className="ml-2"
                >
                  Cover Letter
                </label>
              </div>
            </div>
          </div>
        </div>
        <div className="col-12 border-top-1">
          <label htmlFor="resumeFile">Resume</label>
          <Accordion activeIndex={0}>
            <AccordionTab header="File Upload">
              <FileUpload
                resetValue={resetResume}
                setResetValue={setResetResume}
                accept="text/markdown, text/plain" // Optional: Filter file types
                onChange={(files) => handleFilesSelected(files, "resume")} // Callback function
                width="60vh"
              />
            </AccordionTab>
            <AccordionTab header="Manual Input">
              <div className="flex justify-content-center flex-wrap">
                <MDEditor
                  id="resumeMD"
                  value={resumeMD}
                  onChange={(e) => setResumeMD(e ?? "")}
                  style={{ whiteSpace: "pre-wrap", width: "140vh" }}
                  height={600}
                  textareaProps={{
                    placeholder: `Resume Content: Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro Lorem ipsum dolor sit amet cumque nihil impedit quo minus id quod Integer tincidunt`,
                  }}
                />
              </div>
            </AccordionTab>
          </Accordion>
        </div>
        <div className="col-12 border-top-1">
          <label htmlFor="jobDescriptionFile">Job Description</label>
          <Accordion activeIndex={0}>
            <AccordionTab header="File Upload">
              <FileUpload
                resetValue={resetJob}
                setResetValue={setResetJob}
                accept="text/markdown, text/plain" // Optional: Filter file types
                onChange={(files) => handleFilesSelected(files, "job")} // Callback function
                width="60vh"
              />
            </AccordionTab>
            <AccordionTab header="Manual Input">
              <div className="flex justify-content-center flex-wrap">
                <InputTextarea
                  variant="filled"
                  value={jobText}
                  onChange={(e) =>
                    setJobText(e.target.value ? e.target.value : "")
                  }
                  rows={30}
                  cols={120}
                  placeholder={`Job Description:\n
Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?\n
At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat\n
Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc,
`}
                />
              </div>
            </AccordionTab>
          </Accordion>
        </div>
        <div className="card flex justify-content-center border-top-1 pt-2 col-12">
          <Button
            label="Generate Files"
            onClick={() => handleFormSubmit()}
            className="border-round-xl"
            icon="pi pi-copy"
            iconPos="right"
          />
        </div>
      </div>
    </Card>
  );
}
