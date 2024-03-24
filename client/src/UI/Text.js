import { Typography, useTheme } from "@mui/material";
import React from "react";

function Text({ variant, text, color }) {
  const { palette } = useTheme();
  if (!color) color = "primary";
  if (!text) text = "N/A";
  if (!variant) variant = "caption";

  return (
    <Typography variant={variant} sx={{ color: palette[color].main }}>
      {text}
    </Typography>
  );
}

export default Text;
