import React, { createContext, useContext, useState, useEffect } from 'react';
import type { ReactNode } from 'react';
import { THEMES, THEME_STORAGE_KEY } from '../utils/constants';

type Theme = typeof THEMES.LIGHT | typeof THEMES.DARK;

interface ThemeContextType {
  theme: Theme;
  toggleTheme: () => void;
  isDark: boolean;
}

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

export const ThemeProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [theme, setTheme] = useState<Theme>(() => {
    const stored = localStorage.getItem(THEME_STORAGE_KEY);
    return (stored as Theme) || THEMES.LIGHT;
  });

  useEffect(() => {
    const themeLink = document.getElementById('theme-link') as HTMLLinkElement;
    const themePath = `/themes/${theme}/theme.css`;

    if (themeLink) {
      themeLink.href = themePath;
    } else {
      const link = document.createElement('link');
      link.id = 'theme-link';
      link.rel = 'stylesheet';
      link.href = themePath;
      // Handle font loading errors gracefully
      link.onerror = () => {
        console.warn(`Failed to load theme: ${themePath}`);
      };
      document.head.appendChild(link);
    }

    localStorage.setItem(THEME_STORAGE_KEY, theme);
  }, [theme]);

  const toggleTheme = () => {
    setTheme(prev => (prev === THEMES.LIGHT ? THEMES.DARK : THEMES.LIGHT));
  };

  const isDark = theme === THEMES.DARK;

  return (
    <ThemeContext.Provider value={{ theme, toggleTheme, isDark }}>{children}</ThemeContext.Provider>
  );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useThemeContext = () => {
  const context = useContext(ThemeContext);
  if (!context) {
    throw new Error('useThemeContext must be used within ThemeProvider');
  }
  return context;
};
