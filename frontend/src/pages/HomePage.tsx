import React, { useState } from 'react';
import { TabView, TabPanel } from 'primereact/tabview';
import { MainContentTab } from '../components/Tabs/MainContentTab';
import { AdditionalToolsTab } from '../components/Tabs/AdditionalToolsTab';

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
      </TabView>
    </div>
  );
};
