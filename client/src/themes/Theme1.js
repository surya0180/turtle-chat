import { createTheme } from "@mui/material";

export const Theme1 = createTheme({
  palette: {
    primary: {
      main: "#39FF14",
    },
    secondary: {
      main: "#199515",
    },
    background: {
      default: "#000000",
    },
    success: {
      main: "#FDBD01",
    },
    warning: {
      main: "#04d9ff",
    },
    error: {
      main: "#FF3131",
    },
    info: {
      main: "#00F9FF",
    },
  },
  typography: {
    h1: {
      fontSize: "4.125rem",
      fontFamily: "'JetBrains Mono', monospace",
      fontOpticalSizing: "auto",
      fontWeight: 600,
    },
    h6: {
      fontSize: "1.25rem",
      fontFamily: "'JetBrains Mono', monospace",
      fontOpticalSizing: "auto",
      fontWeight: 500,
    },
    body1: {
      fontSize: "1rem",
      fontFamily: "'JetBrains Mono', monospace",
      fontOpticalSizing: "auto",
      fontWeight: 400,
    },
    caption: {
      fontFamily: "'JetBrains Mono', monospace",
      fontOpticalSizing: "auto",
      fontWeight: 400,
    },
    subtitle2: {
      fontFamily: "'JetBrains Mono', monospace",
      fontOpticalSizing: "auto",
      fontWeight: 400,
      fontSize: "0.9rem",
    },
    overline: {
      fontFamily: "'JetBrains Mono', monospace",
      fontOpticalSizing: "auto",
      fontWeight: 400,
      fontSize: "0.6rem",
    },
  },
  components: {
    MuiButton: {
      styleOverrides: {
        outlined: {
          fontFamily: "'JetBrains Mono', monospace",
        },
        text: {
          fontFamily: "'JetBrains Mono', monospace",
        },
      },
    },
  },
});
