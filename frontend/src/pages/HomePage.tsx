import React, { useState } from 'react';
import { TabView, TabPanel } from 'primereact/tabview';
import { MainContentTab } from '../components/Tabs/MainContentTab';
import { AdditionalToolsTab } from '../components/Tabs/AdditionalToolsTab';
import { SettingsTab } from '../components/Tabs/SettingsTab';

export const HomePage: React.FC = () => {
  const [activeIndex, setActiveIndex] = useState(0);

  return (
    <div className="w-full">
      <TabView activeIndex={activeIndex} onTabChange={e => setActiveIndex(e.index)}>
        <TabPanel header="Main Content" leftIcon="pi pi-file-edit mr-2">
          <MainContentTab />
        </TabPanel>
        <TabPanel header="Additional Tools" leftIcon="pi pi-wrench mr-2">
          <AdditionalToolsTab />
        </TabPanel>
        <TabPanel header="Settings" leftIcon="pi pi-cog mr-2">
          <SettingsTab />
        </TabPanel>
      </TabView>
    </div>
  );
};
