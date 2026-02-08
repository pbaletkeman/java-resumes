import React, { useState } from 'react';
import { TabView, TabPanel } from 'primereact/tabview';
import { MainContentTab } from '../components/Tabs/MainContentTab';
import { AdditionalToolsTab } from '../components/Tabs/AdditionalToolsTab';
import { FilesTab } from '../components/Tabs/FilesTab';
import { SettingsTab } from '../components/Tabs/SettingsTab';

export const HomePage: React.FC = () => {
  const [activeIndex, setActiveIndex] = useState(0);

  return (
    <div className="w-full">
      <TabView activeIndex={activeIndex} onTabChange={e => setActiveIndex(e.index)}>
        <TabPanel
          header={'Main Content\u00A0\u00A0\u00A0\u00A0\u00A0'}
          leftIcon="pi pi-file-edit mr-2"
        >
          <MainContentTab />
        </TabPanel>
        <TabPanel header={'Files\u00A0\u00A0\u00A0\u00A0\u00A0'} leftIcon="pi pi-folder mr-2">
          <FilesTab />
        </TabPanel>
        <TabPanel
          header={'Additional Tools\u00A0\u00A0\u00A0\u00A0\u00A0'}
          leftIcon="pi pi-wrench mr-2"
        >
          <AdditionalToolsTab />
        </TabPanel>

        <TabPanel header={'Settings\u00A0\u00A0\u00A0\u00A0\u00A0'} leftIcon="pi pi-cog mr-2">
          <SettingsTab />
        </TabPanel>
      </TabView>
    </div>
  );
};
