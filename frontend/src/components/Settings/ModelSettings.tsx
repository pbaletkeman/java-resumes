import React, { useState, useEffect } from 'react';
import { Card } from 'primereact/card';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Dialog } from 'primereact/dialog';
import { useAppContext } from '../../context/AppContext';

export interface Model {
  id: string;
  label: string;
  value: string;
}

const STORAGE_KEY = 'java-resumes-models';

const DEFAULT_MODELS: Model[] = [
  { id: '1', label: 'Gemma 3.4B (Default)', value: 'gemma-3-4b-it' },
  { id: '2', label: 'GPT-4 Turbo', value: 'gpt-4-turbo' },
  { id: '3', label: 'Claude 3 Opus', value: 'claude-3-opus' },
  { id: '4', label: 'Llama 2 Chat', value: 'llama-2-chat' },
  { id: '5', label: 'Mistral', value: 'mistral' },
];

export const ModelSettings: React.FC = () => {
  const [models, setModels] = useState<Model[]>(DEFAULT_MODELS);
  const [showDialog, setShowDialog] = useState(false);
  const [newLabel, setNewLabel] = useState('');
  const [newValue, setNewValue] = useState('');
  const { showSuccess, showError } = useAppContext();

  // Load models from storage on mount
  useEffect(() => {
    const saved = localStorage.getItem(STORAGE_KEY);
    if (saved) {
      try {
        setModels(JSON.parse(saved));
      } catch (err) {
        console.error('Failed to load saved models:', err);
      }
    }
  }, []);

  // Save models to storage whenever they change
  useEffect(() => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(models));
  }, [models]);

  const addModel = () => {
    if (!newLabel.trim() || !newValue.trim()) {
      showError('Label and value are required');
      return;
    }

    const newModel: Model = {
      id: Date.now().toString(),
      label: newLabel,
      value: newValue,
    };

    setModels([...models, newModel]);
    setNewLabel('');
    setNewValue('');
    setShowDialog(false);
    showSuccess('Model added successfully');
  };

  const deleteModel = (id: string) => {
    if (models.length === 1) {
      showError('At least one model is required');
      return;
    }
    setModels(models.filter(m => m.id !== id));
    showSuccess('Model deleted successfully');
  };

  const resetToDefaults = () => {
    setModels(DEFAULT_MODELS);
    localStorage.removeItem(STORAGE_KEY);
    showSuccess('Reset to default models');
  };

  // TODO: Implement export/import models functionality
  // const exportModels = () => {
  //   const dataStr = JSON.stringify(models, null, 2);
  //   const dataBlob = new Blob([dataStr], { type: 'application/json' });
  //   const url = URL.createObjectURL(dataBlob);
  //   const link = document.createElement('a');
  //   link.href = url;
  //   link.download = 'models.json';
  //   link.click();
  //   URL.revokeObjectURL(url);
  //   showSuccess('Models exported successfully');
  // };

  // const importModels = (e: React.ChangeEvent<HTMLInputElement>) => {
  //   const file = e.target.files?.[0];
  //   if (!file) return;

  //   const reader = new FileReader();
  //   reader.onload = (event: ProgressEvent<FileReader>) => {
  //     try {
  //       const content = event.target?.result as string;
  //       const imported = JSON.parse(content);
  //       if (Array.isArray(imported)) {
  //         setModels(imported);
  //         showSuccess('Models imported successfully');
  //       } else {
  //         showError('Invalid file format');
  //       }
  //     } catch (err) {
  //       showError('Failed to import models');
  //     }
  //   };
  //   reader.readAsText(file);
  // };

  const actionTemplate = (rowData: Model) => (
    <Button
      icon="pi pi-trash"
      rounded
      severity="danger"
      size="small"
      onClick={() => deleteModel(rowData.id)}
    />
  );

  return (
    <Card className="h-full">
      <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', width: '100%' }}>
        <div>
          <h2 className="text-2xl font-bold mb-2">AI Model Management</h2>
          <p className="text-gray-600">
            Manage the list of AI models available in the application. Non-technical users can add,
            remove, or import/export models without editing code.
          </p>
        </div>

        {/* Models Table */}
        <div style={{ width: '100%' }}>
          <h3 className="font-bold mb-3">Available Models ({models.length})</h3>
          <DataTable value={models} stripedRows responsiveLayout="scroll">
            <Column field="label" header="Display Name" />
            <Column field="value" header="Model ID" />
            <Column header="Actions" body={actionTemplate} style={{ width: '100px' }} />
          </DataTable>
        </div>

        {/* Add Model Dialog */}
        <Dialog
          visible={showDialog}
          onHide={() => setShowDialog(false)}
          header="Add New Model"
          modal
          style={{ width: '40vw', height: '5vw' }}
        >
          <div
            style={{
              display: 'flex',
              flexDirection: 'row',
              gap: '1rem',
              alignItems: 'center',
              justifyContent: 'center',
            }}
          >
            <div style={{ padding: '10px', flex: 1 }}>
              <label className="block mb-2 font-bold">Display Name *</label>
              <InputText
                value={newLabel}
                onChange={e => setNewLabel(e.target.value)}
                placeholder="e.g., GPT-5"
                className="w-full"
              />
            </div>
            <div style={{ padding: '10px', flex: 1 }}>
              <label className="block mb-2 font-bold">Model ID/Value *</label>
              <InputText
                value={newValue}
                onChange={e => setNewValue(e.target.value)}
                placeholder="e.g., gpt-5-turbo"
                className="w-full"
              />
            </div>
            <div
              style={{
                display: 'flex',
                gap: '1rem',
                padding: '10px',
                flex: 1,
              }}
            >
              <Button label="Add Model" onClick={addModel} className="flex-1" />
              <Button
                label="Cancel"
                onClick={() => setShowDialog(false)}
                severity="secondary"
                className="flex-1"
              />
            </div>
          </div>
        </Dialog>

        {/* Action Buttons */}
        <div
          style={{
            display: 'flex',
            flexDirection: 'row',
            gap: '0.5rem',
            width: '100%',
            flexWrap: 'wrap',
          }}
        >
          <Button
            label="Add Model"
            icon="pi pi-plus"
            onClick={() => setShowDialog(true)}
            style={{ flex: 1 }}
          />
          {/* <Button
            label="Export Models"
            icon="pi pi-download"
            onClick={exportModels}
            severity="info"
            style={{ flex: 1 }}
          />
          <label style={{ flex: 1 }}>
            <Button
              label="Import Models"
              icon="pi pi-upload"
              severity="warning"
              style={{ width: '100%' }}
            />
            <input type="file" accept=".json" onChange={importModels} style={{ display: 'none' }} />
          </label> */}
          <Button
            label="Reset to Defaults"
            icon="pi pi-refresh"
            onClick={resetToDefaults}
            severity="secondary"
            style={{ flex: 1 }}
          />
        </div>

        {/* Info Box */}
        <div className="bg-blue-50 border-1 border-blue-300 border-round p-4">
          <p className="text-sm text-gray-700">
            <strong>How it works:</strong>
            <div>&nbsp;</div>
          </p>
          <div
            className="text-sm text-gray-700"
            style={{ display: 'flex', flexDirection: 'column', gap: '0.25rem' }}
          >
            <div>- Models are saved to your browser&apos;s local storage</div>
            <div>- Export models to share configurations with others</div>
            <div>- Import a JSON file to quickly load a set of models</div>
            <div>- Reset to defaults to restore the original model list</div>
          </div>
        </div>
      </div>
    </Card>
  );
};
