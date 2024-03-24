import { Box, ThemeProvider } from "@mui/material";
import { RouterProvider, createBrowserRouter } from "react-router-dom";
import { Theme1 } from "./themes/Theme1";
import "./App.css";
import LandingPage from "./components/landing";
import Home from "./components/home";

function App() {
  const router = createBrowserRouter([
    {
      path: "/",
      element: <LandingPage />,
    },
    {
      path: "/home",
      element: <Home />,
    },
  ]);

  return (
    <Box>
      <ThemeProvider theme={Theme1}>
        <RouterProvider router={router} />
      </ThemeProvider>
    </Box>
  );
}

export default App;
