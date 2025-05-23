import { useRef, useState } from "react";
import { Toast } from "primereact/toast";
import { FileUpload } from "primereact/fileupload";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { InputTextarea } from "primereact/inputtextarea";
import {
  InputNumber,
  type InputNumberValueChangeEvent,
} from "primereact/inputnumber";
import { Checkbox, type CheckboxChangeEvent } from "primereact/checkbox";
import { Button } from "primereact/button";
import MDEditor from "@uiw/react-md-editor";

export default function MainForm() {
  const toast = useRef<Toast>(null);
  const [model, setModel] = useState<string>("");
  const [title, setTitle] = useState<string>("");
  const [company, setCompany] = useState<string>("");
  const [temperature, setTemperature] = useState<number>(0);
  const [resumeMD, setResumeMD] = useState<string>("");
  const [jobText, setJobText] = useState<string>("");

  const onUpload = () => {
    toast?.current?.show({
      severity: "info",
      summary: "Success",
      detail: "File Uploaded",
    });
  };

  const [prompt, setPrompt] = useState<string[]>([]);

  const onPromptChange = (e: CheckboxChangeEvent) => {
    const _prompt = [...prompt];

    if (e.checked) _prompt.push(e.value);
    else _prompt.splice(_prompt.indexOf(e.value), 1);

    setPrompt(_prompt);
  };

  return (
    <Card title="Resume Optimizer">
      <div className="card flex justify-content-center">
        <label htmlFor="Company">
          Company
          <InputText
            id="company"
            value={company}
            onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
              setCompany(e.target.value)
            }
          />
        </label>
      </div>
      <div className="card flex justify-content-center">
        <label htmlFor="title">
          Job Title
          <InputText
            id="title"
            value={title}
            onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
              setTitle(e.target.value)
            }
          />
        </label>
      </div>
      <div className="card flex justify-content-center">
        <label htmlFor="model">
          Model
          <InputText
            id="model"
            value={model}
            onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
              setModel(e.target.value)
            }
          />
        </label>
      </div>
      <div className="card flex justify-content-center">
        <label htmlFor="temperature">
          Temperature
          <InputNumber
            inputId="temperature"
            value={temperature}
            onValueChange={(e: InputNumberValueChangeEvent) =>
              setTemperature(e.value ? e.value : 0)
            }
            minFractionDigits={2}
            max={2.0}
            min={0.01}
            showButtons
          />
        </label>
      </div>
      <div className="card flex flex-wrap justify-content-center gap-3">
        <label>Prompt Type</label>
        <div className="flex align-items-center">
          <Checkbox
            inputId="promptResume"
            name="promptType"
            value="Resume"
            onChange={onPromptChange}
            checked={prompt.includes("Resume")}
          />
          <label
            htmlFor="promptResume"
            className="ml-2"
          >
            Resume
          </label>
        </div>
        <div className="flex align-items-center">
          <Checkbox
            inputId="promptCover"
            name="promptType"
            value="Cover"
            onChange={onPromptChange}
            checked={prompt.includes("Cover")}
          />
          <label
            htmlFor="promptCover"
            className="ml-2"
          >
            Cover
          </label>
        </div>
      </div>
      <div className="card flex justify-content-center">
        <label htmlFor="resumeFile">
          Resume
          <Toast ref={toast}></Toast>
          <FileUpload
            id="resumeFile"
            mode="basic"
            name="resume"
            maxFileSize={1000000}
            onUpload={onUpload}
          />
        </label>
      </div>
      <div className="card flex justify-content-center">
        <label htmlFor="jobDescriptionFile">
          Job Description
          <Toast ref={toast}></Toast>
          <FileUpload
            id="jobDescriptionFile"
            mode="basic"
            name="jobDescription"
            maxFileSize={1000000}
            onUpload={onUpload}
          />
        </label>
      </div>
      <div className="card">
        <p className="pr-6">Resume</p>
        <MDEditor
          id="resumeMD"
          value={resumeMD}
          onChange={(e) => setResumeMD(e ? e : "")}
          style={{ whiteSpace: "pre-wrap", width: "140vh" }}
          height={600}
          textareaProps={{
            placeholder: `Resume Content: Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro Lorem ipsum dolor sit amet cumque nihil impedit quo minus id quod Integer tincidunt`,
          }}
        />
      </div>
      <div className="card">
        <p className="pr-6">Job Description</p>
        <div className="card flex justify-content-center">
          <InputTextarea
            value={jobText}
            onChange={(e) => setJobText(e.target.value ? e.target.value : "")}
            rows={30}
            cols={120}
            placeholder={`Job Description:\n
Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?\n
At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat\n
Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc,
`}
          />
        </div>
      </div>
      <div className="card flex justify-content-center">
        <Button label="Submit" />
      </div>
    </Card>
  );
}
